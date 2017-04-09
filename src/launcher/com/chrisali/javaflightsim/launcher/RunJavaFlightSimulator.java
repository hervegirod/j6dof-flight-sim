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
package com.chrisali.javaflightsim.launcher;

import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.menus.MainFrame;
import com.chrisali.javaflightsim.otw.LWJGLWorldRenderer;
import com.chrisali.javaflightsim.plotting.PlotWindow;
import javax.swing.SwingUtilities;

/**
 * Runner class to start Java Flight Simulator
 *
 * @author Christopher Ali
 *
 */
public class RunJavaFlightSimulator {

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            runApp();
         }
      });
   }

   /**
    * Initializes {@link SimulationController} and {@link MainFrame}; due to cross-referencing
    * needed with both objects, {@link SimulationController#setMainFrame(MainFrame)} needs to be
    * called
    */
   private static void runApp() {
      SimulationController controller = new SimulationController();
      LWJGLWorldRenderer lwjglRenderer = new LWJGLWorldRenderer();
      PlotWindow plotWindow = new PlotWindow();
      plotWindow.setSimulationController(controller);
      lwjglRenderer.setSimulationController(controller);
      controller.setDataAnalyzer(plotWindow);
      controller.setWorldRenderer(lwjglRenderer);
      controller.setTerrainProvider(lwjglRenderer);
      MainFrame mainFrame = new MainFrame(controller);

      // Pass in mainFrame reference so that OTW display can get Canvas
      // reference
      controller.setMainFrame(mainFrame);
   }
}
