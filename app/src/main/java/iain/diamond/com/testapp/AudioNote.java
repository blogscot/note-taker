package iain.diamond.com.testapp;

import java.io.File;

public class AudioNote extends Note {

  public AudioNote(File directory) {
    super(directory);
    prefix = "VOICE";
    extension = ".3gpp";
    pattern = "^VOICE(\\d{2}).*";
  }
}
