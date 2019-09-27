// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 1
 * Name: Xianqiong Wu
 * Username: wuxian1
 * ID: 300403758
 */

import java.util.*;
import ecs100.*;
import java.awt.Color;

/**
 * This class contains the main method of the program. 
 * 
 * A SlideShow object represents the slideshow application and sets up the buttons in the UI. 
 * 
 * @author pondy
 */
public class SlideShow {

    public static final int LARGE_SIZE = 450;   // size of images during slide show
    public static final int SMALL_SIZE = 100;   // size of images when editing list
    public static final int GAP = 10;           // gap between images when editing
    public static final int COLUMNS = 6;        // Number of columns of thumbnails

    private List <String> images = new ArrayList <String>();//  List of image file names. 
    private int currentImage = -1;     // index of currently selected image.
    // Should always be a valid index if there are any images

    private boolean showRunning;      // flag signalling whether the slideshow is running or not
    
    /*# MY ADDITIONAL CODE HERE */
    private int selectedImage;        //field to store the position where mouse pressed
    private double startX,startY;     
    private double speed;
    
    /**
     * Constructor 
     */
    public SlideShow() {
        /*# YOUR CODE HERE */
        setupGUI();   
    }

    /**
     * Initialises the UI window, and sets up the buttons. 
     */
    public void setupGUI() {
        UI.initialise();

        UI.addButton("Run show",           this::runShow);
        UI.addButton("Edit show",          this::editShow);
        UI.addButton("add before",         this::addBefore);
        UI.addButton("add after",          this::addAfter);
        UI.addButton("move left",          this:: moveLeft);
        UI.addButton("move right",         this:: moveRight);
        UI.addButton("move to start",      this:: moveStart);
        UI.addButton("move to end",        this:: moveEnd);
        UI.addButton("remove",             this::remove);
        UI.addButton("remove all",         this::removeAll);
        UI.addButton("reverse",            this::reverse);
        UI.addButton("shuffle",            this::shuffle);
        UI.addButton("Testing",            this::setTestList);
        UI.addSlider("Speed",500,1000,1000,this::selectSpeed);
        UI.addButton("Quit",               UI::quit);

        UI.setKeyListener(this::doKey);
        UI.setMouseListener(this::doMouse);
        UI.setDivider(0);
        UI.printMessage("Mouse must be over graphics pane to use the keys");
    }

    // RUNNING
    /**
     * Add a slider button for adjusting showRunning speed.
     */
    public double selectSpeed(double sp){
        speed = sp;   
        return speed;
    }
    
    /**
     * As long as the show isn't already running, and there are some
     * images to show, start the show running from the currently selected image.
     * The show should keep running indefinitely, as long as the
     * showRunning field is still true.
     * Cycles through the images, going back to the start when it gets to the end.
     * The currentImage field should always contain the index of the current image.
     */
    public void runShow(){
        /*# YOUR CODE HERE */
        UI.clearGraphics();
        showRunning=true;   //turn showRunning into true state
        if(currentImage<0){     //if currentImage is initialise number
            showRunning=false;   //no run
        }
        while(showRunning&&currentImage>-1){            
            if(currentImage==images.size()-1){       //if currentImage is the last one 
                for(int i=0;i<images.size();i++){
                    currentImage=i;
                    if(!showRunning) break; //end the loop if showRunning is false
                    UI.drawImage(images.get(i),GAP,GAP,LARGE_SIZE,LARGE_SIZE);
                    UI.sleep(speed);
                }
            } 
            else{
                for(int i=currentImage;i<images.size();i++){ //if the currentImage is any one except last one 
                    currentImage=i;
                    if(!showRunning) break;  //end the loop if showRunning is false
                    UI.drawImage(images.get(i),GAP,GAP,LARGE_SIZE,LARGE_SIZE);
                    UI.sleep(speed);
                }                 
            }
        }
    }

    /**
     * Stop the show by changing showRunning to false.
     * Redisplay the list of images, so they can be edited
     */
    public void editShow(){
        /*# YOUR CODE HERE */
        showRunning=false; 
        display();
    }

