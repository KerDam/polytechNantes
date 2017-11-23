import java.io.*;

public class SavePetriNet {

    File f;
    FileOutputStream fout;
    PrintStream output;
    String nextLine;
    int Index;

    public SavePetriNet() {
    }

    public void setFile(String file) throws IOException {
        f = new File(file);
        if (f.exists()) {
            throw new FileExistsException("File exists!");
        } else {
            fout = new FileOutputStream(f);
            output = new PrintStream(fout);
        }
    }

    public void deleteFile() throws IOException {
        f.delete();
        fout = new FileOutputStream(f);
        output = new PrintStream(fout);
    }

    public void saveNet(pn PetNet) {
        output.println("*******************************************");
        output.println("**  PNS-NETFILE Javaversion              **");
        output.println("**  WARNING: DON'T EDIT THIS FILE        **");
        output.println("*******************************************");
        saveNodes(PetNet);
        saveTransitions(PetNet);
        saveEdges(PetNet);
    }

    void saveNodes(pn pnet) {
        node n;
        for (int i = 0; i < pnet.numberOfNodes(); i++) {
            n = pnet.getNode(i);
            output.print("S" + i + ": ~" + n.getName() + "~ ");
            output.print(n.getItems() + " ");
            output.print(n.getX() + " ");
            output.print(n.getY() + " ");
            output.print(n.getXName() + " ");    // x.Name
            output.print(n.getYName() + " ");    // y.Name
            output.print("-" + " ");  // Haltebed.
            output.println(0);        // Haltewert
        }
    }

    void saveTransitions(pn pnet) {
        transition t;
        for (int i = 0; i < pnet.numberOfTransitions(); i++) {
            t = pnet.getTransition(i);
            output.print("T" + i + ": ~" + t.getName() + "~ ");
            output.print(t.getPriority() + " ");
            output.print(t.getX() + " ");
            output.print(t.getY() + " ");
            output.print(t.getXName() + " ");    // x.Name
            output.print(t.getYName() + " ");    // y.Name
            output.print(0 + " ");    // Break
            switch (t.getOrientation()) {
            case transition.ORIENTATION_VERTICAL:
                output.println("a");
                break;
            case transition.ORIENTATION_HORIZONTAL :
                output.println("c");
                break;
            case transition.ORIENTATION_DIAGONAL1 :
                output.println("b");
                break;
            case transition.ORIENTATION_DIAGONAL2 :
                output.println("d");
                break;
            case transition.ORIENTATION_ALL :
                output.println("e");
                break;
            }
        }
    }

    void saveEdges(pn pnet) {
        edge e;
        int pointnum;
        for (int i = 0; i < pnet.numberOfEdges(); i++) {
            e = pnet.getEdge(i);
            if (e.getTFrom() == edge.NODE) {
                output.print("KST: ");
                output.print(e.getIFrom() + " ");
                output.print(e.getITo() + " ");
                if (e.isNegated()) {
                    output.print("1 -1 ");
                } else {
                    output.print("0 ");
                    output.print(0 - e.getWeight() + " ");
                }
            } else {
                output.print("KTS: ");
                output.print(e.getITo() + " ");
                output.print(e.getIFrom() + " ");
                if (e.isNegated()) {
                    output.print("1 1 ");
                } else {
                    output.print("0 ");
                    output.print(e.getWeight() + " ");
                }
            }
            output.print(e.getWeightPosition(pnet).x + " ");          // x.Delta
            output.print(e.getWeightPosition(pnet).y + " ");          // y.Delta
            pointnum = e.getNumberOfPoints();
            output.print(pointnum + " ");                      // NVENr
            output.print("E");
            while (pointnum > 0) {                             // additional Edges
                output.print(" e ");
                output.print(e.getPoint(pointnum-1).x + " ");
                output.print(e.getPoint(pointnum-1).y + " ");
                pointnum--;
                output.print(pointnum);
            }
            output.println();
        }
    }


    static public void main(String[] args) throws IOException {
        SavePetriNet Save = new SavePetriNet();
        LoadPetriNet Load = new LoadPetriNet();
        Load.setFile("simple.net");
        pn PN = Load.loadNewNet();
        Save.setFile("out.net");
        Save.saveNet(PN);
    }

}


