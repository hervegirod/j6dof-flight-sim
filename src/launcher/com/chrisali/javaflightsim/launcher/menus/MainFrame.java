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
package com.chrisali.javaflightsim.launcher.menus;

import com.chrisali.javaflightsim.gui.SimulationWindow;
import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.gui.GUIManager;
import com.chrisali.javaflightsim.instrumentpanel.ClosePanelListener;
import com.chrisali.javaflightsim.instrumentpanel.InstrumentPanel;
import com.chrisali.javaflightsim.launcher.menus.aircraftpanel.AircraftConfigurationListener;
import com.chrisali.javaflightsim.launcher.menus.aircraftpanel.AircraftPanel;
import com.chrisali.javaflightsim.launcher.menus.aircraftpanel.WeightConfiguredListener;
import com.chrisali.javaflightsim.launcher.menus.initialconditionspanel.InitialConditionsConfigurationListener;
import com.chrisali.javaflightsim.launcher.menus.initialconditionspanel.InitialConditionsPanel;
import com.chrisali.javaflightsim.gui.AudioOptions;
import com.chrisali.javaflightsim.gui.DisplayOptions;
import com.chrisali.javaflightsim.launcher.menus.optionspanel.OptionsConfigurationListener;
import com.chrisali.javaflightsim.launcher.menus.optionspanel.OptionsPanel;
import com.chrisali.javaflightsim.rendering.RunWorld;
import com.chrisali.javaflightsim.simulation.setup.IntegratorConfig;
import com.chrisali.javaflightsim.simulation.setup.Options;
import com.chrisali.javaflightsim.utilities.FileUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;
import java.util.EnumSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main Swing class that contains the main menus to configure the simulation and simulation window,
 * which renders the Out The Window display and instrument panel.
 *
 * @author Christopher Ali
 */
public class MainFrame extends JFrame implements GUIManager {
   private static final long serialVersionUID = -1803264930661591606L;
   private SimulationController simulationController;
   private ButtonPanel buttonPanel;
   private AircraftPanel aircraftPanel;
   private OptionsPanel optionsPanel;
   private InitialConditionsPanel initialConditionsPanel;
   private SimulationWindow simulationWindow;
   private JPanel cardPanel;
   private CardLayout cardLayout;

