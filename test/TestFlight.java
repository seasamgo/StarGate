// This is a TestController For Building SpaceShips
import objectdraw.*;
import java.awt.*;
import stargate.*;

public class TestFlight extends WindowController implements SpaceController 
{
	private SpaceShip craft; 
	private int count = 0;
	private FilledOval theGate;

	private static final int SIZE = 600;
	private static final int OVAL = 20;
	private static final int INSTX = 10;
	 
	public void begin() 
	{       
		// display instructions
		new Text("Click to Create Spaceships...", INSTX, INSTX, canvas);
		new Text("Click StarGate exit...", INSTX, 2*INSTX, canvas);
		theGate = new FilledOval(SIZE - OVAL, OVAL, OVAL, OVAL, canvas);	
	}
	 
	public void onMouseClick(Location point) 
	{
		// make a new spaceCraft. Not Random
		// note that theGate is NOT passed to this constructor
		//      it needs to be in the StarGate controller
	     	craft = new SpaceShip( count++ % 3, point, 
			(count % 3 + 1) * 0.1, 0.1, 
			(Drawable2DInterface) null, this, canvas);
		if (theGate.contains(point)) 
		{
			System.exit(0);
		}

	}

	// Implement for SpaceController Interface Specification
	public void enter(SpaceObject craft)
	{
		// does nothing in testing
	}

	// Main Method
	 static public void main(String[] args)
	{
		new TestFlight().startController(SIZE, SIZE);
	}
}
