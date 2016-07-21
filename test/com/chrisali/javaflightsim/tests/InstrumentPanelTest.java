package com.chrisali.javaflightsim.tests;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.EnumSet;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.instrumentpanel.InstrumentPanel;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;
import com.chrisali.javaflightsim.simulation.setup.Options;

public class InstrumentPanelTest {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {runApp();}
		});
	}

	private static void runApp() {
		Integrate6DOFEquations runSim = new Integrate6DOFEquations(new AircraftBuilder("Navion"),
																   EnumSet.of(Options.UNLIMITED_FLIGHT, Options.USE_CH_CONTROLS));
		FlightData flightData = new FlightData(runSim);

		new Thread(runSim).start();
		new Thread(flightData).start();
		
		InstrumentPanel panel = new InstrumentPanel();
		flightData.addFlightDataListener(panel);
		
		JFrame panelWindow = new JFrame("Instrument Panel Test");
		panelWindow.setLayout(new BorderLayout());
		panelWindow.add(panel, BorderLayout.CENTER);
		
		panelWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelWindow.setVisible(true);
		panelWindow.setSize(810, 500);
		panelWindow.setMinimumSize(new Dimension(810, 500));
	}
}