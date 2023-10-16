import jakarta.servlet.http.Part;

import java.io.*;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Clock;
import java.util.Scanner;

public class UploadServlet extends HttpServlet{
    public UploadServlet() {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Get the InputStream from the HttpServletRequest object
        InputStream inputStream = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Print the HTTP message to the console
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Get the OutputStream for writing the response
        PrintWriter out = new PrintWriter(response.getOutputStream(), true);

        // Write the HTTP status line and headers
        out.println("GET HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("Charset=UTF-8");
        out.println();  // An empty line to separate headers and content

        // Write the HTML content
        out.println("<html>");
        out.println("<head><title>File Upload Form</title></head>");
        out.println("<body>");
        out.println("<h1>File Upload Form</h1>");
        out.println("<form action='/upload' method='post' enctype='multipart/form-data'>'>");
        out.println("<label for='file'>Select a file:</label>");
        out.println("<input type='file' name='file' id='file'><br>");
        out.println("<label for='caption'>Caption:</label>");
        out.println("<input type='text' name='caption' id='caption'><br>");
        out.println("<label for='date'>Date:</label>");
        out.println("<input type='date' name='date' id='date'><br>");
        out.println("<input type='submit' value='Upload'>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

    }

}
