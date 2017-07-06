package com.proj.civ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.proj.civ.display.GUI;
import com.proj.civ.input.KeyboardHandler;
import com.proj.civ.input.MouseHandler;
import com.proj.civ.map.generation.TerrainGeneration;

@SuppressWarnings("serial")
public class Main extends JPanel implements Runnable {
	
	private JFrame f;
	private JPanel p;
	private Thread gameThread;
	private GUI gui;
	private MouseHandler m;
	private KeyboardHandler k;
	
	private final String TITLE = "Civ";
	
	private final int WIDTH = 1920; 
	private final int HEIGHT = 1080;
	private final int HEX_RADIUS = 40;
	
	private boolean running = false;
	
	public Main() {
		f = new JFrame();
		m = new MouseHandler();
		k = new KeyboardHandler();
		p = new MapPanel();
		gui = new GUI(WIDTH, HEIGHT, HEX_RADIUS, HEX_RADIUS, HEX_RADIUS);
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		m.start();
	}
	
	public synchronized void start() {
		running = true;
		gameThread = new Thread(this, "Game");
		gameThread.start();
		init();
	}
	
	public void init() {		
		f.setTitle(TITLE);
		f.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.addKeyListener(k);
		f.add(p);
		f.pack();
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	
	class MapPanel extends JPanel {
		public MapPanel() {
			setBorder(BorderFactory.createEmptyBorder());
			setBackground(Color.BLACK);
			addMouseListener(m);
			addMouseMotionListener(m);
			addMouseWheelListener(m);
		}
		public Dimension getPreferredSize() {
			return new Dimension(WIDTH, HEIGHT);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			paintComponent((Graphics2D) g);			
		}
		
		protected void paintComponent(Graphics2D g) {
			gui.drawHexGrid(g);
			gui.drawSelectedHex(g);				
		}

	}
 	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0, updates = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update(); //Update logic
				updates++;
				delta--;
				f.repaint(); //Render to the screen
				frames++;
			}
			
			if ((System.currentTimeMillis() - timer) > 1000) {
				timer += 1000;
				f.setTitle(TITLE + " - " + updates + " UPS, " + frames + " FPS");
				
				updates = 0;
				frames = 0;
			}
		}		
	}
	
	public void update() {
		if (this.k.pressedSet.size() > 0) {
			gui.updateScroll(this.k.pressedSet);
		}
	}
}
