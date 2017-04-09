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
import com.chrisali.javaflightsim.simulation.integration.SimOuts;
import java.util.List;
import java.util.Map;

/**
 * This interface represents DataAnalyzers.
 *
 * @since 0.2 Herve
 */
public interface DataAnalyzer {
   /**
    * Return true if the DataAnalyzer is currently running.
    *
    * @return true if the DataAnalyzer is currently running
    */
   public boolean isRunning();

   /**
    * Set the simulation controller.
    *
    * @param controller the simulation controller
    */
   public void setSimulationController(SimulationController controller);

   /**
    * Refresh the Analyzer.
    *
    * @param logsOut the logsOut
    */
   public void refresh(List<Map<SimOuts, Double>> logsOut);
}
