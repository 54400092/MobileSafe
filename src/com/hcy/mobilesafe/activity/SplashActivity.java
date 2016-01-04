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

	// 服务器的信息
	private String mVersionName; // 版本名(成员变量加"m")
	private int mVersionCode; // 版本号
	private String mDescription; // 版本描述
	private String mDownloadUrl; // 下载地址

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;

			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
				break;

			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误错误", Toast.LENGTH_SHORT).show();
				break;

			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
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
		tv_version.setText("版本名:" + getVersion());

		checkVersion();
	}

	/**
	 * 获取版本名称
	 * 
	 * @return
	 */
	private String getVersion() {
		PackageManager packageManager = getPackageManager();
		try {
			// 获取包的信息
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			Log.d("测试1", "versionName=" + versionName + "versionCode" + versionCode);
			return versionName;
		} catch (NameNotFoundException e) {
			// 没有找到包名的时候会走此异常
			e.printStackTrace();
		}
		return "-1";
	}

	/**
	 * 获取本地app的版本号
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			// 获取包的信息
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// 没有找到包名的时候会走此异常
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 从服务器获取版本信息进行校验
	 */
	private void checkVersion() {
		new Thread() {
			
			public void run() {

				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					// 本机地址用localhost,但是如果用模拟器加载本机的地址时,可以用ip(192.168.0.213)来替换
					URL url = new URL("http://192.168.0.213:8090/update.json");
					conn = (HttpURLConnection) url.openConnection();
					// 设置请求方法
					conn.setRequestMethod("GET");
					// 设置连接超时,没有连接上
					conn.setConnectTimeout(5000);
					// 设置响应超时,连接上了,但服务器迟迟不给响应
					conn.setReadTimeout(5000);
					// 连接服务器
					conn.connect();
					// 获取响应码
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						Log.d("测试1", "网络返回:" + result);
						// 解析json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDescription = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						Log.d("测试1", "versionName: " + mVersionName);
						Log.d("测试1", "versionCode: " + mVersionCode);
						Log.d("测试1", "description: " + mDescription);
						Log.d("测试1", "downloadUrl: " + mDownloadUrl);

						// 判断是否有更新
						if (mVersionCode > getVersionCode()) {
							// 服务器的VersionCode > 本地的VersionCode
							// 说明有更新,弹出升级对话框
							msg.what = CODE_UPDATE_DIALOG;
							showUpdateDialog();
						}
					}

				} catch (MalformedURLException e) {
					// url错误异常
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 网络错误异常
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// JSON解析失败
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					mhandler.sendMessage(msg);

					if (conn != null) {
						// 关闭网络连接
						conn.disconnect();
					}
				}
			}

		}.start();

	}

	/**
	 * 升级对话框
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本:" + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("测试1", "立即更新");
			}
		});
		builder.setNegativeButton("以后再说", null);
		builder.show();
	}
}
