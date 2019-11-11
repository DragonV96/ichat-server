package com.ichat.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Create by glw
 * 2018/12/16 11:57
 * 自定义消息的加密解密
 */
public class AESUtils {

    private static final String KEY = "Cwq+cXsLku9VmJq8MOt4+CmW1NjW0QCe";   // 加解密秘钥
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";     // 算法名称/加密模式/数据填充方式
    private static final int KEYINIT = 256;     // 加密长度

    /**
     * 返回key的秘钥对象
     * @return key的对象
     */
    private static Key getKey(){
        return new SecretKeySpec(KEY.getBytes(), "AES");
    }

    /**
     * 将待编码的字节进行base64编码后转为String类型返回
     * @param content 待编码的字节入参
     * @return 编码后的base64字符串
     */
    public static String base64Encode(byte[] content){
        return Base64.encodeBase64String(content);
    }

    /**
     * 将待解码的字符串进行base64解码后转为byte类型返回
     * @param content 待解码的字符串入参
     * @return 解码后的base64字节
     * @throws Exception
     */
    public static byte[] base64Decode(String content) throws Exception{
        if (!StringUtils.isEmpty(content)) {
            return new BASE64Decoder().decodeBuffer(content);
        }
        return null;
    }


    //==============================加密===================================

    /**
     * AES加密
     * @param content 待加密消息
     * @return 加密后的字节内容
     * @throws Exception
     */
    private static byte[] aesEncryptToBytes(String content) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEYINIT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, AESUtils.getKey());
        byte[] result = cipher.doFinal(content.getBytes("utf-8"));

        return result;
    }

    /**
     * 将加密后的字节转为base64
     * @param content 待加密消息
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String aesEncrypt(String content) throws Exception{
        return base64Encode(aesEncryptToBytes(content));
    }


    //==============================解密===================================

    /**
     * AES解密
     * @param content 待解密字节消息
     * @return 解密后的字符串
     * @throws Exception
     */
    private static String aesDecryptToBytes(byte[] content) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEYINIT);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, AESUtils.getKey());
        byte[] result = cipher.doFinal(content);

        return new String(result);
    }

    /**
     * 将解密后的字节转为base64
     * @param content 待解密消息
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String aesDecrypt(String content) throws Exception{
        if (!StringUtils.isEmpty(content)) {
            return aesDecryptToBytes(base64Decode(content));
        }
        return null;
    }
}
