package com.signer.signer;


import java.math.BigInteger;
import java.security.SecureRandom;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.spongycastle.crypto.KeyGenerationParameters;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECConstants;
import org.spongycastle.math.ec.ECPoint;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;


import android.util.Log;

public class ManualCreateKey extends ECKeyPairGenerator
    
{
    ECDomainParameters  params;
    SecureRandom        random=new SecureRandom();
    //X9ECParameters xparams = SECNamedCurves.getByName("secp256k1");
    public void init(
            KeyGenerationParameters param)
        {
            ECKeyGenerationParameters  ecP = (ECKeyGenerationParameters)param;

            this.random = ecP.getRandom();
            this.params = ecP.getDomainParameters();

            if (this.random == null)
            {
                this.random = new SecureRandom();
            }
        }

    /**
     * Given the domain parameters this routine generates an EC key
     * pair in accordance with X9.62 section 5.2.1 pages 26, 27.
     */
   
    public AsymmetricCipherKeyPair getKeyPair(BigInteger d)
    {
        BigInteger n = params.getN();
        //Log.println(3, "alan","N:"+Utils.bytesToHexString(n.toByteArray()));
        int        nBitLength = n.bitLength();
       // Log.println(3, "alan","nBitlength:"+nBitLength);        

        ECPoint Q = params.getG().multiply(d);

        return new AsymmetricCipherKeyPair(
            new ECPublicKeyParameters(Q, params),
            new ECPrivateKeyParameters(d, params));
    }
    public String generateFirstHalf(){
    	String firstHalf=new String();
    	
    	for (int i=0;i<38;i++)
    	{
    		firstHalf+=String.valueOf(Math.abs(random.nextLong())%10);
    	}
//    	String last2digits=String.valueOf(Math.abs(random.nextLong())%64);
//    	if (last2digits.length()==1)
//    		last2digits="0"+last2digits;
//    	firstHalf+=last2digits;
    	return firstHalf;
    }
    public BigInteger generatePriv(String keyString){    	
    	BigInteger d=new BigInteger(keyString);
    	ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) getKeyPair(d).getPrivate();
    	BigInteger priv=privParams.getD();
    	return priv;
    }
    public BigInteger generatePersonalPriv(String keyString){
    	// create starting d(priv key
    	BigInteger d;    	
    	BigInteger n = params.getN();
        int nBitLength = n.bitLength();
    	do
        {
    		d=new BigInteger(nBitLength, random);
        }
        while (d.equals(ZERO)  || (d.compareTo(n.subtract(BigInteger.valueOf(10000000))) >= 0));
    	//create starting pubkey
    	
    	ECPoint Q = params.getG();//for test
    	ECPoint g = params.getG();//for test
    	//ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) getKeyPair(d).getPrivate();
    	//BigInteger priv=privParams.getD();
    	ECPoint temp=g;
    	
    	//ECPoint G1=new ECPoint.Fp(params.getCurve(), params.getG().getX(), params.getG().getY(), true).normalize();
    	ECPoint Q1= g;
    	//loop to find a d(privkey) that matches with the personalized header 
    	long i=1;//counting start
    	 String addressString;
    	 Log.println(3,"alan","gx2:"+g.multiply(TWO).toString());
    	 Log.println(3,"alan","gtwice:"+g.twice().toString());
    	 Log.println(3,"alan","g+g:"+g.add(g).toString());    	 
    	 Log.println(3,"alan","gthree:"+g.threeTimes().toString());
    	 Log.println(3,"alan","gx3:"+g.multiply(THREE).toString());
    	 Log.println(3,"alan","gtwiceplus:"+g.twicePlus(g).toString());
    	 Log.println(3,"alan","g+g+g:"+g.add(g).add(g).toString());
    	 Log.println(3,"alan","g+g+g normalize:"+g.add(g).add(g).normalize().toString());
    	 Log.println(3,"alan","g+g+g+g:"+g.add(g).add(g).add(g).toString());
    	 Log.println(3,"alan","g+g+g+g normalize:"+g.add(g).add(g).add(g).normalize().toString());
    	 Log.println(3,"alan","gx4:"+g.multiply(FOUR).toString());
    	 Log.println(3,"alan","gx3+g:"+g.multiply(THREE).add(g).toString());
    	 Q = params.getG().multiply(d);
    	do{
    		i++;
    		//Log.println(3,"alan","beforeQ:"+Q.toString());
    		Q=Q.add(g);
    		//Q1=Q.subtract(g);
    		//Log.println(3,"alan","reverseQ:"+Q1.toString());
    		//Q1=G1.add(Q1);
    		//temp=params.getG().multiply(d.add(BigInteger.valueOf((long)i)));
    		//Log.println(3,"alan","beforeQ0:"+temp.toString());
    		//temp=g.multiply(BigInteger.valueOf((long)i));    		
            //Log.println(3,"alan","afterQ:"+Q.toString());            
            //Log.println(3,"alan","Q0:"+temp.toString());
            //Q1=temp.subtract(g);
           // Log.println(3,"alan","reverseQ0:"+Q1.toString());
//            if (Q.equals(temp)){
//            	
//            }
    		//get the pubkey PO
    		ECPoint normed=(new ECPoint.Fp(params.getCurve(), Q.getX(), Q.getY(), true)).normalize();
            byte[] X = normed.getXCoord().getEncoded();            
            byte[] PO = new byte[X.length + 1];
            PO[0] = (byte)(normed.getCompressionYTilde() ? 0x03 : 0x02);
            System.arraycopy(X, 0, PO, 1, X.length);
            
            //get pubkey hash
            byte[] hash160 = Utils.sha256hash160(PO);
            //make address bytes
            byte[] addressBytes = new byte[25];
            addressBytes[0] = (byte) 0x00;
            System.arraycopy(hash160, 0, addressBytes, 1, 20);
            //byte[] check = Utils.doubleDigest(addressBytes, 0, 21);
            //System.arraycopy(check, 0, addressBytes, 21, 4);
            //calculate address String
            addressString= Base58.encode(addressBytes);
            //Log.println(3,"alan","fast addr:"+addressString);
            //ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) getKeyPair(d.add(BigInteger.valueOf((long)i))).getPrivate();
        	//BigInteger priv=privParams.getD();
        	//
    	}
    	// compare address String with header 
    	while(!addressString.substring(0,keyString.length()).equals(keyString));
    	Log.println(3,"alan","fast addr:"+addressString);
    	d=d.add(BigInteger.valueOf(i));
    	ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) getKeyPair(d).getPrivate();
    	BigInteger priv=privParams.getD();    	
    	Log.println(3,"alan","normal addr:"+new ECKey(priv).toAddress(SignerApplication.NETWORK_PARAMETERS).toString());
    	return priv;
    }
    public BigInteger parsePrivKey(String keyString){
    	long tempdigits;    	
    	byte[] keyBytes=new byte[32];
    	BigInteger d=new BigInteger(256,random);//Utils.bytesToHexString(keyBytes), 256);
    	for (int i=0;i<12;i++){
    		tempdigits=Long.parseLong(keyString.substring(i*3,i*3+2));
    		d.or(BigInteger.valueOf(tempdigits));
    		d.shiftLeft(10);
    	}
    	tempdigits=Long.parseLong(keyString.substring(36,37));
    	d.or(BigInteger.valueOf(tempdigits));
		d.shiftLeft(6);
		for (int i=0;i<13;i++){
    		tempdigits=Long.parseLong(keyString.substring(i*3+38,i*3+2+38));
    		d.or(BigInteger.valueOf(tempdigits));
    		d.shiftLeft(10);
    	}
		Log.println(3, "alan","parse priv key:"+d.toString());
    	return d;
    }
}
