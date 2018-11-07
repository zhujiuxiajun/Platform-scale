package com.zhujiu.scale;

/**
 * @author Administrator
 *
 */
import java.awt.AWTException;
import java.awt.Color;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;

import org.apache.commons.lang.StringUtils;

import com.zhujiu.util.PingUtil;
import com.zhujiu.util.ServiceUtil;

import net.sf.json.JSONArray;

public class PSMainForm {
	TrayIcon trayIcon; // 托盘图标
	SystemTray tray; // 本操作系统托盘的实例
	boolean networkOk = false; // 网络
	boolean serviceOk = false; // 服务

	// icon
	ImageIcon iconNORMAL = null; // 正常
	ImageIcon iconNetError = null; // 网络异常
	ImageIcon iconSVRError = null; // 服务不用

	private JLabel lbStatus = null;

	private static final String ERROR_IO = "ERRORIO";
	private static final String ERROR_URL = "ERRORURL";

	private static String logFileName = null;
	private static Properties props = new Properties();
	String SvrIp = "122.112.210.4";
	String lastFlowID = "";
	String SvrPort = "8080";
	String dbpath = "";
	String dbsql = "";
	// 获取最近一次上传记录
	String urlLastFlowID = "nbim/api/outif/scale/lastflowid";
	String urlWeightData = "nbim/api/outif/scale/datas";
	String urlhello = "nbim/api/outif/scale/weighbridge";

