package iain.diamond.com.testapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * NotesAdapter provides a simple custom list adaptor
 */

public class NotesAdapter extends ArrayAdapter<String> {

  HashMap<String, Integer> notesMap = new HashMap<>();

  public NotesAdapter(Context context, int resourceId, List<String> fileList) {
    super(context, resourceId, fileList);

    for (int i = 0; i < fileList.size(); i++) {
      notesMap.put(fileList.get(i), i);
    }
  }

  @Override
  public long getItemId(int position) {
    String filename = getItem(position);
    return notesMap.get(filename);
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }
}
