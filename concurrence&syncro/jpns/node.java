import java.util.*;
import java.lang.*;
import java.awt.*;
import pn;


public class node implements Cloneable {

  private static final int      radius = 15;
  private Color    color;

  String name;
  int items;
  int x;
  int y;
  int xName;
  int yName;


   public node() {
    this.name = "";
    this.items = 0;
    this.x = 0;
    this.y = 0;
    this.xName = 0;
    this.yName = 0;
    color = Color.black;
   }

   public static int getRadius() {
    return radius;
   }

   public void setColor(Color col) {
    color = col;
   }

   public Color getColor() {
    return color;
   }

//   public int getRGBColor() {
//    return color.getRGB();
//   }

   public int getItems() {
    return items;
   }


   public int getX() {
    return x;
   }

   public int getY() {
    return y;
   }

   public String getName() {
    return name;
   }

   public void setName(String name) {
    /*System.out.print("Renamed NODE ");
    System.out.print(this.name);
    System.out.print(" to ");
    System.out.print(name);
    System.out.println(" !");*/
    this.name = name;
   }

   // neu (04.05.1997 MK)
   public Point getNamePosition() {
	   Point res = new Point (xName, yName);
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

   public void setItems(int items) {
    /*System.out.print("Set items in NODE ");
    System.out.print(name);
    System.out.print(" to ");
    System.out.print(items);
    System.out.println(" !");*/
    this.items = items;
   }

   public void setX(int x) {
    this.x = x;
   }

   public void setY(int y) {
     this.y = y;
   }

   public void decItems(int w) {
    if (items >= w) {
       this.items = this.items - w;
    }
   }

   public void incItems(int w) {
    this.items = this.items + w;
   }

   // neu (15.4.97 JW)
   public void decItems() {
    if (items > 0) {
       this.items--;
    }
   }

   public void incItems() {
    this.items++;
   }


   public double distance(double x, double y){
    double dx = (this.x - x);
    double dy = (this.y - y);
    double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;
   }

   public boolean checkNode(int w){
    if (items >= w) return true;
     else return false;
   }

   public synchronized Object clone() {
    node n = new node();
    n.setName(name);
    n.setItems(items);
    n.setColor(color);
    n.setX(x);
    n.setY(y);
    n.setXName(xName);
    n.setYName(yName);
    return n;
   }

 }
