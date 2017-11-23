import java.awt.*;
import java.io.*;
import java.net.*;

public class Editor extends Frame {
   MenuBar menubar;                                             // the menubar
   Menu file, help, Settings, Parallel, Sequentiell, Painting;  // menu panels
   CheckboxMenuItem ParMan, ParRan, SeqMan, SeqRan, BW, Col;    // menu checkboxes
   Button okay, cancel;
   FileDialog Load, Save;
   String InputFile, InputPath, OutputFile, OutputPath;
   boolean alreadySaved;
   boolean runHolding;
   boolean demo;
   LoadPetriNet Loading;
   SavePetriNet Saving;
   OverwriteDialog ODialog;
   ShowDialog ShowOp;
   TextField StatusLine, StatusMode, StatusDiv, StatusCount;
   pn PNet;
   pn OldPNet;
   vis Vis;
   Toolkit tk;
   CPictBar PBar;
   HelpBox HelpCom;
   HelpAbout HelpAb;
   runPN runStep;
   int StepCount;
   int runDelay;
   boolean AppletState, NoFileAccess;
   URL Docbase;
   Panel Status;
   LoadPetriNetApplet LoadApplet;



   public Editor(String title, URL DocBase) {
      super(title);

      System.out.println("jPNS is loading...");
      PNet = new pn();
      StepCount = 0;
      runDelay = 100;
      runHolding = false;
      AppletState = false;
      NoFileAccess = false;
      demo = false;
      Docbase = DocBase;

      // Create the menubar.  Tell the frame about it.
      menubar = new MenuBar();
      this.setMenuBar(menubar);

      Save = new FileDialog(this,"Save Petri-Net File",FileDialog.SAVE);
      Load = new FileDialog(this,"Load Petri-Net File",FileDialog.LOAD);
      Loading = new LoadPetriNet();
      Saving = new SavePetriNet();
      alreadySaved = false;

      // Create the file menu.  Add two items to it.  Add to menubar.
      file = new Menu("File");
      file.add(new MenuItem("New"));
      file.add(new MenuItem("Load..."));
      file.add(new MenuItem("Save"));
      file.add(new MenuItem("Save As..."));
      file.add(new MenuItem("Quit"));
      menubar.add(file);

      // Create submenus for Setting menu
      ParMan = new CheckboxMenuItem("Manual");
      ParRan = new CheckboxMenuItem("Random");
      Parallel = new Menu("Parallel");
      Parallel.add(ParRan);
      Parallel.add(ParMan);

      SeqMan = new CheckboxMenuItem("Manual");
      SeqRan = new CheckboxMenuItem("Random");
      Sequentiell = new Menu("Sequential");
      Sequentiell.add(SeqRan);
      Sequentiell.add(SeqMan);
      SeqRan.setState(true);

      BW = new CheckboxMenuItem("Black/White");
      Col = new CheckboxMenuItem("Color");
      Painting = new Menu("Painting");
      Painting.add(Col);
      Painting.add(BW);

      // Create the Settings menu
      Settings = new Menu("Settings");
      Settings.add(Parallel);
      Settings.add(Sequentiell);
      Settings.addSeparator();
      Settings.add(new MenuItem("Options"));
      Settings.addSeparator();
      Settings.add(Painting);
      menubar.add(Settings);
      Col.setState(true);

      // Create Help menu; add an item; add to menubar
      help = new Menu("Help");
      help.add(new MenuItem("About"));
      help.add(new MenuItem("Commands"));
      menubar.add(help);
      // Display the help menu in a special reserved place.
      menubar.setHelpMenu(help);

      // Buttonbar
      tk = this.getToolkit();
      Image[] ups = new Image[12];
      Image[] downs = new Image[12];
      Image[] diss = new Image[12];
      try {
        File dtry = new File("examples");
        if (dtry.isDirectory()) Load.setDirectory(dtry.getAbsolutePath());
      }
      catch (SecurityException se) {NoFileAccess = true;}
      if (DocBase != null) {
        AppletState = true;
        try {
            ups[0] = tk.getImage(new URL(DocBase, "images/Release.GIF"));
            ups[1] = tk.getImage(new URL(DocBase, "images/AddNode.GIF"));
            ups[2] = tk.getImage(new URL(DocBase, "images/AddTransition.GIF"));
            ups[3] = tk.getImage(new URL(DocBase, "images/AddEdge.GIF"));
            ups[4] = tk.getImage(new URL(DocBase, "images/AddToken.GIF"));
            ups[5] = tk.getImage(new URL(DocBase, "images/Edit.GIF"));
            ups[6] = tk.getImage(new URL(DocBase, "images/Delete.GIF"));
            ups[7] = tk.getImage(new URL(DocBase, "images/Save.GIF"));
            ups[8] = tk.getImage(new URL(DocBase, "images/Memorize.GIF"));
            ups[9] = tk.getImage(new URL(DocBase, "images/Memback.GIF"));
            ups[10] = tk.getImage(new URL(DocBase, "images/Step.GIF"));
            ups[11] = tk.getImage(new URL(DocBase, "images/StepSing.GIF"));
            downs[0] = tk.getImage(new URL(DocBase, "images/ReleaseSelected.GIF"));
            downs[1] = tk.getImage(new URL(DocBase, "images/AddNodeSelected.GIF"));
            downs[2] = tk.getImage(new URL(DocBase, "images/AddTransitionSelected.GIF"));
            downs[3] = tk.getImage(new URL(DocBase, "images/AddEdgeSelected.GIF"));
            downs[4] = tk.getImage(new URL(DocBase, "images/AddTokenSelected.GIF"));
            downs[5] = tk.getImage(new URL(DocBase, "images/EditSelected.GIF"));
            downs[6] = tk.getImage(new URL(DocBase, "images/DeleteSelected.GIF"));
            downs[7] = tk.getImage(new URL(DocBase, "images/SaveSelected.GIF"));
            downs[8] = tk.getImage(new URL(DocBase, "images/MemorizeSelected.GIF"));
            downs[9] = tk.getImage(new URL(DocBase, "images/MembackSelected.GIF"));
            downs[10] = tk.getImage(new URL(DocBase, "images/StepSelected.GIF"));
            downs[11] = tk.getImage(new URL(DocBase, "images/StepSingSelected.GIF"));
            diss[0] = tk.getImage(new URL(DocBase, "images/ReleaseSelected.GIF"));
            diss[1] = tk.getImage(new URL(DocBase, "images/AddNodeSelected.GIF"));
            diss[2] = tk.getImage(new URL(DocBase, "images/AddTransitionSelected.GIF"));
            diss[3] = tk.getImage(new URL(DocBase, "images/AddEdgeSelected.GIF"));
            diss[4] = tk.getImage(new URL(DocBase, "images/AddTokenSelected.GIF"));
            diss[5] = tk.getImage(new URL(DocBase, "images/EditSelected.GIF"));
            diss[6] = tk.getImage(new URL(DocBase, "images/DeleteSelected.GIF"));
            diss[7] = tk.getImage(new URL(DocBase, "images/SaveSelected.GIF"));
            diss[8] = tk.getImage(new URL(DocBase, "images/MemorizeSelected.GIF"));
            diss[9] = tk.getImage(new URL(DocBase, "images/MembackSelected.GIF"));
            diss[10] = tk.getImage(new URL(DocBase, "images/StepSelected.GIF"));
            diss[11] = tk.getImage(new URL(DocBase, "images/StepSingSelected.GIF"));
        }
        catch (MalformedURLException m) {System.out.println("Wrong URL");}
      } else {
        ups[0] = tk.getImage("images/Release.GIF");
        ups[1] = tk.getImage("images/AddNode.GIF");
        ups[2] = tk.getImage("images/AddTransition.GIF");
        ups[3] = tk.getImage("images/AddEdge.GIF");
        ups[4] = tk.getImage("images/AddToken.GIF");
        ups[5] = tk.getImage("images/Edit.GIF");
        ups[6] = tk.getImage("images/Delete.GIF");
        ups[7] = tk.getImage("images/Save.GIF");
        ups[8] = tk.getImage("images/Memorize.GIF");
        ups[9] = tk.getImage("images/Memback.GIF");
        ups[10] = tk.getImage("images/Step.GIF");
        ups[11] = tk.getImage("images/StepSing.GIF");
        downs[0] = tk.getImage("images/ReleaseSelected.GIF");
        downs[1] = tk.getImage("images/AddNodeSelected.GIF");
        downs[2] = tk.getImage("images/AddTransitionSelected.GIF");
        downs[3] = tk.getImage("images/AddEdgeSelected.GIF");
        downs[4] = tk.getImage("images/AddTokenSelected.GIF");
        downs[5] = tk.getImage("images/EditSelected.GIF");
        downs[6] = tk.getImage("images/DeleteSelected.GIF");
        downs[7] = tk.getImage("images/SaveSelected.GIF");
        downs[8] = tk.getImage("images/MemorizeSelected.GIF");
        downs[9] = tk.getImage("images/MembackSelected.GIF");
        downs[10] = tk.getImage("images/StepSelected.GIF");
        downs[11] = tk.getImage("images/StepSingSelected.GIF");
        diss[0] = tk.getImage("images/ReleaseSelected.GIF");
        diss[1] = tk.getImage("images/AddNodeSelected.GIF");
        diss[2] = tk.getImage("images/AddTransitionSelected.GIF");
        diss[3] = tk.getImage("images/AddEdgeSelected.GIF");
        diss[4] = tk.getImage("images/AddTokenSelected.GIF");
        diss[5] = tk.getImage("images/EditSelected.GIF");
        diss[6] = tk.getImage("images/DeleteSelected.GIF");
        diss[7] = tk.getImage("images/SaveSelected.GIF");
        diss[8] = tk.getImage("images/MemorizeSelected.GIF");
        diss[9] = tk.getImage("images/MembackSelected.GIF");
        diss[10] = tk.getImage("images/StepSelected.GIF");
        diss[11] = tk.getImage("images/StepSingSelected.GIF");
      }

      PBar = new CPictBar(ups, downs, diss, 12);

      this.setLayout(new BorderLayout(3,1));
      this.add("North", PBar);

      Dimension dimvis = new Dimension(1000,1000);
      Vis = new vis(PNet, dimvis);
      this.add("West", Vis);
      Vis.resize(1000,1000);
      // Status
      Status = new Panel();
      Status.setLayout(new FlowLayout());
      StatusLine = new TextField("Everything OK!", 50);
      StatusLine.setEditable(false);
      StatusMode = new TextField("SeqRan", 10);
      StatusMode.setEditable(false);
      StatusDiv = new TextField("Color", 5);
      StatusDiv.setEditable(false);
      StatusCount = new TextField("0", 5);
      StatusCount.setEditable(false);
      Status.add(StatusLine);
      Status.add(StatusMode);
      Status.add(StatusDiv);
      Status.add(StatusCount);
      this.add("South", Status);
      this.setBackground(Color.lightGray);
      Vis.setBackground(Color.white);
   }

