import java.awt.*;
import node;
import transition;
import NodeDialog;
import Editor;


class vis extends Canvas{

    // height and width of tokens/items (in pixel)
    private static final int ItemH = 10;
    private static final int ItemW = 3;

    // different modes, mouse events are handled by
    // (e.g. what kind of item to add when the mousebutton is
    // pressed down.)
    public static final int NodeMode = 1;
    public static final int TransitionMode = 2;
    public static final int EdgeMode = 3;
    public static final int MODE_ITEM = 4;
    public static final int MODE_DELETE = 5;
    public static final int MODE_DRAG = 6;
    public static final int MODE_EDIT = 7;
    public static final int SELECTMODE = 8;

    private int     mode;

    // color modes (color or black&white)
    public static final int COLORMODE_STANDARD = 20;
    public static final int COLORMODE_BLACKANDWHITE = 21;

    private int     colorMode;

    //
    private static final int MAX_NUM_DELETE_EDGE = 255;

    private int     iDeleteNode;
    private int     iDeleteTransition;
    private int     nDeleteEdge;
    private int     iDeleteEdge[];

    //
    private node        dragingNode;
    private transition  dragingTransition;

    //
    private edge    newEdge;
    private boolean newEdgeFinished;

    //
    private Dimension   ViewDim;

    //
    private pn      PNet;

    //
    private Image   BackBuffer;

    NodeDialog NodeDial;
    TransitionDialog TransDial;
    EdgeDialog EdgeDial;

    public vis(pn PN, Dimension dim) {
        PNet = PN;
        ViewDim = new Dimension(dim);
        resize(dim);
        newEdgeFinished = true;

        colorMode = COLORMODE_STANDARD;
//      colorMode = COLORMODE_BLACKANDWHITE;

        iDeleteNode = -1;
        iDeleteTransition = -1;
        nDeleteEdge = 0;
        iDeleteEdge = new int[MAX_NUM_DELETE_EDGE];
    }

    public void setDimension(Dimension d) {
        ViewDim.height = d.height;
        ViewDim.width = d.width;
    }

    public int getMode() {
        return mode;
    }

    public boolean setMode(int mode, int autorisation) {
        if (this.mode == SELECTMODE) {
            if (autorisation == runPN.CODE) {
                this.mode = mode;
                iDeleteNode = -1;
                iDeleteTransition = -1;
                nDeleteEdge = 0;
                return true;
            }
        } else {
            this.mode = mode;
            iDeleteNode = -1;
            iDeleteTransition = -1;
            nDeleteEdge = 0;
            return true;
        }
        return false;
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }

    public void setPN(pn PN) {
        PNet = PN;
    }

    public boolean handleEvent(Event evt) {
        switch(evt.id) {
        case Event.MOUSE_DOWN:
            // right mouse button down
            if (mode == MODE_DRAG || evt.modifiers == Event.META_MASK) {
                mouseMetaDown(evt);
            }
            else {
                if (mode == SELECTMODE)
                    mouseDownSelect(evt);
                if (mode == NodeMode)
                    mouseDownNode(evt);
                else if (mode == TransitionMode)
                    mouseDownTransition(evt);
                else if (mode == EdgeMode)
                    mouseDownEdge(evt);
                else if (mode == MODE_ITEM)
                    mouseDownItem(evt);
                else if (mode == MODE_DELETE)
                    mouseDownDelete(evt);
                else if (mode == MODE_EDIT)
                    mouseDownEdit(evt);
            }
            repaint();
            break;
        case Event.MOUSE_UP:
            break;
        case Event.MOUSE_DRAG:
            if (mode == MODE_DRAG || evt.modifiers == Event.META_MASK) {
                mouseMetaDrag(evt);
            }
            else {
                if (mode == NodeMode)
                    ;
                else if (mode == TransitionMode)
                    ;
                else if (mode == EdgeMode)
                    mouseDragEdge(evt);
            }
            repaint();
            break;
        case Event.MOUSE_MOVE:
            if (mode == NodeMode)
                ;
            else if (mode == TransitionMode)
                ;
            else if (mode == EdgeMode)
                mouseMoveEdge(evt);
            else if (mode == MODE_DELETE)
                mouseMoveDelete(evt);
            repaint();
            break;
        case Event.MOUSE_EXIT:
            if (mode == EdgeMode && ! newEdgeFinished) {
                PNet.removeEdge(newEdge);
                newEdgeFinished = true;
            }
            repaint();
            return true;
        }
        return true;
    }

