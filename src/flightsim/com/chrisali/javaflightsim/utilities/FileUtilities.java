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
package com.chrisali.javaflightsim.utilities;

import com.chrisali.javaflightsim.menus.optionspanel.AudioOptions;
import com.chrisali.javaflightsim.menus.optionspanel.DisplayOptions;
import com.chrisali.javaflightsim.simulation.aircraft.MassProperties;
import com.chrisali.javaflightsim.simulation.integration.SimOuts;
import com.chrisali.javaflightsim.simulation.setup.Options;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains various static methods for reading and parsing text files into lists.
 */
public class FileUtilities {

   //===================================================================================================
   //										File Reading
   //===================================================================================================
   /**
    * Splits a config file called "fileContents".txt located in the folder
    * specified by filePath whose general syntax on each line is:
    * <code>*parameter name* = *double value*</code>
    * into an ArrayList of string arrays resembling:
    * <code>{*parameter name*,*double value*}</code>.
    *
    * @param aircraftName the aircraft name
    * @param filePath the file path
    * @param fileContents the file content
    * @return An ArrayList of String arrays of length 2
    */
   public static ArrayList<String[]> readFileAndSplit(String aircraftName, String filePath, String fileContents) {
      StringBuilder sb = new StringBuilder();
      sb.append(filePath).append(aircraftName).append("\\").append(fileContents).append(".txt");
      ArrayList<String[]> readAndSplit = new ArrayList<>();
      String readLine = null;

      try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {
         while ((readLine = br.readLine()) != null) {
            readAndSplit.add(readLine.split(" = "));
         }
      } catch (FileNotFoundException e) {
         System.err.println("Could not find: " + fileContents + ".txt!");
      } catch (IOException e) {
         System.err.println("Could not read: " + fileContents + ".txt!");
      } catch (NullPointerException e) {
         System.err.println("Bad reference when reading: " + fileContents + ".txt!");
      } catch (NumberFormatException e) {
         System.err.println("Error parsing data from " + fileContents + ".txt!");
      }

      return readAndSplit;
   }

   /**
    * Splits a config file called "fileName".txt located in the folder
    * specified by filePath whose general syntax on each line is:
    * <code>*parameter name* = *double value*</code>
    * into an ArrayList of string arrays resembling:
    * <code>{*parameter name*,*double value*}</code>.
    *
    * @param fileName the file name
    * @param filePath the file path
    * @return An ArrayList of String arrays of length 2
    */
   public static ArrayList<String[]> readFileAndSplit(String fileName, String filePath) {
      StringBuilder sb = new StringBuilder();
      sb.append(filePath).append(fileName).append(".txt");
      ArrayList<String[]> readAndSplit = new ArrayList<>();
      String readLine = null;

      try (BufferedReader br = new BufferedReader(new FileReader(sb.toString()))) {
         while ((readLine = br.readLine()) != null) {
            readAndSplit.add(readLine.split(" = "));
         }
      } catch (FileNotFoundException e) {
         System.err.println("Could not find: " + fileName + ".txt!");
      } catch (IOException e) {
         System.err.println("Could not read: " + fileName + ".txt!");
      } catch (NullPointerException e) {
         System.err.println("Bad reference when reading: " + fileName + ".txt!");
      } catch (NumberFormatException e) {
         System.err.println("Error parsing data from " + fileName + ".txt!");
      }

      return readAndSplit;
   }

   /**
    * Parses a config file called SimulationSetup.txt located in .\\SimConfig\\
    * where each line is written as <code>"*parameter* = *value*\n"</code>
    * and returns an EnumSet containing enums from Options for each line in the
    * file where *value* contains true.
    *
    * @return EnumSet of selected options
    */
   public static EnumSet<Options> parseSimulationSetup() throws IllegalArgumentException {
      ArrayList<String[]> readSimSetupFile = readFileAndSplit("SimulationSetup", ".\\SimConfig\\");
      EnumSet<Options> options = EnumSet.noneOf(Options.class);

      for (String[] readLine : readSimSetupFile) {
         if (readLine[1].compareTo("true") == 0) {
            options.add(Options.valueOf(readLine[0]));
         }
      }

      return options;
   }

