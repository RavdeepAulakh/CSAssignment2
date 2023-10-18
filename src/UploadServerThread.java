import java.io.*;
import java.net.Socket;

public class UploadServerThread extends Thread {
    private Socket socket = null;

    public UploadServerThread(Socket socket) {
        super("UploadServerThread");
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream in = socket.getInputStream();

            // Use BufferedInputStream directly
            BufferedInputStream bufferedInput = new BufferedInputStream(in);

            // Read the first line of the HTTP request using BufferedInputStream
            ByteArrayOutputStream baosRequestLine = new ByteArrayOutputStream();
            int ch;
            while((ch = bufferedInput.read()) != -1) {
                baosRequestLine.write(ch);
                if (baosRequestLine.toString().endsWith("\r\n")) {
                    break; // stop when we've read one line
                }
            }
            String requestLine = baosRequestLine.toString().trim();
            System.out.println("Received request: " + requestLine);

            // If the request line is empty
            if (requestLine.isEmpty()) {
                System.out.println("Received empty request.");
                //socket.close();
                return;
            }



            // Pass the BufferedInputStream directly to HttpServletRequest
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
