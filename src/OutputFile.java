import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class OutputFile {
    public static void writeTerminalOutputToFile(String path, boolean append) {
        try {
            PrintStream fileStream = new PrintStream(new FileOutputStream(path, append)) {
                private boolean lastLineEmpty = true;

                @Override
                public void println(String line) {
                    if (!line.trim().isEmpty()) {
                        super.println(line);
                        lastLineEmpty = false;
                    } else {

                        if (!lastLineEmpty) {
                            super.println(line);
                        }
                        lastLineEmpty = true;
                    }
                }
            };
            System.setOut(fileStream);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


