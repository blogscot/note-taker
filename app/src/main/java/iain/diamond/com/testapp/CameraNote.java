package iain.diamond.com.testapp;

import java.io.File;

public class CameraNote extends Note {

  public CameraNote(File directory) {
    super(directory);
    prefix = "IMAGE";
    extension = ".png";
    pattern = "^IMAGE(\\d{2}).*";
  }
}
