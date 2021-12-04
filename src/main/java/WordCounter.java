import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class WordCounter {

    public static boolean checkSize(String filePath, long maxFileSize) throws IOException {
            long sizeOfFile = Files.size(Path.of(filePath));
            return sizeOfFile < maxFileSize;
    }
}
