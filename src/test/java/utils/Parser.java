package utils;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Parser {

    public static JSONObject parseJSONFile(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            return new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
