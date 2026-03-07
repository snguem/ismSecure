package sn.ism.cdsd.crypto;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

public interface ICrypto {



    String bytesToHexString(byte[] bytes);
    byte[] hexStringToBytes(String hexString);

    void saveHexkey(Key hexKey, String filePath);
    Key loadHexKey(String filePath, String password);


    /**
     * Génère une clé secrète aléatoire avec l'algorithme AES et une taille de 256 bits
     * @return
     */
    public SecretKey generateKey();
    /**
     * Génère une clé secrète avec l'algorithme AES et une taille de 256 bits à partir d'un mot de passe et d'un sel
     * @param password
     * @return
     */
    public SecretKey generateKey(String password);
    /**
     * Génère une clé secrète avec l'algorithme AES et une taille de 256 bits à partir d'un mot de passe et d'un sel
     * @param password
     * @param salt
     * @return
     */
    public SecretKey generateKey(String password, String salt);
    /**
     * Génère une clé secrète avec  une taille de 256 bits à partir d'un mot de passe et d'un sel
     * @param password
     * @param salt
     * @param algorithm
     * @return
     */
    public SecretKey generateKey(String password, String salt, String algorithm);
    /**
     * Génère une clé secrète avec une taille de 256 bits à partir d'un mot de passe et d'un sel
     * @param password
     * @param salt
     * @param algorithm
     * @param iterationCount
     * @return
     */
    public SecretKey generateKey(String password, String salt, String algorithm, int iterationCount);
    /**
     * Génère une clé secrète 
     * @param password
     * @param salt
     * @param algorithm
     * @param iterationCount
     * @param keyLength
     * @return
     */
    public SecretKey generateKey(String password, String salt, String algorithm, int iterationCount, int keyLength);


    

    /**
     * Chiffre une chaîne de caractères avec une clé secrète
     * @param data
     * @param key
     * @return la chaîne de caractères chiffrée au format Base64 
     */
    public String encrypt(String data, SecretKey key);
    /**
     * Déchiffre une chaîne de caractères chiffrée au format Base64 avec une clé secrète
     * @param data
     * @param key
     * @return la chaîne de caractères déchiffrée en UTF8
     */
    public String decrypt(String data, SecretKey key);

    public KeyPair generateKeyPair();
    public KeyPair generateKeyPair(String algorithm);
    public KeyPair generateKeyPair(String algorithm, int keySize);

    /**
     * Chiffre une chaîne de caractères avec une clé publique
     * @param data
     * @param key
     * @return la chaîne de caractères chiffrée au format Base64
     */
    public String encrypt(String data, PublicKey key);
    /**
     * Déchiffre une chaîne de caractères chiffrée au format Base64 avec une clé privée
     * @param data
     * @param key
     * @return la chaîne de caractères déchiffrée en UTF8
     */
    public String decrypt(String data, PrivateKey key);

    /**
     * Chiffre un fichier avec une clé secrète
     * @param inputFile le chemin du fichier à chiffrer
     * @param outputFile le chemin du fichier chiffré
     * @param key la clé secrète utilisée pour le chiffrement
     */
    public void encryptOrDecryptFile(String inputFile, String outputFile, SecretKey key, int mode);

    //public void decryptFile(String inputFile, String outputFile, SecretKey key);

    /**
     * Déchiffre un fichier avec un mot de passe qui sera converti en clé secrète par la méthode generateKey(String password)
     * @param inputFile le chemin du fichier à chiffrer
     * @param outputFile le chemin du fichier chiffré
     * @param password le mot de passe utilisé pour le chiffrement
     */
    public void encryptOrDecryptFile(String inputFile, String outputFile,String password, int mode );
   // public void decryptFile(String inputFile, String outputFile,String password );


    /**
     * Chiffre un fichier avec une clé publique et une clé secrète générée aléatoirement
     * on génère une clé secrète aléatoire pour chiffrer le fichier avec AES, puis on chiffre la clé secrète avec la clé publique RSA et on stocke la clé secrète chiffrée dans le fichier de sortie
     * le stockage se fait en hexadécimal au format: [clé secrète chiffrée en hexadécimal]:[données chiffrées en hexadécimal]
     * @param inputFile le chemin du fichier à chiffrer
     * @param outputFile le chemin du fichier chiffré
     * @param publicKey la clé publique utilisée pour le chiffrement de la clé secrète
     */
    public void hybridEncrypt(String inputFile, String outputFile, PublicKey publicKey);
    public void hybridDecrypt(String inputFile, String outputFile, PrivateKey privateKey);



}
