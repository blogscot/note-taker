package iain.diamond.com.testapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.GridView;

import java.util.List;

public class PhotosActivity extends AppCompatActivity {

  private GridView gridView;
  private Note noteHandler;
  private notesAdapter myAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photos);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Text files are stored in the application's internal storage
    noteHandler = new Note(getFilesDir(), "IMAGE", ".png");
//    listView = (ListView) findViewById(R.id.photosListView);
    gridView = (GridView) findViewById(R.id.gridView);

    noteHandler.initialiseNotes();
    List<String> photoNotes = noteHandler.getFullPathNoteFilenames();
    gridView.setAdapter(new ImageAdaptor(this, photoNotes));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

//  @Override
//  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
//    noteHandler.removeNoteFromList(position);
//    myAdapter.notifyDataSetChanged();
//    return true;
//  }
}
