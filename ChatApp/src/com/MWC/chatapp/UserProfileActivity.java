package com.MWC.chatapp;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.airbrake.AirbrakeNotifier;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import app.tabsample.SmartImageView.RoundedImageView;
import applicationVo.AppUtils;
import applicationVo.UserProfileDataAsync;

public class UserProfileActivity extends Activity {

	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
			LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,
			LayoutBPM_Check_in, LayoutDashboard, menulayout;
	public static String MENU_ITEM_SELECTED = "";

	ListView usergrouplist;
	public ArrayList<ChatGroupVO> usergroupArrayList = new ArrayList<ChatGroupVO>();
	ProgressDialog pDialog;
	String responseString = "";
	String responseString2 = "";

	TextView tx;
	SharedPreferences sharedPreferences;
	String Userid = "", Uname = "", Password = "";

	FrameLayout mainFrameLyout;
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	Button menuBtn;
	private DisplayMetrics metrics;
	private RelativeLayout slidingPanel;
	int panelWidth = 0;
	private boolean isExpanded;

	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	private ProgressDialog mProgressDialog;

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS1:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Processing request, Please wait ...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;

		default:
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");
			WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiLock wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL,
					"MyWifiLock");
			wifiLock.acquire();

			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			Userid = sharedPreferences.getString("userid", "");
			Uname = sharedPreferences.getString("username", "");
			Password = sharedPreferences.getString("password", "");

			metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			panelWidth = (int) ((metrics.widthPixels) * 0.75);
			slidingPanel = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
			slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
					.getLayoutParams();
			slidingPanelParameters.width = metrics.widthPixels;
			slidingPanel.setLayoutParams(slidingPanelParameters);

			menulayout = (RelativeLayout) findViewById(R.id.Layoutmenubaar);
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
								Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0,
								0.0f);
					} else {
						isExpanded = false;

						// Collapse
						new CollapseAnimation(slidingPanel, panelWidth,
								TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
								TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0,
								0.0f, 0, 0.0f, menulayout);
					}
				}
			});
			Chatlayout = (RelativeLayout) findViewById(R.id.chatlayout);
			// layoutProfile = (RelativeLayout)
			// findViewById(R.id.layoutprofile);
			LayoutRecruit = (RelativeLayout) findViewById(R.id.Layoutrecruit);
			LayoutBusiness = (RelativeLayout) findViewById(R.id.Layoutbusiness);
			LayoutGuest = (RelativeLayout) findViewById(R.id.Layoutguest);
			LayoutMatchup = (RelativeLayout) findViewById(R.id.Layoutmatchup);
			// LayoutLocator = (RelativeLayout)
			// findViewById(R.id.Layoutlocator);
			LayoutLogout = (RelativeLayout) findViewById(R.id.Layoutlogout);
			LayoutBPM_Check_in = (RelativeLayout) findViewById(R.id.LayoutBPM_Check_In);
			LayoutDashboard = (RelativeLayout) findViewById(R.id.DashboardLayout);

			Typeface font = Typeface.createFromAsset(getAssets(),
					"fontawesome-webfont.ttf");
			Button button1 = (Button) findViewById(R.id.btn_recruit);
			Button button2 = (Button) findViewById(R.id.btn_chat);
			Button button3 = (Button) findViewById(R.id.btn_matchup);
			Button button4 = (Button) findViewById(R.id.btn_guest);
			Button button5 = (Button) findViewById(R.id.btn_business);
			Button button6 = (Button) findViewById(R.id.btn_BPM_Check_In);
			Button button7 = (Button) findViewById(R.id.btn_logout);
			Button button8 = (Button) findViewById(R.id.btn_Dashboard);
			button8.setTypeface(font);
			// Button button9 = (Button) findViewById(R.id.btn_MATCHUPBOOK);
			// button9.setTypeface(font);
			button1.setTypeface(font);
			button2.setTypeface(font);
			button3.setTypeface(font);
			button4.setTypeface(font);
			button5.setTypeface(font);
			button6.setTypeface(font);
			button7.setTypeface(font);

			NormalSmartImageView profileimageview = (NormalSmartImageView) findViewById(R.id.userimage);

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

			Button alldata_Button = (Button) findViewById(R.id.All_UserList_Button);
			alldata_Button.setTypeface(font);
			alldata_Button.setVisibility(View.VISIBLE);

			try {
				TextView textView = (TextView) findViewById(R.id.profiletitle);
				UserProfileDataAsync async = UserProfileDataAsync
						.getInstance(UserProfileActivity.this);
				textView.setTypeface(async.getFountType());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

			alldata_Button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent logIntent = new Intent(getApplicationContext(),
							All_User_List_Activity.class);
					startActivity(logIntent);
				}
			});

			if (AppUtils.isNetworkAvailable(UserProfileActivity.this)) {
				new UserProfile_groupdata_fetch().execute();

			} else
				AppUtils.ShowAlertDialog(UserProfileActivity.this,
						"No Internet Connection Available.");

			// ***************************************

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}// end of onCreate

	// ***************************Async task Class
	class UserProfile_groupdata_fetch extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		protected String doInBackground(String... aurl) {

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("user_id", Userid));
			nameValuePair.add(new BasicNameValuePair("sec_user", Uname));
			nameValuePair.add(new BasicNameValuePair("sec_pass", Password));

			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/chat_api/userListwebservice/");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
				AirbrakeNotifier.notify(ex);
			}
			return "";
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String str_resp) {
			super.onPostExecute(str_resp);

			if (responseString.equals("No any group created yet.")
					|| responseString.equals("fail")) {
				// new UserProfile_userdata_fetch().execute();
				final AlertDialog alertDialog = new AlertDialog.Builder(
						UserProfileActivity.this).create();
				alertDialog.setTitle("No any group created yet.");
				alertDialog.setMessage(" !!!");
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								alertDialog.dismiss();
							}
						});
				alertDialog.show();
			} else {

				try {

					JSONObject jresponse = null;
					jresponse = new JSONObject(responseString);

					String responseText = jresponse.getString("message");
					String status = jresponse.getString("status");

					if (responseText.equals("success") && status.equals("ok")) {
						JSONObject json = new JSONObject(responseString);
						JSONArray activityArray = null;

						activityArray = json.getJSONArray("userlist");

						for (int i = 0; i < activityArray.length(); i++) {

							JSONObject jso = (JSONObject) activityArray.get(i);

							ChatGroupVO chatgroup = new ChatGroupVO();

							if (!jso.isNull("groupid")) {
								chatgroup.groupid = jso.getString("groupid");
								chatgroup.isGroup = "true";
							}

							if (!jso.isNull("userid"))
								chatgroup.userid = jso.getString("userid");

							if (!jso.isNull("username"))
								chatgroup.username = jso.getString("username");

							if (!jso.isNull("profileImage"))
								chatgroup.profileImage = jso
										.getString("profileImage");

							if (!jso.isNull("groupmemberdetail"))
								chatgroup.groupmemberdetail = jso
										.getString("groupmemberdetail");

							if (!jso.isNull("groupName"))
								chatgroup.groupName = jso
										.getString("groupName");

							if (!jso.isNull("groupIcon"))
								chatgroup.groupIcon = jso
										.getString("groupIcon");

							if (!jso.isNull("messageid"))
								chatgroup.messageid = jso
										.getString("messageid");

							if (!jso.isNull("message"))
								chatgroup.message = jso.getString("message");

							if (!jso.isNull("attach_image"))
								chatgroup.attach_image = jso
										.getString("attach_image");

							if (!jso.isNull("attach_file"))
								chatgroup.attach_file = jso
										.getString("attach_file");

							if (!jso.isNull("firstname"))
								chatgroup.firstname = jso
										.getString("firstname");

							if (!jso.isNull("lastname"))
								chatgroup.lastname = jso.getString("lastname");

							if (!jso.isNull("msg_time")) {
								DateTimeDifference dt = new DateTimeDifference();
								String date = dt.findDifference(jso
										.getString("msg_time"));
								chatgroup.msg_time = date;
							}
							usergroupArrayList.add(chatgroup);
						}
					}
					if (usergroupArrayList.size() > 0) {
						if (sharedPreferences.getString("NOTIFICATIONSTRING",
								"").equalsIgnoreCase("TRUE")) {
							GCMIntentService.count = 0;

							ChatGroupVO groupVO = usergroupArrayList.get(0);
							String group_id = "";
							String group_name = "";
							String group_icon = "";
							String is_group = "";
							// String receiverid="";

							SharedPreferences.Editor editor = sharedPreferences
									.edit();
							editor.putString("NOTIFICATIONSTRING", "FALSE");
							editor.commit();

							if (groupVO.isGroup.equals("true")) {
								group_id = groupVO.groupid;
								group_name = groupVO.groupName;
								group_icon = groupVO.groupIcon;
								is_group = groupVO.isGroup;
								// receiverid=chat.
							} else {
								group_id = groupVO.userid;
								group_name = groupVO.firstname + " "
										+ groupVO.lastname;
								group_icon = groupVO.profileImage;
								is_group = groupVO.isGroup;
							}

							Intent gropIntent = new Intent(
									getApplicationContext(), ChatActivity.class);
							gropIntent.putExtra("group_id", group_id);
							gropIntent.putExtra("group_name", group_name);
							gropIntent.putExtra("group_icon", group_icon);
							gropIntent.putExtra("is_group", is_group);
							startActivity(gropIntent);

						} else
							setListValues(usergroupArrayList);
					}
					if (mProgressDialog != null)
						mProgressDialog.dismiss();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();

					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(
							UserProfileActivity.this,
							"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
					AirbrakeNotifier.notify(e);
				}
			}

		}// end of else

	}// end of postExecute

	// ***************************End Async task Class --1

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			isExpanded = false;
			new CollapseAnimation(slidingPanel, panelWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0,
					0.0f, menulayout);
		}
		// else if (view.getId() == R.id.layoutprofile) {
		// MENU_ITEM_SELECTED = "PROFILE";
		// Intent intent = new Intent(UserProfileActivity.this,
		// ProfileActivity.class);
		// startActivity(intent);
		// }
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			Intent intent = new Intent(UserProfileActivity.this,
					NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(UserProfileActivity.this,
					AddBusinessActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			Intent intent = new Intent(UserProfileActivity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(UserProfileActivity.this,
					MatchupActivity.class);
			startActivity(intent);
		}
		// else if (view.getId() == R.id.Layoutlocator) {
		// MENU_ITEM_SELECTED = "OFFICE LOCATOR";
		// Intent intent = new Intent(UserProfileActivity.this,
		// OfficeLocatorActivity.class);
		// startActivity(intent);
		// }
		else if (view.getId() == R.id.Layoutlogout) {
			MENU_ITEM_SELECTED = "LOGOUT";
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("userid", "");
			editor.putString("password", "");
			editor.putString("username", "");
			editor.putString("profileImage", "");
			editor.commit();
			Intent intent = new Intent(UserProfileActivity.this,
					LoginActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(UserProfileActivity.this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(UserProfileActivity.this,
					Dashboard_Activity.class);
			startActivity(intent);
		}
		// else if (view.getId() == R.id.LayoutMATCHUPBOOK) {
		// MENU_ITEM_SELECTED = "Dashboard";
		// Intent intent = new Intent(this,
		// MatchupBookListActivity.class);
		// startActivity(intent);
		//
		// }
	}// end of menu

	// **********************************************************
	void setListValues(ArrayList<ChatGroupVO> usergroupArrayList) {
		try {

			RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
			Typeface type = Typeface.createFromAsset(
					UserProfileActivity.this.getAssets(),
					"fonts/calibribold.ttf");
			Typeface type2 = Typeface.createFromAsset(
					UserProfileActivity.this.getAssets(), "fonts/calibri.ttf");
			for (int i = 0; i < usergroupArrayList.size(); i++) {

				final ChatGroupVO groupVO = usergroupArrayList.get(i);

				// ******************************************
				RelativeLayout layout = new RelativeLayout(this);
				layout.setId(100 + i);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				layoutParams.topMargin = 20;
				// layoutParams.bottomMargin=100;
				if (i != 0)
					layoutParams.addRule(RelativeLayout.BELOW, 99 + i);

				layout.setBackgroundResource(R.drawable.lightborder);
				layout.setLayoutParams(layoutParams);
				// layout.setBackgroundResource(R.drawable.lightborder);

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						130, 130);
				RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(
						13, 20);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				// params1.setMargins(5,25,5,55);
				RoundedImageView imageView = new RoundedImageView(
						UserProfileActivity.this);
				imageView.setId(1);
				imageView.setImageResource(R.drawable.chaticon);

				ImageView image3 = new ImageView(UserProfileActivity.this);
				image3.setId(9);
				params5.addRule(RelativeLayout.BELOW, imageView.getId());

				TextView tv4 = new TextView(this);
				params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params4.rightMargin = 10;
				tv4.setId(4);
				tv4.setTypeface(type2);
				tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				// tv4.setTextColor(Color.parseColor("#000000"));
				tv4.setText(groupVO.msg_time);

				final TextView tv2 = new TextView(this);
				params2.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
				params2.addRule(RelativeLayout.BELOW, tv4.getId());
				params2.leftMargin = 5;
				tv2.setId(2);
				tv2.setTextSize(22);
				tv2.setTextColor(Color.parseColor("#000000"));
				tv2.setTypeface(type);

				if (groupVO.isGroup.equalsIgnoreCase("true")) {
					imageView.setImageUrl(groupVO.groupIcon);
					tv2.setText(groupVO.groupName);
				}// end of if
				else {
					imageView.setImageUrl(groupVO.profileImage);
					tv2.setText(groupVO.firstname + " " + groupVO.lastname);
				}// end of if

				String msg = stripHtml(groupVO.message);
				msg = msg.replaceAll("[\\\t|\\\n|\\\r]", " ");

				if (msg == null || msg.equalsIgnoreCase(""))
					msg = "";
				else
					msg = msg.substring(0, Math.min(50, msg.length())) + "...";

				TextView tv3 = new TextView(this);
				params3.addRule(RelativeLayout.BELOW, 2);
				params3.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
				params3.topMargin = 7;
				params3.rightMargin = 20;
				params3.leftMargin = 5;
				// params3.bottomMargin=50;
				tv3.setTypeface(type2);
				tv3.setId(3);
				tv3.setTextColor(Color.parseColor("#000000"));
				tv3.setText(msg);

				layout.addView(imageView, params1);
				layout.addView(image3, params5);
				layout.addView(tv2, params2);
				layout.addView(tv3, params3);
				layout.addView(tv4, params4);
				// *******************************************
				mainlayout.addView(layout, layoutParams);

				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String group_id = "";
						String group_name = "";
						String group_icon = "";
						String is_group = "";
						// String receiverid="";

						if (groupVO.isGroup.equals("true")) {
							group_id = groupVO.groupid;
							group_name = groupVO.groupName;
							group_icon = groupVO.groupIcon;
							is_group = groupVO.isGroup;
							// receiverid=chat.
						} else {
							group_id = groupVO.userid;
							group_name = groupVO.firstname + " "
									+ groupVO.lastname;
							group_icon = groupVO.profileImage;
							is_group = groupVO.isGroup;
						}
						Intent gropIntent = new Intent(getApplicationContext(),
								ChatActivity.class);
						gropIntent.putExtra("group_id", group_id);
						gropIntent.putExtra("group_name", group_name);
						gropIntent.putExtra("group_icon", group_icon);
						gropIntent.putExtra("is_group", is_group);
						startActivity(gropIntent);
					}
				});

			}// end of for

			RelativeLayout.LayoutParams params76 = new RelativeLayout.LayoutParams(
					130, 130);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}// end of list values
		// **********************************************************

	public String stripHtml(String html) {
		return Html.fromHtml(html).toString();
	}

}// end of activity
