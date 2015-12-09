package iain.diamond.com.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * ImageAdapter provides an grid view adapter
 */

public class ImageAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<Bitmap> images = new ArrayList<>();

  public ImageAdapter(Context context, List<String> imageFilenames) {
    this.context = context;
    loadImages(imageFilenames);
  }

  // Read in the bitmap images using the provided filenames
  private void loadImages(List<String> imageFilenames) {
    for (String filename : imageFilenames) {
      images.add(BitmapFactory.decodeFile(filename));
    }
  }

  @Override
  public int getCount() {
    return images.size();
  }

  @Override
  public Object getItem(int position) {
    return images.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;
    Bitmap imageBitmap = images.get(position);

    // let's cycle memory if possible
    if (convertView == null) {
      imageView = new ImageView(context);
    } else {
      imageView = (ImageView) convertView;
    }
    // Scale the images up
    final int THUMBNAIL_SIZE = 330;
    Float ratio = (float)imageBitmap.getWidth() / imageBitmap.getHeight();

    // draw new image into recycled view
    imageView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, (int)(THUMBNAIL_SIZE * ratio),
            THUMBNAIL_SIZE, false));
    return imageView;
  }
}
