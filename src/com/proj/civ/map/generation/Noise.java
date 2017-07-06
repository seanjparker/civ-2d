package com.proj.civ.map.generation;

public class Noise {
	// Hash lookup table as defined by Ken Perlin.  This is a randomly arranged array of all numbers from 0-255 inclusive.
	private final int[] permutation = { 151,160,137,91,90,15,					
			131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
			190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
			88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
			77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
			102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
			135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
			5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
			223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
			129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
			251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
			49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
			138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
		};
	
	private int[] p;
	
	public Noise() {
		p = new int[512];
		for (int x = 0; x < 512; x++) {
			p[x] = permutation[x % 256];
		}
	}
	
	public double moistureNoise(double x, double y, int octaves, double persistance, double seed) {
		double total = 0;
		double freq = 1;
		double amp = 1;
		double max = 0;
		
		double z = freq;
		
		for (int i = 0; i < octaves; i++) {
			total += noise(x * freq * seed, y * freq, z) * amp;
			
			max += amp;
			amp *= persistance;
			freq *= 2;
		}
		
		return total / max;
	}
	
	public double elevationNoise(double x, double y, int octaves, double persistance, double seed, double peakElevation) {
		double total = 0;
		double freq = 1;
		double amp = 1;
		double max = 0;
		
		double z = seed * freq;
		
		for (int i = 0; i < octaves; i++) {
			total += noise(x * freq * seed, y * freq * seed, z) * amp;
			
			max += amp;
			amp *= persistance;
			freq *= 2;
		}
		
		double noise = total / max;
		
		//peakElevation = 5.0D
		double e = Math.pow(noise, peakElevation);
	
		// if e < (0 -> 1) : 0 [no water] -> 1 [really high water]
		//	return water
		//else
		//	return land
		
		//create 2nd noise map (moisture) [m]
		//low elevations are oceans/beaches
		//high elevations are rocky/snowy
		
		//if e < 0.1 return ocean
		//if e < 0.12 return beach
		
		//if e > 0.8
		//	if m < 0.1 return desert1 (no moisture)
		// 	if m < 0.2 return desert2 (increased moisture)
		//	if m < 0.5 return tundra
		//	return snow (high elevation + high moisture)
		
		//if e > 0.6
		//	if m < 0.33 return temperate desert
		//	if m < 0.66 return plains
		//	return taiga
		
		//if e > 0.3
		//	if m < 0.16 return plains
		//	if m < 0.50 return grassland
		//  if m < 0.83 return forest
		//	return rain forest
		
		//if m < 0.16 return subtropical desert
		//if m < 0.33 return grassland
		//if m < 0.66 return tropical forest
		//return tropical rain forest
		
		
		
		return e;
	}

	public double octaveNoise(double x, double y, int octaves, double persistance, double seed) {
		double total = 0;
		double freq = 1;
		double amp = 1;
		double max = 0;
		
		double z = seed * freq;
		
		for (int i = 0; i < octaves; i++) {
			total += noise(x * freq, y * freq, z) * amp;
			
			max += amp;
			amp *= persistance;
			freq *= 2;
		}
		
		return total / max;
	}

	private double noise(double x, double y, double z) {
		int xi = (int) x & 255;
		int yi = (int) y & 255;
		int zi = (int) z & 255;
		
		double xf = x - (int) x;
		double yf = y - (int) y;
		double zf = z - (int) z;
		
		double u = fade(xf);
		double v = fade(yf);
		double w = fade(zf);
		
		int aaa, aba, aab, abb, baa, bba, bab, bbb;
		aaa = p[p[p[	xi ]+ 	  	yi ]+ 		zi ];
		aba = p[p[p[	xi ]+ 	inc(yi)]+ 		zi ];
		aab = p[p[p[	xi ]+ 		yi ]+ 	inc(zi)];
		abb = p[p[p[	xi ]+ 	inc(yi)]+ 	inc(zi)];
		baa = p[p[p[inc(xi)]+ 		yi ]+ 		zi ];
		bba = p[p[p[inc(xi)]+	inc(yi)]+ 		zi ];
		bab = p[p[p[inc(xi)]+ 		yi ]+ 	inc(zi)];
		bbb = p[p[p[inc(xi)]+ 	inc(yi)]+ 	inc(zi)];
		
		double x1, x2, y1, y2;
		
		x1 = lerp(	grad (aaa, xf  , yf  , zf),
					grad (baa, xf - 1, yf  , zf), u);	
		
		x2 = lerp(	grad (aba, xf  , yf - 1, zf),				
					grad (bba, xf - 1, yf - 1, zf), u);
		
		y1 = lerp(x1, x2, v);

		x1 = lerp(	grad (aab, xf  , yf  , zf - 1),
					grad (bab, xf - 1, yf  , zf - 1), u);
		
		x2 = lerp(	grad (abb, xf  , yf - 1, zf - 1),
	          		grad (bbb, xf - 1, yf - 1, zf - 1), u);
		
		y2 = lerp (x1, x2, v);
	
		return (lerp(y1, y2, w) + 1) / 2;	
	}
	
	private int inc(int n) {
		n++;
		return n;
	}
	
	private double grad(int hash, double x, double y, double z) {
		switch (hash & 0xF) {
		case 0x0: return  x + y;
		case 0x1: return -x + y;
		case 0x2: return  x - y;
		case 0x3: return -x - y;
		case 0x4: return  x + z;
		case 0x5: return -x + z;
		case 0x6: return  x - z;
		case 0x7: return -x - z;
		case 0x8: return  y + z;
		case 0x9: return -y + z;
		case 0xA: return  y - z;
		case 0xB: return -y - z;
		case 0xC: return  y + x;
		case 0xD: return -y + z;
		case 0xE: return  y - x;
		case 0xF: return -y - z;
		default: return 0;
		}
	}
	
	private double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
	private double lerp(double a, double b, double x) {
		return a + x * (b - a);
	}
}
