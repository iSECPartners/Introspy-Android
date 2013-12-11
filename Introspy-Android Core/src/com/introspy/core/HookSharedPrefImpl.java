package com.introspy.core;

import java.util.Map;

import android.content.SharedPreferences;
import android.util.Log;

import com.saurik.substrate.MS;


class Func_CHECK_SHARED_PREF extends FuncParent { 
	protected static boolean _onlyRetrievedPrefOnce = true;
	protected static boolean _prefRetrieved = true; // true means it's not dumped
	public void execute(Object... args) {
		
		// this is noisy so only display data when there is a potential issue
		if (!_onlyRetrievedPrefOnce || 
				(_onlyRetrievedPrefOnce && !_prefRetrieved)) {
			String prefName = (String) args[0];
			_l.logParameter("Preference Name", args[0]);
			_l.logLine("### PREF:"+ApplicationConfig.getPackageName() + ":getSharedPref:"+prefName);
			// display the pref retrieved
			try {
				SharedPreferences prefs = (SharedPreferences) 
						_old.invoke(_resources, args);
				if (prefs != null && prefs.getAll().size() > 0)
					_l.logFlush_I("-> " + prefs.getAll());
			} catch (Throwable e) {
				_l.logLine("-> not able to retrieve preferences");
			}
			_prefRetrieved = true;
		}
		
		// arg1 is the sharing modes
		Integer mode = (Integer) args[1];
		String smode = "";
		
		if (mode == android.content.Context.MODE_WORLD_READABLE)
			smode = "MODE_WORLD_READABLE";
		else if (mode == android.content.Context.MODE_WORLD_WRITEABLE)
			smode = "MODE_WORLD_WRITEABLE";
		
		if (!smode.isEmpty()) {
			_l.logParameter("Preference Name", args[0]);
			_l.logParameter("Mode: ", smode);
			_l.logFlush_W("Shared preference accessible to the WORLD. " +
					"(MODE: " + smode + ")");
		}
	}
}

class Func_GET_SHARED_PREF extends FuncParent { 
	public void execute(Object... args) {		
		try {
			String prefName = (String) args[0]; // name of pref to retrieve
			_l.logParameter("Preference Name", args[0]);
			String out = "### PREF:"+ApplicationConfig.getPackageName() + 
							":getSharedPref:"+prefName+" - ";
			Object o = _old.invoke(_resources, args);
			if (o != null) {
				out += "["+o+"]";
				_l.logReturnValue("Value", o);
				_l.logFlush_I(out);
			}
			else {
				_l.logFlush_W("Preference not found (Hidden pref?)");
			}
		} catch (Throwable e) {
			Log.w("IntrospyLog", "error in Func_GET_SHARED_PREF: "+e);
		}
	}
}

class Func_PUT_SHARED_PREF extends FuncParent { 
	public void execute(Object... args) {
		
		String prefName = (String) args[0]; // name of pref to retrieve
		_l.logParameter("Preference Name", args[0]);
		_l.logParameter("Value", args[1]);
		String out = "### PREF:"+ApplicationConfig.getPackageName() + ":writeSharedPref:"
						+prefName+", value: "+args[1];
		_l.logFlush_I(out);
	}
}

class Func_CONTAINS_SHARED_PREF extends FuncParent { 
	public void execute(Object... args) {
		String out = "";
		String prefName = (String) args[0]; // name of pref to retrieve

		try {
			boolean o = (Boolean) _old.invoke(_resources, args);
			_l.logParameter("Preference Name", args[0]);
			if (o == false) {
				out = "### PREF:"+ApplicationConfig.getPackageName()+
						":contains:"+ prefName;
				_l.logReturnValue("Value", o);
				_l.logLine(out);
				_l.logFlush_W("Preference not found (Hidden pref?)");
			}
		} catch (Throwable e) {
			Log.i("IntrospyLog", "error in Func_GET_SHARED_PREF: "+e);
		}
	}
}

class Func_GET_ALL_SHARED_PREF extends FuncParent { 
	public void execute(Object... args) {
			
		// display the pref retrieved
			try {
				Map<String,?> keys = (Map<String, ?>) _old.invoke(_resources, args);
				if (keys != null && keys.size() > 0) {
					_l.logLine("### PREF:"+ApplicationConfig.getPackageName()+":getAll:");
					for(Map.Entry<String,?> entry : keys.entrySet()){
			            _l.logLine("-> " + entry.getKey() + ": " + 
			                                   entry.getValue().toString());            
					}
				}
				_l.logFlush_I();
			} catch (Throwable e) {
				Log.i("IntrospyLog", "-> not able to retrieve all preferences (getAll in " + 
						ApplicationConfig.getPackageName()+")");
			}
	}
}
