package server.utils;

import java.io.*;

public class FileUtils {

    public static String getStringFromFile(String fileName) throws IOException {
        InputStream streamReader = new FileInputStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(streamReader));
        String line = buf.readLine();
        StringBuilder builder = new StringBuilder();
        while (line != null) {
            builder.append(line).append("\n");
            line = buf.readLine();
        }
        return builder.toString();
    }
}
