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
package com.chrisali.javaflightsim.controllers;

import java.io.File;

/**
 *
 * @since 0.2 Herve
 */
public class Configuration {
   private static Configuration conf = null;
   private File resourcesDir = null;
   private File aircraftListDir = null;
   private File simconfigDir = null;
   private String aircraftName = null;
   private File aircraftDir = null;

   private Configuration() {
   }

   /**
    * Return the unique instance.
    *
    * @return the unique instance
    */
   public static Configuration getInstance() {
      if (conf == null) {
         conf = new Configuration();
      }
      return conf;
   }

   /**
    * Set the default configuration. The directories are all relative to the user directory.
    */
   public void setDefaultConfiguration() {
      File dir = new File(System.getProperty("user.dir"));
      File _aircraftsDir = new File(dir, "Aircraft");
      File _resourcesDir = new File(dir, "Resources");
      File _simconfigDir = new File(dir, "SimConfig");
      setResourcesConfig(_resourcesDir);
      setAircraftsConfig(_aircraftsDir);
      setSimConfig(_simconfigDir);
   }

   /**
    * Sets the aircrafts list configuration directory. The default directory will have the "Aircraft" name and
    * be a child of the user directory.
    *
    * @param aircraftListDir the configuration directory
    * @return true if the selected directory exist and is a directory
    */
   public boolean setAircraftsConfig(File aircraftListDir) {
      if (aircraftListDir != null && aircraftListDir.exists() && aircraftListDir.isDirectory()) {
         this.aircraftListDir = aircraftListDir;
         return true;
      } else {
         this.aircraftListDir = null;
         this.aircraftDir = null;
         return false;
      }
   }

   /**
    * Return the aircrafts list configuration directory.
    *
    * @return the aircrafts list configuration directory
    */
   public File getAircraftListConfig() {
      return aircraftListDir;
   }

   /**
    * Sets the resources configuration directory. The default directory will have the "Resources" name and
    * be a child of the user directory.
    *
    * @param resourcesDir the configuration directory
    * @return true if the selected directory exist and is a directory
    */
   public boolean setResourcesConfig(File resourcesDir) {
      if (resourcesDir != null && resourcesDir.exists() && resourcesDir.isDirectory()) {
         this.resourcesDir = resourcesDir;
         return true;
      } else {
         this.resourcesDir = null;
         return false;
      }
   }

   /**
    * Return the resources configuration directory.
    *
    * @return the resources configuration directory
    */
   public File getResourcesConfig() {
      return resourcesDir;
   }

   /**
    * Return the sound resources directory.
    *
    * @return the sound resources directory
    */
   public File getSoundResources() {
      File audio = new File(resourcesDir, "Audio");
      return audio;
   }

   /**
    * Return the terrain resources directory.
    *
    * @return the terrain resources directory
    */
   public File getTerrain() {
      File audio = new File(resourcesDir, "Terrain");
      return audio;
   }

   /**
    * Return the fonts resources directory.
    *
    * @return the fonts resources directory
    */
   public File getFonts() {
      File fonts = new File(resourcesDir, "Fonts");
      return fonts;
   }

   /**
    * Return the entities resources directory.
    *
    * @return the entities resources directory
    */
   public File getEntities() {
      File entities = new File(resourcesDir, "Entities");
      return entities;
   }

   /**
    * Return the particles resources directory.
    *
    * @return the particles resources directory
    */
   public File getParticles() {
      File entities = new File(resourcesDir, "Particles");
      return entities;
   }

   /**
    * Return the water resources directory.
    *
    * @return the water resources directory
    */
   public File getWater() {
      File entities = new File(resourcesDir, "Water");
      return entities;
   }

   /**
    * Sets the simulation configuration directory. The default directory will have the "SimConfig" name and
    * be a child of the user directory.
    *
    * @param simconfigDir the simulation configuration directory
    * @return true if the selected directory exist and is a directory
    */
   public boolean setSimConfig(File simconfigDir) {
      if (simconfigDir != null && simconfigDir.exists() && simconfigDir.isDirectory()) {
         this.simconfigDir = simconfigDir;
         return true;
      } else {
         this.simconfigDir = null;
         return false;
      }
   }

