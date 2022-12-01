import java.io.*;
import java.util.*;

public class HTTPHeader {
    private String method, path, version;
    private HTTPContentBody body;

    private void splitFirstLine(String line) {
        int idx1, idx2;
        idx1 = line.indexOf(' ');
        if (idx1 == -1) return;
        idx2 = line.indexOf(' ', idx1 + 1);
        if (idx2 == -1) return;
        method = line.substring(0, idx1);
        path = line.substring(idx1 + 1, idx2);
        version = line.substring(idx2 + 1);
    }

    public HTTPHeader(BufferedReader reader) {
        try {
            String firstLine = reader.readLine();
            if (firstLine != null) {
                splitFirstLine(firstLine);
                body = new HTTPContentBody(reader);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getHeader(String key) {
        return body.getHeader(key);
    }

    public HashMap<String, String> getHeaders() {
        return body.getHeaders();
    }

    public HTTPContentBody getBody() {
        return body;
    }
}