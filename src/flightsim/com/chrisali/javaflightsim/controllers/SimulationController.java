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

import com.chrisali.javaflightsim.consoletable.ConsoleTablePanel;
import com.chrisali.javaflightsim.datatransfer.EnvironmentData;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.menus.MainFrame;
import com.chrisali.javaflightsim.menus.SimulationWindow;
import com.chrisali.javaflightsim.menus.optionspanel.AudioOptions;
import com.chrisali.javaflightsim.menus.optionspanel.DisplayOptions;
import com.chrisali.javaflightsim.plotting.PlotWindow;
import com.chrisali.javaflightsim.rendering.DataAnalyzer;
import com.chrisali.javaflightsim.rendering.RunWorld;
import com.chrisali.javaflightsim.rendering.TerrainProvider;
import com.chrisali.javaflightsim.rendering.WorldRenderer;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;
import com.chrisali.javaflightsim.simulation.aircraft.MassProperties;
import com.chrisali.javaflightsim.simulation.controls.FlightControlType;
import com.chrisali.javaflightsim.simulation.controls.FlightControls;
import com.chrisali.javaflightsim.simulation.integration.Integrate6DOFEquations;
import com.chrisali.javaflightsim.simulation.integration.SimOuts;
import com.chrisali.javaflightsim.simulation.setup.InitialConditions;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import com.chrisali.javaflightsim.simulation.setup.IntegratorConfig;
import com.chrisali.javaflightsim.simulation.setup.Options;
import com.chrisali.javaflightsim.simulation.setup.Trimming;
import com.chrisali.javaflightsim.utilities.FileUtilities;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Controls the configuration and running of processes supporting the simulation component of JavaFlightSim. This consists of:
 * <p>
 * The simulation engine that integrates the 6DOF equations ({@link Integrate6DOFEquations})</p>
 * <p>
 * Plotting of the simulation states and data ({@link PlotWindow})</p>
 * <p>
 * Raw data display of simulation states ({@link ConsoleTablePanel})</p>
 * <p>
 * Transmission of flight data to the instrument panel and out the window display ({@link FlightData})</p>
 * <p>
 * Transmission of environment data to the simulation ({@link EnvironmentData})</p>
 *
 * @author Christopher Ali
 * @author Herve Girod
 * @since 0.1
 */
public class SimulationController {
   // Configuration
   private EnumMap<DisplayOptions, Integer> displayOptions;
   private EnumMap<AudioOptions, Float> audioOptions;
   private EnumSet<Options> simulationOptions;
   private EnumMap<InitialConditions, Double> initialConditions;
   private EnumMap<IntegratorConfig, Double> integratorConfig;
   private EnumMap<FlightControlType, Double> initialControls;

   // Simulation
   private FlightControls flightControls;
   private Thread flightControlsThread;
   private Integrate6DOFEquations runSim;
   private Thread simulationThread;
   private FlightData flightData;
   private Thread flightDataThread;

   // providers
   private WorldRenderer worldRenderer = null;
   private TerrainProvider terrainProvider = null;
   private DataAnalyzer dataAnalyzer = null;

   // Aircraft
   private AircraftBuilder ab;
   private EnumMap<MassProperties, Double> massProperties;

   // Menus and Integrated Simulation Window
   private MainFrame mainFrame;

   // Raw Data Console
   private ConsoleTablePanel consoleTablePanel;

   // Out the Window
   private RunWorld outTheWindow;
   private Thread outTheWindowThread;
   private Thread environmentDataThread;
   private EnvironmentData environmentData;

   /**
    * Constructor for the controller that initializes initial settings, configurations and conditions
    * to be edited through the menu options in the view
    */
   public SimulationController() {
      simulationOptions = FileUtilities.parseSimulationSetup();
      displayOptions = FileUtilities.parseDisplaySetup();
      audioOptions = FileUtilities.parseAudioSetup();

      Configuration conf = Configuration.getInstance();
      initialConditions = IntegrationSetup.gatherInitialConditions(conf.getInitialConditionsConfig());
      integratorConfig = IntegrationSetup.gatherIntegratorConfig(conf.getIntegratorConfig());
      initialControls = IntegrationSetup.gatherInitialControls(conf.getInitialControlsConfig());

      String aircraftName = FileUtilities.parseSimulationSetupForAircraft();
      Configuration.getInstance().setAircraft(aircraftName);
      ab = new AircraftBuilder(aircraftName);
   }

