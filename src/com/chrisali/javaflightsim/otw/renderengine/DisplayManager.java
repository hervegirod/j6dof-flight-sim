package com.chrisali.javaflightsim.otw.renderengine;

import java.awt.Canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.chrisali.javaflightsim.menus.MainFrame;
import com.chrisali.javaflightsim.menus.SimulationWindow;

public class DisplayManager {
	private static final int FPS_CAP = 60;
	
	private static int height = 900;
	private static int width = 1440;
	
	private static int aaSamples = 0;
	
	private static long lastFrameTime;
	private static float delta;
	
	/**
	 * Commands the creation of the OpenGL display; {@link Canvas} object
	 * reference passed from {@link MainFrame} sets the parent object,
	 * allowing the display to be embedded within the {@link SimulationWindow}
	 * 
	 * @param canvas
	 */
	public static void createDisplay(Canvas canvas) {
		try {
			ContextAttribs attribs = new ContextAttribs(3,3)
										.withForwardCompatible(true)
										.withProfileCore(true);
			Display.setParent(canvas);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Java Flight Simulator");
			Display.create(new PixelFormat().withSamples(aaSamples).withDepthBits(24),attribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, width, height);
		lastFrameTime = getCurrentTime();
	}
	
	/**
	 * Commands the creation of the OpenGL display in its own window
	 */
	public static void createDisplay() {
		try {
			ContextAttribs attribs = new ContextAttribs(3,3)
										.withForwardCompatible(true)
										.withProfileCore(true);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Java Flight Simulator");
			Display.create(new PixelFormat().withSamples(aaSamples).withDepthBits(24),attribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, width, height);
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime= getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;	
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}

	public static void setHeight(int height) {
		DisplayManager.height = height;
	}

	public static void setWidth(int width) {
		DisplayManager.width = width;
	}

	public static void setAaSamples(int aaSamples) {
		DisplayManager.aaSamples = aaSamples;
	}
	}
