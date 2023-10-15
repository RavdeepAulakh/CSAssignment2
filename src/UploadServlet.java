import java.io.*;
import java.io.InputStream;
import java.time.Clock;

public class UploadServlet extends HttpServlet{
    public UploadServlet() {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream in = request.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] content = new byte[1];
            int bytesRead = -1;
            while( ( bytesRead = in.read( content ) ) != -1 ) {
                baos.write( content, 0, bytesRead );
            }
            Clock clock = Clock.systemDefaultZone();
            long milliSeconds=clock.millis();
            OutputStream outputStream = new FileOutputStream(new File(String.valueOf(milliSeconds) + ".png"));
            baos.writeTo(outputStream);
            outputStream.close();
            PrintWriter out = new PrintWriter(response.getOutputStream(), true);
            File dir = new File(".");
            String[] chld = dir.list();
            for(int i = 0; i < chld.length; i++){
                String fileName = chld[i];
                out.println(fileName+"\n");
                System.out.println(fileName);
            }
        } catch(Exception ex) {
            System.err.println(ex);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        PrintWriter out = new PrintWriter(response.getOutputStream(), true);
        response.setContentType("text/html");
        // Write the HTML form for file upload
        out.println("<html>");
        out.println("<head><title>File Upload Form</title></head>");
        out.println("<body>");
        out.println("<h1>File Upload Form</h1>");
        out.println("<form action='' method='post' enctype='multipart/form-data'>");
        out.println("<label for='file'>Select a file:</label>");
        out.println("<input type='file' name='file' id='file'><br>");
        out.println("<input type='submit' value='Upload'>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

        out.close(); // Close the PrintWriter
    }
}
