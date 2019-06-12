/***************************************************************************
 * StarGate.java
 *
 * Author: Sam Tracy
 * Student ID: A11034980
 * Course: CSE11 Spring 2013
 * Assignment: Program 2
 *
 * Description: Creates a window with dimensions SIZExSIZE, here declared to be
 * 		600x600, and a list of instructions. Places a draggable and 
 * 		clickable oval in the top right corner that is the star gate. 
 * 		Calls on the SpaceShip class to create and place space ships on 
 * 		the canvas when clicked by the mouse. Space ships may either 
 * 		teleport or be removed from the canvas when they intersect the 
 * 		star gate, based on a declared probability. StarGate keeps a 
 * 		tally of total space ships and their fate.
 *
 * Build: java -classpath "*":"." StarGate.java
 * Dependencies: objectdraw.jar, java.awt*
 *
 * Implements the SpaceController interface
 *
 * Public Methods Defined:
 * 		void begin()
 * 		void onMousePress(Location)
 * 		void onMouseClick(Location)
 * 		void onMouseDrag(Location)
 * 		void enter(SpaceObject)
 * 		void main(String[] args)
 *
 * Public Class Variables:
 * 		None
 *
 ****************************************************************************/
package stargate;

import objectdraw.*;

public class StarGate extends WindowController implements SpaceController 
{
    private SpaceShip craft; 
    private int randselect; //used to select craft image
    private double randVx, randVy; //used to create craft velocity
    private FilledOval theGate; 
    private Text craftTxt, gateTxt, tallyTxt;
    private int total, jcount, ccount;

    private static final int SIZE = 600; //initial window size
    private static final int OVAL = 60; //star gate size
    private static final int INSTX = 15; //for text location
    private static final int PROBABILITY = 25;//if random number is less
					//then PROBABILITY, craft crushed
	
    private Location lastPoint; // last point mouse was	 
    private boolean gateGrabbed; // whether the point has grabbed theGate

    public void begin() {       
	// display instructions
	craftTxt = new Text("Click to Create Spaceships...", INSTX, INSTX, canvas);
	gateTxt = new Text("Click StarGate to exit, or drag around...", 
					INSTX, 2*INSTX, canvas);
	// display initial totals of 0
	tallyTxt = new Text("#Ships: 0" + " Jumped: 0" + " Crushed: 0",
					INSTX, 3*INSTX, canvas);
	theGate = new FilledOval(SIZE - 2*OVAL, OVAL, OVAL, OVAL, canvas);	
	}
	
	
    // Click canvas to create space ships and the star gate to exit
    public void onMouseClick(Location point) {
	if(theGate.contains(point))
            System.exit(0);

	//randselect is a random integer between 0 and 2
	randselect = ((int)(Math.random()*100))%3;

	//randVx and randVy are random doubles with absolute value
	//in the range [.1, .4] positive or negative
	randVx = (Math.random()*.8)-(.4);
	if(randVx < .1 && randVx >= 0)
            randVx = randVx + .1;
	else if(randVx < 0 && randVx > -0.1)
            randVx = randVx - .1;
		
	randVy = (Math.random()*.8)-(.4);
 	if(randVy < .1 && randVy>=0)
            randVy = randVy +.1;
	else if(randVy < 0 && randVy > -.1)
            randVy = randVy -.1;

	// make a new random SpaceShip
	// note that theGate is NOT passed to this constructor
	// 	it is in the StarGate controller
	craft = new SpaceShip( randselect, point, randVx, randVy, 
                            theGate, this, canvas);

	//total number of ships goes up by one
	total++;
	//update tally of ships
	tallyTxt.removeFromCanvas();
	tallyTxt = new Text("#Ships: " + total + " Jumped: " +
                            jcount + " Crushed: " + ccount,
                            INSTX, 3*INSTX, canvas);
    }
	
    // record location and determine if the star gate has been grabbed
    public void onMousePress(Location point) {
	lastPoint = point;
	if(theGate.contains(point))
            gateGrabbed = true;
	else //otherwise the mouse can drag from anywhere
            gateGrabbed = false;
    }

    // drag the star gate
    public void onMouseDrag(Location point){
	if(gateGrabbed){
            theGate.move(point.getX() - lastPoint.getX(),
					point.getY() - lastPoint.getY());
            lastPoint = point;
	}
    }

    // Implement for SpaceController Interface Specification
    public void enter(SpaceObject craft) {
	//random integer between 1 and 100
	int rand =(int)(Math.random()*100);
	//random doubles between 1 and canvas size
	double randx = Math.random()*canvas.getWidth();
	double randy = Math.random()*canvas.getHeight();
	Location randloc = new Location(randx, randy);

	if(rand < PROBABILITY){
            //keep the tally
            ccount++;
            //blow the craft up!
            craft.crush();
	}	
	else{
            //keep tally
            jcount++;
            //teleport randomly
            //setLocation() should bound randloc.
            craft.setLocation(randloc);
	}
	//update tally of ships
	tallyTxt.removeFromCanvas();
	tallyTxt = new Text ("#Ships: " + total + " Jumped: " + 
					jcount + " Crushed: " + ccount,
					INSTX, 3*INSTX, canvas);
    }

    // Main Method
    static public void main(String[] args)
    {
        new StarGate().startController(SIZE, SIZE);
    }
}
