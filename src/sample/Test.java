package sample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by neek on 18.11.2016.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        byte[] keyB = Files.readAllBytes(Paths.get("smallKey.txt"));
        String keyS = new String(keyB);
        BBs generator = new BBs(keyS);
        for (int i = 0; i < 1000; i++) {
            System.out.println(generator.next());
        }
    }
}
