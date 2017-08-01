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
package com.chrisali.javaflightsim.simulation.controls;

import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.simulation.setup.IntegratorConfig;
import com.chrisali.javaflightsim.simulation.setup.Options;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows to set the Flight Controls externally.
 *
 * @author Herve Girod
 * @since 0.5
 */
public class DefaultFlightControls extends AbstractController implements FlightDataListener {
   private final Map<FlightControlType, Double> controls;
   private final Map<FlightControlType, Float> icontrols = new HashMap<>();
   private boolean gPressed = false;

   /**
    * Constructor for {@link FlightControls}; {@link SimulationController} argument to initialize {@link IntegratorConfig}
    * EnumMap, the {@link Options} EnumSet, as well as to update simulation options and call simulation methods.
    *
    * @param simController the Simulation Controller
    */
   public DefaultFlightControls(SimulationController simController) {
      super();
      this.controls = simController.getControls();

      // initializes static EnumMap that contains trim values of controls for doublets
      FlightControlsUtilities.init();
   }

   /**
    * Set the valud for a flight control
    *
    * @param control the flight control
    * @param value the value
    */
   public void setControl(FlightControlType control, float value) {
      icontrols.put(control, value);
   }

   /**
    * Remove a value for a flight control
    *
    * @param control the flight control
    */
   public void removeControl(FlightControlType control) {
      icontrols.remove(control);
   }

   /**
    * Clear all flight control values
    */
   public void resetControls() {
      icontrols.clear();
   }

   /**
    * Receive fed back flight data to be used with P/PD controllers
    */
   @Override
   public void onFlightDataReceived(FlightData flightData) {
   }

   /**
    * Returns thread-safe map containing flight controls data, with {@link FlightControlType} as the keys
    *
    * @return controls
    */
   public synchronized Map<FlightControlType, Double> getFlightControls() {
      return controls;
   }

