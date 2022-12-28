public class UndoRedoManger {
    Crash crash;
    public UndoRedoManger(Crash crash){
        this.crash = crash;

    }
    public void undo(){
        crash.um.undo();
    }
    public void redo(){
        crash.um.redo();
    }
}
