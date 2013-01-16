package simulation.p5;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SyncPAppletMasterConfigFrame extends JFrame implements ChangeListener{
	SyncPAppletMaster master;
	SliderPanel sliderPanel;
	
	SyncPAppletMasterConfigFrame(SyncPAppletMaster master){
		this.master = master;
		sliderPanel = new SliderPanel("TextSpeedSlider","Text Loop Interval");
		this.getContentPane().add(sliderPanel);
		sliderPanel.addChangeListener(this);
		this.pack();
	}
			
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();		
		if (!source.getValueIsAdjusting()) {
	        int interval = (int)source.getValue();
	        master.resetInterval(interval);
	    }
	}
	
	//---
	private class SliderPanel extends JPanel{
		private JSlider slider;
		
		public SliderPanel(String name, String text) {
			super();
			setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9),
					new TitledBorder(name)));
			slider = new JSlider(SwingConstants.HORIZONTAL, master.LOOP_INTERVAL_MIN, master.LOOP_INTERVAL_MAX, master.LOOP_INTERVAL_INIT);
			slider.setMinorTickSpacing(10);
			slider.setMajorTickSpacing(100);
			add(slider);
		}
		public void addChangeListener(ChangeListener listener) {
			slider.addChangeListener(listener);
		}
	}
}
