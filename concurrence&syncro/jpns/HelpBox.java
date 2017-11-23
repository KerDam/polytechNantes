import java.awt.*;
import java.io.*;
import java.net.*;

public class HelpBox extends Frame {

    Button close;

    public HelpBox(URL docbase) throws IOException {
        super("HelpFile");
        int size = 0;
        InputStream in = null;
        if (docbase != null) {
            try {
                URL help = new URL(docbase, "Helpme.doc");
                URLConnection helpcon = help.openConnection();
                size = helpcon.getContentLength();
                in = helpcon.getInputStream();
            }
            catch (MalformedURLException m) {System.out.println("Wrong URL");}
        } else {
            File f = new File("Helpme.doc");
            size = (int) f.length();
            in = new FileInputStream(f);
        }
        if (in != null) {
/*            int bytes_read = 0;
            byte[] data = new byte[size];
            while (bytes_read < size) {
                bytes_read += in.read(data, bytes_read, size-bytes_read);
            }*/
            int bytes_read = 0;
            int bytes = 0;
            byte[] data = new byte[10000];
            while (bytes_read != -1) {
                bytes_read = in.read(data, bytes, 1);
                bytes++;
            }
            TextArea ta = new TextArea(new String(data, 0), 24, 80);
            ta.setFont(new Font("Courier", Font.PLAIN, 12));
            ta.setEditable(false);
            this.add("Center", ta);
        } else {throw new IOException();}
        close = new Button("Close");
        this.add("South", close);
        this.pack();
    }

    public boolean action(Event e, Object what) {
        if (e.target == close) {
            this.hide();
            return true;
        }
        return false;
    }

}
