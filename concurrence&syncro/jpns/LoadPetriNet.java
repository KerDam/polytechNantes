import java.io.*;
import java.awt.*;
import java.net.*;

public class LoadPetriNet {

    File f;
    FileInputStream fin;
    DataInputStream input;
    String nextLine;
    int Index;

    public LoadPetriNet() {
    }

    public boolean setFile(String file) throws IOException {
        f = new File(file);
        fin = new FileInputStream(f);
        input = new DataInputStream(new FileInputStream(f));
        getNextLine();
        getNextLine();
        if (nextLine.length() > 15) {
           if (nextLine.substring(4,15).equals("PNS-NETFILE")) {
               return true;
           } else return false;
        } else return false;
    }

    public void closeFile() throws IOException {
        fin.close();
    }

    public pn loadNewNet() {
        pn NewNet = new pn();
        loadNet(NewNet);
        return NewNet;
    }

    public pn loadNewNetApplet(URL file) throws IOException {
        pn NewNet = new pn();
        URLConnection loadcon = file.openConnection();
        InputStream in = loadcon.getInputStream();
        input = new DataInputStream(in);
        loadNet(NewNet);
        return NewNet;
    }

    public void loadNet(pn PetNet) {
        boolean b;
        b = getNextLine();
        while (b) {
            switch (nextLine.charAt(0)) {
                case 'S': //System.out.println("Node : " + nextLine);
                          loadNode(PetNet);
                          break;
                case 'T': //System.out.println("Transition : " + nextLine);
                          loadTransition(PetNet);
                          break;
                case '*': break;
                case 'K': if (nextLine.charAt(1) == 'S') {
                            //System.out.println("Edge N-T: " + nextLine);
                            loadNTEdge(PetNet);
                          } else {
                            //System.out.println("Edge T-N: " + nextLine);
                            loadTNEdge(PetNet);
                          }
                          break;
            }
            b = getNextLine();
        }
    }

    void loadNode(pn pnet) {
        node n = new node();
        int beginname = nextLine.indexOf("~");
        int endname = nextLine.lastIndexOf("~");
        n.setName(nextLine.substring(beginname + 1, endname));
        Index = endname + 2;
        n.setItems(Integer.parseInt(getNextToken()));
        n.setX(Integer.parseInt(getNextToken()));
        n.setY(Integer.parseInt(getNextToken()));
     /* getNextToken();     // x.Name
        getNextToken();     // y.Name
        getNextToken();     // Hold condition
        getNextToken();     // Hold value
     */
        pnet.addNode(n);
    }

    void loadTransition(pn pnet) {
        transition t = new transition();
        String orient;
        int beginname = nextLine.indexOf("~");
        int endname = nextLine.lastIndexOf("~");
        t.setName(nextLine.substring(beginname + 1, endname));
        Index = endname + 2;
        t.setPriority(Integer.parseInt(getNextToken()));
        t.setX(Integer.parseInt(getNextToken()));
        t.setY(Integer.parseInt(getNextToken()));
        getNextToken();     // x.Name
        getNextToken();     // y.Name
        getNextToken();     // break
        orient = getNextToken();
        if (orient.equals("a")) t.setOrientation(transition.ORIENTATION_VERTICAL);
        if (orient.equals("e")) t.setOrientation(transition.ORIENTATION_ALL);
        if (orient.equals("b")) t.setOrientation(transition.ORIENTATION_DIAGONAL1);
        if (orient.equals("d")) t.setOrientation(transition.ORIENTATION_DIAGONAL2);
        if (orient.equals("c")) t.setOrientation(transition.ORIENTATION_HORIZONTAL);
        pnet.addTransition(t);
    }

    void loadNTEdge(pn pnet) {
        edge e;
        Polygon p = new Polygon();
        int neg;
        Index = 5;
        //System.out.println("Edge");
        e = pnet.addEdge(edge.NODE,
                         Integer.parseInt(getNextToken()),
                         edge.TRANSITION,
                         Integer.parseInt(getNextToken()),
                         null);
        neg = Integer.parseInt(getNextToken());
        if (neg == 1) e.setNegated(true);
        e.setWeight(0 - Integer.parseInt(getNextToken()));
        getNextToken(); // get x.Delta
        getNextToken(); // get y.Delta
        getNextToken(); // get nven
        getNextToken(); // E
        while (getNextToken().length() != 0) {
            p.addPoint(Integer.parseInt(getNextToken()),
                       Integer.parseInt(getNextToken()));
            getNextToken();
        }
        if (p.npoints > 0) {
            for (int i = p.npoints - 1; i >= 0; i--) {
               e.addPoint(p.xpoints[i], p.ypoints[i]);
            }
        }
    }

    void loadTNEdge(pn pnet) {
        edge e;
        Polygon p = new Polygon();
        Index = 5;
        int neg;
        //System.out.println("Edge");
        int nnum = Integer.parseInt(getNextToken());
        int tnum = Integer.parseInt(getNextToken());
        e = pnet.addEdge(edge.TRANSITION,
                         tnum,
                         edge.NODE,
                         nnum,
                         null);
        neg = Integer.parseInt(getNextToken());
        if (neg == 1) e.setNegated(true);
        e.setWeight(Integer.parseInt(getNextToken()));
        getNextToken(); // get x.Delta
        getNextToken(); // get y.Delta
        getNextToken(); // get nven
        getNextToken(); // E
        while (getNextToken().length() != 0) {
            p.addPoint(Integer.parseInt(getNextToken()),
                       Integer.parseInt(getNextToken()));
            getNextToken();
        }
        if (p.npoints > 0) {
            for (int i = p.npoints - 1; i >= 0; i--) {
               e.addPoint(p.xpoints[i], p.ypoints[i]);
            }
        }

    }

    boolean getNextLine() {
        int a;
        try {
            nextLine = input.readLine();
            a = nextLine.length();
        }
        catch (NullPointerException n) {
            return false;
        }
        catch (IOException i) {
            System.out.println("Can't read next line of " + f.getName());
        }
        return true;
    }

    String getNextToken() {
        int BeginIndex = Index;
        try {
            while (nextLine.charAt(Index) != ' ') {
                Index++;
            }
        }
        catch (StringIndexOutOfBoundsException s) {
            return nextLine.substring(BeginIndex, nextLine.length());
        }
        Index++;
        return nextLine.substring(BeginIndex, Index - 1);
    }

}


