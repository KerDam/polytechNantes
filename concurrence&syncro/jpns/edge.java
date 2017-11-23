import java.awt.*;
import java.util.*;
import java.lang.*;

public class edge implements Cloneable {

    public static final int NOTHING = 0;
    public static final int TRANSITION = 1;
    public static final int NODE = 2;

    // type and index the edge comes from and goes to
    private int tFrom;
    private int iFrom;

    private int tTo;
    private int iTo;

    // NEU (25.03.1997 MK)
    private boolean negated;
    private int weight;

    private Polygon points;

    private Color color;

    public edge(int tFrom, int iFrom, int tTo, int iTo, Polygon points) {
        color = Color.black;
        this.tFrom = tFrom;
        this.iFrom = iFrom;
        this.tTo = tTo;
        this.iTo = iTo;

        // NEU (25.03.1997 MK)
        this.negated = false;
        this.weight = 1;     // new (15.5.97 JW)
                             // sehr sinnvoll, gewicht auf null zu setzen!!

        if (points != null) {
            this.points = new Polygon();
            this.points.addPoint(0, 0);
            for (int i = 0; i < points.npoints; i++)
                this.points.addPoint(points.xpoints[i], points.ypoints[i]);
        }
        else
            this.points = null;
    }

    // NEU (25.03.1997 MK)
    public boolean isNegated() {
        return negated;
    }

    // neu (15.4.97 JW)
    public void setNegated(boolean n) {
        negated = n;
    }

    // NEU (25.03.1997 MK)
    public int getWeight() {
        return weight;
    }

    // NEU (25.03.1997 MK)
    public void setWeight(int weight) {
        this.weight = weight;
    }


    public int getTFrom() {
        return tFrom;
    }

    public void setTFrom(int tFrom) {
        this.tFrom = tFrom;
    }

    public int getTTo() {
        return tTo;
    }

    public void setTTo(int tTo) {
        this.tTo = tTo;
    }

    public int getIFrom() {
        return iFrom;
    }

    public void setIFrom(int iFrom) {
        this.iFrom = iFrom;
    }

    public int getITo() {
        return iTo;
    }

