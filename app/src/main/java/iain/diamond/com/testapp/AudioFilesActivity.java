package iain.diamond.com.testapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

public class AudioFilesActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, MediaPlayer.OnCompletionListener, AdapterView.OnItemLongClickListener {

  private final String TAG = this.getClass().getSimpleName();

  private ListView listView;
  private static String mediaFilename;
  private MediaPlayer mediaPlayer;

  notesAdapter myAdapter;
  NoteHandler noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_audio_files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Audio files are stored in application internal storage
    noteHandler = new NoteHandler(getFilesDir());
    listView = (ListView) findViewById(R.id.listView);

    List<String> audioFilenames = noteHandler.getAudioFilenames();
    myAdapter = new notesAdapter(this, android.R.layout.simple_list_item_1, audioFilenames);
    listView.setAdapter(myAdapter);

    listView.setOnItemClickListener(this);
    listView.setOnItemLongClickListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    Log.d(TAG, noteHandler.getMediaFilename(position));
    startPlaying(position);
  }

  private void startPlaying(int index) {
    try {
      mediaPlayer = new MediaPlayer();
      mediaFilename = noteHandler.getMediaFilename(index);
      mediaPlayer.setDataSource(mediaFilename);
      mediaPlayer.setOnCompletionListener(this);

      mediaPlayer.prepare();
      mediaPlayer.start();
    } catch (IOException e) {
      Log.e(TAG, "prepare() failed");
    }
  }

  private void stopPlaying() {
    mediaPlayer.stop();
    mediaPlayer.release();
  }

  @Override
  public void onCompletion(MediaPlayer mediaPlayer) {
    Log.d(TAG, "Media Player has finished.");
    mediaPlayer.stop();
    mediaPlayer.release();
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
    noteHandler.removeAudioFileFromList(position);
    myAdapter.notifyDataSetChanged();
    return true;
  }
}
