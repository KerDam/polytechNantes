import java.util.*;


public class runPN extends Thread {

    pn PNet;
    Editor Ed;
    vis Vis;
    int StepCount;
    int mode;
    int vismode;
    int delay;
    boolean demo;
    boolean selected;
    boolean single;
    transition selectedTransition;

    static final int PARMAN = 0;
    static final int PARRAN = 1;
    static final int SEQMAN = 2;
    static final int SEQRAN = 3;
    static final int CODE = 99;

    public runPN(pn pnet, Editor ed, vis vi, int m, boolean sing) {
        super("RunPN");
        PNet = pnet;
        Ed = ed;
        Vis = vi;
        StepCount = 0;
        mode = m;
        delay = 0;
        selected = false;
        single = sing;
        demo = false;
    }

    public void setPN(pn pnet) {
        PNet = pnet;
    }

    public void setEditor(Editor ed) {
        Ed = ed;
    }

    public void setVis(vis vi) {
        Vis = vi;
    }

    public void setDemo(boolean d) {
        demo = d;
    }

    public void setMode(int m) {
        mode = m;
    }

    public void enableSingleStep() {
        single = true;
    }

    public void disableSingleStep() {
        single = false;
    }

    public void select(transition t) {
        selected = true;
        selectedTransition = t;
	resume();
    }

    public void restoreVisMode() {
        if (mode == SEQMAN || mode == PARMAN) {
            Vis.setMode(vismode, CODE);
            PNet.unhighlightAllTransitions();
            Vis.repaint();
        } else {
	    PNet.unhighlightAllTransitions();
	    Vis.repaint();
        }
    }

    public void holdVisMode() {
        vismode = Vis.getMode();
        Vis.setMode(vis.SELECTMODE, 0);
        Vis.repaint(1);
    }

