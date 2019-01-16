package particlefield;

import java.awt.Image;

public class Particle {
  public Image image;
  private int x, y, maxX, maxY, minX, minY;
  private double velocityX, velocityY, maxVelocityX, maxVelocityY, width;

  public Particle(Image image, int initialX, int initialY, double velocityX, double velocityY, int minX, int minY,
      int maxX, int maxY, double width) {
    this.image = image;
    this.velocityX = velocityX;
    this.velocityY = velocityY;
    this.maxVelocityX = Math.abs(velocityX * 3);
    this.maxVelocityY = Math.abs(velocityY * 5);
    this.x = initialX;
    this.y = initialY;
    this.minX = minX;
    this.minY = minY;
    this.maxX = maxX;
    this.maxY = maxY;
    this.width = width;
  }

  public void update(double varianceX, double varianceY) {
    double widthMultiplier = Math.max((width / 10.0), 1.0);
    velocityX += varianceX * widthMultiplier;
    velocityY += varianceY * widthMultiplier;
    if (velocityX > maxVelocityX) {
      velocityX = maxVelocityX;
    } else if (velocityX < -maxVelocityX) {
      velocityX = -maxVelocityX;
    }
    if (velocityY > maxVelocityY) {
      velocityY = maxVelocityY;
    } else if (velocityY < -maxVelocityY) {
      velocityY = -maxVelocityY;
    }

    x += velocityX;
    y += velocityY;
    if (x < minX) {
      x = maxX;
    } else if (x > maxX) {
      x = minX;
    }
    if (y < minY) {
      y = maxY;
    } else if (y > maxY) {
      y = minY;
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public double getWidth() {
    return width;
  }

  public void updateMaxes(int maxX, int maxY) {
    this.maxX = maxX;
    this.maxY = maxY;
  }

  public String toString() {
    return String.format("x:%s, y: %s, velocityX: %s, velocityY: %s", x, y, velocityX, velocityY);
  }
}