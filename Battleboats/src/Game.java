import java.util.Scanner;
public class Game {
    public static void main(String[] args){
        int c = 0,r = 0,x,y,s;
        boolean valid = false;
        String words;
        String debug;
        Scanner myScanner = new Scanner(System.in);
        while(!valid) {
            System.out.println("Enter a column and a row number: ");
            c = myScanner.nextInt();
            r = myScanner.nextInt();
            valid = true;
            if (c > 10 || c < 3 || r > 10 || r < 3) {
                System.out.println("invalid size please try again using number between 3 and 10 inclusive.");
                valid = false;
            }
        }
        Board board = new Board(c,r);
        board.placeBoats();
        System.out.println("Would you like to run in debug mode? Type yes or no.");
        debug = myScanner.next();
        if (debug.equals("yes")){
            System.out.println(board);
        }
        System.out.println(board.display());
        System.out.println("(0,0) is the upper left coordinate and (m - 1, n - 1) is the bottom right coordinate of the display.");
        while(myScanner.hasNextLine()) {
            System.out.println("Choose 1 of 4 attack methods: fire (basic attack), missile (area attack), drone (row or column Scanner) or scanner (boat Scanner).");
            words = myScanner.next();
            if(words.equals("fire") || words.equals("Fire")){
                System.out.println("Choose an x and a y coordinate to fire at: ");
                x = myScanner.nextInt();
                y = myScanner.nextInt();
                s = board.fire(x,y);
                if(!board.gameStatus){
                    break;
                }
                if (s == 1){
                    System.out.println("Hit!");
                }
                if (s == 2){
                    System.out.println("Miss!");
                }
                if (s == 3 || s == 4){
                    System.out.println("You already guessed this cell, lose a turn!");
                }
                if (s == 0){
                    System.out.println("coordinates out of bounds, lose a turn");
                }
                if (debug.equals("yes")){
                    System.out.println(board);
                }
                System.out.println(board.display());
            }
            else if(words.equals("missile") || words.equals("Missile")){
                System.out.println("Choose an x and a y coordinate to fire a missile at: ");
                x = myScanner.nextInt();
                y = myScanner.nextInt();
                s = board.missile(x,y);
                if (s == 0){
                    System.out.println("coordinates out of bounds, lose a turn");
                }
                if(!board.gameStatus){
                    break;
                }
                s -= 1;
                System.out.println("You hit " + s + " spots that contain a boat!");
                if (debug.equals("yes")){
                    System.out.println(board);
                }
                System.out.println(board.display());
            }
            else if(words.equals("drone") || words.equals("Drone")){
                System.out.println("Would you like to scan a row or a column? Type 0 for column, and 1 for row:");
                x = myScanner.nextInt();
                System.out.println("Which row or column would you like to scan?");
                y = myScanner.nextInt();
                s = board.drone(x,y);
                if (s == 0){
                    System.out.println("coordinates out of bounds, lose a turn");
                }
                if(!board.gameStatus){
                    break;
                }
                System.out.println("Drone scanned " + s + " targets in specified area.");
                if (debug.equals("yes")){
                    System.out.println(board);
                }
                System.out.println(board.display());
            }
            else if(words.equals("scanner") || words.equals("Scanner")){
                System.out.println("Choose an x and a y coordinate to scan: ");
                x = myScanner.nextInt();
                y = myScanner.nextInt();
                s = board.scanner(x,y);
                if (s == 0){
                    System.out.println("coordinates out of bounds, lose a turn");
                }
                String direction = "";
                if(!board.gameStatus){
                    break;
                }
                if(!board.direction){
                    direction = "horizontal";
                }
                if(board.direction){
                    direction = "vertical";
                }
                if (s > 0){
                    System.out.println("Hit! The boat is " + direction + " and has a size of " + s);
                }
                else{
                    System.out.println("You missed, no boat present.");
                }
                if (debug.equals("yes")){
                    System.out.println(board);
                }
                System.out.println(board.display());
            }
            else{
                System.out.println("invalid firing mode, please try again.");
            }
        }
    }
}
