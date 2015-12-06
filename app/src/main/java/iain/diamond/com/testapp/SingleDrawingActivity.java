package iain.diamond.com.testapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class SingleDrawingActivity extends AppCompatActivity {

  private ImageView imageView;
  private Note noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_drawing);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    imageView = (ImageView) findViewById(R.id.drawingImageView);
    noteHandler = new Note(getFilesDir(), "DRAW", ".drw");

    // get image position (in the Note ArrayList) from the intent extras
    Bundle extras = getIntent().getExtras();
    int position = extras.getInt(DrawingsActivity.DRAW_KEY);

    String filename = noteHandler.getMediaFilename(position);
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

  // loads a bitmap file, returns null if the path is invalid
  private Bitmap loadImage(String filename) {
    return BitmapFactory.decodeFile(filename);
  }
}
