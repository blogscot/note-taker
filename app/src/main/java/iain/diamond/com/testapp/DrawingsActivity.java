package iain.diamond.com.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

public class DrawingsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

  public final static String DRAW_KEY = "draw-key";
  private GridView gridView;
  private Note noteHandler;
  private ImageAdapter myAdapter;
  private static List<String> drawNotes;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_drawings);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Text files are stored in the application's internal storage
    noteHandler = NoteFactory.getNoteFactory()
            .getNoteHandler(getFilesDir(), NoteFactory.NoteType.DrawNote);
    gridView = (GridView) findViewById(R.id.drawingsGridView);

    drawNotes = noteHandler.getFullPathNoteFilenames();
    myAdapter = new ImageAdapter(this, drawNotes);
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
   * Displays the drawing bitmap image in a new activity.
   * @param position      the drawing image position clicked by the user
   */
  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    Intent intent = new Intent(this, SingleDrawingActivity.class);
    intent.putExtra(DRAW_KEY, position);
    startActivity(intent);
  }

  /**
   * Remove a image item from the grid view at the position the user clicked.
   *
   * @param position     the image position clicked by the user
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
