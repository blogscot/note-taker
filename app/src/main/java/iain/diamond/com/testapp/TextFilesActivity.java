package iain.diamond.com.testapp;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TextFilesActivity extends AppCompatActivity implements View.OnClickListener {

  private static android.os.Handler handler;
  private String filename;
  private final static String BUNDLE_KEY = "file-text";
  private Note noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    noteHandler = NoteFactory.getNoteFactory()
            .getNoteHandler(getFilesDir(), NoteFactory.NoteType.TextNote);
    final EditText editText = (EditText) findViewById(R.id.editText);
    Button saveButton = (Button) findViewById(R.id.saveButton);

    saveButton.setOnClickListener(this);

    // update the view when file loading is complete
    handler = new android.os.Handler() {
      @Override
      public void handleMessage(Message msg) {
        editText.setText(msg.getData().getString(BUNDLE_KEY));
      }
    };

    // Check if user wants to open an existing note or create a new note
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      filename = bundle.getString(TextFilesListActivity.FILENAME_KEY);
      loadTextFile(filename);
    } else {
      filename = noteHandler.getNextNoteFilename();
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
      case R.id.saveButton:
        saveTextFile(filename);
        Toast.makeText(getBaseContext(), "File saved", Toast.LENGTH_LONG).show();
        break;
    }
  }

  /**
   * Loads a text from from the storage location specified by filename.
   * Loading runs in a background thread.
   *
   * @param filename  the text file storage location
   */
  private void loadTextFile(String filename) {
    final String f = filename;

    Runnable loadFile = new Runnable() {
      @Override
      public void run() {
        String lines = "", line = "";
        try {
          FileInputStream fis = new FileInputStream(f);
          BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
          while ((line = reader.readLine()) != null) {
            lines = lines + line + "\n";
          }
        } catch (IOException e) {
          e.printStackTrace();
        }

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY, lines);
        msg.setData(bundle);
        handler.sendMessage(msg);
      }
    };
    Thread load = new Thread(loadFile);
    load.start();
  }

  /**
   * Saves a text file to the storage location specified by filename.
   * Saving runs in a background thread.
   *
   * @param filename   the text file storage location.
   */
  private void saveTextFile(String filename) {
    final String f = filename;
    Thread saveFile = new Thread(new Runnable() {
      @Override
      public void run() {
        String text = ((EditText) findViewById(R.id.editText)).getText().toString();

        try {
          FileOutputStream fos = new FileOutputStream(f);
          BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
          bw.write(text);
          bw.close();
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    saveFile.start();
  }
}
