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
package com.chrisali.javaflightsim;

import static org.junit.Assert.assertFalse;
import java.io.File;
import org.jdepend.model.JarDependencies;
import org.jdepend.utils.DependencyPrinter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test jar dependencies.
 *
 * @since 0.2
 */
public class DependenciesTest {
   private static final String CORE = "j6dof-core.jar";
   private static final String LWJGL = "j6dof-lwjgl.jar";
   private static final String LAUNCHER = "j6dof-launcher.jar";
   private static final String LWJGL_LIB = "lwjgl.jar";
   private static final String LWJGLUTIL_LIB = "lwjgl_util.jar";
   private static final String PLOTS = "j6dof-plots.jar";
   private static final String PLOTS_LIB = "jfreechart-1.0.19.jar";
   private static final String PLOTS_LIB2 = "jcommon-1.0.23.jar";
   private static final String INSTRUMENTS = "j6dof-instruments.jar";
   private static final String INSTRUMENTS_LIB1 = "steelseries.jar";
   private static final String INSTRUMENTS_LIB2 = "trident-7.2.1.jar";
   private static final String CONTROLS = "j6dof-controls.jar";
   private static final String CONTROLS_LIB = "jinput.jar";
   private static JarDependencies coreDepend = null;
   private static JarDependencies lwjglDepend = null;
   private static JarDependencies lwjglLib1Depend = null;
   private static JarDependencies lwjglLib2Depend = null;
   private static JarDependencies plotsDepend = null;
   private static JarDependencies plotsLib1Depend = null;
   private static JarDependencies plotsLib2Depend = null;
   private static JarDependencies launcherDepend = null;
   private static JarDependencies instrumentsDepend = null;
   private static JarDependencies instrumentsLib1Depend = null;
   private static JarDependencies instrumentsLib2Depend = null;
   private static JarDependencies controlsDepend = null;
   private static JarDependencies controlsLibDepend = null;

   public DependenciesTest() {
   }

   @BeforeClass
   public static void setUpClass() throws Exception {
      // get the path of the dist directory
      File userDir = new File(System.getProperty("user.dir"));
      File dist = new File(userDir, "dist");

      // get the path of the lib directory
      File lib = new File(userDir, "lib");

      // core library
      File jarFile = new File(dist, CORE);
      coreDepend = new JarDependencies();
      coreDepend.setFile(jarFile);

      // LWJGL
      jarFile = new File(dist, LWJGL);
      lwjglDepend = new JarDependencies();
      lwjglDepend.setFile(jarFile);
      jarFile = new File(lib, LWJGL_LIB);
      lwjglLib1Depend = new JarDependencies();
      lwjglLib1Depend.setFile(jarFile);
      jarFile = new File(lib, LWJGLUTIL_LIB);
      lwjglLib2Depend = new JarDependencies();
      lwjglLib2Depend.setFile(jarFile);

      // plotting
      jarFile = new File(dist, PLOTS);
      plotsDepend = new JarDependencies();
      plotsDepend.setFile(jarFile);
      jarFile = new File(lib, PLOTS_LIB);
      plotsLib1Depend = new JarDependencies();
      plotsLib1Depend.setFile(jarFile);
      jarFile = new File(lib, PLOTS_LIB2);
      plotsLib2Depend = new JarDependencies();
      plotsLib2Depend.setFile(jarFile);

      // launcher
      jarFile = new File(dist, LAUNCHER);
      launcherDepend = new JarDependencies();
      launcherDepend.setFile(jarFile);

      // instruments
      jarFile = new File(dist, INSTRUMENTS);
      instrumentsDepend = new JarDependencies();
      instrumentsDepend.setFile(jarFile);
      jarFile = new File(dist, INSTRUMENTS_LIB1);
      instrumentsLib1Depend = new JarDependencies();
      instrumentsLib1Depend.setFile(jarFile);
      jarFile = new File(lib, INSTRUMENTS_LIB2);
      instrumentsLib2Depend = new JarDependencies();
      instrumentsLib2Depend.setFile(jarFile);

      // controls
      jarFile = new File(dist, CONTROLS);
      controlsDepend = new JarDependencies();
      controlsDepend.setFile(jarFile);
      jarFile = new File(lib, CONTROLS_LIB);
      controlsLibDepend = new JarDependencies();
      controlsLibDepend.setFile(jarFile);
   }

