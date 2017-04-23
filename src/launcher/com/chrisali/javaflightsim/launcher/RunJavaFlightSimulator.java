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
package com.chrisali.javaflightsim.launcher;

import com.chrisali.javaflightsim.conf.Configuration;
import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.gui.Instruments;
import com.chrisali.javaflightsim.gui.SimulationWindowManager;
import com.chrisali.javaflightsim.instruments.InstrumentPanel;
import com.chrisali.javaflightsim.launcher.menus.MainFrame;
import com.chrisali.javaflightsim.otw.LWJGLWorld;
import com.chrisali.javaflightsim.plotting.PlotWindow;
import com.chrisali.javaflightsim.rendering.terrain.DefaultTerrainProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.SwingUtilities;

/**
 * Runner class to start Java Flight Simulator.
 *
 * @author Christopher Ali
 * @version 0.5f
 */
public class RunJavaFlightSimulator {
   private volatile String aircraftName = null;
   private volatile boolean hasMenus = true;
   private volatile boolean hasInstruments = true;
   private volatile boolean hasPlot = true;
   private volatile boolean hasWorld = true;
   private volatile boolean hasSounds = true;

   private static boolean isTrue(String valueS) {
      return valueS.equalsIgnoreCase("true");
   }

   public RunJavaFlightSimulator() {
   }

   private void launch(String[] args) {
      Map<String, String> props = LauncherUtils.getLaunchProperties(args);
      Iterator<Entry<String, String>> it = props.entrySet().iterator();
      while (it.hasNext()) {
         Entry<String, String> entry = it.next();
         if (entry.getKey().equals("hasMenus")) {
            hasMenus = isTrue(entry.getValue());
         } else if (entry.getKey().equals("hasInstruments")) {
            hasInstruments = isTrue(entry.getValue());
         } else if (entry.getKey().equals("hasWorld")) {
            hasWorld = isTrue(entry.getValue());
         } else if (entry.getKey().equals("hasPlot")) {
            hasPlot = isTrue(entry.getValue());
         } else if (entry.getKey().equals("aircraftName")) {
            aircraftName = entry.getValue();
         } else if (entry.getKey().equals("hasSounds")) {
            hasSounds = isTrue(entry.getValue());
         }
      }

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            runApp();
         }
      });
   }

   public static void main(String[] args) {
      RunJavaFlightSimulator flightSim = new RunJavaFlightSimulator();
      flightSim.launch(args);
   }

   /**
    * Initializes {@link SimulationController} and {@link MainFrame}; due to cross-referencing
    * needed with both objects, {@link SimulationController#setMainFrame(MainFrame)} needs to be
    * called
    */
   private void runApp() {
      Configuration conf = Configuration.getInstance();
      conf.setDefaultConfiguration();
      if (aircraftName != null) {
         conf.setAircraft(aircraftName);
      }

      SimulationController controller = new SimulationController();
      if (hasWorld) {
         LWJGLWorld lwjglRenderer = new LWJGLWorld();
         lwjglRenderer.setIncludeSounds(hasSounds);
         controller.setWorldRenderer(lwjglRenderer);
         controller.setTerrainProvider(lwjglRenderer);
         lwjglRenderer.setSimulationController(controller);
      } else {
         DefaultTerrainProvider terrainProvider = new DefaultTerrainProvider();
         controller.setTerrainProvider(terrainProvider);
         terrainProvider.setSimulationController(controller);
      }
      if (hasPlot) {
         PlotWindow plotWindow = new PlotWindow();
         plotWindow.setSimulationController(controller);
         controller.setDataAnalyzer(plotWindow);
      }
      Instruments instruments = null;
      if (hasInstruments) {
         instruments = new InstrumentPanel();
      }

      if (hasMenus) {
         MainFrame mainFrame = new MainFrame(controller, instruments);
         // Pass in mainFrame reference so that OTW display can get Canvas
         // reference
         controller.setGUIManager(mainFrame);
      } else {
         SimulationWindowManager guiManager = new SimulationWindowManager(instruments);
         guiManager.setSimulationController(controller);
         controller.setGUIManager(guiManager);
         guiManager.initSimulationWindow();
         guiManager.startSimulation();
      }
   }
}