   /**
    * Return the simulation configuration directory.
    *
    * @return the simulation configuration directory
    */
   public File getSimConfig() {
      return simconfigDir;
   }

   /**
    * Return true if the simulation configuration directory exist and is a directory.
    *
    * @return true if the simulation configuration directory exist and is a directory
    */
   public boolean hasSimulationConfig() {
      return simconfigDir != null;
   }

   /**
    * Return the Simulation Setup configuration.
    *
    * @return the Simulation Setup configuration
    */
   public File getSimulationSetupConfig() {
      if (simconfigDir != null) {
         return new File(simconfigDir, "SimulationSetup.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the display Setup configuration.
    *
    * @return the display Setup configuration
    */
   public File getDisplaySetupConfig() {
      if (simconfigDir != null) {
         return new File(simconfigDir, "DisplaySetup.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the audio Setup configuration.
    *
    * @return the audio Setup configuration
    */
   public File getAudioSetupConfig() {
      if (simconfigDir != null) {
         return new File(simconfigDir, "AudioSetup.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the integrator configuration.
    *
    * @return the integrator configuration
    */
   public File getIntegratorConfig() {
      if (simconfigDir != null) {
         return new File(simconfigDir, "IntegratorConfig.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the initial conditions configuration.
    *
    * @return the initial conditions configuration
    */
   public File getInitialConditionsConfig() {
      if (simconfigDir != null) {
         return new File(simconfigDir, "InitialConditions.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the initial controls configuration.
    *
    * @return the initial controls configuration
    */
   public File getInitialControlsConfig() {
      if (simconfigDir != null) {
         return new File(simconfigDir, "InitialControls.txt");
      } else {
         return null;
      }
   }

   /**
    * Sets the selected aircraft name and return its associated configuration.
    *
    * @param aircraftName the aircraft name
    * @return the selected aircraft configuration
    */
   public File setAircraft(String aircraftName) {
      this.aircraftName = aircraftName;
      if (aircraftListDir != null) {
         aircraftDir = new File(aircraftListDir, aircraftName);
         if (!aircraftDir.exists() || !aircraftDir.isDirectory()) {
            aircraftDir = null;
         }
      }
      return aircraftDir;
   }

   /**
    * Return the selected aircraft configuration.
    *
    * @return the selected aircraft configuration
    */
   public File getAircraftConfig() {
      return aircraftDir;
   }

   /**
    * Return the selected aircraft name.
    *
    * @return the selected aircraft name
    */
   public String getAircraftName() {
      return aircraftName;
   }

   /**
    * Return the selected aircraft mass properties.
    *
    * @return the selected aircraft mass properties
    */
   public File getAircraftMassProperties() {
      if (aircraftDir != null) {
         return new File(aircraftDir, "MassProperties.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the selected aircraft wing geometry configuration.
    *
    * @return the selected aircraft wing geometry configuration
    */
   public File getAircraftWingGeometry() {
      if (aircraftDir != null) {
         return new File(aircraftDir, "WingGeometry.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the selected aircraft ground reaction configuration.
    *
    * @return the selected aircraft ground reaction configuration
    */
   public File getAircraftGroundReaction() {
      if (aircraftDir != null) {
         return new File(aircraftDir, "GroundReaction.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the selected aircraft propulsion configuration.
    *
    * @return the selected aircraft propulsion configuration
    */
   public File getAircraftPropulsion() {
      if (aircraftDir != null) {
         return new File(aircraftDir, "Propulsion.txt");
      } else {
         return null;
      }
   }

   /**
    * Return the selected aircraft aero configuration.
    *
    * @return the selected aircraft aero configuration
    */
   public File getAircraftAero() {
      if (aircraftDir != null) {
         return new File(aircraftDir, "Aero.txt");
      } else {
         return null;
      }
   }
}
