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

public class ImageAdaptor extends BaseAdapter {
  private Context context;
  private ArrayList<Bitmap> images = new ArrayList<>();

  public ImageAdaptor(Context context, List<String> imageFilenames) {
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
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;

    if (convertView == null) {
      // if it's not recycled, initialize some attributes
      imageView = new ImageView(context);
      imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
      imageView.setPadding(4, 4, 4, 4);
    } else {
      imageView = (ImageView) convertView;
    }
    imageView.setImageBitmap(images.get(position));
    return imageView;
  }
}
