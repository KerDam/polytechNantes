import java.awt.*;

public class NodeDialog extends Dialog {

   Panel Input, Buttons;
   node actual;
   TextField NameField, TokenField, BreakField;

   public NodeDialog(Frame parent, node n) {
      super(parent, "Edit Nodes", true);
      this.setLayout(new BorderLayout());

      Input = new Panel();
      Input.setLayout(new GridLayout(0,2,2,2));
      actual = n;

      this.add("North", new Label("Node"));
      NameField = new TextField(actual.getName(), 1);
      TokenField = new TextField(Integer.toString(actual.getItems()), 1);
      BreakField = new TextField(1);
      Input.add(new Label("Name",Label.RIGHT));
      Input.add(NameField);
      Input.add(new Label("Token",Label.RIGHT));
      Input.add(TokenField);
      Input.add(new Label("Breakcondition",Label.RIGHT));
      Input.add(BreakField);
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

   public boolean handleEvent(Event e) {
      switch (e.id) {
      case Event.ACTION_EVENT:
          if (((String)e.arg).equals("Ok")) {
              actual.setName(NameField.getText());
              try {
                  actual.setItems(Integer.parseInt(TokenField.getText()));
                  if (actual.getItems() < 0)
                        actual.setItems(0);
              }
              catch (NumberFormatException n) {}
              this.hide();
              return true;
          }
          if (((String)e.arg).equals("Cancel")) {
              this.hide();
              return true;
          }
          return false;
      default:
          return super.handleEvent(e);
      }
   }

   public void setNode(node n) {
      actual = n;
      NameField.setText(actual.getName());
      TokenField.setText(Integer.toString(actual.getItems()));
      BreakField.setText("");
   }


} /* class PlaceDialog */