    private void mouseDownSelect(Event evt) {
        transition t = getTransitionAtXY(evt.x, evt.y, null);
        if ((t != null) && (t.highlight)) {
            ((Editor)getParent()).getRunStep().select(t);
        }
    }

    private void mouseDownEdit(Event evt) {
        node n = getNodeAtXY(evt.x, evt.y, null);
        transition t = getTransitionAtXY(evt.x, evt.y, null);
        edge e = getEdgeAtXY(evt.x, evt.y, null);
        if (n != null) {
            if (NodeDial == null) {
                NodeDial = new NodeDialog((Frame)getParent(), n);
            } else {
                NodeDial.setNode(n);
            }
            NodeDial.show();
            this.repaint();
            e = null;
        }
        if (t != null) {
            if (TransDial == null) {
                TransDial = new TransitionDialog((Frame)getParent(), t);
            } else {
                TransDial.setTransition(t);
            }
            TransDial.show();
            this.repaint();
            e = null;
        }
        if (e != null) {
            if (EdgeDial == null) {
                EdgeDial = new EdgeDialog((Frame)getParent(), e);
            } else {
                EdgeDial.setEdge(e);
            }
            EdgeDial.show();
            this.repaint();
        }
    }


    private void mouseMoveDelete(Event evt) {
        node n = getNodeAtXY(evt.x, evt.y, null);
        transition t = getTransitionAtXY(evt.x, evt.y, null);
        edge e = getEdgeAtXY(evt.x, evt.y, null);
        if (n != null) {
            iDeleteNode = PNet.getIndexOf(n);
            iDeleteTransition = -1;
            nDeleteEdge = 0;
            for (int i = 0; i < PNet.numberOfEdges(); i++) {
                edge ee = PNet.getEdge(i);
                if (ee.getTFrom() == edge.NODE && ee.getIFrom() == iDeleteNode ||
                    ee.getTTo() == edge.NODE && ee.getITo() == iDeleteNode) {
                    iDeleteEdge[nDeleteEdge] = i;
                    nDeleteEdge++;
                }
            }
        }
        else if (t != null) {
            iDeleteNode = -1;
            iDeleteTransition = PNet.getIndexOf(t);
            nDeleteEdge = 0;
            for (int i = 0; i < PNet.numberOfEdges(); i++) {
                edge ee = PNet.getEdge(i);
                if (ee.getTFrom() == edge.TRANSITION && ee.getIFrom() == iDeleteTransition ||
                    ee.getTTo() == edge.TRANSITION && ee.getITo() == iDeleteTransition) {
                    iDeleteEdge[nDeleteEdge] = i;
                    nDeleteEdge++;
                }
            }
        }
        else if (e != null) {
            iDeleteNode = -1;
            iDeleteTransition = -1;
            nDeleteEdge = 1;
            iDeleteEdge[0] = PNet.getIndexOf(e);
        }
        else {
            iDeleteNode = -1;
            iDeleteTransition = -1;
            nDeleteEdge = 0;
        }
    }

