package civ.core;

import static civ.core.instance.IData.WINDOW_HEIGHT;
import static civ.core.instance.IData.WINDOW_WIDTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import civ.core.input.KeyboardHandler;
import civ.core.input.MouseHandler;

public class Civilization extends JPanel implements Runnable {

  private JFrame f;
  private JPanel p;
  private Game game;
  private MouseHandler m;
  private KeyboardHandler k;

  private final static String TITLE = "Civilization";
  private final double TARGET_UPS = 60.0D;
  private final double ONE_NANO = 1E9D;

  private boolean running = false;

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
       new Civilization().start();
      }
      
    });
  }

  public Civilization() {
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
    f.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
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
      return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      BufferedImage bufferedImage = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = bufferedImage.createGraphics();
      
      game.draw(g2d);

      Graphics2D g2dComponent = (Graphics2D) g;
      g2dComponent.drawImage(bufferedImage, null, 0, 0);  
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

    final double ns = ONE_NANO / TARGET_UPS;
    double delta = 0;
    int fps = 0;

    while (running) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      
      while (delta >= 1) {
        update();
        
        render();
        fps++;
        
        delta--;
      }

      if ((System.currentTimeMillis() - timer) > 1000) {
        timer += 1000;
        fps = 0;
      }
    }
  }
}
