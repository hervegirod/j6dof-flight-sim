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
import com.chrisali.javaflightsim.rendering.terrain.DefaultTerrainProvider;

/**
 * This class tests the creation and starting of a standalone simulation.
 */
public class TestStandaloneSimulation implements FlightDataListener {

   public TestStandaloneSimulation(SimulationController controller) {
      controller.getFlightData().addFlightDataListener(this);
   }

   public static void main(String[] args) {
      // configuration
      Configuration conf = Configuration.getInstance();
      conf.setDefaultConfiguration();
      conf.setAircraft("LookupNavion");

      // simulation controller creation
      SimulationController controller = new SimulationController(true);

      // terrain provider creation
      DefaultTerrainProvider terrainProvider = new DefaultTerrainProvider();
      controller.setTerrainProvider(terrainProvider);
      terrainProvider.setSimulationController(controller);

      // start simulation
      TestStandaloneSimulation test = new TestStandaloneSimulation(controller);
      controller.startSimulation();
      controller.startOTWThread();
   }

   @Override
   public void onFlightDataReceived(FlightData flightData) {
      double north = flightData.getFlightData().get(FlightDataType.NORTH);
      System.out.println("North: " + north);
   }
}
