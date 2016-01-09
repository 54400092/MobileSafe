package com.hcy.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcy.mobilesafe.R;

/**
 * 设置中心的自定义组合控件
 * 
 * @author Administrator
 * 
 */
public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.hcy.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private String mTitle;
	private String mDescOn;
	private String mDescOff;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		Log.d("测试1", "111");
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(NAMESPACE, "title");
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		initView();
		Log.d("测试1", "222");
		int attributeCount = attrs.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = attrs.getAttributeName(i);
			String attributeValue = attrs.getAttributeValue(i);
			Log.d("测试1", attributeName + ":" + attributeValue);
		}

	}

	public SettingItemView(Context context) {
		super(context);
		initView();
		Log.d("测试1", "333");
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		// 将自定义好的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		tvTitle.setText(mTitle);// 设置标题

	}

	public void setTitle(String title) {
		tvTitle.setText(title);

	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}

	/**
	 * 返回勾选状态
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cbStatus.isChecked();
	}

	public void setChecked(boolean check) {
		cbStatus.setChecked(check);
		// 根据选择的状态更新文本描述
		if (check) {
			setDesc(mDescOn);
		} else {
			setDesc(mDescOff);
		}
	}
}
