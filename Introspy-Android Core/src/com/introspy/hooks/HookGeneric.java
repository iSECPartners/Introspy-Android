package com.introspy.hooks;

import android.util.Log;

import com.introspy.core.IntroHook;

class Intro_DUMP extends IntroHook { 
	protected void _WDump() {
		// dump in W logs
		Log.w(_TAG, "#### "+ _type +" ###, Package: " + _packageName + 
				", localdir: "+_dataDir);													
		Log.w(_TAG, "Method: " + _config.getMethodName());
		Log.w(_TAG, "Notes: " + _config.getNotes());
		Log.w(_TAG, "Resources type: " + 
				(_resources !=  null ? _resources.getClass() : "None (Static method?)"));
	
	}
	
	public void execute(Object... args) {
		super.execute(args);
		// dump a bit more info
		// _WDump();
		Log.i(_TAG, "------------------");
	}
}
