import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Objects;

public class Key implements KeyListener {
    Crash m;
    public Key(Crash crash){
        this.m = crash;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_N)){
            m.setNewFile();
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_W)){
            m.setNewWindow();
        }
        if (e.isControlDown() && e.isAltDown() && Objects.equals(c,KeyEvent.VK_O)){
            m.setOpenFolder();
        }
        if (e.isControlDown()&& Objects.equals(c,KeyEvent.VK_O)){
            m.setOpenFile();
        }
        if (e.isControlDown() && e.isShiftDown() && Objects.equals(c,KeyEvent.VK_S)){
            m.setSaveAsFile();
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_S)){
            m.setSaveFile();
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_P)){
            m.setPrintFile();
        }
        if (Objects.equals(c,KeyEvent.VK_DELETE)){
            m.setDelete();
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_Z)){
            m.urm.undo();
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_Y)){
            m.urm.redo();
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_F)){
            m.setFind();
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_R)){
            m.setReplace();
        }
        if (e.isAltDown() && Objects.equals(c,KeyEvent.VK_S)){
            m.setSearch();
        }
        if (Objects.equals(c,KeyEvent.VK_F5)){
            m.setDateNTime();
        }
        if (e.isAltDown() && Objects.equals(c,KeyEvent.VK_F)){
            m.setFontStyle();
        }
        if(e.isControlDown() && Objects.equals(c,KeyEvent.VK_EQUALS)){
            m.fontSize += 2;
            m.textComponent.setFont(new Font(m.family,Font.PLAIN,m.fontSize));
            m.textCompLineShower.setFont(m.textComponent.getFont());
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_MINUS)){
            m.fontSize -= 2;
            m.textComponent.setFont(new Font(m.family,Font.PLAIN,m.fontSize));
            m.textCompLineShower.setFont(m.textComponent.getFont());
        }
        if (e.isControlDown() && Objects.equals(c,KeyEvent.VK_WINDOWS)){
            try {
                Runtime.getRuntime().exec("cmd /c start");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}