package civ.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import civ.core.input.KeyboardHandler;
import civ.core.input.MouseHandler;
import civ.core.instance.IData;

@SuppressWarnings("serial")
public class Civilisation extends JPanel implements Runnable {

  private JFrame f;
  private JPanel p;
  private Game game;
  private MouseHandler m;
  private KeyboardHandler k;

  private final static String TITLE = "Civilisation";
  private final double FPS = 60.0;

  private boolean running = false;
  private int drawablefps = 0;

  public static void main(String[] args) {
    
    String lcOSName = System.getProperty("os.name");
    boolean IS_MAC = lcOSName.contains("OS X");
    if (IS_MAC) {
      //place menu items on the mac toolbar
      System.setProperty("apple.laf.useScreenMenuBar", "true");

      //use smoother fonts
      System.setProperty("apple.awt.textantialiasing", "true");

      //ref: http://developer.apple.com/releasenotes/Java/Java142RNTiger/1_NewFeatures/chapter_2_section_3.html
      System.setProperty("apple.awt.graphics.EnableQ2DX","true");
    }

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException  | IllegalAccessException | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    
    
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
       new Civilisation().start();
      }
      
    });
  }
  
  public Civilisation() {
    f = new JFrame();
    m = new MouseHandler();
    k = new KeyboardHandler();
    p = new MapPanel();
  }

  public synchronized void start() {
    running = true;

    init();

    new Thread(this, "Game").start();
  }

  private void init() {

    game = new Game(1);

    f.setTitle(TITLE);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setResizable(false);
    f.addKeyListener(k);
    f.setPreferredSize(new Dimension(IData.WIDTH, IData.HEIGHT));
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
      return new Dimension(IData.WIDTH, IData.HEIGHT);
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      paintComponent((Graphics2D) g);
    }

    protected void paintComponent(Graphics2D g) {
      game.draw(g);
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
}
