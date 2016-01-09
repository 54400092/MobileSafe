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
 * ��������
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends Activity {

	private SettingItemView sivUpdate; // ��������
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("�Զ���������");

		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDesc("�Զ������ѿ���");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDesc("�Զ������ѹر�");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �жϵ�ǰ��ѡ״̬
				if (sivUpdate.isChecked()) {
					// ���ò���ѡ
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("�Զ������ѹر�");
					Log.d("����1", "if" + sivUpdate.isChecked());
					// ����SharedPreference
					mPref.edit().putBoolean("auto_update", false).commit();

				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("�Զ������ѿ���");
					Log.d("����1", "else" + sivUpdate.isChecked());
					// ����SharedPreference
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
