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

import com.chrisali.javaflightsim.conf.Configuration;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;
import com.chrisali.javaflightsim.simulation.controls.FlightControlType;
import com.chrisali.javaflightsim.simulation.integration.IntegrateGroundReaction;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

public class TestGroundReaction {
   private IntegrateGroundReaction groundReaction;
   private AircraftBuilder ab;
   private double terrainHeight;
   double[] integratorConfig = null;
   private double t;
   private Map<FlightControlType, Double> controls = null;

   // 6DOF Integration Results
   private double[] linearVelocities = new double[] { 5, 0, 0 };
   private double[] NEDPosition = new double[] { 0, 0, 0 };
   private double[] eulerAngles = new double[] { 0, 0, 0 };
   private double[] angularRates = new double[] { 0, 0, 0 };
   private double[] windParameters = new double[] { 0, 0, 0 };

   private double[] sixDOFDerivatives = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

   public TestGroundReaction() {
      Configuration conf = Configuration.getInstance();
      conf.setDefaultConfiguration();
      conf.setAircraft("Navion");
      ab = new AircraftBuilder(conf.getAircraftName());

      controls = IntegrationSetup.gatherInitialControls(conf.getInitialControlsConfig());
      integratorConfig = ArrayUtils.toPrimitive(IntegrationSetup.gatherInitialConditions(conf.getIntegratorConfig()).values()
         .toArray(new Double[3]));
   }

   public static void main(String[] args) {
      TestGroundReaction test = new TestGroundReaction();
      test.setup();
      test.run();
   }

   private void setup() {
      terrainHeight = 0;
      t = 0;

      groundReaction = new IntegrateGroundReaction(linearVelocities,
         NEDPosition,
         eulerAngles,
         angularRates,
         windParameters,
         integratorConfig,
         sixDOFDerivatives,
         ab.getAircraft(),
         controls);
   }

   private void run() {
      while (t <= (integratorConfig[2] - 95)) {

         NEDPosition[2] = 1.75;
         //controls.put(FlightControls.BRAKE_L, 0.8);
         controls.put(FlightControlType.RUDDER, -0.0);

         groundReaction.integrateStep(terrainHeight);

         System.out.println(groundReaction.toString());

         t += integratorConfig[1];
      }
   }

}
