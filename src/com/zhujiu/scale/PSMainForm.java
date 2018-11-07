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
	TrayIcon trayIcon; // ����ͼ��
	SystemTray tray; // ������ϵͳ���̵�ʵ��
	boolean networkOk = false; // ����
	boolean serviceOk = false; // ����

	// icon
	ImageIcon iconNORMAL = null; // ����
	ImageIcon iconNetError = null; // �����쳣
	ImageIcon iconSVRError = null; // ������

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
	// ��ȡ���һ���ϴ���¼
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
			loginfo("�������ļ� ini.properties ʧ��" + e.getMessage(), false);
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
		// ��ȡ��ʼ������
		SvrIp = props.getProperty("SvrIp", "127.0.0.1");
		SvrPort = props.getProperty("SvrPort", "8080");
		dbpath = props.getProperty("DbPath", "jdbc:Access:///C:/SysDb.mdb");
		dbsql = props.getProperty("DbSql", "select * from WeightDataSubmit");

		final JFrame frame = new JFrame("�ӿڼ�����");
		frame.setSize(300, 200);
		frame.setUndecorated(true); //
		frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);

		frame.setVisible(false);
		lbStatus = new JLabel("");

		lbStatus.setFont(new java.awt.Font("Dialog", 1, 15));
		// ��������
		lbStatus.setForeground(Color.black);
		// ������ɫ

		frame.add(lbStatus);
		tray = SystemTray.getSystemTray(); // ��ñ�����ϵͳ���̵�ʵ��
		iconNORMAL = new ImageIcon("main/resource/OK.png"); // ��Ҫ��ʾ�������е�ͼ�꣬����
		iconNetError = new ImageIcon("main/resource/ERROR_IO.png"); // ��Ҫ��ʾ�������е�ͼ�꣬����
		iconSVRError = new ImageIcon("main/resource/ERROR_URL.png"); // ��Ҫ��ʾ�������е�ͼ�꣬����

		PopupMenu pop = new PopupMenu(); // ����һ���Ҽ�����ʽ�˵�
		final MenuItem show = new MenuItem("�򿪳���");
		final MenuItem exit = new MenuItem("�˳�����");
		pop.add(show);
		pop.add(exit);
		trayIcon = new TrayIcon(iconNORMAL.getImage(), "�ӿڼ�����", pop);// ʵ��������ͼ��
		trayIcon.setImageAutoSize(true);

		// Ϊ����ͼ���������¼�
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)// ���˫��ͼ��
				{
					// trayIcon.displayMessage("����", "����һ��������ʾ!",
					// TrayIcon.MessageType.WARNING);
					// trayIcon.displayMessage("����", "����һ��������ʾ!",
					// TrayIcon.MessageType.ERROR);
					// trayIcon.displayMessage("��Ϣ", "����һ����Ϣ��ʾ!",
					// TrayIcon.MessageType.INFO);
					// tray.remove(trayIcon); // ��ϵͳ������ʵ�����Ƴ�����ͼ��
					frame.setExtendedState(JFrame.NORMAL);// ����״̬Ϊ����
					frame.setVisible(true);// ��ʾ������
				}
			}
		});

		// ѡ��ע���¼�
		ActionListener al2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �˳�����
				if (e.getSource() == exit) {
					System.exit(0);// �˳�����
				}
				// �򿪳���
				if (e.getSource() == show) {
					frame.setExtendedState(JFrame.NORMAL);// ����״̬Ϊ����
					frame.setVisible(true);
				}
			}
		};
		exit.addActionListener(al2);
		show.addActionListener(al2);

		try {
			tray.add(trayIcon); // ������ͼ����ӵ�ϵͳ������ʵ����
		} catch (AWTException ex) {
			ex.printStackTrace();
		}

		// Ϊ������ע�ᴰ���¼�
		frame.addWindowListener(new WindowAdapter() {
			// ������С���¼�
			public void windowIconified(WindowEvent e) {
				frame.setVisible(false);// ʹ���ڲ�����
				frame.dispose();// �ͷŵ�ǰ������Դ
			}
		});

		// �������
		ping();
		// �������ǿ���
		hello();

		// ��ѯ���һ��ͬ����¼
		try {
			String reponse = RESTFulGetUtil.doGet(composeUrl(urlLastFlowID));
			if (ERROR_IO.equals(reponse)) {
				trayIcon.setImage(iconSVRError.getImage());
				networkOk = false;
			} else if (ERROR_URL.equals(reponse)) {
				trayIcon.setImage(iconSVRError.getImage());
				serviceOk = false;
			} else {
				// ����json����
				lastFlowID = reponse;
				trayIcon.setImage(iconNORMAL.getImage());
				serviceOk = true;
			}
		} catch (Exception e) {
			lastFlowID = "";
		}

		// �Ӹ���ʱ����,��ʱ��ȡ����
		try {
			java.util.Timer timer = new java.util.Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					if(hello() == false){
						return;
					}
					System.out.println(lastFlowID+":�ȴ���֤���ϴ�����...");
					// δȡ���ϴ�ͬ������
					if (StringUtils.equals(lastFlowID, "")) {
						
						String reponse = RESTFulGetUtil.doGet(composeUrl(urlLastFlowID));
						if (ERROR_IO.equals(reponse)) {
							trayIcon.setImage(iconSVRError.getImage());
							networkOk = false;
							lbStatus.setText("�����쳣!" + getDateStr());
							return;
						} else if (ERROR_URL.equals(reponse)) {
							trayIcon.setImage(iconSVRError.getImage());
							serviceOk = false;
							lbStatus.setText("�����쳣!" + getDateStr());
							return;
						} else {
							// ����json����
							lastFlowID = reponse;
							trayIcon.setImage(iconNORMAL.getImage());
							serviceOk = true;
							lbStatus.setText("��������" + getDateStr());
						}
					}
					// ��ѯ����
					if (StringUtils.equals(lastFlowID, "") == false) {
						List<WeightData> newDatas = JDBCAccess.queryWeightData(dbpath, lastFlowID, dbsql);
						// �������ϴ�
						if (newDatas.size() == 0) {
							return;
						}
						JSONArray json = JSONArray.fromObject(newDatas);
						String data = json.toString();
						String post = RESTFulPostUtil.doPost(composeUrl(urlWeightData), data);
						// ����json����
						if (StringUtils.equals(post, "OK")) {
							lastFlowID = newDatas.get(newDatas.size() - 1).getWflowid();
							loginfo("�ϴ����ݣ�" + newDatas.size() + " ��", true);
							trayIcon.setImage(iconNORMAL.getImage());
							lbStatus.setText("�����ϴ��ɹ���" + getDateStr());
						} else {
							trayIcon.setImage(iconNetError.getImage());
							lbStatus.setText("�����ϴ�ʧ�ܣ�" + getDateStr());
							serviceOk = false;
						}
					}
				}
			}, 0, 5000);
			// ��������
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
				bw.write(" *****  " + c.get(Calendar.YEAR) + "��" + (c.get(Calendar.MONTH) + 1) + "��"
						+ (c.get(Calendar.DAY_OF_MONTH)) + "��" + c.get(Calendar.HOUR_OF_DAY) + ":"
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
		return c.get(Calendar.YEAR) + "��" + (c.get(Calendar.MONTH) + 1) + "��" + (c.get(Calendar.DAY_OF_MONTH)) + "��"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
	}

	private String composeUrl(String path) {
		return "http://" + SvrIp + ":" + SvrPort + "/" + path;
	}

	private boolean ping() {
		if (PingUtil.ping(SvrIp, 3, 500) == false) {
			//trayIcon.displayMessage("����", "���粻ͨ!", TrayIcon.MessageType.WARNING);
			trayIcon.setImage(iconNetError.getImage());
			networkOk = false;
			loginfo("��������ʧ��", true);
			lbStatus.setText("��������ʧ��");
			System.out.println("��������ʧ�� " + getDateStr());
			return false;
		} else {
			networkOk = true;
			loginfo("�������� OK", true);
			lbStatus.setText("�������� OK!" + getDateStr());
			System.out.println("�������� OK! " + getDateStr());
			return true;
		}
	}

	private boolean hello() {
		// �������ǿ���
		if (ServiceUtil.hello(composeUrl(urlhello)) == false) {
			//trayIcon.displayMessage("����", "���񲻿���!", TrayIcon.MessageType.WARNING);
			trayIcon.setImage(iconSVRError.getImage());
			serviceOk = false;
			loginfo("���񲻿���", true);
			lbStatus.setText("���񲻿���!" + getDateStr());
			System.out.println("���񲻿���! " + getDateStr());
			return false;
		} else {
			serviceOk = true;
			loginfo("��������!", true);
			lbStatus.setText("��������!" + getDateStr());
			System.out.println("��������! " + getDateStr());
			return true;
		}
	}

	public static void main(String[] args) {
		// ��������
		new PSMainForm();
	}

}
