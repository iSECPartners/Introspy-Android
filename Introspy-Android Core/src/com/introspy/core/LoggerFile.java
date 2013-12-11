package com.introspy.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

// This class is not used, a DB is used instead

public class LoggerFile {
	protected void logToFile(String logEntry) {
	// junk code	
		try {
			File logFile = new File(ApplicationConfig.getDataDir() + "/introspy.log"); // "sdcard/introspy.log"
			
			if (!logFile.exists())
		   {
		      try
		      {
		         logFile.createNewFile();
		      }
		      catch (IOException e)
		      {
		    	 Log.w("IntrospyLog", "Cannot create log file: " + e);
		      }
		   }
		   try
		   {
		      //BufferedWriter for performance, true to set append to file flag
		      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
		      buf.append(logEntry);
		      buf.newLine();
		      buf.close();
		   }
		   catch (IOException e)
		   {
			   Log.w("IntrospyLog", "Error with file logger: " + e);
		   }
		} catch (Exception e) {
			Log.w("IntrospyLog", "Error with file logger: " + e);
		}
	}
}