   //=============================== Configuration ===========================================================
   /**
    * @return simulationOptions EnumSet
    */
   public EnumSet<Options> getSimulationOptions() {
      return simulationOptions;
   }

   /**
    * @return displayOptions EnumMap
    */
   public EnumMap<DisplayOptions, Integer> getDisplayOptions() {
      return displayOptions;
   }

   /**
    * @return audioOptions EnumMap
    */
   public EnumMap<AudioOptions, Float> getAudioOptions() {
      return audioOptions;
   }

   /**
    * Updates simulation and display options and then saves the configurations to text files using either
    * {@link FileUtilities#writeConfigFile(java.lang.String, java.lang.String, java.util.Set, java.lang.String)} or
    * {@link FileUtilities#writeConfigFile(java.lang.String, java.lang.String, java.util.Map)}.
    *
    * @param newOptions the new options
    * @param newDisplayOptions the new display options
    * @param newAudioOptions the new audio options
    */
   public void updateOptions(EnumSet<Options> newOptions, EnumMap<DisplayOptions, Integer> newDisplayOptions,
      EnumMap<AudioOptions, Float> newAudioOptions) {
      Configuration conf = Configuration.getInstance();
      if (conf.hasSimulationConfig()) {
         File simulSetup = conf.getSimulationSetupConfig();
         File displaySetup = conf.getDisplaySetupConfig();
         File audioSetup = conf.getAudioSetupConfig();
         simulationOptions = EnumSet.copyOf(newOptions);
         displayOptions = newDisplayOptions;
         audioOptions = newAudioOptions;

         FileUtilities.writeConfigFile(simulSetup, simulationOptions, ab.getAircraft().getName());
         FileUtilities.writeConfigFile(displaySetup, newDisplayOptions);
         FileUtilities.writeConfigFile(audioSetup, newAudioOptions);
      }
   }

   /**
    * Calls the {@link AircraftBuilder} constructor with using the aircraftName argument and updates the SimulationSetup.txt
    * configuration file with the new selected aircraft.
    *
    * @param aircraftName the aircraft name
    */
   public void updateAircraft(String aircraftName) {
      Configuration conf = Configuration.getInstance();
      conf.setAircraft(aircraftName);
      ab = new AircraftBuilder(aircraftName);
      if (conf.hasSimulationConfig()) {
         File simulSetup = conf.getSimulationSetupConfig();
         FileUtilities.writeConfigFile(simulSetup, simulationOptions, aircraftName);
      }
   }

   /**
    * Updates the MassProperties config file for the selected aircraft using aircraftName.
    *
    * @param aircraftName the aircraft name
    * @param fuelWeight the fuel weight
    * @param payloadWeight thr payload weight
    */
   public void updateMassProperties(String aircraftName, double fuelWeight, double payloadWeight) {
      massProperties = FileUtilities.parseMassProperties(aircraftName);

      massProperties.put(MassProperties.WEIGHT_FUEL, fuelWeight);
      massProperties.put(MassProperties.WEIGHT_PAYLOAD, payloadWeight);
      Configuration conf = Configuration.getInstance();
      File massProps = conf.getAircraftMassProperties();
      FileUtilities.writeConfigFile(massProps, massProperties);
   }

   /**
    * @return integratorConfig EnumMap
    */
   public EnumMap<IntegratorConfig, Double> getIntegratorConfig() {
      return integratorConfig;
   }

   /**
    * Updates the IntegratorConfig file with stepSize inverted and converted to a double.
    *
    * @param stepSize the stepSize
    */
   public void updateIntegratorConfig(int stepSize) {
      integratorConfig.put(IntegratorConfig.DT, (1 / ((double) stepSize)));
      Configuration conf = Configuration.getInstance();
      if (conf.hasSimulationConfig()) {
         FileUtilities.writeConfigFile(conf.getIntegratorConfig(), integratorConfig);
      }
   }

   /**
    * @return initialConditions EnumMap
    */
   public EnumMap<InitialConditions, Double> getInitialConditions() {
      return initialConditions;
   }

