public class Boat {
    private int size;
    private boolean orientation;
    private Cell[] boat;

    public Boat(int s, boolean o){
        size = s;
        orientation = o;
        boat = new Cell[s];
    }
    public void setSize(int s){
        size = s;
        boat = new Cell[s];
    }
    public int getSize(){
        return size;
    }
    public void setOrientation(boolean o){
        orientation = o;
    }
    public boolean getOrientation(){
        return orientation;
    }
    public Cell[] getBoat(){
        return boat;
    }
}
