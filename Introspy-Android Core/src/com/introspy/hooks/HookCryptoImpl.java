package com.introspy.hooks;
import com.introspy.core.IntroHook;

import java.security.Key;
import java.util.Stack;

import javax.crypto.Cipher;

import android.util.Log;

import com.introspy.core.ApplicationConfig;

class Intro_CRYPTO extends IntroHook {
	protected static Cipher 	_lastCipher;
	protected static Integer 	_lastMode;
}

class Intro_CRYPTO_FINAL extends Intro_CRYPTO {
	static Stack<byte[]> IVList = new Stack<byte[]>();
	
	public void execute(Object... args) {
		if (_resources != null) {
			boolean warning = false;
			_logBasicInfo();			
			Cipher cipher = (Cipher) _resources;
			try {
				String algo = cipher.getAlgorithm();
				// input				
				byte[] data = (byte[]) args[0];
				if (data != null) { // when no args to doFinal (used update)
					String i_sdata = new String(data);
					if (_isItReadable(i_sdata)) {
						i_sdata = _byteArrayToReadableStr(data);
						_logParameter("input (Encrypt)", i_sdata);
						_logLine("-> ENCRYPT: [" + i_sdata + "]");
					}
					else {
						String sdata = _byteArrayToB64(data);
						_logLine("-> Input data is not in a readable format, " +
									"base64: ["+ sdata +"]");
						_logParameter("Output (converted to b64)", sdata);
					}
				}
				
				//output
				String o_sdata = null;
//				if (cipher == _lastCipher && _lastMode == Cipher.DECRYPT_MODE)
					try {
						data  = (byte[]) _hookInvoke(args);
						o_sdata = new String(data);
						if (_isItReadable(o_sdata)) {
							o_sdata = _byteArrayToReadableStr(data);
							_logParameter("Ouput (Decrypt)", o_sdata);
							_logLine("-> DECRYPT: [" + o_sdata + "]");
						}
						else {
							String sdata = _byteArrayToB64(data);
							_logLine("-> Output data is not in a readable format," +
										" base64: ["+ sdata +"]");
							_logReturnValue("Output (converted to b64)", sdata);
						}
					} catch (Throwable e) {
						Log.i(_TAG_ERROR, "doFinal function failed: "+e);
					}
//				} else {
//				}

				String out = "";
				if (algo != null) {
					out = "-> Algo: " + algo;
					_logParameter("Algo", algo);
					if (cipher.getAlgorithm().contains("ECB")) {
						warning = true;
						out += " - !!! ECB used. ECB mode is broken and should not be used.";
					}
				}
				// dump some params
				if (cipher.getIV() != null) {
					String iv = _getReadableByteArr(cipher.getIV());
					out += "; IV: " + iv;
					_logParameter("IV", iv);

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
					_logLine("Parameters: " + cipher.getParameters());
				
				if (algo != null || cipher.getIV() != null) {
					if (warning)
						_logFlush_W(out);
					else
						_logFlush_I(out);
				}

				
			} catch (Exception e) {
				Log.w(_TAG_ERROR, "Error in Intro_CRYPTO: " + e);
				if (cipher == null)
					Log.w(_TAG_ERROR, "Cipher is null");
			}
		}
		else {
			Log.w(_TAG_ERROR, "Error in Intro_CRYPTO: resource is null");
		}
	}
}

class Intro_CRYPTO_INIT extends Intro_CRYPTO { 
	public void execute(Object... args) {
		// let's not care about init since we are hooking 
		// the key class already
		// BUT it can be useful to get a state of the mode 
		// if needed later
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
				
				_logBasicInfo();
				
				String out = "-> Mode: " + smode;
				
				// get the key
				Key key = (Key) args[1];
				String skey = "";
				if (key != null) {
					skey = _getReadableByteArr(key.getEncoded());
					out += ", Key format: " + key.getFormat() + 
							", Key: [" + skey + "]";
				}
				_logParameter("Mode", smode);
				_logParameter("Key", skey);
				_logParameter("Key Format", key.getFormat());

				_logFlush_I(out);
				
			} catch (Exception e) {
				Log.w(_TAG_ERROR, "Error in Intro_CRYPTO: " + e);
			}
		}
	}
}

