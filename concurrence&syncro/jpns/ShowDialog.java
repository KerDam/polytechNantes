import java.awt.*;

public class ShowDialog extends Dialog {

   Panel Input, Buttons;
   Checkbox showbox;
   Scrollbar delay;
   Label delayCount;

   public ShowDialog(Frame parent) {
      super(parent, "Edit Show Options", false);
      this.setLayout(new BorderLayout());

      Input = new Panel();
      Input.setLayout(new GridLayout(0,2,2,2));


      this.add("North", new Label("Enter Options"));

      Input.add(new Label(""));
      showbox = new Checkbox("Show");
      Input.add(showbox);
      Input.add(new Label("Delay (msec)",Label.RIGHT));
      delay = new Scrollbar(Scrollbar.HORIZONTAL, 0, 100, 0, 10000);
      Input.add(delay);
      Input.add(new Label(""));
      delayCount = new Label(Integer.toString(delay.getValue()));
      Input.add(delayCount);

      this.add("Center", Input);

      Buttons = new Panel();
      Buttons.setLayout(new FlowLayout());
      Buttons.add(new Button("Ok"));
      Buttons.add(new Button("Cancel"));
      this.add("South",Buttons);
      this.pack();
      this.resize(250,180);
      move(parent.location().x + 50, parent.location().y + 50);
      this.setResizable(false);
   }

   public void setDelay(int d) {
      delay.setValue(d);
      delayCount.setText(Integer.toString(delay.getValue()));
   }

   public void setDemo(boolean d) {
      showbox.setState(d);
   }

   public boolean handleEvent(Event e) {
      switch (e.id) {
      case Event.ACTION_EVENT:
        if (e.target instanceof Button) {
          if (((String)e.arg).equals("Ok")) {
              ((Editor)getParent()).setDelay(delay.getValue());
              ((Editor)getParent()).setDemo(showbox.getState());
              this.hide();
              return true;
          }
          if (((String)e.arg).equals("Cancel")) {
              this.hide();
              return true;
          }
          return false;
        }
      case Event.SCROLL_LINE_UP :
      case Event.SCROLL_LINE_DOWN :
      case Event.SCROLL_PAGE_UP :
      case Event.SCROLL_PAGE_DOWN :
      case Event.SCROLL_ABSOLUTE :
        delayCount.setText(Integer.toString(delay.getValue()));
        return true;

      default:
          return super.handleEvent(e);
      }
   }

} /* class TransitionDialog */
