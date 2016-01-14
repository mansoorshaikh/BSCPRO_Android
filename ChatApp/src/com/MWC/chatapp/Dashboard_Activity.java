package com.MWC.chatapp;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.airbrake.AirbrakeNotifier;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;
import applicationVo.DashboardVO;

public class Dashboard_Activity extends Activity {

	public static String MENU_ITEM_SELECTED = "";
	FrameLayout mainFrameLyout;
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	Button menuBtn;
	ScheduledExecutorService ses;
	ExecutorService es;
	public boolean flag = false;
	private DisplayMetrics metrics;
	private RelativeLayout slidingPanel, menulayout;
	int panelWidth = 0, devicewidth = 0;
	private boolean isExpanded;
	public String responseString = "";
	public ArrayList<DashboardVO> dashboardArrayList = new ArrayList<DashboardVO>();
	Handler mHandler = new Handler();
	SharedPreferences sharedPreferences;
	public String responseText = "";
	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	private ProgressDialog mProgressDialog;
	public ProgressBar myProgressBar;

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS1:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please wait ...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();

			return mProgressDialog;

		default:
			return null;
		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard_);

		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");

			WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiLock wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL,
					"MyWifiLock");
			wifiLock.acquire();

			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			panelWidth = (int) ((metrics.widthPixels) * 0.75);
			devicewidth = (int) ((metrics.widthPixels) * 0.45);
			slidingPanel = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
			slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
					.getLayoutParams();
			slidingPanelParameters.width = metrics.widthPixels;
			slidingPanel.setLayoutParams(slidingPanelParameters);

			menulayout = (RelativeLayout) findViewById(R.id.Layoutmenubaar);
			menulayout.setVisibility(View.GONE);

			metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			panelWidth = (int) ((metrics.widthPixels) * 0.75);
			slidingPanel = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
			slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
					.getLayoutParams();
			slidingPanelParameters.width = metrics.widthPixels;
			slidingPanel.setLayoutParams(slidingPanelParameters);

			menulayout=(RelativeLayout)findViewById(R.id.Layoutmenubaar);
			 menulayout.setVisibility(View.GONE);

			menuBtn = (Button) findViewById(R.id.menuBtn);
			menuBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!isExpanded) {
						isExpanded = true;

						menulayout.setVisibility(View.VISIBLE);
						// Expand
						new ExpandAnimation(slidingPanel, panelWidth,
								Animation.RELATIVE_TO_SELF, 0.0f,
								Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);
					} else {
						isExpanded = false;
						
						// Collapse
						new CollapseAnimation(slidingPanel, panelWidth,
								TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
								TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f,
								0, 0.0f,menulayout);

					}
				}
			});
			ChatSingleton chatSingleton = ChatSingleton.getInstance();
			if (chatSingleton.open == 1) {
				if (!isExpanded) {
					isExpanded = true;
					menulayout.setVisibility(View.VISIBLE);
					// Expand
					new ExpandAnimation(slidingPanel, panelWidth,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);
				} else {
					isExpanded = false;

					// Collapse
					new CollapseAnimation(slidingPanel, panelWidth,
							TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
							TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f,
							0, 0.0f, menulayout);
				}
				chatSingleton.open = 0;
			}

			Typeface font = Typeface.createFromAsset(getAssets(),
					"fontawesome-webfont.ttf");
			Button button1 = (Button) findViewById(R.id.btn_recruit);
			Button button2 = (Button) findViewById(R.id.btn_chat);
			Button button3 = (Button) findViewById(R.id.btn_matchup);
			Button button4 = (Button) findViewById(R.id.btn_guest);
			Button button5 = (Button) findViewById(R.id.btn_business);
			Button button6 = (Button) findViewById(R.id.btn_BPM_Check_In);
			Button button7 = (Button) findViewById(R.id.btn_logout);
			button1.setTypeface(font);
			button2.setTypeface(font);
			button3.setTypeface(font);
			button4.setTypeface(font);
			button5.setTypeface(font);
			button6.setTypeface(font);
			button7.setTypeface(font);
			Button button8 = (Button) findViewById(R.id.btn_Dashboard);
			button8.setTypeface(font);
