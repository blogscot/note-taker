package iain.diamond.com.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
  private NoteHandler noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // image files are stored in the application's internal storage
    noteHandler = NoteFactory.getNoteFactory()
            .getNoteHandler(getFilesDir(), NoteFactory.NoteType.PhotoNote);
    Button cameraButton = (Button) findViewById(R.id.cameraButton);
    Button galleryButton = (Button) findViewById(R.id.galleryButton);
    imageView = (ImageView) findViewById(R.id.cameraImageView);

    cameraButton.setOnClickListener(this);
    galleryButton.setOnClickListener(this);

    // display the most recent photo
    String latestPhoto = noteHandler.getMostRecentNote();

    if (!latestPhoto.equals("")) {
      bitmap = loadImage(latestPhoto);
      if (bitmap != null) {
        imageView.setImageBitmap(bitmap);
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.cameraButton:
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);  // 0 Normal, 1 High quality
        startActivityForResult(intent, CAMERA_DATA);
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

        storeImage(bitmap, noteHandler.getNextNoteFilename());
        Toast.makeText(this, "Image file saved", Toast.LENGTH_LONG).show();
      }
    }
  }

  /**
   * Stores a image bitmap to the storage location specified by filename
   * @param image       the bitmap image
   * @param filename    the filename, including path
   */
  private void storeImage(Bitmap image, String filename) {

    if (filename.equals("")) {
      throw new IllegalArgumentException("storeImage Error: Invalid filename given");
    }

    File pictureFile = new File(filename);
    try {
      FileOutputStream fos = new FileOutputStream(pictureFile);
      image.compress(Bitmap.CompressFormat.PNG, 90, fos);
      fos.close();
      noteHandler.updateNotes();
    } catch (FileNotFoundException e) {
      Log.d(TAG, "File not found: " + e.getMessage());
    } catch (IOException e) {
      Log.d(TAG, "Error accessing file: " + e.getMessage());
    }
  }

  /**
   * Loads a bitmap file, returns null if the path is invalid
   * @param filename  the bitmap file location
   * @return  the bitmap image
   */
  private Bitmap loadImage(String filename) {
    return BitmapFactory.decodeFile(filename);
  }
}
