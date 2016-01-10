package com.hcy.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hcy.mobilesafe.R;
import com.hcy.mobilesafe.utils.MD5Utils;

/**
 * 主页面
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends Activity {

	private GridView gvHome;
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] mPics = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps, R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		// 设置监听
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {

				case 0:
					// 手机防盗
					showPaddwordDialog();
					break;

				case 8:
					// 设置中心
					startActivity(new Intent(HomeActivity.this, SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 显示密码弹窗
	 */
	protected void showPaddwordDialog() {
		// 判断是否设置密码
		String savePassword = mPref.getString("password", null);
		if (!TextUtils.isEmpty(savePassword)) {

			// 输入密码弹窗
			showPasswordInputDialog();
		} else {
			// 如果没有设置过,弹出设置密码的弹窗
			showPasswordSetDialog();
		}
	}

	/**
	 * 输入密码弹窗
	 */
	private void showPasswordInputDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_input_password, null);
		// 将自定义的布局文件设置给dialog
		// dialog.setView(view);
		// 设置边距为0,保证在2.X的版本上运行时没有黑边
		dialog.setView(view, 0, 0, 0, 0);
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		Button btOK = (Button) view.findViewById(R.id.bt_ok);
		Button btCancel = (Button) view.findViewById(R.id.bt_cancel);

		btOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				if (!TextUtils.isEmpty(password)) {
					String savedPassword = mPref.getString("password", null);
					if (MD5Utils.encode(password).equals(savedPassword)) {
						Toast.makeText(HomeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						// 跳转到手机防盗页
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空", Toast.LENGTH_SHORT).show();

				}
			}
		});

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// 隐藏dialog
			}
		});

		dialog.show();
	}

	/**
	 * 设置密码的弹窗
	 */
	private void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		// 将自定义的布局文件设置给dialog
		// dialog.setView(view);
		// 设置边距为0,保证在2.X的版本上运行时没有黑边
		dialog.setView(view, 0, 0, 0, 0);
		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);
		Button btOK = (Button) view.findViewById(R.id.bt_ok);
		Button btCancel = (Button) view.findViewById(R.id.bt_cancel);

		btOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();

				// password != null && !password.equals(" ")
				if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(password)) {
					Log.d("测试1", "不为空");
					if (password.equals(passwordConfirm)) {
						Toast.makeText(HomeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
						// 将密码保存起来
						mPref.edit().putString("password", MD5Utils.encode(password)).commit();
						// 跳转到手机防盗页
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
						dialog.dismiss();
					} else {
						Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空", Toast.LENGTH_SHORT).show();
				}

			}
		});

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// 隐藏dialog
			}
		});

		dialog.show();
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
