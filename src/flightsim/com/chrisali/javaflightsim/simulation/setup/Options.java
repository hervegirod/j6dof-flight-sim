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
package com.chrisali.javaflightsim.simulation.setup;

import com.chrisali.javaflightsim.instrumentpanel.InstrumentPanel;
import com.chrisali.javaflightsim.gui.SimulationWindow;
import com.chrisali.javaflightsim.simulation.controls.FlightControlsUtilities;
import com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Joystick;
import com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Mouse;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;

/**
 * Provides Enums for the options EnumSet to provide the following options:
 *
 * <p>
 * ANALYSIS_MODE - Removes real-time aspect of the simulation, generates 3 doublets (aileron, rudder and elevator) using
 * {@link FlightControlsUtilities#doubletSeries(java.util.Map, double)}
 * into controls, and generates plots at the end of the run; used to analyze transient dynamics of the aircraft </p>
 * <p>
 * UNLIMITED_FLIGHT - Removes the end of the simulation to allow for infinite flight; data logging is limited to the last 100 seconds of simulation</p>
 * <p>
 * PAUSED - Pauses the integration and therefore the simulation; used in combination with RESET to return the simulation to initial conditions</p>
 * <p>
 * RESET - Resets the integration to initial conditions using {@link IntegrationSetup#gatherInitialConditions(String)}</p>
 * <p>
 * CONSOLE_DISPLAY - Displays every piece of data in {@link Integrate6DOFEquations#getSimOut()} in the console for each step of integration</p>
 * <p>
 * USE_JOYSTICK - Uses JInput to integrate a {@link Joystick} and {@link com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Keyboard} to allow pilot in the loop simulation</p>
 * <p>
 * USE_MOUSE - Uses JInput to integrate a {@link Mouse} and {@link com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Keyboard} to allow pilot in the loop simulation</p>
 * <p>
 * USE_CH_CONTROLS - Uses JInput to integrate a {@link com.chrisali.javaflightsim.simulation.controls.hidcontrollers.CHControls} and {@link com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Keyboard} to allow pilot in the loop simulation</p>
 * <p>
 * USE_KEYBOARD_ONLY - Uses JInput to integrate only a {@link com.chrisali.javaflightsim.simulation.controls.hidcontrollers.Keyboard} to allow pilot in the loop simulation</p>
 * <p>
 * INSTRUMENT_PANEL - Displays {@link InstrumentPanel} view in {@link SimulationWindow}</p>
 */
public enum Options {
   ANALYSIS_MODE("Analysis Mode"),
   UNLIMITED_FLIGHT("Unlimited Flight"),
   PAUSED("Paused"),
   RESET("Reset"),
   CONSOLE_DISPLAY("Console Display"),
   USE_JOYSTICK("Use Joystick"),
   USE_MOUSE("Use Mouse"),
   USE_CH_CONTROLS("Use CH Controls"),
   USE_KEYBOARD_ONLY("Use Keyboard Only"),
   INSTRUMENT_PANEL("Show Instrument Panel");

   private String option;

   private Options(String option) {
      this.option = option;
   }

   public String toString() {
      return option;
   }
}
