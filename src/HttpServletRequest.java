import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpServletRequest {
    private InputStream inputStream = null;
    public HttpServletRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public InputStream getInputStream() {return inputStream;}


}
