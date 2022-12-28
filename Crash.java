import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Vector;

public class Crash extends JFrame{
    Key keyL = new Key(this);
    Mouse mouseL = new Mouse(this);
    JButton themeXMLSelect = new JButton("Theme XML");
    Action actionL = new Action(this);
    JMenuBar mb;
    JMenu file,edit,font,view,help;
    JMenuItem newFile,newWindow,openFile,openFolder,saveFile,saveAsFile,printFile,closeApplication;
    JMenuItem undo,redo,cut,copy,paste,selectAll,delete,deleteAll,find,replace,search,dateNTime;
    JCheckBoxMenuItem fontWrap;
    JMenuItem fontStyle;
    JMenuItem zoomIn,zoomOut,statusBar,cmdO;
    JMenuItem helpMe,about;
    ImageIcon newFileI,newWindowI,openFileI,openFolI,saveFileI,saveAsFileI,printFileI,closeApplicationI,
            undoI,redoI,cutI,copyI,pasteI,selectAllI,deleteI,deleteAllI,findI,replaceI,searchI,dateNTimeI
            ,fontStyleI,
            zoomInI,zoomOutI,statusBarI,
            helpMeI;
    JTextField infoBox;
    JTextArea textComponent;
    JTextArea textCompLineShower;
    JScrollPane jspForTextComp;

    JFileChooser chooser;
    File currentFile;
    int statusUpdate = 0;
    ImageIcon icon = new ImageIcon("imgs/icon.png");
    UndoManager um = new UndoManager();
    UndoRedoManger urm = new UndoRedoManger(this);

    JFrame findReplaceFrame;
    JTabbedPane findReplacePanel;
    JPanel findPanel,replacePanel;
    JButton findSearchButton,ReplaceButton;
    JTextArea findArea,ReplaceFromArea,ReplaceToArea;

    JFrame fontStyleFrame;
    JComboBox<? extends String> fontStyleType;
    JComboBox<? extends String> fontStyleAll;
    JComboBox<? extends String> fontStyleSize;
    JButton ok;
    int fontSize = 18;
    String family = "Arial";
    JPanel statusBarPanel = new JPanel(new GridLayout(1,4));
    JLabel Inline,InColumn,fontSizeLabel,CurrentOS;
    String textCompText;

    JPopupMenu pop;
    JMenuItem popUndo,popRedo,popCut,popCopy,popPaste,popFind_Replace,popFont;
    public Crash(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        infoBox = new JTextField("Info Box");
        infoBox.setFont(new Font(family, Font.PLAIN,10));
        infoBox.setEditable(false);

        textComponent = new JTextArea();
        textComponent.setFont(new Font("Arial",Font.PLAIN,fontSize));
        textComponent.getDocument().addUndoableEditListener(
                e -> um.addEdit(e.getEdit())
        );
        textComponent.addKeyListener(keyL);
        Inline = new JLabel();
        InColumn = new JLabel();
        fontSizeLabel = new JLabel();
        AtomicInteger line = new AtomicInteger();
        AtomicInteger column = new AtomicInteger();
        textComponent.addCaretListener(e -> {
            JTextArea editArea = (JTextArea) e.getSource();
            line.set(1);
            column.set(1);
            try {
                int caretPos = editArea.getCaretPosition();
                line.set(editArea.getLineOfOffset(caretPos));
                column.set(caretPos - editArea.getLineStartOffset(line.get()));
                line.addAndGet(1);

                Inline.setText("Line : " + line);
                InColumn.setText("Column : " + column);
                fontSizeLabel.setText("Font Size : " + fontSize);
            } catch (Exception err) {
                Inline.setText("UNABLE TO GET LINES AND COLUMNS BECAUSE " + err);
            }
        });
        textComponent.addMouseListener(mouseL);

        textCompLineShower = new JTextArea("1");
        textCompLineShower.setBackground(Color.lightGray);
        textCompLineShower.setEditable(false);
        textCompLineShower.setFont(textComponent.getFont());
        textComponent.getDocument().addDocumentListener(new DocumentListener() {
         public String getText() {
            int caretPosition = textComponent.getDocument().getLength();
            Element root = textComponent.getDocument().getDefaultRootElement();
            String text = "1" + System.getProperty("line.separator");
               for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                  text += i + System.getProperty("line.separator");
               }
            return text;
         }
         @Override
         public void changedUpdate(DocumentEvent de) {
            textCompLineShower.setText(getText());
         }
         @Override
         public void insertUpdate(DocumentEvent de) {
            textCompLineShower.setText(getText());
         }
         @Override
         public void removeUpdate(DocumentEvent de) {
            textCompLineShower.setText(getText());
         }
      });

