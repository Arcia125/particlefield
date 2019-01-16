package particlefield;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.concurrent.ThreadLocalRandom;

public class ParticleField extends JPanel implements Runnable {
  private static final long serialVersionUID = 4009583068748786536L;

  static public final String assetsDir = "src/assets/";

  // "" == all files in assetsDir, "star" == any files in assetsDir that has
  // "star" in it's file name
  static private final String fileNameSearch = "";
  static private final int DELAY = 10;
  static private final int PARTICLE_COUNT = 1000;

  private AssetManager assetManager;
  private Particle[] particles = new Particle[PARTICLE_COUNT];
  private Thread animationThread;
  private Dimension dimension;
  private Dimension screenDimension;
  private GraphicsDevice graphicsDevice;

  public ParticleField() {
    this.dimension = this.getBounds().getSize();
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent componentEvent) {
        dimension = getBounds().getSize();
        int maxX = (int) dimension.getWidth();
        int maxY = (int) dimension.getHeight();
        for (Particle currentParticle : particles) {
          currentParticle.updateMaxes(maxX, maxY);
        }
      }
    });
    this.graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    DisplayMode displayMode = this.graphicsDevice.getDisplayMode();
    this.screenDimension = new Dimension(displayMode.getWidth(), displayMode.getHeight());
    initField();
  }

  private void loadImages(String dirName, String search) {
    assetManager = new AssetManager(dirName, new FilenameFilter() {

      @Override
      public boolean accept(File dir, String name) {
        return name.contains(search);
      }
    });
  }

  private void initField() {
    setSize(dimension);
    setBackground(Color.BLACK);
    setPreferredSize(dimension);

    loadImages(assetsDir, fileNameSearch);
    Image[] images = assetManager.getImages();
    initParticles(images);
  }

  private void initParticles(Image[] images) {
    int boardWidth = (int) dimension.getWidth();
    int boardHeight = (int) dimension.getHeight();
    int marginSize = 2000;
    int minX = -marginSize;
    int minY = -marginSize;
    for (int i = 0; i < PARTICLE_COUNT; i++) {
      Image currentImage = images.length > 0 ? images[ThreadLocalRandom.current().nextInt(images.length)] : images[0];
      int randX = ThreadLocalRandom.current().nextBoolean() ? ThreadLocalRandom.current().nextInt(-marginSize, 0)
          : ThreadLocalRandom.current().nextInt(boardWidth, boardWidth + marginSize);
      int randY = ThreadLocalRandom.current().nextBoolean() ? ThreadLocalRandom.current().nextInt(-marginSize, 0)
          : ThreadLocalRandom.current().nextInt(boardHeight, boardHeight + marginSize);
      double width = currentImage.getWidth(this) == -1 ? 10
          : ThreadLocalRandom.current().nextDouble(1, currentImage.getWidth(this));
      double xVelocity = ThreadLocalRandom.current().nextDouble(-width / 4, width / 4);
      double yVelocity = ThreadLocalRandom.current().nextDouble(width / 6, width / 4);
      particles[i] = new Particle(currentImage, randX, randY, xVelocity, yVelocity, minX, minY, boardWidth, boardHeight,
          width);
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawParticles(g);
  }

  private void drawParticles(Graphics g) {
    for (Particle currentParticle : particles) {
      g.drawImage(currentParticle.image, currentParticle.getX(), currentParticle.getY(),
          (int) currentParticle.getWidth(), (int) currentParticle.getWidth(), this);
    }
    Toolkit.getDefaultToolkit().sync();
  }

  @Override
  public void addNotify() {
    super.addNotify();
    animationThread = new Thread(this);
    animationThread.start();
  }

  private void performAnimationCycle() {
    updateParticles();
  }

  private void updateParticles() {
    Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
    double screenCenterX = screenDimension.width / 2.0;
    double screenCenterY = screenDimension.height / 2.0;
    // the outer negation here causes the stars to move away from rather than
    // towards the mouse pointer
    double xChange = -((mouseLocation.x - screenCenterX) / screenCenterX);
    double yChange = -((mouseLocation.y - screenCenterY) / screenCenterY);
    for (Particle currentParticle : particles) {
      double randXChangeFactor = ThreadLocalRandom.current().nextDouble(.1, 6.5);
      double randYChangeFactor = ThreadLocalRandom.current().nextDouble(.1, 3.5);
      currentParticle.update(xChange * randXChangeFactor * .01, yChange * randYChangeFactor * .01);
    }
  }

  @Override
  public void run() {
    long startTime, endTime, timeDifference, sleepDuration;
    while (true) {
      startTime = System.currentTimeMillis();
      performAnimationCycle();
      repaint();
      endTime = System.currentTimeMillis();
      timeDifference = endTime - startTime;
      sleepDuration = DELAY - timeDifference;
      if (sleepDuration < 0) {
        sleepDuration = 2;
      }

      try {
        Thread.sleep(sleepDuration);
      } catch (InterruptedException e) {
        String message = String.format("Thread interrupted: %s", e.getMessage());
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}