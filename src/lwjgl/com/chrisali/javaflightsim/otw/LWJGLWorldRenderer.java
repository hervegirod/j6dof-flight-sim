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
package com.chrisali.javaflightsim.otw;

import com.chrisali.javaflightsim.controllers.SimulationController;
import com.chrisali.javaflightsim.datatransfer.FlightData;
import com.chrisali.javaflightsim.datatransfer.FlightDataListener;
import com.chrisali.javaflightsim.datatransfer.FlightDataType;
import com.chrisali.javaflightsim.menus.MainFrame;
import com.chrisali.javaflightsim.menus.SimulationWindow;
import com.chrisali.javaflightsim.menus.optionspanel.AudioOptions;
import com.chrisali.javaflightsim.menus.optionspanel.DisplayOptions;
import com.chrisali.javaflightsim.otw.audio.AudioMaster;
import com.chrisali.javaflightsim.otw.audio.SoundCollection;
import com.chrisali.javaflightsim.otw.audio.SoundCollection.SoundCategory;
import com.chrisali.javaflightsim.otw.entities.Camera;
import com.chrisali.javaflightsim.otw.entities.EntityCollections;
import com.chrisali.javaflightsim.otw.entities.Light;
import com.chrisali.javaflightsim.otw.entities.Ownship;
import com.chrisali.javaflightsim.otw.interfaces.font.FontType;
import com.chrisali.javaflightsim.otw.interfaces.font.GUIText;
import com.chrisali.javaflightsim.otw.interfaces.font.TextMaster;
import com.chrisali.javaflightsim.otw.models.TexturedModel;
import com.chrisali.javaflightsim.otw.particles.Cloud;
import com.chrisali.javaflightsim.otw.particles.ParticleMaster;
import com.chrisali.javaflightsim.otw.particles.ParticleTexture;
import com.chrisali.javaflightsim.otw.renderengine.DisplayManager;
import com.chrisali.javaflightsim.otw.renderengine.Loader;
import com.chrisali.javaflightsim.otw.renderengine.MasterRenderer;
import com.chrisali.javaflightsim.otw.renderengine.OBJLoader;
import com.chrisali.javaflightsim.otw.terrain.Terrain;
import com.chrisali.javaflightsim.otw.terrain.TerrainCollection;
import com.chrisali.javaflightsim.otw.textures.ModelTexture;
import com.chrisali.javaflightsim.rendering.TerrainProvider;
import com.chrisali.javaflightsim.rendering.WorldRenderer;
import com.chrisali.javaflightsim.simulation.aircraft.AircraftBuilder;
import com.chrisali.javaflightsim.simulation.setup.InitialConditions;
import com.chrisali.javaflightsim.simulation.setup.Options;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Runner class for out the window display for Java Flight Sim. It utilizes LWJGL to create a 3D world in OpenGL.
 * The display runs in a separate thread that receives data from {@link FlightData} via {@link FlightDataListener}
 *
 * @author Christopher Ali
 *
 */
public class LWJGLWorldRenderer implements WorldRenderer, TerrainProvider {
   private Loader loader;
   private MasterRenderer masterRenderer;
   private List<Light> lights;

   private SimulationController controller;

   // Sound Fields
   private Map<SoundCategory, Double> soundValues = new EnumMap<>(SoundCategory.class);
   private boolean recordPrev = true; // Used in FlightDataListener to record soundValues data to PREV_STEP_* enums

   // Collections for in-game objects
   private TerrainCollection terrainCollection;
   private EntityCollections entities;

   // Ownship is the "player" that moves around the world based on data received from FlightData
   private Ownship ownship;
   private Vector3f ownshipPosition;
   private Vector3f ownshipRotation;
   private Camera camera;
   private Map<String, GUIText> texts = new HashMap<>();
   private boolean running = false;

   /**
    * Sets up OTW display with {@link DisplayOptions} and {@link AudioOptions}, as well as a link to
    * {@link AircraftBuilder} to determine if multiple engines in aircraft. If {@link SimulationController}
    * object specified, display will embed itself within {@link SimulationWindow} in {@link MainFrame}
    *
    * @param controller
    */
   public LWJGLWorldRenderer(SimulationController controller) {
      this.controller = controller;
   }

