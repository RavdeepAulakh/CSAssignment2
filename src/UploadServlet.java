

import java.io.*;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Clock;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.*;
public class UploadServlet extends HttpServlet{
    public UploadServlet() {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Get the InputStream from the HttpServletRequest object
        InputStream inputStream = request.getInputStream();

        // Convert InputStream to a string for parsing
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Split multipart form data into parts
        String[] parts = sb.toString().split("--"); // Split by boundary, no specific boundary needed

        Map<String, String> formFields = new HashMap<>();
        String filename = null;
        byte[] fileData = null;

        for (String part : parts) {
            if (part.contains("Content-Disposition: form-data; name=\"caption\"")) {
                String caption = part.split("\r\n\r\n")[1].trim();
                formFields.put("caption", caption);
            } else if (part.contains("Content-Disposition: form-data; name=\"date\"")) {
                String date = part.split("\r\n\r\n")[1].trim();
                formFields.put("date", date);
            } else if (part.contains("Content-Disposition: form-data; name=\"file\"; filename=\"")) {
                // Extract the file name
                filename = part.split("filename=\"")[1].split("\"")[0];

                // Extract file data
                int dataStart = part.indexOf("\r\n\r\n") + 4;
                byte[] partBytes = part.substring(dataStart).getBytes();
                // Concatenate the part bytes
                if (fileData == null) {
                    fileData = partBytes;
                } else {
                    byte[] combined = new byte[fileData.length + partBytes.length];
                    System.arraycopy(fileData, 0, combined, 0, fileData.length);
                    System.arraycopy(partBytes, 0, combined, fileData.length, partBytes.length);
                    fileData = combined;
                }
            }
        }

        filename = formFields.get("caption") + "_" + formFields.get("date") + "_" + filename;
        System.out.println(filename);

        // Write to the specified folder
        String directoryPath = File.separator + "Users" + File.separator + "ravdeepaulakh" + File.separator + "Documents" + File.separator + "images";
        String filePath = directoryPath + File.separator + filename;

        // Write the file data to the specified file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileData);
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