   @AfterClass
   public static void tearDownClass() {
      coreDepend = null;
      lwjglDepend = null;
      lwjglLib1Depend = null;
      lwjglLib2Depend = null;
      plotsDepend = null;
      plotsLib1Depend = null;
      plotsLib2Depend = null;
      launcherDepend = null;
      instrumentsDepend = null;
      instrumentsLib1Depend = null;
      instrumentsLib2Depend = null;
      controlsDepend = null;
      controlsLibDepend = null;
   }

   @Before
   public void setUp() {
   }

   @After
   public void tearDown() {
   }

   /**
    * Test that the core jar file don't depend on the lwjgl or the lwjgl library.
    */
   @Test
   public void testDependenciesOnLWJGL() {
      System.out.println("DependenciesTest : testDependenciesOnLWJGL");
      boolean isDepending = coreDepend.isDependingOn(lwjglDepend);
      DependencyPrinter.printDependencies(coreDepend, lwjglDepend);
      assertFalse(CORE + " must not depend on " + LWJGL, isDepending);

      isDepending = coreDepend.isDependingOn(lwjglLib1Depend);
      DependencyPrinter.printDependencies(coreDepend, lwjglLib1Depend);
      assertFalse(CORE + " must not depend on " + LWJGL_LIB, isDepending);

      isDepending = coreDepend.isDependingOn(lwjglLib2Depend);
      DependencyPrinter.printDependencies(coreDepend, lwjglLib2Depend);
      assertFalse(CORE + " must not depend on " + LWJGLUTIL_LIB, isDepending);
   }

   /**
    * Test that the core jar file don't depend on the plotting library or jFreecharts.
    */
   @Test
   public void testDependenciesOnJFreeChart() {
      System.out.println("DependenciesTest : testDependenciesOnJFreeChart");
      boolean isDepending = coreDepend.isDependingOn(plotsDepend);
      DependencyPrinter.printDependencies(coreDepend, plotsDepend);
      assertFalse(CORE + " must not depend on " + PLOTS, isDepending);

      isDepending = coreDepend.isDependingOn(plotsLib1Depend);
      DependencyPrinter.printDependencies(coreDepend, plotsLib1Depend);
      assertFalse(CORE + " must not depend on " + PLOTS_LIB, isDepending);

      isDepending = coreDepend.isDependingOn(plotsLib2Depend);
      DependencyPrinter.printDependencies(coreDepend, plotsLib2Depend);
      assertFalse(CORE + " must not depend on " + PLOTS_LIB2, isDepending);
   }

   /**
    * Test that the core jar file don't depend on the launcher.
    */
   @Test
   public void testDependenciesOnLauncher() {
      System.out.println("DependenciesTest : testDependenciesOnLauncher");
      boolean isDepending = coreDepend.isDependingOn(launcherDepend);
      DependencyPrinter.printDependencies(coreDepend, launcherDepend);
      assertFalse(CORE + " must not depend on " + LAUNCHER, isDepending);
   }

   /**
    * Test that the core jar file don't depend on the insreuments library.
    */
   @Test
   public void testDependenciesOnInstruments() {
      System.out.println("DependenciesTest : testDependenciesOnInstruments");
      boolean isDepending = coreDepend.isDependingOn(instrumentsDepend);
      DependencyPrinter.printDependencies(coreDepend, instrumentsDepend);
      assertFalse(CORE + " must not depend on " + INSTRUMENTS, isDepending);

      isDepending = coreDepend.isDependingOn(instrumentsLib1Depend);
      DependencyPrinter.printDependencies(coreDepend, instrumentsLib1Depend);
      assertFalse(CORE + " must not depend on " + INSTRUMENTS_LIB1, isDepending);

      isDepending = coreDepend.isDependingOn(instrumentsLib2Depend);
      DependencyPrinter.printDependencies(coreDepend, instrumentsLib2Depend);
      assertFalse(CORE + " must not depend on " + INSTRUMENTS_LIB2, isDepending);
   }

   /**
    * Test that the core jar file don't depend on the insreuments library.
    */
   @Test
   public void testDependenciesOnControls() {
      System.out.println("DependenciesTest : testDependenciesOnControls");
      boolean isDepending = coreDepend.isDependingOn(controlsDepend);
      DependencyPrinter.printDependencies(coreDepend, controlsDepend);
      assertFalse(CORE + " must not depend on " + CONTROLS, isDepending);

      isDepending = coreDepend.isDependingOn(controlsLibDepend);
      DependencyPrinter.printDependencies(coreDepend, controlsLibDepend);
      assertFalse(CORE + " must not depend on " + CONTROLS_LIB, isDepending);
   }
}
