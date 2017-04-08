/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chrisali.javaflightsim.rendering;

import com.chrisali.javaflightsim.datatransfer.FlightDataListener;

/**
 *
 * @since 0.2
 */
public interface WorldRenderer extends FlightDataListener {
   /**
    * Return the terrain height.
    *
    * @return Height of terrain at the ownship's current position
    */
   public float getTerrainHeight();

   /**
    * Return true if the WorldRenderer is currently running.
    *
    * @return true if the WorldRenderer is currently running
    */
   public boolean isRunning();

   /**
    * Request the WorldRenderer to close.
    */
   public void requestClose();

   /**
    * Start the world renderer.
    */
   public void start();
}