        textComponent.setTabSize(2);

        statusBarPanel.add(Inline);
        statusBarPanel.add(InColumn);
        statusBarPanel.add(fontSizeLabel);
        CurrentOS = new JLabel(System.getProperty("os.name"));
        statusBarPanel.add(CurrentOS);

        jspForTextComp = new JScrollPane();
        jspForTextComp.getViewport().add(textComponent);
        jspForTextComp.setRowHeaderView(textCompLineShower);
        add(infoBox, BorderLayout.NORTH);
        add(jspForTextComp,BorderLayout.CENTER);
        add(statusBarPanel,BorderLayout.SOUTH);

        mb = new JMenuBar();

        file = new JMenu("File");
        newFileI = new ImageIcon("imgs/newFile.png");
        newFile = new JMenuItem("New File",newFileI);
        file.add(newFile);

        newWindowI = new ImageIcon("imgs/newWindow.png");
        newWindow = new JMenuItem("New Window",newWindowI);
        file.add(newWindow);

        openFileI = new ImageIcon("imgs/open.png");
        openFile = new JMenuItem("Open File",openFileI);
        file.add(openFile);

        openFolI = new ImageIcon("imgs/openFol.png");
        openFolder = new JMenuItem("Open Folder",openFolI);
        file.add(openFolder);

        saveFileI = new ImageIcon("imgs/save.png");
        saveFile = new JMenuItem("Save..",saveFileI);
        file.add(saveFile);

        saveAsFileI = new ImageIcon("imgs/saveAs.png");
        saveAsFile = new JMenuItem("Save As..",saveAsFileI);
        file.add(saveAsFile);

        printFileI = new ImageIcon("imgs/print.png");
        printFile = new JMenuItem("Print",printFileI);
        file.add(printFile);

        closeApplicationI = new ImageIcon("imgs/exit.png");
        closeApplication = new JMenuItem("Close Application",closeApplicationI);
        file.add(closeApplication);

        mb.add(file);
        edit = new JMenu("Edit");

        undoI = new ImageIcon("imgs/undo.png");
        undo = new JMenuItem("Undo",undoI);
        edit.add(undo);

        redoI = new ImageIcon("imgs/redo.png");
        redo = new JMenuItem("Redo",redoI);
        edit.add(redo);

        edit.addSeparator();

        cutI = new ImageIcon("imgs/cut.png");
        cut = new JMenuItem("Cut",cutI);
        edit.add(cut);

        copyI = new ImageIcon("imgs/copy.png");
        copy = new JMenuItem("Copy",copyI);
        edit.add(copy);

        pasteI = new ImageIcon("imgs/paste.png");
        paste = new JMenuItem("Paste",pasteI);
        edit.add(paste);

        selectAllI = new ImageIcon("imgs/selectAll.png");
        selectAll = new JMenuItem("Select All",selectAllI);
        edit.add(selectAll);

        deleteI = new ImageIcon("imgs/delete.png");
        delete = new JMenuItem("Delete",deleteI);
        edit.add(delete);

        deleteAllI = new ImageIcon("imgs/deleteAll.png");
        deleteAll = new JMenuItem("Delete All",deleteAllI);
        edit.add(deleteAll);

        edit.addSeparator();

        JLabel findL = new JLabel("Find what?:");
        findI = new ImageIcon("imgs/find.png");
        find = new JMenuItem("Find",findI);
        edit.add(find);
        findPanel = new JPanel(new BorderLayout());
        findArea = new JTextArea();
        findSearchButton = new JButton("Search");
        findPanel.add(findL,BorderLayout.NORTH);
        findPanel.add(new JScrollPane(findArea),BorderLayout.CENTER);
        findPanel.add(findSearchButton,BorderLayout.SOUTH);

