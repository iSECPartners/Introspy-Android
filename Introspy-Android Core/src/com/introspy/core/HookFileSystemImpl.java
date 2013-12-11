package com.introspy.core;

import java.io.File;

import android.util.Log;


class Func_FILE_PARENT extends FuncParent {
	// enherited by the FS hook classes
	protected boolean is_SD_card(String path) {
		if (path != null && 
				(path.contains("sdcard") || 
				path.contains("/storage/"))) {
			
			// crashes with system app:
			// path.contains(Environment.getExternalStorageDirectory().toString()
			
			//super.execute(config, resources, old, args);
			return true;
		}
		return false;
	}
}

class Func_FILE_CHECK_DIR extends Func_FILE_PARENT { 
	public void execute(Object... args) {
		// noisy so display data only when it reads the sdcard
		// arg0 is the path
		try {
			// String root = _dataDir + "/" + args[0];
			String path = "[" +  args[0] + "]";
			_l.logParameter("Path", path);
			if (is_SD_card(path)) {
				_l.logBasicInfo();
				_l.logFlush_W("Read/write on sdcard: " + path);
			} else {
				// one liner on this to avoid too much noise
				_packageName = ApplicationConfig.getPackageName();
				_dataDir = ApplicationConfig.getDataDir();
				_l.logFlush_I("### FS:"+ _packageName + ":" + path);
			}
			
		} catch (Exception e) {
			Log.w("IntrospyLog", "Exception in Func_FILE_CHECK_DIR: " + e);
			Log.w("IntrospyLog", "-> App path: " + ApplicationConfig.getDataDir() + 
					"\n" + e.fillInStackTrace());
		}
	}
}

class Func_CHECK_FS_SET extends Func_FILE_PARENT { 
	public void execute(Object... args) {
		// noisy
		// arg0 is the path
		if ((Boolean)(args[0]) == true && 
				(Boolean)args[1] == false) {
			//super.execute(config, resources, old, args);
			File f = (File) _resources;
			_l.logBasicInfo();
			_l.logParameter("Mode", "WORLD read/write");
			_l.logParameter("Path", f.getAbsolutePath());
			_l.logFlush_W("Writing file with WORLD read/write mode: " + 
						" in " + f.getAbsolutePath());
		}
	}
}

class Func_FILE_CHECK_MODE extends Func_FILE_PARENT { 
	@SuppressWarnings("deprecation")
	public void execute(Object... args) {
		// arg0 is the path
		String path = ": [" + ApplicationConfig.getDataDir() + "/" +  (String)args[0] + "]";
		if (is_SD_card(path)) {
			_l.logBasicInfo();
			_l.logParameter("Path", path);
			_l.logFlush_W("Read/write on sdcard: " + path);
		}
		else {
			// arg1 is the mode
			Integer mode = (Integer) args[1];
			
			String smode;
			switch (mode) {
				case android.content.Context.MODE_PRIVATE: 
					smode = "Private";
					break;
				case android.content.Context.MODE_WORLD_READABLE: 
					smode = "!!! World Readable !!!";
					break;
				case android.content.Context.MODE_WORLD_WRITEABLE: 
					smode = "!!! World Writable !!!";
					break;
				default: 
					smode = "???";
			}
			smode = "MODE: " + smode;
			
			if (mode == android.content.Context.MODE_WORLD_READABLE || 
					mode == android.content.Context.MODE_WORLD_WRITEABLE) {
				_l.logBasicInfo();
				_l.logParameter("Mode", smode);
				_l.logFlush_W("Writing file with dangerous mode: " + 
							smode + " in " + path);
			}
		}
	}
}
