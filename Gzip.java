import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

public class Gzip {
    public static byte[] encode(byte[] content){
        if(content == null){
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
            GZIPOutputStream gzipStream = new GZIPOutputStream(stream);
            gzipStream.write(content);
            gzipStream.finish();
//            gzipStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return stream.toByteArray();
    }
    public static byte[] decode(byte[] content){
        ByteArrayInputStream stream = new ByteArrayInputStream(content);
        try{
            GZIPInputStream gzipStream = new GZIPInputStream(stream);
            byte[] result = gzipStream.readAllBytes();
            gzipStream.close();
            return result;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
