import java.util.*;
import java.util.regex.*;
import java.util.function.Function;

public class Loader {
    private final HashMap<String, Function<HTTPHeader, HTTPResponse>> mapping;

    public void register(String regex, Function<HTTPHeader, HTTPResponse> function) {
        mapping.put(regex, function);
    }

    public Loader() {
        mapping = new HashMap<>();
        init();
    }

    private void init(){
        register("/(index.html)?", SourceLoader::loadIndex);
    }

    private static String getDate() {
        String time = (new Date()).toString();
        String[] part = time.split(" ");
        //from "EEE MMM dd HH:mm:ss zzz yyyy";
        //to   "EEE, dd MMM yyyy HH:mm:ss zzz"
        return part[0] + ", " + part[2] + " " + part[1] + " " + part[5] + " " + part[3] + " " + part[4];
    }

    private static boolean CanMatch(String path, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(path);
        return match.matches();
    }

    public HTTPResponse load(HTTPHeader headers) {
        HTTPResponse[] res = new HTTPResponse[]{null};
        boolean[] finish = new boolean[]{false};
        mapping.forEach(
                (key, value) -> {
                    if (!finish[0] && CanMatch(headers.getPath(), key)) {
                        finish[0] = true;
                        res[0] = value.apply(headers);
                    }
                }
        );
        HTTPResponse result = res[0];
        if (result == null) {
            result = SourceLoader.make_response(404);
            result.setVersion(headers.getVersion());
            return result;
        }
        result.setVersion(headers.getVersion());
        if (headers.getHeader("Accept-Encoding").contains("gzip")) {
            byte[] zippedBody = Gzip.encode(result.getBody());
            result.setBody(zippedBody);
            result.setHeader("Content-Encoding", "gzip");
        }
        result.setHeader("Connection", "keep-alive");
        result.setHeader("Content-language", "zh-CN");
        String type = headers.getHeader("Accept");
        int idx = type.indexOf(',');
        result.setHeader("Content-type", idx != -1 ? type.substring(0, idx) : "text/plain");
        result.setHeader("Date", getDate());
        result.setHeader("Content-length", String.valueOf(result.getBody().length));
        return result;
    }
}