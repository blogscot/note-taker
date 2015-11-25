package iain.diamond.com.testapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Note {

  private File internalStorage;
  protected String fullPath;
  protected String extension;
  protected String prefix;
  protected String pattern;
  protected List<String> notes = new ArrayList<>();

  // The Note Handler is initialised with the internal storage location
  // accessible by the current activity.
  // Files are saved using the pattern prefix + { index } + extension
  public Note(File directory, String prefix, String extension) {
    internalStorage = directory;

    if (directory == null) {
      throw new IllegalArgumentException("Invalid directory specified.");
    }
    this.prefix = prefix;
    this.extension = extension;
    this.pattern = "^" + prefix +"(\\d{2}).*";
    fullPath = directory.getAbsolutePath();
  }

  public void initialiseNotes() {
    updateNotes();
  }

  // Generates the next note filename according to the current
  // list size. The filename are suffixed 01, 02, etc.
  public String getNextNoteFilename() {
    return fullPath + "/" + prefix + getNextNoteIndex() + extension;
  }

  // Adds a leading 0 if number is only one digit
  protected String pad2Digits(String s) {
    if (s.length() < 2) {
      return "0" + s;
    }
    return s;
  }

  // Updates and returns a reference to the note array list
  protected List<String> getNoteFilenames() {
    updateNotes();
    reverseSort(notes);
    return notes;
  }

  public List<String> getFullPathNoteFilenames() {
    List<String> fullPathNotes = new ArrayList<>();
    for (String note : notes) {
      fullPathNotes.add(fullPath + "/" + note);
    }
    return fullPathNotes;
  }

  // Returns the full path note filename at the given index
  // Or an empty string if the index parameters are invalid
  public String getMediaFilename(int index) {
    if (index < notes.size() && index >= 0) {
      return fullPath + "/" + notes.get(index);
    }
    return "";
  }

  // Returns the last note in the array list
  public String getLastNoteFilename() {
    return getMediaFilename(notes.size() - 1);
  }

  // Returns the next available note index.
  protected String getNextNoteIndex() {
    updateNotes();
    return pad2Digits("" + (findMaxNoteIndex() + 1));
  }

  // Searches through the current notes array list
  // Returns the maximum index found or 0 if the list is empty.
  protected int findMaxNoteIndex() {
    ArrayList<Integer> suffixes = new ArrayList<>();
    Pattern r;

    r = Pattern.compile(pattern);
    for (String s : notes) {
      Matcher m = r.matcher(s);
      if (m.find()) {
        suffixes.add(Integer.parseInt(m.group(1)));
      }
    }
    return suffixes.isEmpty() ? 0 : Collections.max(suffixes);
  }

  // Takes a reference to an unsorted list
  // returns the same list sorted then reversed
  protected List<String> reverseSort(List<String> list) {
    Collections.sort(list);
    Collections.reverse(list);
    return list;
  }

  // Updates the note array list filenames
  protected void updateNotes() {
    notes.clear();
    File[] files = internalStorage.listFiles();
    for (File f : files) {
      String filename = f.getName();
      if (filename.endsWith(extension)) {
        notes.add(filename);
      }
    }
  }

  // Removes the note at the specified position both from
  // the app's internal storage and also the note array list
  public void removeNoteFromList(int position) {
    File filename = new File(getMediaFilename(position));
    if (filename.delete()) {
      notes.remove(position);
    }
  }
}
