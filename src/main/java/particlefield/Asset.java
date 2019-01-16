package particlefield;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Asset {
  private String path;
  private Image image;

  public Asset(String path) {
    this.path = path;
  }

  public static Image loadImageFromPath(String imagePath) {
    ImageIcon imageIcon = new ImageIcon(imagePath);
    return imageIcon.getImage();
  }

  public void loadImage() {
    Image img = loadImageFromPath(path);
    this.image = img;
  }

  public Image getImage() {
    return image;
  }

  public String toString() {
    return String.format("%s", path);
  }
}