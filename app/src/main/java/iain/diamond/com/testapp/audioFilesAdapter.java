package iain.diamond.com.testapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

public class audioFilesAdapter extends ArrayAdapter<String> {

  HashMap<String, Integer> audioFileMap = new HashMap<>();

  public audioFilesAdapter(Context context, int resourceId, List<String> fileList) {
    super(context, resourceId, fileList);

    for (int i = 0; i < fileList.size(); i++) {
      audioFileMap.put(fileList.get(i), i);
    }
  }

  public void addToAudioFileList(String audioFile, int value) {
    audioFileMap.put(audioFile, value);
  }

  @Override
  public long getItemId(int position) {
    String filename = getItem(position);
    return audioFileMap.get(filename);
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }
}
