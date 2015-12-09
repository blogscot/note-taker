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

  private NotesAdapter myAdapter;
  private Note noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_audio_files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Audio files are stored in application internal storage
    noteHandler = NoteFactory.getNoteFactory()
            .getNoteHandler(getFilesDir(), NoteFactory.NoteType.AudioNote);
    listView = (ListView) findViewById(R.id.listView);

    List<String> audioFilenames = noteHandler.getNoteFilenames();
    myAdapter = new NotesAdapter(this, android.R.layout.simple_list_item_1, audioFilenames);
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
    startPlaying(position);
  }

  /**
   * Starts playing the audio file at the given index position.
   * @param index   the position clicked by the user
   */
  private void startPlaying(int index) {
    try {
      mediaPlayer = new MediaPlayer();
      mediaFilename = noteHandler.getNoteFilename(index);
      mediaPlayer.setDataSource(mediaFilename);
      mediaPlayer.setOnCompletionListener(this);

      mediaPlayer.prepare();
      mediaPlayer.start();
    } catch (IOException e) {
      Log.e(TAG, "startPlaying failed");
    }
  }

  /**
   * Stops playing an audio file if the activity is paused.
   */
  @Override
  public void onPause() {
    super.onPause();
    if (mediaPlayer != null) {
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

  /**
   * Stops playing an audio file.
   */
  private void stopPlaying() {
    mediaPlayer.stop();
    mediaPlayer.release();
  }

  /**
   * Tidies up resources when the audio playback is finished.
   * @param mediaPlayer
   */
  @Override
  public void onCompletion(MediaPlayer mediaPlayer) {
    mediaPlayer.stop();
    mediaPlayer.release();
  }

  /**
   * Removes a note at the position clicked by the user.
   *
   * @param adapterView   not used
   * @param view          not used
   * @param position      the item clicked
   * @param l             not used
   * @return
   */
  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
    noteHandler.removeNoteFromList(position);
    myAdapter.notifyDataSetChanged();
    return true;
  }
}
