package Incognito;

import org.json.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public record DbProperties() {
    private final static String path = "src/Incognito/db_settings.json"; // Path to custom settings file.
    private static JSONObject settings = null;

    static {
        try {
            settings = new JSONObject(new String(new FileInputStream(path).readAllBytes()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println(e + "\nFile not found at location " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static String user = settings.getString("user");
    public final static String password = settings.getString("password");

    public final static String url = settings.getString("url");
    public final static String driver = settings.getString("driver");

}
