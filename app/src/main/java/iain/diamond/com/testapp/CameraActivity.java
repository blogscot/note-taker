package iain.diamond.com.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

  private final String TAG = this.getClass().getSimpleName();

  private final int CAMERA_DATA = 343;
  private ImageView imageView;
  private Bitmap bitmap;
  private File internalStorage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // image files are stored in the application's internal storage
    internalStorage = getFilesDir();
    Button cameraButton = (Button) findViewById(R.id.cameraButton);
    imageView = (ImageView) findViewById(R.id.cameraImageView);
    cameraButton.setOnClickListener(this);

    bitmap = loadImage("/BITMAP1.png");
    if (bitmap != null) {
      imageView.setImageBitmap(bitmap);
    }
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.cameraButton:
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);  // 0 Normal, 1 High quality
        startActivityForResult(intent, CAMERA_DATA);
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == CAMERA_DATA) {
      if (resultCode == RESULT_OK) {
        Bundle extras = data.getExtras();
        bitmap = (Bitmap) extras.get("data");
        imageView.setImageBitmap(bitmap);

        storeImage(bitmap, "/BITMAP1.png");
        logImageFilenames();  // some debugging code
      }
    }
  }

  private void storeImage(Bitmap image, String filename) {

    if (filename.equals("")) {
      throw new IllegalArgumentException("storeImage Error: Invalid filename given");
    }

    File pictureFile = new File(internalStorage + filename);
    try {
      FileOutputStream fos = new FileOutputStream(pictureFile);
      image.compress(Bitmap.CompressFormat.PNG, 90, fos);
      fos.close();
    } catch (FileNotFoundException e) {
      Log.d(TAG, "File not found: " + e.getMessage());
    } catch (IOException e) {
      Log.d(TAG, "Error accessing file: " + e.getMessage());
    }
  }

  // loads a bitmap file, returns null if the path is invalid
  private Bitmap loadImage(String filename) {
    return BitmapFactory.decodeFile(internalStorage + filename);
  }

  // for debugging only
  private void logImageFilenames() {
    File[] files = internalStorage.listFiles();

    for (File f : files) {
      String filename = f.getName();
      Log.d("logImageFilenames: ", filename);
    }
  }
}