//			Button button9 = (Button) findViewById(R.id.btn_MATCHUPBOOK);
//			button9.setTypeface(font);
			NormalSmartImageView profileimageview = (NormalSmartImageView) findViewById(R.id.userimage);
			myProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

			try {
				profileimageview.setImageUrl(sharedPreferences.getString(
						"profileImage", ""));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
			TextView username = (TextView) findViewById(R.id.Chatusername);
			username.setText(sharedPreferences.getString("username", ""));

			flag = true;
			if (AppUtils.isNetworkAvailable(Dashboard_Activity.this))
				new getDashboarddetailsAsync().execute();
			else
				AppUtils.ShowAlertDialog(Dashboard_Activity.this,
						"No Internet Connection Available.");

			// Timer t = new Timer();
			// t.schedule(new TimerTask() {
			// @Override
			// public void run() {
			// if(AppUtils.isNetworkAvailable(Dashboard_Activity.this))
			// new getDashboarddetailsAsync().execute();
			// }
			// }, 0, 30000);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
	}// end of oncreate()

	// JSON AsyncTask for saveUserDetails upload
	class getDashboarddetailsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
			myProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("user_id",
					sharedPreferences.getString("userid", "")));
			nameValuePair.add(new BasicNameValuePair("sec_user",
					sharedPreferences.getString("username", "")));
			nameValuePair.add(new BasicNameValuePair("sec_pass",
					sharedPreferences.getString("password", "")));

			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/dashbord_api/dashboard_widget");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseText + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
				AirbrakeNotifier.notify(ex);
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void args) {
			JSONObject jresponse = null;
			try {
				jresponse = new JSONObject(responseString);

				responseText = jresponse.getString("message");
				String status = jresponse.getString("status");

				if (responseText.equals("success") && status.equals("ok")) {
					JSONObject json = new JSONObject(responseString);
					JSONArray activityArray = null;
					dashboardArrayList.clear();
					JSONObject jso = (JSONObject) json
							.getJSONObject("speedfilter");
					DashboardVO dashboardVO = new DashboardVO();
					if (!jso.isNull("filtercount"))
						dashboardVO.count = jso.getString("filtercount");
					if (!jso.isNull("filtericon"))
						dashboardVO.icon = jso.getString("filtericon");
					if (!jso.isNull("filtertitle"))
						dashboardVO.title = jso.getString("filtertitle");
					if (!jso.isNull("filtercolor"))
						dashboardVO.backcolor = jso.getString("filtercolor");
					dashboardArrayList.add(dashboardVO);

					JSONObject jso2 = (JSONObject) (JSONObject) json
							.getJSONObject("points");
					DashboardVO dashboardVO2 = new DashboardVO();
					if (!jso2.isNull("pointcount"))
						dashboardVO2.count = jso2.getString("pointcount");
					if (!jso2.isNull("pointicon"))
						dashboardVO2.icon = jso2.getString("pointicon");
					if (!jso2.isNull("pointtitle"))
						dashboardVO2.title = jso2.getString("pointtitle");
					if (!jso2.isNull("pointcolor"))
						dashboardVO2.backcolor = jso2.getString("pointcolor");
					dashboardArrayList.add(dashboardVO2);

					JSONObject jso3 = (JSONObject) json
							.getJSONObject("captainslist");
					DashboardVO dashboardVO3 = new DashboardVO();
					if (!jso3.isNull("captainscount"))
						dashboardVO3.count = jso3.getString("captainscount");
					if (!jso3.isNull("captainsicon"))
						dashboardVO3.icon = jso3.getString("captainsicon");
					if (!jso3.isNull("captainstitle"))
						dashboardVO3.title = jso3.getString("captainstitle");
					if (!jso3.isNull("captainscolor"))
						dashboardVO3.backcolor = jso3
								.getString("captainscolor");
					dashboardArrayList.add(dashboardVO3);

					JSONObject jso4 = (JSONObject) json
							.getJSONObject("license");
					DashboardVO dashboardVO4 = new DashboardVO();
					if (!jso4.isNull("licensecount"))
						dashboardVO4.count = jso4.getString("licensecount");
					if (!jso4.isNull("licenseicon"))
						dashboardVO4.icon = jso4.getString("licenseicon");
					if (!jso4.isNull("licensetitle"))
						dashboardVO4.title = jso4.getString("licensetitle");
					if (!jso4.isNull("licensecolor"))
						dashboardVO4.backcolor = jso4.getString("licensecolor");
					dashboardArrayList.add(dashboardVO4);

					JSONObject jso5 = (JSONObject) json
							.getJSONObject("BPMeventregistration");
					DashboardVO dashboardVO5 = new DashboardVO();
					if (!jso5.isNull("BPMeventregistrationcount"))
						dashboardVO5.count = jso5
								.getString("BPMeventregistrationcount");
					if (!jso5.isNull("BPMeventregistrationicon"))
						dashboardVO5.icon = jso5
								.getString("BPMeventregistrationicon");
					if (!jso5.isNull("BPMeventregistrationtitle"))
						dashboardVO5.title = jso5
								.getString("BPMeventregistrationtitle");
					if (!jso5.isNull("BPMeventregistrationcolor"))
						dashboardVO5.backcolor = jso5
								.getString("BPMeventregistrationcolor");
					dashboardArrayList.add(dashboardVO5);

					JSONObject jso6 = (JSONObject) json
							.getJSONObject("BPMattendance");
					DashboardVO dashboardVO6 = new DashboardVO();
					if (!jso6.isNull("BPMattendancecount"))
						dashboardVO6.count = jso6
								.getString("BPMattendancecount");
					if (!jso6.isNull("BPMattendanceicon"))
						dashboardVO6.icon = jso6.getString("BPMattendanceicon");
					if (!jso6.isNull("BPMattendancetitle"))
						dashboardVO6.title = jso6
								.getString("BPMattendancetitle");
					if (!jso6.isNull("BPMattendancecolor"))
						dashboardVO6.backcolor = jso6
								.getString("BPMattendancecolor");
					dashboardArrayList.add(dashboardVO6);

					myProgressBar.setVisibility(View.GONE);

					if (dashboardArrayList.size() > 0)
						setListValues(dashboardArrayList);

				}// end of if
				else {
					AppUtils.ShowAlertDialog(Dashboard_Activity.this, "Error :"
							+ responseText);
				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				myProgressBar.setVisibility(View.GONE);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();

				myProgressBar.setVisibility(View.GONE);
				flag = false;
				//AppUtils.ShowAlertDialog(Dashboard_Activity.this,
						//"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

			try {

				if (!flag)
					return;
				else {
					final Handler handler = new Handler();
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						public void run() {
							handler.post(new Runnable() {
								public void run() {
									if (!flag)
										finish();
									else {

										if (AppUtils
												.isNetworkAvailable(Dashboard_Activity.this))
											new getDashboarddetailsAsync()
													.execute();
									}

								}
							});
						}
					}, 60000);
				}
			} catch (Exception e) {
				// TODO: handle exception
				flag = false;
				myProgressBar.setVisibility(View.GONE);
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

		}// end of onpost()
	}// ends of Async task

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			flag = false;
			Intent intent = new Intent(Dashboard_Activity.this,
					UserProfileActivity.class);
			startActivity(intent);
			finish();
		}
		// else if (view.getId() == R.id.layoutprofile) {
		// MENU_ITEM_SELECTED = "PROFILE";
		// Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
		// ProfileActivity.class);
		// startActivity(intent);
		// }
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			flag = false;
			Intent intent = new Intent(Dashboard_Activity.this,
					NewRecruitActivity.class);
			startActivity(intent);
			finish();
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			flag = false;
			Intent intent = new Intent(Dashboard_Activity.this,
					AddBusinessActivity.class);
			startActivity(intent);
			finish();
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			flag = false;
			Intent intent = new Intent(Dashboard_Activity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
			finish();
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			flag = false;
			Intent intent = new Intent(Dashboard_Activity.this,
					MatchupActivity.class);
			startActivity(intent);
			finish();
		}
		// else if (view.getId() == R.id.Layoutlocator) {
		// MENU_ITEM_SELECTED = "OFFICE LOCATOR";
		// Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
		// OfficeLocatorActivity.class);
		// startActivity(intent);
		// }
		else if (view.getId() == R.id.Layoutlogout) {
			MENU_ITEM_SELECTED = "LOGOUT";
			flag = false;
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("userid", "");
			editor.putString("password", "");
			editor.putString("username", "");
			editor.putString("profileImage", "");
			editor.commit();
			Intent intent = new Intent(Dashboard_Activity.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
		} else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			flag = false;
			Intent intent = new Intent(Dashboard_Activity.this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);
			finish();
		} else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			isExpanded = false;
			new CollapseAnimation(slidingPanel, panelWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0,
					0.0f, menulayout);

		}
