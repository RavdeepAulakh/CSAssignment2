import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.io.OutputStream;
public class HttpServletResponse {
    private OutputStream outputStream = null;
    private String contentType;
    private String characterEncoding;

    public HttpServletResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }
}
