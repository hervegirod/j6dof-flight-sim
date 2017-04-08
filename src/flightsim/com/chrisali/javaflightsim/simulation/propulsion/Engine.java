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
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Base abstract class for the flight simulation's engine model.
 * It uses the 1976 NASA Standard Atmosphere model, and assumes that gravity is constant in the Z direction.
 */
public abstract class Engine {

   // Propeller Engine Parameters
   final static protected double A_P = 1.132;
   final static protected double B_P = 0.132;
   final static protected double RHO_SSL = 0.002377;
   final static protected double HP_2_FTLBS = 550;

   protected double maxBHP;            //BHP at standard sea level
   protected double maxRPM;			//rev/min
   protected double propDiameter;		//ft
   protected double propArea;			//ft^2
   protected double propEfficiency;
   protected double rpm;
   protected double fuelFlow;

   // Jet Engine Parameters
   //TODO add jet/turboprop
   // Universal Parameters
   protected String engineName;
   protected int engineNumber;
   protected double[] enginePosition; 	   			// {eng_x,eng_y,eng_z}  (ft)

   protected double[] engineThrust = { 0, 0, 0 };	// {T_x,T_y,T_z}	    (lbf)
   protected double[] engineMoment;				// {M_x,M_y,M_z}        (lbf)

   //TODO need engine model properties (etaP, advance ratio, bhp curves) for lookup tables
   //TODO etaP needs to vary
   /**
    * Calculates all parameters of the engine given the input parameters specified below.
    *
    * @param controls the controls
    * @param environmentParameters the environment Parameters
    * @param windParameters the wind Parameters
    */
   public abstract void updateEngineState(Map<FlightControlType, Double> controls,
           Map<EnvironmentParameters, Double> environmentParameters,
           double[] windParameters);

   /**
    * Calculates the moment generated by the engine as a function of its thrust and location
    * relative to the aircraft's center of gravity. Used in {@link Engine#updateEngineState(java.util.Map, java.util.Map, double[])}
    */
   protected void calculateEngMoments() {
      Vector3D forceVector = new Vector3D(engineThrust);
      Vector3D armVector = new Vector3D(enginePosition);

      this.engineMoment = Vector3D.crossProduct(forceVector, armVector).toArray();
   }

   /**
    * @return engine thrust as a double array vector (lbf)
    */
   public double[] getThrust() {
      return engineThrust;
   }

   /**
    * @return engine moment as a double array vector (lb*ft)
    */
   public double[] getEngineMoment() {
      return engineMoment;
   }

   /**
    * @return engine RPM
    */
   public double getRPM() {
      return rpm;
   }

   /**
    * @return engine fuel flow (gal/hr)
    */
   public double getFuelFlow() {
      return fuelFlow;
   }

   public String getEngineName() {
      return engineName;
   }

   public int getEngineNumber() {
      return engineNumber;
   }

   /**
    * @return engine position relative to aircraft CG [x, y, z] (ft)
    */
   public double[] getEnginePosition() {
      return enginePosition;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((engineName == null) ? 0 : engineName.hashCode());
      result = prime * result + engineNumber;
      result = prime * result + Arrays.hashCode(enginePosition);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      Engine other = (Engine) obj;
      if (engineName == null) {
         if (other.engineName != null) {
            return false;
         }
      } else if (!engineName.equals(other.engineName)) {
         return false;
      }
      if (engineNumber != other.engineNumber) {
         return false;
      }
      if (!Arrays.equals(enginePosition, other.enginePosition)) {
         return false;
      }
      return true;
   }
}