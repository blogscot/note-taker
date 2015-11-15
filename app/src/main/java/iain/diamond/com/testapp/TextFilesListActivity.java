package iain.diamond.com.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class TextFilesListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

  public static final java.lang.String FILENAME_KEY = "TEXTNOTE_KEY";
  private ListView listView;
  private Button addButton;
  private NoteHandler noteHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_files_list);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Text files are stored in the application's internal storage
    noteHandler = new NoteHandler(getFilesDir());
    listView = (ListView) findViewById(R.id.textNotesListView);
    addButton = (Button) findViewById(R.id.addButton);

    List<String> textNotes = noteHandler.getTextNotes();
    ListAdapter myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
            android.R.id.text1, textNotes);
    listView.setAdapter(myAdapter);

    addButton.setOnClickListener(this);
    listView.setOnItemClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.addButton:
        startActivity(new Intent(this, TextFilesActivity.class));
        break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    String filename = noteHandler.getTextNoteFilename(position);
    Intent intent = new Intent(this, TextFilesActivity.class);
    intent.putExtra(FILENAME_KEY, filename);
    startActivity(intent);
  }
}