//		else if (view.getId() == R.id.LayoutMATCHUPBOOK) {
//			MENU_ITEM_SELECTED = "Dashboard";
//			Intent intent = new Intent(this,
//					MatchupBookListActivity.class);
//			startActivity(intent);
//
//		}
	}// end of menu

	// **********************************************************
	void setListValues(ArrayList<DashboardVO> dashboardArrayList) {
		try {

			RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
			mainlayout.removeAllViews();
			Typeface font = Typeface.createFromAsset(getAssets(),
					"fontawesome-webfont.ttf");
			Typeface type = Typeface.createFromAsset(
					Dashboard_Activity.this.getAssets(),
					"fonts/RobotoSlab-Bold.ttf");
			for (int i = 0; i < dashboardArrayList.size(); i++) {

				// ******************************************
				RelativeLayout layout = new RelativeLayout(this);
				layout.setId(100 + i);
				// layout.setPadding(1, 1, 1, 50);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = 15;
				layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
				// layoutParams.bottomMargin=100;
				if (i != 0)
					layoutParams.addRule(RelativeLayout.BELOW, 99 + i);

				// layout.setBackgroundResource(R.drawable.lightborder);
				layout.setLayoutParams(layoutParams);

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						layoutParams.MATCH_PARENT, 350);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						devicewidth, 350);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params7 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params8 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				// params1.setMargins(5,25,5,55);
				DashboardVO dashboardVO = dashboardArrayList.get(i);

				RelativeLayout leftlayout = new RelativeLayout(this);
				leftlayout.setId(1000 + i);
				leftlayout.setBackgroundColor(Color
						.parseColor(dashboardVO.backcolor));
				// params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				params1.setMargins(15, 5, 15, 5);
				// params1.topMargin = 30;
				leftlayout.setLayoutParams(params1);

				TextView tv1 = new TextView(this);
				params3.topMargin = 10;
				params3.leftMargin = 10;
				tv1.setTypeface(font);
				tv1.setTextSize(50);
				tv1.setId(3);
				tv1.setTextColor(Color.parseColor("#FFFFFF"));
				// tv1.setText("&#x"+dashboardVO.icon+";");
				tv1.setText(new String(Character.toChars(Integer.parseInt(
						dashboardVO.icon, 16))));

				TextView tv2 = new TextView(this);
				params4.topMargin = 10;
				params4.leftMargin = 20;
				params4.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
				tv2.setTypeface(type);
				tv2.setTextSize(27);
				tv2.setId(4);
				tv2.setTextColor(Color.parseColor("#FFFFFF"));
				tv2.setText(dashboardVO.title);

				TextView tv3 = new TextView(this);
				params5.topMargin = 20;
				params5.leftMargin = 20;
				params5.addRule(RelativeLayout.BELOW, tv2.getId());
				params5.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
				tv3.setTypeface(type);
				tv3.setTextSize(27);
				tv3.setId(5);
				tv3.setTextColor(Color.parseColor("#FFFFFF"));
				tv3.setText(dashboardVO.count);

				leftlayout.addView(tv1, params3);
				leftlayout.addView(tv2, params4);
				leftlayout.addView(tv3, params5);

				layout.addView(leftlayout, params1);
				// layout.addView(rightlayout, params2);
				// *******************************************
				mainlayout.addView(layout, layoutParams);

				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});

			}// end of for
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}// end of list values
		// **********************************************************

}// end of Activity
