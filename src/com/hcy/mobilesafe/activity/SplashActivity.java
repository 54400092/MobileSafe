package com.hcy.mobilesafe.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hcy.mobilesafe.R;
import com.hcy.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTERHOME = 4;

	private TextView tvVersion;
	private TextView tvProgress; // 下载进度展示

	// 服务器的信息
	private String mVersionName; // 版本名(成员变量加"m")
	private int mVersionCode; // 版本号
	private String mDescription; // 版本描述
	private String mDownloadUrl; // 下载地址

	private Handler mhandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;

			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;

			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;

			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTERHOME:
				enterHome();

			default:
				break;
			}
		};
	};
	private SharedPreferences mPref;
	private RelativeLayout rlRoot; // 跟布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本名:" + getVersion());
		tvProgress = (TextView) findViewById(R.id.tv_progress); // 默认隐藏

		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		// 判断是否需要自动更新
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			checkVersion();
		} else {
			mhandler.sendEmptyMessageDelayed(CODE_ENTERHOME, 2000);// 延时2s后发送消息
		}

		// 渐变的动画效果
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);

	}

	/**
	 * 获取版本名称
	 * 
	 * @return
	 */
	private String getVersion() {
		PackageManager packageManager = getPackageManager();
		try {
			// 获取包的信息
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			Log.d("测试1", "versionName=" + versionName + "versionCode" + versionCode);
			return versionName;
		} catch (NameNotFoundException e) {
			// 没有找到包名的时候会走此异常
			e.printStackTrace();
		}
		return "-1";
	}

	/**
	 * 获取本地app的版本号
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			// 获取包的信息
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// 没有找到包名的时候会走此异常
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 从服务器获取版本信息进行校验
	 */
	private void checkVersion() {

		final long startTime = System.currentTimeMillis();

		// 启动子线程异步加载数据
		new Thread() {

			public void run() {

				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					// 本机地址用localhost,但是如果用模拟器加载本机的地址时,可以用ip(192.168.0.213)来替换
					URL url = new URL("http://192.168.0.213:8090/update.json");
					conn = (HttpURLConnection) url.openConnection();
					// 设置请求方法
					conn.setRequestMethod("GET");
					// 设置连接超时,没有连接上
					conn.setConnectTimeout(2000);
					// 设置响应超时,连接上了,但服务器迟迟不给响应
					conn.setReadTimeout(2000);
					// 连接服务器
					conn.connect();
					// 获取响应码
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						Log.d("测试1", "网络返回:" + result);
						// 解析json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDescription = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						Log.d("测试1", "versionName: " + mVersionName);
						Log.d("测试1", "versionCode: " + mVersionCode);
						Log.d("测试1", "description: " + mDescription);
						Log.d("测试1", "downloadUrl: " + mDownloadUrl);

						// 判断是否有更新
						if (mVersionCode > getVersionCode()) {
							// 服务器的VersionCode > 本地的VersionCode
							// 说明有更新,弹出升级对话框
							msg.what = CODE_UPDATE_DIALOG;
							// showUpdateDialog();
						} else {
							// 没有版本更新
							msg.what = CODE_ENTERHOME;
						}
					}

				} catch (MalformedURLException e) {
					// url错误异常
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 网络错误异常
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// JSON解析失败
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					// 访问网络花费的时间
					long timeUsed = endTime - startTime;
					if (timeUsed < 2000) {
						// 强制休眠一一段时间,保证闪屏展示2s
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					mhandler.sendMessage(msg);

					if (conn != null) {
						// 关闭网络连接
						conn.disconnect();
					}
				}
			}

		}.start();

	}

	/**
	 * 升级对话框
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本:" + mVersionName);
		builder.setMessage(mDescription);
		// builder.setCancelable(false);// 不让用户取消对话框,用户体验太差,尽量不要用
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d("测试1", "立即更新");
				downLoad();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});

		// 设置取消的监听,用户点击返回键时会触发
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * 下载APK文件
	 */
	protected void downLoad() {

		// 判断是否有SD卡
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			tvProgress.setVisibility(View.VISIBLE); // 显示进度
			// 获取SD卡的更目录
			String target = Environment.getExternalStorageDirectory() + "/update.apk";
			// XUtils 从GitHub下载的jar包,放入libs文件夹下
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {

				// 下载文件的进度,该方法在主线程运行
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);

					Log.d("测试1", "载进度:" + current + "/" + total);
					tvProgress.setText("下载进度:" + current * 100 / total + "%");
				}

				// 下载成功,该方法在主线程运行
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					Log.d("测试1", "downLoad:" + "下载成功");
					// 跳转到系统下载页面(查看packages\apps\PackageInstaller\AndroidManifest.xml的 PackageInstallerActivity)
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					// startActivity(intent);
					startActivityForResult(intent, 0); // 如果用户取消安装的话会返回结果,回调onActivityResult
				}

				// 下载失败,该方法在主线程运行
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败!", Toast.LENGTH_SHORT).show();

				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "没有找到SD卡!", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 用户下载更新后,按取消,回调方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 进入主页面
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		// 当前Splash页面finish掉
		finish();
	}

}
