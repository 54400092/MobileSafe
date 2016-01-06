package com.hcy.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcy.mobilesafe.R;

/**
 * ��ҳ��
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends Activity {

	private GridView gvhome;
	private String[] mItems = new String[] { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };
	private int[] mPics = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps, R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gvhome = (GridView) findViewById(R.id.gv_home);
		gvhome.setAdapter(new HomeAdapter());
	}

	class HomeAdapter extends BaseAdapter {

		// ϵͳ���ô˷�����������֪ģ�Ͳ��ж���������
		@Override
		public int getCount() {
			return mItems.length;
		}

		/**
		 * 
		 */
		@Override
		public Object getItem(int position) {
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// ϵͳ���ô˷�������ȡҪ��ʾ��ListView��View����
		// position:��return��View��������Ӧ�������ڼ����е�λ��
		// ��Ļ������ʾ���ٸ���Ŀ��getView�����ͻᱻ���ö��ٴΣ���Ļ���»���ʱ��getView����������ã����������View������ʾ����Ļ
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			tvItem.setText(mItems[position]);
			ivItem.setImageResource(mPics[position]);

			return view;
		}

	}

}
