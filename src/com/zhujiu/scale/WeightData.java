package com.zhujiu.scale;

/**
 * 
 * @author Administrator
 *
 */
public class WeightData {
	public static String FN_WFlowID="WFLOWID";
	public static String FN_Serial="SERIAL";
	public static String FN_TruckID="TRUCKID";
	public static String FN_ZC_CT="ZC_CT";
	public static String FN_ZC_WPID1="ZC_WPID1";
	public static String FN_ZC_WPID2="ZC_WPID2";
	public static String FN_ZC_WPID3="ZC_WPID3";	
	public static String FN_ZC_WPID4="ZC_WPID4";
	public static String FN_WeightGross="WEIGHTGROSS";
	public static String FN_ZC_Time="ZC_TIME";
	public static String FN_ZC_Weighman="ZC_WEIGHMAN";	
	public static String FN_KC_CT="KC_CT";
	public static String FN_KC_WPID1="KC_WPID1";		
	public static String FN_KC_WPID2="KC_WPID2";
	public static String FN_KC_WPID3="KC_WPID3";	
	public static String FN_KC_WPID4="KC_WPID4";	
	public static String FN_WeightTare="WEIGHTTARE";
	public static String FN_KC_Time="KC_TIME";
	public static String FN_KC_Weighman="KC_WEIGHMAN";		
	public static String FN_WeightDeduct="WEIGHTDEDUCT";
	public static String FN_GoodsName="GOODSNAME";
	public static String FN_sourceName="SOURCENAME";
	public static String FN_incomeName="INCOMENAME";
	public static String FN_UserField1="USERFIELD1";
	public static String FN_UserField2="USERFIELD2";
	public static String FN_UserField3="USERFIELD3";
	public static String FN_WeightMode="WEIGHTMODE";
	public static String FN_Price="PRICE";
	public static String FN_Currency1="CURRENCY1";
	public	static String FN_TransName="TRANSNAME";
	public static String FN_ModiBZ="MODIBZ";
	public static String FN_PrintNumber="PRINTNUMBER";
	public static String FN_ZSerial="ZSERIAL";
	
