package com.hcy.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hcy.mobilesafe.R;

public class SplashActivity extends Activity {
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("�汾��:" + getVersion());
	}

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
		return "";
	}

}
