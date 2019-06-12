/*****************************************************************************
 * SpaceShip.java
 *
 * Author: Sam Tracy
 * Student ID: A11034980
 * Course: CSE11 Spring 2013
 * Assignment: Program 2
 *
 * Description: Creates an active SpaceShip object that can bounce around the
 * 		canvas and call back to the controller when it intersects a
 * 		region. Space ships move at initial velocities in the X and 
 * 		Y direction. Intended to be called by other classes.
 *		
 * Build: javac -classpath "*":"." SpaceShip.java
 * Dependencies: objectdraw.jar, java.awt.*
 * 
 * Implements the SpaceObject interface
 *
 * Public Methods Defined:
 * 		SpaceShip(int, Location, double, double, Drawable2DInterface,
 * 				SpaceController, DrawingCanvas)
 *		void setLocation(Location)
 *		void run()
 *		void crush()
 *
 * Public Class Variables:
 * 		None
 *
 ****************************************************************************/

package stargate;

import objectdraw.*;
import java.awt.*;

// Space ships move at initial velocities in the X and Y direction.
public class SpaceShip extends ActiveObject implements SpaceObject
{
    
    // constants 
        // SpaceShip images
    private static final String GRAPHIC0 = "src/stargate/Saucer.png";
    private static final String GRAPHIC1 = "src/stargate/Rocket.png";
    private static final String GRAPHIC2 = "src/stargate/Shuttle.png";
    private static final int SCALE = 8; // proportion to canvas width

    // Instance variables
    private final SpaceController controller; //used in run() for StarGate class
    private final Image craftGraphic;	
    private final DrawingCanvas canvas;
    private final VisibleImage spaceCraft;
    private double vx, vy; //spaceCraft velocities
    private boolean crushed;
    private final Location click; //records location of mouse clicks
    private final Drawable2DInterface theGate; //records the stargate shape
						//from the constructor
    private long past, present, elapsed;
    // Constructor
    public SpaceShip(int selection, Location initial, double velX, 
                    double velY, Drawable2DInterface aShape, 
                    SpaceController control, DrawingCanvas aCanvas) 
    {
	// implement getImage
	Toolkit toolkit = Toolkit.getDefaultToolkit();
        canvas = aCanvas;
	// select spaceCraft graphic
	if(selection == 0)
            craftGraphic = toolkit.getImage(GRAPHIC0);
	else if(selection == 1)
            craftGraphic = toolkit.getImage(GRAPHIC1);
	else 
            craftGraphic = toolkit.getImage(GRAPHIC2);

	spaceCraft = new VisibleImage(craftGraphic,initial,canvas);

        // Scale the image and ensure it begins within the canvas
        click = initial; //secure click location
        scaleAndPlace();
		
        // Record other initial conditions and then start
        vx = velX; vy = velY; //secure input velocities
        theGate = aShape;
        controller = control;

        start();
    }
   
    // Scales the image so that its width is 1/SCALE of the canvas width
    // Scales the height by the same factor 
    // Places the image at least one pixel inside
    // the boundaries of the canvas
    private void scaleAndPlace() {
        double canvas_w = canvas.getWidth();

	// Scale to 1/10 canvas size. Set width first.
	double x = spaceCraft.getWidth(); // get the original width
	spaceCraft.setWidth(canvas_w/SCALE); //new width
	spaceCraft.setHeight(spaceCraft.getWidth()/x
                *spaceCraft.getHeight());//proportion
	// make sure spaceCraft is on the canvas with boundLocation()
	spaceCraft.moveTo(boundLocation(click));
    }	

    // Calculates a Location that keeps the spaceCraft on the canvas
    private Location boundLocation(Location newLoc){
	double x, y;
	//ensure the x value is within the canvas
	if(newLoc.getX() > (canvas.getWidth() - spaceCraft.getWidth() - 5))
            x = canvas.getWidth()- spaceCraft.getWidth()-5;
        else
            x = newLoc.getX();
	//ensure the y value is within the canvas
        if(newLoc.getY() > (canvas.getHeight() - spaceCraft.getHeight() - 5))
            y = canvas.getHeight() - spaceCraft.getHeight()-5;
	else
            y = newLoc.getY();
	// return the updated location
        return new Location(x,y);
    }

    // move the spaceCraft based on elapsed time
    private void moveAndReflect(long elapsed){ 
        double imageWidth, imageHeight;
	double x,y, newX, newY, dx, dy, bottom,rwall;

	imageWidth = spaceCraft.getWidth();
	imageHeight= spaceCraft.getHeight();
	x = spaceCraft.getLocation().getX();
	y = spaceCraft.getLocation().getY();
		
	//distance used: distance = velocity * time
	newX = x + vx * elapsed;
	newY = y + vy * elapsed;

	//place the spaceCraft
	spaceCraft.moveTo(newX, newY);

	// reflect at walls
	// left and top
	if (newX < 0 ) { newX = -newX; vx = Math.abs(vx); }
	if (newY < 0 ) { vy = Math.abs(vy); }

	// right wall
	rwall = canvas.getWidth() - imageWidth;
	if ( newX > rwall )
            vx = -Math.abs(vx); 

	// bottom wall, similar to right wall reflection 
	bottom =canvas.getHeight()-imageHeight;
            if( newY > bottom )
                vy = -Math.abs(vy);
    }

    // moves space craft to a new location within bounds
    public void setLocation(Location newPoint) {
	spaceCraft.moveTo(boundLocation(newPoint));
    }

	// Tell the run loop to exit
    public void crush(){
	// decide what to do if we have been crushed
	crushed = true;
    }	

	// Run method
    public void run() {
	// calculates a pause delay time based upon speed
		
	past = System.currentTimeMillis();
	// Loop to update position of SpaceCraft with moveAndReflect()
	// Checks inside the loop if it has intersected theGate
	while(!crushed){
            present = System.currentTimeMillis();
            elapsed = (present-past);
            past = present;
            moveAndReflect(elapsed);
            // if spaceCraft runs into gate call enter method
            if(spaceCraft.overlaps(theGate))
		controller.enter(this);
	}
	// Is removed from canvas when the loop is completed
	spaceCraft.removeFromCanvas();
    }
}
