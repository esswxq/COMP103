// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 3
 * Name: Xianqiong Wu
 * Username: wuxian1    
 * ID: 300403758
 */

import ecs100.*;
import java.awt.Color;
import javax.swing.JButton;

/**
 *  Simple 'Minesweeper' program.
 *  There is a grid of cells, some of which contain a mine.
 *  The user can click on a cell to either expose it or to
 *  mark/unmark it.
 *  
 *  If the user exposes a cell with a mine, they lose.
 *  Otherwise, it is uncovered, and shows a number which represents the
 *  number of mines in the eight cells surrounding that one.
 *  If there are no mines adjacent to it, then all the unexposed cells
 *  immediately adjacent to it are exposed (and and so on)
 *
 *  If the user marks a cell, then they cannot expose the cell,
 *  (unless they unmark it first)
 *  When all squares with mines are marked, and all the squares without
 *  mines are exposed, the user has won.
 */
public class MineSweeper {

    public static final int ROWS = 15;
    public static final int COLS = 15;

    public static final double LEFT = 10; 
    public static final double TOP = 10;
    public static final double CELL_SIZE = 20;

    // Fields
    private boolean marking;

    private Cell[][] cells;

    private JButton mrkButton;
    private JButton expButton;
    Color defaultColor;

    /** 
     * Construct a new MineSweeper object
     * and set up the GUI
     */
    public MineSweeper(){
        setupGUI();
        setMarking(false);
        makeGrid();
    }

