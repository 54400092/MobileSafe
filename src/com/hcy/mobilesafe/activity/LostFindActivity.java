package com.hcy.mobilesafe.activity;

import com.hcy.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * 手机防盗页面
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
		boolean configed = mPrefs.getBoolean("configed", false); // 判断是否进入过设置向导

		if (configed) {
			setContentView(R.layout.activity_lost_find);
		} else {
			// 跳转设置向导页
			startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
			finish();
		}

	}
}
