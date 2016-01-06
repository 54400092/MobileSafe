package com.hcy.mobilesafe.activity;

import java.io.File;
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
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hcy.mobilesafe.R;
import com.hcy.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTERHOME = 4;

	private TextView tvVersion;
	private TextView tvProgress; // ���ؽ���չʾ

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
				enterHome();
				break;

			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "����������", Toast.LENGTH_SHORT).show();
				enterHome();
				break;

			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "���ݽ�������", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTERHOME:
				enterHome();

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("�汾��:" + getVersion());
		tvProgress = (TextView) findViewById(R.id.tv_progress); // Ĭ������

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

		final long startTime = System.currentTimeMillis();

		// �������߳��첽��������
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
					conn.setConnectTimeout(2000);
					// ������Ӧ��ʱ,��������,���������ٳٲ�����Ӧ
					conn.setReadTimeout(2000);
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
							// showUpdateDialog();
						} else {
							// û�а汾����
							msg.what = CODE_ENTERHOME;
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
					long endTime = System.currentTimeMillis();
					// �������绨�ѵ�ʱ��
					long timeUsed = endTime - startTime;
					if (timeUsed < 2000) {
						// ǿ������һһ��ʱ��,��֤����չʾ2s
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

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
		// builder.setCancelable(false);// �����û�ȡ���Ի���,�û�����̫��,������Ҫ��
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("����1", "��������");
				downLoad();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});

		// ����ȡ���ļ���,�û�������ؼ�ʱ�ᴥ��
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * ����APK�ļ�
	 */
	protected void downLoad() {

		// �ж��Ƿ���SD��
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			tvProgress.setVisibility(View.VISIBLE); // ��ʾ����
			// ��ȡSD���ĸ�Ŀ¼
			String target = Environment.getExternalStorageDirectory() + "/update.apk";
			// XUtils ��GitHub���ص�jar��,����libs�ļ�����
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {

				// �����ļ��Ľ���
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);

					Log.d("����1", "�ؽ���:" + current + "/" + total);
					tvProgress.setText("���ؽ���:" + current * 100 / total + "%");
				}

				// ���سɹ�
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					Log.d("����1", "downLoad:" + "���سɹ�");
					// ��ת��ϵͳ����ҳ��(�鿴packages\apps\PackageInstaller\AndroidManifest.xml�� PackageInstallerActivity)
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					// startActivity(intent);
					startActivityForResult(intent, 0); // ����û�ȡ����װ�Ļ��᷵�ؽ��,�ص�onActivityResult
				}

				// ����ʧ��
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "����ʧ��!", Toast.LENGTH_SHORT).show();

				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "û���ҵ�SD��!", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * �û����ظ��º�,��ȡ��,�ص�����
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * ������ҳ��
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		// ��ǰSplashҳ��finish��
		finish();
	}

}
