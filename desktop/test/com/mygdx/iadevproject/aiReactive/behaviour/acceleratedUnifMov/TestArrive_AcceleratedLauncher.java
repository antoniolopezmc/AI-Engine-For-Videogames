package com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.iadevproject.aiReactive.behaviour.acceleratedUnifMov.TestArrive_Accelerated;

public class TestArrive_AcceleratedLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 2000;
		//config.height = 1000;
		new LwjglApplication(new TestArrive_Accelerated(), config);
	}
}
