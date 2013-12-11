package com.introspy.config;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HookTypes extends UpdateAppList {

	private Boolean _alwaysOverwriteConfig = false;
	
	protected static String[] _hookTypes ={
			"CRYPTO",
			"KEY",
			"HASH",
			"FS", 
			"IPC", 
			"PREF",
			"URI", 
			"SSL",
			"WEBVIEW",
			"SQLite\n|+NO DB",
			"| STACK TRACES",
			"| NO DB"
	};

	public static String[] getHookTypes() {
		return _hookTypes;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_context = this.getActivity().getApplicationContext();
		
		ListAdapter myListAdapter = new ArrayAdapter<String>(
				getActivity(),
				R.layout.list_filter_types,
				_hookTypes);
		
		setListAdapter(myListAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.listfragment, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		// save new config in prefs
		super.onListItemClick(l, v, position, id);
		
		// only update the list once (slow)
		// TODO: add asyncTask on this
		// and update list of apps regularly
		// (currently need to restart the app to refresh the list)
		if (_appList.isEmpty())
			updateAppList();
		
		// get list of supposedly monitored apps from prefs
		SharedPreferences _sp_appFilter = 
				_context.getSharedPreferences("ApplicationFilter", 0);

		// update selected apps with the new config
		for (int i = 0; i < _appList.size(); i++) {
			String appDir = _appList.get(i);
			Boolean checked = _sp_appFilter.getBoolean(appDir, false);
			// overwrite / remove the config file even if preferences
			// state otherwise because the tester may remove/add it
			if (_alwaysOverwriteConfig)
				InjectConfig.getInstance().writeConfig(checked, appDir, _context);
			else if (checked) {
				InjectConfig.getInstance().enableApp(appDir, _context);
			}
		}
		
	}
	
    @Override
    public void onStart() {
            super.onStart();
            try {
	            _sp = _context.getSharedPreferences("HookTypes", 0);
	            Boolean setup = _sp.getBoolean("Enabled", false);
	            
	            if (setup == false) {
	        		_sp.edit().putBoolean("Enabled", true).commit();
	            	for(String item : _hookTypes ) {
	            		_sp.edit().putBoolean(item, true).commit();
	            	}
	            	
		        	final ListView list = getListView();
		        	for (int i = 0; i < getListAdapter().getCount(); i++) {
		        	        list.setItemChecked(i, true);
		        	}
	            }
	            else {
	            	final ListView list = getListView();
	            	for (int i = 0; i < _hookTypes.length; i++) {
	            		list.setItemChecked(i, _sp.getBoolean(_hookTypes[i], false));
	            	}
	            }
            }
            catch (Exception e) {
            	Log.w("IntrospyConfig", "Error: " + e + "\n "+e.fillInStackTrace());
            }
    }
}
