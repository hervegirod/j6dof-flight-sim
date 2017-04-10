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
package com.chrisali.javaflightsim.controls.hidcontrollers;

import net.java.games.input.Controller;
import com.chrisali.javaflightsim.conf.Configuration;
import com.chrisali.javaflightsim.simulation.aero.Aerodynamics;
import com.chrisali.javaflightsim.simulation.controls.FlightControlType;
import com.chrisali.javaflightsim.simulation.controls.FlightControlsUtilities;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import com.chrisali.javaflightsim.simulation.setup.IntegratorConfig;
import java.util.ArrayList;
import java.util.Map;

/**
 * Contains methods implemented {@link Joystick}, {@link Keyboard} and {@link Mouse} to take controller inputs
 * given by JInput, and convert them to actual control deflections used by the simulation in {@link Integrate6DOFEquations}
 * and {@link Aerodynamics}
 */
public abstract class AbstractController {
   protected ArrayList<Controller> controllerList;

   // Gets the frame time DT from IntegratorConfig.txt
   protected double dt = 0;

   // Add these trim values to getControlDeflection method call to emulate trim deflections
   protected static double trimElevator = 0.0;
   protected static double trimAileron = 0.0;
   protected static double trimRudder = 0.0;

   // Flaps deflection
   protected static double flaps = 0.0;

   protected abstract void searchForControllers();

   protected abstract Map<FlightControlType, Double> updateControllerValues(Map<FlightControlType, Double> controls);

   public AbstractController() {
      Configuration conf = Configuration.getInstance();
      dt = IntegrationSetup.gatherIntegratorConfig(conf.getIntegratorConfig()).get(IntegratorConfig.DT);
   }

   /**
    * Standardizes rate of control deflection of keyboard and joystick button inputs regardless of the
    * simulation update rate based on the {@link FlightControlType} argument provided and the
    *
    * @param type the control type
    * @return the deflection rate
    */
   protected double getDeflectionRate(FlightControlType type) {
      return FlightControlsUtilities.getDeflectionRate(type, dt);
   }

   /**
    * Uses maximum and minimum values defined in {@link FlightControlType} to convert normalized
    * joystick axis value to actual control deflection
    *
    * @param controlType
    * @param axisValue
    * @return Actual control deflection
    */
   protected double calculateControlDeflection(FlightControlType controlType, double axisValue) {
      // Calculate positive and negative slope
      // (elevator has different values for positive/negative max)
      if (axisValue <= 0) {
         return (controlType.getMaximum() * Math.abs(axisValue));
      } else {
         return (controlType.getMinimum() * axisValue);
      }
   }

   /**
    * Squares a value without removing its sign if negative.
    *
    * @param value the value
    * @return value squared that retains its original sign
    */
   protected double negativeSquare(double value) {
      if (value < 0) {
         return -(Math.pow(value, 2));
      } else {
         return Math.pow(value, 2);
      }
   }

   /**
    * Updates values for controls in controls EnumMap, limiting their max/min via limitControls method.
    *
    * @param controls the controls
    * @return flightControls EnumMap limited by {@link FlightControlsUtilities#limitControls(java.util.Map)}
    */
   public Map<FlightControlType, Double> updateFlightControls(Map<FlightControlType, Double> controls) {
      return FlightControlsUtilities.limitControls(updateControllerValues(controls));
   }

}
