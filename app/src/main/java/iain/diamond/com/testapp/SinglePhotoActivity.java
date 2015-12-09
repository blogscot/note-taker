package iain.diamond.com.testapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

public class SinglePhotoActivity extends AppCompatActivity {

  private ImageView imageView;
  private Note noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_photo);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    imageView = (ImageView) findViewById(R.id.photoImageView);
    noteHandler = new Note(getFilesDir(), "IMAGE", ".png");

    // get image position (in the Note ArrayList) from the intent extras
    Bundle extras = getIntent().getExtras();
    int position = extras.getInt(PhotosActivity.PHOTO_KEY);

    String filename = noteHandler.getNoteFilename(position);
    Bitmap bitmap = loadImage(filename);
    if (bitmap != null) {
      imageView.setImageBitmap(bitmap);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  /**
   * Loads a bitmap file, returns null if the path is invalid
   * @param filename  the photo storage location
   * @return          the photo bitmap
   */
  private Bitmap loadImage(String filename) {
    return BitmapFactory.decodeFile(filename);
  }
}
