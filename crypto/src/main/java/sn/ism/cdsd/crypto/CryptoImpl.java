package sn.ism.cdsd.crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoImpl implements ICrypto {

    char[] hexTab= {'0', '1','2','3', '4', '5', '6',
     '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Override
     public String bytesToHexString(byte[] bytes) {
        StringBuilder sb=new StringBuilder();
        for (byte b : bytes) {
            char char1=hexTab[b&0xf];
            char char2=hexTab[(b>>4)&0xf];
            
            sb.append(char1); sb.append(char2);
                  
        }
        return sb.toString();
     }
    @Override
    public byte[] hexStringToBytes(String hexString) {
        int len=hexString.length();
        byte[] bytes=new byte[len/2];
        for (int i = 0; i < len; i+=2) {
            char char1=hexString.charAt(i);
            char char2=hexString.charAt(i+1);
            int b1=Integer.parseInt(char1+"", 16);
            int b2=Integer.parseInt(char2+"", 16);
            bytes[i/2]=(byte)(b1|(b2<<4));
        }
        return bytes;
    }

    @Override
    public void saveHexkey(Key hexKey, String filePath) {
        try {
             String contenu=bytesToHexString(hexKey.getEncoded());
             String algo=hexKey.getAlgorithm();
            FileOutputStream fos=new  FileOutputStream(filePath);
            PrintWriter out=new PrintWriter(fos);
            out.print(algo+"|"+contenu);
            out.close();
            fos.close();
            
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Key loadHexKey(String filePath, String password) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
            byte[] data=new byte[fis.available()];
            fis.read(data);
            String contenu=new String(data);
            String[] parts=contenu.split("\\|");
            String algo=parts[0];
            String hexKey=parts[1];
            byte[] bytes=hexStringToBytes(hexKey);
            Key key=new javax.crypto.spec.SecretKeySpec(bytes, algo);
            return key;
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
      return null;
    }

    @Override
    public SecretKey generateKey() {
        try {
            //SecureRandom sec=SecureRandom.getInstance("SHA1PRNG");
            ////sec.setSeed("graine".getBytes());
            KeyGenerator kg=KeyGenerator.getInstance("AES");
            kg.init(256);
            return kg.generateKey();
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    
    }

    @Override
    public SecretKey generateKey(String mdp) {
        // genrer une clé à partir d'un mot de passe seulment
        SecretKey clepbe=null;
        //on appelle Transforme le mot de passe en tableau de Char
        //MessageDigest.getInstance("SHA256");
        char[] password = mdp.toCharArray();
        PBEKeySpec pbe = new PBEKeySpec(password,
                "M2-CDSD-S3".getBytes(), 1024, 256);
        //on vide le tableau de char password
        mdp="";
	for (int j = 0; j < password.length; j++) {
		password[j] = 0;
	}
	try {  
         //on appelle le KDF: PBEKeySpec pour construire une clé
          SecretKeyFactory kdfFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
          SecretKey keyPBE = kdfFactory.generateSecret(pbe);// cle generique
          clepbe=new SecretKeySpec(keyPBE.getEncoded(), "AES");
			
       } catch (Exception e) {
	     // TODO Auto-generated catch block
	      e.printStackTrace();
       }
                
       return clepbe;


    }

    @Override
    public SecretKey generateKey(String password, String salt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public SecretKey generateKey(String password, String salt, String algorithm) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public SecretKey generateKey(String password, String salt, String algorithm, int iterationCount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public SecretKey generateKey(String password, String salt, String algorithm, int iterationCount, int keyLength) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String encrypt(String data, SecretKey key) {
        try {
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv="une chaine de 16".getBytes();
            IvParameterSpec ivspec=new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key,ivspec );
            
            byte[] enc=cipher.doFinal(data.getBytes());
            
            return bytesToHexString(enc);
            
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }

    @Override
    public String decrypt(String data, SecretKey key) {
        try {
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv="une chaine de 16".getBytes();
            IvParameterSpec ivspec=new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key,ivspec );
            byte[] dec=cipher.doFinal(hexStringToBytes(data));
            return new String(dec);
            
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }

    @Override
    public KeyPair generateKeyPair() {
        // genretate une paire de clé asymétrique RSA
        try {
            KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            return kpg.generateKeyPair();
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;        
        }

        }

    @Override
    public KeyPair generateKeyPair(String algorithm) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public KeyPair generateKeyPair(String algorithm, int keySize) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String encrypt(String data, PublicKey key) {
        
        try {
            Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] enc=cipher.doFinal(data.getBytes());
            return bytesToHexString(enc);
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String decrypt(String data, PrivateKey key) {
        try {
            Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dec=cipher.doFinal(hexStringToBytes(data));
            return new String(dec);
        } catch (Exception ex) {
            Logger.getLogger(CryptoImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    @Override
    public void encryptOrDecryptFile(String inputFile, String outputFile, SecretKey key, int mode) {
       // on lit sur inputFile, on chiffre puis on ecrit sur outputFile
       try {
        FileInputStream fis=new FileInputStream(inputFile);
        FileOutputStream fos=new FileOutputStream(outputFile);
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, key);
        CipherInputStream cis=new CipherInputStream(fis, cipher);
        byte[] buffer=new byte[1024*1024];
        int nbrBytesLus = cis.read(buffer);// le nombre de bytes rempli dans buffer
        while (nbrBytesLus!=-1) {
            fos.write(buffer, 0, nbrBytesLus);
            nbrBytesLus = cis.read(buffer);
        }
        fos.close();
        cis.close();
        fis.close();
       } catch (Exception e) {
        e.printStackTrace();
       }
    }
    
    @Override
    public void encryptOrDecryptFile(String inputFile, String outputFile, String password,int mode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'encryptFile'");
    }
   
    @Override
    public void hybridEncrypt(String inputFile, String outputFile, PublicKey publicKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hybridEncrypt'");
    }
    @Override
    public void hybridDecrypt(String inputFile, String outputFile, PrivateKey privateKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hybridDecrypt'");
    }
}