   @Override
   public void start() {

      //=================================== Set Up ==========================================================
      // Initializes display window depending on presence of SimulationController's MainFrame object,
      // set in RunJavaFlightSimulator
      if (controller.getMainFrame() != null) {
         DisplayManager.createDisplay(controller.getMainFrame().getSimulationWindow());
      } else {
         DisplayManager.createDisplay();
      }

      loader = new Loader();

      // Sets up renderer with fog and sky config
      masterRenderer = new MasterRenderer();
      MasterRenderer.setSkyColor(new Vector3f(0.70f, 0.90f, 1.0f));
      MasterRenderer.setFogDensity(0.0005f);
      MasterRenderer.setFogGradient(3.5f);

      // Initialize sounds and position of listener
      AudioMaster.init();
      AudioMaster.setListenerData(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

      // Load particles and on-screen text
      ParticleMaster.init(loader, masterRenderer.getProjectionMatrix());
      TextMaster.init(loader);

      // Load all entities (lights, entities, particles, etc)
      loadAssets();

      running = true;

      //=============================== Main Loop ==========================================================
      while (!Display.isCloseRequested() && running) {

         //--------- Movement ----------------
         camera.move(ownshipPosition, ownshipRotation.x, ownshipRotation.y, ownshipRotation.z);
         ownship.move(ownshipPosition, ownshipRotation.x, ownshipRotation.y, ownshipRotation.z);

         //--------- Particles ---------------
         ParticleMaster.update(camera);

         //--------- Audio--------------------
         SoundCollection.update(soundValues, controller);

         //----------- UI --------------------
         texts.get("Paused").setTextString(controller.getSimulationOptions()
            .contains(Options.PAUSED)
            ? "PAUSED" : "");

         //------ Render Everything -----------
         masterRenderer.renderWholeScene(entities, terrainCollection.getTerrainTree(),
            lights, camera, new Vector4f(0, 1, 0, 0));
         ParticleMaster.renderParticles(camera);
         TextMaster.render(texts);

         DisplayManager.updateDisplay();
      }

      running = false;

      //================================ Clean Up ==========================================================
      AudioMaster.cleanUp();
      ParticleMaster.cleanUp();
      TextMaster.cleanUp();
      masterRenderer.cleanUp();
      loader.cleanUp();

      DisplayManager.closeDisplay();
   }

   /**
    * Initializes and generates all assets needed to render lights, entities, particles terrain and text
    */
   private void loadAssets() {

      //==================================== Sun ===========================================================
      lights = new ArrayList<>();
      lights.add(new Light(new Vector3f(20000, 40000, 20000), new Vector3f(0.95f, 0.95f, 0.95f)));

      //================================= Entities ==========================================================
      entities = new EntityCollections(lights, loader);

      //================================= Ownship ===========================================================
      // Model used for aircraft; scale set to 0 to be invisible for now
      TexturedModel bunny = new TexturedModel(OBJLoader.loadObjModel("bunny", "Entities", loader),
         new ModelTexture(loader.loadTexture("bunny", "Entities")));
      // Initial position of ownship
      Map<InitialConditions, Double> initialConditions = controller.getInitialConditions();
      ownshipPosition = new Vector3f((float) initialConditions.get(InitialConditions.INITN).doubleValue() / 15,
         (float) initialConditions.get(InitialConditions.INITD).doubleValue() / 15,
         (float) initialConditions.get(InitialConditions.INITE).doubleValue() / 15); //(800, 150, 800)
      ownshipRotation = new Vector3f((float) Math.toDegrees(initialConditions.get(InitialConditions.INITPHI)),
         (float) Math.toDegrees(initialConditions.get(InitialConditions.INITTHETA)),
         (float) Math.toDegrees(initialConditions.get(InitialConditions.INITPSI)) - 270); // (0, 0, 135)
      ownship = new Ownship(bunny, ownshipPosition, ownshipRotation.z, ownshipRotation.z, ownshipRotation.x, 0.000f);

      entities.addToStaticEntities(ownship);

      // Camera tied to ownship as first person view
      camera = new Camera(ownship);
      camera.setChaseView(false);
      camera.setPilotPosition(new Vector3f(0, 0, 0));

      //================================= Terrain ==========================================================
      terrainCollection = new TerrainCollection(10, loader, ownship);
      entities.setTerrainTree(terrainCollection.getTerrainTree());

      //=============================== Particles ==========================================================
      ParticleTexture clouds = new ParticleTexture(loader.loadTexture("clouds", "Particles"), 4, true);

      // Generates clouds at random positions along terrain map
      Random random = new Random();
      for (int i = 0; i < 2000; i++) {
         new Cloud(clouds, new Vector3f(random.nextInt(800 * 10), 300, i * 10), new Vector3f(0, 0, 0), 0, 200);
      }

      //=============================== Interface ==========================================================
      // Generates font and on screen text
      FontType font = new FontType(loader.loadTexture("ubuntu", "Fonts"), new File("Resources\\Fonts\\ubuntu.fnt"));
      texts.put("FlightData", new GUIText("", 0.85f, font, new Vector2f(0, 0), 1f, true));
      texts.put("Paused", new GUIText("PAUSED", 1.15f, font, new Vector2f(0.5f, 0.5f), 1f, false, new Vector3f(1, 0, 0)));

      //==================================== Audio =========================================================
      SoundCollection.initializeSounds(controller);
   }

   /**
    * Return the terrain height.
    *
    * @return Height of terrain at the ownship's current position
    */
   @Override
   public synchronized float getTerrainHeight() {
      if (running) {
         TreeMap<String, Terrain> terrainTree = terrainCollection.getTerrainTree();
         Vector3f position = ownship.getPosition();
         // Terrain object ownship is currently on
         Terrain currentTerrain = Terrain.getCurrentTerrain(terrainTree, position.x, position.z);
         // If outside world bounds, return 0 as terrain height
         return (currentTerrain == null) ? 0.0f : currentTerrain.getTerrainHeight(position.x, position.z);
      } else {
         return 0.0f;
      }
   }

   //=============================== Synchronization ======================================================
   /**
    * Return true if the WorldRenderer is currently running.
    *
    * @return true if the WorldRenderer is currently running
    */
   @Override
   public synchronized boolean isRunning() {
      return running;
   }

   /**
    * Request the WorldRenderer to close.
    */
   @Override
   public synchronized void requestClose() {
      this.running = false;
   }

   //===================================== Text ============================================================
   /**
    * Prepares a string of flight data that is output on the OTW using the {@link GUIText} object
    *
    * @param receivedFlightData
    * @return string displaying flight data output
    */
   private String setTextInfo(Map<FlightDataType, Double> receivedFlightData) {
      DecimalFormat df4 = new DecimalFormat("0.0000");
      DecimalFormat df2 = new DecimalFormat("0.00");
      DecimalFormat df0 = new DecimalFormat("0");

      StringBuffer sb = new StringBuffer();
      sb.append("AIRSPEED: ").append(df0.format(receivedFlightData.get(FlightDataType.IAS))).append(" KIAS | ")
         .append("HEADING: ").append(df0.format(receivedFlightData.get(FlightDataType.HEADING))).append(" DEG | ")
         .append("ALTITUDE: ").append(df0.format(receivedFlightData.get(FlightDataType.ALTITUDE))).append(" FT | ")
         .append("LATITUDE: ").append(df4.format(receivedFlightData.get(FlightDataType.LATITUDE))).append(" DEG | ")
         .append("LONGITUDE: ").append(df4.format(receivedFlightData.get(FlightDataType.LONGITUDE))).append(" DEG | ")
         .append("G-FORCE: ").append(df2.format(receivedFlightData.get(FlightDataType.GFORCE))).append(" G ");

      return sb.toString();
   }

   @Override
   public void setPosition(double x, double y, double z) {
      // Scale distances from simulation to OTW
      ownshipPosition.x = (float) (x / 15);
      ownshipPosition.y = (float) (y / 15);
      ownshipPosition.z = (float) (z / 15);
   }

   @Override
   public void onFlightDataReceived(FlightData flightData) {

      Map<FlightDataType, Double> receivedFlightData = flightData.getFlightData();

      if (!receivedFlightData.containsValue(null) && (ownshipPosition != null || ownshipRotation != null)
         && receivedFlightData != null) {

         // Convert right-handed coordinates from simulation to left-handed coordinates of OTW
         ownshipRotation.x = (float) -(receivedFlightData.get(FlightDataType.ROLL));
         ownshipRotation.y = (float) -(receivedFlightData.get(FlightDataType.PITCH));
         ownshipRotation.z = (float) (receivedFlightData.get(FlightDataType.HEADING) - 270);

         // Set values for each sound in the simulation that depends on flight data
         soundValues.put(SoundCategory.RPM_1, receivedFlightData.get(FlightDataType.RPM_1));
         soundValues.put(SoundCategory.RPM_2, receivedFlightData.get(FlightDataType.RPM_2));
         soundValues.put(SoundCategory.RPM_3, receivedFlightData.get(FlightDataType.RPM_3));
         soundValues.put(SoundCategory.RPM_4, receivedFlightData.get(FlightDataType.RPM_4));
         soundValues.put(SoundCategory.WIND, receivedFlightData.get(FlightDataType.TAS));
         soundValues.put(SoundCategory.FLAPS, receivedFlightData.get(FlightDataType.FLAPS));
         soundValues.put(SoundCategory.GEAR, receivedFlightData.get(FlightDataType.GEAR));
         soundValues.put(SoundCategory.STALL_HORN, receivedFlightData.get(FlightDataType.AOA));

         // Record flight data into text string to display on OTW screen
         if (!controller.getSimulationOptions().contains(Options.INSTRUMENT_PANEL)) {
            texts.get("FlightData").setTextString(setTextInfo(receivedFlightData));
         }

         // Record value every other step to ensure a difference between previous and current values; used to
         // trigger flaps and gear sounds
         if (recordPrev) {
            soundValues.put(SoundCategory.PREV_STEP_FLAPS, receivedFlightData.get(FlightDataType.FLAPS));
            soundValues.put(SoundCategory.PREV_STEP_GEAR, receivedFlightData.get(FlightDataType.GEAR));
         }
         recordPrev ^= true;
      }
   }
}
