package com.introspy.core;

class Intro_CRYPTO_KEY extends Intro_CRYPTO {
	
}

class Intro_GET_KEY extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		byte[] key = (byte[]) args[0];
		if (key != null) {
			String skey = StringHelper.getReadableByteArr(key);
			_l.logParameter("Key", skey);
			_l.logParameter("Algo", args[1]);
			_l.logBasicInfo();
			_l.logFlush_I("-> Key: ["+skey+"], algo: "+args[1]);
		}
	}
}

class Intro_CRYPTO_KEYSTORE_HOSTNAME extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		
		_l.logBasicInfo();
		// arg2 is the passcode for this trustore
		if (args[2] != null) {
			String passcode = 
					StringHelper.getReadableByteArr((byte[]) args[2]);
			_l.logParameter("Passcode", args[2]);
			_l.logFlush_I("-> TrustStore passcode: " + passcode);
		}
	}
}

class Intro_CRYPTO_KEYSTORE extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		
		_l.logBasicInfo();
		// arg1 is the passcode for the trustore
		if (args[1] != null) {
			String passcode = 
					StringHelper.getReadableByteArr((byte[]) args[1]);
			_l.logParameter("Passcode", args[1]);
			_l.logFlush_I("-> TrustStore passcode: " + passcode);
		}
	}
}

class Intro_CRYPTO_PBEKEY extends Intro_CRYPTO_KEY { 
	public void execute(Object... args) {
		
		_l.logBasicInfo();

		String passcode = new String((char[])args[0]);
		String salt = null;
		int iterationCount = -1;
		if (args.length >= 2 && args[1] != null) {
			salt = 
				StringHelper.byteArrayToReadableStr((byte[])args[1]);
			iterationCount = (Integer)args[2];
			_l.logParameter("Passcode", passcode);
			_l.logParameter("Salt", salt);
			_l.logParameter("Iterations", iterationCount);
			// _l.logReturnValue("Key", _hookInvoke(args));
			_l.logFlush_I("-> Passcode: [" + passcode + "], Salt: [" + salt + 
					"], iterations: " + iterationCount + "");
		}
		else {
			_l.logParameter("Passcode", passcode);
			_l.logFlush_I("Passcode: [" + passcode + "]");
		}
	}
}
