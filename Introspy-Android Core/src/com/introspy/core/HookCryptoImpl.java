package com.introspy.core;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Stack;

import javax.crypto.Cipher;

import android.util.Log;


class Func_CRYPTO extends FuncParent {
	static Stack<byte[]> IVList = new Stack<byte[]>();
	
	public void execute(Object... args) {
		if (_resources != null) {
			boolean warning = false;
			_l.logBasicInfo();			
			Cipher cipher = (Cipher) _resources;
			try {
				String algo = cipher.getAlgorithm();
				// input				
				byte[] data = (byte[]) args[0];
				if (data == null) { // when no args to doFinal (used update)
					String i_sdata = new String(data);
					if (StringHelper.isItReadable(i_sdata)) {
						i_sdata = StringHelper.filter(i_sdata);
						_l.logParameter("input (Encrypt)", i_sdata);
						_l.logLine("-> ENCRYPT: [" + i_sdata + "]");
					}
					else {
						String sdata = StringHelper.byteArrayToB64(data);
						sdata = StringHelper.filter(sdata);
						_l.logLine("-> Input data is not in a readable format, base64 version:");
						_l.logLine("-> ["+ sdata +"]");
						_l.logParameter("Output (converted to b64)", sdata);
					}
				}
				
				//output
				String o_sdata = null;
//				if (cipher == _lastCipher && _lastMode == Cipher.DECRYPT_MODE)
					try {
						data  = (byte[]) _old.invoke(_resources, args);
						o_sdata = new String(data);
						if (StringHelper.isItReadable(o_sdata)) {
							o_sdata = StringHelper.filter(o_sdata);
							_l.logParameter("Ouput (Decrypt)", o_sdata);
							_l.logLine("-> DECRYPT: [" + o_sdata + "]");
						}
						else {
							String sdata = StringHelper.byteArrayToB64(data);
							sdata = StringHelper.filter(sdata);
							_l.logLine("-> Input data is not in a readable format, base64 version:");
							_l.logLine("->["+ sdata +"]");
							_l.logReturnValue("Output (converted to b64)", sdata);
						}
					} catch (Throwable e) {
						Log.i("IntrospyLog", "doFinal function failed: "+e);
					}
//				} else {
//				}

				String out = "";
				if (algo != null) {
					out = "-> Algo: " + algo;
					_l.logParameter("Algo", algo);
					if (cipher.getAlgorithm().contains("ECB")) {
						warning = true;
						out += " - !!! ECB used";
					}
				}
				// dump some params
				if (cipher.getIV() != null) {
					String iv = StringHelper.getReadableByteArr(cipher.getIV());
					out += "; IV: " + iv;
					_l.logParameter("IV", iv);

					if (cipher.getIV()[0] == 0) {
						Log.w("Introspy", "!!! IV of 0");
						warning = true;
					}
					else {
						// check if this IV has already been used
						if (IVList.contains(cipher.getIV())) {
							out  += " - !!! Static IV";
							warning = true;
						}
						IVList.push(cipher.getIV());
						// keep a list of a max of 10 IVs
						if (IVList.size() > 10)
							IVList.pop();
					}
				}
				if (cipher.getParameters() != null && ApplicationConfig.g_debug)
					_l.logLine("Parameters: " + cipher.getParameters());
				
				if (algo != null || cipher.getIV() != null) {
					if (warning)
						_l.logFlush_W(out);
					else
						_l.logFlush_I(out);
				}

				
			} catch (Exception e) {
				Log.w("IntrospyLog", "Error in Func_CRYPTO: " + e);
				if (cipher == null)
					Log.w("IntrospyLog", "Cipher is null");
			}
		}
	}
}

class Func_CRYPTO_INIT extends FuncParent { 
	public void execute(Object... args) {
		if (_resources != null) {
			try {
				_lastCipher = (Cipher) _resources;
				
				// get the mode
				Integer mode = _lastMode = (Integer) args[0];
				String smode;
				switch (mode) {
					case Cipher.DECRYPT_MODE: 
						smode = "DECRYPT_MODE";
						break;
					case Cipher.ENCRYPT_MODE: 
						smode = "ENCRYPT_MODE";
						break;
					default: 
						smode = "???";
				}
				
				_l.logBasicInfo();
				
				String out = "-> Mode: " + smode;
				
				// get the key
				Key key = (Key) args[1];
				String skey = "";
				if (key != null) {
					out += ", Key format: " + key.getFormat() + ", Key: ";
					skey = new String(key.getEncoded());
					skey = StringHelper.getReadableByteArr(key.getEncoded());
					out += skey;
				}
				_l.logParameter("Mode", smode);
				_l.logParameter("Key", skey);
				_l.logParameter("Key Format", key.getFormat());

				_l.logFlush_I(out);
				
			} catch (Exception e) {
				Log.w("IntrospyLog", "Error in Func_CRYPTO: " + e);
			}
		}
	}
}


class Func_GET_HASH extends FuncParent { 
	public void execute(Object... args) {		
		try {
			byte[] data  = (byte[]) _old.invoke(_resources, args);
			MessageDigest dg = (MessageDigest) _resources;
			_l.logBasicInfo();
			
			String sdata = StringHelper.getReadableByteArr(data);
			
			_l.logReturnValue("Data", sdata);
			_l.logParameter("Algo", dg);
			
			_l.logFlush_I("-> Algo: " + dg + ", Data: " + sdata);
		} catch (Throwable e) {
			Log.i("IntrospyLog", "Error in Fun_GET_HASH" + e);
		}
	}
}