   void loadAppletPN(String file) {
      try {
         URL LoadNetAppl = new URL(Docbase, "examples/" + file + ".net");
         PNet = Loading.loadNewNetApplet(LoadNetAppl);
         Vis.setPN(PNet);
         StatusLine.setText("PetriNet loaded.");
      }
      catch (MalformedURLException m) {
         StatusLine.setText("Sorry, the specified URL can't be opened.");
      }
      catch (IOException i) {
         StatusLine.setText("Sorry, the specified URL can't be connected to.");
      }
   }

   boolean loadPN() {
      if (InputFile.length() != 0) {
         try {
            if (Loading.setFile(InputPath)) {
               PNet = Loading.loadNewNet();
               return true;
            } else {
               StatusLine.setText("The selected file \"" + InputPath + "\" is no PetriNet-File.");

               return false;
            }
         }
         catch (FileNotFoundException f) {
            StatusLine.setText("Sorry, the file \"" + InputPath + "\" can't be found.");
            return false;
         }
         catch (IOException i) {
            StatusLine.setText("Sorry, the file \"" + InputPath + "\" can't be opened.");
            return false;
         }
         catch (SecurityException s) {
            (new SorryDialog(this)).show();
            return false;
         }
      } else {
         return false;
      }
   }


   boolean savePN(boolean simple) {
      if (OutputFile.length() != 0) {
         try {
            Saving.setFile(OutputPath);
            Saving.saveNet(PNet);
            return true;
         }
         catch (FileExistsException f) {
            if (!simple || !alreadySaved) {
               ODialog = new OverwriteDialog(this, "File " + OutputPath + " exists! Overwrite?", Saving, PNet);
               ODialog.show();
               return true;
            } else {
               try {
                   Saving.deleteFile();
                   Saving.saveNet(PNet);
                   return true;
               }
               catch (IOException i) {
                   StatusLine.setText("Can't save PetriNet in file \"" + OutputPath + ".\" Writeprotected?");
                   return false;
               }
               catch (SecurityException s) {
                   (new SorryDialog(this)).show();
                   return false;
               }
            }
         }
         catch (IOException i) {
            StatusLine.setText("Can't save PetriNet in file \"" + OutputPath + ".\" Writeprotected?");
            return false;
         }
         catch (SecurityException s) {
            (new SorryDialog(this)).show();
            return false;
         }
      } else return false;
   }

