package com.proj.civ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.proj.civ.input.KeyboardHandler;
import com.proj.civ.input.MouseHandler;

@SuppressWarnings("serial")
public class Main extends JPanel implements Runnable {
	
	private JFrame f;
	private JPanel p;
	private Thread gameThread;
	private Game game;
	private MouseHandler m;
	private KeyboardHandler k;
	
	private final String TITLE = "Civilization";
	private final double FPS = 60.0;

	
	private boolean running = false;
	private int WIDTH, HEIGHT;
	private int drawablefps = 0;
	
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
		init();
		
		gameThread.start();
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
		WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width * 3 / 4;
		HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height * 3 / 4;
		int hr = ((WIDTH >> 4) + (HEIGHT >> 4)) >> 1; // Fits ~16 hexes on the screen based on the above size
		
		game = new Game(1, WIDTH, HEIGHT, hr); //players -- width -- height -- hex raduis
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
			game.draw(g);
			drawFPS(g);
		}
	}
 	
	private void render() {
		f.repaint();
	}
	private void update() {
		game.update(this.k);
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		final double ns = 1000000000.0 / FPS;
		double delta = 0;
		int fps = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
				
				render();
				fps++;
			}
			
			if ((System.currentTimeMillis() - timer) > 1000) {
				timer += 1000;
				drawablefps = fps;
				fps = 0;
			}
		}
	}
	
	private void drawFPS(Graphics2D g) {
		int textWidth = Integer.toString(drawablefps).length() * g.getFont().getSize();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, textWidth, g.getFontMetrics().getHeight());
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString(drawablefps), 0, g.getFontMetrics().getHeight());
	}
}
