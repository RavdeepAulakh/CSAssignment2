import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class UploadServerThread extends Thread {
    private Socket socket;
    private Logger logger = Logger.getLogger(UploadServerThread.class.getName());

    public UploadServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream in = socket.getInputStream();

            BufferedInputStream bufferedInput = new BufferedInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedInput));

            // Read the first line of the HTTP request
            String requestLine = reader.readLine();

            HttpServletRequest req = new HttpServletRequest(bufferedInput);
            OutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);

            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String urlPath = requestParts[1];

            // Load the FileUploadServlet class dynamically using reflection
            Class<?> servletClass = Class.forName("UploadServlet");
            HttpServlet httpServlet = (HttpServlet) servletClass.getDeclaredConstructor().newInstance();

            logger.info("Loaded the FileUploadServlet class dynamically.");

            if ("GET".equals(method) && "/".equals(urlPath)) {
                httpServlet.doGet(req, res);
                logger.info("doGet method of FileUploadServlet invoked.");
            }

            if ("POST".equals(method) && "/upload".equals(urlPath)) {
                httpServlet.doPost(req, res);
                logger.info("doPost method of FileUploadServlet invoked.");
            }

            OutputStream out = socket.getOutputStream();
            out.write(((ByteArrayOutputStream) baos).toByteArray());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error occurred: " + e.getMessage());
        }
    }
}
