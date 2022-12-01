import java.io.*;

public class SourceLoader {
    public static HTTPResponse make_response(int code) {
        HTTPResponse res = new HTTPResponse();
        res.setCode(code);
        switch (code) {
            case 200 -> res.setStatus("OK");
            case 201 -> res.setStatus("CREATED");
            case 202 -> res.setStatus("ACCEPTED");
            case 404 -> res.setStatus("NOT_FOUND");
            case 405 -> res.setStatus("METHOD_NOT_ALLOWED");
            case 505 -> res.setStatus("HTTP_VERSION_NOT_SUPPORTED");
            default -> res.setStatus("DEFAULT");
        }
        return res;
    }

    public static byte[] loadFileContent(String path) {
        try {
            InputStream stream;
            try {
                stream = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                System.out.printf("%s is not Found!\n", path);
                return null;
            }
            int length = stream.available();
            byte[] bytes = new byte[length];
            int count = stream.read(bytes);
            return bytes;
        } catch (IOException e) {
            return null;
        }
    }

    public static String getFilePath(String url) {
        StringBuilder builder = new StringBuilder("files");
        for (int i = 0; i < url.length(); ++i) {
            char ch = url.charAt(i);
            if (ch == '/') builder.append(File.separator);
            else builder.append(ch);
        }
        return builder.toString();
    }

    public static HTTPResponse loadIndex(HTTPHeader header) {
        if (!header.getMethod().equals("GET")) return make_response(405);
        HTTPResponse res = make_response(200);
        res.setBody(loadFileContent(getFilePath("/index.html")));
        return res;
    }
}
