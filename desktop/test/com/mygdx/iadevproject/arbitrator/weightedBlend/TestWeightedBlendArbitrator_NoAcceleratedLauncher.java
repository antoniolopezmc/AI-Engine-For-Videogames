package com.mygdx.iadevproject.arbitrator.weightedBlend;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class TestWeightedBlendArbitrator_NoAcceleratedLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 2000;
		//config.height = 1000;
		new LwjglApplication(new TestWeightedBlendArbitrator_NoAccelerated(), config);
	}

}
