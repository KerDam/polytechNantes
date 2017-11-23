import java.awt.*;
import java.applet.*;

public class jpnsApplet extends Applet {

   Button show;
   Editor f;

   public void init() {
      super.init();
      show = new Button("Open jPNS");
      setLayout(new GridLayout(0,1));
      Panel p = new Panel();
      p.setLayout(new FlowLayout(FlowLayout.CENTER));
      p.add(show);
      f = new Editor("jPNS - The Petri - Net - Simulator", getDocumentBase());
      f.resize(700, 600);
      add("Center", p);
   }

   public boolean action(Event e, Object arg) {
        if (e.target == show) {
            if (f == null) {
                f = new Editor("jPNS - The Petri - Net - Simulator", getDocumentBase());
                f.resize(700, 600);
            }
            f.show();
            return true;
        } else return false;
   }

 /*  public void paint(Graphics g) {
      g.drawString("PNS running...", 20, 20);
   }
 */
}
