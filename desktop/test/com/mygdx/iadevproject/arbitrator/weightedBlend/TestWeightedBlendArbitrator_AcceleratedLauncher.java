package com.mygdx.iadevproject.arbitrator.weightedBlend;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.iadevproject.arbitrator.weightedBlend.TestWeightedBlendArbitrator_Accelerated;

public class TestWeightedBlendArbitrator_AcceleratedLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 2000;
		//config.height = 1000;
		new LwjglApplication(new TestWeightedBlendArbitrator_Accelerated(), config);
	}

}
