// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement;

import java.awt.Color;

import java.util.ArrayList;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import RootElement.Environment;
import RootElement.Robot;
import java.awt.image.BufferedImage;
import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.LampActuator;
import simbad.sim.RobotFactory;

/************************************************************/
/**
 * 
 */
public class Mapper extends Robot {
	
	public CameraSensor camera;
	public BufferedImage cameraImage;
	public RangeSensorBelt sonars;
	public RangeSensorBelt bumpers;
	public LampActuator lamp;
	
	ArrayList<Point3d> posArray = new ArrayList<Point3d>(); 
	
	CentralStation cs = CentralStation.getInstance();
	
	//CONSTRUCTOR
	public Mapper(Vector3d pos, String name, Color3f color) {
		super(pos, name);
		this.setColor(color);
		camera = RobotFactory.addCameraSensor(this);
		sonars = RobotFactory.addSonarBeltSensor(this, 8);
		bumpers = RobotFactory.addBumperBeltSensor(this, 16);
		
	}
	
	@Override
	public void initBehavior() {
		System.out.println("I exist");
	}
	
	public boolean boxDetected(){
		cameraImage = camera.createCompatibleImage();
		camera.copyVisionImage(cameraImage);
		Color pixelRGB = new Color(cameraImage.getRGB(50,50));
		int red = pixelRGB.getRed();
		int green = pixelRGB.getGreen();
		int blue = pixelRGB.getBlue();
		if (red > 0 && green == 0 && blue == 0){
			return true;
		}
		return false;
	}
	
	public void pinpoint() {
    	boolean contains = false;
        Point3d coordinates = new Point3d();
    	getCoords(coordinates);
    	coordinates.x = Math.round(coordinates.x);
    	coordinates.y = Math.round(coordinates.y);
    	coordinates.z = Math.round(coordinates.z);
    	
    	// check if coordinates are already present in list
    	for(int i=0;i<posArray.size();i++){
    		if((coordinates.x == posArray.get(i).x) && (coordinates.z == posArray.get(i).z)){
    			contains = true;
    		}
    	} // otherwise add new node
    	if(!contains){
    		posArray.add(coordinates);

    	}
    	
    	System.out.println("DEBUG: " + coordinates);
    	System.out.println("DEBUG: " + posArray.size());
	}
	
	public void sendCoordinates(){
		cs.setCoordinates(posArray);
	};
	
	public boolean missionComplete(){
    	if(posArray.size() >= 4){
    		return true;
    	}
    	return false;
    }
	
	public void performBehavior() {
	        
		// perform the following actions every 5 virtual seconds
		
		if(this.myTurn){
			
			if(missionComplete()){
				setTranslationalVelocity(0);
				moveToStartPosition();
				this.myTurn = false;
				//this.detach();
			} else {
				
				setTranslationalVelocity(0.5); 
			
				if(getCounter() % 5 == 0) {
					
					double frontSonar = sonars.getMeasurement(0);
					double frontLeftSonar = sonars.getMeasurement(5);
					double leftSonar = sonars.getMeasurement(4);
					double rightSonar = sonars.getMeasurement(2);
					double frontRightSonar = sonars.getMeasurement(1);
				
					if(bumpers.oneHasHit()){
						rotateY(-45);
					}
					if(boxDetected()){
						setRotationalVelocity(0);
						if(frontSonar < 0.3){
							pinpoint();
							avoidObstacle(leftSonar, frontLeftSonar, frontSonar, frontRightSonar, rightSonar);
						}
					} else if((sonars.getFrontQuadrantHits() > 0) || rightSonar < 0.5 || leftSonar < 0.5){
						avoidObstacle(leftSonar, frontLeftSonar, frontSonar, frontRightSonar, rightSonar);
					} else if ((getCounter() % 50) == 0) {
							setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));
					}
				}
			}
		} 
		//else {
		//	moveToStartPosition();
		//}
	}};
