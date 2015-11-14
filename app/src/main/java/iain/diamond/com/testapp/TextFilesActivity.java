package iain.diamond.com.testapp;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TextFilesActivity extends AppCompatActivity implements View.OnClickListener {

  private static android.os.Handler handler;
  private String fullPath;
  private final static String BUNDLE_KEY = "file-text";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_text_files);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    File internalStorage = getFilesDir();
    String filename = "NOTE1.txt";
    fullPath = internalStorage + "/" + filename;

    final EditText editText = (EditText) findViewById(R.id.editText);
    Button saveButton = (Button) findViewById(R.id.saveButton);
    Button loadButton = (Button) findViewById(R.id.loadButton);

    saveButton.setOnClickListener(this);
    loadButton.setOnClickListener(this);

    handler = new android.os.Handler() {
      @Override
      public void handleMessage(Message msg) {
        editText.setText(msg.getData().getString(BUNDLE_KEY));
      }
    };
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.saveButton:
        saveTextFile();
        Toast.makeText(getBaseContext(), "File saved", Toast.LENGTH_LONG).show();
        break;
      case R.id.loadButton:
        loadTextFile();
        Toast.makeText(getBaseContext(), "File loaded", Toast.LENGTH_LONG).show();
        break;
    }
  }

  private void loadTextFile() {

    Runnable loadFile = new Runnable() {
      @Override
      public void run() {
        String lines = "", line = "";
        try {
          FileInputStream fis = new FileInputStream(fullPath);
          BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
          while ((line = reader.readLine()) != null) {
            lines = lines + line;
          }
        } catch (IOException e) {
          e.printStackTrace();
        }

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY, lines);
        msg.setData(bundle);
        handler.sendMessage(msg);
      }
    };
    Thread load = new Thread(loadFile);
    load.start();
  }

  private void saveTextFile() {
    Thread saveFile = new Thread(new Runnable() {
      @Override
      public void run() {
        String text = "Tim Cook, Apple’s chief executive, said that any attempt to weaken encryption could have “very dire consequences” for consumers by making their data less secure.\n\n\nLater today, MPs on the science and technology committee will explore the tech issues arising from the draft Investigatory Powers Bill during a one-off evidence session.\n\nFinally, Cook touched on the subject of encryption, a hot topic due to an ongoing case in which the U.S. Justice Department wants Apple to decrypt a locked iPhone.\n\nThe bill also targets encrypted messaging services such as WhatsApp and Apple’s iMessenger.\n\n“We consider very strongly in finish-to-finish encryption and no again doorways”, Cook stated throughout a go to Britain that comes after plans for a new Investigatory Powers Bill have been outlined this month.\n\nHe said: “We don’t think people want us to read their messages”. We don’t feel we have the right to read their emails, he added. Everybody wants to crack down on terrorists. “Opening a back door can have very dire consequences”, Cook argued. “In times like these when fear-driven bills compromise the right to privacy, we can look to security tools, such as encryption, to defend online communications from unwanted access”.\n\nApple CEO Tim Cook has a lot to say about the future, most of it about how Apple will be smashing the competition.\n\n‘Encryption is widely available.\n\nCook also signaled however that there would be an outcome which he and others would find favorable, predicting: “When the public gets engaged, the press gets engaged deeply, it will become clear to people what needs to occur”.\n\nCook’s comments have been echoed by other prominent figures in the technology industry including the United Nations’ special rapporteur on privacy, Joseph Cannataci. That, of course, doesn’t require FDA accreditation, and this seems to be the first time Apple has explicitly said there are no intentions to pursue it.\n\n \n\n“Just do a media analysis, ladies and gentlemen, do a media analysis of the way the British establishment is trotting out news about the law and the need for the law and ask yourselves the question ‘If this is not orchestrated then what is?'” During the interview, Cook highlighted the health benefits of the Apple Watch, which let customers monitor activity and heart rate, telling the story of a senior high school football player whose Watch saved his life.";

        try {
          FileOutputStream fos = new FileOutputStream(fullPath);
          BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
          fos.write(text.getBytes());
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    saveFile.start();
  }
}
