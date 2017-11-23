import java.awt.*;
import node;

public class arrow {

        private static final int        Cnothing = 0;
        private static final int        Ctransition = 1;
        private static final int        Cnode = 2;
        private static final int        Cedge = 3;

        int     typeS;
        int     typeE;

        // xy coordinates of the object from where the arrow comes
        int     x, y;

        Object  objectS;
        Object  objectE;

        boolean visible;

        public arrow() {
                visible = false;
        }

        public void setStartObject(Object o) {
                objectS = o;
/*                if (o instanceof transition) {
                        typeS = Ctransition;
                        transition t = (transition) o;
                        x = t.getX();
                        y = t.getY();
                }
                else if (o instanceof node) {
                        typeS = Cnode;
                        node n = (node) o;
                        x = n.getX();
                        y = n.getY();
                }
                else if (o instanceof edge) {
                        typeS = edge;
                }
  */              
        }

        public void draw(Graphics g, int x, int y) {               
                drawArrow(g, getStartX(x, y), getStartY(x, y), x, y);        
        }

        public void draw(Graphics g) {
                drawArrow(g, getStartX(100, 100), getStartY(100, 100), 100, 100);        
        }

        public void setEndObject(Object o) {
                objectE = o;
        }

        private int getStartX(int x, int y) {
                if (objectS instanceof node) {
                        node n = (node) objectS;
                        double dx = (double) (n.getX() - x);
                        double dy = (double) (n.getY() - y);
                        double length = Math.sqrt(dx * dx + dy * dy);
                        double xAdd = (double) (node.getRadius() + 1) * dx / length;
                        return (n.getX() - (int) xAdd);
                }
                else
                        return 0;
        }

        private int getStartY(int x, int y) {
                if (objectS instanceof node) {
                        node n = (node) objectS;
                        double dx = (double) (n.getX() - x);
                        double dy = (double) (n.getY() - y);
                        double length = Math.sqrt(dx * dx + dy * dy);
                        double yAdd = (double) (node.getRadius() + 1) * dy / length;                                                                                                                                                                                
                        return (n.getY() - (int) yAdd);
                }
                else
                        return 0;
        }


        public static void drawArrow(Graphics g, int xS, int yS, int xE, int yE) {
                // variables to store the x, y koordinates of the polygon
                // forming the head of the arrow.
                int xP[] = new int[3];
                int yP[] = new int[3];

                if (xE < 0.0)
                        xP[0] = (int) (xE - 1.0);
                else
                        xP[0] = (int) (xE + 1.0);
                if (yE < 0.0)
                        yP[0] = (int) (yE - 1.0);
                else
                        yP[0] = (int) (yE + 1.0);

                double  dx = xS - xE;
                double  dy = yS - yE;
                double  length = Math.sqrt(dx * dx + dy * dy);

                double  xAdd = 9.0 * dx / length;
                double  yAdd = 9.0 * dy / length;

                xP[1] = (int) Math.round (xE + xAdd - (yAdd / 3.0));
                yP[1] = (int) Math.round (yE + yAdd + (xAdd / 3.0));
                xP[2] = (int) Math.round (xE + xAdd + (yAdd / 3.0));
                yP[2] = (int) Math.round (yE + yAdd - (xAdd / 3.0));

                g.drawLine(xS, yS, xP[0], yP[0]);
                g.fillPolygon(xP, yP, 3);
        }
}
                                                        
