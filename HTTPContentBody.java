import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.util.HashMap;

public class HTTPContentBody {
    private HashMap<String, String> headers;
    private byte[] body;

    public HTTPContentBody() {
        headers = new HashMap<>();
        body = null;
    }

    public HTTPContentBody(BufferedReader reader) {
        this();
        while (true) {
            try {
                String line = reader.readLine();
                if (line == null || line.length() == 0) break;
                int idx = line.indexOf(": ");
                if (idx == -1) continue;
                String key = line.substring(0, idx);
                String value = line.substring(idx + 2);
                headers.put(key, value);
            } catch (IOException e) {
                break;
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            while (reader.ready()) {
                int _byte = reader.read();
                if (_byte == -1) break;
                stream.write(_byte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            body = stream.toByteArray();
        }
    }

    public void write(OutputStream writer) {
        headers.forEach((key, value) -> {
            try {
                writer.write((key + ": " + value + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            writer.write('\n');
            if (body != null) writer.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getHeader(String key) {
        return headers.getOrDefault(key, null);
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }
}
