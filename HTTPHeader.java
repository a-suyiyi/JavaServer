import java.io.*;
import java.util.*;

public class HTTPHeader {
    private String method, path, version;
    private HTTPContentBody body;
    private Map<String, String> arguments;

    private void splitFirstLine(String line) {
        int idx1, idx2;
        idx1 = line.indexOf(' ');
        if (idx1 == -1) return;
        idx2 = line.indexOf(' ', idx1 + 1);
        if (idx2 == -1) return;
        method = line.substring(0, idx1);
        path = line.substring(idx1 + 1, idx2);
        version = line.substring(idx2 + 1);
        idx1 = path.indexOf('?');
        if (idx1 == -1) return;
        String args = path.substring(idx1 + 1), now;
        path = path.substring(0, idx1);
        idx2 = -1;
        int tmp;
        do {
            tmp = idx2 + 1;
            idx2 = args.indexOf('&', idx2 + 1);
            if (idx2 != -1) now = args.substring(tmp, idx2);
            else now = args.substring(tmp);
            tmp = now.indexOf('=');
            if (tmp == -1) return;
            arguments.put(now.substring(0, tmp), now.substring(tmp + 1));
        } while (idx2 + 1 != 0);
    }

    public HTTPHeader(BufferedReader reader) {
        arguments = new HashMap<>();
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

    public String getArgument(String key) {
        return arguments.get(key);
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