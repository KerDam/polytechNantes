import java.io.IOException;

public class FileExistsException extends IOException {

    public FileExistsException(String msg) {
        super(msg);
    }

}
