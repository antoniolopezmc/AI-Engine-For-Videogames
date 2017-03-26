package com.mygdx.iadevproject.aiReactive.behaviour.noAcceleratedUnifMov;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.iadevproject.aiReactive.behaviour.noAcceleratedUnifMov.TestArrive_NoAccelerated;

public class TestArrive_NoAcceleratedLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 2000;
		//config.height = 1000;
		new LwjglApplication(new TestArrive_NoAccelerated(), config);
	}
}
