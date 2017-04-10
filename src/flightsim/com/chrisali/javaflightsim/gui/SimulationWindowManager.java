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
package com.chrisali.javaflightsim.gui;

import com.chrisali.javaflightsim.controllers.SimulationController;

/**
 *
 * @since 0.2
 */
public class SimulationWindowManager implements GUIManager {
   private SimulationController controller;
   private SimulationWindow simulationWindow;
   private final Instruments instruments;

   public SimulationWindowManager(Instruments instruments) {
      this.instruments = instruments;
   }

   public void setSimulationController(SimulationController controller) {
      this.controller = controller;
   }

   @Override
   public Instruments getInstruments() {
      return instruments;
   }

   /**
    * Return the SimulationWindow object.
    *
    * @return {@link SimulationWindow} object.
    */
   @Override
   public SimulationWindow getSimulationWindow() {
      return simulationWindow;
   }

   /**
    * (Re)initializes simulationWindow object so that instrument panel and OTW view are scaled correctly depending
    * on if the instrument panel is shown or not.
    */
   @Override
   public void initSimulationWindow() {
      simulationWindow = new SimulationWindow(controller, instruments);
      simulationWindow.setClosePanelListener(new ClosePanelListener() {
         @Override
         public void panelWindowClosed() {
            controller.stopSimulation();
            simulationWindow.setVisible(false);
         }
      });
   }

   public void startSimulation() {
      controller.startSimulation();
      simulationWindow.setVisible(true);
   }

   @Override
   public void disposeSimulationWindow() {
      simulationWindow.dispose();
   }
}
