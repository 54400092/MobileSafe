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
 * 主页面
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends Activity {

	private GridView gvhome;
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] mPics = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps, R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gvhome = (GridView) findViewById(R.id.gv_home);
		gvhome.setAdapter(new HomeAdapter());
	}

	class HomeAdapter extends BaseAdapter {

		// 系统调用此方法，用来获知模型层有多少条数据
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

		// 系统调用此方法，获取要显示至ListView的View对象
		// position:是return的View对象所对应的数据在集合中的位置
		// 屏幕上能显示多少个条目，getView方法就会被调用多少次，屏幕向下滑动时，getView会继续被调用，创建更多的View对象显示至屏幕
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
