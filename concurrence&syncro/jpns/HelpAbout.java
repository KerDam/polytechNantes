import java.awt.*;

public class HelpAbout extends Frame {

    Button close;

    public HelpAbout(Frame par) {
        super("About");
        this.setLayout(new GridLayout(0,1));
        Font f = new Font("Helvetica", Font.PLAIN, 12);
        Font fb = new Font("Helvetica", Font.BOLD, 12);
        Label a = new Label("PNS - The Petri Net Simulator", Label.CENTER);
        Label b = new Label("*** Java - Version ***", Label.CENTER);
        a.setFont(fb);
        b.setFont(fb);
        this.add(a);
        this.add(b);
        this.add(new Label(""));
        Label c = new Label("Created by", Label.CENTER);
        c.setFont(fb);
        this.add(c);
        this.setFont(f);
        this.add(new Label("Achim Haessler", Label.CENTER));
        this.add(new Label("email: amhaessl@tick.informatik.uni-stuttgart.de", Label.CENTER));
        this.add(new Label("Martin Kada", Label.CENTER));
        this.add(new Label("email: mnkada@tick.informatik.uni-stuttgart.de", Label.CENTER));
        this.add(new Label("Joerg Walz", Label.CENTER));
        this.add(new Label("email: jgwalz@tick.informatik.uni-stuttgart.de", Label.CENTER));
        this.add(new Label(""));
        Label e = new Label("Supervisor", Label.CENTER);
        e.setFont(fb);
        this.add(e);
        this.add(new Label("Thomas Braeunl", Label.CENTER));
        this.add(new Label("WEB: http://www.informatik.uni-stuttgart.de/ipvr/bv/braunl", Label.CENTER));
        this.add(new Label("email: braunl@informatik.uni-stuttgart.de", Label.CENTER));
        this.add(new Label(""));
        Label d = new Label("Thanks for playing around!", Label.CENTER);
        d.setFont(fb);
        this.add(d);
        this.add(new Label(""));
        close = new Button("Close");
        this.add(close);
        this.setResizable(false);
        move(par.location().x + 50, par.location().y + 50);
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
