// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 6
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.Color;

/** 
 * Calculator for Cambridge-Polish Notation expressions
 * User can type in an expression (in CPN) and the program
 * will compute and print out the value of the expression.
 * The template is based on the version in the lectures,
 *  which only handled + - * /, and did not do any checking
 *  for valid expressions
 * The program should handle a wider range of operators and
 *  check and report certain kinds of invalid expressions
 */

public class CPNCalculator{



    /**
     * Main Read-evaluate-print loop
     * Reads an expression from the user then evaluates it and prints the result
     * Invalid expressions could cause errors when reading.
     * The try-catch prevents these errors from crashing the programe - 
     *  the error is caught, and a message printed, then the loop continues.
     */
    public static void main(String[] args){
        UI.addButton("Quit", UI::quit); 
        UI.setDivider(1.0);
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        while (true){
            UI.println();
            Scanner sc = new Scanner(UI.askString("expr:"));
            try {
                GTNode<String> expr = readExpr(sc);  
                UI.println(" -> " + evaluate(expr));
            }catch(Exception e){UI.println("invalid expression"+e);}
        }
    }




    /**
     * Recursively construct expression tree from scanner input
     */
    public static GTNode<String> readExpr(Scanner sc){
        if (sc.hasNext("\\(")) {                     // next token is an opening bracket
            sc.next();                               // the opening (
            String op = sc.next();                   // the operator
            GTNode<String> node = new GTNode<String>(op);  
            while (! sc.hasNext("\\)")){
                GTNode<String> child = readExpr(sc); // the arguments
                node.addChild(child);          
            }
            sc.next();                               // the closing )
            return node;
        }
        else {                                       // next token must be a number
            return new GTNode<String>(sc.next());
        }
    }

    /**
     * Evaluate an expression and return the value
     * Returns Double.NaN if the expression is invalid in some way.
     */
    public static double evaluate(GTNode<String> expr){
        if (expr==null){ return Double.NaN; }
        if (expr.numberOfChildren()==0){            // must be a number
            try { return Double.parseDouble(expr.getItem());}
            catch(Exception e){return Double.NaN;}
        }
        else {
            double ans = Double.NaN;                // answer if no valid operator
            if (expr.getItem().equals("+")){        // addition operator
                ans = 0;
                for(GTNode<String> child: expr) {
                    ans += evaluate(child);
                }
            }
            else if (expr.getItem().equals("*")) {  // multiplication operator 
                ans = 1;
                for(GTNode<String> child: expr) {
                    ans *= evaluate(child);
                }
            }
            else if (expr.getItem().equals("-")){  // subtraction operator 
                ans = evaluate(expr.getChild(0));
                for(int i=1; i<expr.numberOfChildren(); i++){
                    ans -= evaluate(expr.getChild(i));
                }
            }
            else if (expr.getItem().equals("/")){  // division operator          
                ans = evaluate(expr.getChild(0));
                for(int i=1; i<expr.numberOfChildren(); i++){
                    ans /= evaluate(expr.getChild(i));
                }
            }
            /*# YOUR CODE HERE */

            return ans; 
        }
    }

}

