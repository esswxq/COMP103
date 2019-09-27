// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 2
 * Name: Xianqiong Wu 
 * Username: wuxian1
 * ID: 300403758
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/** Genealogy:
 * Prints out information from a genealogical database
 */

public class Genealogy  {

    // all the people:  key is a name,  value is a Person object
    private final Map<String, Person> database = new HashMap<String, Person>();

    private String selectedName;  //currently selected name.

    private boolean databaseHasBeenFixed = false;
    
    private Set<String> missPerson = new HashSet<String>(); //creat a new set for person who is not in databse but to be added as a child of someone 

    /**
     * Constructor
     */
    public Genealogy() {
        loadData();
        setupGUI();
    }

    /**
     * Buttons and text field for operations.
     */
    public void setupGUI(){
        UI.addButton("Print all names", this::printAllNames);
        UI.addButton("Print all details", this::printAllDetails);
        UI.addTextField("Name", this::selectPerson);
        UI.addButton("Parent details", this::printParentDetails);
        UI.addButton("Add child", this::addChild);
        UI.addButton("Find & print Children", this::printChildren);
        UI.addButton("Fix database", this::fixDatabase);
        UI.addButton("Print GrandChildren", this::printGrandChildren);
        UI.addButton("Print Missing", this::printMissing);
        UI.addButton("Print Ancestors", this::printAncestors);
        UI.addButton("Print Descendants", this::printDescendants);
        UI.addButton("Clear Text", UI::clearText);
        UI.addButton("Reload Database", this::loadData);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    /** 
     *  Load the information from the file "database.txt".
     *        Each line of the file has information about one person:
     *        name, year of birth, mother's name, father's name
     *        (note: a '-' instead of a name means  the mother or father are unknown)
     *        For each line,
     *         - construct a new Person with the information, and
     *   - add to the database map.
     */
    public void loadData() {
        try{
        Scanner sc = new Scanner(new File("database.txt"));
        // read the file to construct the Persons to put in the map
        /*# YOUR CODE HERE */
        while(sc.hasNext()){
            String info = sc.nextLine();    //get whole line info
            Scanner scper = new Scanner(info);    //make a new scanner for whole line
            String name = scper.next();      //get name of person
            int year = scper.nextInt();      //get DOB
            String mother = scper.next();    //get mother 
            String father = scper.next();    //get father
            Person object = new Person(name,year,mother,father);   //create a new person object for database
            database.put(name,object);  //add each person into database
        }    
        /*# MY CODE END */
        sc.close();
        UI.println("Loaded "+database.size()+" people into the database");
        }catch(IOException e){throw new RuntimeException("Loading database.txt failed" + e);}
    }

    /** Prints out names of all the people in the database */
    public void printAllNames(){
        for (String name : database.keySet()) {UI.println(name);}  //print each name of person in database
        UI.println("-----------------");
    }

    /** Prints out details of all the people in the database */
    public void printAllDetails(){
        /*# YOUR CODE HERE */
        for (String name : database.keySet()) {UI.println(database.get(name).toString());}   //print each person's details              
        UI.println("-----------------");
    }

    /**
     * Store value (capitalised properly) in the selectedName field.
     * If there is a person with that name currently in people,
     *  then print out the details of that person,
     * Otherwise, offer to add the person:
     * If the user wants to add the person,
     *  ask for year of birth, mother, and father
     *  create the new Person,
     *  add to the database, and
     *  print out the details of the person.
     * Hint: it may be useful to make an askPerson(String name) method
     * Hint: remember to capitalise the names that you read from the user
     */
    public void selectPerson(String value){
        selectedName = capitalise(value); 
        /*# YOUR CODE HERE */        
        for(String name : database.keySet()){
            if(database.containsKey(selectedName)){
                UI.println(database.get(selectedName).toString()); //get selected person's detail which is in the database
                return;
            }
            else if(value==null){UI.println("No person called null");}//if no name input
            else if(!database.containsKey(selectedName)){   //if the name is not contained in database
                UI.println("That person is not known");  
                Boolean b = UI.askBoolean("Add the person?");  //ask if user would like to add a new person into database
                if(b){
                    int year = UI.askInt("Year of birth:");  //ask DOB
                    String mother = UI.askString("Mother's name (or -):");  //ask new person's mother
                    String father = UI.askString("Father's name (or -):");  //ask new person's father
                    Person object = new Person(selectedName,year,mother,father);  //create this new person
                    database.put(selectedName,object);   //add to database
                    UI.println("Added: "+database.get(selectedName).toString()); //print
                    return;
                }      
                else{break;}
            }
        }
        UI.println("-----------------");
    }

    /**
     * Print all the details of the mother and father of the person
     * with selectedName (if there is one).
     * (If there is no person with the current name, print "no person called ...")
     * If the mother or father's names are unknown, print "unknown".
     * If the mother or father names are known but they are not in
     *  the database, print "...: No details known".
     */
    public void printParentDetails(){
        /*# YOUR CODE HERE */        
        for(String name:database.keySet()){
            if(database.containsKey(selectedName)){
                if(database.get(selectedName).getMother().equals("-")&&    //mother and father's name are "-"
                   database.get(selectedName).getFather().equals("-")){
                    UI.println("M: unknown");  
                    UI.println("F: unknown");
                    return;
                }
                else if(!database.get(selectedName).getMother().equals("-")&&     //has both mother and father
                        !database.get(selectedName).getFather().equals("-")&&
                        database.containsKey(database.get(selectedName).getMother())&&
                        database.containsKey(database.get(selectedName).getFather())){
                    UI.println("M: "+database.get(database.get(selectedName).getMother()).toString());
                    UI.println("F: "+database.get(database.get(selectedName).getFather()).toString());
                    return;
                }
                else if(!database.get(selectedName).getMother().equals("-")&&
                        !database.get(selectedName).getFather().equals("-")&&
                        !database.containsKey(database.get(selectedName).getMother())&&
                        database.containsKey(database.get(selectedName).getFather())){
                    UI.println("M: No details known");
                    UI.println("F: "+database.get(database.get(selectedName).getFather()).toString());
                    return;
                }
                else if(!database.get(selectedName).getMother().equals("-")&&
                        !database.get(selectedName).getFather().equals("-")&&
                        database.containsKey(database.get(selectedName).getMother())&&
                        !database.containsKey(database.get(selectedName).getFather())){
                    UI.println("M: "+database.get(database.get(selectedName).getMother()).toString());
                    UI.println("F: No details known");     
                    return;
                }
                else if(!database.get(selectedName).getMother().equals("-")&&
                        !database.get(selectedName).getFather().equals("-")&&
                        !database.containsKey(database.get(selectedName).getMother())&&
                        !database.containsKey(database.get(selectedName).getFather())){
                    UI.println("M: No details known");
                    UI.println("F: No details known");    
                    return;
                }                                
                else if(database.get(selectedName).getMother().equals("-")&&
                        !database.get(selectedName).getFather().equals("-")){
                    UI.println("M: unknown");
                    UI.println("F: "+database.get(database.get(selectedName).getFather()).toString());
                    return;
                }
                else if(!database.get(selectedName).getMother().equals("-")&&
                        database.get(selectedName).getFather().equals("-")){
                    UI.println("M: "+database.get(database.get(selectedName).getMother()).toString());
                    UI.println("F: unknown");                      
                    return;
                }
            }else if(!database.containsKey(selectedName)){
                UI.println("no person called "+ selectedName);  
                return;
            }
        }
        UI.println("-----------------");
    }

    /**
     * Add a child to the person with selectedName (if there is one).
     * If there is no person with the selectedName,
     *   print "no person called ..." and return
     * Ask for the name of a child of the selectedName
     *  (remember to capitalise the child's name)
     * If the child is already recorded as a child of the person,
     *  print a message
     * Otherwise, add the child's name to the current person.
     * If the child's name is not in the current database,
     *   offer to add the child's details to the current database.
     *   Check that the selectedName is either the mother or the father.
     */
    public void addChild(){
        /*# YOUR CODE HERE */
        for(String name:database.keySet()){
            if(database.containsKey(selectedName)){
                String child = capitalise(UI.askString("Name of child of "+selectedName));  //ask name of child 
                if(database.get(selectedName).getChildren().contains(child)){ //if the name of child is child of selected person
                    UI.println(child+" is already a child of "+selectedName);
                    break;
                }
                if(database.containsKey(child)){
                    database.get(selectedName).addChild(child); //if child is in database
                    break;
                } 
                if(!database.containsKey(child)){
                        boolean b = UI.askBoolean("Add "+child+" to the database?"); //ask if add child into databse
                        if(b){  // if yes
                            int year = UI.askInt("Year of birth: ");//ask child's DOB
                            String mother = capitalise(UI.askString("Mother's name (or -): "));//ask child's mother's name
                            String father = capitalise(UI.askString("Father's name (or -): "));//ask child's father's name
                            Person object = new Person(child,year,mother,father); //create a child as new person 
                            if(mother.equals(selectedName)||father.equals(selectedName)){//if mother or father is selected name
                                database.put(child,object);//add child into database with mother or father as selected name
                                database.get(selectedName).addChild(child);
                                UI.println("Added: "+database.get(child).toString());  //print
                                break;
                            }
                            else{
                                UI.println(selectedName+" must be one of the parents of "+child);//warning
                                break;
                            }                       
                        }
                        else if(!b){
                            missPerson.add(child);  //add child into missPerson field which contains person who is child of someone but not in database
                            break;
                        }
                    }
                }
            }
        UI.println("-----------------");
    }

    /**
     * Print the number of children of the selectedName and their names (if any)
     * Find the children by searching the database for people with
     * selectedName as a parent.
     * Hint: Use the findChildren method (which is needed for other methods also)
     */
    public void printChildren(){
        /*# YOUR CODE HERE */
        if(selectedName!=null){
            Set<String> children = findChildren(selectedName);  //get children from findChildren method
            UI.println(selectedName+" has "+children.size()+" children:\n"); 
            for(String child: children){
                UI.println(child); //print each child
            }
        }        
        UI.println("-----------------");
    }

    /**
     * Find (and return) the set of all the names of the children of
     * the given person by searching the database for every person 
     * who has a mother or father equal to the person's name.
     * If there are no children, then return an empty Set
     */
    public Set<String> findChildren(String name){
        /*# YOUR CODE HERE */
        Set<String> children = new HashSet<String>();
        if(database.containsKey(name)){
            for(String child:database.keySet()){
                String mother= database.get(child).getMother();
                String father= database.get(child).getMother();
                if(name.equals(mother)||name.equals(father)){
                       children.add(child);
                }
            }
        }  
        return children;// get children of someone
    }

    /**
     * When the database is first loaded, none of the Persons will
     * have any children recorded in their children field. 
     * Fix the database so every Person's children includes all the
     * people that have that Person as a parent.
     * Hint: use the findChildren method
     */
    public void fixDatabase(){
        /*# YOUR CODE HERE */
        for(String name:database.keySet()){
           Set<String> children = this.findChildren(name);  //create a set for children
           for(String c: children){
               database.get(name).addChild(c); //find each person's children if he has
           }      
        }   
        /*# END*/
        databaseHasBeenFixed = true;
        UI.println("Found children of each person in database\n-----------------");
    }

    /**
     * Print out all the grandchildren of the selectedName (if any)
     * Assume that the database has been "fixed" so that every Person
     * contains a set of all their children.
     * If the selectedName is not in the database, print "... is not known"
     */
    public void printGrandChildren(){
        if (!databaseHasBeenFixed) { UI.println("Database must be fixed first!");}
        if (!database.containsKey(selectedName)){
            UI.println("That person is not known");
            return;
        }
        /*# YOUR CODE HERE */
        UI.println("GrandChildren of "+selectedName+" : ");
        for(String name:findChildren(selectedName)){  //from selected name's children
            for(String child:findChildren(name)){     //from each children of selected name
                UI.println(child);                    //print children's name
            }
        }
        UI.println("------------------");
    }

    /**
     * Print out all the names that are in the database but for which
     * there is no Person in the database. Do not print any name twice.
     * These will be names of parents or children of Persons in the Database
     * for which a Person object has not been created.
     */
    public void printMissing(){
        UI.println("Missing names:");
        /*# YOUR CODE HERE */
        for(String name:missPerson) {UI.println(name);}    //print name from missPerson field
        UI.println("------------------");
    }

    /*# CHALLENGE*/
    /**

     */
    public void printAncestors(){
        UI.println("Ancestor tree for "+selectedName);
    }
    
    /**

     */
    public void printDescendants(){
        UI.println("Descendant tree of "+selectedName);        
    }    
    
    /**
     * Return a capitalised version of a string
     */
    public String capitalise(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static void main(String[] args) throws IOException {
        new Genealogy();
    }
}
