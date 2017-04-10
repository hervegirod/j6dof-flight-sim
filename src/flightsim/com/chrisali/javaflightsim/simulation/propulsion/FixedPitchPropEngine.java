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
package com.chrisali.javaflightsim.simulation.propulsion;

import com.chrisali.javaflightsim.simulation.controls.FlightControlType;
import com.chrisali.javaflightsim.simulation.enviroment.EnvironmentParameters;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;
import java.util.Arrays;
import java.util.Map;

/**
 * Simple piston engine model with a fixed pitch propeller.
 */
public class FixedPitchPropEngine extends Engine {
   private double throttle;
   private double mixture;

   /**
    * Default constructor, generating a Lycoming IO-360 representation.
    */
   public FixedPitchPropEngine() {
      this.engineName = "Lycoming IO-360";
      this.maxBHP = 200;
      this.maxRPM = 2700;
      this.propDiameter = 6.5;
      this.propArea = Math.PI * (Math.pow(propDiameter, 2)) / 4;
      this.propEfficiency = 0.85;
      this.enginePosition = new double[]{ 0, 0, 0 };
      this.engineNumber = 1;
   }

   /**
    * Creates a custom {@link FixedPitchPropEngine}.
    *
    * @param engineName the engine name
    * @param maxBHP the max BHP
    * @param maxRPM the max RPM
    * @param propDiam the propeller diameter
    * @param enginePosition the engine position
    * @param engineNumber the number of engines
    */
   public FixedPitchPropEngine(String engineName, double maxBHP, double maxRPM, double propDiam, double[] enginePosition, int engineNumber) {
      this.engineName = engineName;
      this.maxBHP = maxBHP;
      this.maxRPM = maxRPM;
      this.propDiameter = propDiam;
      this.propArea = Math.PI * (Math.pow(propDiameter, 2)) / 4;
      this.propEfficiency = 0.85;
      this.enginePosition = enginePosition;
      this.engineNumber = engineNumber;
   }

   /**
    * Updates all fields of engine. Called by {@link Integrate6DOFEquations} to recalculate thrust, moment,
    * fuel flow and RPM for this engine.
    */
   @Override
   public void updateEngineState(Map<FlightControlType, Double> controls, Map<EnvironmentParameters, Double> environmentParameters, double[] windParameters) {
      // Assign engine controls depending on engine number specified
      switch (engineNumber) {
         case 1:
            mixture = controls.get(FlightControlType.MIXTURE_1);
            throttle = controls.get(FlightControlType.THROTTLE_1);
            break;
         case 2:
            mixture = controls.get(FlightControlType.MIXTURE_2);
            throttle = controls.get(FlightControlType.THROTTLE_2);
            break;
         case 3:
            mixture = controls.get(FlightControlType.MIXTURE_3);
            throttle = controls.get(FlightControlType.THROTTLE_3);
            break;
         case 4:
            mixture = controls.get(FlightControlType.MIXTURE_4);
            throttle = controls.get(FlightControlType.THROTTLE_4);
            break;
      }

      calculateThrust(environmentParameters, windParameters);

      calculateEngMoments();

      calculateFuelFlow();

      calculateRPM();
   }

   //TODO consider engine orientation
   /**
    * Calculates thrust of the engine.
    *
    * @param environmentParameters the environment Parameters
    * @param windParameters the wind Parameters
    *
    * @return Double array vector of engine force (lbf)
    */
   private void calculateThrust(Map<EnvironmentParameters, Double> environmentParameters,
           double[] windParameters) {
      // Consider static thrust case at low speeds
      if (windParameters[0] <= 65) {
         this.engineThrust[0] = Math.pow((throttle * maxBHP * HP_2_FTLBS), 0.6667) * Math.pow((2 * environmentParameters.get(EnvironmentParameters.RHO) * propArea), 0.3333);
      } else {
         this.engineThrust[0] = (throttle * maxBHP * HP_2_FTLBS) * ((A_P * environmentParameters.get(EnvironmentParameters.RHO) / RHO_SSL) - B_P) * (propEfficiency / windParameters[0]);
      }
   }

   /**
    * Simple calculation of fuel flow of the engine.
    */
   private void calculateFuelFlow() {
      this.fuelFlow = (0.9 + (throttle * 14.8)) * mixture;
   } // TODO need better method of getting fuel flow

   /**
    * Simple calculation of engine RPM.
    */
   private void calculateRPM() {
      this.rpm = 500 + (throttle * (maxRPM - 500));
   } 		 // TODO need better method of getting RPM

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append("Engine: ").append(engineName).append(" (# ").append(Integer.toString(engineNumber)).append(")")
              .append("\nMax BHP: ").append(maxBHP)
              .append("\nMax RPM: ").append(maxRPM)
              .append("\nProp Diameter [ft]: ").append(propDiameter)
              .append("\nEngine Position [ft]: ").append(Arrays.toString(enginePosition));

      return sb.toString();
   }
}
