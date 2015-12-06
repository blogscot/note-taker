package iain.diamond.com.testapp;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private final String TAG = this.getClass().getSimpleName();

  Button writeButton, recordButton, drawButton, shootButton, recordings, drawingsButton;

  boolean isPlaying = false;

  private static String mediaFileName;
  private MediaRecorder mediaRecorder;
  private Note noteHandler;

  File fileStorage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Audio files are stored in internal storage
    fileStorage = getFilesDir();
    noteHandler = new Note(getFilesDir(), "VOICE", ".3gpp");

    writeButton = (Button) findViewById(R.id.buttonWrite);
    recordButton = (Button) findViewById(R.id.buttonRecord);
    drawButton = (Button) findViewById(R.id.buttonDraw);
    shootButton = (Button) findViewById(R.id.buttonShoot);

    recordings = (Button) findViewById(R.id.buttonList);
    drawingsButton = (Button) findViewById(R.id.drawingsButton);

    writeButton.setOnClickListener(this);
    recordButton.setOnClickListener(this);
    drawButton.setOnClickListener(this);
    shootButton.setOnClickListener(this);
    recordings.setOnClickListener(this);
    drawingsButton.setOnClickListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.buttonRecord:
        if (!isPlaying) {
          recordButton.setText("Stop");
          // The user may need to enable audio recording permissions
          try {
            startRecording();
          } catch (RuntimeException re) {
            Toast.makeText(this,"Please enable audio recording permissions.", Toast.LENGTH_LONG).show();
          }
        } else  {
          recordButton.setText("Record");
          stopRecording();
          Toast.makeText(this, "Audio file recorded", Toast.LENGTH_LONG).show();
        }
        isPlaying = !isPlaying;
        break;
      case R.id.buttonWrite:
        startActivity(new Intent(this, TextFilesListActivity.class));
        break;
      case R.id.buttonDraw:
        startActivity(new Intent(this, DrawActivity.class));
        break;
      case R.id.buttonShoot:
        startActivity(new Intent(this, CameraActivity.class));
        break;
      case R.id.buttonList:
        startActivity(new Intent(this, AudioFilesActivity.class));
        break;
      case R.id.drawingsButton:
        startActivity(new Intent(this, DrawingsActivity.class));
        break;
    }
  }

  private void startRecording() {
    mediaRecorder = new MediaRecorder();
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    mediaFileName = noteHandler.getNextNoteFilename();
    mediaRecorder.setOutputFile(mediaFileName);
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    try {
      mediaRecorder.prepare();
    } catch (IOException e) {
      Log.e(TAG, "prepare() failed");
    }
    mediaRecorder.start();
  }

  private void stopRecording() {
    mediaRecorder.stop();
    mediaRecorder.release();
  }

  @Override
  public void onPause() {
    super.onPause();
    if (mediaRecorder != null) {
      mediaRecorder.release();
      mediaRecorder = null;
    }
  }
}
