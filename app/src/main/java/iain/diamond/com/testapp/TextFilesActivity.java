package iain.diamond.com.testapp;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TextFilesActivity extends AppCompatActivity implements View.OnClickListener {

  private static android.os.Handler handler;
  private String fullPath;
  private final static String BUNDLE_KEY = "file-text";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    File internalStorage = getFilesDir();
    String filename = "NOTE1.txt";
    fullPath = internalStorage + "/" + filename;

    final EditText editText = (EditText) findViewById(R.id.editText);
    Button saveButton = (Button) findViewById(R.id.saveButton);
    Button loadButton = (Button) findViewById(R.id.loadButton);

    saveButton.setOnClickListener(this);
    loadButton.setOnClickListener(this);

    handler = new android.os.Handler() {
      @Override
      public void handleMessage(Message msg) {
        editText.setText(msg.getData().getString(BUNDLE_KEY));
      }
    };
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.saveButton:
        saveTextFile();
        Toast.makeText(getBaseContext(), "File saved", Toast.LENGTH_LONG).show();
        break;
      case R.id.loadButton:
        loadTextFile();
        Toast.makeText(getBaseContext(), "File loaded", Toast.LENGTH_LONG).show();
        break;
    }
  }

  private void loadTextFile() {

    Runnable loadFile = new Runnable() {
      @Override
      public void run() {
        String lines = "", line = "";
        try {
          FileInputStream fis = new FileInputStream(fullPath);
          BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
          while ((line = reader.readLine()) != null) {
            lines = lines + line;
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

  private void saveTextFile() {
    Thread saveFile = new Thread(new Runnable() {
      @Override
      public void run() {
        String text = ((EditText) findViewById(R.id.editText)).getText().toString();

        try {
          FileOutputStream fos = new FileOutputStream(fullPath);
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
