package com.MWC.chatapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AgentListVO;
import applicationVo.AppUtils;
import applicationVo.BPMondateVO;
import applicationVo.ChatSingleton;
import applicationVo.PhoneNumberFilter;
import applicationVo.PhoneNumberTextWatcher;

import com.loopj.android.airbrake.AirbrakeNotifier;

public class AddNewGuestActivity extends Activity {
	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
			LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,
			LayoutBPM_Check_in, MAINSEARCHLAYOUT, ALLMAINLAYOUT, menulayout,
			INVITEDLAYOUT, IMAGELAYOUT;
	RelativeLayout mainlayout;
	public static String MENU_ITEM_SELECTED = "", OPTIONSELECTED = "";;

	Button canclebtn;
	Button addnewtrainee;
	public String traineeuserid = "";
	public ArrayList<AgentListVO> agentvoArrayList = new ArrayList<AgentListVO>();
	// public ArrayList<AgentListVO> agentByTypeArrayList = new
	// ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> sortUserlistArrayList = new ArrayList<AgentListVO>();
	public String agentresponseString = "";
	Button AddAgentbtn;
	public AlertDialog myAlertDialog = null;

	public ArrayList<BPMondateVO> bpmondateaArrayList = new ArrayList<BPMondateVO>();
	public ArrayList<BPMondateVO> sortArrayList = new ArrayList<BPMondateVO>();
	public EditText searchEdittext, Email;
	FrameLayout mainFrameLyout;
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	public ChatSingleton chatSingleton;
	Button menuBtn;
	private DisplayMetrics metrics;
	private RelativeLayout slidingPanel;
	int panelWidth = 0;
	private boolean isExpanded;
	public EditText guestFirstnameEdittext, guestLastnameEdittext,
			guestphoneEdittext, bpmDateEdittext, inviterFirstnameEdittext,
			bpmonDateEdittext, inviterLastnameEdittext;
	SharedPreferences sharedPreferences;
	public String responseText = "", bpmkey = "";
	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	private ProgressDialog mProgressDialog;

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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnewguest);
		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");
			chatSingleton = ChatSingleton.getInstance();
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
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
			// Button button9 = (Button) findViewById(R.id.btn_MATCHUPBOOK);
			// button9.setTypeface(font);
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

			guestFirstnameEdittext = (EditText) findViewById(R.id.guestnameedt);
			guestLastnameEdittext = (EditText) findViewById(R.id.lastnameedt);
			guestphoneEdittext = (EditText) findViewById(R.id.phoneedt);
			bpmDateEdittext = (EditText) findViewById(R.id.bpmdateedt);
			inviterFirstnameEdittext = (EditText) findViewById(R.id.invitedbyedt);
			inviterLastnameEdittext = (EditText) findViewById(R.id.invitedlastedt);
			bpmonDateEdittext = (EditText) findViewById(R.id.bpmondateEdittext);
			bpmDateEdittext.setText(chatSingleton.getDate(sharedPreferences
					.getString("usertimezone", "UTC")));

			Email = (EditText) findViewById(R.id.EMAILeedt);

			INVITEDLAYOUT = (RelativeLayout) findViewById(R.id.invitedlastlayout);
			IMAGELAYOUT = (RelativeLayout) findViewById(R.id.image4layout);

			bpmDateEdittext.setFocusable(false);
			bpmDateEdittext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DATETIMEPICKER();
				}
			});

			try {
				guestphoneEdittext
						.addTextChangedListener(new PhoneNumberTextWatcher());
				guestphoneEdittext.setFilters(new InputFilter[] {
						new PhoneNumberFilter(),
						new InputFilter.LengthFilter(12) });
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
			Button submitbtn = (Button) findViewById(R.id.Guestsubmitbtn);
			submitbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (AppUtils.isNetworkAvailable(AddNewGuestActivity.this)) {
						if (emptyCheck(guestFirstnameEdittext)
								&& emptyCheck(guestLastnameEdittext)
								&& emptyCheck(guestphoneEdittext)
								&& emptyCheck(bpmDateEdittext)
								&& emptyCheck(inviterFirstnameEdittext)
								&& (traineeuserid.equalsIgnoreCase("") ? inviterLastnameEdittext
										.getText().toString().equals("") ? false
										: true
										: true)) {
							if (isValidMobile(guestphoneEdittext.getText()
									.toString()))

								if (bpmonDateEdittext.getHint().toString()
										.equalsIgnoreCase("Select BPM")
										|| !bpmonDateEdittext.getText()
												.toString()
												.equalsIgnoreCase("")) {
									if (bpmonDateEdittext.getText().toString()
											.equalsIgnoreCase(""))
										AppUtils.ShowAlertDialog(
												AddNewGuestActivity.this,
												"Please Select BPM Location.");
									else if (Email.getText().toString()
											.equals(""))
										new myTask_saveUserDetails_call()
												.execute();
									else if (isValidEmail(Email.getText()
											.toString()))
										new myTask_saveUserDetails_call()
												.execute();
									else
										AppUtils.ShowAlertDialog(
												AddNewGuestActivity.this,
												"Please Enter Valid Email.");
								} else
									AppUtils.ShowAlertDialog(
											AddNewGuestActivity.this,
											"There are no BPMs on that date, Guest can not be added!!!");
							else
								AppUtils.ShowAlertDialog(
										AddNewGuestActivity.this,
										"Contact must be 10 digits length");

						}// end of if
						else
							AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
									"All field are mandatory!!!");

					}// end of if for internet check
					else
						AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
								"No Internet Connection Available.");

				}
			});

			if (AppUtils.isNetworkAvailable(AddNewGuestActivity.this)) {
				new getAgentData().execute();
			}// end of if for internet check
			else {
				AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
						"No Internet Connection Available.");
				bpmonDateEdittext.setText("");
			}

			bpmonDateEdittext.setFocusable(false);
			bpmonDateEdittext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (bpmondateaArrayList != null
							&& bpmondateaArrayList.size() > 0) {
						OPTIONSELECTED = "BPMLIST";
						sortArrayList.addAll(bpmondateaArrayList);
						searchEdittext.setText("");
						// myScrollView.smoothScrollTo(0, 0);
						SearchTrainee(sortArrayList);
					}
				}
			});
			ALLMAINLAYOUT = (RelativeLayout) findViewById(R.id.MAILALL_LAYOUT);
			MAINSEARCHLAYOUT = (RelativeLayout) findViewById(R.id.TRAINEELIST_MAIN_LAYOUT);
			searchEdittext = (EditText) findViewById(R.id.TRAINEE_SEARCH_EDITTEXT);
			// ************************************************
			searchEdittext.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
					// Abstract Method of TextWatcher Interface.
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// Abstract Method of TextWatcher Interface.
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					int textlength = searchEdittext.getText().length();

					if (OPTIONSELECTED.equalsIgnoreCase("USERLIST")) {
						sortUserlistArrayList.clear();
						for (int i = 0; i < agentvoArrayList.size(); i++) {
							AgentListVO agentVo = agentvoArrayList.get(i);
							String compairstr = agentVo.firstname + " "
									+ agentVo.lastname + "(" + agentVo.agent_id
									+ ")";
							if (textlength <= compairstr.length()) {
								if (searchEdittext
										.getText()
										.toString()
										.equalsIgnoreCase(
												(String) compairstr
														.subSequence(0,
																textlength))) {
									sortUserlistArrayList.add(agentVo);
								}
							}
						}// end of for
						SearchUserllist(sortUserlistArrayList);
					}// end of if
					else if (OPTIONSELECTED.equalsIgnoreCase("BPMLIST")) {
						sortArrayList.clear();
						for (int i = 0; i < bpmondateaArrayList.size(); i++) {
							BPMondateVO agentVo = bpmondateaArrayList.get(i);
							String compairstr = agentVo.bpm_location;
							if (textlength <= compairstr.length()) {
								if (searchEdittext
										.getText()
										.toString()
										.equalsIgnoreCase(
												(String) compairstr
														.subSequence(0,
																textlength))) {
									sortArrayList.add(agentVo);
								}
							}
						}// end of for
						SearchTrainee(sortArrayList);
					}// end of else

				}
			});

			inviterFirstnameEdittext.setFocusable(false);
			inviterFirstnameEdittext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (agentvoArrayList != null && agentvoArrayList.size() > 0) {
						OPTIONSELECTED = "USERLIST";
						sortUserlistArrayList.clear();
						sortUserlistArrayList.addAll(agentvoArrayList);
						searchEdittext.setText("");
						SearchUserllist(sortUserlistArrayList);
					} else
						AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
								"Please wait while data is loading..");
				}
			});

			// ***********************************************
			canclebtn = (Button) findViewById(R.id.TRAINEE_CANCLE_BTN);
			canclebtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// mainlayout = (RelativeLayout)
					// findViewById(R.id.TRAINEELIST_LAYOUT);
					mainlayout.removeAllViews();
					MAINSEARCHLAYOUT.setVisibility(View.GONE);
					ALLMAINLAYOUT.setVisibility(View.VISIBLE);
				}
			});

			addnewtrainee = (Button) findViewById(R.id.TRAINEE_ADD_BTN);
			addnewtrainee.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ShowDialog();
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
			AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
					"Something is gone wrong..!,please retry.");
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}// -------------------- end of onCreate()

	// JSON AsyncTask for saveUserDetails upload
	class myTask_saveUserDetails_call extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
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

			nameValuePair.add(new BasicNameValuePair("guest_fname",
					guestFirstnameEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("guest_lname",
					guestLastnameEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("phone",
					guestphoneEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("guest_email", Email
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("bpm_date",
					bpmDateEdittext.getText().toString()));
			if (traineeuserid.equals(""))
				nameValuePair.add(new BasicNameValuePair("userlist",
						inviterFirstnameEdittext.getText().toString()));
			else
				nameValuePair.add(new BasicNameValuePair("userlist",
						traineeuserid));
			nameValuePair.add(new BasicNameValuePair("BPM_SMD",
					inviterLastnameEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("bpm_id", bpmkey));
			// nameValuePair.add(new BasicNameValuePair("BPM_SMD", bpmkey));

			try {
				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/captains_api/add_captain");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseText = EntityUtils.toString(response.getEntity());
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
				jresponse = new JSONObject(responseText);

				responseText = jresponse.getString("message");
				String status = jresponse.getString("status");

				if (responseText.equals("success") && status.equals("ok")) {
					AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
							"Saved Successfully.");
					clearData();
				} else {
					AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
							"Error :" + responseText);
				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(
						AddNewGuestActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");

				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		}// end of onpost()
	}// ends of Async task

	// ******************************Async task claass---GetAgent data BY TYPE
	public class getBPMonData extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected String doInBackground(String... strings) {
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("user_id",
					sharedPreferences.getString("userid", "")));
			nameValuePair.add(new BasicNameValuePair("sec_user",
					sharedPreferences.getString("username", "")));
			nameValuePair.add(new BasicNameValuePair("sec_pass",
					sharedPreferences.getString("password", "")));
			nameValuePair.add(new BasicNameValuePair("bpm_date",
					bpmDateEdittext.getText().toString()));
			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/captains_api/selectbpmoffice");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseText = EntityUtils.toString(response.getEntity());
				// System.out.println(agentresponseString +
				// "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
				AirbrakeNotifier.notify(ex);
			}

			return null;
		} // end of doInBackground();

		@Override
		protected void onPostExecute(String lenghtOfFile) {

			JSONObject jresponse = null;
			try {
				jresponse = new JSONObject(responseText);

				String message = jresponse.getString("message");
				String status = jresponse.getString("status");
				bpmondateaArrayList.clear();
				if (message.equals("success") && status.equals("ok")) {
					JSONObject json = new JSONObject(responseText);
					JSONArray activityArray = null;

					activityArray = json.getJSONArray("BPMList");

					for (int i = 0; i < activityArray.length(); i++) {

						JSONObject jso = (JSONObject) activityArray.get(i);

						BPMondateVO bpMondateVO = new BPMondateVO();

						if (!jso.isNull("bpm_id")) {
							bpMondateVO.bpm_key = jso.getString("bpm_id");
						}

						if (!jso.isNull("bpm_office"))
							bpMondateVO.bpm_location = jso
									.getString("bpm_office");

						bpmondateaArrayList.add(bpMondateVO);
					}// end of for
					if (bpmondateaArrayList.size() > 0) {
						bpmonDateEdittext.setFocusable(false);
						bpmkey = bpmondateaArrayList.get(0).bpm_key;
						bpmonDateEdittext
								.setText(bpmondateaArrayList.get(0).bpm_location);
						bpmonDateEdittext.setClickable(true);
						bpmonDateEdittext.setHint("Select BPM");
					}

				} else {
					bpmonDateEdittext.setFocusable(false);
					bpmonDateEdittext.setText("");
				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(
						AddNewGuestActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				bpmonDateEdittext.setFocusable(false);
				bpmonDateEdittext.setClickable(false);
				bpmonDateEdittext.setText("");
				bpmonDateEdittext.setHint("No BPM's On This Date");
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

		}
	}// end of getby Type

	// ******************************Async task claass---GetAgent data
	public class getAgentData extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected String doInBackground(String... strings) {
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			// nameValuePair.add(new BasicNameValuePair("userid",
			// sharedPreferences.getString("userid", "")));
			nameValuePair.add(new BasicNameValuePair("sec_user",
					sharedPreferences.getString("username", "")));
			nameValuePair.add(new BasicNameValuePair("sec_pass",
					sharedPreferences.getString("password", "")));

			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/profile_api/getUserlist");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				agentresponseString = EntityUtils
						.toString(response.getEntity());
				System.out.println(agentresponseString + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
				AirbrakeNotifier.notify(ex);
			}

			return null;
		} // end of doInBackground();

		@Override
		protected void onPostExecute(String lenghtOfFile) {
			String status = null;
			String usermsg = null;
			JSONObject jresponse = null;
			try {

				jresponse = new JSONObject(agentresponseString);
				agentvoArrayList.clear();
				usermsg = jresponse.getString("message");
				status = jresponse.getString("status");
				JSONArray activityArray = null;
				if (usermsg.equalsIgnoreCase("") && status.equals("ok")) {

					activityArray = jresponse.getJSONArray("userlist");

					for (int i = 0; i < activityArray.length(); i++) {

						JSONObject activityObject = (JSONObject) activityArray
								.get(i);
						AgentListVO cVo = new AgentListVO();

						if (!activityObject.isNull("id"))
							cVo.userid = activityObject.getString("id");

						if (!activityObject.isNull("username"))
							cVo.username = activityObject.getString("username");

						if (!activityObject.isNull("firstname"))
							cVo.firstname = activityObject
									.getString("firstname");
						if (!activityObject.isNull("lastname"))
							cVo.lastname = activityObject.getString("lastname");

						if (!activityObject.isNull("agent_id"))
							cVo.agent_id = activityObject.getString("agent_id");

						agentvoArrayList.add(cVo);
					}// end of for

				} else
					AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
							"ERROR: " + responseText);

				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				new getBPMonData().execute();

			} catch (Exception e) {
				// TODO: handle exception
				// myProgressBar.setVisibility(View.GONE);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(
						AddNewGuestActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

			// new group_chat_message().execute();
			// do stuff after posting data
		}
	}

	public void clearData() {
		guestFirstnameEdittext.setText("");
		guestLastnameEdittext.setText("");
		guestphoneEdittext.setText("");
		bpmDateEdittext.setText(chatSingleton.getDate(sharedPreferences
				.getString("usertimezone", "UTC")));
		inviterFirstnameEdittext.setText("");
		inviterLastnameEdittext.setText("");
		Email.setText("");

		if (bpmondateaArrayList.size() > 0) {
			bpmonDateEdittext.setFocusable(false);
			bpmonDateEdittext.setText(bpmondateaArrayList.get(0).bpm_location);
			bpmkey = bpmondateaArrayList.get(0).bpm_key;
			bpmonDateEdittext.setClickable(true);
			bpmonDateEdittext.setHint("Select BPM");
		}

	}

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(AddNewGuestActivity.this,
					UserProfileActivity.class);
			startActivity(intent);
		}
		// else if (view.getId() == R.id.layoutprofile) {
		// MENU_ITEM_SELECTED = "PROFILE";
		// Intent intent = new Intent(AddNewGuestActivity.this,
		// ProfileActivity.class);
		// startActivity(intent);
		// }
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			Intent intent = new Intent(AddNewGuestActivity.this,
					NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(AddNewGuestActivity.this,
					AddBusinessActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			isExpanded = false;
			new CollapseAnimation(slidingPanel, panelWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0,
					0.0f, menulayout);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(AddNewGuestActivity.this,
					MatchupActivity.class);
			startActivity(intent);
		}
		// else if (view.getId() == R.id.Layoutlocator) {
		// MENU_ITEM_SELECTED = "OFFICE LOCATOR";
		// Intent intent = new Intent(AddNewGuestActivity.this,
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
			Intent intent = new Intent(AddNewGuestActivity.this,
					LoginActivity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(AddNewGuestActivity.this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(AddNewGuestActivity.this,
					Dashboard_Activity.class);
			startActivity(intent);

		}
		// else if (view.getId() == R.id.LayoutMATCHUPBOOK) {
		// Intent intent = new Intent(this,
		// MatchupBookListActivity.class); startActivity(intent);
		//
		// }

	}// end of menu

	public void DATETIMEPICKER() {

		final View dialogView = View.inflate(this, R.layout.date_time_picker,
				null);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		final DatePicker datePicker = (DatePicker) dialogView
				.findViewById(R.id.date_picker);
		if (!bpmDateEdittext.getText().toString().equalsIgnoreCase("")) {
			try {

				String temp2 = bpmDateEdittext.getText().toString();
				String[] strArray = temp2.split("\\/");

				int month = Integer.parseInt(strArray[0]);
				int day = Integer.parseInt(strArray[1]);
				int year = Integer.parseInt(strArray[2]);

				datePicker.updateDate(year, month - 1, day);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		}

		final TimePicker timePicker = (TimePicker) dialogView
				.findViewById(R.id.time_picker);

		// if(!timeEdittext.getText().toString().equalsIgnoreCase(""))
		// {
		// try {
		//
		// SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
		// Date date = null;
		// try {
		// date = sdf.parse(timeEdittext.getText().toString());
		// } catch (ParseException e) {
		// }
		// Calendar c = Calendar.getInstance();
		// c.setTime(date);
		// timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		// timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		//
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		// }
		timePicker.setVisibility(View.GONE);
		dialogView.findViewById(R.id.date_time_set).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						Calendar calendar = new GregorianCalendar(datePicker
								.getYear(), datePicker.getMonth(), datePicker
								.getDayOfMonth());

						Date time = calendar.getTime();

						DateFormat writeFormat2 = new SimpleDateFormat(
								"MM/dd/yyyy");
						// writeFormat2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));

						bpmDateEdittext.setText(writeFormat2.format(time));
						bpmDateEdittext.setFocusable(false);
						alertDialog.dismiss();

						if (AppUtils
								.isNetworkAvailable(AddNewGuestActivity.this)) {
							new getBPMonData().execute();
						}// end of if for internet check
						else {
							AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
									"No Internet Connection Available.");
							bpmonDateEdittext.setText("");
						}

					}
				});
		alertDialog.setView(dialogView);
		alertDialog.show();
	}// end of date time picker

	public boolean emptyCheck(EditText editText) {
		if (editText.getText().toString().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidMobile(String phone2) {
		boolean check;
		if (phone2.length() != 12) {
			check = false;
			// contactedittext.setError("Not Valid Contact");
		} else {
			check = true;
		}
		return check;
	}

	public void SearchTrainee(ArrayList<BPMondateVO> agentListArrayList) {
		mainlayout = (RelativeLayout) findViewById(R.id.TRAINEELIST_LAYOUT);
		mainlayout.removeAllViews();

		addnewtrainee.setVisibility(View.GONE);
		mainlayout.setVisibility(View.VISIBLE);
		MAINSEARCHLAYOUT.setVisibility(View.VISIBLE);
		ALLMAINLAYOUT.setVisibility(View.INVISIBLE);
		Typeface type = Typeface.createFromAsset(
				AddNewGuestActivity.this.getAssets(), "fonts/calibribold.ttf");
		Typeface type2 = Typeface.createFromAsset(
				AddNewGuestActivity.this.getAssets(), "fonts/calibri.ttf");
		for (int i = 0; i < agentListArrayList.size(); i++) {
			// final ChatGroupVO groupVO=usergroupArrayList.get(i);
			final BPMondateVO agentVo = agentListArrayList.get(i);

			// ******************************************
			final RelativeLayout layout = new RelativeLayout(this);
			layout.setId(100 + i);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 40;
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			// layoutParams.bottomMargin=100;

			if (i != 0)
				layoutParams.addRule(RelativeLayout.BELOW, 99 + i);

			layout.setBackgroundResource(R.drawable.lightborder);
			layout.setLayoutParams(layoutParams);
			// layout.setBackgroundResource(R.drawable.lightborder);

			// RelativeLayout.LayoutParams params1 = new
			// RelativeLayout.LayoutParams(40,40);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			params2.addRule(RelativeLayout.CENTER_IN_PARENT);
			final TextView tv1 = new TextView(this);
			tv1.setId(4 + i);
			tv1.setPadding(1, 1, 1, 40);
			tv1.setTypeface(type2);
			tv1.setTextSize(18);
			tv1.setTextColor(Color.parseColor("#000000"));
			tv1.setText(agentVo.bpm_location);

			layout.addView(tv1, params2);
			// *******************************************
			mainlayout.addView(layout, layoutParams);

			final int k = i;
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						// tv1.setText("");
						// tv1.setVisibility(View.GONE);

						bpmonDateEdittext.setText(tv1.getText().toString());
						bpmkey = agentVo.bpm_key;
						mainlayout.setVisibility(View.GONE);
						layout.removeAllViews();
						MAINSEARCHLAYOUT.setVisibility(View.GONE);
						ALLMAINLAYOUT.setVisibility(View.VISIBLE);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						AirbrakeNotifier.notify(e);
					}
				}
			});

		}// end of for
	}// end of SEARCH TRAINEE fields

	public void SearchUserllist(ArrayList<AgentListVO> agentListArrayList) {
		mainlayout = (RelativeLayout) findViewById(R.id.TRAINEELIST_LAYOUT);
		mainlayout.removeAllViews();
		addnewtrainee.setVisibility(View.VISIBLE);
		mainlayout.setVisibility(View.VISIBLE);
		MAINSEARCHLAYOUT.setVisibility(View.VISIBLE);
		ALLMAINLAYOUT.setVisibility(View.INVISIBLE);
		Typeface type = Typeface.createFromAsset(
				AddNewGuestActivity.this.getAssets(), "fonts/calibribold.ttf");
		Typeface type2 = Typeface.createFromAsset(
				AddNewGuestActivity.this.getAssets(), "fonts/calibri.ttf");
		for (int i = 0; i < agentListArrayList.size(); i++) {
			// final ChatGroupVO groupVO=usergroupArrayList.get(i);
			final AgentListVO agentVo = agentListArrayList.get(i);

			// ******************************************
			final RelativeLayout layout = new RelativeLayout(this);
			layout.setId(100 + i);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = 40;
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			// layoutParams.bottomMargin=100;

			if (i != 0)
				layoutParams.addRule(RelativeLayout.BELOW, 99 + i);

			layout.setBackgroundResource(R.drawable.lightborder);
			layout.setLayoutParams(layoutParams);
			// layout.setBackgroundResource(R.drawable.lightborder);

			// RelativeLayout.LayoutParams params1 = new
			// RelativeLayout.LayoutParams(40,40);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			params2.addRule(RelativeLayout.CENTER_IN_PARENT);
			final TextView tv1 = new TextView(this);
			tv1.setId(4 + i);
			tv1.setPadding(1, 1, 1, 40);
			tv1.setTypeface(type2);
			tv1.setTextSize(18);
			tv1.setTextColor(Color.parseColor("#000000"));
			tv1.setText(agentVo.firstname + " " + agentVo.lastname + "("
					+ agentVo.agent_id + ")");

			layout.addView(tv1, params2);
			// *******************************************
			mainlayout.addView(layout, layoutParams);

			final int k = i;
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {

						inviterFirstnameEdittext.setText(tv1.getText()
								.toString());
						traineeuserid = agentVo.userid;

						inviterLastnameEdittext.setText("");
						INVITEDLAYOUT.setVisibility(View.GONE);
						IMAGELAYOUT.setVisibility(View.GONE);

						mainlayout.setVisibility(View.GONE);
						layout.removeAllViews();
						MAINSEARCHLAYOUT.setVisibility(View.GONE);
						ALLMAINLAYOUT.setVisibility(View.VISIBLE);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			});

		}// end of for
	}// end of SEARCH TRAINEE fields

	public void ShowDialog() {

		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		final View Viewlayout = inflater.inflate(R.layout.addtrainee_dialodxml,
				(ViewGroup) findViewById(R.id.TRAINEE_dialog_Layout));

		final Button donebtn = (Button) Viewlayout
				.findViewById(R.id.TRAINEE_DONE_button);
		final EditText newtrainee = (EditText) Viewlayout
				.findViewById(R.id.TRAINEE_NEW_ADD_EDITTEXT);
		final Button cancelbtn = (Button) Viewlayout
				.findViewById(R.id.TRAINEE_CANCLE_button);

		// popDialog.setIcon(android.R.drawable.btn_star_big_on);
		// if (OPTIONSELECTED.equalsIgnoreCase("TRAINEE"))
		// {
		// newtrainee.setHint("Add new Trainee");
		// popDialog.setTitle("Add new Trainee");
		//
		// }else if (OPTIONSELECTED.equalsIgnoreCase("AGENT"))
		// {
		// newtrainee.setHint("Add new Agent");
		// popDialog.setTitle("Add new Agent");
		// }

		popDialog.setCancelable(false);
		popDialog.setView(Viewlayout);
		// txtPerc.setText(url);
		donebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {

					if (!newtrainee.getText().toString().equals("")) {

						inviterFirstnameEdittext.setText(newtrainee.getText()
								.toString());
						traineeuserid = "";

						inviterLastnameEdittext.setText("");
						INVITEDLAYOUT.setVisibility(View.VISIBLE);
						IMAGELAYOUT.setVisibility(View.VISIBLE);

						mainlayout.removeAllViews();
						MAINSEARCHLAYOUT.setVisibility(View.GONE);
						ALLMAINLAYOUT.setVisibility(View.VISIBLE);
						myAlertDialog.dismiss();
					} else
						AppUtils.ShowAlertDialog(AddNewGuestActivity.this,
								"Please Enter value");

				} catch (Exception e) {
					// TODO: handle exception
					myAlertDialog.dismiss();
					e.printStackTrace();
					AirbrakeNotifier.notify(e);
				}

			}
		});

		cancelbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myAlertDialog.dismiss();

				// traineeuserid = "";
				inviterLastnameEdittext.setText("");
				INVITEDLAYOUT.setVisibility(View.GONE);
				IMAGELAYOUT.setVisibility(View.GONE);
			}
		});

		myAlertDialog = popDialog.create();
		myAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams wmlp = myAlertDialog.getWindow()
				.getAttributes();
		myAlertDialog.show();
	}// end of showdialog

	// validating email id
	private boolean isValidEmail(String mail) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}

}// end of class