   /**
    * Constructor, which takes a {@link SimulationController} reference to gain access to methods to
    * configure the simulation
    *
    * @param controller
    */
   public MainFrame(SimulationController controller) {
      super("Java Flight Sim");

      simulationController = controller;

      setLayout(new BorderLayout());
      Dimension dims = new Dimension(200, 400);

      //---------------------------- Card Panel --------------------------------------------------
      cardLayout = new ResizingCardLayout();
      cardPanel = new JPanel();
      cardPanel.setLayout(cardLayout);
      cardPanel.setVisible(false);
      add(cardPanel, BorderLayout.EAST);

      //------------------------- Simulation Window ----------------------------------------------
      initSimulationWindow();

      //-------------------------- Aircraft Panel ------------------------------------------------
      aircraftPanel = new AircraftPanel(this);
      aircraftPanel.setAircraftConfigurationListener(new AircraftConfigurationListener() {
         @Override
         public void aircraftConfigured(String aircraftName) {
            buttonPanel.setAircraftLabel(aircraftName);
            simulationController.updateAircraft(aircraftName);

            setSize(dims);
            cardPanel.setVisible(false);
         }
      });
      aircraftPanel.setWeightConfiguredListener(new WeightConfiguredListener() {
         @Override
         public void weightConfigured(String aircraftName, double fuelWeight, double payloadWeight) {
            simulationController.updateMassProperties(aircraftName, fuelWeight, payloadWeight);
         }
      });
      aircraftPanel.setCancelButtonListener(new CancelButtonListener() {
         @Override
         public void cancelButtonClicked() {
            setSize(dims);
            cardPanel.setVisible(false);
         }
      });
      cardPanel.add(aircraftPanel, "aircraft");

      //--------------------------- Options Panel ------------------------------------------------
      optionsPanel = new OptionsPanel();
      optionsPanel.setOptionsConfigurationListener(new OptionsConfigurationListener() {
         @Override
         public void simulationOptionsConfigured(EnumSet<Options> options, int stepSize,
            EnumMap<DisplayOptions, Integer> displayOptions,
            EnumMap<AudioOptions, Float> audioOptions) {
            buttonPanel.setOptionsLabel(options, stepSize);
            simulationController.updateIntegratorConfig(stepSize);
            simulationController.updateOptions(options, displayOptions, audioOptions);

            setSize(dims);
            cardPanel.setVisible(false);
         }
      });
      optionsPanel.setCancelButtonListener(new CancelButtonListener() {
         @Override
         public void cancelButtonClicked() {
            setSize(dims);
            cardPanel.setVisible(false);
         }
      });
      cardPanel.add(optionsPanel, "options");

      //-------------------- Initial Conditions Panel --------------------------------------------
      initialConditionsPanel = new InitialConditionsPanel();
      initialConditionsPanel.setInitialConditionsPanel(controller.getInitialConditions());
      initialConditionsPanel.setInitialConditionsConfigurationListener(new InitialConditionsConfigurationListener() {
         @Override
         public void initialConditionsConfigured(double[] coordinates, double heading, double altitude, double airspeed) {
            buttonPanel.setInitialConditionsLabel(coordinates, heading, altitude, airspeed);
            simulationController.updateInitialConditions(coordinates, heading, altitude, airspeed);

            setSize(dims);
            cardPanel.setVisible(false);
         }
      });
      initialConditionsPanel.setCancelButtonListener(new CancelButtonListener() {
         @Override
         public void cancelButtonClicked() {
            setSize(dims);
            cardPanel.setVisible(false);
         }
      });
      cardPanel.add(initialConditionsPanel, "initialConditions");

      //-------------------------- Button Panel --------------------------------------------------
      buttonPanel = new ButtonPanel();
      buttonPanel.setAircraftButtonListener(new AircraftButtonListener() {
         @Override
         public void buttonEventOccurred() {
            setSize((dims.width + aircraftPanel.getPreferredSize().width), dims.height);
            cardPanel.setVisible(true);
            cardLayout.show(cardPanel, "aircraft");
         }
      });
      buttonPanel.setInitialConditionsButtonListener(new InitialConditionsButtonListener() {
         @Override
         public void buttonEventOccurred() {
            setSize((dims.width + initialConditionsPanel.getPreferredSize().width), dims.height);
            cardPanel.setVisible(true);
            cardLayout.show(cardPanel, "initialConditions");
         }
      });
      buttonPanel.setOptionsButtonListener(new OptionsButtonListener() {
         @Override
         public void buttonEventOccurred() {
            setSize((dims.width + optionsPanel.getPreferredSize().width), dims.height);
            cardPanel.setVisible(true);
            cardLayout.show(cardPanel, "options");
         }
      });
      buttonPanel.setStartSimulationButtonListener(new StartSimulationButtonListener() {
         @Override
         public void buttonEventOccurred() {
            setSize(dims);
            cardPanel.setVisible(false);

            simulationController.startSimulation();
            MainFrame.this.setVisible(simulationController.getSimulationOptions().contains(Options.ANALYSIS_MODE));
            simulationWindow.setVisible(!simulationController.getSimulationOptions().contains(Options.ANALYSIS_MODE));
         }
      });
      add(buttonPanel, BorderLayout.CENTER);

      //============================ Miscellaneous ===============================================
      setOptionsAndText();

      //========================== Window Settings ===============================================
      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            int closeDialog = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you wish to quit?",
               "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (closeDialog == JOptionPane.YES_OPTION) {
               System.gc();
               System.exit(0);
            }
         }
      });

      setSize(dims);
      setResizable(false);

      setVisible(true);
      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
   }

   /**
    * Sets all options and text on panels by calling methods in {@link SimulationController} to
    * parse setup files and get EnumMap values
    */
   private void setOptionsAndText() {
      try {
         simulationController.updateOptions(FileUtilities.parseSimulationSetup(),
            FileUtilities.parseDisplaySetup(),
            FileUtilities.parseAudioSetup());
         simulationController.updateAircraft(FileUtilities.parseSimulationSetupForAircraft());
      } catch (IllegalArgumentException e) {
         JOptionPane.showMessageDialog(this, "Unable to read SimulationSetup.txt!",
            "Error Reading File", JOptionPane.ERROR_MESSAGE);
      }

      int stepSize = (int) (1 / simulationController.getIntegratorConfig().get(IntegratorConfig.DT));
      String aircraftName = simulationController.getAircraftBuilder().getAircraft().getName();

      buttonPanel.setOptionsLabel(simulationController.getSimulationOptions(), stepSize);
      buttonPanel.setAircraftLabel(aircraftName);

      aircraftPanel.setAircraftPanel(aircraftName);
      optionsPanel.setAllOptions(simulationController.getSimulationOptions(), stepSize,
         simulationController.getDisplayOptions(),
         simulationController.getAudioOptions());
   }

   //=============================== Simulation Window ==============================================
   /**
    * (Re)initializes simulationWindow object so that instrument panel and OTW view are scaled correctly depending
    * on if the instrument panel is shown or not
    */
   @Override
   public void initSimulationWindow() {
      simulationWindow = new SimulationWindow(simulationController);
      simulationWindow.setClosePanelListener(new ClosePanelListener() {
         @Override
         public void panelWindowClosed() {
            simulationController.stopSimulation();
            simulationWindow.setVisible(false);
            MainFrame.this.setVisible(true);
         }
      });
   }

   /**
    * Return the SimulationWindow object.
    *
    * @return {@link SimulationWindow} object for {@link RunWorld}.
    */
   @Override
   public SimulationWindow getSimulationWindow() {
      return simulationWindow;
   }

   @Override
   public void disposeSimulationWindow() {
      simulationWindow.dispose();
      this.setVisible(true);
   }   

   @Override
   public void addFlightDataListeners(FlightData flightData) {
      flightData.addFlightDataListener(getInstrumentPanel());
   }

   /**
    * @return {@link InstrumentPanel} object for {@link SimulationController} to set a
    * {@link FlightDataListener} to when {@link SimulationController#startSimulation()} is called
    */
   public InstrumentPanel getInstrumentPanel() {
      return simulationWindow.getInstrumentPanel();
   }
}