    private void mouseDownDelete(Event evt) {
        node n = getNodeAtXY(evt.x, evt.y, null);
        transition t = getTransitionAtXY(evt.x, evt.y, null);
        edge e = getEdgeAtXY(evt.x, evt.y, null);
        if (n != null) {
            iDeleteNode = PNet.getIndexOf(n);
            iDeleteTransition = -1;
            nDeleteEdge = 0;
            for (int i = 0; i < PNet.numberOfEdges(); i++) {
                edge ee = PNet.getEdge(i);
                if (ee.getTFrom() == edge.NODE && ee.getIFrom() == iDeleteNode ||
                    ee.getTTo() == edge.NODE && ee.getITo() == iDeleteNode) {
                    PNet.removeEdge(ee);
                }
            }
            PNet.removeNode(n);
            iDeleteNode = -1;
        }
        else if (t != null) {
            iDeleteNode = -1;
            iDeleteTransition = PNet.getIndexOf(t);
            nDeleteEdge = 0;
            for (int i = 0; i < PNet.numberOfEdges(); i++) {
                edge ee = PNet.getEdge(i);
                if (ee.getTFrom() == edge.TRANSITION && ee.getIFrom() == iDeleteTransition ||
                    ee.getTTo() == edge.TRANSITION && ee.getITo() == iDeleteTransition) {
                    PNet.removeEdge(ee);
                }
            }
            PNet.removeTransition(t);
            iDeleteTransition = -1;
        }
        else if (e != null) {
            iDeleteNode = -1;
            iDeleteTransition = -1;
            nDeleteEdge = 0;
            PNet.removeEdge(e);
        }
        else {
            iDeleteNode = -1;
            iDeleteTransition = -1;
            nDeleteEdge = 0;
        }
    }

    private void mouseMetaDown(Event evt) {
        node n = getNodeAtXY(evt.x, evt.y, null);
        transition t = getTransitionAtXY(evt.x, evt.y, null);
        if (n != null) {
            if (mode == MODE_ITEM) {
                n.decItems();
                dragingNode = null;
                dragingTransition = null;
            } else {
                dragingNode = n;
                dragingTransition = null;
            }
        }
        else if (t != null) {
            dragingNode = null;
            dragingTransition = t;
        }
        else {
            dragingNode = null;
            dragingTransition = null;
        }
    }

    private void mouseMetaDrag(Event evt) {
        if (dragingNode != null) {
            dragingNode.setX(evt.x);
            dragingNode.setY(evt.y);
        }
        else if (dragingTransition != null) {
            dragingTransition.setX(evt.x);
            dragingTransition.setY(evt.y);
        }
    }

    private void mouseDownNode(Event evt) {
        if (!tooCloseToNode(evt.x, evt.y, null) &&
            !tooCloseToTransition(evt.x, evt.y , null))
            PNet.addNode(evt.x, evt.y);
    }

    private void mouseDownTransition(Event evt) {
        if (!tooCloseToNode(evt.x, evt.y, null) &&
            !tooCloseToTransition(evt.x, evt.y, null))
            PNet.addTransition(evt.x, evt.y);
        else {
            transition t = getTransitionAtXY(evt.x, evt.y, null);
            if (t != null)
                t.cycleOrientation();
        }
    }

    private void mouseDownItem(Event evt) {
        node n = getNodeAtXY(evt.x, evt.y, null);
        if (n != null)
            n.incItems();
    }

    private void mouseDownEdge(Event evt) {
        node n = getNodeAtXY(evt.x, evt.y, null);
        transition t = getTransitionAtXY(evt.x, evt.y, null);

        if (newEdgeFinished == true) {
            if (n != null) {
                newEdge = PNet.addEdge(edge.NODE, PNet.getIndexOf(n), edge.NOTHING, 0, null);
                newEdgeFinished = false;
            }
            else if (t != null) {
                newEdge = PNet.addEdge(edge.TRANSITION, PNet.getIndexOf(t), edge.NOTHING, 0, null);
                newEdgeFinished = false;
            }
        }
        else {
            if (n != null) {
                // edge starts and ends at a node
                if (newEdge.getTFrom() == edge.NODE) {
                    PNet.removeEdge(newEdge);
                    newEdgeFinished = true;
                }
                else {
                    newEdge.clearLastPoint();
                    newEdge.setTTo(edge.NODE);
                    newEdge.setITo(PNet.getIndexOf(n));
                    newEdgeFinished = true;
                }
            }
            else if (t != null) {
                // edge starts and ends at a transition
                if (newEdge.getTFrom() == edge.TRANSITION) {
                    PNet.removeEdge(newEdge);
                    newEdgeFinished = true;
                }
                else {
                    newEdge.clearLastPoint();
                    newEdge.setTTo(edge.TRANSITION);
                    newEdge.setITo(PNet.getIndexOf(t));
                    newEdgeFinished = true;
                }
            }
            else {
                newEdge.setLastPoint(evt.x, evt.y);
                newEdge.addPoint(evt.x, evt.y);
            }
        }
    }

