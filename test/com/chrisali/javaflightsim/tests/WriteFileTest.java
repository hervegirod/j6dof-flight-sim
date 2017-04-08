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
package com.chrisali.javaflightsim.tests;

import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import com.chrisali.javaflightsim.simulation.setup.IntegratorConfig;
import com.chrisali.javaflightsim.utilities.FileUtilities;
import java.io.File;
import java.util.Map;

public class WriteFileTest {

   private static final String FILE_PATH = ".\\SimConfig\\";
   private static final String OLD_CONFIG_FILE_NAME = "IntegratorConfig";
   private static final String NEW_CONFIG_FILE_NAME = "NewIntegratorConfig";

   public static void main(String[] args) {
      Map<IntegratorConfig, Double> simConfig = IntegrationSetup.gatherIntegratorConfig(OLD_CONFIG_FILE_NAME);

      FileUtilities.writeConfigFile(NEW_CONFIG_FILE_NAME, FILE_PATH, simConfig);

      Map<IntegratorConfig, Double> newSimConfig = IntegrationSetup.gatherIntegratorConfig(NEW_CONFIG_FILE_NAME);

      if (simConfig.equals(newSimConfig)) {
         System.out.println("Parsed Maps are identical");
      } else {
         System.err.println("Parsed Maps are different!");
      }

      new File(FILE_PATH + NEW_CONFIG_FILE_NAME + ".txt").deleteOnExit();
   }

}
