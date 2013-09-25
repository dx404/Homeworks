package miniJava.SyntacticAnalyzer;

import java.io.*;

public class SourceFile {

  static final char eol = '\n';
  static final char eot = '\u0000';

  File sourceFile;
  FileInputStream source; // use it to real file
  int currentLine; //current Line number ? 

  public SourceFile(String filename) {
    try {
      sourceFile = new File(filename);
      source = new FileInputStream(sourceFile);
      currentLine = 1;
    }
    catch (IOException s) {
      sourceFile = null;
      source = null;
      currentLine = 0;
    }
  }

  char getSource() {
    try {
      int c = source.read();

      if (c == -1) {
        c = eot;
      } else if (c == eol) {
          currentLine++;
      }
      return (char) c;
    }
    catch (java.io.IOException s) {
      return eot;
    }
  }

  int getCurrentLine() {
    return currentLine;
  }
}
