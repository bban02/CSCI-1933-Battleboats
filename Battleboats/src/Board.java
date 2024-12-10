
public class Board {
    private Cell[][] cell;
    private Boat[] boats;
    private int shots;
    private int ships;
    private int turns;
    public boolean direction = false;
    public int boatsSunk = 0;
    public boolean gameStatus = true;
    public int drones = 1;

    public Board(int rows, int columns){// creating the board
        cell = new Cell[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                cell[i][j] = new Cell(i,j,'_');
            }
        }
    }
    public void placeBoats(){
        boats = new Boat[5];//creating the boat array
        boats[0] = new Boat(2, true);
        boats[1] = new Boat(3, true);
        boats[2] = new Boat(3, true);
        boats[3] = new Boat(4, true);
        boats[4] = new Boat(5, true);

        int min = Math.min(cell.length, cell[0].length);

        if (min == 3){ // these if statements check the smallest dimension of the board and
            ships = 1; // decides how many ships should be placed on the board based on that dimension
        }
        else if (min == 4){
            ships = 2;
        }
        else if (4 < min && min <= 6){
            ships = 3;
        }
        else if (6 < min && min <= 8){
            ships = 4;
        }
        else{
            ships = 5;
        }

        for (int i = 0; i < ships; i++){
            int num = (int) Math.round(Math.random());
            boolean o = false;
            if (num == 0){
                o = true;
            }
            if (num == 1){
                o = false;
            }
            boats[i].setOrientation(o);

            boolean valid = false;
            int x = 0;
            int y = 0;
            while (!valid){
                x = (int) Math.floor(Math.random()*10);
                y = (int) Math.floor(Math.random()*10);
                for(int k = 0; k < boats[i].getSize(); k++){
                    if(boats[i].getOrientation()) {
                        if (x > cell.length - boats[i].getSize() || cell[x+k][y].getStatus() == 'B') {// this checks to see if x and y are in bounds and that the cell[x][y] is not occupied
                            valid = false;
                            break;// if this if statements runs then the for loop will break and will restart the while loop
                        }
                    }
                    if(!boats[i].getOrientation()) {
                        if (y > cell[0].length - boats[i].getSize() || cell[x][y+k].getStatus() == 'B') {
                            valid = false;
                            break;
                        }
                    }
                    valid = true;
                }
            }
            for (int j = 0; j < boats[i].getBoat().length; j++) {
                if (boats[i].getOrientation()) {
                    boats[i].getBoat()[j] = new Cell(x+j, y, 'B');
                    cell[x+j][y].setStatus('B'); // this line places a vertical boat on the board
                }
                if (!boats[i].getOrientation()){
                    boats[i].getBoat()[j] = new Cell(x, y+j, 'B');
                    cell[x][y+j].setStatus('B'); // this line places a horizontal boat on the board
                }
            }
        }
    }
    public int getShips() {
        return ships;
    }
    public String display(){// display displays an empty board that will update when the user fires
        String string = "";
        for(int i = 0; i < cell.length; i++){
            for(int j = 0; j < cell[0].length; j++){
                if(cell[i][j].getStatus() == 'B')
                    string += "_|";
                if(cell[i][j].getStatus() == '_')
                    string += "_|";
                if(cell[i][j].getStatus() == 'H')
                    string += "H|";
                if(cell[i][j].getStatus() == 'M')
                    string += "M|";
            }
            string += "\n";
        }
        return string;
    }
    public String toString(){// toString displays the cheat board that shows where all of the boats are
        String string = "";
            for(int i = 0; i < cell.length; i++){
                for(int j = 0; j < cell[0].length; j++){
                    if(cell[i][j].getStatus() == 'B')
                        string += "B|";
                    if(cell[i][j].getStatus() == '_')
                        string += "_|";
                    if(cell[i][j].getStatus() == 'H')
                        string += "H|";
                    if(cell[i][j].getStatus() == 'M')
                        string += "M|";
                }
                string += "\n";
            }
        return string;
    }
    public int fire(int x, int y) {// fire attacks a single location on the board
        int fire = 0;
        int hits = 0;
        shots++;
        turns++;

        if (x < 0 || x > cell.length - 1 || y < 0 || y > cell[0].length - 1){
            turns++;
            return 0;
        }
        if (cell[x][y].getStatus() == 'B') {
            cell[x][y].setStatus('H');
            fire = 1;
        }
        else if (cell[x][y].getStatus() == '_') {
            cell[x][y].setStatus('M');
            fire = 2;
        }
        else if (cell[x][y].getStatus() == 'H') {
            fire = 3;
            turns++;
        }
        else if (cell[x][y].getStatus() == 'M') {
            fire = 4;
            turns++;
        }

        if (cell[x][y].getStatus() == 'B' || cell[x][y].getStatus() == 'H'){// this nested loop checks the boat array to see if the boat the we just attacked has sunk
            for(int i = 0; i < ships; i++){                                 // it then checks to see if all of the boats are sunk and if they are it is game over
                for(int j = 0; j < boats[i].getBoat().length; j++){
                    if (boats[i].getBoat()[j].getRow() == x && boats[i].getBoat()[j].getColumn() == y){
                        for (int k = 0; k < boats[i].getSize(); k++){
                            if (cell[boats[i].getBoat()[k].getRow()][boats[i].getBoat()[k].getColumn()].getStatus() == 'H'){
                                hits++;
                            }
                            if (hits == boats[i].getSize()){
                                System.out.println("Boat sunk!");
                                boatsSunk++;
                                if(boatsSunk == ships){
                                    System.out.println("Game Over \nShots taken: " + shots + "\nTurns: " + turns);
                                    gameStatus = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return fire;
    }
    public int missile(int x, int y){// missile attacks a 3x3 area
        int hits = 1;
        int hit = 0;
        shots++;
        turns++;
        if (x < 0 || x > cell.length - 1 || y < 0 || y > cell[0].length - 1){
            turns++;
            return 0;
        }
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                hit = fire(x+i,y+j);
                if (hit == 1) {
                    hits++;
                }
            }
        }
        return hits;
    }
    public int drone(int direction, int index){// drone scans a row or a column for any boats and returns the number of spots that contain a boat
        int scan = 0;
        turns++;
        if (drones < 1) {
            return -1;
        }
        if (index < 0 || index > cell.length){
            turns++;
            return 0;
        }
        if (direction == 1){
            for(int i = 0; i < cell.length; i++){
                if (cell[i][index].getStatus() == 'B'){
                    scan++;
                }
            }
        }
        if (direction == 0){
            for(int i = 0; i < cell[0].length; i++){
                if (cell[index][i].getStatus() == 'B'){
                    scan++;
                }
            }
        }
        drones--;
        return scan;
    }
    public int scanner(int x, int y){// scanner will scan a coordinate and return if there is a boat there, if there is it will return the size and the direction of the boat
        int size;
        shots++;
        turns++;
        if (x < 0 || x > cell.length - 1|| y < 0 || y > cell[0].length - 1){
            turns++;
            return 0;
        }
        else {
            size = -1;
        }
        if (cell[x][y].getStatus() == 'B' || cell[x][y].getStatus() == 'H'){
            for(int i = 0; i < ships; i++){
                for(int j = 0; j < boats[i].getBoat().length; j++){
                    if (boats[i].getBoat()[j].getRow() == x && boats[i].getBoat()[j].getColumn() == y){
                        direction = boats[i].getOrientation();
                        size = boats[i].getSize();
                        fire(x,y);
                    }
                }
            }
        }
        return size;
    }
}
