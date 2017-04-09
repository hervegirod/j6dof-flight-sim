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

import com.chrisali.javaflightsim.controllers.Configuration;
import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;
import com.chrisali.javaflightsim.simulation.setup.Trimming;

public class TestTrimming {

   public static void main(String[] args) {
      Configuration conf = Configuration.getInstance();
      conf.setDefaultConfiguration();
      conf.setAircraft("Navion");
      new TestTrimming();
      conf.setAircraft("TwinNavion");
      new TestTrimming();
   }

   private TestTrimming() {
      Configuration conf = Configuration.getInstance();
      SimulationController controller = new SimulationController();
      controller.setAircraftBuilder(new AircraftBuilder(conf.getAircraftName()));

      Trimming.trimSim(controller, true);
   }
}