   /**
    * Parses a config file called SimulationSetup.txt located in .\\SimConfig\\
    * where each line is written as <code>"*parameter* = *value*\n"</code>
    * and returns a String of the right hand side value contained on the line
    * <code>"selectedAircraft = *value*\n"</code>.
    *
    * @return selectedAircraft
    */
   public static String parseSimulationSetupForAircraft() throws IllegalArgumentException {
      ArrayList<String[]> readSimSetupFile = readFileAndSplit("SimulationSetup", ".\\SimConfig\\");
      String selectedAircraft = "";

      for (String[] readLine : readSimSetupFile) {
         if (readLine[0].compareTo("selectedAircraft") == 0) {
            selectedAircraft = readLine[1];
         }
      }

      return selectedAircraft;
   }

   /**
    * Parses the DisplaySetup.txt file in .\SimConfig\ and returns an EnumMap with {@link DisplayOptions}
    * as the keys.
    *
    * @return displayOptions EnumMap
    */
   public static EnumMap<DisplayOptions, Integer> parseDisplaySetup() {
      EnumMap<DisplayOptions, Integer> displayOptions = new EnumMap<DisplayOptions, Integer>(DisplayOptions.class);

      // Display options
      ArrayList<String[]> readDisplaySetupFile = readFileAndSplit("DisplaySetup", ".\\SimConfig\\");

      for (DisplayOptions displayOptionsKey : DisplayOptions.values()) {
         for (String[] readLine : readDisplaySetupFile) {
            if (displayOptionsKey.toString().equals(readLine[0])) {
               displayOptions.put(displayOptionsKey, Integer.decode(readLine[1]));
            }
         }
      }

      return displayOptions;
   }

   /**
    * Parses the AudioSetup.txt file in .\SimConfig\ and returns an EnumMap with {@link DisplayOptions}
    * as the keys.
    *
    * @return audioOptions EnumMap
    */
   public static EnumMap<AudioOptions, Float> parseAudioSetup() {
      EnumMap<AudioOptions, Float> audioOptions = new EnumMap<AudioOptions, Float>(AudioOptions.class);

      // Display options
      ArrayList<String[]> readAudioSetupFile = readFileAndSplit("AudioSetup", ".\\SimConfig\\");

      for (AudioOptions audioOptionsKey : AudioOptions.values()) {
         for (String[] readLine : readAudioSetupFile) {
            if (audioOptionsKey.toString().equals(readLine[0])) {
               audioOptions.put(audioOptionsKey, Float.valueOf(readLine[1]));
            }
         }
      }

      return audioOptions;
   }

   /**
    * Parses the MassProperties.txt file in .\Aircraft\aircraftName and returns an EnumMap with {@link MassProperties}
    * as the keys.
    *
    * @param aircraftName
    * @return massProperties EnumMap
    */
   public static EnumMap<MassProperties, Double> parseMassProperties(String aircraftName) {
      EnumMap<MassProperties, Double> massProperties = new EnumMap<MassProperties, Double>(MassProperties.class);

      // Mass Properties
      ArrayList<String[]> readMassPropFile = readFileAndSplit(aircraftName, ".//Aircraft//", "MassProperties");

      for (MassProperties massPropKey : MassProperties.values()) {
         for (String[] readLine : readMassPropFile) {
            if (massPropKey.toString().equals(readLine[0])) {
               massProperties.put(massPropKey, Double.parseDouble(readLine[1]));
            }
         }
      }

      return massProperties;
   }

   /**
    * @param fileName
    * @return string containing the file's extension
    */
   public static String getFileExtension(String fileName) {
      int periodLocation = fileName.lastIndexOf(".");

      if (periodLocation == -1) {
         return "";
      } else if (periodLocation == fileName.length() - 1) {
         return "";
      } else {
         return fileName.substring(periodLocation + 1, fileName.length());
      }
   }

