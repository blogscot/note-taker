package iain.diamond.com.testapp;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteHandler {

  private final String TAG = this.getClass().getSimpleName();

  private File internalStorage;
  private String fullPath;
  private List<String> audioFilenames = new ArrayList<>();

  // The Note Handler is initialised with the internal storage location
  // accessible by the current activity.
  public NoteHandler(File directory) {
    internalStorage = directory;

    if (directory == null) {
      throw new IllegalArgumentException("Invalid directory specified.");
    }
    fullPath = directory.getAbsolutePath();
  }

  // Generates the next audio filename according to the current
  // list size. The filename are suffixed 01, 02, etc.
  public String getNextAudioFilename() {
    return fullPath + "/" + "VOICE" + getNextFileSuffix() + ".3gp";
  }

  // Returns the full path audio filename at the given index
  // Index numbers range 1, 2, 3 ...
  public String getMediaFilename(int index) {

    if (index < 0 || index > audioFilenames.size())
    {
      // If index out of bound return first entry
      index = 0;
      Log.e(TAG, "Audio File index out of bounds");
    }

    return fullPath + "/" + audioFilenames.get(index);
  }

  // TODO: This method doesn't take into account when intermediate sequence numbered
  // files are deleted. ie. 1, 2, 5.
  private String getNextFileSuffix() {

    // get an up to date list of audio files
    // before calculating the correct list size
    loadAudioFilenames();

    int size = audioFilenames.size() + 1; // first file suffix is '1'
    String result = "" + size;
    return pad2Digits(result);
  }

  String pad2Digits(String s) {
    if (s.length() < 2) {
      return "0" + s;
    }
    return s;
  }

  // Updates and returns a reference to the audio file list
  public final List<String> getAudioFilenames() {
    loadAudioFilenames();
    return audioFilenames;
  }

  // Returns a list of audio filenames
  private void loadAudioFilenames() {
    File[] files = internalStorage.listFiles();

    audioFilenames.clear();
    for (File f : files) {
      String filename = f.getName();
      if (filename.endsWith(".3gp")) {
        audioFilenames.add(filename);
      }
    }
   }

  public void removeFileFromList(int position) {
    String mf = getMediaFilename(position);
    Log.d("removeFileFromList", mf);

    File audioFile = new File(getMediaFilename(position));
    audioFile.delete();
    audioFilenames.remove(position);
//    adapter.notifyDataSetChanged();
  }
}
