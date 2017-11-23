import java.lang.*;
import java.util.*;
import java.awt.*;
import edge;

public class pn implements Cloneable {

  Vector nodes;
  Vector transitions;
  Vector edges;
  String name;
  int stepCount;


  public pn() {
    this.nodes = new Vector();
    this.transitions = new Vector();
    this.edges = new Vector();
    this.name = "PN";
    this.stepCount = 0;
    /*System.out.print("Created Petri-Net ");
    System.out.print(name);
    System.out.println(" !");*/
  }

  public void setName(String name){
   /*System.out.print("Renamed Petri-Net ");
   System.out.print(this.name);
   System.out.print(" to ");
   System.out.print(name);
   System.out.println(" !");*/
   this.name = name;
  }

  public int getIndexOf(node n) {
      return nodes.indexOf(n);
  }

  public int getIndexOf(transition t) {
      return transitions.indexOf(t);
  }

  public int getIndexOf(edge e) {
      return edges.indexOf(e);
  }

  public boolean equal(node n1, node n2) {
      if (n1 == null || n2 == null)
          return false;
      if (nodes.indexOf(n1) == nodes.indexOf(n2))
          return true;
      return false;
  }

  public boolean equal(transition t1, transition t2) {
      if (t1 == null || t2 == null)
          return false;
      if(transitions.indexOf(t1) == transitions.indexOf(t2))
          return true;
      return false;
  }

  public boolean equal(edge e1, edge e2) {
      if (e1 == null || e2 == null)
          return false;
      if(edges.indexOf(e1) == edges.indexOf(e2))
          return true;
      return false;
  }

  public String getName(){
   return name;
  }

  public int numberOfNodes() {
   return nodes.size();
  }

  public int numberOfTransitions() {
   return transitions.size();
  }

  public int numberOfEdges() {
   return edges.size();
  }


  public node getNode(int i) {
   node n;
   n = (node) nodes.elementAt(i);
   return n;
  }

  public transition getTransition(int i) {
   transition t;
   t = (transition) transitions.elementAt(i);
   return t;
  }

  public edge getEdge(int i) {
   edge e;
   e = (edge) edges.elementAt(i);
   return e;
  }

  public int getTransIndex(transition t){
   int i;
   int ret = -1;

   for (i=0; i< numberOfTransitions(); i++){
    if ( (transition) transitions.elementAt(i) == t) {ret = i;
                                                     break;}
   }
   return ret;
  }

  public int getNodeIndex(node n){
   int i;
   int ret = -1;

   for (i=0; i< numberOfNodes(); i++){
    if ( (node) nodes.elementAt(i) == n) {ret = i;
                                          break;}
   }
   return ret;
  }

/*
  public node getClosestNode(int x, int y) {
   node    n, cn;
   double  d;
   double  cd = Integer.MAX_VALUE;
   if (this.numberOfNodes() == 0)
    return null;
   else
    cn = this.getNode(0);

   for (int i = 0; i < this.numberOfNodes(); i++) {
    n = this.getNode(i);
    d = n.distance((double) x, (double) y);
    if (d < cd) {
     cn = n;
     cd = d;
    }

   }
   return cn;
  }

  public transition getClosestTransition(int x, int y) {
   transition    t, ct;
   double  d;
   double  cd = Integer.MAX_VALUE;
   if (this.numberOfTransitions() == 0)
    return null;
   else
    ct = this.getTransition(0);

   for (int i = 0; i < this.numberOfTransitions(); i++) {
    t = this.getTransition(i);
    d = t.distance((double) x, (double) y);
    if (d < cd) {
     ct = t;
     cd = d;
    }

   }
   return ct;
  }

  public Object getClosestItem(int x, int y) {
  Object o;
  o = (Object) getClosestNode(x, y);
  if (getDistanceToClosestNode(x, y) > getDistanceToClosestTransition(x, y))
   o = (Object) getClosestTransition(x, y);
  return o;
  }

  */

  public static int getX(Object o) {
  if (o instanceof node) {
        node n = (node) o;
        return n.getX();
  }
  if (o instanceof transition) {
        transition t = (transition) o;
        return t.getX();
  }
  return 0;
  }

  public static int getY(Object o) {
  if (o instanceof node) {
        node n = (node) o;
        return n.getY();
  }
  if (o instanceof transition) {
        transition t = (transition) o;
        return t.getY();
  }
  return 0;
  }

/*
 public double getDistanceToClosestNode(int x, int y) {
 double d = Double.MAX_VALUE;
 node n = getClosestNode(x, y);
 if (n != null)
  d = n.distance(x, y);
 return d;
 }

 public double getDistanceToClosestTransition(int x, int y) {
 double d = Double.MAX_VALUE;
 transition t = getClosestTransition(x, y);
 if (t != null)
  d = t.distance(x, y);
 return d;
 }

 public double getDistanceToClosestItem(int x, int y) {
 double dd;
 double d = getDistanceToClosestNode(x, y);
 dd = getDistanceToClosestTransition(x, y);
 if (dd < d)
  d = dd;
 return d;
 }

 */

