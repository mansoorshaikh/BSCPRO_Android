package com.MWC.chatapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.loopj.android.airbrake.AirbrakeNotifier;
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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AgentListVO;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;
import applicationVo.MatchupBookVO;
public class MatchupBookListActivity extends Activity {

	public ListView matchuplistview;
	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
			LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,
			LayoutBPM_Check_in, MAINSEARCHLAYOUT, ALLMAINLAYOUT, menulayout;
	RelativeLayout mainlayout,matchuplistLayout;
	public static String MENU_ITEM_SELECTED = "";
	public ArrayList<MatchupBookVO> matchbookArrayList = new ArrayList<MatchupBookVO>();
	public ArrayList<MatchupBookVO> TempmatchbookArrayList = new ArrayList<MatchupBookVO>();
	
	public String SELECTED_MATCHUPID="";
	public String SELECTED_TRAINEEARRAY="";
	public String Matchupresponse="";
	
	int matchuppos=0;
	ScrollView myScrollView;
	public ArrayList<AgentListVO> sortArrayList = new ArrayList<AgentListVO>();
	
	public ArrayList<AgentListVO> agentvoArrayList = new ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> UserlistArrayList = new ArrayList<AgentListVO>();
	
	public ArrayList<AgentListVO> SelectedagentvoArrayList = new ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> TempagentvoArrayList = new ArrayList<AgentListVO>();

	public String agentresponseString = "";

	
	public EditText searchEdittext;
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
		setContentView(R.layout.matchbook_listview_xml);
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
//			Button button9 = (Button) findViewById(R.id.btn_MATCHUPBOOK);
//			button9.setTypeface(font);
			
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
			matchuplistLayout=(RelativeLayout)findViewById(R.id.mainlayout);
			
