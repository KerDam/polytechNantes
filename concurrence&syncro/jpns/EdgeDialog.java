import java.awt.*;

public class EdgeDialog extends Dialog {

   Panel Input, Buttons;
   edge actual;
   TextField WeightField;
   Checkbox Negated;
   int WeightBackup;

   public EdgeDialog(Frame parent, edge e) {
      super(parent, "Edit Edges", false);
      this.setLayout(new BorderLayout());

      Input = new Panel();
      Input.setLayout(new GridLayout(0,2,2,2));
      actual = e;

      this.add("North", new Label("Edge"));
      WeightBackup = actual.getWeight();
      WeightField = new TextField(Integer.toString(WeightBackup), 1);
      Negated = new Checkbox("Negated");
      Negated.setState(actual.isNegated());
      Input.add(new Label(""));
      Input.add(new Label(""));
      Input.add(new Label("Weight",Label.RIGHT));
      Input.add(WeightField);
      Input.add(new Label(""));
      Input.add(Negated);

      this.add("Center", Input);

      Buttons = new Panel();
      Buttons.setLayout(new FlowLayout());
      Buttons.add(new Button("Ok"));
      Buttons.add(new Button("Cancel"));
      this.add("South",Buttons);
      this.pack();
      this.resize(250,180);
      testModifyable();
      move(parent.location().x + 50, parent.location().y + 50);
      this.setResizable(false);
   }


   public boolean handleEvent(Event e) {
      switch (e.id) {
      case Event.ACTION_EVENT:
        if (e.target instanceof Checkbox) {
          if (! Negated.getState()) {
              WeightField.setText(Integer.toString(WeightBackup));
              WeightField.enable();
          } else {
              WeightBackup = Integer.parseInt(WeightField.getText());
              WeightField.setText(Integer.toString(1));
              WeightField.disable();
          }
          return false;
        }
        if (e.target instanceof Button) {
          if (((String)e.arg).equals("Ok")) {
              try {
                  actual.setWeight(Integer.parseInt(WeightField.getText()));
              }
              catch (NumberFormatException n) {}
              actual.setNegated(Negated.getState());
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

   void testModifyable() {
      if (actual.isNegated()) {
          WeightField.setText(Integer.toString(1));
          WeightField.disable();
      } else {
          WeightField.enable();
      }
   }

   public void setEdge(edge e) {
      actual = e;
      WeightBackup = actual.getWeight();
      WeightField.setText(Integer.toString(WeightBackup));
      Negated.setState(actual.isNegated());
      testModifyable();
   }

} /* class EdgeDialog */
