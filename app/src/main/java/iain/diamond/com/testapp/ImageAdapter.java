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
 * ImageAdapter is a custom grid view adapter
 */

public class ImageAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<String> images;

  public ImageAdapter(Context context, List<String> imageFilenames) {
    this.context = context;
    images = new ArrayList<>(imageFilenames);
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
    Bitmap imageBitmap = BitmapFactory.decodeFile(images.get(position));

    // let's recycle memory if possible
    if (convertView == null) {
      imageView = new ImageView(context);
    } else {
      imageView = (ImageView) convertView;
    }
    // Scale the images up
    final int THUMBNAIL_SIZE = 330;
    Float ratio = (float)imageBitmap.getWidth() / imageBitmap.getHeight();
    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) (THUMBNAIL_SIZE * ratio),
            THUMBNAIL_SIZE, false);

    // draw thumbnail image into view
    imageView.setImageBitmap(imageBitmap);
    return imageView;
  }
}