    private void mouseMoveEdge(Event evt) {
        node n = getNodeAtXY(evt.x, evt.y, null);
        transition t = getTransitionAtXY(evt.x, evt.y, null);

        if (newEdgeFinished == false) {
            if (n != null) {
                Point p;
                if (newEdge.getNumberOfPoints() > 1)
                    p = newEdge.getPoint(newEdge.getNumberOfPoints() - 2);
                else
                    p = newEdge.getXYFrom(PNet);
                newEdge.setLastPoint(p.x, p.y);
                newEdge.setTTo(edge.NODE);
                newEdge.setITo(PNet.getIndexOf(n));
            }
            else if (t != null) {
                Point p;
                if (newEdge.getNumberOfPoints() > 1)
                    p = newEdge.getPoint(newEdge.getNumberOfPoints() - 2);
                else
                    p = newEdge.getXYFrom(PNet);
                newEdge.setLastPoint(p.x, p.y);
                newEdge.setTTo(edge.TRANSITION);
                newEdge.setITo(PNet.getIndexOf(t));
            }
            else {
                newEdge.setLastPoint(evt.x, evt.y);
                newEdge.setTTo(edge.NOTHING);
                newEdge.setITo(0);
            }
        }
    }

    private void mouseDragEdge(Event evt) {
    }

    private void mouseUpEdge(Event evt) {
    }

    public void update(Graphics g) {
        if (BackBuffer == null)
            BackBuffer = createImage(ViewDim.width, ViewDim.height);
        Graphics gBB = BackBuffer.getGraphics();
        gBB.setColor(getBackground());
        gBB.fillRect(0, 0, ViewDim.width, ViewDim.height);
        gBB.setColor(Color.black);

        drawNodes(PNet, gBB);
        drawTransitions(PNet, gBB);
        drawEdges(PNet, gBB);

        g.drawImage(BackBuffer, 0, 0, ViewDim.width, ViewDim.height, null);
    }

    public void paint(Graphics g) {
        if (BackBuffer == null)
            BackBuffer = createImage(ViewDim.width, ViewDim.height);
        Graphics gBB = BackBuffer.getGraphics();
        gBB.setColor(getBackground());
        gBB.fillRect(0, 0, ViewDim.width, ViewDim.height);
        gBB.setColor(Color.black);

        drawNodes(PNet, gBB);
        drawTransitions(PNet, gBB);
        drawEdges(PNet, gBB);

        g.drawImage(BackBuffer, 0, 0, ViewDim.width, ViewDim.height, null);
    }

    // ge�ndert (04.05.1997 MK)
    private boolean drawNodes(pn PN, Graphics g) {
        for (int i = 0; i < PN.numberOfNodes(); i++) {
            node n;
            n = PN.getNode(i);

            if (iDeleteNode == -1)
                g.setColor(n.getColor());
            else if (iDeleteNode == i)
                g.setColor(Color.lightGray);
            else
                g.setColor(n.getColor());

            g.drawOval(n.getX() - n.getRadius(),
                       n.getY() - n.getRadius(),
                       2 * n.getRadius(),
                       2 * n.getRadius());

            if (n.getItems() == 1) {
                g.fillRect(n.getX() - (ItemW / 2), n.getY() - (ItemH / 2), ItemW, ItemH);
            }
            else if (n.getItems() == 2) {
                g.fillRect(n.getX() + (ItemW / 2), n.getY() - (ItemH / 2), ItemW, ItemH);
                g.fillRect(n.getX() - (3 * (ItemW / 2)), n.getY() - (ItemH / 2), ItemW, ItemH);
            }
            else if (n.getItems() == 3) {
                g.fillRect(n.getX() - (5 * (ItemW / 2)), n.getY() - (ItemH / 2), ItemW, ItemH);
                g.fillRect(n.getX() - (ItemW / 2), n.getY() - (ItemH / 2), ItemW, ItemH);
                g.fillRect(n.getX() + (3 * (ItemW / 2)), n.getY() - (ItemH / 2), ItemW, ItemH);
            }
            else if (n.getItems() > 3 && n.getItems() < 10) {
                Font f = g.getFont();
                int size = f.getSize();
                g.drawString(Integer.toString(n.getItems()), n.getX() - (size / 4), n.getY() + (size / 2));
            }
            else if (n.getItems() > 9) {
                Font f = g.getFont();
                int size = f.getSize();
                g.drawString(Integer.toString(n.getItems()), n.getX() - (size / 2), n.getY() + (size / 2));
            }
            // display name of node
            String s = n.getName();
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(s);
            int h = fm.getHeight();
            n.setXName(n.getX() - (w / 2));
            n.setYName(n.getY() - (n.getRadius() / 2) - h);
            g.drawString(s, n.getXName(), n.getYName());
        }
        return true;
    }

