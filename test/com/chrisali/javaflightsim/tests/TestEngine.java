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

import com.chrisali.javaflightsim.simulation.controls.FlightControlType;
import com.chrisali.javaflightsim.simulation.enviroment.Environment;
import com.chrisali.javaflightsim.simulation.enviroment.EnvironmentParameters;
import com.chrisali.javaflightsim.simulation.propulsion.Engine;
import com.chrisali.javaflightsim.simulation.propulsion.FixedPitchPropEngine;
import com.chrisali.javaflightsim.simulation.setup.IntegrationSetup;
import java.util.EnumMap;
import java.util.Map;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TestEngine extends ApplicationFrame {
   private static final long serialVersionUID = 1L;

   public TestEngine(String testType) {
      super("Engine Test");

      EnumMap<FlightControlType, Double> controls = IntegrationSetup.gatherInitialControls("InitialControls");
      Map<EnvironmentParameters, Double> environmentParameters = Environment.getAndUpdateEnvironmentParams(new double[] { 0, 0, 0 });
      StringBuilder constraint = new StringBuilder();

      Engine defaultEngine = new FixedPitchPropEngine();

      XYSeries thrustXData = new XYSeries("T_x");
      XYSeries thrustYData = new XYSeries("T_y");
      XYSeries thrustZData = new XYSeries("T_z");

      XYSeries momentXData = new XYSeries("M_x");
      XYSeries momentYData = new XYSeries("M_y");
      XYSeries momentZData = new XYSeries("M_z");

      XYSeries fuelFlowData = new XYSeries("Rho");
      XYSeries rpmData = new XYSeries("Speed of Sound");

      XYSeriesCollection thrustSeries = new XYSeriesCollection();
      XYSeriesCollection momentSeries = new XYSeriesCollection();
      XYSeriesCollection fuelFlowSeries = new XYSeriesCollection();
      XYSeriesCollection rpmSeries = new XYSeriesCollection();

      switch (testType) {
         case "Airspeed": {
            constraint.append("Constant Throttle (")
               .append((controls.get(FlightControlType.THROTTLE_1) * 100))
               .append("%) and Altitude (Sea Level)");

            for (double vTrue = 0; vTrue < 500; vTrue += 1) {
               defaultEngine.updateEngineState(controls,
                  environmentParameters,
                  new double[] { vTrue, 0, 0 });

               thrustXData.add(vTrue, defaultEngine.getThrust()[0]);
               thrustYData.add(vTrue, defaultEngine.getThrust()[1]);
               thrustZData.add(vTrue, defaultEngine.getThrust()[2]);

               momentXData.add(vTrue, defaultEngine.getEngineMoment()[0]);
               momentYData.add(vTrue, defaultEngine.getEngineMoment()[1]);
               momentZData.add(vTrue, defaultEngine.getEngineMoment()[2]);

               fuelFlowData.add(vTrue, defaultEngine.getFuelFlow());
               rpmData.add(vTrue, defaultEngine.getRPM());
            }
            break;
         }
         case "Throttle": {
            double vTrue = 0;

            constraint.append("Constant Airspeed (")
               .append(vTrue)
               .append(" ft/sec) and Altitude (Sea Level)");

            for (double throttle = 0; throttle < 1.0; throttle += 0.01) {
               controls.put(FlightControlType.THROTTLE_1, throttle);

               defaultEngine.updateEngineState(controls,
                  environmentParameters,
                  new double[] { vTrue, 0, 0 });

               thrustXData.add(throttle, defaultEngine.getThrust()[0]);
               thrustYData.add(throttle, defaultEngine.getThrust()[1]);
               thrustZData.add(throttle, defaultEngine.getThrust()[2]);

               momentXData.add(throttle, defaultEngine.getEngineMoment()[0]);
               momentYData.add(throttle, defaultEngine.getEngineMoment()[1]);
               momentZData.add(throttle, defaultEngine.getEngineMoment()[2]);

               fuelFlowData.add(throttle, defaultEngine.getFuelFlow());
               rpmData.add(throttle, defaultEngine.getRPM());
            }
            break;
         }
         case "Altitude": {
            double vTrue = 210;

            constraint.append("Constant Throttle (")
               .append((controls.get(FlightControlType.THROTTLE_1) * 100))
               .append("%) and Airspeed (")
               .append(vTrue)
               .append(" ft/sec)");

            for (double altitude = 0; altitude < 20000; altitude += 10) {
               environmentParameters = Environment.getAndUpdateEnvironmentParams(new double[] { 0, 0, altitude });

               defaultEngine.updateEngineState(controls,
                  environmentParameters,
                  new double[] { vTrue, 0, 0 });

               thrustXData.add(altitude, defaultEngine.getThrust()[0]);
               thrustYData.add(altitude, defaultEngine.getThrust()[1]);
               thrustZData.add(altitude, defaultEngine.getThrust()[2]);

               momentXData.add(altitude, defaultEngine.getEngineMoment()[0]);
               momentYData.add(altitude, defaultEngine.getEngineMoment()[1]);
               momentZData.add(altitude, defaultEngine.getEngineMoment()[2]);

               fuelFlowData.add(altitude, defaultEngine.getFuelFlow());
               rpmData.add(altitude, defaultEngine.getRPM());
            }
            break;
         }
      }

      // Add series data to XYSeriesCollections
      thrustSeries.addSeries(thrustXData);
      thrustSeries.addSeries(thrustYData);
      thrustSeries.addSeries(thrustZData);

      momentSeries.addSeries(momentXData);
      momentSeries.addSeries(momentYData);
      momentSeries.addSeries(momentZData);

      fuelFlowSeries.addSeries(fuelFlowData);
      rpmSeries.addSeries(rpmData);

      // Create plots and add XYSeriesCollections to them
      XYPlot thrustPlot = new XYPlot(thrustSeries,
         null,
         new NumberAxis("Thrust [lbf]"),
         new StandardXYItemRenderer());

      XYPlot momentPlot = new XYPlot(momentSeries,
         null,
         new NumberAxis("Moment [lbf*ft]"),
         new StandardXYItemRenderer());

      XYPlot fuelFlowPlot = new XYPlot(fuelFlowSeries,
         null,
         new NumberAxis("Fuel Flow [lbf/hr]"),
         new StandardXYItemRenderer());

      XYPlot rpmPlot = new XYPlot(rpmSeries,
         null,
         new NumberAxis("RPM [1/min]"),
         new StandardXYItemRenderer());

      // Create CombinedDomainXYPlots and add XYPlots to them
      CombinedDomainXYPlot enginePlot = new CombinedDomainXYPlot(new NumberAxis(testType));

      enginePlot.add(thrustPlot, 1);
      enginePlot.add(momentPlot, 1);
      enginePlot.add(fuelFlowPlot, 1);
      enginePlot.add(rpmPlot, 1);

      enginePlot.setOrientation(PlotOrientation.VERTICAL);
      enginePlot.setGap(20);

      // Creates a chart panel to populate AWT window
      ChartPanel engPlotPanel = new ChartPanel(new JFreeChart(("Engine Test with " + testType + " Sweep; " + constraint.toString()),
         JFreeChart.DEFAULT_TITLE_FONT,
         enginePlot,
         true));

      engPlotPanel.setPreferredSize(new java.awt.Dimension(1000, 950));
      setContentPane(engPlotPanel);

      this.pack();
      RefineryUtilities.centerFrameOnScreen(this);
      this.setVisible(true);
   }

   public static void main(String[] args) {
      for (String test : new String[] { "Altitude", "Throttle", "Airspeed" }) {
         new TestEngine(test);
      }
   }
}
