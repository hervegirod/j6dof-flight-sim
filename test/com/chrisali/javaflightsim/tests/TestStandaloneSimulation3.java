/*
 * Copyright (c) 2016, 2017 Chris Ali. All rights reserved.
   Copyright (c) 2017 Herve Girod. All rights reserved.
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

import com.chrisali.javaflightsim.conf.Configuration;
import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.datatransfer.FlightDataType;
import com.chrisali.javaflightsim.otw.LWJGLWorld;
import com.chrisali.javaflightsim.simulation.controls.FlightControlType;
import java.util.EnumMap;

/**
 * This class tests the creation and starting of a standalone simulation.
 */
public class TestStandaloneSimulation3 implements FlightDataListener {
   private SimulationController controller = null;
   private EnumMap<FlightControlType, Double> controls = null;
   private boolean started = false;

   public TestStandaloneSimulation3(SimulationController controller) {
      this.controller = controller;
      controller.getFlightData().addFlightDataListener(this);
      controls = controller.getControls();
   }

   public static void main(String[] args) {
      // configuration
      Configuration conf = Configuration.getInstance();
      conf.setDefaultConfiguration();
      conf.setAircraft("LookupNavion");

      // simulation controller creation
      SimulationController controller = new SimulationController(true);

      // World renderer creation
      LWJGLWorld lwjglRenderer = new LWJGLWorld();
      lwjglRenderer.setIncludeSounds(false);
      controller.setWorldRenderer(lwjglRenderer);
      controller.setTerrainProvider(lwjglRenderer);
      lwjglRenderer.setSimulationController(controller);

      // start simulation
      TestStandaloneSimulation3 test = new TestStandaloneSimulation3(controller);
      controller.startSimulation();
      controller.startOTWThread();
   }

   @Override
   public void onFlightDataReceived(FlightData flightData) {
      double north = flightData.getFlightData().get(FlightDataType.NORTH);
      double alt = flightData.getFlightData().get(FlightDataType.ALTITUDE);
      System.out.println("North: " + north + " Altitude: " + alt);
      if (north > 1000 && !started) {
         started = true;
         double value = controls.get(FlightControlType.RUDDER);
         controls.put(FlightControlType.THROTTLE_1, 10d);
         controller.updateControls();
         System.out.println("Apply controls: " + value);
      }
   }
}