    // ge�ndert (04.05.1997 MK)
    private boolean drawTransitions(pn PN, Graphics g) {
        for (int i = 0; i < PN.numberOfTransitions(); i++) {
            transition t;
            t = PN.getTransition(i);

            if (iDeleteTransition != -1 && iDeleteTransition == i)
                g.setColor(Color.lightGray);
            else {
                if (colorMode == COLORMODE_BLACKANDWHITE)
                    g.setColor(Color.black);
                else if (colorMode == COLORMODE_STANDARD)
                    g.setColor(Color.blue);
            }

            if (t.highlight) g.setColor(Color.yellow);

            switch(t.getOrientation()) {
            case transition.ORIENTATION_VERTICAL:
                g.fillRect(t.getX() - (t.getWidth() / 2),
                           t.getY() - (t.getHeight() / 2),
                           t.getWidth(),
                           t.getHeight());
                break;
            case transition.ORIENTATION_DIAGONAL1:
                {

                    int[] xpoints = new int[4];
                    int[] ypoints = new int[4];

                    double Sin45 = 0.707106781;

                    xpoints[0] = (int) (t.getX() + ((-t.getWidth() / 2.0) * Sin45 + (-t.getHeight() / 2.0) * Sin45));
                    ypoints[0] = (int) (t.getY() + ((-t.getWidth() / 2.0) * Sin45 + (-t.getHeight() / 2.0) * (-Sin45)));
                    xpoints[1] = (int) (t.getX() + (( t.getWidth() / 2.0) * Sin45 + (-t.getHeight() / 2.0) * Sin45));
                    ypoints[1] = (int) (t.getY() + (( t.getWidth() / 2.0) * Sin45 + (-t.getHeight() / 2.0) * (-Sin45)));
                    xpoints[2] = (int) (t.getX() + (( t.getWidth() / 2.0) * Sin45 + ( t.getHeight() / 2.0) * Sin45));
                    ypoints[2] = (int) (t.getY() + (( t.getWidth() / 2.0) * Sin45 + ( t.getHeight() / 2.0) * (-Sin45)));
                    xpoints[3] = (int) (t.getX() + ((-t.getWidth() / 2.0) * Sin45 + ( t.getHeight() / 2.0) * Sin45));
                    ypoints[3] = (int) (t.getY() + ((-t.getWidth() / 2.0) * Sin45 + ( t.getHeight() / 2.0) * (-Sin45)));

                    g.fillPolygon(xpoints, ypoints, 4);
                }
                break;
            case transition.ORIENTATION_HORIZONTAL:
                g.fillRect(t.getX() - (t.getHeight() / 2),
                           t.getY() - (t.getWidth() / 2),
                           t.getHeight(),
                           t.getWidth());
                break;
            case transition.ORIENTATION_DIAGONAL2:
                {
                    int[] xpoints = new int[4];
                    int[] ypoints = new int[4];

                    double Sin45 = 0.707106781;

                    xpoints[0] = (int) (t.getX() + ((-t.getWidth() / 2.0) * Sin45 + (-t.getHeight() / 2.0) * Sin45));
                    ypoints[0] = (int) (t.getY() + ((-t.getWidth() / 2.0) * (-Sin45) + (-t.getHeight() / 2.0) * Sin45));
                    xpoints[1] = (int) (t.getX() + (( t.getWidth() / 2.0) * Sin45 + (-t.getHeight() / 2.0) * Sin45));
                    ypoints[1] = (int) (t.getY() + (( t.getWidth() / 2.0) * (-Sin45) + (-t.getHeight() / 2.0) * Sin45));
                    xpoints[2] = (int) (t.getX() + (( t.getWidth() / 2.0) * Sin45 + ( t.getHeight() / 2.0) * Sin45));
                    ypoints[2] = (int) (t.getY() + (( t.getWidth() / 2.0) * (-Sin45) + ( t.getHeight() / 2.0) * Sin45));
                    xpoints[3] = (int) (t.getX() + ((-t.getWidth() / 2.0) * Sin45 + ( t.getHeight() / 2.0) * Sin45));
                    ypoints[3] = (int) (t.getY() + ((-t.getWidth() / 2.0) * (-Sin45) + ( t.getHeight() / 2.0) * Sin45));

                    g.fillPolygon(xpoints, ypoints, 4);
                }
                break;
            case transition.ORIENTATION_ALL:
                for (int j = 0; j < t.getWidth(); j++) {
                    g.drawRect(t.getX() - (t.getHeight() / 2) + j,
                               t.getY() - (t.getHeight() / 2) + j,
                               t.getHeight() - (2*j),
                               t.getHeight() - (2*j));
                }
                break;
            }
            // display name of transition
            String s = t.getName();
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(s);
            int h = fm.getHeight();
            t.setXName(t.getX() - (w / 2));
            t.setYName(t.getY() - (t.getHeight() / 2) - h);
            g.drawString(s, t.getXName(), t.getYName());
        }
        return true;
    }

