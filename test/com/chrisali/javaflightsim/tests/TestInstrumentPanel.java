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
import com.chrisali.javaflightsim.controls.PhysicalFlightControls;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.instruments.InstrumentPanel;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;
import com.chrisali.javaflightsim.simulation.setup.Options;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates a real-time pilot in the loop simulation using {@link Integrate6DOFEquations}, and
 * creates an {@link InstrumentPanel} object in JFrame object to test all gauges with the
 * simulation; uses CH controls running on separate thread in {@link PhysicalFlightControls} for flight controls
 *
 * @author Christopher Ali
 *
 */
public class TestInstrumentPanel {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            runApp();
         }
      });
   }

   private static void runApp() {
      SimulationController simController = new SimulationController();
      simController.getSimulationOptions().clear();
      simController.getSimulationOptions().add(Options.UNLIMITED_FLIGHT);
      simController.getSimulationOptions().add(Options.USE_CH_CONTROLS);

      PhysicalFlightControls flightControls = new PhysicalFlightControls(simController);

      Integrate6DOFEquations runSim = new Integrate6DOFEquations(flightControls.getFlightControls(), simController);
      FlightData flightData = new FlightData();
      flightData.setIntegrate6DOFEquations(runSim);

      new Thread(flightControls).start();
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
      panelWindow.setResizable(false);
   }
}
