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
    return images.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;
    Bitmap bitmap = images.get(position);
    final int THUMBNAIL_SIZE = 330;

    if (convertView == null) {
      // if it's not recycled, initialize some attributes
      imageView = new ImageView(context);
//      imageView.setPadding(4, 4, 4, 4);


    } else {
      imageView = (ImageView) convertView;
    }
    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    Float width = new Float(bitmap.getWidth());
    Float height = new Float(bitmap.getHeight());
    Float ratio = width / height;
    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (THUMBNAIL_SIZE * ratio),
            THUMBNAIL_SIZE, false);

    imageView.setImageBitmap(bitmap);
    return imageView;
  }
}
