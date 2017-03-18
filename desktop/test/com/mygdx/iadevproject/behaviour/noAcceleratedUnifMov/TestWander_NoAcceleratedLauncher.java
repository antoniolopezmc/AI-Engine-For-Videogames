package com.mygdx.iadevproject.behaviour.noAcceleratedUnifMov;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.iadevproject.behaviour.noAcceleratedUnifMov.TestWander_NoAccelerated;

public class TestWander_NoAcceleratedLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 2000;
		//config.height = 1000;
		new LwjglApplication(new TestWander_NoAccelerated(), config);
	}
}
