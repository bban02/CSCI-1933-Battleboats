public class Cell {
    private int column;
    private int row;
    private char status;

    public Cell(int r, int c, char s){
        row = r;
        column = c;
        status = s;
    }
    public void setColumn(int c){
        column = c;
    }
    public int getColumn(){
        return column;
    }
    public void setRow(int r){
        row = r;
    }
    public int getRow(){
        return row;
    }
    public void setStatus(char s){
        status = s;
    }
    public int getStatus(){
        return status;
    }
}
