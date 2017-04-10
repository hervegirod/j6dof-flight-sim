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

import com.chrisali.javaflightsim.conf.DisplayOptions;
import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.instruments.InstrumentPanel;
import com.chrisali.javaflightsim.rendering.RunWorld;
import com.chrisali.javaflightsim.simulation.setup.Options;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * JPanel that integrates {@link InstrumentPanel} and the OpenGL OTW view from {@link RunWorld},
 * producing a display similar to a traditional flight simulator program; uses addNotify() and removeNodity()
 * stop OTW thread on this thread to destroy OpenGL display correctly when window is closed. The solution is shown here:
 * <p>
 * http://stackoverflow.com/questions/26199534/how-to-attach-opengl-display-to-a-jframe-and-dispose-of-it-properly</p>
 *
 * @author Christopher Ali
 *
 */
public class SimulationWindow extends JFrame {
   private static final long serialVersionUID = 7290660958478331031L;
   private Instruments instruments;
   private final Canvas outTheWindowCanvas;
   private ClosePanelListener closePanelListener;
   private SimulationController controller = null;
   private GridBagConstraints gc = null;

   /**
    * Constructor for simulation window; takes {@link SimulationController} argument to gain access to
    * starting and stopping threads for {@link RunWorld} on this thread.
    *
    * @param controller the Simulation Controller
    * @param instruments the instruments
    */
   public SimulationWindow(SimulationController controller, Instruments instruments) {
      super("Java Flight Simulator");
      this.controller = controller;
      setLayout(new GridBagLayout());
      gc = new GridBagConstraints();

      gc.fill = GridBagConstraints.BOTH;
      gc.gridy = 0;
      gc.weighty = 100;
      gc.weightx = 100;

      //---------------------- Out the Window Canvas -------------------------------------------
      outTheWindowCanvas = new Canvas() {

         private static final long serialVersionUID = 6438710048789252704L;

         @Override
         public void addNotify() {
            super.addNotify();
            controller.startOTWThread();
         }

         @Override
         public void removeNotify() {
            controller.stopOTWThread();
            super.removeNotify();
         }
      };

      add(outTheWindowCanvas, gc);

      //------------------------- Instrument Panel ---------------------------------------------
      gc.gridy++;

      if (instruments != null && controller.getSimulationOptions().contains(Options.INSTRUMENT_PANEL)) {
         controller.getFlightData().addFlightDataListener(instruments);
         add((Component) instruments, gc);
      }

      //========================== Window Settings =============================================
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            if (closePanelListener != null) {
               closePanelListener.panelWindowClosed();
               dispose(); // calls removeNotify(), which gracefully stops OTW thread and OpenGL display
            }
         }
      });

      Dimension windowSize = new Dimension(controller.getDisplayOptions().get(DisplayOptions.DISPLAY_WIDTH),
              controller.getDisplayOptions().get(DisplayOptions.DISPLAY_HEIGHT));
      setSize(windowSize.width, windowSize.height);
      setResizable(false);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
   }

   /**
    * Return the Simulation Controller.
    *
    * @return the Simulation Controlle
    */
   public SimulationController getSimulationController() {
      return controller;
   }

   /**
    * Return the Instruments object.
    *
    * @return the Instruments object to set {@link FlightDataListener} to the instrument panel
    */
   public Instruments getInstruments() {
      return instruments;
   }

   /**
    * @return {@link Canvas} object used to render the {@link RunWorld} out the window display
    */
   public Canvas getOutTheWindowCanvas() {
      return outTheWindowCanvas;
   }

   /**
    * Sets a listener to monitor for a window closing event so that the simulation can stop
    *
    * @param closePanelListener
    */
   public void setClosePanelListener(ClosePanelListener closePanelListener) {
      this.closePanelListener = closePanelListener;
   }
}
