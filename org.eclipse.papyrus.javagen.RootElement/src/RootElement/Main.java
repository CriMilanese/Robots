package RootElement;


//import javax.vecmath.Vector3d;

import simbad.gui.Simbad;
import simbad.sim.EnvironmentDescription;

public class Main{
	public static void main(String[] args) {
		
		
		System.setProperty("j3d.implicitAntialiasing", "true");
         
//        final Vector3d spawnPoint = new Vector3d(-9,0,9);
        
        
        // creation of the environment containing all obstacles and robots
        EnvironmentDescription environment = new Environment();

        
        CentralStation cs = CentralStation.getInstance();
        environment.add(cs);
        cs.sendMapper(environment, 2);
        cs.sendRescuer(environment, 2);
        // here we create an instance of the whole Simbad simulator and we assign the newly created environment 
        Simbad frame = new Simbad(environment, false);
        frame.update(frame.getGraphics());
	
	}
}
