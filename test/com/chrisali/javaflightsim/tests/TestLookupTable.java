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
import com.chrisali.javaflightsim.simulation.aero.Aerodynamics;
import com.chrisali.javaflightsim.simulation.aero.StabilityDerivatives;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;
import com.chrisali.javaflightsim.simulation.controls.FlightControlType;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import java.util.EnumMap;

public class TestLookupTable {
   private EnumMap<FlightControlType, Double> controls = null;
   private double[] alpha = new double[] { -14, -12, -10, -8, -6, -4, -2, 0, 2, 4, 6, 8, 10, 12, 14, 16 };
   private double[] dFlap = new double[] { 0, 10, 20, 30, 40 };
   AircraftBuilder ab;
   private Aerodynamics aero;

   public TestLookupTable(String aircraftName) {
      Configuration conf = Configuration.getInstance();
      conf.setDefaultConfiguration();
      conf.setAircraft("LookupNavion");
      controls = IntegrationSetup.gatherInitialControls(conf.getInitialControlsConfig());

      this.ab = new AircraftBuilder(conf.getAircraftName());
      this.aero = new Aerodynamics(ab.getAircraft());
      double clAlpha = 0.0;

      for (int j = 0; j < dFlap.length; j++) {
         controls.put(FlightControlType.FLAPS, Math.toRadians(dFlap[j]));

         for (double aoa = alpha[0]; aoa <= alpha[alpha.length - 1]; aoa += 1) {
            clAlpha = aero.calculateInterpStabDer(new double[] { 0.0, 0.0, Math.toRadians(aoa) }, controls, StabilityDerivatives.CM_ALPHA);

            System.out.printf("Flaps: %2.0f | Alpha: %2.1f | CL_Alpha: %4.3f%n", dFlap[j], aoa, clAlpha);
            System.out.println("-----------------------------------------");
         }
         System.out.println();
      }
   }

   public static void main(String[] args) {
      new TestLookupTable("LookupNavion");
   }
}
