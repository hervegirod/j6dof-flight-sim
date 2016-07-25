package com.chrisali.javaflightsim.simulation.controls;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.simulation.controls.hidcontrollers.AbstractController;
import com.chrisali.javaflightsim.simulation.controls.hidcontrollers.CHControls;
import com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Joystick;
import com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Keyboard;
import com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Mouse;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import com.chrisali.javaflightsim.simulation.setup.IntegratorConfig;
import com.chrisali.javaflightsim.simulation.setup.Options;

/**
 * Contains the Flight Controls thread used to handle flight controls actuated by human interface devices, such as
 * {@link Joystick}, {@link Keyboard}, {@link Mouse} or {@link CHControls}, or by P/PD controllers such as autopilots
 * and stability augmentation sytems. Also contains method to inject doublets into controls when simulation is run
 * as analysis. Uses {@link FlightDataListener} to feed back {@link FlightData} to use in 
 * 
 * 
 * @author Christopher Ali
 *
 */
public class FlightControls implements Runnable, FlightDataListener {

	private static boolean running;
	
	private EnumMap<FlightControlType, Double> controls;
	
	private Map<IntegratorConfig, Double> integratorConfig;
	private Set<Options> options;
//	private Map<FlightDataType, Double> flightData;
	
	private AbstractController hidController;
	private Keyboard hidKeyboard;
	
	/**
	 * Constructor for {@link FlightControls}; uses {@link IntegrationSetup#gatherIntegratorConfig(String)} and
	 * {@link IntegrationSetup#gatherInitialControls(String)} to initialize the EnumMap objects in this class;
	 * 
	 * 
	 * @param options
	 */
	public FlightControls(Set<Options> options) {
		this.controls = IntegrationSetup.gatherInitialControls("InitialControls");
		this.integratorConfig = IntegrationSetup.gatherIntegratorConfig("IntegratorConfig");
		this.options = options;
		
		// Use controllers for pilot in loop simulation if ANALYSIS_MODE not enabled 
		if (!options.contains(Options.ANALYSIS_MODE)) {
			if (options.contains(Options.USE_JOYSTICK))
				hidController = new Joystick(controls);
			else if (options.contains(Options.USE_MOUSE))
				hidController = new Mouse(controls);
			else if (options.contains(Options.USE_CH_CONTROLS))
				hidController = new CHControls(controls);
			
			hidKeyboard = new Keyboard(controls);
		}
	}
	
	@Override
	public void run() {
		running = true;
		
		double t = 0.0;
		
		while (running) {
			try {
				if (!options.contains(Options.ANALYSIS_MODE)) {
					controls = hidController.updateFlightControls(controls);
					controls = hidKeyboard.updateFlightControls(controls);
					
					Thread.sleep((long) (integratorConfig.get(IntegratorConfig.DT)*1000));
				} else 
					controls = FlightControlsUtilities.doubletSeries(controls, (t += integratorConfig.get(IntegratorConfig.DT)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		running = false;
	}

	/**
	 * Receive feedback flight data to be used with P/PD controllers
	 */
	@Override
	public void onFlightDataReceived(FlightData flightData) {
//		if (flightData!= null)
//			this.flightData = flightData.getFlightData();
	}
	
	/**
	 * Returns thread-safe map containing flight controls data, with {@link FlightControlType} as the keys 
	 * 
	 * @return controls
	 */
	public synchronized Map<FlightControlType, Double> getFlightControls() {return Collections.unmodifiableMap(controls);}

	/**
	 * Lets other objects know if {@link FlightControls} thread is running
	 * 
	 * @return if {@link FlightControls} thread is running
	 */
	public static synchronized boolean isRunning() {return running;}

	/**
	 * Lets other objects request to stop the {@link FlightControls} thread by setting running to false
	 * 
	 * @param running
	 */
	public static synchronized void setRunning(boolean running) {FlightControls.running = running;}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<FlightControlType, Double> entry : controls.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		sb.append("\n");
		
		return sb.toString();
	}
}
