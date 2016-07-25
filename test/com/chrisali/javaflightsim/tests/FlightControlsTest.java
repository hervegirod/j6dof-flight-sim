package com.chrisali.javaflightsim.tests;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import com.chrisali.javaflightsim.simulation.controls.FlightControls;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import com.chrisali.javaflightsim.simulation.setup.IntegratorConfig;
import com.chrisali.javaflightsim.simulation.setup.Options;

/**
 * Test class for {@link FlightControls}. Creates flight controls object and thread to
 * run, and outputs values for each flight control deflection/setting
 * 
 * @author Christopher Ali
 *
 */
public class FlightControlsTest implements Runnable {
	private FlightControls flightControls;
	private Set<Options> options;
	private EnumMap<IntegratorConfig, Double> integratorConfig;
	private Thread flightControlsThread;
	
	public FlightControlsTest() {
		this.options = EnumSet.of(Options.USE_CH_CONTROLS);
		this.flightControls = new FlightControls(options);
		this.flightControlsThread = new Thread(flightControls);
		this.integratorConfig = IntegrationSetup.gatherIntegratorConfig("IntegratorConfig");
	}
	
	@Override
	public void run() {
		flightControlsThread.start();
		
		try {
			Thread.sleep(500);
			
			while (FlightControls.isRunning()) {
				System.out.println(flightControls.toString());
				Thread.sleep((long) (integratorConfig.get(IntegratorConfig.DT)*1000));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {new Thread(new FlightControlsTest()).start();}
}
