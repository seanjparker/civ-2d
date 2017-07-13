package com.proj.civ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.proj.civ.display.GUI;
import com.proj.civ.input.KeyboardHandler;
import com.proj.civ.input.MouseHandler;

@SuppressWarnings("serial")
public class Main extends JPanel implements Runnable {
	
	private JFrame f;
	private JPanel p;
	private Thread gameThread;
	private GUI gui;
	private MouseHandler m;
	private KeyboardHandler k;
	
	private final String TITLE = "Civilization";
	private final double FPS = 60.0;
	
	private int WIDTH = 1920; //Default
	private int HEIGHT = 1080; //Default
	private int HEX_RADIUS = 40; //Default
	
	private boolean running = false;
	
	public Main() {
		f = new JFrame();
		m = new MouseHandler();
		k = new KeyboardHandler();
		p = new MapPanel();
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
	
	private void init() {	
		createAndSetupGUI();
		
		f.setTitle(TITLE);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.addKeyListener(k);
		f.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		f.add(p);
		f.pack();
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private void createAndSetupGUI() {
		int w = Toolkit.getDefaultToolkit().getScreenSize().width * 3 / 4;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height * 3 / 4;
		this.WIDTH = w;
		this.HEIGHT = h;
		this.HEX_RADIUS = ((w >> 4) + (h >> 4)) >> 1; // Fits ~16 hexes on the screen based on the above size
		gui = new GUI(w, h, HEX_RADIUS, HEX_RADIUS, HEX_RADIUS);
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
			gui.drawHexInspect(g);
			gui.drawPath(g);
			gui.drawFocusHex(g);
		}
	}
 	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		final double ns = 1000000000.0 / FPS;
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
			gui.updateKeys(this.k.pressedSet);
		}
	}
}