    private boolean drawEdges(pn PN, Graphics g) {
        int iDel = 0;
        for (int i = 0; i < PN.numberOfEdges(); i++) {
            edge e;
            e = PN.getEdge(i);
            if (iDel < nDeleteEdge && iDeleteEdge[iDel] == i) {
                g.setColor(Color.lightGray);
                iDel++;
            }
            else
                g.setColor(Color.black);
            drawEdge(e, g, PN);
        }
        return true;
    }

    // ge�ndert (25.03.1997 MK)
    // v�llig ge�ndert (04.05.1997 MK)
    private boolean drawEdge(edge e, Graphics g, pn PN) {
        Polygon points = e.getPoints();
        //
        if (e.getTFrom() == e.getTTo()) {
            if (colorMode == COLORMODE_BLACKANDWHITE)
                g.setColor(Color.lightGray);
            else if (colorMode == COLORMODE_STANDARD)
                g.setColor(Color.red);
            if (e.getIFrom() == e.getITo() && points != null) {
                if (points.npoints <= 2)
                    return false;
            }
        }
        // edges that will not be drawn
        if (e.getTFrom() == edge.NOTHING)
            return false;
        if (e.getTTo() == edge.NOTHING && points == null)
            return false;

        Point pFrom, pTo;
        pFrom = e.getXYFrom(PN);
        pTo = e.getXYTo(PN);
/*       if (e.getTFrom() == edge.TRANSITION) {
            transition t = PN.getTransition(e.getIFrom());
            if (t.getOrientation() == transition.ORIENTATION_ALL) {
                pFrom = e.getXYFrom(PN, 15.0);
                pTo = e.getXYTo(PN, 15.0);
            } else {
                pFrom = e.getXYFrom(PN);
                pTo = e.getXYTo(PN);
            }
        } else {
            pFrom = e.getXYFrom(PN);
            pTo = e.getXYTo(PN);

        }

*/
        // if edge is negative, the edge is drawn further away from the transition,
        // so it doesnt stick out of the oval at the end of the edge
        if (e.isNegated() == true) {
            if (e.getTFrom() == edge.TRANSITION) {
                pFrom = e.getXYFrom(PN, 6.0);
                g.fillOval(pFrom.x - 4, pFrom.y - 4, 8, 8);
            }
            else if (e.getTTo() == edge.TRANSITION) {
                pTo = e.getXYTo(PN, 6.0);
                g.fillOval(pTo.x - 4, pTo.y - 4, 8, 8);
            }
        }

        // if edge is build out of more than one line draw, draw all lines except
        // the last one (which will be an arrow or a just line again)
        if (points != null) {
            points.xpoints[0] = pFrom.x;
            points.ypoints[0] = pFrom.y;
            g.drawPolygon(points);

            // x and y position for the last line are adjusted
            pFrom.x = points.xpoints[points.npoints-1];
            pFrom.y = points.ypoints[points.npoints-1];
        }

        // draw arrow or line (the line already got the oval at the transition)
        if (e.isNegated() == false) {
            drawArrow(g, pFrom, pTo);
        }
        else {
            g.drawLine(pFrom.x, pFrom.y, pTo.x, pTo.y);
        }

        // the weight of the edge will be drawn if the weight of the edge
        // is higher than 1 and not negated
        if (e.getWeight() > 1 && e.isNegated() == false) {
            Point weightPos = e.getWeightPosition(PN);
            g.drawString("" + e.getWeight(), weightPos.x, weightPos.y);
        }

        return true;
    }

