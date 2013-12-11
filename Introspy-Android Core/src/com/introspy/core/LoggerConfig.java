package com.introspy.core;

public class LoggerConfig {
	protected LoggerConfig() {
	}
	
	protected HookConfig _config;
	
	public static String _TAG = "Introspy";
	public static String _TAG_ERROR = "IntrospyLog";
	
	public static String getTag() {
		return _TAG;
	}
	
	public static String getTagError() {
		return _TAG_ERROR;
	}
	
	protected String _out = "";
	protected String _notes = "";
	protected String _traces = "";
	
	protected boolean _enableDB = true;
	
	// this can be enabled via the _config file
	protected boolean _stackTraces = false;
	
	// change this value to get full traces
	protected boolean _fullTraces = false;
	
}