	//Á÷³ÌºÅ
	private String wflowid;
	private Integer serial;
	private String truckid;
	private Integer zc_ct;
	private String zc_wpid1;
	private String zc_wpid2;
	private String zc_wpid3;
	private String zc_wpid4;
	private float weightgross;
	private Long zc_time;
	private String zc_weighman;
	private Integer kc_ct;
	private String kc_wpid1;
	private String kc_wpid2;
	private String kc_wpid3;
	private String kc_wpid4;
	private float weighttare;
	private Long kc_time;
	private String kc_weighman;
	private String weightdeduct;
	private String goodsname;
	private String sourcename;
	private String incomename;
	private String userfield1;
	private String userfield2;
	private String userfield3;
	private Integer weightmode;
	private float price;
	private float currency1;
	private	String transname;
	private String modibz;
	private Integer printnumber;
	private String zserial;
	public String getWflowid() {
		return wflowid;
	}
	public void setWflowid(String wflowid) {
		this.wflowid = wflowid;
	}
	public Integer getSerial() {
		return serial;
	}
	public void setSerial(Integer serial) {
		this.serial = serial;
	}
	public String getTruckid() {
		return truckid;
	}
	public void setTruckid(String truckid) {
		this.truckid = truckid;
	}
	public Integer getZc_ct() {
		return zc_ct;
	}
	public void setZc_ct(Integer zc_ct) {
		this.zc_ct = zc_ct;
	}
	public String getZc_wpid1() {
		return zc_wpid1;
	}
	public void setZc_wpid1(String zc_wpid1) {
		this.zc_wpid1 = zc_wpid1;
	}
	public String getZc_wpid2() {
		return zc_wpid2;
	}
	public void setZc_wpid2(String zc_wpid2) {
		this.zc_wpid2 = zc_wpid2;
	}
	public String getZc_wpid3() {
		return zc_wpid3;
	}
	public void setZc_wpid3(String zc_wpid3) {
		this.zc_wpid3 = zc_wpid3;
	}
	public String getZc_wpid4() {
		return zc_wpid4;
	}
	public void setZc_wpid4(String zc_wpid4) {
		this.zc_wpid4 = zc_wpid4;
	}
	public float getWeightgross() {
		return weightgross;
	}
	public void setWeightgross(float weightgross) {
		this.weightgross = weightgross;
	}
	public Long getZc_time() {
		return zc_time;
	}
	public void setZc_time(Long zc_time) {
		this.zc_time = zc_time;
	}
	public String getZc_weighman() {
		return zc_weighman;
	}
	public void setZc_weighman(String zc_weighman) {
		this.zc_weighman = zc_weighman;
	}
	public Integer getKc_ct() {
		return kc_ct;
	}
	public void setKc_ct(Integer kc_ct) {
		this.kc_ct = kc_ct;
	}
	public String getKc_wpid1() {
		return kc_wpid1;
	}
	public void setKc_wpid1(String kc_wpid1) {
		this.kc_wpid1 = kc_wpid1;
	}
	public String getKc_wpid2() {
		return kc_wpid2;
	}
	public void setKc_wpid2(String kc_wpid2) {
		this.kc_wpid2 = kc_wpid2;
	}
	public String getKc_wpid3() {
		return kc_wpid3;
	}
	public void setKc_wpid3(String kc_wpid3) {
		this.kc_wpid3 = kc_wpid3;
	}
	public String getKc_wpid4() {
		return kc_wpid4;
	}
	public void setKc_wpid4(String kc_wpid4) {
		this.kc_wpid4 = kc_wpid4;
	}
	public float getWeighttare() {
		return weighttare;
	}
	public void setWeighttare(float weighttare) {
		this.weighttare = weighttare;
	}
	public Long getKc_time() {
		return kc_time;
	}
	public void setKc_time(Long kc_time) {
		this.kc_time = kc_time;
	}
	public String getKc_weighman() {
		return kc_weighman;
	}
	public void setKc_weighman(String kc_weighman) {
		this.kc_weighman = kc_weighman;
	}
	public String getWeightdeduct() {
		return weightdeduct;
	}
	public void setWeightdeduct(String weightdeduct) {
		this.weightdeduct = weightdeduct;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getSourcename() {
		return sourcename;
	}
	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}
	public String getIncomename() {
		return incomename;
	}
	public void setIncomename(String incomename) {
		this.incomename = incomename;
	}
	public String getUserfield1() {
		return userfield1;
	}
	public void setUserfield1(String userfield1) {
		this.userfield1 = userfield1;
	}
	public String getUserfield2() {
		return userfield2;
	}
	public void setUserfield2(String userfield2) {
		this.userfield2 = userfield2;
	}
	public String getUserfield3() {
		return userfield3;
	}
	public void setUserfield3(String userfield3) {
		this.userfield3 = userfield3;
	}
	public Integer getWeightmode() {
		return weightmode;
	}
	public void setWeightmode(Integer weightmode) {
		this.weightmode = weightmode;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getCurrency1() {
		return currency1;
	}
	public void setCurrency1(float currency1) {
		this.currency1 = currency1;
	}
	public String getTransname() {
		return transname;
	}
	public void setTransname(String transname) {
		this.transname = transname;
	}
	public String getModibz() {
		return modibz;
	}
	public void setModibz(String modibz) {
		this.modibz = modibz;
	}
	public Integer getPrintnumber() {
		return printnumber;
	}
	public void setPrintnumber(Integer printnumber) {
		this.printnumber = printnumber;
	}
	public String getZserial() {
		return zserial;
	}
	public void setZserial(String zserial) {
		this.zserial = zserial;
	}
	
		
}
