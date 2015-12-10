package iain.diamond.com.testapp;

import java.io.File;

/**
 * The NoteFactory builds Note handlers.
 */

public class NoteFactory {

  enum NoteType {TextNote, AudioNote, PhotoNote, DrawNote}
  private static NoteFactory noteFactory;

  // make NoteFactory a singleton class
  private NoteFactory() {}

  public static NoteFactory getNoteFactory() {
    if (noteFactory == null) {
      noteFactory = new NoteFactory();
    }
    return noteFactory;
  }

  /**
   * Returns a new note handler depending on the note type specified.
   *
   * @param directory   the activities internal storage location
   * @param noteType    the note type, see enum NoteType
   * @return
   */
  public NoteHandler getNoteHandler(File directory, NoteType noteType) {
    switch (noteType) {
      case TextNote:
        return new NoteHandler(directory, "TEXT", ".txt");
      case AudioNote:
        return new NoteHandler(directory, "VOICE", ".3gpp");
      case PhotoNote:
        return new NoteHandler(directory, "IMAGE", ".png");
      case DrawNote:
        return new NoteHandler(directory, "DRAW", ".drw");
    }
    return null;
  }
}
