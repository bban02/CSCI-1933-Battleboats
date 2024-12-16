import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BattleboatsGUI implements ActionListener, MouseListener {
    private final BoardPanel boardPanel;
    private final Label message;
    private final Label stats;
    private final Rectangle[][] rectangles = new Rectangle[10][10];

    private final Board board;
    private boolean revealed;
    //stat tracker variables
    private int hits = 0;
    private int turns = 0;
    private int misses = 0;
    //button clicked variables
    private boolean missile = false;
    private boolean drone = false;
    private boolean scanner = false;
    //drone variables
    private boolean rowB = false;
    private boolean columnB = false;

    Button rowButton = new Button("Row");
    Button columnButton = new Button("Column");


    public BattleboatsGUI() {
		// Create the board
        board = new Board(10, 10);
        board.placeBoats();

        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);

        boardPanel = new BoardPanel(this);
        boardPanel.setBounds(50, 50, 400, 400);
        boardPanel.addMouseListener(this);

		// Initialize the message displayed over the board
        message = new Label("Welcome to Battleboats!");
        message.setBounds(100,0,400,30);
		
        // Label to keep track of game stats
        stats = new Label();
        stats.setText("Turns: " + turns + ", Hits: " + hits + ", Misses: " + misses);
        stats.setBounds(100, 20, 400, 30);

        // Reveal the boats on the board
        Button reveal = new Button("Reveal Board");
        reveal.addActionListener(this);
        reveal.setActionCommand("Reveal");
        reveal.setBounds(475, 475, 100, 50);


        Button drone = new Button("Drone");
        drone.addActionListener(this);
        drone.setActionCommand("Drone");
        drone.setBounds(20, 475, 100, 50);

        rowButton.addActionListener(this);
        rowButton.setActionCommand("Row");
        rowButton.setBounds(125, 475, 50, 25);
        rowButton.setVisible(false);
        columnButton.addActionListener(this);
        columnButton.setActionCommand("Column");
        columnButton.setBounds(180, 475, 50, 25);
        columnButton.setVisible(false);
		
		Button missile = new Button("Missile");
        missile.addActionListener(this);
        missile.setActionCommand("Missile");
        missile.setBounds(370, 475, 100, 50);
	
        Button scanner = new Button("Scanner");
        scanner.addActionListener(this);
        scanner.setActionCommand("Scanner");
        scanner.setBounds(265, 475, 100, 50);

        // Adding buttons and labels to the board
        frame.add(message);
        frame.add(stats);
        frame.add(boardPanel);
        frame.add(reveal);
        frame.add(drone);
        frame.add(missile);
        frame.add(scanner);
        frame.add(rowButton);
        frame.add(columnButton);

        // display the frame
        frame.setVisible(true);

        for(int row = 0; row < rectangles.length; row++) {
            for (int col = 0; col < rectangles[0].length; col++) {
				// Create a new Rectangle(int x, int y, int width, int height) object representing each cell on the board
                rectangles[row][col] = new Rectangle(col * 40, row * 40, 40, 40);
            }
        }
		// Call the repaint method to draw the rectangles for the first time
        boardPanel.repaint();
    }

    public void repaint(Graphics g) {
        for (int row = 0; row < rectangles.length; row++) {
            for (int col = 0; col < rectangles[0].length; col++) {
				// Use the cell status to decide what color to fill the rectangle
				if (board.getCellStatus(row, col) == 'H') {
                    g.setColor(Color.RED);
                }
                else if (board.getCellStatus(row, col) == '_') {
                    g.setColor(Color.BLUE);
                }
                else if (board.getCellStatus(row, col) == 'B') {
                    if (revealed) {
                        g.setColor(Color.GRAY);
                    }
                    else {
                        g.setColor(Color.BLUE);
                    }
                }
                else if (board.getCellStatus(row, col) == 'M') {
                    g.setColor(Color.BLACK);
                }
                // Ddraw a filled in rectangle using the x, y, width, and height of the rectangle
				// Draw an unfilled rectangle to make a border around each cell
                g.fillRect(rectangles[row][col].x, rectangles[row][col].y, rectangles[row][col].width, rectangles[row][col].height);
                g.setColor(Color.WHITE);
                g.drawRect(rectangles[row][col].x, rectangles[row][col].y, rectangles[row][col].width, rectangles[row][col].height);
            }
        }
    }

    public static void main(String[] args) {
        // Starts the game
        BattleboatsGUI game = new BattleboatsGUI();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        String actionName = action.getActionCommand();
        // implements the button functions
        switch (actionName) {
            case "Reveal":
                revealed = true;
                boardPanel.repaint();
                break;
            case "Missile":
                missile = true;
                break;
            case "Drone":
                drone = true;
                rowButton.setVisible(true);
                columnButton.setVisible(true);
                message.setText("Select Row or Column");
                break;
            case "Row":
                if (drone) {
                    rowB = true;
                    message.setText("Row Selected");
                } else {
                    message.setText("Select Drone first");
                }
                break;
            case "Column":
                if (drone) {
                    columnB = true;
                    message.setText("Column Selected");
                } else {
                    message.setText("Select Drone first");
                }
                break;
            case "Scanner":
                scanner = true;
                break;
            // TODO: implement cases for all other buttons
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int s = 0;
        int xCoord = mouseEvent.getX();
        int yCoord = mouseEvent.getY();

        // makes sure the click was inside the board
        if (!boardPanel.contains(xCoord, yCoord)) {
            return;
        }

        // Calculate the row and column position of the click
        int row = yCoord / 40;
        int col = xCoord / 40;

        // Dtermine what attack the user wanted to use and run the relevant method
        if (missile == true) {
            s = board.missile(col, row);
            missile = false;
            message.setText("Missile hit " + (s-1) + " targets");
            hits = hits + (s - 1);
            misses = misses + 9 - (s - 1);
        }
        else if(drone == true) {
            if (rowB == true) {
                s = board.drone(1, row);
                rowB = false;
            }
            else if (columnB == true) {
                s = board.drone(0, col);
                columnB = false;
            }
            drone = false;
            // hide row and col buttons again
            rowButton.setVisible(false);
            columnButton.setVisible(false);
            if (s == -1) {
                message.setText("Out of Drones please try a different attack");
            }
            else {
                message.setText("Drone scanned " + s + " targets in the specified column/row");
            }
        }
        else if(scanner == true) {
            s = board.scanner(col, row);
            scanner = false;
            String direction = "";
                if(board.direction){
                    direction = "horizontal";
                }
                if(!board.direction){
                    direction = "vertical";
                }
                if (s > 0){
                    message.setText("Hit! The boat is " + direction + " and has a size of " + s);
                    hits++;
                }
                else{
                    message.setText("You missed, no boat present.");
                    misses++;
                }
        }
        else {
            s = board.fire(col, row);
            if (s == 1){
                message.setText("Hit!");
                hits++;
            }
            if (s == 2){
                message.setText("Miss!");
                misses++;
            }
            if (s == 3 || s == 4){
                message.setText("You already guessed this cell, lose a turn!");
            }
        }
        turns++;
        stats.setText("Turns: " + turns + ", Hits: " + hits + ", Misses: " + misses);
        this.boardPanel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

class BoardPanel extends JPanel {
    BattleboatsGUI game;

    public BoardPanel(BattleboatsGUI game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.repaint(g);
    }
}