   /**
    * Updates initialConditions file with the following arguments, converted to radians and ft/sec.
    *
    * @param coordinates [latitude, longitude]
    * @param heading the heading
    * @param altitude the altitude
    * @param airspeed the airspeed
    */
   public void updateInitialConditions(double[] coordinates, double heading, double altitude, double airspeed) {
      initialConditions.put(InitialConditions.INITLAT, Math.toRadians(coordinates[0]));
      initialConditions.put(InitialConditions.INITLON, Math.toRadians(coordinates[1]));
      initialConditions.put(InitialConditions.INITPSI, Math.toRadians(heading));
      initialConditions.put(InitialConditions.INITU, FileUtilities.toFtPerSec(airspeed));
      initialConditions.put(InitialConditions.INITD, altitude);

      // Temporary method to calculate north/east position from lat/lon position
      initialConditions.put(InitialConditions.INITN, (Math.sin(Math.toRadians(coordinates[0])) * 20903520));
      initialConditions.put(InitialConditions.INITE, (Math.sin(Math.toRadians(coordinates[1])) * 20903520));

      Configuration conf = Configuration.getInstance();
      if (conf.hasSimulationConfig()) {
         FileUtilities.writeConfigFile(conf.getInitialConditionsConfig(), initialConditions);
      }
   }

   /**
    * Updates the InitialControls config file.
    */
   public void updateIninitialControls() {
      Configuration conf = Configuration.getInstance();
      if (conf.hasSimulationConfig()) {
         FileUtilities.writeConfigFile(conf.getInitialControlsConfig(), initialConditions);
      }
   }

   /**
    * @return initialControls EnumMap
    */
   public EnumMap<FlightControlType, Double> getInitialControls() {
      return initialControls;
   }

   //=============================== Simulation ===========================================================
   /**
    * @return instance of simulation
    */
   public Integrate6DOFEquations getSimulation() {
      return runSim;
   }

   /**
    * @return {@link AircraftBuilder} object
    */
   public AircraftBuilder getAircraftBuilder() {
      return ab;
   }

   /**
    * Allows {@link AircraftBuilder} to be changed to a different aircraft outside of being parsed in
    * the SimulationSetup.txt configuration file.
    *
    * @param ab the AircraftBuilder
    */
   public void setAircraftBuilder(AircraftBuilder ab) {
      this.ab = ab;
   }

   /**
    * @return ArrayList of simulation output data
    * @see SimOuts
    */
   public List<Map<SimOuts, Double>> getLogsOut() {
      return runSim.getLogsOut();
   }

   /**
    * @return if runSim was able to clear simulation data kept in logsOut
    */
   public boolean clearLogsOut() {
      if (runSim != null) {
         return runSim.clearLogsOut();
      } else {
         return false;
      }
   }

   public void setWorldRenderer(WorldRenderer worldRenderer) {
      this.worldRenderer = worldRenderer;
   }

   public void setTerrainProvider(TerrainProvider terrainProvider) {
      this.terrainProvider = terrainProvider;
   }

   public void setDataAnalyzer(DataAnalyzer dataAnalyzer) {
      this.dataAnalyzer = dataAnalyzer;
      dataAnalyzer.setSimulationController(this);
   }

   /**
    * Initializes, trims and starts the flight controls, simulation (and flight and environment data, if selected) threads.
    * Depending on options specified, a console panel and/or plot window will also be initialized and opened
    */
   public void startSimulation() {
      Trimming.trimSim(this, false);

      flightControls = new FlightControls(this);
      flightControlsThread = new Thread(flightControls);

      runSim = new Integrate6DOFEquations(flightControls, this);
      simulationThread = new Thread(runSim);

      flightControlsThread.start();
      simulationThread.start();

      if (simulationOptions.contains(Options.CONSOLE_DISPLAY)) {
         initializeConsole();
      }

      if (simulationOptions.contains(Options.ANALYSIS_MODE)) {
         try {
            // Wait a bit to allow the simulation to finish running
            Thread.sleep(1000);
            analyzeSimulation();
            //Stop flight controls thread after analysis finished
            FlightControls.setRunning(false);
         } catch (InterruptedException e) {
         }

      } else {
         outTheWindow = new RunWorld(this);
         outTheWindow.setTerrainProvider(terrainProvider);
         outTheWindow.setWorldRenderer(worldRenderer);
         //(Re)initalize simulation window to prevent scaling issues with instrument panel
         getMainFrame().initSimulationWindow();

         environmentData = new EnvironmentData(outTheWindow);
         environmentData.addEnvironmentDataListener(runSim);

         environmentDataThread = new Thread(environmentData);
         environmentDataThread.start();

         flightData = new FlightData(runSim);
         flightData.addFlightDataListener(mainFrame.getInstrumentPanel());
         flightData.addFlightDataListener(outTheWindow.getWorldRenderer());
         flightData.addFlightDataListener(outTheWindow.getTerrainProvider());

         flightDataThread = new Thread(flightData);
         flightDataThread.start();
      }
   }

