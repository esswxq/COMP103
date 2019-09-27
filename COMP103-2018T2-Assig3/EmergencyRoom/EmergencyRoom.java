// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 3
 * Name: Xianqiong Wu
 * Username: wuxian1
 * ID: 300403758
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/**
 * Simulation of an EmergencyRoom,
 * The Emergency room has a collection of departments for treating patients (ER beds, X-ray,
 *  Operating Theatre, MRI, Ultrasound, etc).
 * 
 * When patients arrive at the emergency room, they are immediately assessed by the
 *  triage team who determine the priority of the patient and a sequence of treatments
 *  that the patient will need.
 *
 * Each department has
 *  - a Set of patients that they are currently treating,
 *    (There is a maximum size of this set for each department)
 *  - a Queue for the patients waiting for that department.
 *
 * The departments should be in a Map, with the department name (= treatment type) as the key.
 * 
 * When a patient has finished a treatment, they should be moved to the
 *   department for the next treatment they require.
 *
 * When a patient has finished all their treatments, they should be discharged:
 *  - a record of their total time, treatment time, and wait time should be printed,
 *  - the details should be added to the statistics. 
 *
 *
 * The main simulation should consist of
 * a setting up phase which initialises all the queues,
 * a loop that steps through time:
 *   - advances the time by one "tick"
 *   - Processes one time tick for each patient currently in each department
 *     (either making them wait if they are on the queue, or
 *      advancing their treatment if they are being treated)
 *   - Checks for any patients who have completed their current treatment,
 *      and remove from the department
 *   - Move all Patients that completed a treatment to their next department (or discharge them)
 *   - Checks each department, and moves patients from the front of the
 *       waiting queues into the Sets of patients being treated, if there is room
 *   - Gets any new patient that has arrived (depends on arrivalInterval) and
 *       puts them on the appropriate queue
 *   - Redraws all the departments - showing the patients being treated, and
 *     the patients waiting in the queues
 *   - Pauses briefly
 *
 * The simple simulation just has one department - ER beds that can treat 5 people at once.
 * Patients arrive and need treatment for random times.
 */

public class EmergencyRoom{

    private Map<String, Department> departments = new HashMap<String, Department>();
    private boolean running = false;

    // fields controlling the probabilities.
    private int arrivalInterval = 5;   // new patient every 5 ticks, on average
    private double probPri1 = 0.1; // 10% priority 1 patients
    private double probPri2 = 0.2; // 20% priority 2 patients
    private Random random = new Random();  // The random number generator.
    private Set<Patient> patients = new HashSet<Patient>();
        
    private int time = 0; // The simulated time

    /**
     * Construct a new EmergencyRoom object, setting up the GUI
     */
    public EmergencyRoom(){
        setupGUI();
        reset();
    }

