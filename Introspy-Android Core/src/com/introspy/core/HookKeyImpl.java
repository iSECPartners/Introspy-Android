package com.introspy.core;

class Func_GET_KEY extends FuncParent { 
	public void execute(Object... args) {
		byte[] key = (byte[]) args[0];
		if (key != null) {
			String skey = new String(key);
			if (!StringHelper.isItReadable(skey))
				skey = StringHelper.byteArrayToB64(key);
			_l.logParameter("Key", skey);
			_l.logParameter("Algo", args[1]);
			_l.logBasicInfo();
			_l.logFlush_I("-> Key: ["+skey+"], algo: "+args[1]);
		}
	}
}

class Func_CRYPTO_KEYSTORE_HOSTNAME extends FuncParent { 
	public void execute(Object... args) {
		
		_l.logBasicInfo();
		// arg1 is the passcode for the trustore
		if (args[2] != null) {
			String passcode = (String) args[2];
			_l.logParameter("Passcode", args[2]);
			_l.logFlush_I("-> TrustStore passcode: " + passcode);
		}
	}
}

class Func_CRYPTO_KEYSTORE extends FuncParent { 
	public void execute(Object... args) {
		
		_l.logBasicInfo();
		// arg1 is the passcode for the trustore
		if (args[1] != null) {
			_l.logParameter("Passcode", args[1]);
			String passcode = (String) args[1];
			_l.logFlush_I("-> TrustStore passcode: " + passcode);
		}
	}
}

class Func_CRYPTO_PBEKEY extends FuncParent { 
	public void execute(Object... args) {
		
		_l.logBasicInfo();

		String passcode = new String((char[])args[0]);
		String salt = null;
		int iterationCount = -1;
		if (args.length >= 2 && args[1] != null) {
			salt = new String((byte[])args[1]);
			if (!StringHelper.isItReadable(salt))
				salt = StringHelper.byteArrayToB64((byte[])args[1]);
			iterationCount = (Integer)args[2];
			_l.logParameter("Passcode", passcode);
			_l.logParameter("Salt", salt);
			_l.logParameter("Iterations", iterationCount);
			_l.logFlush_I("-> Passcode: [" + passcode + "], Salt: [" + salt + 
					"], iterations: " + iterationCount);
		}
		else {
			_l.logParameter("Passcode", passcode);
			_l.logFlush_I("Passcode: [" + passcode + "]");
		}
	}
}
