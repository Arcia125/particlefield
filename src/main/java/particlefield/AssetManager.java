package particlefield;

import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

class AssetManager {
  private ArrayList<Asset> assets = new ArrayList<Asset>();

  public AssetManager(String directory) {
    this(directory, null);
  }

  public AssetManager(String directory, FilenameFilter filter) {
    File folder = new File(directory);
    File[] fileNames = folder.listFiles(filter);
    for (File file : fileNames) {
      if (file.isFile()) {
        Asset asset = new Asset(file.getPath());
        asset.loadImage();
        assets.add(asset);
      }
    }
  }

  public static Image[] getImagesFromAssets(ArrayList<Asset> assetList) {
    Image[] images = new Image[assetList.size()];
    int i = 0;
    for (Asset asset : assetList) {
      images[i] = asset.getImage();
      i++;
    }
    return images;
  }

  /**
   * @return the assets
   */
  public ArrayList<Asset> getAssets() {
    return assets;
  }

  public Image[] getImages() {
    return getImagesFromAssets(assets);
  }
}