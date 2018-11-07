package com.zhujiu.util;

import org.apache.commons.lang.StringUtils;

import com.zhujiu.scale.RESTFulGetUtil;

public class ServiceUtil {
	public static boolean hello(String url) {
		try {
			String reponse = RESTFulGetUtil.doGet(url);
			if (StringUtils.equals("OK", reponse)) {				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