   public void setAlreadySaved(boolean value) {
      alreadySaved = value;
   }

   public void setCount(int c) {
      StepCount = c;
      StatusCount.setText(Integer.toString(StepCount));
      Status.repaint();
   }

   public void runIsHolding() {
     runButtonsUnHighlight();
     runHolding = true;
   }

   public int getDelay() {
      return runDelay;
   }

   public void setDelay(int d) {
      runDelay = d;
   }

   public void setDemo(boolean d) {
      demo = d;
      if (runStep != null) {
          runStep.setDemo(demo);
      }
   }

   public boolean getDemo() {
      return demo;
   }

   public void setStatus(String s) {
      StatusLine.setText(s);
   }

   public runPN getRunStep() {
      return runStep;
   }

   void controlRunStep() {
      if (runStep != null) {
          if (runStep.isAlive()) {
              runStep.restoreVisMode();
              runStep.stop();
         runHolding = false;                             //new 27.10.97 jw
         runButtonsUnHighlight();
              StatusLine.setText("Petri Net stopped.");
          }
      }
   }

   void runSeqRan(int i) {
      runButtonHighlight(i);
      StatusLine.setText("Petri Net is running...");
      runStep.start();
   }

   public void runButtonsUnHighlight() {
      PBar.unHighlight(10);
      PBar.unHighlight(11);
   }

