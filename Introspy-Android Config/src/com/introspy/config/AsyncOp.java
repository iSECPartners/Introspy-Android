package com.introspy.config;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

@SuppressWarnings("rawtypes")
public class AsyncOp extends AsyncTask {
	public AsyncOp(Context _context) {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object doInBackground(Object... params) {
		if (!InjectConfig.getInstance().execute((String) params[0])) {
			//Toast.makeText(getActivity(), "This app. needs root!", 
			//		Toast.LENGTH_LONG).show();
		}
		return null;
	}
}