        JLabel replaceL1 = new JLabel("Replace From"),replaceL2 = new JLabel("Replace To:");
        JPanel replaceG1 = new JPanel(new BorderLayout()),replaceG2 = new JPanel(new BorderLayout());
        replaceI = new ImageIcon("imgs/replace.png");
        replace = new JMenuItem("Replace",replaceI);
        replacePanel = new JPanel(new BorderLayout());
        ReplaceToArea = new JTextArea();
        ReplaceFromArea = new JTextArea();
        ReplaceButton = new JButton("Replace");
        replaceG1.add(replaceL1,BorderLayout.NORTH);
        replaceG1.add(new JScrollPane(ReplaceFromArea),BorderLayout.CENTER);
        replacePanel.add(replaceG1,BorderLayout.NORTH);
        replaceG2.add(replaceL2,BorderLayout.NORTH);
        replaceG2.add(new JScrollPane(ReplaceToArea),BorderLayout.CENTER);
        replacePanel.add(replaceG2,BorderLayout.CENTER);
        replacePanel.add(ReplaceButton,BorderLayout.SOUTH);

        edit.add(replace);

        edit.addSeparator();

        searchI = new ImageIcon("imgs/browser.png");
        search = new JMenuItem("Search",searchI);

        edit.add(search);

        dateNTimeI = new ImageIcon("imgs/timeNDate.png");
        dateNTime = new JMenuItem("Paste Date And Time",dateNTimeI);
        edit.add(dateNTime);

        mb.add(edit);

        font = new JMenu("Font");

        fontWrap = new JCheckBoxMenuItem("Font Wrap");
        font.add(fontWrap);
        fontStyleI = new ImageIcon("imgs/font.png");
        fontStyle = new JMenuItem("Font Style",fontStyleI);
        font.add(fontStyle);

        mb.add(font);
        view = new JMenu("View");

        zoomInI = new ImageIcon("imgs/zoomIn.png");
        zoomIn = new JMenuItem("Zoom In",zoomInI);
        view.add(zoomIn);

        zoomOutI = new ImageIcon("imgs/zoomOut.png");
        zoomOut = new JMenuItem("Zoom Out",zoomOutI);
        view.add(zoomOut);

        edit.addSeparator();

        statusBarI = new ImageIcon("imgs/statusBar.png");
        statusBar = new JMenuItem("Status Bar",statusBarI);
        view.add(statusBar);

        cmdO = new JMenuItem("Control prompt");
        view.add(cmdO);

        mb.add(view);
        help = new JMenu("Help");
        helpMeI = new ImageIcon("imgs/help.png");
        helpMe = new JMenuItem("Help Me",helpMeI);
        help.add(helpMe);
        about = new JMenuItem("About Us..");
        help.add(about);
        mb.add(help);

        pop = new JPopupMenu();
        popUndo = new JMenuItem("Undo",undoI);
        popRedo = new JMenuItem("Redo",redoI);
        popCut = new JMenuItem("Cut",cutI);
        popCopy = new JMenuItem("Copy",copyI);
        popPaste = new JMenuItem("Paste",pasteI);
        popFont = new JMenuItem("Font",fontStyleI);
        popFind_Replace = new JMenuItem("Find / Replace",findI);

        pop.add(popUndo);
        pop.add(popRedo);
        pop.add(popCut);
        pop.add(popCopy);
        pop.add(popPaste);
        pop.add(popFont);
        pop.add(popFind_Replace);

        setJMenuBar(mb);
        setVisible(true);
        setSize(700,400);
        setTitle("Text Crash");
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setIconImage(icon.getImage());

