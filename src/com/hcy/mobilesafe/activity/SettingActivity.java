package com.hcy.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.hcy.mobilesafe.R;
import com.hcy.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends Activity {

	private SettingItemView sivUpdate; // 设置升级
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");

		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDesc("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDesc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断当前勾选状态
				if (sivUpdate.isChecked()) {
					// 设置不勾选
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("自动更新已关闭");
					Log.d("测试1", "if" + sivUpdate.isChecked());
					// 更新SharedPreference
					mPref.edit().putBoolean("auto_update", false).commit();

				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("自动更新已开启");
					Log.d("测试1", "else" + sivUpdate.isChecked());
					// 更新SharedPreference
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