    /**
     * Display just the current slide if the show is running.
     * If the show is not running, display the list of images
     * (as thumbnails) highlighting the current image
     */
    public void display(){
        /*# YOUR CODE HERE */
        UI.clearGraphics();
        if(showRunning){
            UI.drawImage(images.get(currentImage),GAP,GAP,LARGE_SIZE,LARGE_SIZE); //display the current image in large size
        }        
        else if(!showRunning){
            for(int i=0;i<images.size();i++){                
                int row = i/COLUMNS; //get the row number of thumbnails
                int col = i%COLUMNS; //get the column number of thumbnails
                UI.drawImage(images.get(i),GAP*(col+1)+SMALL_SIZE*col,  //draw it 
                                           GAP*(row+1)+SMALL_SIZE*row,
                                               SMALL_SIZE,SMALL_SIZE);
                if(currentImage==i){     //draw red rectangle to highlight the selected image
                    UI.setColor(Color.red);
                    UI.setLineWidth(GAP/2.0);
                    UI.drawRect(GAP*(col+1)+SMALL_SIZE*col,GAP*(row+1)+SMALL_SIZE*row,SMALL_SIZE,SMALL_SIZE);    
                }            
            }
        }
    }

    // Other Methods (you will need quite a lot of additional methods).
    /*# YOUR CODE HERE */     
    /**
     * Use the key to change currentImage to be the left one and select 
     * it.
     */
    public void goLeft(){    
        currentImage=currentImage-1;//select the image at the left side
        display();
    }

    /**
     * Use the key to change currentImage to be the right one and select
     * it.
     */
    public void goRight(){
        currentImage=currentImage+1;//select the image at the right side
        display();
    }

    /**
     * Use the key to change currentImage to be the first one and select
     * it.
     */
    public void goStart(){
        currentImage=0;//select first image 
        display();
    }

    /**
     * Use the key to change currentImage to be the last one and select 
     * it.
     */
    public void goEnd(){
        currentImage=images.size();//select last image
        display();
    }
    
    /**
     * Use the key to change currentImage to be the upside one and select 
     * it.
     */
    public void goUp(){
        if(currentImage-COLUMNS>=0 && !showRunning){
            currentImage=currentImage-COLUMNS;//select image which is on the top of currentImage
            display();
        }
    }
    
    /**
     * Use the key to change currentImage to be the downside one and select
     * it.
     */
    public void goDown(){
        if(currentImage+COLUMNS<images.size() && !showRunning){
            currentImage=currentImage+COLUMNS; //select image down the previous one
            display();
        }
    }
    
    /**
     * Choose a file to add it before the currentImage and then select
     * the new image.
     */
    public void addBefore(){
        String newImage = UIFileChooser.open();//choose a new file 
        images.add(currentImage-1,newImage);   //add file at left position
        display();                                  
    }
    
    /**
     * Choose a file to add it after the currentImage and then select
     * the new image.
     */
    public void addAfter(){
        String newImage = UIFileChooser.open();  //choose a new file
        images.add(currentImage+1,newImage);     //add file at right position
        currentImage++;                          //select right position
        display();
    }
    
    /**
     * Move current image to its left position and stil select it.
     */
    public void moveLeft(){
        if(currentImage>0 && currentImage<images.size()&&!showRunning){
            String copy = images.get(currentImage);//make the temporary copy of the selected image
            images.remove(currentImage);            //delete the seleted image
            images.add(currentImage-1,copy);        //add the copy to its left position
            currentImage--;                         //select the left position
            display();
        }
    }
    
    /**
     * Move current image to its right position and still select it.
     */
    public void moveRight(){
        if(currentImage>=0 && currentImage<images.size()-1&&!showRunning){
            String copy = images.get(currentImage); //make the temporary copy of the selected image
            images.remove(currentImage);            //delete the selected image
            images.add(currentImage+1,copy);        //add the copy to the next position
            currentImage++;                         //select the next image
            display();
        }        
    }
    
    /**
     * Move current image to the first position and keep select it.
     */
    public void moveStart(){
        if(currentImage>=0 && currentImage<images.size()&&!showRunning){
            String copy = images.get(currentImage);//make the temporary copy of the selected image
            images.remove(currentImage);    //delete the selected image
            images.add(0,copy);             //add the cpoy to the first position
            currentImage=0;                 //select the first image
            display();
        }        
    }
    
