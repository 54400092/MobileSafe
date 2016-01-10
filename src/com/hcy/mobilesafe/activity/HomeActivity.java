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
 * ��ҳ��
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends Activity {

	private GridView gvHome;
	private String[] mItems = new String[] { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "����ͳ��", "�ֻ�ɱ��", "��������", "�߼�����", "��������" };
	private int[] mPics = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps, R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		// ���ü���
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {

				case 0:
					// �ֻ�����
					showPaddwordDialog();
					break;

				case 8:
					// ��������
					startActivity(new Intent(HomeActivity.this, SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * ��ʾ���뵯��
	 */
	protected void showPaddwordDialog() {
		// �ж��Ƿ���������
		String savePassword = mPref.getString("password", null);
		if (!TextUtils.isEmpty(savePassword)) {

			// �������뵯��
			showPasswordInputDialog();
		} else {
			// ���û�����ù�,������������ĵ���
			showPasswordSetDialog();
		}
	}

	/**
	 * �������뵯��
	 */
	private void showPasswordInputDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_input_password, null);
		// ���Զ���Ĳ����ļ����ø�dialog
		// dialog.setView(view);
		// ���ñ߾�Ϊ0,��֤��2.X�İ汾������ʱû�кڱ�
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
						Toast.makeText(HomeActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						// ��ת���ֻ�����ҳ
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "�������", Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(HomeActivity.this, "��������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();

				}
			}
		});

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// ����dialog
			}
		});

		dialog.show();
	}

	/**
	 * ��������ĵ���
	 */
	private void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		// ���Զ���Ĳ����ļ����ø�dialog
		// dialog.setView(view);
		// ���ñ߾�Ϊ0,��֤��2.X�İ汾������ʱû�кڱ�
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
					Log.d("����1", "��Ϊ��");
					if (password.equals(passwordConfirm)) {
						Toast.makeText(HomeActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
						// �����뱣������
						mPref.edit().putString("password", MD5Utils.encode(password)).commit();
						// ��ת���ֻ�����ҳ
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
						dialog.dismiss();
					} else {
						Toast.makeText(HomeActivity.this, "�������벻һ��", Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(HomeActivity.this, "��������ݲ���Ϊ��", Toast.LENGTH_SHORT).show();
				}

			}
		});

		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();// ����dialog
			}
		});

		dialog.show();
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
