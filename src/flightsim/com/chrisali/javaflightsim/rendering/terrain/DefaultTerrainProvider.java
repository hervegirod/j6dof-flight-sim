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
package com.chrisali.javaflightsim.rendering.terrain;

import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.datatransfer.FlightDataType;
import com.chrisali.javaflightsim.rendering.TerrainProvider;
import com.chrisali.javaflightsim.simulation.setup.InitialConditions;
import com.chrisali.javaflightsim.utilities.Vector3D;
import java.util.Map;
import java.util.TreeMap;

/**
 * A default Terrain Provider implementation.
 *
 * @since 0.2
 */
public class DefaultTerrainProvider implements TerrainProvider {
   private boolean isStarted = false;
   private boolean isRunning = false;
   private SimulationController controller = null;
   private volatile DefaultTerrainCollection terrainCollection = null;
   private volatile Vector3D.Float ownship = null;

   public DefaultTerrainProvider() {
   }

   @Override
   public void setSimulationController(SimulationController controller) {
      this.controller = controller;
   }

   /**
    * Initializes and generates all assets needed to render lights, entities, particles terrain and text
    */
   private void loadAssets() {
      // Initial position of ownship
      Map<InitialConditions, Double> initialConditions = controller.getInitialConditions();
      ownship = new Vector3D.Float((float) initialConditions.get(InitialConditions.INITN).doubleValue() / 15,
         (float) initialConditions.get(InitialConditions.INITD).doubleValue() / 15,
         (float) initialConditions.get(InitialConditions.INITE).doubleValue() / 15); //(800, 150, 800)
      terrainCollection = new DefaultTerrainCollection();
      terrainCollection.setup(10, ownship);
   }

   @Override
   public float getTerrainHeight() {
      if (isRunning) {
         TreeMap<String, DefaultTerrain> terrainTree = terrainCollection.getTerrainTree();
         // Terrain object ownship is currently on
         DefaultTerrain currentTerrain = DefaultTerrain.getCurrentTerrain(terrainTree, ownship.x, ownship.z);
         // If outside world bounds, return 0 as terrain height
         return (currentTerrain == null) ? 0.0f : currentTerrain.getTerrainHeight(ownship.x, ownship.z);
      } else {
         return 0.0f;
      }
   }

   @Override
   public boolean isRunning() {
      return isRunning;
   }

   @Override
   public void requestClose() {
      isRunning = false;
   }

   @Override
   public void start() {
      this.isStarted = true;
      this.isRunning = true;
      loadAssets();
   }

   @Override
   public boolean isStarted() {
      return isStarted;
   }

   @Override
   public void onFlightDataReceived(FlightData flightData) {
      Map<FlightDataType, Double> receivedFlightData = flightData.getFlightData();
      double x = receivedFlightData.get(FlightDataType.NORTH);
      double z = receivedFlightData.get(FlightDataType.EAST);
      double y = receivedFlightData.get(FlightDataType.ALTITUDE);
      ownship.setLocation((float) (x / 15), (float) (y / 15), (float) (z / 15));
   }

}
