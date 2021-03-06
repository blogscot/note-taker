package iain.diamond.com.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

  public final static String PHOTO_KEY = "photo-key";
  private GridView gridView;
  private NoteHandler noteHandler;
  private ImageAdapter myAdapter;
  private static List<String> photoNotes;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photos);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Text files are stored in the application's internal storage
    noteHandler = NoteFactory.getNoteFactory()
            .getNoteHandler(getFilesDir(), NoteFactory.NoteType.PhotoNote);
    gridView = (GridView) findViewById(R.id.gridView);

    photoNotes = noteHandler.getFullPathNoteFilenames();
    myAdapter = new ImageAdapter(this, photoNotes);
    gridView.setAdapter(myAdapter);
    gridView.setOnItemClickListener(this);
    gridView.setOnItemLongClickListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  /**
   * Displays the clicked photo in a new activity.
   *
   * @param position    the photo position clicked by the user.
   */
  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    Intent intent = new Intent(this, SinglePhotoActivity.class);
    intent.putExtra(PHOTO_KEY, position);
    startActivity(intent);
  }

  /**
   * Removes a photo from the note list.
   *
   * @param position    the photo position clicked by the user.
   * @return  true, the action has been handled
   */
  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
    noteHandler.removeNoteFromList(position);
    // Force the gridView to redraw
    myAdapter.updateImageList(noteHandler.getFullPathNoteFilenames());
    myAdapter.notifyDataSetChanged();
    return true;
  }
}
