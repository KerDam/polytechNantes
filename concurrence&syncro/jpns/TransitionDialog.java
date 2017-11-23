import java.awt.*;

public class TransitionDialog extends Dialog {

   Panel Input, Buttons;
   transition actual;
   TextField NameField, PriorityField;
   Choice Orientation;
   Checkbox Stop;

   public TransitionDialog(Frame parent, transition t) {
      super(parent, "Edit Transitions", false);
      this.setLayout(new BorderLayout());

      Input = new Panel();
      Input.setLayout(new GridLayout(0,2,2,2));
      actual = t;

      Orientation = new Choice();
      Orientation.addItem("Horizontal");
      Orientation.addItem("Vertical");
      Orientation.addItem("Square");
      Orientation.addItem("Diagonal 1");
      Orientation.addItem("Diagonal 2");

      this.add("North", new Label("Token"));
      NameField = new TextField(actual.getName(), 1);
      PriorityField = new TextField(Integer.toString(actual.getPriority()), 1);
      getOrientation();
      Stop = new Checkbox("Stop after fire");
      Input.add(new Label("Name",Label.RIGHT));
      Input.add(NameField);
      Input.add(new Label("Priority",Label.RIGHT));
      Input.add(PriorityField);
      Input.add(new Label(""));
      Input.add(Stop);
      Input.add(new Label("Orientation",Label.RIGHT));
      Input.add(Orientation);

      this.add("Center", Input);

      Buttons = new Panel();
      Buttons.setLayout(new FlowLayout());
      Buttons.add(new Button("Ok"));
      Buttons.add(new Button("Cancel"));
      this.add("South",Buttons);
      Stop.disable();
      this.pack();
      this.resize(250,180);
      move(parent.location().x + 50, parent.location().y + 50);
      this.setResizable(false);
   }

   void getOrientation() {
      switch (actual.getOrientation()) {
      case transition.ORIENTATION_VERTICAL :
         Orientation.select("Vertical");
         break;
      case transition.ORIENTATION_HORIZONTAL :
         Orientation.select("Horizontal");
         break;
      case transition.ORIENTATION_ALL :
         Orientation.select("Square");
         break;
      case transition.ORIENTATION_DIAGONAL1 :
         Orientation.select("Diagonal 1");
         break;
      case transition.ORIENTATION_DIAGONAL2 :
         Orientation.select("Diagonal 2");
         break;
      }
   }

   void setOrientation() {
      if (Orientation.getSelectedItem().equals("Vertical"))
         actual.setOrientation(transition.ORIENTATION_VERTICAL);
      if (Orientation.getSelectedItem().equals("Horizontal"))
         actual.setOrientation(transition.ORIENTATION_HORIZONTAL);
      if (Orientation.getSelectedItem().equals("Square"))
         actual.setOrientation(transition.ORIENTATION_ALL);
      if (Orientation.getSelectedItem().equals("Diagonal 1"))
         actual.setOrientation(transition.ORIENTATION_DIAGONAL1);
      if (Orientation.getSelectedItem().equals("Diagonal 2"))
         actual.setOrientation(transition.ORIENTATION_DIAGONAL2);
   }

   public boolean handleEvent(Event e) {
      switch (e.id) {
      case Event.ACTION_EVENT:
        if (e.target instanceof Button) {
          if (((String)e.arg).equals("Ok")) {
              actual.setName(NameField.getText());
              try {
                  actual.setPriority(Integer.parseInt(PriorityField.getText()));
              }
              catch (NumberFormatException n) {}
              setOrientation();
              this.hide();
              return true;
          }
          if (((String)e.arg).equals("Cancel")) {
              this.hide();
              return true;
          }
          return false;
        }
      default:
          return super.handleEvent(e);
      }
   }

   public void setTransition(transition t) {
      actual = t;
      NameField.setText(actual.getName());
      PriorityField.setText(Integer.toString(actual.getPriority()));
      getOrientation();
   }

} /* class TransitionDialog */
