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
//            System.out.println("Request Line: " + requestLine);
            HttpServletRequest req = new HttpServletRequest(bufferedInput);

            OutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);


            HttpServlet httpServlet = new UploadServlet();

            if(requestLine.contains("GET")) {
                if(requestLine.charAt(4) == '/'){
                    httpServlet.doGet(req, res);
                }
            }

            if(requestLine.contains("POST")) {
                if(requestLine.charAt(4) == '/'){
                    httpServlet.doPost(req, res);
                }
            }

            OutputStream out = socket.getOutputStream();
            out.write(((ByteArrayOutputStream) baos).toByteArray());
            socket.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