			ALLMAINLAYOUT=(RelativeLayout)findViewById(R.id.MAILALL_LAYOUT);
			MAINSEARCHLAYOUT=(RelativeLayout)findViewById(R.id.TRAINEELIST_MAIN_LAYOUT);
			searchEdittext=(EditText)findViewById(R.id.TRAINEE_SEARCH_EDITTEXT);
			myScrollView=(ScrollView)findViewById(R.id.scrollview);
			//************************************************
			searchEdittext.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
					// Abstract Method of TextWatcher Interface.
				}

				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// Abstract Method of TextWatcher Interface.
				}
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					int textlength = searchEdittext.getText().length();
					sortArrayList.clear();
					
					for (int i = 0; i < TempagentvoArrayList.size(); i++) {
						AgentListVO agentVo=TempagentvoArrayList.get(i);
						String compairstr=agentVo.firstname +" "+ agentVo.lastname;
						if (textlength <= compairstr.length()) {
							
							if (searchEdittext.getText().toString().equalsIgnoreCase(
											(String) compairstr.subSequence(
													0, textlength))) {
								sortArrayList.add(agentVo);
								// image_sort.add(listview_images[i]);
							}


						}
					}// end of for
					
					
					
					 SearchTrainee(sortArrayList);
				}
			});
			
			//***********************************************
			Button canclebtn=(Button)findViewById(R.id.TRAINEE_CANCLE_BTN);
			canclebtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//mainlayout = (RelativeLayout) findViewById(R.id.TRAINEELIST_LAYOUT);
					  
					  MAINSEARCHLAYOUT.setVisibility(View.GONE);
						 ALLMAINLAYOUT.setVisibility(View.VISIBLE);
						 matchuplistLayout.setVisibility(View.VISIBLE);
						mainlayout.removeAllViews();
				}
			});
			
			
			
			

			if (AppUtils.isNetworkAvailable(MatchupBookListActivity.this)) {
				new getMatchupListData().execute();
			}// end of if for internet check
			else {
				AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
						"No Internet Connection Available.");
			}

		} catch (Exception e) {
			// TODO: handle exception
			AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
					"Something is gone wrong..!,please retry.");
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}// end of onCreate()

	// ******************************Async task claass---GetAgent data BY TYPE
	public class getMatchupListData extends AsyncTask<String, String, String> {

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
			try {
				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/matchup_api/matchupList");
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
			String status = null;
			// String responseText = null;
			JSONObject jresponse = null;
			try {

				jresponse = new JSONObject(responseText);

				responseText = jresponse.getString("message");
				status = jresponse.getString("status");
				JSONArray activityArray = null;
				if (responseText.equalsIgnoreCase("success")
						&& status.equals("ok")) {
					matchbookArrayList.clear();
					activityArray = jresponse.getJSONArray("matchups");

					for (int i = 0; i < activityArray.length(); i++) {

						JSONObject activityObject = (JSONObject) activityArray
								.get(i);
						MatchupBookVO cVo = new MatchupBookVO();

						if (!activityObject.isNull("match_id"))
							cVo.match_id = activityObject.getString("match_id");

						if (!activityObject.isNull("user_id"))
							cVo.user_id = activityObject.getString("user_id");

						if (!activityObject.isNull("matchday"))
							cVo.matchday = activityObject.getString("matchday");

						if (!activityObject.isNull("date_match"))
							cVo.date_match = activityObject
									.getString("date_match");

						if (!activityObject.isNull("match_time"))
							cVo.match_time = activityObject
									.getString("match_time");
						
						if (!activityObject.isNull("matchup_timezone"))
							cVo.matchup_timezone = activityObject
									.getString("matchup_timezone");
						
						
						if (!activityObject.isNull("matchup_modified"))
							cVo.matchup_modified = activityObject
									.getString("matchup_modified");

						if (!activityObject.isNull("place_match"))
							cVo.place_match = activityObject
									.getString("place_match");
						if (!activityObject.isNull("match_name"))
							cVo.match_name = activityObject
									.getString("match_name");
						if (!activityObject.isNull("spouse_name"))
							cVo.spouse_name = activityObject
									.getString("spouse_name");

						if (!activityObject.isNull("contact_match"))
							cVo.contact_match = activityObject
									.getString("contact_match");
						if (!activityObject.isNull("match_profile"))
							cVo.match_profile = activityObject
									.getString("match_profile");
						if (!activityObject.isNull("match_trainee"))
							cVo.match_trainee = activityObject
									.getString("match_trainee");

						if (!activityObject.isNull("trainee_ph"))
							cVo.trainee_ph = activityObject
									.getString("trainee_ph");

						if (!activityObject.isNull("appttype"))
							cVo.appttype = activityObject.getString("appttype");
						if (!activityObject.isNull("trainer_types_select"))
							cVo.trainer_types_select = activityObject
									.getString("trainer_types_select");
						if (!activityObject.isNull("notes_match"))
							cVo.notes_match = activityObject
									.getString("notes_match");

						if (!activityObject.isNull("accept_trainer_matchup"))
							cVo.accept_trainer_matchup = activityObject
									.getString("accept_trainer_matchup");
						if (!activityObject.isNull("submitted_matchup_date"))
							cVo.submitted_matchup_date = activityObject
									.getString("submitted_matchup_date");
						if (!activityObject.isNull("elem_order"))
							cVo.elem_order = activityObject
									.getString("elem_order");
						// if (!activityObject.isNull("teamcallsi"))
						// cVo.teamcallsi =
						// activityObject.getString("teamcallsi");
						// if (!activityObject.isNull("agent_id"))
						// cVo.agent_id = activityObject.getString("agent_id");
						// if (!activityObject.isNull("agent_id"))
						// cVo.agent_id = activityObject.getString("agent_id");

						matchbookArrayList.add(cVo);
					}// end of for


				} else
					AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
							"ERROR: " + responseText);

				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				
				if (matchbookArrayList.size() > 0) {
					new getAgentData().execute();
					//setListValues(matchbookArrayList);
				}

			} catch (Exception e) {
				// TODO: handle exception
				// myProgressBar.setVisibility(View.GONE);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(
						MatchupBookListActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

			// new group_chat_message().execute();
			// do stuff after posting data
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
					//System.out.println(agentresponseString + "response is display");

				} catch (Exception ex) {
					ex.printStackTrace();
					AirbrakeNotifier.notify(ex);
				}

				return null;
			} // end of doInBackground();

			@Override
			protected void onPostExecute(String lenghtOfFile) {
				String status = null;
				String responseText = null;
				JSONObject jresponse = null;
				try {

					jresponse = new JSONObject(agentresponseString);

					responseText = jresponse.getString("message");
					status = jresponse.getString("status");
					JSONArray activityArray = null;
					if (responseText.equalsIgnoreCase("") && status.equals("ok")) {
						agentvoArrayList.clear();
						UserlistArrayList.clear();
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
						
						if (matchbookArrayList.size() > 0) {
							
							//agentvoArrayList.size();
							UserlistArrayList.addAll(agentvoArrayList);
							TempmatchbookArrayList.addAll(matchbookArrayList);
							setListValues(matchbookArrayList,agentvoArrayList);
						}

					} else
						AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
								"ERROR: " + responseText);

					if (mProgressDialog != null)
						mProgressDialog.dismiss();

				} catch (Exception e) {
					// TODO: handle exception
					// myProgressBar.setVisibility(View.GONE);
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
							"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
					e.printStackTrace();
					AirbrakeNotifier.notify(e);
				}

				// new group_chat_message().execute();
				// do stuff after posting data
			}
		}
		
		// JSON AsyncTask for saveUserDetails upload
		class myTask_AddMatchupTrainee_call extends AsyncTask<Void, Void, Void> {

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

				nameValuePair.add(new BasicNameValuePair("matchup_id",
						SELECTED_MATCHUPID));
				nameValuePair.add(new BasicNameValuePair("trainer_id",
						SELECTED_TRAINEEARRAY.equals("")?"null":SELECTED_TRAINEEARRAY));
				try {
					// Defined URL where to send data
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"https://bscpro.com/matchup_api/update_trainer");
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
							"UTF-8"));
					HttpResponse response = httpclient.execute(httppost);
					Matchupresponse = EntityUtils.toString(response.getEntity());
					System.out.println(Matchupresponse + "response is display");

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
					jresponse = new JSONObject(Matchupresponse);

					String responseText = jresponse.getString("message");
					String status = jresponse.getString("status");

					if (responseText.equals("success") && status.equals("ok")) {
//						AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
//								"Saved Successfully.");
						if (mProgressDialog != null)
							mProgressDialog.dismiss();
						try {
							Intent intent=new Intent(getApplicationContext(), MatchupBookListActivity.class);
							startActivity(intent);
							 MatchupBookListActivity.this.finish();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
					} else {
						AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
								"Error :" + responseText);
						 MAINSEARCHLAYOUT.setVisibility(View.GONE);
						 ALLMAINLAYOUT.setVisibility(View.VISIBLE);
						 matchuplistLayout.setVisibility(View.VISIBLE);
						 mainlayout.setVisibility(View.GONE);
						
					}
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(
							MatchupBookListActivity.this,
							"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
					e.printStackTrace();
					AirbrakeNotifier.notify(e);
					 MAINSEARCHLAYOUT.setVisibility(View.GONE);
					 ALLMAINLAYOUT.setVisibility(View.VISIBLE);
					 matchuplistLayout.setVisibility(View.VISIBLE);
					 mainlayout.setVisibility(View.GONE);

				}
			}// end of onpost()
		}// ends of Async task

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(this, UserProfileActivity.class);
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
			Intent intent = new Intent(this, NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(this, AddBusinessActivity.class);
			startActivity(intent);
		}
