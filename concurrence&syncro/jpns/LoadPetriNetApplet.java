import java.awt.*;
import java.net.*;

public class LoadPetriNetApplet extends Dialog {

    URL DocBase;
    Editor Parent;
    List Examples;

    public LoadPetriNetApplet(Frame parent, URL docbase) {
        super(parent,"Examples", true);
        DocBase = docbase;
        Parent = (Editor)parent;
        Examples = new List(10, false);
        Examples.addItem("a1");
        Examples.addItem("a2");
        Examples.addItem("add");
        Examples.addItem("mult");
        Examples.addItem("phil");
        Examples.addItem("rw");
        Examples.addItem("simple");
        Examples.addItem("sub");
        Examples.addItem("sub1");
        Examples.addItem("sub2");
        Examples.select(0);
        this.setLayout(new BorderLayout());
        this.add("North", new Label("Please select the wanted example"));
        this.add("Center", Examples);
        Panel Buttons = new Panel();
        Buttons.setLayout(new FlowLayout());
        Buttons.add(new Button("Ok"));
        Buttons.add(new Button("Cancel"));
        this.add("South",Buttons);
        this.pack();
        move(Parent.location().x + 50, Parent.location().y + 50);
        this.setResizable(false);
    }

    public boolean handleEvent(Event e) {
        switch (e.id) {
        case Event.ACTION_EVENT:
            if (e.target instanceof Button) {
                if (((String)e.arg).equals("Ok")) {
                    // System.out.println("Item Nr. " + Examples.getSelectedIndex());
                    Parent.loadAppletPN(Examples.getSelectedItem());
                    this.hide();
                    return true;
                }
                if (((String)e.arg).equals("Cancel")) {
                    this.hide();
                    return true;
                }
            }
            if (e.target instanceof List) {
                // System.out.println("Item Nr. " + Examples.getSelectedIndex());
                Parent.loadAppletPN(Examples.getSelectedItem());
                this.hide();
                return true;
            }
            return false;
        default:
            return false;
        }
    }
}



