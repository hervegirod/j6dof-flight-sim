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
package com.chrisali.javaflightsim.simulation.controls;

import com.chrisali.javaflightsim.conf.Configuration;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import com.chrisali.javaflightsim.simulation.setup.Options;
import java.util.Map;

/**
 * Contains methods to modify the aircraft's flight controls to generate doublets for dynamic stability analysis,
 * or limiting flight control deflections
 */
public class FlightControlsUtilities {
   /**
    * Main trim values of flight controls to determine default value if doublet input not underway
    */
   private static Map<FlightControlType, Double> trimControls;

   /**
    * Initializes trimControls EnumMap in {@link FlightControlsUtilities}; needs to be called each time controls and
    * initial conditions are changed so that new trim values can be read from InitialControls.txt
    */
   public static void init() {
      Configuration conf = Configuration.getInstance();
      trimControls = IntegrationSetup.gatherInitialControls(conf.getInitialControlsConfig());
   }

   /**
    * Generates a control doublet in the positive and then negative direction, returning to trim value. The start
    * time defines when the double should start, the duration indicates how long the control is held in that direction,
    * and the amplitude the amount of deflection in one direction. controlInput uses {@link FlightControlType} to select
    * the desired control to use as a doublet
    *
    * @param controls
    * @param t
    * @param startTime
    * @param duration
    * @param amplitude
    * @param controlType
    * @return flightControls EnumMap
    */
   public static Map<FlightControlType, Double> makeDoublet(Map<FlightControlType, Double> controls,
           double t,
           double startTime,
           double duration,
           double amplitude,
           FlightControlType controlType) {

      if (t > startTime && t < (startTime + duration)) {
         controls.put(controlType, trimControls.get(controlType) + amplitude);
      } else if (t > (startTime + duration) && t < (startTime + (2 * duration))) {
         controls.put(controlType, trimControls.get(controlType) - amplitude);
      } else {
         controls.put(controlType, trimControls.get(controlType));
      }

      return controls;
   }

   /**
    * Creates a series of doublets (aileron, rudder and then elevator) using the makeDoublet methods. It is used
    * when the simulation is set to {@link Options#ANALYSIS_MODE} to examine the transient dynamic response of
    * the aircraft in the simulation
    *
    * @param controls
    * @param t
    * @return flightControls EnumMap
    */
   public static Map<FlightControlType, Double> doubletSeries(Map<FlightControlType, Double> controls, double t) {
      // Update controls with an aileron doublet
      controls = makeDoublet(controls,
              t,
              10.0,
              0.5,
              0.035,
              FlightControlType.AILERON);
      // Update controls with a rudder doublet
      controls = makeDoublet(controls,
              t,
              13.0,
              0.5,
              0.035,
              FlightControlType.RUDDER);
      // Update controls with an elevator doublet
      controls = makeDoublet(controls,
              t,
              50.0,
              0.5,
              0.035,
              FlightControlType.ELEVATOR);
      return controls;
   }

   /**
    * Limit control inputs to sensible deflection values based on the minimum and maximum values defines for
    * each member of {@link FlightControlType}
    *
    * @param map
    * @return flightControls EnumMap
    */
   public static Map<FlightControlType, Double> limitControls(Map<FlightControlType, Double> map) {
      // Loop through enum list; if value in EnumMap controls is greater/less than max/min specified in FlightControls enum,
      // set that EnumMap value to Enum's max/min value
      for (FlightControlType flc : FlightControlType.values()) {
         if (map.get(flc) > flc.getMaximum()) {
            map.put(flc, flc.getMaximum());
         } else if (map.get(flc) < flc.getMinimum()) {
            map.put(flc, flc.getMinimum());
         }
      }
      return map;
   }

   /**
    * Standardizes rate of control deflection of keyboard and joystick button inputs regardless of the
    * simulation update rate based on the {@link FlightControlType} argument provided and the
    *
    * @param type the control type
    * @param dt the frame time DT
    * @return the deflection rate
    */
   public static double getDeflectionRate(FlightControlType type, double dt) {
      switch (type) {
         case AILERON:
         case ELEVATOR:
         case RUDDER:
            return 0.12 * dt;
         case THROTTLE_1:
         case THROTTLE_2:
         case THROTTLE_3:
         case THROTTLE_4:
         case PROPELLER_1:
         case PROPELLER_2:
         case PROPELLER_3:
         case PROPELLER_4:
         case MIXTURE_1:
         case MIXTURE_2:
         case MIXTURE_3:
         case MIXTURE_4:
            return 0.5 * dt;
         case FLAPS:
            return 0.15 * dt;
         default:
            return 0;
      }
   }
}