	static {
		logFileName = System.getProperty("user.dir") + "/ScaleLogs.txt";
		InputStream in = null;
		try {
			in = PSMainForm.class.getResourceAsStream("/ini.properties");
			if (in != null)
				props.load(in);
		} catch (IOException e) {
			loginfo("打开属性文件 ini.properties 失败" + e.getMessage(), false);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public PSMainForm() {
		// 读取初始化参数
		SvrIp = props.getProperty("SvrIp", "127.0.0.1");
		SvrPort = props.getProperty("SvrPort", "8080");
		dbpath = props.getProperty("DbPath", "jdbc:Access:///C:/SysDb.mdb");
		dbsql = props.getProperty("DbSql", "select * from WeightDataSubmit");

		final JFrame frame = new JFrame("接口监视器");
		frame.setSize(300, 200);
		frame.setUndecorated(true); //
		frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);

		frame.setVisible(false);
		lbStatus = new JLabel("");

		lbStatus.setFont(new java.awt.Font("Dialog", 1, 15));
		// 设置字体
		lbStatus.setForeground(Color.black);
		// 设置颜色

		frame.add(lbStatus);
		tray = SystemTray.getSystemTray(); // 获得本操作系统托盘的实例
		iconNORMAL = new ImageIcon("main/resource/OK.png"); // 将要显示到托盘中的图标，正常
		iconNetError = new ImageIcon("main/resource/ERROR_IO.png"); // 将要显示到托盘中的图标，正常
		iconSVRError = new ImageIcon("main/resource/ERROR_URL.png"); // 将要显示到托盘中的图标，正常

		PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
		final MenuItem show = new MenuItem("打开程序");
		final MenuItem exit = new MenuItem("退出程序");
		pop.add(show);
		pop.add(exit);
		trayIcon = new TrayIcon(iconNORMAL.getImage(), "接口监视器", pop);// 实例化托盘图标
		trayIcon.setImageAutoSize(true);

		// 为托盘图标监听点击事件
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)// 鼠标双击图标
				{
					// trayIcon.displayMessage("警告", "这是一个警告提示!",
					// TrayIcon.MessageType.WARNING);
					// trayIcon.displayMessage("错误", "这是一个错误提示!",
					// TrayIcon.MessageType.ERROR);
					// trayIcon.displayMessage("信息", "这是一个信息提示!",
					// TrayIcon.MessageType.INFO);
					// tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
					frame.setExtendedState(JFrame.NORMAL);// 设置状态为正常
					frame.setVisible(true);// 显示主窗体
				}
			}
		});

		// 选项注册事件
		ActionListener al2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 退出程序
				if (e.getSource() == exit) {
					System.exit(0);// 退出程序
				}
				// 打开程序
				if (e.getSource() == show) {
					frame.setExtendedState(JFrame.NORMAL);// 设置状态为正常
					frame.setVisible(true);
				}
			}
		};
		exit.addActionListener(al2);
		show.addActionListener(al2);

		try {
			tray.add(trayIcon); // 将托盘图标添加到系统的托盘实例中
		} catch (AWTException ex) {
			ex.printStackTrace();
		}

		// 为主窗体注册窗体事件
		frame.addWindowListener(new WindowAdapter() {
			// 窗体最小化事件
			public void windowIconified(WindowEvent e) {
				frame.setVisible(false);// 使窗口不可视
				frame.dispose();// 释放当前窗体资源
			}
		});

		// 检查网络
		ping();
		// 检查服务是可以
		hello();

		// 查询最后一次同步记录
		try {
			String reponse = RESTFulGetUtil.doGet(composeUrl(urlLastFlowID));
			if (ERROR_IO.equals(reponse)) {
				trayIcon.setImage(iconSVRError.getImage());
				networkOk = false;
			} else if (ERROR_URL.equals(reponse)) {
				trayIcon.setImage(iconSVRError.getImage());
				serviceOk = false;
			} else {
				// 处理json对象
				lastFlowID = reponse;
				trayIcon.setImage(iconNORMAL.getImage());
				serviceOk = true;
			}
		} catch (Exception e) {
			lastFlowID = "";
		}

		// 加个定时监听,定时读取数据
		try {
			java.util.Timer timer = new java.util.Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					if(hello() == false){
						return;
					}
					System.out.println(lastFlowID+":等待验证，上传数据...");
					// 未取得上传同步数据
					if (StringUtils.equals(lastFlowID, "")) {
						
						String reponse = RESTFulGetUtil.doGet(composeUrl(urlLastFlowID));
						if (ERROR_IO.equals(reponse)) {
							trayIcon.setImage(iconSVRError.getImage());
							networkOk = false;
							lbStatus.setText("网络异常!" + getDateStr());
							return;
						} else if (ERROR_URL.equals(reponse)) {
							trayIcon.setImage(iconSVRError.getImage());
							serviceOk = false;
							lbStatus.setText("服务异常!" + getDateStr());
							return;
						} else {
							// 处理json对象
							lastFlowID = reponse;
							trayIcon.setImage(iconNORMAL.getImage());
							serviceOk = true;
							lbStatus.setText("服务正常" + getDateStr());
						}
					}
					// 查询数据
					if (StringUtils.equals(lastFlowID, "") == false) {
						List<WeightData> newDatas = JDBCAccess.queryWeightData(dbpath, lastFlowID, dbsql);
						// 无数据上传
						if (newDatas.size() == 0) {
							return;
						}
						JSONArray json = JSONArray.fromObject(newDatas);
						String data = json.toString();
						String post = RESTFulPostUtil.doPost(composeUrl(urlWeightData), data);
						// 处理json对象
						if (StringUtils.equals(post, "OK")) {
							lastFlowID = newDatas.get(newDatas.size() - 1).getWflowid();
							loginfo("上传数据：" + newDatas.size() + " 条", true);
							trayIcon.setImage(iconNORMAL.getImage());
							lbStatus.setText("数据上传成功！" + getDateStr());
						} else {
							trayIcon.setImage(iconNetError.getImage());
							lbStatus.setText("数据上传失败！" + getDateStr());
							serviceOk = false;
						}
					}
				}
			}, 0, 5000);
			// 监听结束
		} catch (Exception ex) {
			System.out.println("Timer exception");
		}
	}

	public static void loginfo(String msg, Boolean appendTime) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(logFileName, true));
			if (appendTime) {
				bw.newLine();
				bw.newLine();
				Calendar c = new GregorianCalendar();
				bw.write(" *****  " + c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月"
						+ (c.get(Calendar.DAY_OF_MONTH)) + "日" + c.get(Calendar.HOUR_OF_DAY) + ":"
						+ c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + "  *****");
			}
			bw.newLine();
			bw.write(msg);
			bw.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
				}
				bw = null;
			}
		}
	}

	private String getDateStr() {
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + (c.get(Calendar.DAY_OF_MONTH)) + "日"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
	}

	private String composeUrl(String path) {
		return "http://" + SvrIp + ":" + SvrPort + "/" + path;
	}

	private boolean ping() {
		if (PingUtil.ping(SvrIp, 3, 500) == false) {
			//trayIcon.displayMessage("警告", "网络不通!", TrayIcon.MessageType.WARNING);
			trayIcon.setImage(iconNetError.getImage());
			networkOk = false;
			loginfo("网络连接失败", true);
			lbStatus.setText("网络连接失败");
			System.out.println("网络连接失败 " + getDateStr());
			return false;
		} else {
			networkOk = true;
			loginfo("网络连接 OK", true);
			lbStatus.setText("网络连接 OK!" + getDateStr());
			System.out.println("网络连接 OK! " + getDateStr());
			return true;
		}
	}

	private boolean hello() {
		// 检查服务是可以
		if (ServiceUtil.hello(composeUrl(urlhello)) == false) {
			//trayIcon.displayMessage("警告", "服务不可用!", TrayIcon.MessageType.WARNING);
			trayIcon.setImage(iconSVRError.getImage());
			serviceOk = false;
			loginfo("服务不可用", true);
			lbStatus.setText("服务不可用!" + getDateStr());
			System.out.println("服务不可用! " + getDateStr());
			return false;
		} else {
			serviceOk = true;
			loginfo("服务正常!", true);
			lbStatus.setText("服务正常!" + getDateStr());
			System.out.println("服务正常! " + getDateStr());
			return true;
		}
	}

	public static void main(String[] args) {
		// 创建托盘
		new PSMainForm();
	}

}
