import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class WordCounter {

    public static boolean checkSize(String filePath, long maxFileSize) throws IOException {
        long sizeOfFile = Files.size(Path.of(filePath));
        return sizeOfFile < maxFileSize;
    }

    public static HashMap<String, Integer> countWords(String filePath) throws IOException {
        HashMap<String, Integer> wordsMap = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        for (String line; (line = reader.readLine()) != null; ) {
            for (String w : line.trim().split("\\s+")) {
                w = w.replaceAll("[^\\p{L}]", "").toLowerCase();
                wordsMap.merge(w, 1, Integer::sum);
            }
        }
        reader.close();
        return wordsMap;
    }

    public static Stream<Map.Entry<String, Integer>> sortWords(HashMap<String, Integer> wordsMap) {
        return wordsMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    }
}