//		else if (view.getId() == R.id.LayoutMATCHUPBOOK) {
//			MENU_ITEM_SELECTED = "ADD NEW GUEST";
//			isExpanded = false;
//			new CollapseAnimation(slidingPanel, panelWidth,
//					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
//					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0,
//					0.0f, menulayout);
//		}
		else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(this, MatchupActivity.class);
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
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(this, BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(this, Dashboard_Activity.class);
			startActivity(intent);

		}
		else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(this, AddNewGuestActivity.class);
			startActivity(intent);

		}
	}// end of menu

	// **********************************************************
	void setListValues(ArrayList<MatchupBookVO> usergroupArrayList,final ArrayList<AgentListVO> agentvoArrayList) {
		try {

			RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
			mainlayout.removeAllViews();
			Typeface type = Typeface.createFromAsset(
					MatchupBookListActivity.this.getAssets(),
					"fonts/calibribold.ttf");
			Typeface type2 = Typeface.createFromAsset(
					MatchupBookListActivity.this.getAssets(),
					"fonts/calibri.ttf");
			for (int i = 0; i < usergroupArrayList.size(); i++) {

				final MatchupBookVO matchupvo = usergroupArrayList.get(i);
		final ArrayList<AgentListVO> TraineeselectArrayList = new ArrayList<AgentListVO>();

				TraineeselectArrayList.clear();
				
				String[] selectlist = matchupvo.trainer_types_select.split(",");
				
				for (int j = 0; j < selectlist.length; j++)
				{
					for (int j2 = 0; j2 < agentvoArrayList.size(); j2++)
					{
						if(selectlist[j].equalsIgnoreCase(agentvoArrayList.get(j2).userid))
							TraineeselectArrayList.add(agentvoArrayList.get(j2));
					
					}//end of inner for
				}//end of outer for
				
				String Sentfromuser="";
				for (int j = 0; j < agentvoArrayList.size(); j++) {
					if(matchupvo.user_id.equalsIgnoreCase(agentvoArrayList.get(j).userid))
					{
						Sentfromuser=agentvoArrayList.get(j).firstname+" "+agentvoArrayList.get(j).lastname;
					      break;
					}
				}
				
				// ******************************************
				RelativeLayout layout = new RelativeLayout(this);
				layout.setId(100 + i);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				
				//layout.setPadding(1, 1, 1, 90);
				if (i != 0)
				{
					layoutParams.topMargin = 10;
					layoutParams.addRule(RelativeLayout.BELOW, 99 + i);
				}

				layout.setBackgroundResource(R.drawable.lightborder);
				layout.setLayoutParams(layoutParams);
				// layout.setBackgroundResource(R.drawable.lightborder);

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params7 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params8 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params9 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params10 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params11 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params12 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params13 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params14 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params15 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params16 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params17 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params18 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params19 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params20 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params21 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params22 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params23 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params24 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params25 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params26 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params27 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params28 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params29 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				RelativeLayout.LayoutParams params32 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params33 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params34 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params35 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params36 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params37 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params38 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params39 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				// //params1.setMargins(5,25,5,55);
				// RoundedImageView imageView = new
				// RoundedImageView(MatchupBookListActivity.this);
				// imageView.setId(1);
				// imageView.setImageResource(R.drawable.chaticon);
				//
				// ImageView image3=new ImageView(MatchupBookListActivity.this);
				// image3.setId(9);
				// params5.addRule(RelativeLayout.BELOW,imageView.getId());

				final TextView tv1 = new TextView(this);
				params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params1.leftMargin = 5;
				params1.rightMargin = 5;
				tv1.setId(1000 + i);
				tv1.setTextSize(13);
				tv1.setTextColor(Color.parseColor("#000000"));
				tv1.setTypeface(type2);
				
				String input = matchupvo.submitted_matchup_date;
			       DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			       DateFormat outputformat = new SimpleDateFormat("MMM dd, hh:mm aa");
			       Date date = null;
			       String output = null;
			       try{
			    	  date= df.parse(input);
			    	  output = outputformat.format(date);
			    	  System.out.println(output);
			    	}catch(ParseException pe){
			    	    pe.printStackTrace();
			    	    AirbrakeNotifier.notify(pe);
			    	 }
				tv1.setText(output);

				final TextView tv2 = new TextView(this);
				params2.addRule(RelativeLayout.LEFT_OF, tv1.getId());
				params2.leftMargin = 5;
				params2.rightMargin = 5;
				tv2.setId(2000 + i);
				tv2.setTextSize(13);
				tv2.setTextColor(Color.parseColor("#000000"));
				tv2.setTypeface(type2);
				tv2.setText("Submitted : ");

				TextView tv3 = new TextView(this);
				params3.addRule(RelativeLayout.BELOW, tv1.getId());
				params3.topMargin = 5;
				// params3.rightMargin=180;
				params3.leftMargin = 5;
				tv3.setTypeface(type);
				tv3.setId(3000 + i);
				tv3.setTextSize(20);
				tv3.setTextColor(Color.parseColor("#000000"));
				tv3.setText("Match Up ");

				TextView tv4 = new TextView(this);
				params4.addRule(RelativeLayout.RIGHT_OF, tv3.getId());
				params4.addRule(RelativeLayout.BELOW, tv1.getId());
				params4.leftMargin = 5;
				tv4.setId(4000 + i);
				tv4.setTypeface(type);
				tv4.setTextSize(20);
				tv4.setTextColor(Color.parseColor("#000000"));
				tv4.setText("" + (i + 1));

				TextView tv5 = new TextView(this);
				params5.addRule(RelativeLayout.RIGHT_OF, tv4.getId());
				params5.addRule(RelativeLayout.BELOW, tv1.getId());
				params5.leftMargin = 5;
				tv5.setId(5000 + i);
				tv5.setTypeface(type);
				tv5.setTextSize(20);
				tv5.setTextColor(Color.parseColor("#000000"));
				tv5.setText("- sent from");

				TextView tv6 = new TextView(this);
				params6.addRule(RelativeLayout.BELOW, tv5.getId());
				params6.leftMargin = 5;
				tv6.setId(6000 + i);
				tv6.setTypeface(type);
				tv6.setTextSize(20);
				tv6.setTextColor(Color.parseColor("#000000"));
				tv6.setText(Sentfromuser);

				TextView tv7 = new TextView(this);
				params7.addRule(RelativeLayout.BELOW, tv6.getId());
				params7.leftMargin = 5;
				params7.topMargin = 15;
				tv7.setId(7000 + i);
				tv7.setTypeface(type);
				tv7.setTextSize(14);
				tv7.setTextColor(Color.parseColor("#000000"));
				tv7.setText("Day :");
				tv7.setPaintFlags(tv7.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv8 = new TextView(this);
				params8.addRule(RelativeLayout.BELOW, tv6.getId());
				params8.addRule(RelativeLayout.RIGHT_OF, tv7.getId());
				params8.leftMargin = 5;
				params8.topMargin = 15;
				tv8.setId(8000 + i);
				tv8.setTextColor(Color.parseColor("#000000"));
				tv8.setTypeface(type2);
				tv8.setTextSize(14);
				tv8.setText(matchupvo.matchday);
				
				TextView tv9 = new TextView(this);
				params9.addRule(RelativeLayout.BELOW, tv6.getId());
				params9.addRule(RelativeLayout.RIGHT_OF, tv8.getId());
				params9.leftMargin =20;
				params9.topMargin = 15;
				tv9.setId(9000 + i);
				tv9.setTypeface(type);
				tv9.setTextSize(14);
				tv9.setTextColor(Color.parseColor("#000000"));
				tv9.setText("Date :");
				tv9.setPaintFlags(tv7.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv10 = new TextView(this);
				params10.addRule(RelativeLayout.BELOW, tv6.getId());
				params10.addRule(RelativeLayout.RIGHT_OF, tv9.getId());
				params10.leftMargin = 5;
				params10.topMargin = 15;
				tv10.setId(10000 + i);
				tv10.setTextColor(Color.parseColor("#000000"));
				tv10.setTypeface(type2);
				tv10.setTextSize(14);
				
				 input = matchupvo.date_match;
			        df = new SimpleDateFormat("yyyy-MM-dd");
			        outputformat = new SimpleDateFormat("MMM dd,yyyy");
			        date = null;
			        output = null;
			       try{
			    	  date= df.parse(input);
			    	  output = outputformat.format(date);
			    	  System.out.println(output);
			    	}catch(ParseException pe){
			    	    pe.printStackTrace();
			    	    AirbrakeNotifier.notify(pe);
			    	 }
				
				tv10.setText(output);
				
				
				TextView tv11 = new TextView(this);
				params11.addRule(RelativeLayout.BELOW, tv10.getId());
				params11.leftMargin = 5;
				params11.topMargin = 7;
				tv11.setId(11000 + i);
				tv11.setTypeface(type);
				tv11.setTextSize(14);
				tv11.setTextColor(Color.parseColor("#000000"));
				tv11.setText("Time :");
				tv11.setPaintFlags(tv7.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv12 = new TextView(this);
				params12.addRule(RelativeLayout.BELOW, tv10.getId());
				params12.addRule(RelativeLayout.RIGHT_OF, tv11.getId());
				params12.leftMargin = 5;
				params12.topMargin = 7;
				tv12.setId(12000 + i);
				tv12.setTextColor(Color.parseColor("#000000"));
				tv12.setTypeface(type2);
				tv12.setTextSize(14);
				//tv12.setText(matchupvo.match_time);
				
				String inputtime = matchupvo.match_time;
			       DateFormat dftime = new SimpleDateFormat("HH:mm:ss");
			       DateFormat outputtimeformat = new SimpleDateFormat("hh:mm aa");
			       Date date2 = null;
			       String output2 = null;
			       try{
			    	  date2= dftime.parse(inputtime);
			    	  output2 = outputtimeformat.format(date2);
			    	  System.out.println(output2);
			    	}catch(ParseException pe){
			    	    pe.printStackTrace();
			    	    AirbrakeNotifier.notify(pe);
			    	 }
				tv12.setText(output2+" "+matchupvo.matchup_timezone);
				
				
				
				TextView tv13 = new TextView(this);
				params13.addRule(RelativeLayout.BELOW, tv10.getId());
				params13.addRule(RelativeLayout.RIGHT_OF, tv12.getId());
				params13.leftMargin =20;
				params13.topMargin = 7;
				tv13.setId(13000 + i);
				tv13.setTypeface(type);
				tv13.setTextSize(14);
				tv13.setTextColor(Color.parseColor("#000000"));
				tv13.setText(" Place :");
				tv13.setPaintFlags(tv7.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv14 = new TextView(this);
				params14.addRule(RelativeLayout.BELOW, tv10.getId());
				params14.addRule(RelativeLayout.RIGHT_OF, tv13.getId());
				params14.leftMargin = 5;
				params14.topMargin = 7;
				tv14.setId(14000 + i);
				tv14.setTextColor(Color.parseColor("#000000"));
				tv14.setTypeface(type2);
				tv14.setTextSize(14);
				tv14.setText(matchupvo.place_match);
				
				TextView tv15 = new TextView(this);
				params15.addRule(RelativeLayout.BELOW, tv14.getId());
				params15.leftMargin = 5;
				params15.topMargin = 7;
				tv15.setId(15000 + i);
				tv15.setTypeface(type);
				tv15.setTextSize(14);
				tv15.setTextColor(Color.parseColor("#000000"));
				tv15.setText("Name :");
				tv15.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv16 = new TextView(this);
				params16.addRule(RelativeLayout.BELOW, tv14.getId());
				params16.addRule(RelativeLayout.RIGHT_OF, tv15.getId());
				params16.leftMargin = 5;
				params16.topMargin = 7;
				tv16.setId(16000 + i);
				tv16.setTextColor(Color.parseColor("#000000"));
				tv16.setTypeface(type2);
				tv16.setTextSize(14);
				tv16.setText(matchupvo.match_name);
				

				TextView tv17 = new TextView(this);
				params17.addRule(RelativeLayout.BELOW, tv16.getId());
				params17.leftMargin = 5;
				params17.topMargin = 7;
				tv17.setId(17000 + i);
				tv17.setTypeface(type);
				tv17.setTextSize(14);
				tv17.setTextColor(Color.parseColor("#000000"));
				tv17.setText("Contact :");
				tv17.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv18 = new TextView(this);
				params18.addRule(RelativeLayout.BELOW, tv16.getId());
				params18.addRule(RelativeLayout.RIGHT_OF, tv17.getId());
				params18.leftMargin = 5;
				params18.topMargin = 7;
				tv18.setId(18000 + i);
				tv18.setTextColor(Color.parseColor("#000000"));
				tv18.setTypeface(type2);
				tv18.setTextSize(14);
				tv18.setText(matchupvo.contact_match);
				
				TextView tv19 = new TextView(this);
				params19.addRule(RelativeLayout.BELOW, tv18.getId());
				params19.leftMargin = 5;
				params19.topMargin = 7;
				tv19.setId(19000 + i);
				tv19.setTypeface(type);
				tv19.setTextSize(14);
				tv19.setTextColor(Color.parseColor("#000000"));
				tv19.setText("Profile :");
				tv19.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv20 = new TextView(this);
				params20.addRule(RelativeLayout.BELOW, tv18.getId());
				params20.addRule(RelativeLayout.RIGHT_OF, tv19.getId());
				params20.leftMargin = 5;
				params20.topMargin = 7;
				tv20.setId(20000 + i);
				tv20.setTextColor(Color.parseColor("#000000"));
				tv20.setTypeface(type2);
				tv20.setTextSize(14);
				
				String[] proflistarray = matchupvo.match_profile.split(",");
				
				String profilestr="";
				try {
					int count=0;
					
					if(proflistarray[0].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"25+y.o";
						else
							profilestr=profilestr+",25+y.o";
					}
					if(proflistarray[1].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"Married";
						else
							profilestr=profilestr+",Married";
					}
					if(proflistarray[2].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"Dependent Children";
						else
							profilestr=profilestr+",Dependent Children";
					}
					if(proflistarray[3].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"Homeowner";
						else
							profilestr=profilestr+",Homeowner";
					}
					if(proflistarray[4].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"Solid Carrer Background";
						else
							profilestr=profilestr+",Solid Carrer Background";
					}
					if(proflistarray[5].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"$40,000 +income";
						else
							profilestr=profilestr+",$40,000 +income";
					}
					if(proflistarray[6].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"Dissatisfied";
						else
							profilestr=profilestr+",Dissatisfied";
					}
					if(proflistarray[7].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"Entrepreneurial";
						else
							profilestr=profilestr+",Entrepreneurial";
					}
					if(proflistarray[8].equalsIgnoreCase("1"))
					{
						count++;
						if(count==1)
						profilestr=profilestr+"Spanish Speaking Preferred";
						else
							profilestr=profilestr+",Spanish Speaking Preferred";
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					AirbrakeNotifier.notify(e);
				}
				
				tv20.setText(profilestr);
				
				TextView tv21 = new TextView(this);
				params21.addRule(RelativeLayout.BELOW, tv20.getId());
				params21.leftMargin = 5;
				params21.topMargin = 7;
				tv21.setId(21000 + i);
				tv21.setTypeface(type);
				tv21.setTextSize(14);
				tv21.setTextColor(Color.parseColor("#000000"));
				tv21.setText("Trainee :");
				tv21.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv22 = new TextView(this);
				params22.addRule(RelativeLayout.BELOW, tv20.getId());
				params22.addRule(RelativeLayout.RIGHT_OF, tv21.getId());
				params22.leftMargin = 5;
				params22.topMargin = 7;
				tv22.setId(22000 + i);
				tv22.setTextColor(Color.parseColor("#000000"));
				tv22.setTypeface(type2);
				tv22.setTextSize(14);
				tv22.setText(matchupvo.match_trainee);
				
				TextView tv23 = new TextView(this);
				params23.addRule(RelativeLayout.BELOW, tv22.getId());
				params23.leftMargin = 5;
				params23.topMargin = 7;
				tv23.setId(23000 + i);
				tv23.setTypeface(type);
				tv23.setTextSize(14);
				tv23.setTextColor(Color.parseColor("#000000"));
				tv23.setText("Trainee Contact :");
				tv23.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv24 = new TextView(this);
				params24.addRule(RelativeLayout.BELOW, tv22.getId());
				params24.addRule(RelativeLayout.RIGHT_OF, tv23.getId());
				params24.leftMargin = 5;
				params24.topMargin = 7;
				tv24.setId(24000 + i);
				tv24.setTextColor(Color.parseColor("#000000"));
				tv24.setTypeface(type2);
				tv24.setTextSize(14);
				tv24.setText(matchupvo.trainee_ph);
				
				TextView tv25 = new TextView(this);
				params25.addRule(RelativeLayout.BELOW, tv24.getId());
				params25.leftMargin = 5;
				params25.topMargin = 7;
				tv25.setId(25000 + i);
				tv25.setTypeface(type);
				tv25.setTextSize(14);
				tv25.setTextColor(Color.parseColor("#000000"));
				tv25.setText("Appt.Type :");
				tv25.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv26 = new TextView(this);
				params26.addRule(RelativeLayout.BELOW, tv24.getId());
				params26.addRule(RelativeLayout.RIGHT_OF, tv25.getId());
				params26.leftMargin = 5;
				params26.topMargin = 7;
				tv26.setId(26000 + i);
				tv26.setTextColor(Color.parseColor("#000000"));
				tv26.setTypeface(type2);
				tv26.setTextSize(14);
				tv26.setText(matchupvo.appttype);
				
				TextView tv27 = new TextView(this);
				params27.addRule(RelativeLayout.BELOW, tv26.getId());
				params27.leftMargin = 5;
				params27.topMargin = 7;
				tv27.setId(27000 + i);
				tv27.setTypeface(type);
				tv27.setTextSize(14);
				tv27.setTextColor(Color.parseColor("#000000"));
				tv27.setText("Spouse's Name : ");
				tv27.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);
				if(matchupvo.spouse_name.equals(""))
					tv27.setVisibility(View.GONE);

				TextView tv28 = new TextView(this);
				params28.addRule(RelativeLayout.BELOW, tv26.getId());
				params28.addRule(RelativeLayout.RIGHT_OF, tv27.getId());
				params28.leftMargin = 5;
				params28.topMargin = 7;
				tv28.setId(28000 + i);
				tv28.setTextColor(Color.parseColor("#000000"));
				tv28.setTypeface(type2);
				tv28.setTextSize(14);
				tv28.setText(matchupvo.spouse_name);
				if(matchupvo.spouse_name.equals(""))
					tv28.setVisibility(View.GONE);
				
				TextView tv29 = new TextView(this);
				params29.addRule(RelativeLayout.BELOW, tv28.getId());
				params29.leftMargin = 5;
				params29.topMargin = 7;
				tv29.setId(29000 + i);
				tv29.setTypeface(type);
				tv29.setTextSize(14);
				tv29.setTextColor(Color.parseColor("#000000"));
				tv29.setText("Choose a Field Trainer :");
				tv29.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);
				
						
				
				RelativeLayout traineelayout = new RelativeLayout(this);
				traineelayout.setId(70000 + i);
				RelativeLayout.LayoutParams traineelayoutParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				traineelayoutParams.topMargin = 10;
				traineelayoutParams.addRule(RelativeLayout.BELOW, tv29.getId());
				traineelayout.setBackgroundResource(R.drawable.rounded_corner);
				traineelayout.setLayoutParams(traineelayoutParams);
				
				if(TraineeselectArrayList.size()>0)
				{
				for (int j = 0; j < TraineeselectArrayList.size(); j++) {
					
					RelativeLayout innerlayout = new RelativeLayout(this);
					innerlayout.setId(71000 + j);
					RelativeLayout.LayoutParams innerlayoutParams = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					
					// layoutParams.bottomMargin=100;
					if (j != 0)
					{
						innerlayoutParams.topMargin = 10;
						innerlayoutParams.addRule(RelativeLayout.BELOW, innerlayout.getId()-1);
					}

					//innerlayout.setBackgroundResource(R.drawable.lightborder);
					innerlayout.setLayoutParams(innerlayoutParams);
					
					RelativeLayout.LayoutParams params30 = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					RelativeLayout.LayoutParams params31 = new RelativeLayout.LayoutParams(
							50,50);
			
					
					TextView tv30 = new TextView(this);
					//params29.addRule(RelativeLayout.BELOW, tv28.getId());
					params30.leftMargin = 5;
					params30.topMargin = 7;
					//tv30.setPadding(7, 7, 7, 7);
					tv30.setId(30000 + j);
					tv30.setTypeface(type);
					tv30.setTextSize(14);
					tv30.setTextColor(Color.parseColor("#000000"));
					tv30.setText(TraineeselectArrayList.get(j).firstname+" "+TraineeselectArrayList.get(j).lastname+"("+TraineeselectArrayList.get(j).agent_id+")");
					tv30.setPaintFlags(tv15.getPaintFlags()
							| Paint.UNDERLINE_TEXT_FLAG);
					
					final ImageView cancleimage = new ImageView(this);
					cancleimage.setId(31000 + j);
					cancleimage.setImageResource(R.drawable.canclebtn);
					params31.addRule(RelativeLayout.RIGHT_OF, tv30.getId());
					params31.leftMargin = 15;
					params31.topMargin = 7;
					innerlayout.addView(tv30, params30);
					innerlayout.addView(cancleimage, params31);
					
					traineelayout.addView(innerlayout, innerlayoutParams);
					
					final int canpos=j;
					final int canmatchpos=i;
					cancleimage.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							//Toast.makeText(getApplicationContext(), "cancle"+canpos, Toast.LENGTH_LONG).show();
							//TraineeselectArrayList.get(canpos);
//							if(TraineeselectArrayList.size()>0)
//							{
								String str="";
								
								if(TraineeselectArrayList.size()==1)
									str="";
								
								int cnt=0;
								for (int k = 0; k < TraineeselectArrayList.size(); k++) {
									
									if(k!=canpos)
									{
									  cnt++;
									  if(cnt==1)
										str=TraineeselectArrayList.get(k).userid;
									  else
										  str=str+","+TraineeselectArrayList.get(k).userid;
									}
								}
								
								//TempmatchbookArrayList.get(canmatchpos).trainer_types_select=str;
								
								SELECTED_MATCHUPID=matchupvo.match_id;
								SELECTED_TRAINEEARRAY=str;
								
								new myTask_AddMatchupTrainee_call().execute();
								
								//setListValues(TempmatchbookArrayList, agentvoArrayList);
						}
					});
					
					
					
				}//end of trainee for loop
				
				}//end of if
				else
				{
					TextView tv34 = new TextView(this);
					//params29.addRule(RelativeLayout.BELOW, tv28.getId());
					params35.leftMargin = 5;
					//params34.topMargin = 7;
					tv34.setPadding(7, 7, 7, 7);
					tv34.setId(80000 +i);
					tv34.setTypeface(type2);
					tv34.setTextSize(15);
					tv34.setTextColor(Color.parseColor("#000000"));
					tv34.setText("Select Some Options");
//					tv34.setPaintFlags(tv15.getPaintFlags()
//							| Paint.UNDERLINE_TEXT_FLAG);
					traineelayout.addView(tv34, params35);
				}
				
				final int pos=i;
				traineelayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sortArrayList.clear();
						matchuppos=pos;
						
						SELECTED_MATCHUPID=matchupvo.match_id;
						
						if (agentvoArrayList != null && agentvoArrayList.size() > 0) {

								TempagentvoArrayList.clear();
								TempagentvoArrayList.addAll(agentvoArrayList);
								if(TraineeselectArrayList.size()>0)
								for (int i = 0; i < TempagentvoArrayList.size(); i++) {
									for (int j = 0; j < TraineeselectArrayList.size(); j++) {
										if (TempagentvoArrayList.get(i).userid.equalsIgnoreCase
												(TraineeselectArrayList.get(j).userid)) {
											TempagentvoArrayList.remove(i);
										}
									}
								}

								if (TempagentvoArrayList != null
										&& TempagentvoArrayList.size() > 0) {
									sortArrayList.clear();
									sortArrayList.addAll(TempagentvoArrayList);
									searchEdittext.setText("");

							}// end of if
						
						} else
								AppUtils.ShowAlertDialog(MatchupBookListActivity.this,
										"Please Reload the contents");
						
					}
				});
				
				
				
				
				
				
				
				
				TextView tv32 = new TextView(this);
				params32.addRule(RelativeLayout.BELOW, traineelayout.getId());
				params32.leftMargin = 5;
				params32.topMargin = 7;
				tv32.setId(32000 + i);
				tv32.setTypeface(type);
				tv32.setTextSize(14);
				tv32.setTextColor(Color.parseColor("#000000"));
				tv32.setText("Notes :");
				tv32.setPaintFlags(tv15.getPaintFlags()
						| Paint.UNDERLINE_TEXT_FLAG);

				TextView tv33 = new TextView(this);
				params33.addRule(RelativeLayout.BELOW, traineelayout.getId());
				params33.addRule(RelativeLayout.RIGHT_OF, tv32.getId());
				params33.leftMargin = 5;
				params33.topMargin = 7;
				tv33.setId(33000 + i);
				tv33.setTextColor(Color.parseColor("#000000"));
				tv33.setTypeface(type2);
				tv33.setTextSize(14);
				tv33.setText(matchupvo.notes_match);
				
				TextView tv34 = new TextView(this);
				params34.addRule(RelativeLayout.BELOW, tv33.getId());
				params34.leftMargin = 5;
				params34.topMargin = 20;
				tv34.setId(34000 + i);
				tv34.setTextColor(Color.parseColor("#000000"));
				tv34.setTypeface(type2);
				tv34.setTextSize(20);
				tv34.setText(" ");

				layout.addView(tv1, params1);
				layout.addView(tv2, params2);
				layout.addView(tv3, params3);
				layout.addView(tv4, params4);
				layout.addView(tv5, params5);
				layout.addView(tv6, params6);
				layout.addView(tv7, params7);
				layout.addView(tv8, params8);
				layout.addView(tv9, params9);
				layout.addView(tv10, params10);
				
				layout.addView(tv11, params11);
				layout.addView(tv12, params12);
				layout.addView(tv13, params13);
				layout.addView(tv14, params14);
				layout.addView(tv15, params15);
				layout.addView(tv16, params16);
				layout.addView(tv17, params17);
				layout.addView(tv18, params18);
				layout.addView(tv19, params19);
				layout.addView(tv20, params20);
				layout.addView(tv21, params21);
				layout.addView(tv22, params22);
				layout.addView(tv23, params23);
				layout.addView(tv24, params24);
				layout.addView(tv25, params25);
				layout.addView(tv26, params26);
				
				layout.addView(tv27, params27);
				layout.addView(tv28, params28);
				
				layout.addView(tv29, params29);
				layout.addView(traineelayout, traineelayoutParams);
				layout.addView(tv32, params32);
				layout.addView(tv33, params33);
				layout.addView(tv34, params34);
				// *******************************************
				mainlayout.addView(layout, layoutParams);


			}// end of for
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
	
	
	
	public void SearchTrainee(final ArrayList<AgentListVO> agentListArrayList2)
	{
		try {
			
		
		  mainlayout = (RelativeLayout) findViewById(R.id.TRAINEELIST_LAYOUT);
		  mainlayout.removeAllViews();
		  
		 mainlayout.setVisibility(View.VISIBLE);
		 MAINSEARCHLAYOUT.setVisibility(View.VISIBLE);
		 ALLMAINLAYOUT.setVisibility(View.INVISIBLE);
		 
		 matchuplistLayout.setVisibility(View.GONE);
		 
		    Typeface type = Typeface.createFromAsset(this.getAssets(),"fonts/calibribold.ttf");	
		    Typeface type2 = Typeface.createFromAsset(this.getAssets(),"fonts/calibri.ttf");	
		    for (int i = 0; i < agentListArrayList2.size(); i++) {
					//final ChatGroupVO groupVO=usergroupArrayList.get(i);
	               final AgentListVO agentVo=agentListArrayList2.get(i);	
	               
		    	// ******************************************
					final RelativeLayout layout= new RelativeLayout(this);
					layout.setId(100+i);
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					layoutParams.topMargin=40;
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					//layoutParams.bottomMargin=100;
					
					if(i!=0)
					layoutParams.addRule(RelativeLayout.BELOW,99+i);
					
					layout.setBackgroundResource(R.drawable.lightborder);
					layout.setLayoutParams(layoutParams);
					//layout.setBackgroundResource(R.drawable.lightborder);

					//RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(40,40);
					RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		            
					params2.addRule(RelativeLayout.CENTER_IN_PARENT);
					final TextView tv1 = new TextView(this);
					tv1.setId(4+i);
					tv1.setPadding(1, 1, 1, 40);
					tv1.setTypeface(type2);
					tv1.setTextSize(18);
					tv1.setTextColor(Color.parseColor("#000000"));
					tv1.setText(agentVo.firstname + " "+ agentVo.lastname+"("+agentVo.agent_id+")");
				
					layout.addView(tv1, params2);
					// *******************************************
					mainlayout.addView(layout,layoutParams);
					
					final int k=i;
					layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
					
							try {
								
								//SelectedagentvoArrayList.add(agentVo);
								
//								 MAINSEARCHLAYOUT.setVisibility(View.GONE);
//								 ALLMAINLAYOUT.setVisibility(View.VISIBLE);
//								 matchuplistLayout.setVisibility(View.VISIBLE);
//								 mainlayout.setVisibility(View.GONE);
									
									if(!TempmatchbookArrayList.get(matchuppos).trainer_types_select.equals(""))
									{
									//TempmatchbookArrayList.get(matchuppos).trainer_types_select=TempmatchbookArrayList.get(matchuppos).trainer_types_select+","+agentVo.userid;
									SELECTED_TRAINEEARRAY=TempmatchbookArrayList.get(matchuppos).trainer_types_select+","+agentVo.userid;
									
									}else
									{
										//TempmatchbookArrayList.get(matchuppos).trainer_types_select=agentVo.userid;
										SELECTED_TRAINEEARRAY=agentVo.userid;
									}
									
									new myTask_AddMatchupTrainee_call().execute(); 
									
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								AirbrakeNotifier.notify(e);
							}
						}
					});
					
				}// end of for
		} catch (Exception e) {
			// TODO: handle exception
			AirbrakeNotifier.notify(e);
		}
	}//end of SEARCH TRAINEE fields



}// end of Activity