 public void unhighlightAllTransitions() {
   for (int i = 0; i < numberOfTransitions(); i++) {
      getTransition(i).highlight = false;
   }
 }

 public void addNode(){
   node n = new node();
   nodes.addElement(n);
   /*System.out.print("NODE ");
   System.out.print(n.name);
   System.out.println(" created !");*/
 }

 public boolean addNode(int x, int y) {
// if (getDistanceToClosestItem(x, y) < 45.0)
//  return false;

  node n = new node();
  n.setX(x);
  n.setY(y);
  nodes.addElement(n);
  /*System.out.print("NODE ");
  System.out.print(n.name);
  System.out.println(" created !");*/
  return true;
 }

 public boolean addNode(node n) {
//   if (getDistanceToClosestItem(n.getX(), n.getY()) < 45.0)
//    return false;
   nodes.addElement(n);
   //System.out.println("Node "+n.name+" created!");
   return true;
 }


  public void addTransition() {
   transition t = new transition();
   transitions.addElement(t);
  }

  public boolean addTransition(int x, int y) {
//   if (getDistanceToClosestItem(x, y) < 45.0)
//    return false;

   transition t = new transition();
   t.setX(x);
   t.setY(y);
   transitions.addElement(t);
   /*System.out.print("Created TRANSITION ");
   System.out.print(name);
   System.out.println(" !");*/
   return true;
  }

  public boolean addTransition(transition t) {
//   if (getDistanceToClosestItem(t.getX(), t.getY()) < 45.0)
//    return false;
   transitions.addElement(t);
   //System.out.println("transition "+t.name+" created!");
   return true;
  }

  public edge addEdge(edge e) {
      edges.addElement(e);
      addEdgeProtocol(e);
      return e;
  }

  public edge addEdge(int tFrom, int iFrom, int tTo, int iTo, Polygon points) {
      edge e = new edge(tFrom, iFrom, tTo, iTo, points);
      edges.addElement(e);
      addEdgeProtocol(e);
      return e;
  }

    public String numberToString(int n){
        String s;
        switch (n) {
            case edge.TRANSITION:
                return "TRANSITION";
            case edge.NODE:
                return "NODE";
            case edge.NOTHING:
                return "NOTHING";
            default:
                return "UNKNOWN";
        }
  }

    public void addEdgeProtocol(edge e){
       /*System.out.print("Created edge from ");
       System.out.print(numberToString(e.getTFrom()));
       System.out.print(" ");
       if (e.getTFrom() == edge.TRANSITION)
           System.out.print(getTransition(e.getIFrom()).name);
       else if (e.getTFrom() == edge.NODE)
           System.out.print(getNode(e.getIFrom()).name);
       System.out.print(" to ");
       System.out.print(numberToString(e.getTTo()));
       System.out.print(" ");
       if (e.getTTo() == edge.TRANSITION)
           System.out.print(getTransition(e.getITo()).name);
       else if (e.getTTo() == edge.NODE)
           System.out.print(getNode(e.getTTo()).name);
       System.out.println(" ! ");*/
    }


  public void removeNode(node n){
    int index = nodes.indexOf(n);
    int i;
    int k = 0;
    int todelete[] = new int[nodes.size()];


    this.nodes.removeElement(n);

    /*System.out.print("NODE ");
    System.out.print(n.name);
    System.out.println(" removed !");*/

    for (i = 0; i < edges.size(); i++) {
      edge e = (edge) edges.elementAt(i);
      if (e.adjust(this, index, edge.NODE))
      {todelete[k] = i; k++;}
    }

    for (i = k; i > 0; i--) {
      edge d = (edge) edges.elementAt(todelete[i-1]);
      edges.removeElement(d);
    }
   }

  public void removeTransition(transition t){
    int i;
    int k = 0;
    int todelete[] = new int[edges.size()];
    int index = transitions.indexOf(t);


    /*System.out.print("TRANSITION ");
    System.out.print(t.name);
    System.out.println(" removed ! (==> deleting of connected edges)");*/

    for (i = 0; i < edges.size(); i++) {
      edge e = (edge) edges.elementAt(i);
      if (e.adjust(this, index, edge.TRANSITION))
      {todelete[k] = i; k++;}
    }

    for (i = k; i > 0; i--) {
      edge d = (edge) edges.elementAt(todelete[i-1]);
      edges.removeElement(d);
    }
    this.transitions.removeElement(t);
   }

  public boolean removeEdge(edge e){
        removeEdgeProtocol(e);
        edges.removeElement(e);
        return true;
 }


    public void removeEdgeProtocol(edge e){
       /*System.out.print("Removed edge from ");
       System.out.print(numberToString(e.getTFrom()));
       System.out.print(" ");
       if (e.getTFrom() == edge.TRANSITION) System.out.print(getTransition(e.getIFrom()).name);
        else if (e.getTFrom() == edge.NODE) System.out.print(getNode(e.getIFrom()).name);
       System.out.print(" to ");
       System.out.print(numberToString(e.getTTo()));
       System.out.print(" ");
       if (e.getTTo() == edge.TRANSITION) System.out.print(getTransition(e.getITo()).name);
        else if (e.getTTo() == edge.NODE) System.out.print(getNode(e.getITo()).name);
       System.out.println(" ! ");*/
    }

