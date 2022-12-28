import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Action implements ActionListener {
    Crash m;
    public Action(Crash m){this.m = m;}
    @Override
    public void actionPerformed(ActionEvent e) {
        //Actions of Crash
        if (e.getSource() == m.newFile){
            m.setNewFile();
        }
        if (e.getSource() == m.newWindow){
            m.setNewWindow();
        }
        if (e.getSource() == m.openFile){
            m.setOpenFile();
        }
        if (e.getSource() == m.openFolder){
            m.setOpenFolder();
        }
        if (e.getSource() == m.saveAsFile){
            m.setSaveAsFile();
        }
        if (e.getSource() == m.saveFile){
            m.setSaveFile();
        }
        if (e.getSource() == m.printFile){
            m.setPrintFile();
        }
        if (e.getSource() == m.undo){
            m.urm.undo();
        }
        if (e.getSource() == m.redo){
            m.urm.redo();
        }
        if(e.getSource() == m.cut){
            m.textComponent.cut();
        }
        if (e.getSource() == m.copy){
            m.textComponent.copy();
        }
        if (e.getSource() == m.paste){
            m.textComponent.paste();
        }
        if (e.getSource() == m.selectAll){
            m.textComponent.selectAll();
        }
        if (e.getSource() == m.deleteAll){
            m.textComponent.setText("");
        }
        if (e.getSource() == m.delete){
            m.setDelete();
        }
        if (e.getSource() == m.find){
            m.setFind();
        }
        if (e.getSource() == m.replace){
            m.setReplace();
        }
        if (e.getSource() == m.search){
            m.setSearch();
        }
        if (e.getSource() == m.dateNTime){
            m.setDateNTime();
        }
        if (e.getSource() == m.fontWrap){
            m.textComponent.setLineWrap(true);
        }else {
            m.textComponent.setLineWrap(false);
        }
        if (e.getSource() == m.fontStyle){
            m.setFontStyle();
        }
        if(e.getSource() == m.zoomIn){
            m.fontSize += 2;
            m.textComponent.setFont(new Font(m.family,Font.PLAIN,m.fontSize));
            m.textCompLineShower.setFont(m.textComponent.getFont());
        }
        if (e.getSource() == m.zoomOut){
            m.fontSize -= 2;
            m.textComponent.setFont(new Font(m.family,Font.PLAIN,m.fontSize));
            m.textCompLineShower.setFont(m.textComponent.getFont());
        }
        if (e.getSource() == m.cmdO){
            try {
                Runtime.getRuntime().exec("cmd /c start");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        //Popup Menu Actions
        if (e.getSource() == m.popUndo){
            m.urm.undo();
        }
        if (e.getSource() == m.popRedo){
            m.urm.redo();
        }
        if (e.getSource() == m.popCut){
            m.textComponent.cut();
        }
        if (e.getSource() == m.popCopy){
            m.textComponent.copy();
        }
        if (e.getSource() == m.popPaste){
            m.textComponent.paste();
        }
        if (e.getSource() == m.popFont){
            m.setFontStyle();
        }
        if (e.getSource()  ==m.popFind_Replace){
            m.setFind();
        }
        if (e.getSource() == m.helpMe){
            m.setHelpMe();
        }
        if (e.getSource() == m.about){
            m.setAbout();
        }
    }
}
