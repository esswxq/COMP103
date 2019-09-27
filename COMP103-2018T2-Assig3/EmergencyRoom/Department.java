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
 * A treatment Department (operating theatre, X-ray room,  ER bed, Ultrasound, etc)
 * Each department has
 * - A name,
 * - A maximum number of patients that can be treated at the same time
 * - A Set of Patients that are currently being treated
 * - A Queue of Patients waiting to be treated.
 *
 * FOR THE CORE AND COMPLETION, YOU CAN COMPLETE THE METHODS HERE,
 * BUT DO NOT MODIFY ANY OF THE CODE THAT IS GIVEN!!!
 */

public class Department{

    private String name;   
    private int maxPatients;
    private Set<Patient> currentPatients;  //currently being treated
    private Queue<Patient> waitingPatients;  //waiting queue for department

    /**
     * Construct a new Department object
     * Initialise the waiting queue and the current Set.
     */
    public Department(String name, int max){
        /*# YOUR CODE HERE */
        waitingPatients = new PriorityQueue<Patient>(Patient::compareTo);   //create priority queue to determine which patient order front                            
        currentPatients = new HashSet<Patient>(max);  //set for patient who are treatmented in department
        this.name = name;    //get department name
        maxPatients = max;   //get department time
    }

    /**
     * Return the collection of patients currently being treated
     * Note: returns it as an unmodifiable collection - the calling code
     * can step through the Patients, can call methods on the Patients,
     * but can't add new patients to the set or remove patients from the set.
     */
    public Collection<Patient> getCurrentPatients(){
        return Collections.unmodifiableCollection(currentPatients);
    }

    /**
     * Return the collection of patients waiting in the department
     * Note: returns it as an unmodifiable collection - the calling code
     * can step through the Patients, but can't add to them or remove them.
     */
    public Collection<Patient> getWaitingPatients(){
        return Collections.unmodifiableCollection(waitingPatients);
    }

    /**
     * A new patient for the department.
     * Always starts by being put on the wait queue.
     */
    public void addPatient(Patient p){
        /*# YOUR CODE HERE */
        waitingPatients.offer(p);   //get new patient put into waiting queue
    }

    /**
     * Move patients off the wait queue (if any) into the set of patients
     * being treated (if there is space - fewer than the maximum)
     */
    public void moveFromWaiting(){
        /*# YOUR CODE HERE */
        if(currentPatients.size()<maxPatients&&!waitingPatients.isEmpty()){
            Patient p = waitingPatients.peek();       //get patient from first position of waiting list
            waitingPatients.poll();                   //remove that patient from waiting list
            currentPatients.add(p);                   //add the patient into current patient set
        }
    }

    /**
     * Move patient out of the department
     */
    public void removePatient(Patient p){
        /*# YOUR CODE HERE */
        currentPatients.remove(p);          //remove the patient from current patient set
    }

    /**
     * Draw the department: the patients being treated and the patients waiting 
     */
    public void redraw(double y){
        UI.setFontSize(14);
        UI.drawString(name, 0, y-35);
        double x = 10;
        UI.drawRect(x-5, y-30, maxPatients*10, 30);  // box to show max number of patients
        for(Patient p : currentPatients){
            p.redraw(x, y);
            x += 10;
        }
        x = 200;
        for(Patient p : waitingPatients){
            p.redraw(x, y);
            x += 10;
        }
    }
}