    public void setITo(int iTo) {
        this.iTo = iTo;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Polygon getPoints() {
        return points;
    }

    public void setPoints(Polygon p) {
        points = p;
    }

    public int getNumberOfPoints() {
        if (points == null)
            return 0;
        return (points.npoints - 1);
    }

    public Point getPoint(int i) {
        if (points == null)
            return null;
        Point p = new Point(points.xpoints[i + 1],
                            points.ypoints[i + 1]);
        return p;
    }

	// neu (04.05.1997 MK)
	public Point getWeightPosition(pn PN) {
		Point pFrom, pTo;
		if (points == null) {
			pFrom = getXYFrom(PN);
			pTo = getXYTo(PN);
		}
		else {
			pFrom = new Point(0, 0);
			pTo = new Point(0, 0);
			pFrom.x = points.xpoints[0];
			pFrom.y = points.ypoints[0];
			pTo.x = points.xpoints[1];
			pTo.y = points.ypoints[1];
		}
        double dx = (double) (pTo.x - pFrom.x);
        double dy = (double) (pTo.y - pFrom.y);
        double length = Math.sqrt(dx * dx + dy * dy);
        int xAdd = (int) (dy * 10.0 / length);
        int yAdd = (int) (dx * 10.0 / length);

        Point res = new Point(0, 0);
        if (dy <= 0) {
	        if (dx <= 0) {
		        res.y = pFrom.y + (int) (dy / 2.0) + yAdd;
                res.x = pFrom.x + (int) (dx / 2.0) - xAdd;
            }
            else {
                res.y = pFrom.y + (int) (dy / 2.0) - yAdd;
                res.x = pFrom.x + (int) (dx / 2.0) + xAdd;
            }
		}
        else {
			if (dx <= 0) {
				res.y = pFrom.y + (int) (dy / 2.0) + yAdd;
                res.x = pFrom.x + (int) (dx / 2.0) - xAdd;
			}
            else {
                res.y = pFrom.y + (int) (dy / 2.0) - yAdd;
                res.x = pFrom.x + (int) (dx / 2.0) + xAdd;
            }
		}
		return res;
	}

    // neu (15.4.97 JW)
    public boolean pointingTo(pn p, transition t){
     int ind;
     ind =p.getTransIndex(t);
     boolean ret = false;

     if ((getTTo() == TRANSITION) && (getITo() == ind)) ret = true;

     return ret;
    }

    public void setLastPoint(int x, int y) {
        if (points == null) {
            int[] xpoints = new int[2];
            int[] ypoints = new int[2];
            xpoints[0] = ypoints[0] = 0;
            xpoints[1] = x;
            ypoints[1] = y;
            points = new Polygon(xpoints, ypoints, 2);
        }
        else {
            points.xpoints[points.npoints - 1] = x;
            points.ypoints[points.npoints - 1] = y;
        }
    }

    public void clearAllPoints() {
        points = null;
    }

    public void clearLastPoint() {
        if (points.npoints <= 2)
            points = null;
        else {

            int[] newX = new int[points.npoints];
            int[] newY = new int[points.npoints];

            for (int i = 0; i < points.npoints - 1; i++) {
                newX[i] = points.xpoints[i];
                newY[i] = points.ypoints[i];
            }
            Polygon p = new Polygon(newX, newY, points.npoints - 1);
            points = p;
        }
    }

    public void addPoint(int x, int y) {
        if (points == null) {
            points = new Polygon();
            points.addPoint(0,0);
        }
        points.addPoint(x, y);
    }

    public double distance (double px, double py, pn PN) {
        Point p1 = getXYFrom(PN);
        Point p2 = getXYTo(PN);
        if (points == null) {
            return distancePointToLine(px, py, p1.x, p1.y, p2.x, p2.y);
        }
        else if (points.npoints > 1) {
            double d1 = distancePointToLine(px, py,
                                            p1.x, p1.y,
                                            points.xpoints[1],
                                            points.ypoints[1]);
            double d2 = distancePointToLine(px, py,
                                            p2.x, p2.y,
                                            points.xpoints[points.npoints - 1],
                                            points.ypoints[points.npoints - 1]);
            double d3 = Integer.MAX_VALUE;
            if (points.npoints > 2) {
                for (int i = 1; i < points.npoints - 1; i++) {
                    double d3new = distancePointToLine(px, py,
                                                       points.xpoints[i],
                                                       points.ypoints[i],
                                                       points.xpoints[i+1],
                                                       points.ypoints[i+1]);
                    if (d3new < d3)
                        d3 = d3new;
                }
            }

            if (d1 > d2)
                d1 = d2;

            if (d1 > d3)
                d1 = d3;

            return d1;
        }
        else
            return Integer.MAX_VALUE;
}

    private double distancePointToLine (double px, double py, double x1, double y1, double x2, double y2) {
        double ax, ay, bx, by;

        ax = x1;
        ay = y1;

        bx = x2 - x1;
        by = y2 - y1;

        // length of vector b
        double lb = Math.sqrt(bx * bx + by * by);

        double t0 = ((px - ax) * bx + (py - ay) * by) / (lb * lb);

        double x0x = ax + t0 * bx;
        double x0y = ay + t0 * by;

        if (x0x < x1 && x0x < x2 ||
            x0x > x1 && x0x > x2 ||
            x0y < y1 && x0y < y2 ||
            x0y > y1 && x0y > y2) {
            double dH, dT;
            dH = distancePointToPoint(px, py, x1, y1);
            dT = distancePointToPoint(px, py, x2, y2);
            if (dH < dT)
                return dH;
            return dT;
        }

        double tmpx = (by * (px - ax)) - (bx * (py - ay));
        double tmpy = (bx * (py - ay)) - (by * (px - ax));

        double ltmp = Math.sqrt(tmpx * tmpx + tmpy * tmpy);

        double l = ltmp / lb;

        return l;
    }

    private double distancePointToPoint(double px1, double py1, double px2, double py2) {
        double dx = (px1 - px2);
        double dy = (py1 - py2);
        double dist = Math.sqrt (dx*dx + dy*dy);
        return dist;
    }


/*
    public Point getXYFrom(pn PN) {
        int xF, yF, xT, yT;
        Point p;
        switch(tFrom) {
            case TRANSITION:
                transition t = PN.getTransition(iFrom);
                p = new Point(t.getX(), t.getY());
                return p;
            case NODE:
                node n = PN.getNode(iFrom);
                xF = n.getX();
                yF = n.getY();
                break;
            default:
                p = new Point(0, 0);
                return p;
        }
        if (points == null) {
            switch(tTo) {
                case NOTHING:
                    p = new Point(xF, yF);
                    return p;
                case TRANSITION:
                    transition t = PN.getTransition(iTo);
                    xT = t.getX();
                    yT = t.getY();
                    break;
                case NODE:
                    node n = PN.getNode(iTo);
                    xT = n.getX();
                    yT = n.getY();
                    break;
                default:
                    xT = yT = 0;
            }
        }
        else {
            xT = points.xpoints[1];
            yT = points.ypoints[1];
        }
        double dx = (double) (xT - xF);
        double dy = (double) (yT - yF);
        double length = Math.sqrt(dx * dx + dy * dy);
        int xAdd = (int) Math.round((double) (node.getRadius() + 1) * dx / length);
        int yAdd = (int) Math.round((double) (node.getRadius() + 1) * dy / length);
        p = new Point(xF + xAdd, yF + yAdd);
        return p;
    }


*/


   public Point getXYFrom(pn PN) {
 	return getXYFrom(PN, 0.0);
    }
 

    // NEU (geringer Unterschied zu 'getXYFrom(pn PN)') (25.03.1997 MK)
    // changed (26.06.1997 MK)
   public Point getXYFrom(pn PN, double distance) {
        int xF, yF, xT, yT;
        Point p;
        switch(tFrom) {
            case TRANSITION:
                transition t = PN.getTransition(iFrom);
                if (t.getOrientation() == transition.ORIENTATION_ALL) {
                    distance += 20.0;   
                }
                xF = t.getX();
                yF = t.getY();
                break;
            case NODE:
                node n = PN.getNode(iFrom);
                xF = n.getX();
                yF = n.getY();
                break;
            default:
                p = new Point(0, 0);
                return p;
        }
        if (points == null) {
            switch(tTo) {
                case NOTHING:
                    p = new Point(xF, yF);
                    return p;
                case TRANSITION:
                    transition t = PN.getTransition(iTo);
                    xT = t.getX();
                    yT = t.getY();
                    break;
                case NODE:
                    node n = PN.getNode(iTo);
                    xT = n.getX();
                    yT = n.getY();
                    break;
                default:
                    xT = yT = 0;
            }
        }
        else {
            xT = points.xpoints[1];
            yT = points.ypoints[1];
        }
        double dx = (double) (xT - xF);
        double dy = (double) (yT - yF);
        double length = Math.sqrt(dx * dx + dy * dy);
        int xAdd, yAdd;
        xAdd = yAdd = 0;
        if (tFrom == TRANSITION) {
            xAdd = (int) Math.round((1 + distance) * dx / length);
            yAdd = (int) Math.round((1 + distance) * dy / length);
        }
        else if (tFrom == NODE) {
            xAdd = (int) Math.round((double) (node.getRadius() + 1 + distance) * dx / length);
            yAdd = (int) Math.round((double) (node.getRadius() + 1 + distance) * dy / length);
        }
        p = new Point(xF + xAdd, yF + yAdd);
        return p;
    }


/*    public Point getXYTo(pn PN) {
        int xF, yF, xT, yT;
        Point p;
        switch(tTo) {
            case NOTHING:
                p = new Point(points.xpoints[points.npoints-1],
                              points.ypoints[points.npoints-1]);
                return p;
            case TRANSITION:
                transition t = PN.getTransition(iTo);
                p = new Point(t.getX(), t.getY());
                return p;
            case NODE:
                node n = PN.getNode(iTo);
                xT = n.getX();
                yT = n.getY();
                break;
            default:
                p = new Point(0, 0);
                return p;
        }
        if (points == null) {
            switch(tFrom) {
                case TRANSITION:
                    transition t = PN.getTransition(iFrom);
                    xF = t.getX();
                    yF = t.getY();
                    break;
                case NODE:
                    node n = PN.getNode(iFrom);
                    xF = n.getX();
                    yF = n.getY();
                    break;
                default:
                    xF = yF = 0;
            }
        }
        else {
            xF = points.xpoints[points.npoints - 1];
            yF = points.ypoints[points.npoints - 1];
        }
        double dx = (double) (xT - xF);
        double dy = (double) (yT - yF);
        double length = Math.sqrt(dx * dx + dy * dy);
        int xAdd = (int) Math.round((double) (node.getRadius() + 1) * dx / length);
        int yAdd = (int) Math.round((double) (node.getRadius() + 1) * dy / length);
        p = new Point(xT - xAdd, yT - yAdd);
        return p;
    }

*/

   public Point getXYTo(pn PN) {
  	return getXYTo(PN, 0.0);
    }


	// neu (27.04.1997 MK)
	// changed (26.06.1997 MK)
    public Point getXYTo(pn PN, double distance) {
        int xF, yF, xT, yT;
        Point p;
        switch(tTo) {
            case TRANSITION:
                transition t = PN.getTransition(iTo);
		if (t.getOrientation() == transition.ORIENTATION_ALL) {
                    distance += 20.0;   
                }
		xT = t.getX();
		yT = t.getY();
		break;
            case NODE:
                node n = PN.getNode(iTo);
                xT = n.getX();
                yT = n.getY();
                break;
            case NOTHING:
                xT = points.xpoints[points.npoints-1];
                yT = points.ypoints[points.npoints-1];
                p = new Point(xT, yT);
                return p;
 //               break;
            default:
                p = new Point(0, 0);
                return p;
        }
        if (points == null) {
            switch(tFrom) {
		case NOTHING:
		    p = new Point(xT, yT);
		    return p;
                case TRANSITION:
                    transition t = PN.getTransition(iFrom);
                    xF = t.getX();
                    yF = t.getY();
                    break;
                case NODE:
                    node n = PN.getNode(iFrom);
                    xF = n.getX();
                    yF = n.getY();
                    break;
                default:
                    xF = yF = 0;
            }
        }
        else {
            xF = points.xpoints[points.npoints - 1];
            yF = points.ypoints[points.npoints - 1];
        }
        double dx = (double) (xT - xF);
        double dy = (double) (yT - yF);
        double length = Math.sqrt(dx * dx + dy * dy);
		int xAdd, yAdd;
		xAdd = yAdd = 0;
		if (tTo == TRANSITION) {
			xAdd = (int) Math.round ((1 + distance) * dx / length);
			yAdd = (int) Math.round ((1 + distance) * dy / length);

		}
		else {
			xAdd = (int) Math.round((double) (node.getRadius() + 1 + distance) * dx / length);
			yAdd = (int) Math.round((double) (node.getRadius() + 1 + distance) * dy / length);
		}
        p = new Point(xT - xAdd, yT - yAdd);
        return p;
    }


    public boolean adjust(pn PN, int index, int type) {
        boolean ret = false;

        if (type == tFrom) {
            if (index == iFrom)
                ret = true;
            else if (iFrom > index)
                iFrom--;
        }
        if (type == tTo) {
            if (index == iTo)
                ret = true;
            else if (iTo > index)
                iTo--;
        }
        return ret;
    }

    public synchronized Object clone() {
        Polygon copyPoly = null;
        if (points != null) {
            copyPoly = new Polygon();
            for (int i = 1; i < points.npoints; i++) {
                copyPoly.addPoint(points.xpoints[i], points.ypoints[i]);
            }
        }
        edge e = new edge(tFrom, iFrom, tTo, iTo, copyPoly);
        e.setColor(color);
        e.setNegated(negated);
        e.setWeight(weight);
        return e;
    }

}
