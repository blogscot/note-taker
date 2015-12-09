package iain.diamond.com.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class TextFilesListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

  public static final java.lang.String FILENAME_KEY = "TEXTNOTE_KEY";
  private ListView listView;
  private Button addButton;
  private Note noteHandler;
  private NotesAdapter myAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_files_list);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Text files are stored in the application's internal storage
    noteHandler = new Note(getFilesDir(), "NOTE", ".txt");
    listView = (ListView) findViewById(R.id.textNotesListView);
    addButton = (Button) findViewById(R.id.addButton);

    List<String> textNotes = noteHandler.getNoteFilenames();
    myAdapter = new NotesAdapter(this, android.R.layout.simple_list_item_1, textNotes);
    listView.setAdapter(myAdapter);

    addButton.setOnClickListener(this);
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
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.addButton:
        startActivity(new Intent(this, TextFilesActivity.class));
        break;
    }
  }

  /**
   * Display the selected text file in its own view.
   * @param position    the text file list position clicked
   */
  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    String filename = noteHandler.getNoteFilename(position);
    Intent intent = new Intent(this, TextFilesActivity.class);
    intent.putExtra(FILENAME_KEY, filename);
    startActivity(intent);
  }

  /**
   * Removed a text file from the notes list.
   *
   * @param position    the text file list position clicked
   * @return  true - the action is finished
   */
  @Override
  public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
    noteHandler.removeNoteFromList(position);
    myAdapter.notifyDataSetChanged();
    return true;
  }
}
