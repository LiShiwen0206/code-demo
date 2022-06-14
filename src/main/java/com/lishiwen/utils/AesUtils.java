package com.lishiwen.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * ============<br/>
 * AES密码工具
 * 部分方法
 * <br/>============
 *
 * @author : lishiwen
 * @Class_name : AesUtils
 * @Date : 2022/6/10
 */
@Slf4j
public class AesUtils {

    private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int INTERNAL_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String BASE64_CHARSET = "UTF-8";
    private static final String ALGORITHM = "AES";
    private static String APP_KEY = "45jd4ju0wtxgq6nuuz6snroorrkrkmf9";
    private static String SALT = "42emqoxnzwt48cie8d96xqwo2tleorhn";

    private static String IV = "6zc3wh54odgvk3bm";
    private static final String TRANS_FORMATION = "AES/CBC/PKCS5PADDING";


    /**
     * ============<br/>
     * PBEKeySpec
     * 加密含盐
     * <br/>============
     *
     * @param strToEncrypt:
     * @return : {@link String}
     * @author : lishiwen
     * @Date : 2022/6/10
     */
    public static String encrypt(String strToEncrypt) {
        try {
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(APP_KEY.toCharArray(), SALT.getBytes(), INTERNAL_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANS_FORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(BASE64_CHARSET)));
        } catch (Exception e) {
            log.warn("Error while encrypting,strToEncrypt: {}", strToEncrypt, e);
        }

        return null;
    }

    /**
     * ============<br/>
     * 解密含盐
     * <br/>============
     *
     * @param strToDecrypt:
     * @return : {@link String}
     * @author : lishiwen
     * @Date : 2022/6/10
     */
    public static String decrypt(String strToDecrypt) {
        try {
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(APP_KEY.toCharArray(), SALT.getBytes(), INTERNAL_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANS_FORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.warn("Error while decrypting,strToDecrypt: {}", strToDecrypt, e);
        }
        return null;
    }

    public static String encryptNotSalt(String content) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            SecretKey secretKey = new SecretKeySpec(APP_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.info("encryptNotSalt error is ", e);
        }
        return null;

    }

    public static String decryptNotSalt(String base64Content) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(APP_KEY.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decode = Base64.getDecoder().decode(base64Content);
            byte[] encrypted = cipher.doFinal(decode);
            return new String(encrypted);
        } catch (Exception e) {
            log.info("decryptNotSalt error is ", e);
        }
        return null;

    }

    /**
     * ============<br/>
     * aes文件加密
     * <br/>============
     *
     * @param file:
     * @return : {@link String}
     * @author : za-lishiwen
     * @Date : 2022/6/13
     */
    public static String encryptFile(File file) {
        try {
            // 初期ベクトル準備
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(APP_KEY.toCharArray(), SALT.getBytes(), INTERNAL_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANS_FORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            InputStream in = new FileInputStream(file);
            CipherInputStream cis = new CipherInputStream(in, cipher);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = cis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] bytes = baos.toByteArray();
            cis.close();
            baos.close();
            in.close();
            return java.util.Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.warn("Error while encryptFile encrypting,fileName is {}", file.getName());
        }

        return null;
    }

    /**
     * ============<br/>
     * aes文件解密
     * <br/>============
     *
     * @param strToDecrypt : 需解密的字符串
     * @param filePath:    输出位置
     * @return : {@link String}
     * @author : za-lishiwen
     * @Date : 2022/6/13
     */
    public static String decryptFile(String strToDecrypt, String filePath) {
        try {
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            KeySpec spec = new PBEKeySpec(APP_KEY.toCharArray(), SALT.getBytes(), INTERNAL_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANS_FORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            Files.write(Paths.get(filePath), bytes, StandardOpenOption.CREATE);
            return filePath;
        } catch (Exception e) {
            log.warn("Error while decryptFile decrypting,strToDecrypt is {}", strToDecrypt);
        }
        return null;
    }

    public static void main(String[] args) {
        //
        //String encrypt = encrypt("test");
        //String encryptNotSalt = encryptNotSalt("test");
        //Provider[] providers = Security.getProviders();
        //log.info(JSON.toJSONString(providers));
        //log.info("encrypt is {}", encrypt);
        //log.info("encryptNotSalt is {}", encryptNotSalt);


        String fileName = "/Users/test/Downloads/CONTRACT_20220607_00001_20220608103450552.csv";
        File sourceFile = new File(fileName);
        String s = encryptFile(sourceFile);
        log.info(s);
        //
        //
        //String fileOutPath = "/Users/test/Downloads/abc.csv";
        //String strToDecrypt = "";
        //String s1 = decryptFile(strToDecrypt, fileOutPath);
    }
}
