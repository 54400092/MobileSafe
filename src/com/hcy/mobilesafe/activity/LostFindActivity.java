package com.hcy.mobilesafe.activity;

import com.hcy.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * �ֻ�����ҳ��
 * 
 * @author Administrator
 * 
 */
public class LostFindActivity extends Activity {
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		boolean configed = mPrefs.getBoolean("configed", false); // �ж��Ƿ�����������

		if (configed) {
			setContentView(R.layout.activity_lost_find);
		} else {
			// ��ת������ҳ
			startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
			finish();
		}

	}
}
