package iain.diamond.com.testapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Note class is used as an abstraction layer over the Android file system.
 * It provides methods to:
 *    generate new filenames in a sequence Item1, Item2, etc. for the parent activity
 *    return a list of filenames, with or without the full path
 *    return individual filenames within the list, based on position
 *    remove individual filenames from the list, based on position
 *
 *    Note, all files are stored in internal storage
 */

public class Note {

  private File internalStorage;
  private String fullPath;
  private String extension;
  private String prefix;
  private String pattern;
  private List<String> notes = new ArrayList<>();
  private List<String> fullPathNotes = new ArrayList<>();

  /**
   * The Note Handler is initialised with the internal storage location
   * accessible by the current activity.
   * Files are saved using the pattern prefix + { index } + extension
   *
   * @param directory   the internal storage path
   * @param prefix      the standard file prefix    (e.g. "TEXT")
   * @param extension   the standard file extension (e.g. ".txt")
   */

  public Note(File directory, String prefix, String extension) {
    internalStorage = directory;

    if (directory == null) {
      throw new IllegalArgumentException("Invalid directory specified.");
    }
    this.prefix = prefix;
    this.extension = extension;
    this.pattern = "^" + prefix +"(\\d{2}).*";
    fullPath = directory.getAbsolutePath();

    // initialise the note list with the current directory contents
    initialiseNotes();
  }

  /**
   * Initialises the filename array lists
   */
  private void initialiseNotes() {
    updateNotes();
    buildFullPathNoteFilenames();
  }

  /**
   * Generates the next note filename according to the current
   * list size. The filename are suffixed 01, 02, etc.
   *
   * @return the note filename, including path
   */

  public String getNextNoteFilename() {
    return fullPath + "/" + prefix + getNextNoteIndex() + extension;
  }

  /**
   * Adds a leading 0 if number is only one digit
   *
   * @param s   a digit of variable length
   * @return    the padded digit (or not)
   */

  private String pad2Digits(String s) {
    if (s.length() < 2) {
      return "0" + s;
    }
    return s;
  }

  /**
   * Returns a reference to the note array list
   *
   * @return  the current note filenames
   */
  public List<String> getNoteFilenames() {
    return notes;
  }

  /**
   * Returns a reference to the note array list, containing full path filenames
   *
   * @return  the current note filenames
   */
  public List<String> getFullPathNoteFilenames() {
    return fullPathNotes;
  }

  /**
   * Returns the full path note filename at the given index
   * @param index
   * @return the full path filename for the note
   *         or an empty string if the index parameters are invalid
   */
  public String getNoteFilename(int index) {
    if (index < notes.size() && index >= 0) {
      return fullPath + "/" + notes.get(index);
    }
    return "";
  }

  /**
   * Returns the most recent note saved by the user
   *
   * @return  the most recent note saved
   */
  //
  // TODO Fix this
  // This was supposed to get the most recent photo image, assuming that it would
  // be the last in the list. Alas, because of how Stable arrays work new list entries
  // are sometimes placed in the gaps left after deleting entries.
  // A solution to this would be to store the most latest filename in a
  // share preferences item.
  public String getLastNoteFilename() {
    return getNoteFilename(notes.size() - 1);
  }

  /**
   * Returns the next available note index.
   *
   * @return  the next available note number
   */
  private String getNextNoteIndex() {
    updateNotes();
    return pad2Digits("" + (findMaxNoteIndex() + 1));
  }

  /**
   * Pattern matches filenames in the notes list, building up
   * a list of index numbers.
   * Returns the maximum index found or 0 if the list is empty.
   *
   * @return  the maximum matching index number
   */
  private int findMaxNoteIndex() {
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

  /**
   * Updates the note list
   */
  public void updateNotes() {
    notes.clear();
    File[] files = internalStorage.listFiles();
    for (File f : files) {
      String filename = f.getName();
      if (filename.endsWith(extension)) {
        notes.add(filename);
      }
    }
  }

  /**
   * Updates the note list, containing full path filenames
   */
  private void buildFullPathNoteFilenames() {
    String filename;
    fullPathNotes.clear();
    for (String note : notes) {
      filename = fullPath + "/" + note;
      fullPathNotes.add(filename);
    }
  }

  /**
   * Removes the note at the specified position both from
   * the app's internal storage and also the note array list
   * @param position   the note position in the list
   */

  public void removeNoteFromList(int position) {
    File filename = new File(getNoteFilename(position));
    if (filename.delete()) {
      notes.remove(position);
    }
    buildFullPathNoteFilenames();
  }
}
