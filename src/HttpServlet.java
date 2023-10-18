import java.io.IOException;
import java.io.InputStream;

public abstract class HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) { return; };
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException { return; };

}
