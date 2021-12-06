import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WordCounterTest {
    File testFile;

    @org.junit.jupiter.api.BeforeEach
    public void before() throws IOException {
        testFile = new File("test.txt");
        FileWriter fw = new FileWriter(testFile);

        fw.write("Programista, programista powinien umieć w Google. ");
        fw.close();
    }
    @org.junit.jupiter.api.Test
    void countWords() throws IOException {

        assertEquals(new HashMap<String, Integer>() {{
            put("programista", 2);
            put("powinien", 1);
            put("umieć", 1);
            put("w", 1);
            put("google", 1);
        }}, WordCounter.countWords("test.txt"));

    }
    @org.junit.jupiter.api.AfterEach
    public void after() {
        testFile.delete();
    }
}