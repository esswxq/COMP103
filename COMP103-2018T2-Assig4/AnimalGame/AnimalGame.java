// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 4
 * Name: Xianqiong Wu
 * Username: wuxian1
 * ID: 300403758
 */

/**
 * Guess the Animal Game.
 * The program will play a "guess the animal" game and learn from its mistakes.
 * It has a decision tree for determining the player's animal.
 * When it guesses wrong, it asks the player of another question that would
 *  help it in the future, and adds it to the decision tree. 
 * The program can display the decision tree, and save the tree to a file and load it again,
 *
 * A decision tree is a tree in which all the internal modes have a question, 
 * The answer to the the question determines which way the program will
 *  proceed down the tree.  
 * All the leaf nodes have answers (animals in this case).
 * 
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

public class AnimalGame {

    public DTNode questionsTree;    // root of the decision tree;
    private DTNode currentDTNode;    
    private String text;        
    private DTNode y;       //yes child of current DTNode
    private DTNode n;       //no child of current DTNode
    private Boolean b;      //the player's answer, yes/no
    private final static double dis = 100;
    
    public AnimalGame(){
        setupGUI();
        resetTree();
    }

    /**
     * Set up the interface
     */
    public void setupGUI(){
        UI.addButton("Play", this::play);
        UI.addButton("Print Tree", this::printTree);
        UI.addButton("Draw Tree", this::drawTree);
        UI.addButton("Reset", this::resetTree);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.5);
    }

    /**
     * Makes an initial tree with two question nodes and three leaf nodes.
     */
    public void resetTree(){
        questionsTree = new DTNode("has whiskers",
                                   new DTNode("bigger than person",
                                              new DTNode("tiger"),
                                              new DTNode("cat")),
                                   new DTNode("has trunk",
                                              new DTNode("Elephant"),
                                              new DTNode("Snake")));
    }                   

    /**
     * Play the game.
     * Starts at the top (questionsTree), and works its way down the tree
     *  until it finally gets to a leaf node (with an answer in it)
     * If the current node has a question, then it asks the question in the node,
     * and depending on the answer, goes to the "yes" child or the "no" child.
     * If the current node is a leaf it calls processLeaf on the node 
     */
    public void play () {
        /*# YOUR CODE HERE */  
        currentDTNode = questionsTree;          //start at the top
        while(currentDTNode.isQuestion()){      //work its way down until it is not a question
            UI.print("Your animal: ");          //question
            b = UI.askBoolean(currentDTNode.getText()+" (Y/N)");  //ask question and get player's answer
            if(b){                                         //when answer is yes
                currentDTNode = currentDTNode.getYes();    //go to yes child node          
            }
            else if(!b){                                   //when answer is no
                currentDTNode = currentDTNode.getNo();     //go to no child node              
            }
        }
        if(!currentDTNode.isQuestion()){        //if currentDTNode is answer node
            processLeaf(currentDTNode);         //call processLeaf 
        }        
    }
    
    /**
     * Process a leaf node (a node with an answer in it)
     * Tell the player what the answer is, and ask if it is correct.
     * If it is not correct, ask for the right answer, and a property to distinguish
     *  the guess from the right answer
     * Change the leaf node into a question node asking about that fact,
     *  adding two new leaf nodes to the node, with the guess and the right
     *  answer.
    */
    public void processLeaf(DTNode leaf){    
        //CurrentNode must be a leaf node (an answer node)
        if (leaf==null || leaf.isQuestion()) { return; }
        /*# YOUR CODE HERE */
        text = leaf.getText();                                  
        b = UI.askBoolean("I think I know. Is it a "+text);   //give player the guess
        if(b){              
            UI.println("Yay! I win!");      //if guess is right, win the game
        }
        else if (!b){        //if guess is wrong
            String animal = UI.askString("OK, what animal is it?");   //ask what animal the player is thinking, and get the animal's name
            UI.println("Oh, I can't distinguish a "+text+" from a "+animal);
            UI.println("Tell me something that's true for a "+animal+" but not for a "+text+"?"); 
            String property = UI.askString("Property: ");//get the animal's property
            UI.println("Thank you!");  //finish this round
            y = new DTNode(animal);  //the new question node's yes child is the new animal
            n = new DTNode(text);    //the new question node's no child is the animal which is the guess one 
            currentDTNode.convertToQuestion(property,y,n);   //convert the answer node to question node        
        }        
    }       

    /**  COMPLETION
     * Print out the contents of the decision tree in the text pane.
     * The root node should be at the top, followed by its "yes" subtree, and then
     * its "no" subtree.
     * Each node should be indented by how deep it is in the tree.
     */
    public void printTree(){
        UI.clearText();
        /*# YOUR CODE HERE */
        DTNode d = questionsTree;        //get contents of the questionsTree
        printSubTree(d,"   ");           //call printSubTree method
    }

    /**
     * Recursively print a subtree, given the node at its root
     *  - print the text in the node with the given indentation
     *  - if it is a question node, then 
     *    print its two subtrees with increased indentation
    */
    public void printSubTree(DTNode node, String indent){
        /*# YOUR CODE HERE */
        if(node!=null){            
            UI.println(indent+node);                     
            printSubTree(node.getYes(),indent+"   ");   //recursively print yes node
            printSubTree(node.getNo(),indent+"   ");    //recursively print no node
        }
    }

    /**  CHALLENGE
     * Draw the tree on the graphics pane as boxes, connected by lines.
     * To make the tree fit in a window, the tree should go from left to right
     * (ie, the root should be drawn at the left)
     * The lines should be drawn before the boxes that they are connecting
     */
    public void drawTree(){
        UI.clearGraphics();
        /*# YOUR CODE HERE */
        DTNode d = questionsTree;        //get contents of the questionsTree
        drawSubTree(d,100,300,0);        //call method to draw nodes
    }
    
    /**
     * Recursively draw a subtree, given the node at its root
     *  - print the line which linked nodes
     *  - if it is a question node,
     *    draw its two subtrees with line linked them
    */
    public void drawSubTree(DTNode node,double x,double y,int count){
        if(node!=null){
            node.draw(x,y);              //draw root box                       
            if(node.getYes()!=null){
                UI.drawLine(x,y-7.5,x+80,y-dis/Math.pow(2,count)+7.5);           //draw line 
                drawSubTree(node.getYes(),x+80,y-dis/Math.pow(2,count),count+1); //recursively draw yes node
            }
            if(node.getNo()!=null){              
                drawSubTree(node.getNo(),x+80,y+dis/Math.pow(2,count),count+1);  //draw line 
                UI.drawLine(x,y+7.5,x+80,y+dis/Math.pow(2,count)-7.5);           //recursively draw no node
            }
        }
    }    

    public static void main (String[] args) { 
        new AnimalGame();
    }
}