    /** setup buttons */
    public void setupGUI(){
        UI.setMouseListener(this::doMouse);
        UI.addButton("New Game", this::makeGrid);
        this.expButton = UI.addButton("Expose", ()->setMarking(false));
        this.mrkButton = UI.addButton("Mark", ()->setMarking(true));
        
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.0);
    }

    /** Respond to mouse events */
    public void doMouse(String action, double x, double y) {
        if (action.equals("released")){
            int row = (int)((y-TOP)/CELL_SIZE);
            int col = (int)((x-LEFT)/CELL_SIZE);
            if (row>=0 && row < ROWS && col >= 0 && col < COLS){
                if (marking) { mark(row, col);}
                else         { tryExpose(row, col); }
            }
        }
    }

    /**
     * Remember whether it is "Mark" or "Expose"
     * Change the colour of the "Mark", "Expose" buttons
     */
    public void setMarking(boolean v){
        marking=v;
        if (marking) {
            mrkButton.setBackground(Color.red);
            expButton.setBackground(null);
        }
        else {
            expButton.setBackground(Color.red);
            mrkButton.setBackground(null);
        }
    }

    // Other Methods

    /** 
     * The player has clicked on a cell to expose it
     * - if it is already exposed or marked, do nothing.
     * - if it's a mine: lose (call drawLose()) 
     * - otherwise expose it (call exposeCellAt)
     * then check to see if the player has won and call drawWin() if they have.
     * (This method is not recursive)
     */
    public void tryExpose(int row, int col){
        /*# YOUR CODE HERE */    
        
        if(cells[row][col].isExposed()||cells[row][col].isMarked()){
            return;  //if it is already exposed or marked, do nothing
        }        
        else if(cells[row][col].hasMine()){
            this.drawLose();//if it's a mine: lose (call drawLose()
        }
        else {
            exposeCellAt(row,col);//otherwise expose it (call exposeCellAt)
        }       
        /*# YOUR CODE END */
        
        if (hasWon()){
            drawWin();
        }
    }

    /** 
     *  Expose a cell, and spread to its neighbours if safe to do so.
     *  It is guaranteed that this cell is safe to expose (ie, does not have a mine).
     *  If it is already exposed, we are done.
     *  Otherwise expose it, and redraw it.
     *  If the number of adjacent mines of this cell is 0, then
     *     expose all its neighbours (which are safe to expose)
     *     (and if they have no adjacent mine, expose their neighbours, and ....)
     */
    public void exposeCellAt(int row, int col){
        /*# YOUR CODE HERE */
        marking=false;
        if(row<0||row>=ROWS||col<0||col>=COLS){return;} //make sure click in bound
        if(cells[row][col].isExposed()){return;} //if it is already exposed, do nothing
        if(!cells[row][col].hasMine()){    //make sure the cell has no mine
            cells[row][col].setExposed(); //make the cell be exposed
            cells[row][col].draw(LEFT+col*CELL_SIZE,TOP+row*CELL_SIZE,CELL_SIZE);//redraw
            if(cells[row][col].getAdjacentMines()==0){//it is a recursion to spread
                exposeCellAt(row-1,col);
                exposeCellAt(row-1,col+1);
                exposeCellAt(row-1,col-1);
                exposeCellAt(row,col-1);
                exposeCellAt(row,col+1);
                exposeCellAt(row+1,col);  
                exposeCellAt(row+1,col+1); 
                exposeCellAt(row+1,col-1); 
            }    
        }
    }

    /**
     * Mark/unmark the cell.
     * If the cell is exposed, don't do anything,
     * If it is marked, unmark it.
     * otherwise mark it and redraw.
     * (Marking cannot make the player win or lose)
     */
    public void mark(int row, int col){
        /*# YOUR CODE HERE */
        marking = true;
        if(cells[row][col].isExposed()){return;}//If the cell is exposed, don't do anything
        else if(cells[row][col].isMarked()){
            cells[row][col].toggleMark();//chsnge boolean
            cells[row][col].draw(LEFT+col*CELL_SIZE,TOP+row*CELL_SIZE,CELL_SIZE);}//redraw
        else {
            cells[row][col].toggleMark();//change boolean
            cells[row][col].draw(LEFT+col*CELL_SIZE,TOP+row*CELL_SIZE,CELL_SIZE);//redraw
        }        
    }
    
    /** 
     * Returns true if the player has won:
     * If all the cells without a mine have been exposed, then the player has won.
     */
    public boolean hasWon(){
        /*# YOUR CODE HERE */
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(!cells[i][j].isExposed()&&!cells[i][j].hasMine()){//if a cell has no mine but hasn't been exposed, can not success unless every cell with no mine has been exposed
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Construct a grid with random mines.
     */
    public void makeGrid(){
        UI.clearGraphics();
        this.cells = new Cell[ROWS][COLS];
        for (int row=0; row < ROWS; row++){
            double y = TOP+row*CELL_SIZE;
            for (int col=0; col<COLS; col++){
                double x =LEFT+col*CELL_SIZE;
                boolean isMine = Math.random()<0.1;     // approx 1 in 10 cells is a mine 
                this.cells[row][col] = new Cell(isMine);
                this.cells[row][col].draw(x, y, CELL_SIZE);
            }
        }
        // now compute the number of adjacent mines for each cell
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                int count = 0;
                //look at each cell in the neighbourhood.
                for (int r=Math.max(row-1,0); r<Math.min(row+2, ROWS); r++){
                    for (int c=Math.max(col-1,0); c<Math.min(col+2, COLS); c++){
                        if (cells[r][c].hasMine())
                            count++;
                    }
                }
                if (this.cells[row][col].hasMine())
                    count--;  // we weren't suppose to count this cell, just the adjacent ones.

                this.cells[row][col].setAdjacentMines(count);
            }
        }
    }

    /** Draw a message telling the player they have won */
    public void drawWin(){
        UI.setFontSize(28);
        UI.drawString("You Win!", LEFT + COLS*CELL_SIZE + 20, TOP + ROWS*CELL_SIZE/2);
        UI.setFontSize(12);
    }

    /**
     * Draw a message telling the player they have lost
     * and expose all the cells and redraw them
     */
    public void drawLose(){
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                cells[row][col].setExposed();
                cells[row][col].draw(LEFT+col*CELL_SIZE, TOP+row*CELL_SIZE, CELL_SIZE);
            }
        }
        UI.setFontSize(28);
        UI.drawString("You Lose!", LEFT + COLS*CELL_SIZE+20, TOP + ROWS*CELL_SIZE/2);
        UI.setFontSize(12);
    }
    
    // Main
    public static void main(String[] arguments){
        new MineSweeper();
    }        
}
