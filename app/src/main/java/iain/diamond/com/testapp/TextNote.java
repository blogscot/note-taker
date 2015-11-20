package iain.diamond.com.testapp;

import java.io.File;

public class TextNote extends Note {

  public TextNote(File directory) {
    super(directory);
    prefix = "NOTE";
    extension = ".txt";
    pattern = "^NOTE(\\d{2}).*";
  }
}
