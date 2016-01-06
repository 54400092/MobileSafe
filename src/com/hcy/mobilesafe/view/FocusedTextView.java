package com.hcy.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * ��ȡ�����TextView
 * 
 * @author Administrator
 * 
 */
public class FocusedTextView extends TextView {

	// ��style��ʽ�Ļ����ߴ˷���
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// ������ʱ�ߴ˷���
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// �ô���new����ʱ�ߴ˷���
	public FocusedTextView(Context context) {
		super(context);
	}

	/**
	 * ��ʾ��û�л�ȡ����
	 * 
	 * �����Ҫ����,���ȵ��ô˺����ж��Ƿ��н���,��true,����ƲŻ���Ч��
	 * 
	 * ���ǲ���TextVeiw��û�н���,���Ƕ�ǿ�Ʒ���True,���������Ϊ�н���
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}

}
