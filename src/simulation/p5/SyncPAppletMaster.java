package simulation.p5;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import processing.core.PApplet;

public class SyncPAppletMaster {	
	static int textSize = 96;
	
	Timer t;
	static final int LOOP_INTERVAL_MIN = 100;	
	static final int LOOP_INTERVAL_INIT = 1000;	
	static final int LOOP_INTERVAL_MAX = 2000;
	int updateInterval = LOOP_INTERVAL_INIT;
	static SyncPAppletMasterConfigFrame configFrame;	
	int 	masterStatus = 0;
	String	message;	
	ArrayList<SyncPAppletSlave>	slaveArray;
	
	public boolean realDeployment = false;
	
	public SyncPAppletMaster(){
		super();
		setup();
	}	
	public SyncPAppletMaster(String message){
		if(message != null)
			this.message = message;
		setup();
	}

	public void setup() {
		configFrame = new SyncPAppletMasterConfigFrame(this);
		configFrame.setVisible(true);

		slaveArray = new ArrayList<SyncPAppletSlave>();
		if(realDeployment ==true)
			for(int i=0; i<message.length(); i++){
				SyncPAppletSlave slave = new SyncPAppletSlave(i, message);
				slaveArray.add(slave);
				slave.init();
				slave.setup();
		        JFrame frame = new JFrame();
		        frame.setLayout(new BorderLayout());
		        frame.setSize(slave.width, slave.height);        
		        frame.setVisible(true);
		        frame.add(slave, BorderLayout.CENTER);		        
			}					
	}
		// TimerTask
		class MyTimer extends TimerTask {
			public void run(){
				updateSlaves();
			}
		}
		//
	public void addSlave(SyncPAppletSlave slave){
		slaveArray.add(slave);		
	}
	
	public void start(){
		t = new Timer();
		t.schedule(new MyTimer(),0,updateInterval);
	}
	
	public void resetInterval(int interval) {
		updateInterval = interval;
		t.cancel();
		t = new Timer();
		t.schedule(new MyTimer(),0,updateInterval);
	}
		
	public void updateSlaves(){		
		masterStatus = masterStatus % message.length();		
		for(SyncPAppletSlave slave : slaveArray)
	    	slave.updateStatus(masterStatus);	    		
	    masterStatus++;	   
	}
	
	public static void main(String[] args){
		SyncPAppletMaster master = new SyncPAppletMaster("Hello World.");
		master.setup();
		master.start();
	}
}