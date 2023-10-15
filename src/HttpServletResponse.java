import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.io.OutputStream;

public class HttpServletResponse {
    private OutputStream outputStream = null;
    public HttpServletResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
    public OutputStream getOutputStream() {return outputStream;}

}