   @Override
   protected Map<FlightControlType, Double> updateControllerValues(Map<FlightControlType, Double> controls) {
      if (icontrols.containsKey(FlightControlType.ELEVATOR)) {
         if (controls.get(FlightControlType.ELEVATOR) <= FlightControlType.ELEVATOR.getMaximum()) {
            float f = icontrols.get(FlightControlType.ELEVATOR);
            if (f > 0f) {
               controls.put(FlightControlType.ELEVATOR, controls.get(FlightControlType.ELEVATOR) + getDeflectionRate(FlightControlType.ELEVATOR));
            }
         } else if (controls.get(FlightControlType.ELEVATOR) >= FlightControlType.ELEVATOR.getMinimum()) {
            float f = icontrols.get(FlightControlType.ELEVATOR);
            if (f < 0f) {
               controls.put(FlightControlType.ELEVATOR, controls.get(FlightControlType.ELEVATOR) - getDeflectionRate(FlightControlType.ELEVATOR));
            }
         }
      }
      if (icontrols.containsKey(FlightControlType.AILERON)) {
         if (controls.get(FlightControlType.AILERON) <= FlightControlType.AILERON.getMaximum()) {
            float f = icontrols.get(FlightControlType.AILERON);
            if (f > 0f) {
               controls.put(FlightControlType.AILERON, controls.get(FlightControlType.AILERON) + getDeflectionRate(FlightControlType.AILERON));
            }
         } else if (controls.get(FlightControlType.AILERON) >= FlightControlType.AILERON.getMinimum()) {
            float f = icontrols.get(FlightControlType.AILERON);
            if (f < 0f) {
               controls.put(FlightControlType.AILERON, controls.get(FlightControlType.AILERON) - getDeflectionRate(FlightControlType.AILERON));
            }
         }
      }
      if (icontrols.containsKey(FlightControlType.RUDDER)) {
         float axisValue = icontrols.get(FlightControlType.RUDDER);
         controls.put(FlightControlType.RUDDER, calculateControlDeflection(FlightControlType.RUDDER, negativeSquare(axisValue)) + trimRudder);
      }
      if (icontrols.containsKey(FlightControlType.THROTTLE_1)) {
         if (controls.get(FlightControlType.THROTTLE_1) <= FlightControlType.THROTTLE_1.getMaximum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_1);
            if (f > 0f) {
               controls.put(FlightControlType.THROTTLE_1, controls.get(FlightControlType.THROTTLE_1) + getDeflectionRate(FlightControlType.THROTTLE_1));
            }
         } else if (controls.get(FlightControlType.THROTTLE_1) >= FlightControlType.THROTTLE_1.getMinimum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_1);
            if (f < 0f) {
               controls.put(FlightControlType.THROTTLE_1, controls.get(FlightControlType.THROTTLE_1) - getDeflectionRate(FlightControlType.THROTTLE_1));
            }
         }
      }
      if (icontrols.containsKey(FlightControlType.THROTTLE_2)) {
         if (controls.get(FlightControlType.THROTTLE_2) <= FlightControlType.THROTTLE_2.getMaximum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_2);
            if (f > 0f) {
               controls.put(FlightControlType.THROTTLE_2, controls.get(FlightControlType.THROTTLE_2) + getDeflectionRate(FlightControlType.THROTTLE_2));
            }
         } else if (controls.get(FlightControlType.THROTTLE_2) >= FlightControlType.THROTTLE_2.getMinimum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_2);
            if (f < 0f) {
               controls.put(FlightControlType.THROTTLE_2, controls.get(FlightControlType.THROTTLE_2) - getDeflectionRate(FlightControlType.THROTTLE_2));
            }
         }
      }
      if (icontrols.containsKey(FlightControlType.THROTTLE_3)) {
         if (controls.get(FlightControlType.THROTTLE_3) <= FlightControlType.THROTTLE_3.getMaximum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_3);
            if (f > 0f) {
               controls.put(FlightControlType.THROTTLE_3, controls.get(FlightControlType.THROTTLE_3) + getDeflectionRate(FlightControlType.THROTTLE_3));
            }
         } else if (controls.get(FlightControlType.THROTTLE_3) >= FlightControlType.THROTTLE_3.getMinimum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_3);
            if (f < 0f) {
               controls.put(FlightControlType.THROTTLE_3, controls.get(FlightControlType.THROTTLE_3) - getDeflectionRate(FlightControlType.THROTTLE_3));
            }
         }
      }
      if (icontrols.containsKey(FlightControlType.THROTTLE_4)) {
         if (controls.get(FlightControlType.THROTTLE_4) <= FlightControlType.THROTTLE_4.getMaximum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_4);
            if (f > 0f) {
               controls.put(FlightControlType.THROTTLE_4, controls.get(FlightControlType.THROTTLE_4) + getDeflectionRate(FlightControlType.THROTTLE_4));
            }
         } else if (controls.get(FlightControlType.THROTTLE_4) >= FlightControlType.THROTTLE_4.getMinimum()) {
            float f = icontrols.get(FlightControlType.THROTTLE_4);
            if (f < 0f) {
               controls.put(FlightControlType.THROTTLE_4, controls.get(FlightControlType.THROTTLE_4) - getDeflectionRate(FlightControlType.THROTTLE_4));
            }
         }
      }
      if (icontrols.containsKey(FlightControlType.FLAPS)) {
         if (controls.get(FlightControlType.FLAPS) <= FlightControlType.FLAPS.getMaximum()) {
            float f = icontrols.get(FlightControlType.FLAPS);
            if (f > 0f) {
               controls.put(FlightControlType.FLAPS, (flaps += getDeflectionRate(FlightControlType.FLAPS)));
            }
         } else if (controls.get(FlightControlType.FLAPS) >= FlightControlType.FLAPS.getMinimum()) {
            float f = icontrols.get(FlightControlType.FLAPS);
            if (f < 0f) {
               controls.put(FlightControlType.FLAPS, (flaps -= getDeflectionRate(FlightControlType.FLAPS)));
            }
         }
      }
      if (icontrols.containsKey(FlightControlType.GEAR)) {
         if (controls.get(FlightControlType.GEAR) < 0.5 && !gPressed) {
            float f = icontrols.get(FlightControlType.GEAR);
            if (f > 1f) {
               controls.put(FlightControlType.GEAR, 1.0);
               gPressed = true;
            }
         } else if (controls.get(FlightControlType.GEAR) >= -0.5) {
            float f = icontrols.get(FlightControlType.GEAR);
            if (f >= 1f) {
               controls.put(FlightControlType.GEAR, 0.0);
               gPressed = true;
            }
         } else if (gPressed) {
            float f = icontrols.get(FlightControlType.GEAR);
            if (f == 0.0) {
               gPressed = false;
            }
         }
      }
      return controls;
   }
}
