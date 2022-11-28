package Incognito;

import org.json.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public record DbProperties() {
    private final static String path = "db_settings.json"; // Path to custom settings file.
    private static JSONObject settings;

    static {
        try {
            settings = new JSONObject(new FileInputStream(path).toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println(e + "\nFile not found at " + path);
        }
    }

    public final static String user = settings.getString("user");
    public final static String password = settings.getString("password");

    public final static String url = settings.getString("url");
    public final static String driver = settings.getString("driver");

}
