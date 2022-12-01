import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    public static Loader loader = new Loader();
    private final ServerSocket serverSocket;
    private final static boolean DEBUG = false;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        ExecutorService ThreadPool = Executors.newCachedThreadPool();
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ThreadPool.execute(() -> processConnection(clientSocket));
        }
    }

    private void processConnection(Socket clientSocket) {
        try {
            while (!clientSocket.isClosed()) {
                InputStreamReader streamReader = new InputStreamReader(clientSocket.getInputStream());
                if (!streamReader.ready()) continue;
                HTTPHeader header = new HTTPHeader(new BufferedReader(streamReader));
                HTTPResponse res;
                if(header.getVersion().contains("HTTP/2")){
                    res = SourceLoader.make_response(505);
                }
                else{
                    res = loader.load(header);
                }
                OutputStream writer = clientSocket.getOutputStream();
                res.write(writer);
                if(DEBUG)
                System.out.printf("%s %s:%d %s %s %d\n",
                        header.getMethod(),
                        clientSocket.getInetAddress().toString(),
                        clientSocket.getPort(),
                        header.getVersion(),
                        header.getPath(),
                        res.getCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}