   /**
    * Stops simulation, flight controls and data transfer threads (if running), closes the raw data {@link ConsoleTablePanel},
    * {@link SimulationWindow}, and opens the main menus window again
    */
   public void stopSimulation() {
      if (runSim != null && Integrate6DOFEquations.isRunning() && simulationThread != null && simulationThread.isAlive()) {
         Integrate6DOFEquations.setRunning(false);
         FlightControls.setRunning(false);
      }

      if (flightDataThread != null && flightDataThread.isAlive()) {
         FlightData.setRunning(false);
      }

      if (outTheWindowThread != null && outTheWindowThread.isAlive()) {
         EnvironmentData.setRunning(false);
      }

      getMainFrame().getSimulationWindow().dispose();
      getMainFrame().setVisible(true);
   }

   //=============================== Plotting =============================================================
   /**
    * Initializes the plot window if not already initialized, otherwise refreshes the window and sets it visible again
    */
   public void analyzeSimulation() {
      if (dataAnalyzer != null) {
         dataAnalyzer.refresh(runSim.getLogsOut());
      }
   }

   /**
    * Return true if the data analyzer is running.
    *
    * @return true if the data analyzer is running
    */
   public boolean isDataAnalyzerRunning() {
      if (dataAnalyzer == null) {
         return false;
      } else {
         return dataAnalyzer.isRunning();
      }
   }

   //=============================== Console =============================================================
   /**
    * Initializes the raw data console window and starts the auto-refresh of its contents
    */
   public void initializeConsole() {
      consoleTablePanel = new ConsoleTablePanel(this);
      consoleTablePanel.startTableRefresh();
   }

   /**
    * @return if the raw data console window is visible
    */
   public boolean isConsoleWindowVisible() {
      if (consoleTablePanel == null) {
         return false;
      } else {
         return consoleTablePanel.isVisible();
      }
   }

   /**
    * Saves the raw data in the console window to a .csv file
    *
    * @param file
    * @throws IOException
    */
   public void saveConsoleOutput(File file) throws IOException {
      FileUtilities.saveToCSVFile(file, runSim.getLogsOut());
   }

   //========================== Main Frame Menus =========================================================
   /**
    * Sets {@link MainFrame} reference for {@link RunWorld}.
    *
    * @param mainFrame the MainFrame
    */
   public void setMainFrame(MainFrame mainFrame) {
      this.mainFrame = mainFrame;
   }

   /**
    * @return reference to {@link MainFrame} object in {@link SimulationController}
    */
   public MainFrame getMainFrame() {
      return mainFrame;
   }

   //=========================== OTW Threading ==========================================================
   /**
    * Initalizes and starts out the window thread; called from {@link SimulationWindow}'s addNotify() method
    * to allow OTW thread to start gracefully; uses the Stack Overflow solution shown here:
    * <p>
    * http://stackoverflow.com/questions/26199534/how-to-attach-opengl-display-to-a-jframe-and-dispose-of-it-properly</p>
    */
   public void startOTWThread() {
      outTheWindowThread = new Thread(outTheWindow);
      outTheWindowThread.start();
   }

   /**
    * Stops out the window thread; called from {@link SimulationWindow}'s removeNotify() method
    * to allow OTW thread to stop gracefully; uses the Stack Overflow solution shown here:
    * <p>
    * http://stackoverflow.com/questions/26199534/how-to-attach-opengl-display-to-a-jframe-and-dispose-of-it-properly</p>
    */
   public void stopOTWThread() {
      outTheWindow.requestClose(); // sets running boolean in RunWorld to false to begin the clean up process

      try {
         outTheWindowThread.join();
      } catch (InterruptedException e) {
      }
   }
}
