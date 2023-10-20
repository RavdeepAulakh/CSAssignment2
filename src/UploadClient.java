import java.io.*;
import java.net.*;

public class UploadClient {
    public UploadClient() { }

    public String uploadFile() {
        String message = "Hello from the client side";
        String listing = "";

        try {
            Socket socket = new Socket("localhost", 8082);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            // HTTP POST request
            String httpHeaders = "POST /upload HTTP/1.1\r\n" +
                    "Host: localhost\r\n" +
                    "User-Agent: UploadClient\r\n" +
                    "Accept: */*\r\n" +
                    "Content-Length: " + message.getBytes().length + "\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "\r\n";

            // Send the headers and the message
            out.write(httpHeaders.getBytes());
            out.write(message.getBytes());
            socket.shutdownOutput();

            String filename;
            while ((filename = in.readLine()) != null) {
                listing += filename + "\n";
            }
            socket.shutdownInput();
        } catch (Exception e) {
            System.err.println(e);
        }

        return listing;
    }

    public static void main(String[] args) {
        UploadClient client = new UploadClient();
        String listing = client.uploadFile();
        System.out.println("Received directory listing:\n" + listing);
    }
}
