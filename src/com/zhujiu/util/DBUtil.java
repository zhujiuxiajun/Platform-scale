package com.zhujiu.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	/**
	 * 将rs集合，转换成list
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map> convertList(ResultSet rs) throws SQLException {
		List<Map> list = new ArrayList<Map>();
		ResultSetMetaData md = rs.getMetaData();// 获取键名
		int columnCount = md.getColumnCount();// 获取行的数量
		while (rs.next()) {
			Map rowData = new HashMap();// 声明Map
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i).toUpperCase(), rs.getObject(i));// 获取键名及值
			}
			list.add(rowData);
		}
		return list;
	}

	public static String toString(Object o) {
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	public static Integer toInteger(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return Integer.parseInt(o.toString());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Float toFloat(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return Float.parseFloat(o.toString());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Long toDate(Object o) {
		if (o == null) {
			return null;
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			return formatter.parse(o.toString()).getTime();
		} catch (Exception e) {
			return null;
		}
	}

}
