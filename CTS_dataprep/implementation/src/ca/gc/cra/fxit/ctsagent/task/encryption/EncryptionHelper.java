/*
 * NOTICE: This source code belongs solely to the copyright holder.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from
 * CRA, Government of Canada.
 * 
 */
package ca.gc.cra.fxit.ctsagent.task.encryption;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ca.gc.cra.fxit.ctsagent.model.PackageInfo;
import ca.gc.cra.fxit.ctsagent.task.AbstractTask;
import ca.gc.cra.fxit.ctsagent.util.Utils;
import ca.gc.cra.fxit.ctsagent.util.Constants;


/**
 * 
 * 
 * <a href="EncryptionAbstractStep.java.html"> <em>View Source</em> </a>
 * 
 * @author 
 */
public class EncryptionHelper 
{

	private static final Logger log = Logger.getLogger(EncryptionHelper.class);
	
	private PackageInfo packageInfo;
	
	public EncryptionHelper(PackageInfo p){
		this.packageInfo = p;
	}

	/**
	 *
	 */
	public int encrypt() {
		int result = -1;
		//
		return result;
	}

	/**
	 * 
	 */
	public int decrypt() 
				//	throws Exception 
					{
		int status = -1;
		return status;
	}

}
