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
package com.chrisali.javaflightsim.launcher.menus.optionspanel;

import com.chrisali.javaflightsim.gui.AudioOptions;
import com.chrisali.javaflightsim.gui.DisplayOptions;
import com.chrisali.javaflightsim.launcher.menus.CancelButtonListener;
import com.chrisali.javaflightsim.simulation.setup.Options;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.EnumSet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class OptionsPanel extends JPanel {

   private static final long serialVersionUID = -8867495044721208756L;

   private SimulationOptionsTab simulationOptionsTab;
   private DisplayOptionsTab displayOptionsTab;
   private AudioOptionsTab audioOptionsTab;
   private JButton okButton;
   private JButton cancelButton;

   private OptionsConfigurationListener optionsConfigurationListener;
   private CancelButtonListener cancelButtonListener;

   private int stepSize;

   public OptionsPanel() {

      //-------------------- Panels ---------------------------
      JTabbedPane optionsTabs = new JTabbedPane();
      JPanel buttonPanel = new JPanel();

      buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

      setLayout(new BorderLayout());
      add(optionsTabs, BorderLayout.CENTER);
      add(buttonPanel, BorderLayout.SOUTH);

      //------------- Simulation Options Tab -------------------
      simulationOptionsTab = new SimulationOptionsTab();
      simulationOptionsTab.setStepSizeValueChangedListener(new StepSizeValueChangedListener() {
         @Override
         public void valueChanged(int newStepValue) {
            stepSize = newStepValue;
         }
      });
      optionsTabs.add(simulationOptionsTab, "Simulation");

      //------------- Display Options Tab -------------------
      displayOptionsTab = new DisplayOptionsTab();
      optionsTabs.add(displayOptionsTab, "Display");

      //-------------- Audio Options Tab ---------------------
      audioOptionsTab = new AudioOptionsTab();
      optionsTabs.add(audioOptionsTab, "Audio");

      //----------------- OK Button ----------------------------
      okButton = new JButton("OK");
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!simulationOptionsTab.getSimulationOptions().contains(Options.ANALYSIS_MODE)) {
               simulationOptionsTab.getSimulationOptions().add(Options.UNLIMITED_FLIGHT);
            }

            if (optionsConfigurationListener != null) {
               optionsConfigurationListener.simulationOptionsConfigured(simulationOptionsTab.getSimulationOptions(), stepSize,
                       displayOptionsTab.getDisplayOptions(),
                       audioOptionsTab.getAudioOptions());
            }
         }
      });
      buttonPanel.add(okButton);

      //------------------- Cancel Button ------------------------
      cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (cancelButtonListener != null) {
               cancelButtonListener.cancelButtonClicked();
            }
         }
      });
      buttonPanel.add(cancelButton);
      okButton.setPreferredSize(cancelButton.getPreferredSize());

      //========================== Window Settings ===============================================
      Dimension dims = new Dimension(400, 400);
      setSize(dims);
      setPreferredSize(dims);
   }

   public void setAllOptions(EnumSet<Options> simulationOptions, int stepSize,
           EnumMap<DisplayOptions, Integer> displayOptions,
           EnumMap<AudioOptions, Float> audioOptions) {
      this.stepSize = stepSize;
      simulationOptionsTab.setOptionsTab(simulationOptions, stepSize);
      displayOptionsTab.setOptionsTab(displayOptions);
      audioOptionsTab.setOptionsTab(audioOptions);

   }

   public void setCancelButtonListener(CancelButtonListener cancelButtonListener) {
      this.cancelButtonListener = cancelButtonListener;
   }

   public void setOptionsConfigurationListener(OptionsConfigurationListener optionsConfigurationListener) {
      this.optionsConfigurationListener = optionsConfigurationListener;
   }
}
