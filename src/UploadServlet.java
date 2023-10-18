//import jakarta.servlet.http.Part;

import java.io.*;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.*;
public class UploadServlet extends HttpServlet {

    private String directoryPath = "/Users/laurieannesolkoski/IdeaProjects/CSAssignment2/images";

    public UploadServlet() {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Getting the InputStream from the HttpServletRequest object
        InputStream inputStream = request.getInputStream();

        // Initialize buffers and data structures for parsing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        byte[] requestData = baos.toByteArray();

        // Convert to a string for parsing; might be inefficient for large data
        String requestDataStr = new String(requestData, StandardCharsets.UTF_8);

        // Split multipart form data into parts using a simplistic approach
        String[] parts = requestDataStr.split("--"); // Split by boundary, no specific boundary needed

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
                fileData = part.substring(dataStart).getBytes();
            }
        }

        if (filename != null) {
            filename = formFields.get("caption") + "_" + formFields.get("date") + "_" + filename;
            String filePath = directoryPath + File.separator + filename;

            // Write the file data to the specified file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        displayDirectoryListing(response);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Get the OutputStream for writing the response
        PrintWriter out = new PrintWriter(response.getOutputStream(), true);

        // Write the HTTP status line and headers
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("Charset=UTF-8");
        out.println();  // An empty line to separate headers and content

        // Write the HTML content
        out.println("<html>");
        out.println("<head><title>File Upload Form</title></head>");
        out.println("<body>");
        out.println("<h1>File Upload Form</h1>");
        out.println("<form action='/upload' method='post' enctype='multipart/form-data'>\n");
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


    private void displayDirectoryListing(HttpServletResponse response) throws IOException {
        PrintWriter out = new PrintWriter(response.getOutputStream(), true);

        File directory = new File(directoryPath);
        String[] files = directory.list();

        // Sort files alphabetically
        Arrays.sort(files);

        // Print to console
        System.out.println("Files in Directory:");
        for (String filename : files) {
            System.out.println(filename);
        }


        // Set the content type and character encoding, although these might be placeholders for now
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        out.println("<html>");
        out.println("<head><title>Files List</title></head>");
        out.println("<body>");
        out.println("<h1>Files in Directory</h1>");
        out.println("<ul>");

        for (String filename : files) {
            out.println("<li>" + filename + "</li>");
        }

        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }
}
