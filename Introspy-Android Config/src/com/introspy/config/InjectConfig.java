package com.introspy.config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
//    	ret += "\n";
		return ret;
	}
	
	public void enableApp(String appDir, Context context) {
		String path = appDir + "/introspy.config";
		_command += "echo '" + getHookTypesFromPrefs(context) + "' > " + path + " \n";
		_command += "chmod 664 " + path + " \n";
	}
	
	public void disableApp(String appDir) {
		String path = appDir + "/introspy.config";
		_command += "rm " + path + " \n";
	}
	
	private String _command = "";
	
	public Boolean commit() {
		if (_command.isEmpty())
			return false;
		Log.i(_TAG, _command);
		try {
			execSuCommand(_command);
		} catch (Exception e) {
			_command = "";
			Log.w(_TAG, "error: this app needs to be root.");
			return false;
		}
		_command = "";
		return true;
	}
	
	public void writeConfig(Boolean enable, String appDir, Context context) { 
		if (enable)
			enableApp(appDir, context);
		else
			disableApp(appDir);
	}
	
	public static String execSuCommand(String cmd) throws IOException
	{
		Log.e("execSuCommand", cmd);
		Process process = Runtime.getRuntime().exec("su");  
		  
        DataOutputStream os = new DataOutputStream(process.getOutputStream());  
        os.writeBytes(cmd+"\n");
        os.flush();
        os.writeBytes("exit\n");
        os.flush();
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(  
                process.getInputStream()));  
        int read;  
        char[] buffer = new char[4096];  
        StringBuffer output = new StringBuffer();  
        while ((read = reader.read(buffer)) > 0) {  
            output.append(buffer, 0, read);  
        }  
        reader.close();
        os.close();
        return output.toString();
	}

}
