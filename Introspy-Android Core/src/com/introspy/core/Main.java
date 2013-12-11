package com.introspy.core;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.util.Log;
import com.saurik.substrate.*;

public class Main {	
	static private String 	_TAG = LoggerConfig.getTag();
	static private String 	_TAG_ERROR = LoggerConfig.getTagError();

	public static void initialize() {
		final HookConfig[] _config = HookConfig.config;
			for (final HookConfig elemConfig : _config) {
				if (!elemConfig.isActive())
					continue;
				
				MS.hookClassLoad(elemConfig.getClassName(),
					new MS.ClassLoadHook() {
						public void classLoaded(Class<?> resources) {
							_hookMethod(resources, elemConfig);
						}
					});
		}
		
		MS.hookClassLoad("android.app.ContextImpl", new MS.ClassLoadHook() {
			public void classLoaded(Class<?> resources) { 
				_initApplicationState(resources);
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void _hookMethod(Class<?> resources, final HookConfig elemConfig) {
		
		GenericDeclaration pMethod = null;
		final String className = elemConfig.getClassName();
		final String methodName = elemConfig.getMethodName();
		final Class<?>[] parameters = elemConfig.getParameters();
		
		Log.i(_TAG, "### Hooking: " + className + "->" + 
				methodName + "() with " + 
        		parameters.length + " args");
		try {
			// check if the method is a constructor
			if (className.substring(className.lastIndexOf('.') + 1).equals(methodName))
				pMethod = resources.getDeclaredConstructor(parameters);
			else	
				pMethod = resources.getMethod(methodName, parameters); 
		}
		catch (NoSuchMethodException e) {
				Log.w(_TAG_ERROR, "Error - No such method: " + methodName + " with " + 
	            		parameters.length + " args");
		        for (int j = 0; j < parameters.length; j++) 
		        	Log.i(_TAG_ERROR, "Arg "+ (j+1) +" type: " + parameters[j]);
		        return;
		}
				
		final MS.MethodPointer old = new MS.MethodPointer();
		MS.hookMethod_(resources, (Member) pMethod,
				new MS.MethodHook() {
					public Object invoked(final Object resources,
							final Object... args) throws Throwable {
						if (ApplicationConfig.isEnabled())
							_hookMethodImpl(old, resources, elemConfig, args);
						return old.invoke(resources, args);
					}
				}, old);
	}
		
	@SuppressWarnings("rawtypes")
	protected static void _hookMethodImpl(final MS.MethodPointer old,
			Object resources, final HookConfig elemConfig,
			Object... args) {
		
		String packageName = ApplicationConfig.getPackageName();
		String type = elemConfig.getType();
		if (packageName == null)
			ApplicationConfig.setPackageName("System?");
		String dataDir = ApplicationConfig.getDataDir();
		
		if ((LoadConfig.getInstance().initConfig(dataDir) && 
				LoadConfig.getInstance().getHookTypes().contains(type))) {
			try {
				elemConfig.getFunc().init(elemConfig, resources, old, args);

				if (LoadConfig.getInstance().getHookTypes().contains("STACK TRACES"))
					elemConfig.getFunc().enableTraces();
				else
					elemConfig.getFunc().disableTraces();
				
				if (LoadConfig.getInstance().getHookTypes().contains("NO DB") ||
						 LoadConfig.getInstance().getHookTypes().contains("SQLite"))
					elemConfig.getFunc().disableDBlogger();
				else
					elemConfig.getFunc().enableDBlogger();
				
				elemConfig.getFunc().init(elemConfig, resources, old, args);
				elemConfig.getFunc().execute(args);
			} catch (Exception e) {
				Log.w(_TAG_ERROR, "-> Error in injected code: " + e);
				Log.w(_TAG_ERROR, ApplicationConfig.getPackageName() + 
						", method: " + elemConfig.getMethodName() + 
						", class: " + elemConfig.getClassName());
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void _initApplicationState(Class<?> resources) {
		
		String methodName = "getPackageName";
		Class<?>[] params = new Class<?>[]{};						
		Method pMethod = null;
		try { 
			pMethod = resources.getMethod(methodName, params);
		} catch (Exception e) {
            Log.w(_TAG_ERROR, "Error - No such method: " + methodName);
            return;
		}

		final MS.MethodPointer old = new MS.MethodPointer();
		MS.hookMethod(resources, pMethod, new MS.MethodHook() {
					public Object invoked(Object resources, 
						Object... args) throws Throwable {
					
						String packageName = (String)old.invoke(resources, args);
						if (ApplicationConfig.getPackageName() == null || 
								ApplicationConfig.getPackageName() == "android") {
							ApplicationConfig.setPackageName(packageName);
						
							Class<?> cls = Class.forName("android.app.ContextImpl");
							Class<?> noparams[] = {};
							Method _method = cls.getDeclaredMethod("getApplicationContext", noparams);
							
							Context context = (Context) _method.invoke(resources);
							ApplicationConfig.setContext(context);
							
							//PackageManager pm = context.getPackageManager();
							_method = cls.getDeclaredMethod("getPackageManager", noparams);
							PackageManager pm = (PackageManager) _method.invoke(resources);

							
							android.content.pm.ApplicationInfo ai = 
									pm.getApplicationInfo(packageName, 0);
					
							if ((ai.flags & 0x81) != 0) {
								ApplicationConfig.disable();
							}
							else {
								try {
								    PackageInfo p = pm.getPackageInfo(packageName, 0);
								    ApplicationConfig.setDataDir(p.applicationInfo.dataDir);
								} catch (NameNotFoundException e) {
								    Log.w(_TAG_ERROR, "Error Package name not found ", e);
								}
							}
						}
						return packageName;
					}
		}, old);
	}
}
