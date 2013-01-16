package net.unitedfield.cc.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class SpatialInspector implements Control {
	
	static	private SpatialInspector inspector;
	static	private	boolean		visible = false;
	static	private	JFrame		frame;
	static	private	HashMap<String, Spatial> spatialMap;
	static	private	JComboBox	comboBox;
	static	private	JPanel		locPanel;
	static	private	JRadioButton xButton, yButton, zButton, onetenthButton, oneButton, tenButton;
	static	private	JTextField	xLocValue, yLocValue, zLocValue, xAngleValue, yAngleValue, zAngleValue;
	static	private	JButton		upButton, downButton;
	static	private	JPanel		rotationPanel; 
	static	private	JSlider[] 	sliders = new JSlider[3];
	static	private	Spatial		targetSpatial = null;
	private final Box box = Box.createVerticalBox();
	private final Component glue = Box.createVerticalGlue();
	boolean 	changeLoc = false, changeRot = false;	
	Vector3f	offsetVec = new Vector3f();
	Quaternion	offsetRot = new Quaternion();
	
	private SpatialInspector(){
		spatialMap = new HashMap<String, Spatial>();
		
		frame = new JFrame();
		
		comboBox = new JComboBox();					
		comboBox.addActionListener(new ComboBoxSelectListener());
		
		setLocationPanel();
		setRotationPanel();
	}
	
	public static SpatialInspector getInstance() {
		if (inspector == null) {
			inspector = new SpatialInspector();
		}
		return inspector;
	}
	
	public void show(){
		if(visible)
			return;
		
		frame.setTitle("Spatial Inspector");	
		addCompoment2Box(comboBox);
		addCompoment2Box(locPanel);
		addCompoment2Box(rotationPanel);				
		frame.getContentPane().add(box);		
		frame.pack();
		frame.setVisible(true);
		frame.validate();
		visible = true;		
	}
	
	public void addCompoment2Box(final JComponent comp) {
		comp.setMaximumSize(new Dimension(Short.MAX_VALUE, comp.getPreferredSize().height));
	    box.remove(glue);
	    box.add(Box.createVerticalStrut(3));
	    box.add(comp);
	    box.add(glue);
	    box.revalidate();
	}
	
	private void setLocationPanel() {		
		//--- xyz
		xButton = new JRadioButton(" X  ", true);
		yButton = new JRadioButton(" Y  ");
		zButton = new JRadioButton(" Z  ");	
		JPanel buttonsPanel = new JPanel();	
		buttonsPanel.setLayout(new GridLayout(1,3));
		buttonsPanel.add(xButton);
		buttonsPanel.add(yButton);
		buttonsPanel.add(zButton);
		ButtonGroup xzybg = new ButtonGroup();
		xzybg.add(xButton);
		xzybg.add(yButton);
		xzybg.add(zButton);
		//--- updown
		JPanel updownPanel = new JPanel();
		upButton	= new JButton("UP");
		downButton	= new JButton("DOWN");
		UpDownButtonListener upDownButtonListener = new UpDownButtonListener();
		upButton.addActionListener(  upDownButtonListener);
		downButton.addActionListener(upDownButtonListener);		
		updownPanel.setLayout(new GridLayout(1,2));
		updownPanel.add(upButton);
		updownPanel.add(downButton);
		//--- length
		onetenthButton	= new JRadioButton("0.1 ", true);
		oneButton		= new JRadioButton("1.0 ");
		tenButton		= new JRadioButton("10.0");
		JPanel lengthPanel = new JPanel();		
		lengthPanel.setLayout(new GridLayout(1,3));
		lengthPanel.add(onetenthButton);
		lengthPanel.add(oneButton);
		lengthPanel.add(tenButton);		
		ButtonGroup lengthbg = new ButtonGroup();
		lengthbg.add(onetenthButton);
		lengthbg.add(oneButton);
		lengthbg.add(tenButton);
		//--- textfield for current value
		JPanel valuePanel = new JPanel(new GridLayout(3,2));
		valuePanel.setSize(200,200);
		JLabel xLoc = new JLabel(" X loc");
		JLabel yLoc = new JLabel(" Y loc");
		JLabel zLoc = new JLabel(" Z loc");
		xLocValue = new JTextField();		
		yLocValue = new JTextField();
		zLocValue = new JTextField();
		LocValueEnterListener locValueEnterListener = new LocValueEnterListener();
		xLocValue.addActionListener(locValueEnterListener);
		yLocValue.addActionListener(locValueEnterListener);
		zLocValue.addActionListener(locValueEnterListener);		
		valuePanel.add(xLoc);
		valuePanel.add(xLocValue);		
		valuePanel.add(yLoc);
		valuePanel.add(yLocValue);
		valuePanel.add(zLoc);
		valuePanel.add(zLocValue);
				
		//locPanel = new JPanel(new GridLayout(4,1));
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;		
		layout.setConstraints(buttonsPanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		layout.setConstraints(lengthPanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		layout.setConstraints(updownPanel, gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		layout.setConstraints(valuePanel, gbc);

		locPanel = new JPanel();	
		locPanel.setLayout(layout);		
		locPanel.add(buttonsPanel);
		locPanel.add(lengthPanel);
		locPanel.add(updownPanel);
		locPanel.add(valuePanel);			
	}
			
	private void setRotationPanel(){
		rotationPanel = new JPanel();
		rotationPanel.setLayout(new GridLayout(2,1));
		//---
			JPanel sliderPanel = new JPanel();
			sliderPanel.setLayout(new GridLayout(3,1));					
			sliders[0] = new JSlider(-180, 180, 0);
			sliderPanel.add(sliders[0]);
			//
			sliders[1] = new JSlider(-180, 180, 0);
			sliderPanel.add(sliders[1]);
			//
			sliders[2] = new JSlider(-180, 180, 0);
			sliderPanel.add(sliders[2]);
			//
			RotationSliderListener rotationSliderListener = new RotationSliderListener();
			sliders[0].addChangeListener(rotationSliderListener);
			sliders[1].addChangeListener(rotationSliderListener);
			sliders[2].addChangeListener(rotationSliderListener);
		
		//---
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new GridLayout(3,2));
		JLabel xLabel = new JLabel(" X angle, pitch");
		JLabel yLabel = new JLabel(" Y angle, yaw  ");
		JLabel zLabel = new JLabel(" Z angle, roll ");		
		xAngleValue = new JTextField();
		yAngleValue = new JTextField();
		zAngleValue = new JTextField();
		AngleValueEnterListener angleValueEnterListener = new AngleValueEnterListener();
		xAngleValue.addActionListener(angleValueEnterListener);
		yAngleValue.addActionListener(angleValueEnterListener);
		zAngleValue.addActionListener(angleValueEnterListener);
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;		
		layout.setConstraints(xLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 0;		
		layout.setConstraints(xAngleValue, gbc);		
		gbc.gridx = 0; gbc.gridy = 1;		
		layout.setConstraints(yLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 1;		
		layout.setConstraints(yAngleValue, gbc);
		gbc.gridx = 0; gbc.gridy = 2;		
		layout.setConstraints(zLabel, gbc);
		gbc.gridx = 1; gbc.gridy = 2;		
		layout.setConstraints(zAngleValue, gbc);
		
		valuePanel.add(xLabel);
		valuePanel.add(xAngleValue);
		valuePanel.add(yLabel);
		valuePanel.add(yAngleValue);
		valuePanel.add(zLabel);
		valuePanel.add(zAngleValue);				
		
		rotationPanel.add(sliderPanel);
		rotationPanel.add(valuePanel);
	}	
	
	private	class ComboBoxSelectListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if(ae.getSource() == comboBox){				
				targetSpatial = spatialMap.get(comboBox.getSelectedItem().toString());				
				//--- set current locations and angles of the selected spatial
				showUpdatedLoc();
				showUpdatedAngle();				
				float[] angles = new float[3];
				targetSpatial.getLocalRotation().toAngles(angles);
				sliders[0].setValue((int)(angles[0]/FastMath.PI * 180));
				sliders[1].setValue((int)(angles[1]/FastMath.PI * 180));
				sliders[2].setValue((int)(angles[2]/FastMath.PI * 180));			
			}					
		}		
	}
	
	private class UpDownButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			//--- length
			float	locChangeLength = 0.0f;
				 if(onetenthButton.isSelected())
				locChangeLength = 0.1f;
			else if(oneButton.isSelected())
				locChangeLength = 1.0f;
			else if(tenButton.isSelected())
				locChangeLength = 10.0f;

			//--- updown and xyz
				  if(ae.getSource() == upButton){
				if(xButton.isSelected())
					offsetVec.set(locChangeLength, 0, 0);
				else if(yButton.isSelected())
					offsetVec.set(0, locChangeLength, 0);
				else if(zButton.isSelected())
					offsetVec.set(0, 0, locChangeLength);
			}else if(ae.getSource() == downButton){
				if(xButton.isSelected())
					offsetVec.set(-locChangeLength, 0, 0);
				else if(yButton.isSelected())
					offsetVec.set(0, -locChangeLength, 0);
				else if(zButton.isSelected())
					offsetVec.set(0, 0, -locChangeLength);
			}				  
		    changeLoc = true;
		}
	}	
	
	private class LocValueEnterListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			Object aesrc = ae.getSource();
			float	enterlocvalue = Float.parseFloat(((JTextField)aesrc).getText());
			float	lastvalue;
			if(aesrc == xLocValue){
				lastvalue = targetSpatial.getLocalTranslation().getX();
				offsetVec.set(enterlocvalue-lastvalue, 0, 0);
			}else if(aesrc == yLocValue){
				lastvalue = targetSpatial.getLocalTranslation().getY();
				offsetVec.set(0, enterlocvalue-lastvalue, 0);				
			}else if(aesrc == zLocValue){
				lastvalue = targetSpatial.getLocalTranslation().getZ();
				offsetVec.set(0, 0, enterlocvalue-lastvalue);	
			}
			changeLoc = true;
		}		
	}

	private class RotationSliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {					
			if(e.getSource() == sliders[0] || e.getSource() == sliders[1] || e.getSource() == sliders[2] ){					
				offsetRot.fromAngles(
						    (float)(FastMath.PI * sliders[0].getValue()/180.0), 
				         	(float)(FastMath.PI * sliders[1].getValue()/180.0), 
				         	(float)(FastMath.PI * sliders[2].getValue()/180.0));
				changeRot = true;
			}
		}
	}
	
	private	class AngleValueEnterListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			Object aesrc = ae.getSource();
			float	enteranglevalue = Float.parseFloat(((JTextField)aesrc).getText());
			float	lastvalue;
			if(aesrc == xAngleValue){
				offsetRot.fromAngles(enteranglevalue, (float)(FastMath.PI * sliders[1].getValue()/180.0), (float)(FastMath.PI * sliders[2].getValue()/180.0));		
			}else if(aesrc == yAngleValue){
				offsetRot.fromAngles((float)(FastMath.PI * sliders[0].getValue()/180.0), enteranglevalue, (float)(FastMath.PI * sliders[2].getValue()/180.0));	
			}else if(aesrc == zAngleValue){
				offsetRot.fromAngles((float)(FastMath.PI * sliders[0].getValue()/180.0), (float)(FastMath.PI * sliders[1].getValue()/180.0), enteranglevalue);	
			}
			changeRot = true;
		}			
	}
	
	private void showUpdatedLoc(){
		//--- set current location of the selected spatial
		Vector3f currentLoc = targetSpatial.getWorldTranslation();
		xLocValue.setText(Float.toString(currentLoc.x));
		yLocValue.setText(Float.toString(currentLoc.y));
		zLocValue.setText(Float.toString(currentLoc.z));
	}
	private void showUpdatedAngle() {
		float[] angles = new float[3];
		targetSpatial.getLocalRotation().toAngles(angles);
		xAngleValue.setText(Float.toString(angles[0]));
		yAngleValue.setText(Float.toString(angles[1]));
		zAngleValue.setText(Float.toString(angles[2]));
		
		sliders[0].setValue((int)(angles[0]/FastMath.PI * 180));
		sliders[1].setValue((int)(angles[1]/FastMath.PI * 180));
		sliders[2].setValue((int)(angles[2]/FastMath.PI * 180));		
	}
	
	@Override
	public void update(float tpf) {
		if(changeLoc){
			if(targetSpatial != null){
				targetSpatial.move(offsetVec);
				showUpdatedLoc();
			}
			changeLoc = false;
		}
		
		if(changeRot){
			if(targetSpatial != null){
				targetSpatial.setLocalRotation(offsetRot); // rotation() is relative, setLocalRotation is more intuitive
				showUpdatedAngle();
			}
			changeRot = false;
		}
	}	
	public void setSpatial(Spatial spatial) {
		spatialMap.put(spatial.getName(), spatial);
		comboBox.addItem(spatial.getName());		
	}	
	public void read(JmeImporter arg0) throws IOException { }
	public void write(JmeExporter arg0) throws IOException { }
	public Control cloneForSpatial(Spatial arg0) {return null;}
	public void render(RenderManager arg0, ViewPort arg1) {	}
}
