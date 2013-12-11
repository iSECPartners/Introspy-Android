package com.introspy.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class InjectConfig {
	
	private static String _TAG = "IntrospyGUI";
	
	private static InjectConfig _instance = null;
	
	protected InjectConfig() {
	}
	static InjectConfig getInstance() {
		if (_instance == null)
			_instance = new InjectConfig();
		return _instance;
	}
	
	public String getHookTypesFromPrefs(Context context) {
		String[] hookTypes = HookTypes.getHookTypes();
		SharedPreferences sp = 
				context.getSharedPreferences("HookTypes", 0);
		String ret = "";
		
    	for (int i = 0; i < hookTypes.length; i++) {
    		if (sp.getBoolean(hookTypes[i], false)) {
    			ret += hookTypes[i];
    			ret += ",";
    		}
    	}
    	
    	ret = ret.substring(0, ret.length() - 1);
    	ret += "\n";
		return ret;
	}
	
	public Boolean enableApp(String appDir, Context context) {
		String path = appDir + "/introspy.config";
		try {
			Runtime.getRuntime().exec("su -c echo '" + 
					getHookTypesFromPrefs(context) + "' > " + path + 
					" ; chmod 664 " + path);
		} catch (Exception e) {
			Log.w(_TAG, "error: this app needs to be root.");
			return false;
		}
		return true;
	}
	
	public Boolean disableApp(String appDir) {
		String path = appDir + "/introspy.config";
		try {
			Runtime.getRuntime().exec("su -c rm " + path);
		} catch (Exception e) {
			Log.w(_TAG, "error: this app needs to be root.");
			return false;
		}
		return true;
	}
	
	public Boolean execute(String command) {
		try {
			Runtime.getRuntime().exec("su -c '" + command + "'");
		} catch (Exception e) {
			Log.w(_TAG, "error: this app needs to be root.");
			return false;
		}
		return true;
	}
	
	public Boolean writeConfig(Boolean write, String appDir, Context context) { 
		return (write ? enableApp(appDir, context) : disableApp(appDir));
	}
}
