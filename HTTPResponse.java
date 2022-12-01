import java.io.*;
import java.util.HashMap;

public class HTTPResponse {
    private String version, status;
    private int code = 404;
    private HTTPContentBody body;

    HTTPResponse() {
        body = new HTTPContentBody();
    }

    HTTPResponse(String version, int code, String status) {
        this();
        this.version = version;
        this.code = code;
        this.status = status;
    }

    HTTPResponse(int code, String status) {
        this();
        this.code = code;
        this.status = status;
    }

    HTTPResponse(HTTPResponse response){
        this();
        this.version = response.version;
        this.code = response.code;
        this.status = response.status;
        this.body = response.body;
    }

    public void write(OutputStream writer) {
        try {
            writer.write((version + " " + code + " " + status + "\n").getBytes());
            body.write(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCode() {
        return code;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHeader(String key, String value) {
        this.body.setHeader(key, value);
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.body.setHeaders(headers);
    }

    public void setBody(byte[] body) {
        this.body.setBody(body);
    }

    public byte[] getBody() {
        return this.body.getBody();
    }
}