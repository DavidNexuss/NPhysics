package com.nsoft.nphysics.desktop;

import java.awt.Toolkit;

import org.lwjgl.input.Mouse;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nsoft.nphysics.IsInWindow;
import com.nsoft.nphysics.NPhysics;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.samples = 4;
		config.fullscreen = true;
		config.width = Toolkit.getDefaultToolkit().getScreenSize().width;
		config.height = Toolkit.getDefaultToolkit().getScreenSize().height;
		new LwjglApplication(new NPhysics(new IsInWindow() {
			
			@Override
			public boolean isMouseInWindow() {
				
				return Mouse.isInsideWindow();	
			}
		}), config);
	}
}
