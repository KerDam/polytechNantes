import java.util.*;
import java.lang.*;
import java.awt.*;
import pn;
import edge;


  public class transition implements Cloneable {

    public static final int ORIENTATION_VERTICAL = 1;
    public static final int ORIENTATION_DIAGONAL1 = 2;
    public static final int ORIENTATION_HORIZONTAL = 3;
    public static final int ORIENTATION_DIAGONAL2 = 4;
    public static final int ORIENTATION_ALL = 5;

    private int orientation;

    public static final int height = 30;
    public static final int width = 3;

    int x;
    int y;
    int priority;
    String name;
    int xName;
    int yName;

    public boolean highlight;

    public double ranval;

   public transition() {
       this.x = 0;
       this.y = 0;
       this.priority = 0;
       this.name = "";
       this.xName = 0;
       this.yName = 0;
       this.orientation = ORIENTATION_VERTICAL;
       highlight = false;
       ranval = 0;
    }


    public void setName(String name) {
       /*System.out.print("Renamed TRANSITION ");
       System.out.print(this.name);
       System.out.print(" to ");
       System.out.print(name);
       System.out.println(" !"); */
       this.name = name;
    }

    public void setPriority(int p){
       /*System.out.print("Set priority in TRANSITION ");
       System.out.print(this.name);
       System.out.print(" to ");
       System.out.print(p);
       System.out.println(" !");*/
       this.priority = p;
    }

    public void setRan(double ran){      //NEW 23.5.97 AH
     ranval = ran;
    }

    public double getRan(){             //NEW 22.5.97 AH
     return ranval;
    }


    public String getName() {
     return name;
    }

    public int getPriority(){
     return this.priority;
    }

    public void setX(int x) {
     this.x = x;
    }
    public void setY(int y) {
     this.y = y;
    }
    public int getX() {
     return x;
    }
    public int getY() {
     return y;
    }
    public static int getHeight() {
     return height;
    }
    public static int getWidth() {
     return width;
    }

    public int getOrientation() {
        return orientation;
    }

    // neu (04.05.1997 MK)
    public Point getNamePosition() {
        Point res = new Point(xName, yName);
        return res;
    }

    public int getXName() {
        return xName;
    }

    public int getYName() {
        return yName;
    }

    public void setXName(int x) {
        xName = x;
    }

    public void setYName(int y) {
        yName = y;
    }

    public void setOrientation(int o) {
        orientation = o;
    }

    public void cycleOrientation() {
        switch (orientation) {
        case ORIENTATION_VERTICAL:
            orientation = ORIENTATION_DIAGONAL1;
            break;
        case ORIENTATION_DIAGONAL1:
            orientation = ORIENTATION_HORIZONTAL;
            break;
        case ORIENTATION_HORIZONTAL:
            orientation = ORIENTATION_DIAGONAL2;
            break;
        case ORIENTATION_DIAGONAL2:
            orientation = ORIENTATION_ALL;
            break;
        case ORIENTATION_ALL:
            orientation = ORIENTATION_VERTICAL;
            break;
        }
    }

  public boolean canFire(pn p, boolean priorEnabled){        //NEW 23.5.97 AH
     int i;
     boolean canfire = true;
     boolean nonode = true;
     boolean higherprio = false;
     boolean negatedEdge = false;
     int nodeindex;

     int index = p.transitions.indexOf(this);

    for (i=0; i<p.edges.size(); i++){
      edge e = ((edge) p.edges.elementAt(i));
      if (e.isNegated()) negatedEdge = true;


      if (((e.getTFrom() == edge.TRANSITION) && (e.getIFrom() == index))
           || ((e.getTTo() == edge.TRANSITION) && (e.getITo() == index))) {
        nonode = false;
        // System.out.println("4");
        if (e.getTFrom() == edge.NODE) {
            if (negatedEdge)  {
                int ind = e.getIFrom();
                node z = ((node) p.nodes.elementAt(ind));

                if (z.getItems() > 0) { canfire = false;
                                        break;
                                       }          //leave **-for-loop
                Vector testTransition = new Vector();                   //
                testTransition.addElement(this);                        //
                if (p.getConnectedItems(testTransition) < 1) {          // neu
                     canfire = false;                                   // 11.5.97
                     break;                                             // JW
                }                                                       //
            }

            if (priorEnabled) {
                for (int k = 0; k < p.edges.size(); k++){
                    edge f = (edge) p.edges.elementAt(k);
                    if ((f.getTTo() == edge.TRANSITION) && (index != f.getITo())
                        && (p.getTransition(f.getITo()).priority > this.priority)
                        && (p.getNode(f.getIFrom()).checkNode(f.getWeight())))
                            higherprio = true;
                }
            }


            if (negatedEdge == false){ // System.out.println("7");
                nodeindex = e.getIFrom();
                int w = e.getWeight();
                canfire = (((node) p.nodes.elementAt(nodeindex)).checkNode(w))
                        /*&& (checkTrans(p))*/;
                if (canfire == false) break; //leave **-for-loop
            }
        }
      }
      negatedEdge = false;
    }
    if (nonode == true) canfire = false;
    if (priorEnabled == true) {
      if (higherprio == true) canfire = false;}


    return canfire;
 }


   public boolean fire(pn p, boolean priorEnabled){          //NEW 23.4.97 AH
    int i;
    int nodeindexFrom;
    int nodeindexTo;
    int index;
    boolean ret = false;
    int w;


    index = p.transitions.indexOf(this);

    if (canFire(p, priorEnabled) == true)
    {
     for (i=0; i<p.edges.size(); i++){
       edge e = (edge) p.edges.elementAt(i);
     if (((e.getTFrom() == edge.TRANSITION) && (e.getIFrom() == index))
         || ((e.getTTo() == edge.TRANSITION) && (e.getITo() == index)))
      {

        if (e.getTFrom() == edge.NODE)
        {  w = e.getWeight();
           nodeindexFrom = ((edge) p.edges.elementAt(i)).getIFrom();
          ((node) p.nodes.elementAt(nodeindexFrom)).decItems(w);
        }
       if (e.getTTo() == edge.NODE)
         {
           w = e.getWeight();
           nodeindexTo = ((edge) p.edges.elementAt(i)).getITo();
           if (e.isNegated()) ((node) p.nodes.elementAt(nodeindexTo)).decItems(1);
            else            ((node) p.nodes.elementAt(nodeindexTo)).incItems(w);
         }
      }
     }
     ret = true;
     /*System.out.print("TRANSITION ");
     System.out.print(this.name);
     System.out.println(" fired ! "); */
     }
    else {/*System.out.print("TRANSITION ");
          System.out.print(this.name);
          System.out.println(" cannot fire !");*/}

    return ret;

 }

   public boolean checkTrans(pn p){
    int i;
    boolean ret = false;
    int index = p.transitions.indexOf(this);

    for (i=0; i< p.edges.size(); i++){
     edge e = (edge) p.edges.elementAt(i);
     if ((e.getTTo() == edge.NODE) && (e.getIFrom() == index) && (e.getTFrom() == edge.TRANSITION))
      ret = true;
    }
    return ret;
   }


   public double distance(double x, double y){
    double dist;
    double dx;
    double dy;
    dx = (this.x - x);
    dy = (this.y - y);
    dist = Math.sqrt(dx*dx + dy*dy);
    return dist;
   }

   public synchronized Object clone() {
    transition t = new transition();
    t.setName(name);
    t.setPriority(priority);
    t.setX(x);
    t.setY(y);
    t.setOrientation(orientation);
    t.setXName(xName);
    t.setYName(yName);
    t.setRan(ranval);
    return t;
   }

   public void randomize(){          //NEW 22.5.97 AH

    Random ran = new Random();
    this.ranval = Math.random();
   }

   public boolean equals(transition t){
     boolean isequal = false;

     if ( (x == t.x) && (y == t.y) && (priority == t.priority) && (name == t.name)
          && (xName == t.xName) && (yName == t.yName) && (orientation == t.orientation)
          && (highlight == t.highlight) && (ranval == t.ranval))
        isequal = true;

     return isequal;
   }
 }