    public void setupGUI(){
        UI.addButton("Reset", this::reset);
        UI.addButton("Start", this::run);
        UI.addButton("Stop", ()->{running=false;});
        UI.addSlider("Av arrival interval", 1, 50, arrivalInterval,
            (double val)-> {arrivalInterval = (int)val;});
        UI.addSlider("Prob of Pri 1", 1, 100, probPri1*100,
            (double val)-> {probPri1 = val/100;});
        UI.addSlider("Prob of Pri 2", 1, 100, probPri2*100,
            (double val)-> {probPri2 = Math.min(val/100,1-probPri1);});
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1000,600);
        UI.setDivider(0.5);
    }

    /**
     * Define the departments available and put them in the map of departments.
     * Each department needs to have a name and a maximum number of patients that
     * it can be treating at the same time.
     * Simple version: just a collection of 5 ER beds.
     */

    public void reset(){
        UI.clearGraphics();
        UI.clearText();
        running=false;
        time = 0;
        departments.put("ER beds",           new Department("ER beds", 8));
        departments.put("Operating Theatre", new Department("Operating Theatre", 2));
        departments.put("X-ray",             new Department("X-ray", 2));
        departments.put("MRI",               new Department("MRI", 1));
        departments.put("Ultrasound",        new Department("Ultrasound", 3));
        departments.put("Children's room",   new Department("Children's room", 4));
    }

     
    /**
     * Main loop of the simulation
     */
    public void run(){
        running = true;
        while (running){
            // Hint: if you are stepping through a set, you can't remove
            //   items from the set inside the loop!
            //   If you need to remove items, you can add the items to a
            //   temporary list, and after the loop is done, remove all 
            //   the items on the temporary list from the set.
            
            /*# YOUR CODE HERE */
            
            //advanceds the time by one "tick"
            time++;      
            UI.printMessage("T: "+time);
            
            //Processes one time tick for each patient currently being treated (either in queue or in treatment)           
            for(String dep : departments.keySet()){
                if(!departments.get(dep).getCurrentPatients().isEmpty()){
                    for(Patient p:departments.get(dep).getCurrentPatients()){ 
                        if(!patients.contains(p)){    //if the patient hasn't finish all treatment
                            if(p.completedCurrentTreatment()){
                                if(p.completedAllTreatments()){
                                    discharge(p);         //print out details of that patient
                                    patients.add(p);      //add that patient to patients which contains all completed patients
                                }
                                else{
                                    String nextDep = p.getNextTreatment();
                                    departments.get(nextDep).addPatient(p);
                                    p.incrementTreatmentNumber();        //increment number of treatment of the patient                          
                                }
                            }
                            else {
                                p.advanceTreatmentByTick();     //advance one tick
                            }
                        }
                    }  
                }
                for(Patient p:departments.get(dep).getWaitingPatients()){
                    p.waitForATick();   //add one tick wait time
                } 
            } 
            for(String dep:departments.keySet()){
                for(Patient p:patients){
                    departments.get(dep).removePatient(p);  //remove patient from department set when they finish their treatments
                }
            }
            
            patients.clear();
            
            //Checks each department, and moves patients from the front of the 
            //waiting queues into the Sets of patients being treated, if there is room 
            //move patients from waitlist
            for(String dep : departments.keySet()){       
                if(!departments.get(dep).getWaitingPatients().isEmpty()){
                    departments.get(dep).moveFromWaiting();      
                } 
            }
            
            //Gets any new patient that has arrived (depends on arrivalInterval) and puts them on the appropriate queue
            if(random.nextDouble()<1.0/arrivalInterval){
               Patient newPatient = new Patient(time,randomPriority());
               newPatient.makeRandomTreatments();
               String nextDep =  newPatient.getNextTreatment();
               departments.get(nextDep).addPatient(newPatient);
               newPatient.incrementTreatmentNumber();
               UI.println("Arrived: "+ newPatient);
            }
                                                       
            redraw();
            UI.sleep(300);
        }        
        // Stopped
        //reportStatistics();
    }

    /**
     * Report that a patient has been discharged, along with any
     * useful statistics about the patient
     */
    public void discharge(Patient p){
        UI.println("Discharge: " + p);
    }

    /**
     * Report summary statistics about the simulation
     */
    public void reportStatistics(){
    }

    /**
     * Redraws all the departments
     */
    public void redraw(){
        UI.clearGraphics();
        UI.setFontSize(14);
        UI.drawString("Treating Patients", 5, 15);
        UI.drawString("Waiting Queues", 200, 15);
        UI.drawLine(0,32,400, 32);
        double y = 80;
        for (String dept : new String[]{"ER beds","Operating Theatre", "X-ray", "Ultrasound", "MRI","Children's room"}){
            departments.get(dept).redraw(y);
            UI.drawLine(0,y+2,400, y+2);
            y += 50;
        }
    }

    /**  (COMPLETION)
     * Returns a random priority 1 - 3
     * Probability of a priority 1 patient should be probPri1
     * Probability of a priority 2 patient should be probPri2
     * Probability of a priority 3 patient should be (1-probPri1-probPri2)
     */
    public int randomPriority(){
        if (random.nextDouble()<probPri1) return 1;        //only 10% posibility to get 1
        else if (random.nextDouble()<probPri2) return 2;   //20%
        else return 3;
    }


    public static void main(String[] arguments){
        new EmergencyRoom();
    }        

}