  public boolean isDead(boolean priorEnabled){
   boolean isdead = true;
   int i;
    for (i=0; i<transitions.size(); i++){
     isdead = ! (((transition) transitions.elementAt(i)).canFire(this, priorEnabled));
     if (isdead == false) break;
    }
   return isdead;
  }

  public synchronized Object clone() {
    pn p = new pn();
    p.setName(name);
    p.setStepCount(stepCount);
    for (int i = 0; i < nodes.size(); i++) {
        p.addNode((node)((node)nodes.elementAt(i)).clone());
    }
    for (int i = 0; i < transitions.size(); i++) {
        p.addTransition((transition)((transition)transitions.elementAt(i)).clone());
    }
    for (int i = 0; i < edges.size(); i++) {
        p.addEdge((edge)((edge)edges.elementAt(i)).clone());
    }
    return p;
  }

  public void setStepCount(int s) {
    stepCount = s;
  }

 public int getStepCount(){
   return stepCount;          // Fuer die Bildschirmausgabe abzuaendern !!!
 }

 public Vector getAllConnectedTrans(transition t){
  int i,j;
  int ind = 0;

  edge e,f;

  Vector connected = new Vector();

  for (i=0; i < numberOfEdges(); i++){
   e = (edge) edges.elementAt(i);
   if ((e.getTTo() == edge.TRANSITION) && (e.getITo() == getTransIndex(t)))
    {ind = e.getIFrom();
     for (j=0; j< numberOfEdges(); j++){
       f = getEdge(j);
      if (( f.getTFrom() == edge.NODE) && (f.getIFrom() == ind)){
          if (! connected.contains(getTransition(f.getITo())))
          connected.addElement(getTransition(f.getITo()));
      }
     }
   }
  }
  return connected;
 }

    public Vector getAllConnectedFireableTrans(transition t, boolean prior) {
        Vector vec = getAllConnectedTrans(t);
        for (int i = 0; i < vec.size(); i++) {
            if (! ((transition)vec.elementAt(i)).canFire(this, prior)) {
                vec.removeElementAt(i);
            }
        }
        return vec;
    }


   public boolean connectedWith(node n, transition t){
    int i;
    edge e;
    boolean ret = false;

    for (i=0; i<numberOfEdges(); i++){
     e = (edge) edges.elementAt(i);
     if (e.pointingTo(this,t)){
       if (getNodeIndex(n) == e.getIFrom()) ret = true;
     }
    }
    return ret;
  }

 public int getAllWeights(Vector v){
  int i,j;
  edge e;
  transition t = new transition();
  int weights = 0;

  for (i=0; i< v.size(); i++){
    t = (transition) v.elementAt(i);
    for (j=0; j< numberOfEdges(); j++){
     e = (edge) getEdge(j);
     if (e.pointingTo(this,t)) weights = weights + e.getWeight();
    }
  }
  return weights;
 }

 public int getConnectedItems(Vector v){
  int i,j;
  int items = 0;
  transition t = new transition();
  node n = new node();
  for (i=0; i< v.size(); i++){
    t = (transition) v.elementAt(i);
   for (j=0; j<numberOfNodes(); j++){
    n = (node) nodes.elementAt(j);
    if (connectedWith(n,t)) items = items + n.getItems();
   }
  }
  return items;
 }


  public Vector multipleCanFire(Vector v, boolean priorEnabled){     //NEW 22.5.97 AH
    int i,j,size;
    transition s,t;
    boolean b;
    Vector w = new Vector();
    Vector z = new Vector();

    pn q = (pn) this.clone();

    size = v.size();

    for (i=0; i < size; i++){
     t = (transition) v.elementAt(i);
     for (j=0; j < q.numberOfTransitions(); j++){
      s = q.getTransition(j);
      if (s.equals(t)) z.addElement(s);
     }
    }

    size = z.size();

    for (i=0; i < size; i++){
      t = (transition) z.elementAt(i);
     // System.out.println("3");               //***
      b = t.fire(q, false); // System.out.println(b);
      if (b) {  // System.out.println(t.getName());
                                      w.addElement(t);
                                    }
    }
    v.removeAllElements();

    for (i=0; i < w.size(); i++){
     t = (transition) w.elementAt(i);
     for (j=0; j < numberOfTransitions(); j++){
      s = getTransition(j);
      if (s.equals(t)) v.addElement(s);
     }
    }




    return v;
  }



 // static methods

 // new 18.5.97 jw
 public static void subVectorOfVector(Vector sub,Vector vec) {
    for (int i = 0; i < sub.size(); i++) {
        vec.removeElement(sub.elementAt(i));
    }
 }

}
