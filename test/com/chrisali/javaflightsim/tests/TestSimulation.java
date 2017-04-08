/*
 * Copyright (c) 2016, 2017 Chris Ali. All rights reserved.
 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program;
if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

 If you have any questions about this project, you can visit
 the project website at the project page on http://github.com/chris-ali/j6dof-flight-sim/
 */
package com.chrisali.javaflightsim.tests;

import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.plotting.PlotWindow;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;
import com.chrisali.javaflightsim.simulation.controls.FlightControls;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;
import com.chrisali.javaflightsim.simulation.setup.Options;
import com.chrisali.javaflightsim.simulation.setup.Trimming;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JFrame;

/**
 * Runs a test of the flight simulation module in Analysis mode to test an aircraft and the simulation
 * workings; uses {@link SimulationController} to set the options, {@link AircraftBuilder} to initalize
 * the aircraft to test, {@link FlightControls} thread to run doublet inputs and {@link PlotWindow} to
 * plot the simulation results at the end
 *
 * @author Christopher Ali
 *
 */
public class TestSimulation {

   private FlightControls flightControls;
   private Thread flightControlsThread;

   private Integrate6DOFEquations runSim;
   private Thread simulationThread;

   private SimulationController simController;

   private PlotWindow plots;

   public TestSimulation() {
      this.simController = new SimulationController();
      simController.getSimulationOptions().clear();
      simController.getSimulationOptions().add(Options.ANALYSIS_MODE);
      simController.setAircraftBuilder(new AircraftBuilder("TwinNavion")); // Twin Navion with 2 Lycoming IO-360
      //simController.setAircraftBuilder(new AircraftBuilder()); // Default Navion with Lycoming IO-360
      //simController.setAircraftBuilder(new AircraftBuilder("Navion")); // Navion with lookup tables with Lycoming IO-360

      Trimming.trimSim(simController, false);

      this.flightControls = new FlightControls(simController);
      this.flightControlsThread = new Thread(flightControls);

      this.runSim = new Integrate6DOFEquations(flightControls, simController);
      this.simulationThread = new Thread(runSim);

      flightControlsThread.start();
      simulationThread.start();

      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
      }

      this.plots = new PlotWindow(new HashSet<String>(Arrays.asList("Controls", "Instruments", "Position", "Rates", "Miscellaneous")),
         simController);
      plots.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      FlightControls.setRunning(false);
   }

   public static void main(String[] args) {
      new TestSimulation();
   }
}
