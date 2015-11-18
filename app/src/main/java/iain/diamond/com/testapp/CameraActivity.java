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
import android.widget.Toast;

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
  private NoteHandler noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // image files are stored in the application's internal storage
    internalStorage = getFilesDir();
    noteHandler = new NoteHandler(getFilesDir());
    Button cameraButton = (Button) findViewById(R.id.cameraButton);
    Button savePhotoButton = (Button) findViewById(R.id.savePhotoButton);
    Button galleryButton = (Button) findViewById(R.id.galleryButton);
    imageView = (ImageView) findViewById(R.id.cameraImageView);

    cameraButton.setOnClickListener(this);
    savePhotoButton.setOnClickListener(this);
    galleryButton.setOnClickListener(this);

    // display the first photo in list
    noteHandler.initialiseNotes(NoteFormat.Photo);
    String firstPhoto = noteHandler.getMediaFilename(NoteFormat.Photo, 0);
    Log.d("First Photo :", firstPhoto);

    if (!firstPhoto.equals("")) {
      bitmap = loadImage(firstPhoto);
      if (bitmap != null) {
        imageView.setImageBitmap(bitmap);
      }
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
      case R.id.savePhotoButton:
        if (bitmap != null) {
          String filename = noteHandler.getNextCameraNoteFilename();
          Log.d("Saving Image at: ", filename);
          storeImage(bitmap, filename);
          Toast.makeText(this, "Image file saved", Toast.LENGTH_LONG).show();
//          logImageFilenames();
        }
        break;
      case R.id.galleryButton:
        startActivity(new Intent(this, PhotosActivity.class));
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
      }
    }
  }

  // Stores a image bitmap to the full path specified by filename
  private void storeImage(Bitmap image, String filename) {

    if (filename.equals("")) {
      throw new IllegalArgumentException("storeImage Error: Invalid filename given");
    }

    File pictureFile = new File(filename);
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
    return BitmapFactory.decodeFile(filename);
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
