import java.io.*;
import java.net.Socket;

public class UploadServerThread extends Thread{
    private Socket socket = null;
    public UploadServerThread(Socket socket) {
        super("DirServerThread");
        this.socket = socket;
    }
    public void run() {
        try {
            InputStream in = socket.getInputStream();

            BufferedInputStream bufferedInput = new BufferedInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedInput));

            //read the first line of http request
            String requestLine = reader.readLine();

            HttpServletRequest req = new HttpServletRequest(bufferedInput);

            OutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);

            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String urlPath = requestParts[1];

            HttpServlet httpServlet = new UploadServlet();

            if ("GET".equals(method) && "/".equals(urlPath)) {
                httpServlet.doGet(req, res);
            }

            if ("POST".equals(method) && "/upload".equals(urlPath)) {
                httpServlet.doPost(req, res);
            }

            OutputStream out = socket.getOutputStream();
            out.write(((ByteArrayOutputStream) baos).toByteArray());
            socket.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
