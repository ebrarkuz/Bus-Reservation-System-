import java.util.Locale;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookingSystem {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        if (args.length != 2) {
            System.out.println("ERROR: This program works exactly with two command line arguments, the first one is the path to the input file whereas the second one is the path to the output file. Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            return;
        }

        String filePath = args[0];
        OutputFile.writeTerminalOutputToFile(args[1], true);

        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("ERROR: This program cannot read from the \"<INPUT_FILE_PATH>\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!");
            return;
        }

        try {
            String[] bustypeLines = InputFile.readFile(filePath, true, true);
            MakeBus makebus = new MakeBus();
            MakeBus.processInput(bustypeLines);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


