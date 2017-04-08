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
package com.chrisali.javaflightsim.rendering;

import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.menus.MainFrame;
import com.chrisali.javaflightsim.menus.SimulationWindow;
import com.chrisali.javaflightsim.menus.optionspanel.AudioOptions;
import com.chrisali.javaflightsim.menus.optionspanel.DisplayOptions;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;

/**
 * Runner class for out the window display for Java Flight Sim. It utilizes LWJGL to create a 3D world in OpenGL.
 * The display runs in a separate thread that receives data from {@link FlightData} via {@link FlightDataListener}
 *
 * @author Christopher Ali
 * @author Herve Girod
 * @since 0.1
 */
public class RunWorld implements Runnable {
   private SimulationController controller;
   private WorldRenderer worldRenderer = null;
   private TerrainProvider terrainProvider = null;

   /**
    * Sets up OTW display with {@link DisplayOptions} and {@link AudioOptions}, as well as a link to
    * {@link AircraftBuilder} to determine if multiple engines in aircraft. If {@link SimulationController}
    * object specified, display will embed itself within {@link SimulationWindow} in {@link MainFrame}
    *
    * @param controller the Simulation Controller
    */
   public RunWorld(SimulationController controller) {
      this.controller = controller;
   }

   /**
    * Set the World Renderer.
    *
    * @param worldRenderer the World Renderer
    */
   public void setWorldRenderer(WorldRenderer worldRenderer) {
      this.worldRenderer = worldRenderer;
   }

   /**
    * Set the Terrain Provider.
    *
    * @param terrainProvider the Terrain Provider
    */
   public void setTerrainProvider(TerrainProvider terrainProvider) {
      this.terrainProvider = terrainProvider;
   }

   /**
    * Return the World Renderer.
    *
    * @return the World Renderer
    */
   public WorldRenderer getWorldRenderer() {
      return worldRenderer;
   }

   /**
    * Return the Terrain Provider.
    *
    * @return the Terrain Provider
    */
   public TerrainProvider getTerrainProvider() {
      return terrainProvider;
   }

   /**
    * Return the Simulation Controller.
    *
    * @return the Simulation Controller
    */
   public SimulationController getSimulationController() {
      return controller;
   }

   @Override
   public void run() {
      if (worldRenderer != null) {
         worldRenderer.start();
      }
      if (!terrainProvider.isStarted()) {
         terrainProvider.start();
      }

   }

   /**
    * Return true if the WorldRenderer or the TerrainProvider are currently running.
    *
    * @return true if the WorldRenderer or the TerrainProvider are currently running
    */
   public synchronized boolean isRunning() {
      boolean isRunning = false;
      if (worldRenderer != null) {
         isRunning = worldRenderer.isRunning();
      }
      isRunning = isRunning || terrainProvider.isRunning();
      return isRunning;
   }

   /**
    * Request the WorldRenderer to close.
    */
   public synchronized void requestClose() {
      if (worldRenderer != null) {
         worldRenderer.requestClose();
      }
      if (terrainProvider.isRunning()) {
         terrainProvider.requestClose();
      }
   }

   /**
    * Return the terrain height.
    *
    * @return Height of terrain at the ownship's current position
    */
   public float getTerrainHeight() {
      return terrainProvider.getTerrainHeight();
   }
}