    private static void drawArrow(Graphics g, Point p1, Point p2) {
 //     System.out.println("drawArrow from (" + p1.x + ", " + p1.y + ") to (" + p2.x + ", " + p2.y + ")");
        drawArrow(g, p1.x, p1.y, p2.x, p2.y);
    }

    private static void drawArrow(Graphics g, int xS, int yS, int xE, int yE) {
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
 //       System.out.println(xS + " - " + xE + " = " + dx);
 //       System.out.println(yS + " - " + yE + " = " + dy);
        double  length = Math.sqrt(dx * dx + dy * dy);

        double  xAdd = 9.0 * dx / length;
        double  yAdd = 9.0 * dy / length;

        xP[1] = (int) Math.round (xE + xAdd - (yAdd / 3.0));
        yP[1] = (int) Math.round (yE + yAdd + (xAdd / 3.0));
        xP[2] = (int) Math.round (xE + xAdd + (yAdd / 3.0));
        yP[2] = (int) Math.round (yE + yAdd - (xAdd / 3.0));

//      System.out.println(xS + " " + yS + ", " + xE + " " + yE + ", " + xAdd + " " + yAdd);

        g.drawLine(xS, yS, xP[0], yP[0]);
        g.fillPolygon(xP, yP, 3);
    }

    private boolean tooCloseToNode(int x, int y, node IgnoreNode) {
        node n = getClosestNode(x, y, IgnoreNode);
        if (n == null)
            return false;
        if (n.distance(x, y) < (1.5 * node.getRadius()))
            return true;
        return false;
    }

    private boolean tooCloseToTransition(int x, int y, transition IgnoreTransition) {
        transition t = getClosestTransition(x, y, IgnoreTransition);
        if (t == null)
            return false;
        if (t.distance(x, y) < (1.5 * transition.getHeight()))
            return true;
        return false;
    }

    public node getNodeAtXY(int x, int y, node IgnoreNode) {
        node n = getClosestNode(x, y, IgnoreNode);
        if (n == null)
            return null;
        if (n.distance(x, y) < node.getRadius())
            return n;
        return null;
    }

    public transition getTransitionAtXY(int x, int y, transition IgnoreTransition) {
        transition t = getClosestTransition(x, y, IgnoreTransition);
        if (t == null)
            return null;
        if (t.distance(x, y) < (transition.getHeight() / 2))
            return t;
        return null;
    }

    public edge getEdgeAtXY(int x, int y, edge IgnoreEdge) {
        edge e = getClosestEdge(x, y, IgnoreEdge);
        if (e == null)
            return null;
        if (e.distance(x, y, PNet) < 10.0)
            return e;
        return null;
    }