    /**
     * Move current image to the last position and keep select it.
     */
    public void moveEnd(){
        if(currentImage>=0 && currentImage<images.size()&&!showRunning){
            String copy = images.get(currentImage);  //make the temporary copy of the selected image
            images.remove(currentImage);      //delete the selected image
            images.add(images.size(),copy);   //add the copy to the end
            currentImage=images.size()-1;     //select the last image
            display();
        }     
    }
    
    /**
     * Delete the current image.
     */
    public void remove(){
        images.remove(currentImage);
        if(currentImage==images.size()){    //if the image which is the last one, select the one before it after deleting it
            currentImage=currentImage-1;
        }
        display();    
    }
    
    /**
     * Delete all the images.
     */
    public void removeAll(){
        images.clear();
        display();
    }
    
    /**
     * Reverse all the images.
     */
    public void reverse(){
        Collections.reverse(images);
        display();
    }
    
    /**
     * Change images position randomly.
     */
    public void shuffle(){
        Collections.shuffle(images); 
        display();
    }

    /**
     * Change currentImage position when mouse do action.
     */
    public void doMouse(String action, double x,double y){    
        if(action.equals("pressed")){
            selectedImage=(int)(y/(GAP+SMALL_SIZE))*COLUMNS+(int)(x/(GAP+SMALL_SIZE));
        }
        //do select
        if(action.equals("released")){
            currentImage=(int)(y/(GAP+SMALL_SIZE))*COLUMNS+(int)(x/(GAP+SMALL_SIZE));//Decide which image the mouse clicked
            if(selectedImage!=currentImage&&currentImage<images.size()){
                String copy = images.get(selectedImage); //make the temporary copy of the selected image
                images.remove(selectedImage);            //delete the selected image
                images.add(currentImage,copy);           //add the copy to the next position
                display();
            }
            else if(selectedImage!=currentImage&&currentImage>=images.size()){
                String copy = images.get(selectedImage);  //make the temporary copy of the selected image
                images.remove(selectedImage);      //delete the selected image
                images.add(images.size(),copy);    //add the copy to the end
                currentImage=images.size()-1;      //select the last image
                display();
            }
        }    
        display();
    }
    
    // More methods for the user interface: keys (and mouse, for challenge)
    /**
     * Interprets key presses.
     * works in both editing the list and in the slide show.
     */  
    public void doKey(String key) {
        if (key.equals("Left"))         goLeft();
        else if (key.equals("Right"))   goRight();
        else if (key.equals("Home"))    goStart();
        else if (key.equals("End"))     goEnd();
        else if (key.equals("Up"))      goUp();
        else if (key.equals("Down"))    goDown();
        else if (key.equals("a"))       addAfter();  //Key a to add image after cureentImage
        else if (key.equals("b"))       addBefore(); //Key b to add image before cureentImage
        else if (key.equals("l"))       moveLeft();  //Key l to move image to left
        else if (key.equals("r"))       moveRight(); //key r to move image to right
        else if (key.equals("s"))       moveStart(); //Key s to move image to the first position
        else if (key.equals("e"))       moveEnd();   //Key e to move image to the end
        else if (key.equals("m"))       remove();    //Key m to delete cureentImage
    }

    /**
     * A method that adds a bunch of names to the list of images, for testing.
     */
    public void setTestList(){
        if (showRunning) return;
        String[] names = new String[] {"Atmosphere.jpg", "BachalpseeFlowers.jpg",
                "BoraBora.jpg", "Branch.jpg", "DesertHills.jpg",
                "DropsOfDew.jpg", "Earth_Apollo17.jpg",
                "Frame.jpg", "Galunggung.jpg", "HopetounFalls.jpg",
                "Palma.jpg", "Sky.jpg", "SoapBubble.jpg",
                "Sunrise.jpg", "Winter.jpg"};

        for(String name : names){
            images.add(name);
        }
        currentImage = 0;
        display();
    }

    public static void main(String[] args) {
        new SlideShow();
    }

}
