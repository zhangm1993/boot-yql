package com.ideal.manage.dsp.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;


public class ThreeDESUtils {

    private static final String key = "ICHuQplJ0YR9l7XeVNKi6FMn";
    private static final String Algorithm = "DESede";

    /**
     * 根据字符串生成密钥字节数组
     * @param key
     * @return
     */
    public static byte[] hex(String key){
        String f = DigestUtils.md5Hex(key);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i=0;i<24;i++){
            enk[i] = bkeys[i];
        }
        return enk;
    }

    /**
     * 3DES加密方式
     * @param srcStr
     * @return
     */
    public static String encode3Des(String srcStr){

        try {
            //根据 key与Algorithm 生成密钥对象
            SecretKey deskey = new SecretKeySpec(hex(key), Algorithm);

            //加密初始化
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);

            return new BASE64Encoder().encodeBuffer(c1.doFinal(srcStr.getBytes())).trim();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES解密方式
     * @param desStr
     * @return
     */
    public static String decode3Des(String desStr){

        try {
            //根据 key与Algorithm 生成密钥对象
            SecretKey deskey = new SecretKeySpec(hex(key), Algorithm);

            //解密初始化
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);

            return new String(c1.doFinal(new BASE64Decoder().decodeBuffer(desStr)));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密 生成签名
     * @param input
     * @return
     */
    public static String md5Sign(String input) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes("UTF-8"));

            byte[] byteArray = messageDigest.digest();
            StringBuffer md5StrBuff = new StringBuffer();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                    md5StrBuff.append("0").append(
                            Integer.toHexString(0xFF & byteArray[i]));
                else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            return md5StrBuff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        //认证码 authCode
        String authCode = "b2173799bc2ea29f84c99d8f64246d93aa0212986a795d3f";
        //MD5 key
        String md5Key = "GPhKt7sh4dxQQZZkINGFtefRKNPyAj8S00cgAwtRyy0ufD7alNC28xCBKpa6IU7u54zzWSAv4PqUDKMgpOnM7fucO1wuwMi4RgPAnietmqYIhHXZ3TqTGKNzkxA55qYH";

        //对JSON参数进行加密
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("callNumber", "18052029394");
        jsonObject.put("accountDate", "201501");
        String param = ThreeDESUtils.encode3Des(jsonObject.toJSONString());
        System.out.println("param:" + param);

        //对param解密
        String param_de = ThreeDESUtils.decode3Des(param);
        System.out.println("解密param:" + param_de);

        JSONObject param_json = JSON.parseObject(param_de);
        System.out.println("param_json:" + param_json);

        //MD5签名 根据 (认证码 + param + MD5key)生成MD5签名
        String sign = ThreeDESUtils.md5Sign(authCode + param + md5Key);
        System.out.println("MD5签名:" + sign);

        //post请求参数
        JSONObject jsonData = new JSONObject();
        jsonData.put("authCode", authCode);
        jsonData.put("param", param);
        jsonData.put("sign", sign);
        System.out.println("post请求参数" + jsonData.toJSONString());

        //TODO   发送post请求.请求接口
        String url = "http://localhost:8080/smsPort/creditScore.do";
        String result1 = HttpUtils.post(url, jsonData.toString(), "application/json;charset=UTF-8");
        System.out.println("请求响应数据----" + result1);
    }
}
