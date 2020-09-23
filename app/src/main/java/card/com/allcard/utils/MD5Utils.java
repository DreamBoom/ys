package card.com.allcard.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 49927 on 2016/12/29.
 */

public class MD5Utils {
    /**
     * 加密
     *
     * @param plaintext 明文
     * @return ciphertext 密文
     */
    public final static String encrypt(String plaintext) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 简单测试
     *
     * @param args public static void main(String[] args) {
     *             String plaintext="123456";
     *             System.out.println("原文："+plaintext);
     *             <p>
     *             String ciphertext=MD5Util.encrypt(plaintext);
     *             System.out.println("加密成密文："+ciphertext);
     *             }
     */
    public static Cipher getCipher(int type,String seed){
        try {
            // 创建AES的KeyGenerator对象
            KeyGenerator  kgen = KeyGenerator.getInstance("AES");
            // 利用用户传入的字符串作为种子初始化出128位的key生产者不能使用
            //kgen.init(128, new SecureRandom(seed.getBytes()));因为在linux上会导致加解密出错,
            //见代码后边的说明。
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(seed.getBytes());
            kgen.init(128, secureRandom);
            //生成秘钥
            SecretKey secretKey = kgen.generateKey();
            //返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            byte[] enCodeFormat = secretKey.getEncoded();
            // 转换为AES专用密钥
            SecretKeySpec specKey = new SecretKeySpec(enCodeFormat, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化为加密模式Cipher.ENCRYPT_MODE或解密模式Cipher.DECRYPT_MODE的密码器
            cipher.init(type, specKey);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
