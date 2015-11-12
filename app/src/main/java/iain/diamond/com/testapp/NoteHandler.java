package iain.diamond.com.testapp;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  // Returns the next suffix in the sequence 01, 02, 03 etc.
  private String getNextFileSuffix() {

    // get an up to date list of audio files
    // before calculating the correct list size
    loadAudioFilenames();
    return pad2Digits(""+(findMaxSuffix(audioFilenames) + 1));
  }

  // Searches through a list of strings, pulling out the suffix numbers
  // Returns the maximum value found or 0 if the list is empty.
  private int findMaxSuffix(List<String> list) {
    ArrayList<Integer> suffices = new ArrayList<>();
    String pattern = "^VOICE(\\d{2}).*";
    Pattern r = Pattern.compile(pattern);

    for (String s : list) {
      Matcher m = r.matcher(s);
      if (m.find()) {
        suffices.add(Integer.parseInt(m.group(1)));
      }
    }
    return suffices.isEmpty() ? 0 : Collections.max(suffices);
  }

  // Adds a leading 0 if number is only one digit
  String pad2Digits(String s) {
    if (s.length() < 2) {
      return "0" + s;
    }
    return s;
  }

  // Updates and returns a reference to the audio file list
  public final List<String> getAudioFilenames() {
    loadAudioFilenames();
    // The last added audio file appears at the top of the list
    reverseSort(audioFilenames);
    return audioFilenames;
  }

  // Takes a reference to an unsorted list
  // returns the same list sorted then reversed
  private List<String> reverseSort(List<String> list) {
    Collections.sort(list);
    Collections.reverse(list);
    return list;
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
    if (audioFile.delete()) {
      audioFilenames.remove(position);
    }
  }
}
