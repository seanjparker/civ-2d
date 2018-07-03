package civ.core;

import static civ.core.instance.IData.*;
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

public class Civilization extends JPanel implements Runnable {
  private static final long serialVersionUID = -5800474980111119057L;
  
  private JFrame f;
  private JPanel p;
  private transient Game game;
  private transient MouseHandler m;
  private transient KeyboardHandler k;

  private static final String TITLE = "Civilization";
  private static final double TARGET_UPS = 60.0D;
  private static final double ONE_NANO = 1E9D;

  private boolean running = false;

  public static void main(String[] args) {
    
    String lcOSName = System.getProperty("os.name");
    boolean isMac = lcOSName.contains("OS X");
    if (isMac) {
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
    
    EventQueue.invokeLater(() -> new Civilization().start());
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
    private static final long serialVersionUID = -5681252766728523060L;
    public MapPanel() {
      setBorder(BorderFactory.createEmptyBorder());
      setBackground(Color.BLACK);
      addMouseListener(m);
      addMouseMotionListener(m);
      addMouseWheelListener(m);
    }
    
    @Override
    public Dimension getPreferredSize() {
      return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
    }
    
    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      game.draw(g2d);
    }
  }

  private void render() {
    f.repaint();
  }

  private void update() {
    game.update();
  }

  public void run() {
    long lastTime = System.nanoTime();
    long timer = System.currentTimeMillis();
    long now = 0;
    
    final double ns = ONE_NANO / TARGET_UPS;
    double delta = 0;

    while (running) {
      now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      
      while (delta >= 1) {
        update();
        
        render();
        
        delta--;
      }

      if ((System.currentTimeMillis() - timer) > 1000)
        timer += 1000;
    }
  }
}
