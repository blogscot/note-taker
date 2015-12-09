package iain.diamond.com.testapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawActivity extends AppCompatActivity implements View.OnClickListener{

  private final String TAG = this.getClass().getSimpleName();

  private static Handler hander;
  private DrawingView drawView;
  private Note noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_draw);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    noteHandler = NoteFactory.getNoteFactory()
            .getNoteHandler(getFilesDir(), NoteFactory.NoteType.DrawNote);

    drawView = (DrawingView) findViewById(R.id.drawing);
    Button clearButton = (Button) findViewById(R.id.clearButton);
    Button saveButton = (Button) findViewById(R.id.saveButton);
    clearButton.setOnClickListener(this);
    saveButton.setOnClickListener(this);

    hander = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        Toast.makeText(DrawActivity.this, "Drawing saved", Toast.LENGTH_LONG).show();
        finish();
      }
    };
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
      case R.id.clearButton:
        drawView.startNew();
        break;
      case R.id.saveButton:
        drawView.setDrawingCacheEnabled(true);
        storeImage(drawView.getDrawingCache(), noteHandler.getNextNoteFilename());
        break;
    }
  }

  /**
   * Stores a image bitmap to the internal storage location specified by filename
   * This is relatively slow, so it occurs in a background thread.
   *
   * @param image     the drawing bitmap image
   * @param filename  the storage location
   */

  private void storeImage(final Bitmap image, final String filename) {

    Runnable storeInBackground = new Runnable() {
      @Override
      public void run() {

        if (filename.equals("")) {
          throw new IllegalArgumentException("storeImage Error: Invalid filename given");
        }
        File pictureFile = new File(filename);
        try {
          FileOutputStream fos = new FileOutputStream(pictureFile);
          image.compress(Bitmap.CompressFormat.PNG, 90, fos);
          fos.close();
          hander.sendEmptyMessage(0);
          noteHandler.updateNotes();
        } catch (FileNotFoundException e) {
          Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
          Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
      }
    };
    Thread thread = new Thread(storeInBackground);
    thread.start();
  }
}