    public node getClosestNode(int x, int y, node IgnoreNode) {
        node    n, cn;
        double  d;
        double  cd = Double.MAX_VALUE;
        if (PNet.numberOfNodes() == 0)
            return null;
        else
            cn = PNet.getNode(0);

        for (int i = 0; i < PNet.numberOfNodes(); i++) {
            n = PNet.getNode(i);
            // Node is ignored, if it has the same index (-> nodes are the same)
            if (! PNet.equal (n, IgnoreNode)) {
                d = n.distance((double) x, (double) y);
                if (d < cd) {
                    cn = n;
                    cd = d;
                }
            }
        }
        return cn;
    }

/*  public double getDistanceToClosestNode(int x, int y, node IgnoreNode) {
        double d = Double.MAX_VALUE;
        node n = getClosestNode(x, y, IgnoreNode);
        if (n != null)
            d = n.distance(x, y);
        return d;
    }
*/
    public transition getClosestTransition(int x, int y, transition IgnoreTrans) {
        transition    t, ct;
        double  d;
        double  cd = Double.MAX_VALUE;
        if (PNet.numberOfTransitions() == 0)
            return null;
        else
            ct = PNet.getTransition(0);

        for (int i = 0; i < PNet.numberOfTransitions(); i++) {
            t = PNet.getTransition(i);
            // Transition is ignored, if it has the same index (-> Transitions
            // are the same)
            if (!PNet.equal (t, IgnoreTrans)) {
                d = t.distance((double) x, (double) y);
                if (d < cd) {
                    ct = t;
                    cd = d;
                }
            }
        }
        return ct;
    }

/*  public double getDistanceToClosestTransition(int x, int y, transition IgnoreTrans) {
        double d = Double.MAX_VALUE;
        transition t = getClosestTransition(x, y, IgnoreTrans);
        if (t != null)
            d = t.distance(x, y);
        return d;
    }
*/
    public edge getClosestEdge(int x, int y, edge IgnoreEdge) {
        edge    e, ce;
        double  d;
        double  cd = Double.MAX_VALUE;
        if (PNet.numberOfEdges() == 0)
            return null;
        else
            ce = PNet.getEdge(0);

        for (int i = 0; i < PNet.numberOfEdges(); i++) {
            e = PNet.getEdge(i);
            // Edge is ignored, if it has the same index ( => Edges
            // are the same)
            if (!PNet.equal (e, IgnoreEdge)) {
                d = e.distance((double) x, (double) y, PNet);
                if (d < cd) {
                    ce = e;
                    cd = d;
                }
            }
        }
        return ce;
    }

/*  public Object getClosestItem(int x, int y, Object IgnoreObject) {
        Object o;
        double distNode, distTrans;
        node closestNode;
        transition closestTransition;

        o = null;

        if (IgnoreObject == null) {
            o = (Object) getClosestNode(x, y, null);
            if (getDistanceToClosestNode(x, y, null) > getDistanceToClosestTransition(x, y, null))
                o = (Object) getClosestTransition(x, y, null);
            return o;
        }
        return o;
    }
*/
/*  public double getDistanceToClosestItem(int x, int y, Object IgnoreObject) {
        double distTrans, distNode, dist;
        distTrans = distNode = dist = Double.MAX_VALUE;
        if (IgnoreObject == null) {
            dist = distNode = getDistanceToClosestNode(x, y, null);
            distTrans = getDistanceToClosestTransition(x, y, null);
        }
        else {
            if (IgnoreObject instanceof node) {
                dist = distNode = getDistanceToClosestNode(x, y, (node) IgnoreObject);
                distTrans = getDistanceToClosestTransition(x, y, null);
            }
            else if (IgnoreObject instanceof transition) {
                dist = distNode = getDistanceToClosestNode(x, y, null);
                distTrans = getDistanceToClosestTransition(x, y, (transition) IgnoreObject);
            }
        }

        if (distTrans < distNode)
            dist = distTrans;
        return dist;
    }
*/
}