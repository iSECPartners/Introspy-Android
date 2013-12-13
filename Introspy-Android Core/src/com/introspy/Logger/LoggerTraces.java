package com.introspy.Logger;

import android.util.Log;

public class LoggerTraces extends LoggerConfig {
	
	private String _getFullTraces() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	    if (ste == null)
	        return "";
	    StringBuilder stringBuilder = new StringBuilder();
	    for (StackTraceElement element : ste)
	        stringBuilder.append(element.toString()).append("\n");
	    return stringBuilder.toString();
	}
	
	public void enableTraces() {
		_stackTraces  = true;
	}
	public void disableTraces() {
		_stackTraces  = false;
	}
	
	protected void _addTraces() {
		_traces = "";
		if (_stackTraces) {
			if (_fullTraces)
				_traces = _getFullTraces();
			else
				_traces = _getTraces();
			
			_out = _traces + _out;
		}
	}
	
	protected String _getTraces() {
	    String out = "";
		try {
			StackTraceElement[] ste = 
					Thread.currentThread().getStackTrace();
		    if (ste == null)
		        return "";
			out += "----------- " + ste[10] + "\n"; 
			out += "----------- " + ste[11] + "\n"; 
			out += "----------- " + ste[12] + "\n"; 
		}
		catch (Exception e) {
			out = "-> Cannot get Stack Traces";
			Log.w(_TAG_ERROR, out);
		}
		return out;
	}
}
