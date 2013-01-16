package net.unitedfield.cc;

import processing.core.PApplet;
import cc.arduino.Arduino;

public class DistanceSensorFirmata extends PApplet implements DistanceSensor {
	Arduino arduino;
	int 	pinID = 0;
	int 	rawdata;
	double	rawvoltage;
	float 	rawdistance;
	
	int 	BUFFER_LENGTH = 10;
	float[] buffer	= new float[BUFFER_LENGTH];	
	int		index	= 0;

	public DistanceSensorFirmata(int analogReadPinID){
		super();
		this.pinID = analogReadPinID;
		this.init();
	}
	
	public void setup(){
		arduino = new Arduino(this, Arduino.list()[0], 57600);
		arduino.pinMode(pinID, Arduino.INPUT);		
		noLoop();
	}

//	public void draw(){
//		rawdata = arduino.analogRead(pin);		
//		rawvoltage   = 5.0 * rawdata / 1024.0; 				           // voltage
//		rawdistance  = (float)(26.549* Math.pow(rawvoltage, -1.2091)); // distance,  sharp GP2Y0A21YK
//		//System.out.println(rawdistance);
//	}
		
	public	float	getSenseMin(){
		return 0.1f; // sharp GP2Y0A21YK
	}
	public	float	getSenseMax(){
		return 0.8f; // sharp GP2Y0A21YK
	}	
		
	public	float getDistance(){
		senseDistance();
		return getClosestDistance();
	}
	
	public	boolean senseDistance(){
		rawdata = arduino.analogRead(pinID);		
		rawvoltage   = 5.0 * rawdata / 1024.0; 				           // voltage
		rawdistance  = (float)(26.549* Math.pow(rawvoltage, -1.2091)); // cm distance,  sharp GP2Y0A21YK
		rawdistance  /= 100;                                           // m distance
		//---
		buffer[index] = rawdistance;
		index = (index+1)%BUFFER_LENGTH;	
		
		return (rawdata>0);
	}
	
	public float getClosestDistance(){			
		double sum = 0.0;
		for(int i=0; i<buffer.length; i++)
			sum+=buffer[i];
		double meanDistance = sum/BUFFER_LENGTH;		
		//---
		if(meanDistance> getSenseMax())
			return getSenseMax();
		else
			return (float)(meanDistance);
	}
	

}
