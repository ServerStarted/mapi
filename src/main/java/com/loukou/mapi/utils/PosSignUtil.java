package com.loukou.mapi.utils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PosSignUtil {

	private static final Logger logger = Logger.getLogger(PosSignUtil.class);
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public static final String KEY_APP_ID = "appid";
	public static final String KEY_CITY_ID = "cityid";
	public static final String KEY_CVS_ID = "cvsid";
	public static final String KEY_MACHINE_ID = "machineid";
	public static final String KEY_TIME = "time";
	public static final String KEY_SIGN = "sign";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final long VALID_SPAN_MS = 60*1000; //TODO
	
    public static String getStringToSign(Map<String, String> params, String secret) {
        Object[] key_arr = params.keySet().toArray();
        Arrays.sort(key_arr);
        String str = StringUtils.EMPTY;

        for (Object key : key_arr) {
            String val = params.get(key);
            str += val;
        }
        return str+secret;
    }

    public static String getSign(Map<String, String> params, String secret) {
    	Map<String, String> signParams = new HashMap<String, String>(params);
    	signParams.remove(KEY_SIGN);
        String toSign = getStringToSign(signParams, secret);
        String sign = StringUtils.EMPTY;

	    sign = DigestUtils.md5Hex(toSign.getBytes());
	    
        return sign;
    }

    /**
     * 拼接url
     * @param params
     * @param secret
     * @return
     */
    public static String getQueryString(Map<String, String> params, String secret)
    {
    	Map<String, String> signParams = new HashMap<String, String>(params);
    	String toSign = getStringToSign(signParams, secret);
        String sign = StringUtils.EMPTY;

 	    sign = DigestUtils.md5Hex(toSign.getBytes());
 	    signParams.put("sign", sign);
 	    int size = signParams.size();
 	    String[] key_arr = new String[size];
 	    int i = 0;
 	    Iterator iter = signParams.entrySet().iterator();
 	    while(iter.hasNext())
 	    {
 	    	Map.Entry<String, String> entry = (Map.Entry<String, String>)iter.next();
 	    	key_arr[i] = entry.getKey()+"="+entry.getValue();
 	    	i++;
 	    }
 	    String queryString = StringUtils.join(key_arr,"&");
 	    return queryString;
    }
    
    public static void main(String[] args) {
    	Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "12");
		params.put("time", ""+new Date().getTime());
		System.out.println(PosSignUtil.getQueryString(params, "bcd"));
	}
}
