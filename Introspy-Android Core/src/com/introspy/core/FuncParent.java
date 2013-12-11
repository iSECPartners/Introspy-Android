package com.introspy.core;

import android.util.Log;

import javax.crypto.Cipher;

import com.saurik.substrate.MS;

@SuppressWarnings("rawtypes")
class FuncParent {
	protected HookConfig _config = null;
	
	protected String _TAG = LoggerConfig.getTag();
	protected String _TAG_ERROR = LoggerConfig.getTagError();
	
	protected String _className, _methodName, _type;
	protected String _packageName, _dataDir;
	protected Object _resources;
	protected Object _args;
	protected Class<?>[] _parameters;
	protected MS.MethodPointer _old = null;
		
	protected static Cipher _lastCipher;
	protected static Integer _lastMode;
	
	protected static boolean _filterSSLCrypto = true;
	protected static boolean _filterFS = true;
	
	protected Logger _l = null;
	
	// Protected constructor prevents instantiation from other classes
	protected FuncParent() {

	}
	
	public void init(HookConfig config, Object resources, MS.MethodPointer old, Object... args) {
		if (_config == null) {
			_config = config;
			_className = _config.getClassName();
			_methodName = _config.getMethodName();
			_parameters = _config.getParameters();
			_type = _config.getType();
			_old = old;
			
			_resources = resources;
			
			_packageName = ApplicationConfig.getPackageName();
			_dataDir = ApplicationConfig.getDataDir();
			
			_l = new Logger(_config);
		}
	}
	
	public void enableTraces() {
		_l.enableTraces();
	}
	public void disableTraces() {
		_l.disableTraces();
	}
	public void disableDBlogger() {
		_l.disableDBlogger();
	}
	public void enableDBlogger() {
		_l.enableDBlogger();
	}
	
	public void execute(Object... args) {
		// display info on the app related to the hook
		_l.logBasicInfo();
		if (!_config.getNotes().isEmpty())
			_l.logLine("Notes: " + _config.getNotes());
		_l.logLine("-> Resources type: " + 
				(_resources !=  null ? _resources.getClass() : "None (Static method?)"));
		
		try {
			Class<?>[] parameters = _config.getParameters();
			if (_config.getParameters() != null) {
				int argNb = 0;
				for (Class<?> elemParameter : parameters) {
					_l.logLine("-> Argument " + (argNb + 1) +
							", Data: " + args[argNb].toString() +
							" (Type: " + elemParameter.getName() + ")"
							);
					argNb++;
				}
			}
		}
		catch (Exception e) {
			Log.w(_TAG, "-> Error getting arguments");
		}
		_l.logFlush_I();
	}
	
	protected void _WDump() {
		// dump in W logs
		Log.w(_TAG, "#### "+ _type +" ###, Package: " + _packageName + 
				", localdir: "+_dataDir);													
		Log.w(_TAG, "Method: " + _config.getMethodName());
		Log.w(_TAG, "Notes: " + _config.getNotes());
		Log.w(_TAG, "Resources type: " + 
				(_resources !=  null ? _resources.getClass() : "None (Static method?)"));
	}
}

//##############################################

class Func_DUMP extends FuncParent { 
	public void execute(Object... args) {
		super.execute(args);
		Log.i(_TAG, "------------------");
	}
}
