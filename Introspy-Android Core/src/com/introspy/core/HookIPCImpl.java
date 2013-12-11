package com.introspy.core;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

class FuncIPC extends FuncParent {
	protected String getExtras(Intent intent) {
		String out = "";
		try {
			Bundle bundle = intent.getExtras();
		    if (bundle != null) {
				for (String key : bundle.keySet()) {
				    Object value = bundle.get(key);
				    out += String.format("--> [%s %s (%s)]\n", key,  
				        value.toString(), value.getClass().getName());
				}
				out = out.substring(0, out.length() - 1);
		    }
		}
		catch (Exception e) {
			out = "Cannot get intent extra";
		}
		return out;
	}
}

class Func_DUMP_INTENT extends FuncIPC { 
	public void execute(Object... args) {		
		_l.logBasicInfo();
		
		// arg0 is an Intent
		Intent intent = (Intent) args[0];
		String out = "-> " + intent;
		_l.logParameter("Intent", intent);
		String extra = getExtras(intent);
		if (!extra.isEmpty()) {
			_l.logParameter("Extra", extra);
			out += "\n-> Extra: \n" + extra + "";
		}
		_l.logFlush_I(out);
	}
}

// Hook:
// Intent registerReceiver (BroadcastReceiver receiver, IntentFilter filter)
// Intent registerReceiver (BroadcastReceiver receiver, IntentFilter filter, 
//							String broadcastPermission, Handler scheduler)
class Func_IPC_RECEIVER extends FuncIPC { 
	public void execute(Object... args) {
		_l.logBasicInfo();
		String out = "";
		
		// arg1 is an intent filter
		IntentFilter intentFilter = (IntentFilter) args[1];
		if (intentFilter != null) {
			out = "-> Intent Filter: \n";
			for (int i = 0; i < intentFilter.countActions(); i++)
				out += "--> [Action "+ i +":"+intentFilter.getAction(i)+"]\n";
			out = out.substring(0, out.length() - 1);
			_l.logParameter("Intent Filter", out);
		}
		
		// args[2] is the permissions
		if (args.length > 2 && args[2] != null) {
			out += ", permissions: " + args[2];
			_l.logParameter("Permissions", args[2]);
		}
		_l.logLine(out);
		
		if (args.length == 2 || (args.length > 2 && args[2] == null))			
			_l.logFlush_I("-> No permissions explicitely defined for the Receiver");
		else
			_l.logFlush_I();
	}
}

class Func_URI_REGISTER extends FuncIPC { 
	public void execute(Object... args) {
		String uriPath = (String)args[1];
		_l.logParameter("URI Path", uriPath);
		
		String data = "URI:"+_config.getMethodName()+":"+ApplicationConfig.getPackageName()+uriPath;
		_l.logBasicInfo();
		_l.logFlush_I(data);
	}
}
	
// IPCs disabled in the manifest can be enabled dynamically
class Func_IPC_MODIFIED extends FuncIPC { 
	public void execute(Object... args) {
		
		// arg1: newState
		int newState = (Integer)args[1];
		if (newState == 
				android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			_l.logBasicInfo();
			_l.logParameter("New State", "COMPONENT_ENABLED_STATE_ENABLED");
			_l.logFlush_W("-> !!! Component ["+ args[0] +
					"] is ENABLED dynamically");
		}
	}
}

