package com.introspy.core;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saurik.substrate.MS;

class Func_ExecSQL extends FuncParent { 
	public void execute(Object... args) {
//		SQLiteDatabase db = (SQLiteDatabase) _resources;
		_l.logBasicInfo();
		_l.logFlush_I("-> [" + args[0] + "]");
	}
}

class Func_SQLite_UPDATE extends FuncParent { 
	public void execute(Object... args) {
//		SQLiteDatabase db = (SQLiteDatabase) _resources;
		_l.logBasicInfo();
		
		_l.logParameter("Table", args[0]);
		_l.logParameter("Content Values", args[1]);
		_l.logParameter("Where", args[2]);
		_l.logParameter("Where Args", args[3]);
		
		_l.logFlush_I("-> " + "Table: " + args[0] + ", " +
				"ContentValues: " + args[1] + ", " +
				"Where: " + args[2] + ", " +
				"WhereArgs: " + args[3]);
		// TODO: dump 'where' args (array of strings for args[3])
	}
}

class Func_SQLite_INSERT extends FuncParent { 
	public void execute(Object... args) {
//		SQLiteDatabase db = (SQLiteDatabase) _resources;
		_l.logBasicInfo();
	
		_l.logParameter("Table", args[0]);
		_l.logParameter("Content Values", args[1]);
		
		_l.logFlush_I("-> " + "Table: " + args[0] + ", " +
						"ContentValues: " + args[2]);
		// TODO: dump this other array of strings
	}
}