   void runButtonHighlight(int i) {
      PBar.highlightPermanent(i);
      if (i == 10) PBar.unHighlight(11);
    else PBar.unHighlight(10);
   }

   public void paint(Graphics g) {
      StatusCount.setText(Integer.toString(StepCount));
   }


   public boolean mouseDown(Event event, int x, int y) {
      int a;
      if (event.target instanceof CPictButton) {
         if (event.target == (Component)PBar.m_button[0]) {
             if (Vis.setMode(vis.MODE_DRAG, 0)) {
                PBar.enableAll();
                PBar.highlight(0);
                StatusLine.setText("No draw mode selected.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[1]) {
             if (Vis.setMode(vis.NodeMode, 0)) {
                PBar.enableAll();
                PBar.disable(1);
                StatusLine.setText("Draw mode set to Add Node.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[2]) {
             if (Vis.setMode(vis.TransitionMode, 0)) {
                PBar.enableAll();
                PBar.disable(2);
                StatusLine.setText("Draw mode set to Add Transition.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[3]) {
             if (Vis.setMode(vis.EdgeMode, 0)) {
                PBar.enableAll();
                PBar.disable(3);
                StatusLine.setText("Draw mode set to Add Edge.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[4]) {
             if (Vis.setMode(vis.MODE_ITEM, 0)) {
                PBar.enableAll();
                PBar.disable(4);
                StatusLine.setText("Draw mode set to Add Token.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[5]) {
             if (Vis.setMode(vis.MODE_EDIT, 0)) {
                PBar.enableAll();
                PBar.disable(5);
                StatusLine.setText("Draw mode set to Edit.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[6]) {
             if (Vis.setMode(vis.MODE_DELETE, 0)) {
                PBar.enableAll();
                PBar.disable(6);
                StatusLine.setText("Draw mode set to Delete.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[7]) {
             controlRunStep();
             if (!alreadySaved) {
                   StatusLine.setText("Insert file name to save PetriNet to...");
                   Save.show();
                   OutputFile = Save.getFile();
                   try {
                       a = OutputFile.length();
                       OutputPath = Save.getDirectory() + OutputFile;
                       Save.setFile(OutputFile);
                       if (savePN(true)) {
                           alreadySaved = true;
                           StatusLine.setText("Petri Net saved.");
                       } else alreadySaved = false;
                   }
                   catch (NullPointerException n) {
                       StatusLine.setText("No file selected.");
                   }
             } else {
                   if (savePN(true)) {
                        alreadySaved = true;
                        StatusLine.setText("Petri Net saved.");
                   } else alreadySaved = false;
             }
             PBar.enable(7);
             return true;
         }
         if (event.target == (Component)PBar.m_button[8]) {
             controlRunStep();
             if (PNet != null) {
                  OldPNet = (pn)PNet.clone();
                  StatusLine.setText("PetriNet memorized.");
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[9]) {
             if (OldPNet != null) {
                  controlRunStep();
                  PNet = (pn)OldPNet.clone();
                  Vis.setPN(PNet);
                  Vis.repaint();
                  StatusLine.setText("Memorized PetriNet loaded.");
             } else StatusLine.setText("Sorry, no PetriNet was memorized.");
             return true;
         }
         if (event.target == (Component)PBar.m_button[10]) {
             if (ParMan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.PARMAN, false);
                    runSeqRan(10);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
                            runStep.disableSingleStep();
                            runButtonHighlight(10);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.PARMAN, false);
                        runSeqRan(10);
                    }
                }
             }
             if (ParRan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.PARRAN, false);
                    runSeqRan(10);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
                            runStep.disableSingleStep();
                            runButtonHighlight(10);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.PARRAN, false);
                        runSeqRan(10);
                    }
                }
             }
             if (SeqMan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.SEQMAN, false);
            runSeqRan(10);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
                            runStep.disableSingleStep();
                            runButtonHighlight(10);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.SEQMAN, false);
                        runSeqRan(10);
                    }
                }
             }
             if (SeqRan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.SEQRAN, false);
                    runSeqRan(10);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
                            runStep.disableSingleStep();
                            runButtonHighlight(10);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.SEQRAN, false);
                        runSeqRan(10);
                    }
                }
             }
             return true;
         }
         if (event.target == (Component)PBar.m_button[11]) {
             if (ParMan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.PARMAN, true);
                    runSeqRan(11);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
                            runButtonHighlight(11);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.PARMAN, true);
                        runSeqRan(11);
                    }
                }
             }
             if (ParRan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.PARRAN, true);
                    runSeqRan(11);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
                            runButtonHighlight(11);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.PARRAN, true);
                        runSeqRan(11);
                    }
                }
             }
             if (SeqMan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.SEQMAN, true);
                    runSeqRan(11);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
                            runButtonHighlight(11);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.SEQMAN, true);
                        runSeqRan(11);
                    }
                }
             }
             if (SeqRan.getState()) {
                if (runStep == null) {
                    runStep = new runPN(PNet, this, Vis, runPN.SEQRAN, true);
                    runSeqRan(11);
                } else {
                    if (runStep.isAlive()) {
                        if (runHolding) {
                            runHolding = false;
             runButtonHighlight(11);
                            runStep.resume();
                        } else {
                            controlRunStep();
                        }
                    } else {
                        runStep = new runPN(PNet, this, Vis, runPN.SEQRAN, true);
                        runSeqRan(11);
                    }
                }
             }
             return true;
         }
      return false;
      }
   return false;
   }

   public boolean handleEvent(Event event) {
      int a;
      switch(event.id) {
      case Event.ACTION_EVENT:
         if (event.target instanceof MenuItem) {
      		if (event.target instanceof CheckboxMenuItem) {
            	if (event.target == ParRan) {
               	controlRunStep();
                  StatusLine.setText("Step mode set to Parallel Random.");
                  StatusMode.setText("ParRan");
                  ParMan.setState(false);
                  SeqRan.setState(false);
                  SeqMan.setState(false);
            	}
               if (event.target == ParMan) {
                  controlRunStep();
                  StatusLine.setText("Step mode set to Parallel Manual.");
                  StatusMode.setText("ParMan");
                  ParRan.setState(false);
                  SeqRan.setState(false);
                  SeqMan.setState(false);
               }
               if (event.target == SeqRan) {
                  controlRunStep();
                  StatusLine.setText("Step mode set to Sequential Random.");
                  StatusMode.setText("SeqRan");
                  SeqMan.setState(false);
                  ParRan.setState(false);
                  ParMan.setState(false);
               }
               if (event.target == SeqMan) {
                  controlRunStep();
                  StatusLine.setText("Step mode set to Sequential Manual.");
                  StatusMode.setText("SeqMan");
                  SeqRan.setState(false);
                  ParRan.setState(false);
                  ParMan.setState(false);
               }
               if (event.target == Col) {
                  Vis.setColorMode(vis.COLORMODE_STANDARD);
                  StatusLine.setText("Paintings set to Color.");
                  StatusDiv.setText("Color");
                  BW.setState(false);
		  Vis.repaint();
               }
               if (event.target == BW) {
                  Vis.setColorMode(vis.COLORMODE_BLACKANDWHITE);
                  StatusLine.setText("Paintings set to Black/White.");
                  StatusDiv.setText("B/W");
                  Col.setState(false);
		  Vis.repaint();
               }
      		} else {
               if (((String)event.arg).equals("New")) {
                  controlRunStep();
                  PNet = new pn();
                  alreadySaved = false;
                  Vis.setPN(PNet);
                  Vis.repaint();
               }
               if (((String)event.arg).equals("Quit")) {
                  controlRunStep();
                  this.hide();
                  if (! AppletState) {
                   this.dispose();
                   System.exit(0);
                  }
                  return true;
               }
               if (((String)event.arg).equals("Load...")) {
                  controlRunStep();
                  StatusLine.setText("Insert file name to load PetriNet from..");
                  if (! NoFileAccess) {
                     Load.show();
                     InputFile = Load.getFile();
                     try {
                         a = InputFile.length();
                         InputPath = Load.getDirectory() + InputFile;
                         Load.setFile(InputFile);
                         if (loadPN()) {
                            Vis.setPN(PNet);
                            OutputFile = InputFile;
                            OutputPath = InputPath;
                            Save.setDirectory(Load.getDirectory());
                            Save.setFile(InputFile);
                            alreadySaved = false;
                            StatusLine.setText("PetriNet loaded.");
                         }
                     }
                     catch (NullPointerException n) {
                     }
                  } else {
                     if (LoadApplet == null) {
                        LoadApplet = new LoadPetriNetApplet(this, Docbase);
                     }
                     LoadApplet.show();
                 }
               }
               if (((String)event.arg).equals("Save")) {
                  controlRunStep();
                  if (!alreadySaved) {
                      StatusLine.setText("Insert file name to save PetriNet to...");
                      Save.show();
                      OutputFile = Save.getFile();
                      try {
                          a = OutputFile.length();
                          OutputPath = Save.getDirectory() + OutputFile;
                          Save.setFile(OutputFile);
                          if (savePN(true)) {
                              alreadySaved = true;
                              StatusLine.setText("PetriNet saved.");
                          } else alreadySaved = false;
                      }
                      catch (NullPointerException n) {
                          StatusLine.setText("Sorry, there was a problem...");
                      }
                  } else {
                      if (savePN(true)) {
                           alreadySaved = true;
                           StatusLine.setText("Petri Net saved.");
                      } else alreadySaved = false;
                  }
               }
               if (((String)event.arg).equals("Save As...")) {
                  controlRunStep();
                  Save.show();
                  OutputFile = Save.getFile();
                  try {
                      a = OutputFile.length();
                      OutputPath = Save.getDirectory() + OutputFile;
                      Save.setFile(OutputFile);
                      if (savePN(false)) {
                          alreadySaved = true;
                          StatusLine.setText("Petri Net saved.");
                      } else alreadySaved = false;
                  }
                  catch (NullPointerException n) {
                      StatusLine.setText("No file selected.");
                  }
               }
               if (((String)event.arg).equals("Commands")) {
                  if (HelpCom == null) {
                     try {
                        HelpCom = new HelpBox(Docbase);
                     }
                     catch (IOException e) {
                        StatusLine.setText("Can't find file \"Helpme.doc\"");
                     }
                  }
                  if (HelpCom != null) HelpCom.show();
               }
               if (((String)event.arg).equals("About")) {
                  if (HelpAb == null) {
                     HelpAb = new HelpAbout(this);
                  }
                  HelpAb.show();
               }
               if (((String)event.arg).equals("Options")) {
                  if (ShowOp == null) {
                     ShowOp = new ShowDialog(this);
                  }
                  ShowOp.setDelay(runDelay);
                  ShowOp.setDemo(demo);
                  controlRunStep();
                  ShowOp.show();
               }

       		} // end if CheckboxMenuItem
         } /* end if menuitem */

         return super.handleEvent(event);

      case Event.WINDOW_DESTROY:
         if (AppletState) {
            this.hide();
         } else {
            System.exit(0);
         }
         return true;


      default:
      return super.handleEvent(event);
      }

   }