        newFile.addActionListener(actionL);
        newWindow.addActionListener(actionL);
        openFile.addActionListener(actionL);
        openFolder.addActionListener(actionL);
        saveFile.addActionListener(actionL);
        saveAsFile.addActionListener(actionL);
        printFile.addActionListener(actionL);
        closeApplication.addActionListener(actionL);
        undo.addActionListener(actionL);
        redo.addActionListener(actionL);
        cut.addActionListener(actionL);
        copy.addActionListener(actionL);
        paste.addActionListener(actionL);
        selectAll.addActionListener(actionL);
        delete.addActionListener(actionL);
        deleteAll.addActionListener(actionL);
        find.addActionListener(actionL);
        replace.addActionListener(actionL);
        search.addActionListener(actionL);
        dateNTime.addActionListener(actionL);
        fontWrap.addActionListener(actionL);
        fontStyle.addActionListener(actionL);
        zoomIn.addActionListener(actionL);
        zoomOut.addActionListener(actionL);
        statusBar.addActionListener(actionL);
        cmdO.addActionListener(actionL);
        helpMe.addActionListener(actionL);
        about.addActionListener(actionL);

        popUndo.addActionListener(actionL);
        popRedo.addActionListener(actionL);
        popCut.addActionListener(actionL);
        popCopy.addActionListener(actionL);
        popPaste.addActionListener(actionL);
        popFont.addActionListener(actionL);
        popFind_Replace.addActionListener(actionL);

