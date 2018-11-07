package com.zhujiu.scale;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhujiu.util.DBUtil;

public class JDBCAccess {

	public static void main(String[] args) {
		// url表示需要连接的数据源的位置，此时使用的是JDBC-ODBC桥的连接方式，url是"jdbc:odbc:数据源名"
		String url = "jdbc:Access:///G:/SysDb.mdb";
		queryWeightData(url,"1-20180605150125","select * from WeightDataSubmit");
		
/*		try {	
			Class.forName("com.hxtt.sql.access.AccessDriver");
			Connection conn = DriverManager.getConnection(url);
			Statement stat = conn.createStatement();
			String sql = "select * from WeightDataSubmit";
			ResultSet rs = stat.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(3));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}*/
	}

	@SuppressWarnings("rawtypes")
	public static List<WeightData> queryWeightData(String url, String lastFlowID,String psql) {
		List<WeightData> datas = new ArrayList<WeightData>();
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver");
			Connection conn = DriverManager.getConnection(url);
			Statement stat = conn.createStatement();
			String sql = psql+" where WFlowID > '"+lastFlowID+"'"+" order by WFlowID";
			ResultSet rs = stat.executeQuery(sql);
			List<Map> lst = DBUtil.convertList(rs);
			for(Map mr : lst){
				WeightData data = new WeightData();
				data.setWflowid(DBUtil.toString(mr.get(WeightData.FN_WFlowID)));
				data.setSerial(DBUtil.toInteger(mr.get(WeightData.FN_Serial)));
				data.setTruckid(DBUtil.toString(mr.get(WeightData.FN_TruckID)));
				data.setZc_ct(DBUtil.toInteger(mr.get(WeightData.FN_ZC_CT)));
				data.setWeightgross(DBUtil.toFloat(mr.get(WeightData.FN_WeightGross)));
				data.setZc_time(DBUtil.toDate(mr.get(WeightData.FN_ZC_Time)));
				data.setZc_weighman(DBUtil.toString(mr.get(WeightData.FN_ZC_Weighman)));
				data.setKc_ct(DBUtil.toInteger(mr.get(WeightData.FN_KC_CT)));
				data.setWeighttare(DBUtil.toFloat(mr.get(WeightData.FN_WeightTare)));
				data.setKc_time(DBUtil.toDate(mr.get(WeightData.FN_KC_Time)));
				data.setKc_weighman(DBUtil.toString(mr.get(WeightData.FN_KC_Weighman)));
				data.setWeightdeduct(DBUtil.toString(mr.get(WeightData.FN_WeightDeduct)));
				data.setGoodsname(DBUtil.toString(mr.get(WeightData.FN_GoodsName)));
				data.setSourcename(DBUtil.toString(mr.get(WeightData.FN_sourceName)));
				data.setIncomename(DBUtil.toString(mr.get(WeightData.FN_incomeName)));
				data.setTransname(DBUtil.toString(mr.get(WeightData.FN_TransName)));
				datas.add(data);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		return datas;
	}

	


}