/*   public static void main(String[] args) {

      Editor f = new Editor("PNS - The Petri - Net - Simulator");
      f.resize(700, 600);
      f.show();
   } */
}


class OverwriteDialog extends Dialog {

    boolean yes;
    Button Yes, No;
    Thread th;
    SavePetriNet Save;
    pn PNet;


    public OverwriteDialog(Frame par, String title, SavePetriNet s, pn PN) {
        super(par, "Really..?", true);
        yes = false;
        PNet = PN;
        Save = s;
        this.setLayout(new BorderLayout(15,15));
        Label text = new Label(title);
        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout(FlowLayout.CENTER));
        p1.add(text);
        this.add("Center", p1);
        Yes = new Button("Yes");
        No = new Button("No");
        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.CENTER));
        p2.add(Yes);
        p2.add(No);
        this.add("South", p2);
        this.setResizable(false);
        move(par.location().x + 50, par.location().y + 50);
        this.pack();
    }

    public boolean action(Event e, Object arg) {
        if (e.target == Yes) {
            try {
                   Save.deleteFile();
                   Save.saveNet(PNet);
                }
                catch (IOException i) {
                }
                catch (SecurityException s) {
                    (new SorryDialog((Editor)this.getParent())).show();
                }
            this.hide();
            yes = true;
            return true;
        } else if (e.target == No) {
                this.hide();
                yes = false;
                ((Editor)this.getParent()).setAlreadySaved(false);
                return true;
            } else return false;
    }

    public boolean getDecision() {
        return yes;
    }

}

class SorryDialog extends Dialog {

    Button OK;

    public SorryDialog(Frame parent) {

        super(parent, "PNS-Message", true);
        this.setLayout(new BorderLayout(15,15));
        Panel p1 = new Panel();
        p1.setLayout(new GridLayout(0,1));
        p1.add(new Label("Sorry, you called jPNS as an applet!", Label.CENTER));
        p1.add(new Label(""));
        p1.add(new Label("It is restricted for applets to load or save", Label.CENTER));
        p1.add(new Label("to the local disk. For these features please try", Label.CENTER));
        p1.add(new Label("to run jPNS within the appletviewer", Label.CENTER));
        p1.add(new Label("or better as the application version", Label.CENTER));
        this.add("Center", p1);
        OK = new Button("OK");
        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        p2.add(OK);
        this.add("South", p2);
        this.setResizable(false);
        this.pack();
        move(parent.location().x + 50, parent.location().y + 50);
    }

    public boolean action(Event e, Object arg) {
        if (e.target == OK) {
            this.hide();
            this.dispose();
            return true;
        } else return false;
    }

}