        if (currentFile == null){
            System.out.println("File is null");
        }else {
            textComponent.addCaretListener(e -> statusUpdate = 1);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ImageIcon icon = new ImageIcon("imgs/exit_question.png");
                int a = JOptionPane.showConfirmDialog(null,"<html><i>Save File!?</i></html>","Crash",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
                if (a == 0){
                    setSaveFile();
                } else if (a == 1) {
                    System.out.println("Exit Without Save");
                }
            }
        });
    }
    public static void main(String[] args) {
        Crash crash = new Crash();
        if (args == null){
            System.out.println("No File Association");
        }else {
            for (String arg : args) {
                crash.setOpenFileWithArgs(new File(arg));
            }
        }
    }
    public void setNewFile(){
        chooser = null;
        currentFile = null;
        textComponent.setText("");
        infoBox.setText("New File Created");
    }
    public void setNewWindow(){
        setDefaultCloseOperation(2);
        new Crash();
        infoBox.setText("New Window Created");
    }
    public void setOpenFile(){
        chooser = new JFileChooser(System.getProperty("user.home"));
        if (currentFile == null){
            int selectFile = chooser.showOpenDialog(null);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (selectFile == JFileChooser.APPROVE_OPTION) {
                currentFile = new File(chooser.getSelectedFile().getAbsolutePath());
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(currentFile));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                String st;
                while (true) {
                    try {
                        if ((st = br.readLine()) == null) break;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Objects.equals(textComponent.getText(), "")){
                        textComponent.setText(st);
                    }else {textComponent.setText(textComponent.getText() +"\n"+ st);}
                }
                infoBox.setText("Opened File ** " + currentFile.getAbsolutePath());
                setTitle("Current File : " + currentFile.getAbsolutePath());
        }else {
                infoBox.setText("Cancel??");
            }
        }else {
            textComponent.setText("");
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(currentFile));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                String st;
                while (true) {
                    try {
                        if ((st = br.readLine()) == null) break;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Objects.equals(textComponent.getText(), "")){
                        textComponent.setText(st);
                    }else {textComponent.setText(textComponent.getText() +"\n"+ st);}
                }
                infoBox.setText("Opened File ** " + currentFile.getAbsolutePath());
                setTitle("Current File : " + currentFile.getAbsolutePath());
        }
}
        public class FileTree extends JPanel {
  /** Construct a FileTree */
  public FileTree(File dir) {
    setLayout(new BorderLayout());

    // Make a tree list with all the nodes, and make it a JTree
    JTree tree = new JTree(addNodes(null, dir));

    // Add a listener
    tree.addTreeSelectionListener(e -> {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

      tree.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e1) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
            JPopupMenu popupMenu = new JPopupMenu();
      JMenuItem openFolder = new JMenuItem("Open Folder...");
      popupMenu.add(openFolder);
      openFolder.addActionListener(e2 -> {
          try {
              File file1 = new File(String.valueOf(node.getParent()));
                  Desktop.getDesktop().browse(new URI(file1.getAbsolutePath().replace("\\","/")));
          } catch (Exception ex) {
              throw new RuntimeException(ex);
          }
      });
            if (e1.getButton() == MouseEvent.BUTTON3){
                popupMenu.show(Crash.this,node.getLevel(),node.getDepth());
            }
        }

        @Override
        public void mousePressed(MouseEvent e1) {

        }

        @Override
        public void mouseReleased(MouseEvent e1) {

        }

        @Override
        public void mouseEntered(MouseEvent e1) {

        }

        @Override
        public void mouseExited(MouseEvent e1) {

        }
    });

      try{
          File file = new File(node.getParent() + File.separator + node);
          if (file.isFile()){
              currentFile = file;
              setOpenFile();
          }
      }catch (Exception iDoNotKnow){
          iDoNotKnow.printStackTrace();
      }
    });

    // Lastly, put the JTree into a JScrollPane.
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.getViewport().add(tree);
    add(BorderLayout.CENTER, scrollPane);
  }

  /** Add nodes from under "dir" into curTop. Highly recursive. */
  DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
    String curPath = dir.getPath();
    DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
    if (curTop != null) { // should only be null at root
      curTop.add(curDir);
    }
    Vector<String> ol = new Vector<>();
    String[] tmp = dir.list();
    for (int i = 0; i < (tmp != null ? tmp.length : 0); i++)
      ol.addElement(tmp[i]);
    ol.sort(String.CASE_INSENSITIVE_ORDER);
    File f;
    Vector<String> files = new Vector<>();
    // Make two passes, one for Dirs and one for Files. This is #1.
    for (int i = 0; i < ol.size(); i++) {
      String thisObject = ol.elementAt(i);
      String newPath;
      if (curPath.equals("."))
        newPath = thisObject;
      else
        newPath = curPath + File.separator + thisObject;
      if ((f = new File(newPath)).isDirectory())
        addNodes(curDir, f);
      else
        files.addElement(thisObject);
    }
    // Pass two: for files.
    for (int num = 0; num < files.size(); num++)
      curDir.add(new DefaultMutableTreeNode(files.elementAt(num)));
    return curDir;
  }

  public Dimension getMinimumSize() {
    return new Dimension(200,200);
  }

  public Dimension getPreferredSize() {
    return new Dimension(200,200);
  }
}
void FileTreeP(String[] av){
        JFileChooser jrr = new JFileChooser();
        jrr.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jrr.showOpenDialog(null);
        FileTree fileTree = new FileTree(new File(jrr.getSelectedFile().getAbsolutePath()));
        infoBox.setText("Opened Folder :" + jrr.getSelectedFile().getAbsolutePath());
        if (av.length == 0) {
            add(fileTree,BorderLayout.LINE_START);
        }  else {
            for (String s : av) fileTree = new FileTree(new File(s));
        add(fileTree,BorderLayout.LINE_START);
    }
    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
        public void setOpenFolder(){
            chooser = null;
            FileTreeP(new String[0]);
            chooser = null;
    }
        public void setOpenFileWithArgs(File file){
            currentFile = file;
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(currentFile));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                String st;
                while (true) {
                    try {
                        if ((st = br.readLine()) == null) break;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (Objects.equals(textComponent.getText(), "")){
                        textComponent.setText(st);
                    }else {textComponent.setText(textComponent.getText() +"\n"+ st);}
                }
                infoBox.setText("Opened File ** " + currentFile.getAbsolutePath());
                setTitle("Current File : " + currentFile.getAbsolutePath());
        }
        @SuppressWarnings("ResultOfMethodCallIgnored")
        public void setSaveAsFile(){
            chooser = new JFileChooser(System.getProperty("user.home"));
            int saveFile = chooser.showSaveDialog(null);
            if (saveFile == JFileChooser.APPROVE_OPTION) {
                currentFile = new File(chooser.getCurrentDirectory().getPath() + "\\" + chooser.getSelectedFile().getName());
                try {
                    currentFile.createNewFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Path filePath = Path.of(currentFile.getAbsolutePath());
                String content = textComponent.getText();

                try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
                    fileWriter.write(content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try (FileWriter fileWriter = new FileWriter(filePath.toFile());
                     PrintWriter printWriter = new PrintWriter(fileWriter)) {
                    printWriter.print(content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                infoBox.setText("Saved file ** " + currentFile.getAbsolutePath());
                setTitle("Current File  : " + currentFile.getAbsolutePath());
            } else if (saveFile == JFileChooser.CANCEL_OPTION) {
                chooser = null;
            }
        }
        public void setSaveFile(){
            if (currentFile == null){
                setSaveAsFile();
            }else {
                Path filePath = Path.of(currentFile.getAbsolutePath());
                String content = textComponent.getText();

                try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
                    fileWriter.write(content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try (FileWriter fileWriter = new FileWriter(filePath.toFile());
                     PrintWriter printWriter = new PrintWriter(fileWriter)) {
                    printWriter.print(content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                infoBox.setText("Saved :" + currentFile.getAbsolutePath());
            }
        }
        public void setPrintFile(){
            infoBox.setText("Function Unable in your system");
        }
        public void setCloseApplication(){
            System.exit(0);
        }
        public void setDelete(){
            if (textComponent.getSelectedText() == null){
                int option = JOptionPane.showConfirmDialog(null,"Delete Document?","Question",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (option == 0){
                    currentFile = null;
                    textComponent.setText("");
                    infoBox.setText("Deleted File");
                }
            }else {
                textComponent.setText(textComponent.getText().replace(textComponent.getSelectedText(),""));
                infoBox.setText("Deleted Text " + textComponent.getSelectedText());
            }
        }
        public void setFind(){
            findReplaceFrame = new JFrame();
            findReplaceFrame.setVisible(true);
            findReplaceFrame.setLocationRelativeTo(null);
            findReplaceFrame.setSize(400,300);

            JLabel label = new JLabel("Find:");

            findReplacePanel = new JTabbedPane();
            findReplacePanel.add("Find",findPanel);
            findReplacePanel.add("Replace",replacePanel);

            infoBox.setText("Find And Replace Frame Starting");
            findSearchButton.addActionListener(e -> {
                Highlighter.HighlightPainter painter =
                        new DefaultHighlighter.DefaultHighlightPainter( Color.ORANGE );
                String searchWord = findArea.getText();
                int offset = textComponent.getText().indexOf(searchWord);
                int length = searchWord.length();

                while ( offset != -1)
                {
                    try
                    {
                        textComponent.getHighlighter().addHighlight(offset, offset + length, painter);
                        offset = textComponent.getText().indexOf(searchWord, offset+1);
                    }
                    catch(BadLocationException ble) {
                        ble.printStackTrace();
                    }
                }
                if (length == 0){
                    JOptionPane.showConfirmDialog(null,"No text Found","Err",JOptionPane.OK_OPTION,JOptionPane.ERROR_MESSAGE);
                }
            });
            ReplaceButton.addActionListener(e -> {
                String searchWord = ReplaceFromArea.getText();
                String replaceWorld = ReplaceToArea.getText();
                    try {
                        textComponent.setText(textComponent.getText().replace(searchWord, replaceWorld));
                    }
                    catch(Exception ble) {
                        ble.printStackTrace();
                }
            });
            findReplaceFrame.setIconImage(icon.getImage());
            findReplaceFrame.add(findReplacePanel);
            infoBox.setText("Find and Replace");
        }
        public void setReplace(){
            findReplaceFrame = new JFrame();
            findReplaceFrame.setVisible(true);
            findReplaceFrame.setLocationRelativeTo(null);
            findReplaceFrame.setSize(400,300);

            findReplacePanel = new JTabbedPane();
            findReplacePanel.add("Replace",replacePanel);
            findReplacePanel.add("Find",findPanel);

            infoBox.setText("Find And Replace Frame Starting");

            ReplaceButton.addActionListener(e -> {
                String searchWord = ReplaceFromArea.getText();
                String replaceWorld = ReplaceToArea.getText();
                    try {
                        textComponent.setText(textComponent.getText().replace(searchWord, replaceWorld));
                    }
                    catch(Exception ble) {
                        ble.printStackTrace();
                }
            });
            findSearchButton.addActionListener(e -> {
                Highlighter.HighlightPainter painter =
                        new DefaultHighlighter.DefaultHighlightPainter( Color.ORANGE );
                String searchWord = findArea.getText();
                int offset = textComponent.getText().indexOf(searchWord);
                int length = searchWord.length();

                while ( offset != -1)
                {
                    try
                    {
                        textComponent.getHighlighter().addHighlight(offset, offset + length, painter);
                        offset = textComponent.getText().indexOf(searchWord, offset+1);
                    }
                    catch(BadLocationException ble) {
                        ble.printStackTrace();
                    }
                }
                if (length == 0){
                    JOptionPane.showConfirmDialog(null,"No text Found","Err",JOptionPane.OK_OPTION,JOptionPane.ERROR_MESSAGE);
                }
            });
            findReplaceFrame.setIconImage(icon.getImage());
            findReplaceFrame.add(findReplacePanel);
            infoBox.setText("Find And Replace");
        }
        public void setSearch(){
            if (textComponent.getSelectedText() == null){
                String searchInput = JOptionPane.showInputDialog("<html><i>Search What?</i></html>");
                try {
                    Desktop.getDesktop().browse(new URI("https://www.google.com/search?q="+searchInput.replace(" ","+")));
                    infoBox.setText("Searching " + searchInput);
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }else {
                String searchInput = textComponent.getSelectedText();
                try {
                    Desktop.getDesktop().browse(new URI("https://www.google.com/search?q="+searchInput.replace(" ","+")));
                    infoBox.setText("Searching " + searchInput);
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void setDateNTime(){
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E,dd MM yyyy;hh:mm.ss");
            String updated = localDateTime.format(formatter);
            textComponent.setText(textComponent.getText() + updated);
            infoBox.setText("Inserted Date And Time");
        }
        public void setFontStyle(){
            fontStyleFrame = new JFrame();
            fontStyleFrame.setVisible(true);
            fontStyleFrame.setSize(400,300);
            fontStyleFrame.setDefaultCloseOperation(2);
            fontStyleFrame.setIconImage(icon.getImage());
            JPanel container = new JPanel(new GridLayout(7,1));

            JLabel fSTL = new JLabel("Font Family:");
            String[] ge = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            fontStyleType = new JComboBox<>(ge);

            JLabel fSSL = new JLabel("Font Size:");
            String[] AbS = new String[33];
            int[] sizeAb = {8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58,60,62,64,66,68,70,72};
            for (int i = 0; i < sizeAb.length; i++) {
                AbS[i] = String.valueOf(sizeAb[i]);
            }
            fontStyleSize = new JComboBox<>(AbS);
            if (fontSize >= 8 && fontSize <= 72) {
                fontStyleSize.setSelectedIndex(fontSize / 2 - 4);
            }else {
                fontStyleSize.setSelectedIndex(6);
            }
            JLabel fSAL = new JLabel("Font Type:");
            String[] fontStyleFormats = {"<html>Plain</html>","<html><b>Bold</b></html>","<html><i>Italic</i></html>"};
            fontStyleAll = new JComboBox<>(fontStyleFormats);

            ok = new JButton("Ok");

            container.add(fSTL);
            container.add(fontStyleType);
            container.add(fSSL);
            container.add(fontStyleSize);
            container.add(fSAL);
            container.add(fontStyleAll);
            container.add(ok);
            ok.addActionListener(e -> {
                family = (String) fontStyleType.getSelectedItem();
                fontSize = Integer.parseInt(Objects.requireNonNull(fontStyleSize.getSelectedItem()).toString());
                int type = fontStyleAll.getSelectedIndex();

                textComponent.setFont(new Font(family,type,fontSize));

                textCompLineShower.setFont(textComponent.getFont());
                fontStyleFrame.dispose();
            });
            fontStyleFrame.add(container);
            infoBox.setText("Font Style");
        }
        public void setHelpMe(){
        //Creating Help Me frame
            JFrame helpMe= new JFrame("Help Me...");
            helpMe.setVisible(true);
            helpMe.setSize(400,400);
            helpMe.setIconImage(icon.getImage());
            helpMe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            helpMe.setLocationRelativeTo(null);
            helpMe.setLayout(new BorderLayout());

            //Creating tabbed Pane for different panel such as Mouse,Keyboard
            JTabbedPane tabbedPane = new JTabbedPane();

            JPanel mouseInfo = new JPanel(new BorderLayout()),keyInfo = new JPanel(new BorderLayout());
            String mouseInfoString = """
                    <html>
                    There are many menus from which you can do many different tasks.
                    <b>File Menu:</b>
                    It contains New File From which you can create a new file
                    New Window from which you can create a new window
                    Open File from which you can open any existing file
                    Open Folder from which you can open any folder
                    Save from which you can save any file which is open
                    Save As from which you can save a file to your computer
                    Print from which you can print your file
                    Close Application from which you can close Text Crash
                    <b>Edit Menu</b>
                    It contains undo,redo,paste,cut,copy,paste,selectAll,delete and delete All
                    It also have Find from which you can find any text in a opened file (It is case sensitive)
                    Replace from which you can replace any text in a opened file (It is also case sensitive)
                    Search from which you can search any thing if you have selected a text than that text is search
                    else a pop up menu as what to search
                    Date And Time will paste current date and time.
                    <b>Font Menu:</b>
                    From here you can customize you font
                    <b>View Menu:</b>
                    Zoom In and Out your text and open command prompt also
                    <b>Click:</b>
                    Right click on text box to get a pop up menu to quickly edit your text
                    </html>""";
            mouseInfo.add(new JScrollPane(new JLabel(mouseInfoString)),BorderLayout.CENTER);

            String keyS = """
                    <html>
                    There are many menus from which you can do many different tasks.
                    <b>File Menu:</b>
                    It contains New File From which you can create a new file <i>short cut is Control + N</i>
                    New Window from which you can create a new window <i>short cut is Control + W</i>
                    Open File from which you can open any existing file <i>short cut is Control + O</i>
                    Open Folder from which you can open any folder <i>short cut is Control + Alt + O</i>
                    Save from which you can save any file which is open <i>short cut is Control + S</i>
                    Save As from which you can save a file to your computer <i>short cut is Control + Shift + S</i>
                    Print from which you can print your file <i>short cut is Control + P</i>
                    Close Application from which you can close Text Crash <i>short cut is Alt + f4</i>
                    <b>Edit Menu</b>
                    It contains undo<i>short cut is Control + Z</i>
                    ,redo<i>short cut is Control + Y</i>
                    ,paste<i>short cut is Control + P</i>
                    ,cut <i>short cut is Control + X</i>,
                    copy <i>short cut is Control + C</i>,
                    paste <i>short cut is Control + V</i>,
                    selectAll <i>short cut is Control + A</i>.
                    It also have Find from which you can find any text in a opened file (It is case sensitive) <i>short cut is Control + F</i>
                    Replace from which you can replace any text in a opened file (It is also case sensitive) <i>short cut is Control + R</i>
                    Search from which you can search any thing if you have selected a text than that text is search <i>short cut is Alt + S</i>
                    else a pop up menu as what to search
                    Date And Time will paste current date and time.<i>short cut is f5</i>
                    <b>Font Menu:</b>
                    From here you can customize you font <i>short cut is Alt + F</i>
                    <b>View Menu:</b>
                    Zoom In <i>short cut is Control + plus</i>
                     and Out <i>short cut is Control + minus</i> your text and open command prompt also</html>""";
            keyInfo.add(new JScrollPane(new JLabel(keyS)),BorderLayout.CENTER);

            tabbedPane.add("Mouse",mouseInfo);
            tabbedPane.add("Key",keyInfo);

            helpMe.add(tabbedPane);
        }
        public void setAbout(){
            JFrame frame = new JFrame("About");
            frame.setSize(400,400);
            frame.setVisible(true);
            frame.setIconImage(icon.getImage());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new JLabel(icon),BorderLayout.NORTH);
            frame.add(new JLabel("This is an open source application in java\nMade for test purpose Only"),BorderLayout.CENTER);
            frame.add(new JLabel("By K.P [Kushagra Pal]"),BorderLayout.SOUTH);
        }
}