   //===================================================================================================
   //										File Writing
   //===================================================================================================
   /**
    * Creates a config file called "fileName".txt located in the folder
    * specified by filePath using an EnumMap where each line is written as:
    * <code>"*parameter name* = *double value*\n"</code>.
    *
    * @param filePath the file path
    * @param fileName the file name
    * @param enumMap the EnumMap
    */
   public static void writeConfigFile(String filePath, String fileName, Map<?, ?> enumMap) {
      StringBuilder sb = new StringBuilder();
      sb.append(filePath).append(fileName).append(".txt");

      try (BufferedWriter bw = new BufferedWriter(new FileWriter(sb.toString()))) {
         for (Map.Entry<?, ?> entry : enumMap.entrySet()) {
            bw.write(entry.getKey().toString() + " = " + entry.getValue());
            bw.newLine();
         }
      } catch (FileNotFoundException e) {
         System.err.println("Could not find: " + fileName + ".txt!");
      } catch (IOException e) {
         System.err.println("Could not read: " + fileName + ".txt!");
      } catch (NullPointerException e) {
         System.err.println("Bad reference when reading: " + fileName + ".txt!");
      } catch (NumberFormatException e) {
         System.err.println("Error parsing data from " + fileName + ".txt!");
      }
   }

   /**
    * Creates a config file called "fileName".txt located in the folder specified by filePath
    * using the optionsSet EnumSet of selected options and the selected aircraft's name,
    * where each line is written as <code>"*parameter* = *value*\n"</code>.
    *
    * @param filePath the file path
    * @param fileName the file name
    * @param optionsSet the optionsSet
    * @param selectedAircraft the aircraft
    */
   public static void writeConfigFile(String filePath, String fileName, Set<Options> optionsSet, String selectedAircraft) {
      StringBuilder sb = new StringBuilder();
      sb.append(filePath).append(fileName).append(".txt");

      try (BufferedWriter bw = new BufferedWriter(new FileWriter(sb.toString()))) {
         for (Options option : Options.values()) {
            bw.write(option.name() + " = " + optionsSet.contains(option));
            bw.newLine();
         }
         bw.write("selectedAircraft = " + selectedAircraft);
         bw.newLine();
      } catch (FileNotFoundException e) {
         System.err.println("Could not find: SimulationSetup.txt!");
      } catch (IOException e) {
         System.err.println("Could not read: SimulationSetup.txt!");
      } catch (NullPointerException e) {
         System.err.println("Bad reference to: SimulationSetup.txt!");
      }
   }

   /**
    * Writes a CSV file from data contained within the logsOut ArrayList.
    *
    * @param file the file
    * @param logsOut the logsOut
    * @throws IOException
    */
   public static void saveToCSVFile(File file, List<Map<SimOuts, Double>> logsOut) throws IOException {

      BufferedWriter bw = new BufferedWriter(new FileWriter(file.getPath()));

      // First line of CSV file should have the names of each parameter
      StringBuilder sb_line1 = new StringBuilder();
      for (SimOuts simOut : SimOuts.values()) {
         sb_line1.append(simOut.toString()).append(",");
      }
      bw.write(sb_line1.append("\n").toString());

      // Subsequent lines contain data
      for (Map<SimOuts, Double> simOut : logsOut) {
         StringBuilder sb = new StringBuilder();
         for (Map.Entry<?, Double> entry : simOut.entrySet()) {
            sb.append(entry.getValue().toString()).append(",");
         }
         bw.write(sb.append("\n").toString());
      }

      bw.close();
   }

   //===================================================================================================
   //										Unit Conversions
   //===================================================================================================
   /**
    * Convert knots to ft/sec.
    *
    * @param knots the knots
    * @return Airspeed converted from knots to ft/sec
    */
   public static double toFtPerSec(double knots) {
      return knots * 1.687810;
   }

   /**
    * Convert ft/sec to knots.
    *
    * @param ftPerSec the ft/sec
    * @return Airspeed converted from ft/sec to knots
    */
   public static double toKnots(double ftPerSec) {
      return ftPerSec / 1.687810;
   }
}