    public void blink(transition t) {
        for (int i = 0; i < 3; i++) {
            t.highlight = true;
            Vis.repaint();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException l) {}
            t.highlight = false;
            Vis.repaint();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException l) {}
         }
    }

    public void blink(Vector v) {           //new 18.5.97 jw
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < v.size(); j++) {
                ((transition)v.elementAt(j)).highlight = true;
            }
            Vis.repaint();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException l) {}
            for (int j = 0; j < v.size(); j++) {
                ((transition)v.elementAt(j)).highlight = false;
            }
            Vis.repaint();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException l) {}
        }
    }

    public void run() {                      //New 22.5.97 ah
        delay = Ed.getDelay();               //New 13.6.97 jw
        demo = Ed.getDemo();
        switch (mode) {
        case runPN.SEQRAN :
            transition t;
            boolean changedseq;
            boolean ret = false;
            boolean increaseCounter;
            Vector seq = new Vector();

            while (! PNet.isDead(true)) {
                for (int i = 0; i < PNet.numberOfTransitions(); i++){
                    t = PNet.getTransition(i);
                    t.randomize();
                    if (t.canFire(PNet,true)) seq.addElement(t);
                }
                transition seqtemp = (transition)seq.elementAt(0);
                for (int i = 1; i < seq.size()-1; i++) {
                    if (seqtemp.getRan() > ((transition)seq.elementAt(i)).getRan())
                        seqtemp = ((transition)seq.elementAt(i));
                }
                t = seqtemp;
                increaseCounter = t.canFire(PNet,true);
                if (increaseCounter) {
		    if (demo) blink(t);
		    t.fire(PNet,true);
                    StepCount++;
                    Vis.repaint();
                    Ed.setCount(StepCount);
                    if (single) {
                        Ed.runIsHolding();
                        suspend();
                    } else {
                        try {
                            Thread.sleep(delay);
                        }
                        catch (InterruptedException l) {}
                    }
                }
            }
            StepCount = 0;
	    Ed.runButtonsUnHighlight();
            Ed.setStatus("Petri Net is dead!");
            break;
        case runPN.SEQMAN :
            transition t1;
	   
            while (! PNet.isDead(false)) {
                for (int i = 0; i < PNet.numberOfTransitions(); i++) {
                    t1 = PNet.getTransition(i);
                    if (t1.canFire(PNet, false)) {
                        t1.highlight = true;
                    }
                }
                selected = false;
                holdVisMode();
                setPriority(Thread.MIN_PRIORITY);
                while (! selected) {
		  suspend();
		}
                setPriority(Thread.NORM_PRIORITY);
                selectedTransition.fire(PNet, false);
                StepCount++;
                Ed.setCount(StepCount);
                restoreVisMode();
                if (single) {
                    Ed.runIsHolding();
                    suspend();
                }
            }
            StepCount = 0;
	    Ed.runButtonsUnHighlight();
            Ed.setStatus("Petri Net is dead!");
            break;

        case runPN.PARRAN :
            transition t2;
            boolean changed;
            boolean increaseCounter1;
            transition prio, nextprio, help;
            Vector par = new Vector();


            while (! PNet.isDead(false)){
                for (int i = 0; i < PNet.numberOfTransitions(); i++){
                    t2 = PNet.getTransition(i);
                    t2.randomize();
                    if (t2.canFire(PNet,false)) par.addElement(t2);
                }

                do {
                    changed = false;
                    int size = (par.size()-1);
                    for (int i = 0; i < size ; i++){
                        prio = (transition) par.elementAt(i);
                        nextprio = (transition) par.elementAt(i+1);
                        if (prio.getRan() < nextprio.getRan() ) {
                            help = nextprio;
                            par.removeElementAt(i+1);
                            par.insertElementAt(prio, i+1);
                            par.removeElementAt(i);
                            par.insertElementAt(help, i);
                            changed = true;
                        }
                    }
                } while (changed);

                par = PNet.multipleCanFire(par,false);

                if (demo) {
                    blink(par);
                }
                increaseCounter1 = false;
                for (int i = 0; i< par.size(); i++){
                    t2 = (transition) par.elementAt(i);
                    increaseCounter1 = (t2.fire(PNet, false));
                  }
                Vis.repaint();
                par.removeAllElements();
                if (increaseCounter1) {
                    StepCount++;
                    Vis.repaint();
                    Ed.setCount(StepCount);
                    if (single) {
                        Ed.runIsHolding();
                        suspend();
                    } else {
                        try {
                            Thread.sleep(delay);
                        }
                        catch (InterruptedException l) {}
                    }
                }
                increaseCounter1 = false;
            }
            StepCount = 0;
	    Ed.runButtonsUnHighlight();
            Ed.setStatus("Petri Net is dead!");
            break;

        case runPN.PARMAN :
            transition t3,t4;
            boolean changed2 = false;
            boolean moreThanOne;
            int weights, items;
            transition prio2, nextprio2, help2;
            int i,j;


            Vector par2 = new Vector();
            Vector fired = new Vector();


            while (! PNet.isDead(false)){
              for (i = 0; i < PNet.numberOfTransitions(); i++){
                    t3 = PNet.getTransition(i);
                    t3.randomize();
                    if (t3.canFire(PNet,false))
                        par2.addElement(t3);
                }
                do {
                    changed2 = false;
                    int size = (par2.size()-1);
                    for (i = 0; i < size ; i++){
                        prio2 = (transition) par2.elementAt(i);
                        nextprio2 = (transition) par2.elementAt(i+1);
                        if (prio2.getRan() < nextprio2.getRan() ) {
                            help2 = nextprio2;
                            par2.removeElementAt(i+1);
                            par2.insertElementAt(prio2, i+1);
                            par2.removeElementAt(i);
                            par2.insertElementAt(help2, i);
                            changed2 = true;
                        }
                    }
                } while (changed2);

                moreThanOne = false;
                Vector fireable = new Vector();

                for (i = 0; i < par2.size(); i++){
                  transition test = (transition) par2.elementAt(i);
                  if ((PNet.getAllConnectedFireableTrans(test, false)).size() > 1) {
                    moreThanOne = true;
                    fireable.addElement(test);
                  }
                }
                if (moreThanOne){
                  pn.subVectorOfVector(fireable, par2);
                  for (i=0; i < fireable.size(); i++){
                    t4 = (transition) fireable.elementAt(i);
                    t4.highlight = true;
                  }
                  holdVisMode();
                  while (fireable.size() > 1) {
                    selected = false;

                    setPriority(Thread.MIN_PRIORITY);
                    while (! selected) {
		      suspend();
		    }
                    setPriority(Thread.NORM_PRIORITY);

                    fired = PNet.getAllConnectedTrans(selectedTransition);

                    for (int k = 0; k < fired.size(); k++) {
                      transition unhigh = (transition) fired.elementAt(k);
                      unhigh.highlight = false;
                    }
                    if (demo) blink(selectedTransition);
                    selectedTransition.fire(PNet, false);
                    Vis.repaint();

                    pn.subVectorOfVector(fired, fireable);
                  }
                  restoreVisMode();
                  if (fireable.size() == 1) {
                    ((transition)fireable.elementAt(0)).fire(PNet, false);
                    Vis.repaint();
                  }
                }

                par2 = PNet.multipleCanFire(par2, false);

                if (demo) {
                           blink(par2);
                          }

                for (i=0; i < par2.size(); i++){
                     transition fire = (transition) par2.elementAt(i);
                     fire.fire(PNet, false);
                }


                par2.removeAllElements();

                StepCount++;              //zaehlt vielleicht nicht richtig
                Vis.repaint();
                Ed.setCount(StepCount);
                if (single) {
                   Ed.runIsHolding();
                   suspend();
                } else {
                   try {
                     Thread.sleep(delay);
                   }
                   catch (InterruptedException l) {}
                }
            }
            StepCount = 0;
	    Ed.runButtonsUnHighlight();
            Ed.setStatus("Petri Net is dead!");
            break;

        }
    }

}

