package RootElement;

import java.awt.Color;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

public class DeviceFactory {
	public static Robot getRobot(String str){
		if(str == null){
			return null;
		}
		if(str=="mapper"){
			return new Mapper(new Vector3d(-9,0,(9)), "mapper", new Color3f(Color.blue));
		}
		else if (str == "rescuer"){
			return new Rescuer(new Vector3d(-9,0,(-9)), "rescuer", new Color3f(Color.yellow));
		}
		return null;
	}
}
