package com.hcy.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hcy.mobilesafe.R;
import com.hcy.mobilesafe.utils.StreamUtils;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;

	private TextView tv_version;

	// ����������Ϣ
	private String mVersionName; // �汾��(��Ա������"m")
	private int mVersionCode; // �汾��
	private String mDescription; // �汾����
	private String mDownloadUrl; // ���ص�ַ

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;

			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL����", Toast.LENGTH_SHORT).show();
				break;

			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "����������", Toast.LENGTH_SHORT).show();
				break;

			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "���ݽ�������", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("�汾��:" + getVersion());

		checkVersion();
	}

	/**
	 * ��ȡ�汾����
	 * 
	 * @return
	 */
	private String getVersion() {
		PackageManager packageManager = getPackageManager();
		try {
			// ��ȡ������Ϣ
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			Log.d("����1", "versionName=" + versionName + "versionCode" + versionCode);
			return versionName;
		} catch (NameNotFoundException e) {
			// û���ҵ�������ʱ����ߴ��쳣
			e.printStackTrace();
		}
		return "-1";
	}

	/**
	 * ��ȡ����app�İ汾��
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			// ��ȡ������Ϣ
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// û���ҵ�������ʱ����ߴ��쳣
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * �ӷ�������ȡ�汾��Ϣ����У��
	 */
	private void checkVersion() {
		new Thread() {
			
			public void run() {

				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					// ������ַ��localhost,���������ģ�������ر����ĵ�ַʱ,������ip(192.168.0.213)���滻
					URL url = new URL("http://192.168.0.213:8090/update.json");
					conn = (HttpURLConnection) url.openConnection();
					// �������󷽷�
					conn.setRequestMethod("GET");
					// �������ӳ�ʱ,û��������
					conn.setConnectTimeout(5000);
					// ������Ӧ��ʱ,��������,���������ٳٲ�����Ӧ
					conn.setReadTimeout(5000);
					// ���ӷ�����
					conn.connect();
					// ��ȡ��Ӧ��
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						Log.d("����1", "���緵��:" + result);
						// ����json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDescription = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						Log.d("����1", "versionName: " + mVersionName);
						Log.d("����1", "versionCode: " + mVersionCode);
						Log.d("����1", "description: " + mDescription);
						Log.d("����1", "downloadUrl: " + mDownloadUrl);

						// �ж��Ƿ��и���
						if (mVersionCode > getVersionCode()) {
							// ��������VersionCode > ���ص�VersionCode
							// ˵���и���,���������Ի���
							msg.what = CODE_UPDATE_DIALOG;
							showUpdateDialog();
						}
					}

				} catch (MalformedURLException e) {
					// url�����쳣
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// ��������쳣
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// JSON����ʧ��
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					mhandler.sendMessage(msg);

					if (conn != null) {
						// �ر���������
						conn.disconnect();
					}
				}
			}

		}.start();

	}

	/**
	 * �����Ի���
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���°汾:" + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("����1", "��������");
			}
		});
		builder.setNegativeButton("�Ժ���˵", null);
		builder.show();
	}
}
