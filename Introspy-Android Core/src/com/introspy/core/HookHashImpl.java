package com.introspy.core;

import java.lang.reflect.Method;

import android.util.Log;


class Func_HASH extends FuncParent { 
	public void execute(Object... args) {
		
	    StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	    // this is called within apps and is super noisy so not displaying it
	    if (ste[7].toString().contains("com.crashlytics."))
	    	return;
	    // the above code may only work on android 4.2.2
	    // replace with the code below if so
	    /* for (int i = 0; i < ste.length; i++)
	        if (ste[i].toString().contains("com.crashlytics."))
	        	return; */
	    
		if (args[0] != null) {
            _l.logBasicInfo();
            String input = null;

            input = new String((byte[])args[0]);
            if (!StringHelper.isItReadable(input))
                    input = StringHelper.byteArrayToB64((byte[])args[0]);
            
            byte[] output = null;
            try {
                    // execution the method to calculate the digest
                    // using reflection to call digest from the object's instance
                    try {
                            Class<?> cls = Class.forName("java.security.MessageDigest");
                            Object obj =_resources;
                            Class<?> noparams[] = {};
                            Method xmethod = cls.getDeclaredMethod("digest", noparams);
                            output = (byte[]) xmethod.invoke(obj);
                    }
                    catch (Exception e) {
                    	Log.w(_TAG_ERROR, "Error in Hash fun: " + e);
                    }
            }
            catch (Throwable e) {
            	Log.w(_TAG_ERROR, "Error in Hash fun: " + e);
            }
            String s_output = StringHelper.byteArrayToB64(output);

            // use reflection to call a method from this instance
            String algoName = null;
            try {
                    Class<?> cls = Class.forName("java.security.MessageDigest");
                    Object obj =_resources;
                    Class<?> noparams[] = {};
                    Method xmethod = cls.getDeclaredMethod("getAlgorithm", noparams);
                    algoName = (String) xmethod.invoke(obj);
            }
            catch (Exception e) {
                    algoName = "error: " + e;        
            }
            
            _l.logLine("-> Hash of : [" + input + "] is: [" + 
                            s_output +"] , Algo: [" + algoName + "]");
            
            _l.logParameter("Input", input);
            _l.logParameter("Output", s_output);
            _l.logParameter("Algo", algoName);
            
            if (algoName.contains("MD5")) {
                    _l.logFlush_W("MD5 used");
            }
            else
                    _l.logFlush_I();
		}
	}
}