package com.MWC.chatapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

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
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AgentListVO;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;
import applicationVo.PhoneNumberFilter;
import applicationVo.PhoneNumberTextWatcher;

public class MatchupActivity extends Activity implements OnClickListener {

	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
	LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,LayoutBPM_Check_in,MAINSEARCHLAYOUT,ALLMAINLAYOUT,menulayout;
	RelativeLayout mainlayout;
	public EditText searchEdittext;
	public AlertDialog myAlertDialog=null;

	public ArrayList<AgentListVO> agentByTypeArrayList = new ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> sortArrayList = new ArrayList<AgentListVO>();
	public String typeresponseString = "";
	public String traineeuserid = "";
	public static String MENU_ITEM_SELECTED = "";
	FrameLayout mainFrameLyout;
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;
	Button menuBtn;
	SharedPreferences sharedPreferences;
	private DisplayMetrics metrics;
	private RelativeLayout slidingPanel;
	int panelWidth = 0;
	private boolean isExpanded;
	public DateFormat writeFormat2;
	//public SimpleDateFormat dateFormatGmt;
	public String UTCTIME="",UTCDATE="";
	public boolean btntruefalse[]=new boolean[9];
	public String[] dayListStringArray=new String[] {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	public String[] timeZoneStringArray=new String[] {"PST","MST","CST","EST"};
	String ClickedEdittext="";
	public ChatSingleton chatSingleton;
	public EditText dayEdittext,dateEdittext,timeEdittext,timeZoneEdittext,placeEdittext,prospectNameEdittext,spousesNameEdittext,
	contactEdittext,traineeEdittext,traineephoneEdittext,appt_typeEdittext,notesEdittext;
	public TextView spouseTextView; 
	public Button profbtn1,profbtn2,profbtn3,profbtn4,profbtn5,profbtn6,profbtn7,profbtn8,profbtnspan;
	
	WheelView WheelEvents;
	SlidingDrawer SDEvents;
	Button WheelDone, WheelCancle;
	int WheelPosition;
	boolean scrolling = false;
	ScrollView myScrollView;
	
	public String responseText="";
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
	
	void addValueintoEventList() {
		SDEvents = (SlidingDrawer) findViewById(R.id.Events_slidingDrawer);
		WheelCancle = (Button) findViewById(R.id.Events_Wheel_Cancle_Button);
		WheelCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SDEvents.animateClose();
				SDEvents.setVisibility(View.GONE);
			}
		});
		WheelDone = (Button) findViewById(R.id.Events_Wheel_Done_button);
		WheelEvents = (WheelView) findViewById(R.id.Events_Wheel);

		WheelEvents.setVisibleItems(2);
		WheelEvents.setCurrentItem(1);
		WheelEvents.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView WheelEvents, int oldValue,
					int newValue) {
				if (!scrolling) {
					WheelPosition = newValue;
				}
			}
		});
		WheelEvents.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView WheelEvents) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView WheelEvents) {
				scrolling = false;
				WheelPosition = WheelEvents.getCurrentItem();
			}
		});

		WheelDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SDEvents.animateClose();
				SDEvents.setVisibility(View.GONE);

				 if (ClickedEdittext.equals("day")) {
					 
					 if(WheelPosition<=(dayListStringArray.length-1))
						dayEdittext.setText(dayListStringArray[WheelPosition]);
					 else
						 dayEdittext.setText("");
						dayEdittext.setFocusable(false);
						//agentuserid=agentVo.userid;
					
				} else if (ClickedEdittext.equals("timezone")) {
					
					if(WheelPosition<=(timeZoneStringArray.length-1))
					timeZoneEdittext.setText(timeZoneStringArray[WheelPosition]);
					else
						timeZoneEdittext.setText("PST");
					
					timeZoneEdittext.setFocusable(false);
					//agentuserid=agentVo.userid;
				}//end of else
			}
		});
	}


	 @Override
	 public void onBackPressed() {
		 
	 }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matchup);
		
		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");
		chatSingleton=ChatSingleton.getInstance();

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

		Chatlayout = (RelativeLayout) findViewById(R.id.chatlayout);
		//layoutProfile = (RelativeLayout) findViewById(R.id.layoutprofile);
		LayoutRecruit = (RelativeLayout) findViewById(R.id.Layoutrecruit);
		LayoutBusiness = (RelativeLayout) findViewById(R.id.Layoutbusiness);
		LayoutGuest = (RelativeLayout) findViewById(R.id.Layoutguest);
		LayoutMatchup = (RelativeLayout) findViewById(R.id.Layoutmatchup);
		//LayoutLocator = (RelativeLayout) findViewById(R.id.Layoutlocator);
		LayoutLogout = (RelativeLayout) findViewById(R.id.Layoutlogout);
		LayoutBPM_Check_in = (RelativeLayout) findViewById(R.id.LayoutBPM_Check_In);
		 sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		 Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
	        Button button1 = (Button)findViewById( R.id.btn_recruit );
	        Button button2 = (Button)findViewById( R.id.btn_chat );
	        Button button3 = (Button)findViewById( R.id.btn_matchup );
	        Button button4 = (Button)findViewById( R.id.btn_guest );
	        Button button5 = (Button)findViewById( R.id.btn_business );
	        Button button6 = (Button)findViewById( R.id.btn_BPM_Check_In );
	        Button button7 = (Button)findViewById( R.id.btn_logout );
	        button1.setTypeface(font);
	        button2.setTypeface(font);
	        button3.setTypeface(font);
	        button4.setTypeface(font);
	        button5.setTypeface(font);
	        button6.setTypeface(font);
	        button7.setTypeface(font);
	        Button button8 = (Button)findViewById( R.id.btn_Dashboard );
	        button8.setTypeface(font);
//	        Button button9 = (Button) findViewById(R.id.btn_MATCHUPBOOK);
//			button9.setTypeface(font);
		 NormalSmartImageView profileimageview=(NormalSmartImageView)findViewById(R.id.userimage);
		
		try {
			profileimageview.setImageUrl(sharedPreferences.getString("profileImage", ""));	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
		
		 TextView username=(TextView)findViewById(R.id.Chatusername);
			username.setText(sharedPreferences.getString("username", ""));
			
		dayEdittext = (EditText) findViewById(R.id.dayedt);
		dateEdittext= (EditText) findViewById(R.id.dateedt);
		timeEdittext = (EditText) findViewById(R.id.timeedt);
		timeZoneEdittext = (EditText) findViewById(R.id.timezoneedt);
		placeEdittext = (EditText) findViewById(R.id.placeedt);
		prospectNameEdittext = (EditText) findViewById(R.id.nameedt);
		spousesNameEdittext = (EditText) findViewById(R.id.sposesnameedt);
		contactEdittext = (EditText) findViewById(R.id.contactedt);
		traineeEdittext = (EditText) findViewById(R.id.traineeedt);
		traineephoneEdittext= (EditText) findViewById(R.id.traineephedt);
		appt_typeEdittext = (EditText) findViewById(R.id.apptedt);
		notesEdittext = (EditText) findViewById(R.id.noteedt);
		myScrollView=(ScrollView)findViewById(R.id.scrollview);
		
		profbtn1=(Button)findViewById(R.id.matchupProfileButton1);
		profbtn2=(Button)findViewById(R.id.matchupProfileButton2);
		profbtn3=(Button)findViewById(R.id.matchupProfileButton3);
		profbtn4=(Button)findViewById(R.id.matchupProfileButton4);
		profbtn5=(Button)findViewById(R.id.matchupProfileButton5);
		profbtn6=(Button)findViewById(R.id.matchupProfileButton6);
		profbtn7=(Button)findViewById(R.id.matchupProfileButton7);
		profbtn8=(Button)findViewById(R.id.matchupProfileButton8);
		profbtnspan=(Button)findViewById(R.id.matchupProfileButtonSpan);
		
		spouseTextView=(TextView)findViewById(R.id.sposesnametxt);
		
		//traineeEdittext.setText(sharedPreferences.getString("firstname", "")+" "+sharedPreferences.getString("lastname", ""));
		
		profbtn1.setOnClickListener(this);
		profbtn2.setOnClickListener( this);
		profbtn3.setOnClickListener( this);
		profbtn4.setOnClickListener( this);
		profbtn5.setOnClickListener( this);
		profbtn6.setOnClickListener(this);
		profbtn7.setOnClickListener( this);
		profbtn8.setOnClickListener(this);
		profbtnspan.setOnClickListener(this);
		
		for (int i = 0; i <9; i++) {
			btntruefalse[i]=false;
		}
		
		
		ALLMAINLAYOUT=(RelativeLayout)findViewById(R.id.MAILALL_LAYOUT);
		MAINSEARCHLAYOUT=(RelativeLayout)findViewById(R.id.TRAINEELIST_MAIN_LAYOUT);
		searchEdittext=(EditText)findViewById(R.id.TRAINEE_SEARCH_EDITTEXT);
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
				for (int i = 0; i < agentByTypeArrayList.size(); i++) {
					AgentListVO agentVo=agentByTypeArrayList.get(i);
					String compairstr=agentVo.firstname + " "+ agentVo.lastname + "(" + agentVo.agent_id+ ")";
					if (textlength <= compairstr.length()) {
						/***
						 * If you want to highlight the countries which start
						 * with entered letters then choose this block. And
						 * comment the below If condition Block
						 */
						if (searchEdittext.getText().toString().equalsIgnoreCase(
										(String) compairstr.subSequence(
												0, textlength))) {
							sortArrayList.add(agentVo);
							// image_sort.add(listview_images[i]);
						}

						/***
						 * If you choose the below block then it will act like a
						 * Like operator in the Mysql
						 */

						/*
						 * if(listview_names[i].toLowerCase().contains(
						 * et.getText().toString().toLowerCase().trim())) {
						 * array_sort.add(listview_names[i]); }
						 */
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
				  mainlayout.removeAllViews();
				  MAINSEARCHLAYOUT.setVisibility(View.GONE);
				  ALLMAINLAYOUT.setVisibility(View.VISIBLE);
			}
		});
		
		ImageButton addnewtrainee=(ImageButton)findViewById(R.id.TRAINEE_ADD_BTN);
		addnewtrainee.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			ShowDialog();	
			}
		});
		
		traineeEdittext.setFocusable(false);
		traineeEdittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (agentByTypeArrayList != null
						&& agentByTypeArrayList.size() > 0) {
					sortArrayList.addAll(agentByTypeArrayList);
                    searchEdittext.setText("");
                    myScrollView.smoothScrollTo(0, 0);
					SearchTrainee(sortArrayList);
				} else
					AppUtils.ShowAlertDialog(MatchupActivity.this,
							"Please Reload the contents");
			}
		});

		if (AppUtils.isNetworkAvailable(MatchupActivity.this)) {
			addValueintoEventList();
			new getAgentData_By_Type().execute();
		} else
			AppUtils.ShowAlertDialog(MatchupActivity.this,
					"No Internet Connection Available.");

		
		
		addValueintoEventList();
		
		dayEdittext.setFocusable(false);
		dayEdittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
					ClickedEdittext="day";
					WheelEvents.setViewAdapter(new WheelAdapter(
							MatchupActivity.this));

					if (!SDEvents.isOpened()) {
						SDEvents.animateOpen();
						SDEvents.setVisibility(View.VISIBLE);
					}
			}
		});

		timeZoneEdittext.setFocusable(false);
		timeZoneEdittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
					ClickedEdittext="timezone";
					WheelEvents.setViewAdapter(new WheelAdapter(
							MatchupActivity.this));

					if (!SDEvents.isOpened()) {
						SDEvents.animateOpen();
						SDEvents.setVisibility(View.VISIBLE);
					}
			}
		});
		
		dateEdittext.setText(chatSingleton.getDate(sharedPreferences.getString("usertimezone", "PST")));
		
		if(!dateEdittext.getText().toString().equalsIgnoreCase(""))
		   UTCDATE=dateEdittext.getText().toString();
		
		dateEdittext.setFocusable(false);
		dateEdittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ClickedEdittext="date";
				DATETIMEPICKER();
				//setDateTimeField();
			}
		});
		timeEdittext.setText(chatSingleton.getTime(sharedPreferences.getString("usertimezone", "PST")));
		if(!timeEdittext.getText().toString().equalsIgnoreCase(""))
			   UTCTIME=timeEdittext.getText().toString();
		
		timeEdittext.setFocusable(false);
		timeEdittext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ClickedEdittext="time";
				DATETIMEPICKER();
			}
		});
		try {
			traineephoneEdittext.addTextChangedListener(new PhoneNumberTextWatcher());
			traineephoneEdittext.setFilters(new InputFilter[] { new PhoneNumberFilter(), new InputFilter.LengthFilter(12) });
			
			contactEdittext.addTextChangedListener(new PhoneNumberTextWatcher());
			contactEdittext.setFilters(new InputFilter[] { new PhoneNumberFilter(), new InputFilter.LengthFilter(12) });
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
		
		Button submitbtn=(Button)findViewById(R.id.Matchupsubmitbtn);
		submitbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(AppUtils.isNetworkAvailable(MatchupActivity.this))
				{
					if(emptyCheck(dayEdittext)&&emptyCheck(dateEdittext)&&emptyCheck(timeEdittext)&&emptyCheck(timeZoneEdittext)&&emptyCheck(placeEdittext)&&emptyCheck(prospectNameEdittext)&&emptyCheck(contactEdittext)&&emptyCheck(traineeEdittext)&&emptyCheck(traineephoneEdittext)&&emptyCheck(appt_typeEdittext)&&emptyCheck(notesEdittext))
					{ 
						
							if(!emptyCheck(spousesNameEdittext)&&btntruefalse[1])
								AppUtils.ShowAlertDialog(MatchupActivity.this,"All field are mandatory!!!");
							else
							{
								if(isValidMobile(contactEdittext.getText().toString())&&isValidMobile(traineephoneEdittext.getText().toString()))
								{
									SimpleDateFormat format1=new SimpleDateFormat("MM/dd/yyyy");
									Date dt1=null;
									try {
										dt1 = format1.parse(dateEdittext.getText().toString());
									} catch (ParseException e) {
										e.printStackTrace();
									}
									DateFormat format2=new SimpleDateFormat("EEEE"); 
									
									if(dayEdittext.getText().toString().equalsIgnoreCase(format2.format(dt1)))
									new myTask_saveUserDetails_call().execute();
									else
										AppUtils.ShowAlertDialog(MatchupActivity.this,"Day and Date do not Match..");
									
								}else
								AppUtils.ShowAlertDialog(MatchupActivity.this,"Contact must be 10 digits length");
							}
						
						
						
					}//end of if
					else
						AppUtils.ShowAlertDialog(MatchupActivity.this,"All field are mandatory!!!");
					
				}//end of if for internet check
				else
					AppUtils.ShowAlertDialog(MatchupActivity.this,"No Internet Connection Available.");
						
			}
		});
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
		
	}//*******end of onCreate()
	
	

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
				String spoustext="",btnarray="";
				
				if(!emptyCheck(spousesNameEdittext))
					spoustext="";
				else
					spoustext=spousesNameEdittext.getText().toString();
				
				if(btntruefalse[0])
					btnarray=btnarray+"1";
				else
					btnarray=btnarray+"0";
				
			for (int i = 1; i < 9; i++) {
				if(btntruefalse[i])
					btnarray=btnarray+",1";
				else
					btnarray=btnarray+",0";
			}
					
				
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

				nameValuePair.add(new BasicNameValuePair("user_id",
						sharedPreferences.getString("userid", "")));
				nameValuePair
						.add(new BasicNameValuePair("sec_user", sharedPreferences.getString("username", "")));
				nameValuePair
						.add(new BasicNameValuePair("sec_pass", sharedPreferences.getString("password", "")));

				nameValuePair.add(new BasicNameValuePair("match_day",
						dayEdittext.getText().toString()));
				nameValuePair.add(new BasicNameValuePair("match_date", UTCDATE.equalsIgnoreCase("")?chatSingleton.UTCDATE:UTCDATE));
				nameValuePair.add(new BasicNameValuePair("match_time", UTCTIME.equalsIgnoreCase("")?chatSingleton.UTCTIME:UTCTIME));
				nameValuePair.add(new BasicNameValuePair("place", placeEdittext
						.getText().toString()));
				nameValuePair.add(new BasicNameValuePair("match_name",
						prospectNameEdittext.getText().toString()));
				nameValuePair.add(new BasicNameValuePair("contact",contactEdittext
						.getText().toString()));
				nameValuePair.add(new BasicNameValuePair("trainee",traineeuserid.equals("")?traineeEdittext.getText().toString():traineeuserid));
				nameValuePair.add(new BasicNameValuePair("trainee_phone", traineephoneEdittext
						.getText().toString()));
				nameValuePair.add(new BasicNameValuePair("app_type",
						appt_typeEdittext.getText().toString()));
				nameValuePair.add(new BasicNameValuePair("notes",notesEdittext
						.getText().toString()));
				
				nameValuePair.add(new BasicNameValuePair("match_timezone",timeZoneEdittext
						.getText().toString()));
				nameValuePair.add(new BasicNameValuePair("spouse_name", spoustext));
				nameValuePair.add(new BasicNameValuePair("profile_arr", btnarray));
				
				try {

					// Defined URL where to send data
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"https://bscpro.com/matchup_api/");
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
							"UTF-8"));
					HttpResponse response = httpclient.execute(httppost);
					responseText = EntityUtils.toString(response.getEntity());
					//System.out.println(responseText + "response is display");

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
				
				 if (responseText.equals("success")&&status.equals("ok")) {
				 AppUtils.ShowAlertDialog(MatchupActivity.this,"Saved Successfully.");
				 clearData();
				 } else {
					 AppUtils.ShowAlertDialog(MatchupActivity.this,"Error :"+responseText);
				 }
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				 } catch (JSONException e) {
					 // TODO Auto-generated catch block
					 if (mProgressDialog != null)
							mProgressDialog.dismiss();
					 AppUtils.ShowAlertDialog(MatchupActivity.this,"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
						
					 e.printStackTrace();
					 AirbrakeNotifier.notify(e);
					 }
			}// end of onpost()
		}// ends of Async task

		// ******************************Async task claass---GetAgent data BY TYPE
		public class getAgentData_By_Type extends AsyncTask<String, String, String> {

			protected void onPreExecute() {
				super.onPreExecute();
				 onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
			}

			@Override
			protected String doInBackground(String... strings) {
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
				nameValuePair.add(new BasicNameValuePair("type", "TA"));
				nameValuePair.add(new BasicNameValuePair("sec_user",
						sharedPreferences.getString("username", "")));
				nameValuePair.add(new BasicNameValuePair("sec_pass",
						sharedPreferences.getString("password", "")));

				try {

					// Defined URL where to send data
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"https://bscpro.com/profile_api/getUsersByType/");
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
							"UTF-8"));
					HttpResponse response = httpclient.execute(httppost);
					typeresponseString = EntityUtils.toString(response.getEntity());
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

					jresponse = new JSONObject(typeresponseString);

					responseText = jresponse.getString("message");
					status = jresponse.getString("status");
					JSONArray activityArray = null;
					if (responseText.equalsIgnoreCase("success") && status.equals("ok")) {
						agentByTypeArrayList.clear();
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

							agentByTypeArrayList.add(cVo);
						}// end of for

					} else
						AppUtils.ShowAlertDialog(MatchupActivity.this,
								"ERROR: " + responseText);

					if (mProgressDialog != null)
						mProgressDialog.dismiss();

				} catch (Exception e) {
					// TODO: handle exception
					// myProgressBar.setVisibility(View.GONE);
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(MatchupActivity.this,
							"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
					e.printStackTrace();
					AirbrakeNotifier.notify(e);
				}

				// new group_chat_message().execute();
				// do stuff after posting data
			}
		}// end of getby Type

		
	public void onClick(View v) {

       if(v == profbtn1){
    	   
    	   if(!btntruefalse[0])
    	   {
    		 btntruefalse[0]=true;  
             profbtn1.setBackgroundColor(Color.parseColor("#2175d9"));
    	   }
    	   else
    	   {
    		   btntruefalse[0]=false;  
    		   profbtn1.setBackgroundColor(Color.parseColor("#aaa8aa"));
    	   }
    	   displayToastAboveButton(profbtn1,"25+Y.O");
        }
       else if(v == profbtn2){
    	   if(!btntruefalse[1])
    	   {
    		 btntruefalse[1]=true;  
             profbtn2.setBackgroundColor(Color.parseColor("#2175d9"));
             spousesNameEdittext.setVisibility(View.VISIBLE);
             spouseTextView.setVisibility(View.VISIBLE);
    	   }
    	   else
    	   {
    		   btntruefalse[1]=false;  
    		   profbtn2.setBackgroundColor(Color.parseColor("#aaa8aa"));
    		   spousesNameEdittext.setVisibility(View.GONE);
    		   spousesNameEdittext.setText("");
               spouseTextView.setVisibility(View.GONE);
    	   }    	   
    	   displayToastAboveButton(profbtn2,"Married");
        }
       else if(v == profbtn3){
    	   if(!btntruefalse[2])
    	   {
    		 btntruefalse[2]=true;  
             profbtn3.setBackgroundColor(Color.parseColor("#2175d9"));
    	   }
    	   else
    	   {
    		   btntruefalse[2]=false;  
    		   profbtn3.setBackgroundColor(Color.parseColor("#aaa8aa"));
    	   }
    	   displayToastAboveButton(profbtn3,"Dependent Children");
        }
       else if(v == profbtn4){
    	   if(!btntruefalse[3])
    	   {
    		 btntruefalse[3]=true;  
             profbtn4.setBackgroundColor(Color.parseColor("#2175d9"));
    	   }
    	   else
    	   {
    		   btntruefalse[3]=false;  
    		   profbtn4.setBackgroundColor(Color.parseColor("#aaa8aa"));
    	   }
    	   displayToastAboveButton(profbtn4,"Homeowner");
        }
       else if(v == profbtn5){
    	   if(!btntruefalse[4])
    	   {
    		 btntruefalse[4]=true;  
             profbtn5.setBackgroundColor(Color.parseColor("#2175d9"));
    	   }
    	   else
    	   {
    		   btntruefalse[4]=false;  
    		   profbtn5.setBackgroundColor(Color.parseColor("#aaa8aa"));
    	   }
    	   displayToastAboveButton(profbtn5,"Solid Carreer Background");
     }
    else if(v == profbtn6){
    	 if(!btntruefalse[5])
  	   {
  		 btntruefalse[5]=true;  
           profbtn6.setBackgroundColor(Color.parseColor("#2175d9"));
  	   }
  	   else
  	   {
  		   btntruefalse[5]=false;  
  		   profbtn6.setBackgroundColor(Color.parseColor("#aaa8aa"));
  	   }
    	 displayToastAboveButton(profbtn6,"$40,000+ income");
     }
    else if(v == profbtn7){
    	 if(!btntruefalse[6])
  	   {
  		 btntruefalse[6]=true;  
           profbtn7.setBackgroundColor(Color.parseColor("#2175d9"));
  	   }
  	   else
  	   {
  		   btntruefalse[6]=false;  
  		   profbtn7.setBackgroundColor(Color.parseColor("#aaa8aa"));
  	   }
    	 displayToastAboveButton(profbtn7,"Dissatisfied");
     }
    else if(v == profbtn8){
    	 if(!btntruefalse[7])
  	   {
  		 btntruefalse[7]=true;  
           profbtn8.setBackgroundColor(Color.parseColor("#2175d9"));
  	   }
  	   else
  	   {
  		   btntruefalse[7]=false;  
  		   profbtn8.setBackgroundColor(Color.parseColor("#aaa8aa"));
  	   }
    	 displayToastAboveButton(profbtn8,"Enterpreneurial");
  }
 else if(v == profbtnspan){
	 if(!btntruefalse[8])
	   {
		 btntruefalse[8]=true;  
       profbtnspan.setBackgroundColor(Color.parseColor("#2175d9"));
	   }
	   else
	   {
		   btntruefalse[8]=false;  
		   profbtnspan.setBackgroundColor(Color.parseColor("#aaa8aa"));
	   }
	 displayToastAboveButton(profbtnspan,"Spanish Speaking Preferred");
    }
  }//end of onclick
	
	
	public void DatepickerDialog()
	{
		Calendar c = Calendar.getInstance();
	      int mYear = c.get(Calendar.YEAR);
	      int mMonth = c.get(Calendar.MONTH);
	      int mDay = c.get(Calendar.DAY_OF_MONTH);

	         OnDateSetListener mDateSetListener = null;
			DatePickerDialog dialog =
	        new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
	        dialog.show();
	}
	
	public void DATETIMEPICKER() {
		
		
		if(!emptyCheck(dayEdittext))
		{
			AppUtils.ShowAlertDialog(MatchupActivity.this, "Please Select The Day.");
			return ;
		}
		else
		{
		
		final View dialogView = View.inflate(this, R.layout.date_time_picker,
				null);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		final DatePicker datePicker = (DatePicker) dialogView
				.findViewById(R.id.date_picker);
		if(!dateEdittext.getText().toString().equalsIgnoreCase(""))
		  {
			try {
			String temp2=dateEdittext.getText().toString();
			String[] strArray = temp2.split("\\/");
			
			int month=Integer.parseInt(strArray[0]);
			int day=Integer.parseInt(strArray[1]);
			int year=Integer.parseInt(strArray[2]);
			
			datePicker.updateDate(year,month-1,day);
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		  }
		
		final TimePicker timePicker = (TimePicker) dialogView
				.findViewById(R.id.time_picker);
		
		if(!timeEdittext.getText().toString().equalsIgnoreCase(""))
		  {
			try {
			
				 SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa");
			        Date date = null;
			        try {
			            date = sdf.parse(timeEdittext.getText().toString());
			        } catch (ParseException e) {
			        }
			        Calendar c = Calendar.getInstance();
			        c.setTime(date);
			        timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
			        timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		  }
		final SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
		
		if(ClickedEdittext.equalsIgnoreCase("time"))
		{
				datePicker.setVisibility(View.GONE);
				writeFormat2 = new SimpleDateFormat(
						"hh:mm aaa");
				//writeFormat2.setTimeZone(TimeZone.getTimeZone("PST"));
				//dateFormatGmt = new SimpleDateFormat("hh:mm aaa");
				//dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
			
		}
		else if(ClickedEdittext.equalsIgnoreCase("date"))
		{
			
				timePicker.setVisibility(View.GONE);
				writeFormat2 = new SimpleDateFormat(
						"MM/dd/yyyy");
				//writeFormat2.setTimeZone(TimeZone.getTimeZone("PST"));
				//dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd");
				//dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		}

		dialogView.findViewById(R.id.date_time_set).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Calendar calendar = new GregorianCalendar(datePicker
								.getYear(), datePicker.getMonth(), datePicker
								.getDayOfMonth(), timePicker.getCurrentHour(),
								timePicker.getCurrentMinute());

						Date time = calendar.getTime();
						
						if(ClickedEdittext.equalsIgnoreCase("time"))
						{
							timeEdittext.setText(writeFormat2.format(time));
							UTCTIME=writeFormat2.format(time);
							timeEdittext.setFocusable(false);
						}
						else
							if(ClickedEdittext.equalsIgnoreCase("date"))
							{
								if(dayEdittext.getText().toString().equalsIgnoreCase(simpledateformat.format(time)))
								{
									dateEdittext.setText(writeFormat2.format(time));
									UTCDATE=writeFormat2.format(time);
								}
								else
								{
									AppUtils.ShowAlertDialog(MatchupActivity.this, "Do Not Match Selected Date And Day,Please Try Again.");
									//dateEdittext.setText(chatSingleton.getDate(timezone));
								}
								
								dateEdittext.setFocusable(false);
							}

						//bussinessdateEdittext.setText(writeFormat2.format(time));
						//bussinessdateEdittext.setFocusable(false);
						alertDialog.dismiss();
					}
				});
		alertDialog.setView(dialogView);
		alertDialog.show();
	
		}//end of else emptycheck

		}// end of date time picker
	
	public void clearData()
	{
		dayEdittext.setText("");
		dateEdittext.setText(chatSingleton.getDate(sharedPreferences.getString("usertimezone", "UTC")));
		timeEdittext.setText(chatSingleton.getTime(sharedPreferences.getString("usertimezone", "UTC")));
		timeZoneEdittext.setText("PST");
		placeEdittext.setText("");
		prospectNameEdittext.setText("");
		spousesNameEdittext.setText("");
		contactEdittext.setText("");
		traineeEdittext.setText("");
		traineephoneEdittext.setText("");
		appt_typeEdittext.setText("");
		notesEdittext.setText("");
		
		for (int i = 0; i <9; i++) {
			btntruefalse[i]=false;
			 
		}
		profbtn1.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtn2.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtn3.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtn4.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtn5.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtn6.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtn7.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtn8.setBackgroundColor(Color.parseColor("#aaa8aa"));
		profbtnspan.setBackgroundColor(Color.parseColor("#aaa8aa"));
		
		 spousesNameEdittext.setVisibility(View.GONE);
         spouseTextView.setVisibility(View.GONE);
	}
	
	public boolean emptyCheck(EditText editText) {
		if (editText.getText().toString().equals("")) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean isValidMobile(String phone2) 
	{
	    boolean check;
	    if(phone2.length()!=12)
	    {
	        check = false;
	        //contactedittext.setError("Not Valid Contact");
	    }
	    else
	    {
	        check = true;
	    }
	    return check;
	}

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(MatchupActivity.this,
					UserProfileActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.layoutprofile) {
//			MENU_ITEM_SELECTED = "PROFILE";
//			Intent intent = new Intent(MatchupActivity.this,
//					ProfileActivity.class);
//			startActivity(intent);
//		} 
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			Intent intent = new Intent(MatchupActivity.this,
					NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(MatchupActivity.this,
					AddBusinessActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			Intent intent = new Intent(MatchupActivity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			isExpanded=false;
			new CollapseAnimation(slidingPanel, panelWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f,menulayout);
		} 
//		else if (view.getId() == R.id.Layoutlocator) {
//			MENU_ITEM_SELECTED = "OFFICE LOCATOR";
//			Intent intent = new Intent(MatchupActivity.this,
//					OfficeLocatorActivity.class);
//			startActivity(intent);
//		} 
		else if (view.getId() == R.id.Layoutlogout) {
			MENU_ITEM_SELECTED = "LOGOUT";
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences
					.edit();
			editor.putString("userid", "");
			editor.putString("password", "");
			editor.putString("username", "");
			editor.putString("profileImage","");
			editor.commit();
			Intent intent = new Intent(MatchupActivity.this,
					LoginActivity.class);
			startActivity(intent);

		}else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(MatchupActivity.this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		}
		else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(MatchupActivity.this,
					Dashboard_Activity.class);
			startActivity(intent);

		}
//		else if (view.getId() == R.id.LayoutMATCHUPBOOK) {
//			MENU_ITEM_SELECTED = "Dashboard";
//			Intent intent = new Intent(this,
//					MatchupBookListActivity.class);
//			startActivity(intent);
//
//		}
	}//end of menu
	
	
	private void displayToastAboveButton(View v, String message)
	{
	    int xOffset = 0;
	    int yOffset = 0;
	    Rect gvr = new Rect();

	    View parent = (View) v.getParent(); 
	    int parentHeight = parent.getHeight();

	    if (v.getGlobalVisibleRect(gvr)) 
	    {       
	        View root = v.getRootView();

	        int halfWidth = root.getRight() / 2;
	        int halfHeight = root.getBottom() / 2;

	        int parentCenterX = ((gvr.right - gvr.left) / 2) + gvr.left;

	        int parentCenterY = ((gvr.bottom - gvr.top) / 2) + gvr.top;

	        if (parentCenterY <= halfHeight) 
	        {
	            yOffset = -(halfHeight - parentCenterY) - parentHeight;
	        }
	        else 
	        {
	            yOffset = (parentCenterY - halfHeight) - parentHeight;
	        }

	        if (parentCenterX < halfWidth) 
	        {         
	            xOffset = -(halfWidth - parentCenterX);     
	        }   

	        if (parentCenterX >= halfWidth) 
	        {
	            xOffset = parentCenterX - halfWidth;
	        }  
	    }

	    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
	    toast.setGravity(Gravity.CENTER, xOffset, yOffset);
	    toast.show();       
	}
	
	
	class WheelAdapter extends AbstractWheelTextAdapter {

		protected WheelAdapter(Context context) {
			super(context);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			
			if (ClickedEdittext.equals("day")) {
				return dayListStringArray.length;
			}
			if (ClickedEdittext.equals("timezone")) {
				return timeZoneStringArray.length;
			}
			return WheelPosition;

		}

		@Override
		protected CharSequence getItemText(int index) {
			
			if (ClickedEdittext.equals("day")) {
				return dayListStringArray[index];
			}
			if (ClickedEdittext.equals("timezone")) {
				return timeZoneStringArray[index];
			}
			return "";
		}
	}//end of wheel adapter
	public void SearchTrainee(ArrayList<AgentListVO> agentListArrayList)
	{
		  mainlayout = (RelativeLayout) findViewById(R.id.TRAINEELIST_LAYOUT);
		  mainlayout.removeAllViews();
		  
		 mainlayout.setVisibility(View.VISIBLE);
		 MAINSEARCHLAYOUT.setVisibility(View.VISIBLE);
		 ALLMAINLAYOUT.setVisibility(View.INVISIBLE);
		    Typeface type = Typeface.createFromAsset(MatchupActivity.this.getAssets(),"fonts/calibribold.ttf");	
		    Typeface type2 = Typeface.createFromAsset(MatchupActivity.this.getAssets(),"fonts/calibri.ttf");	
		    for (int i = 0; i < agentListArrayList.size(); i++) {
					//final ChatGroupVO groupVO=usergroupArrayList.get(i);
	               final AgentListVO agentVo=agentListArrayList.get(i);	
	               
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
					tv1.setText(agentVo.firstname + " "+ agentVo.lastname + "(" + agentVo.agent_id+ ")");
				
					layout.addView(tv1, params2);
					// *******************************************
					mainlayout.addView(layout,layoutParams);
					
					final int k=i;
					layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								//tv1.setText("");
								//tv1.setVisibility(View.GONE);
								
								traineeEdittext.setText(tv1.getText().toString());
								traineeuserid = agentVo.userid;
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
	}//end of SEARCH TRAINEE fields

	public void ShowDialog()
	{

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        
       
        final View Viewlayout = inflater.inflate(R.layout.addtrainee_dialodxml,
                (ViewGroup) findViewById(R.id.TRAINEE_dialog_Layout));       

		final Button donebtn=(Button)Viewlayout.findViewById(R.id.TRAINEE_DONE_button);
		final EditText newtrainee=(EditText)Viewlayout.findViewById(R.id.TRAINEE_NEW_ADD_EDITTEXT);
		final Button cancelbtn=(Button)Viewlayout.findViewById(R.id.TRAINEE_CANCLE_button);
		
		//popDialog.setIcon(android.R.drawable.btn_star_big_on);
		popDialog.setTitle("Add new Trainee");
		popDialog.setCancelable(false);
		popDialog.setView(Viewlayout);
		//txtPerc.setText(url);
		donebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					
					if(!newtrainee.getText().toString().equals(""))
					{
						traineeEdittext.setText(newtrainee.getText().toString());
						  mainlayout.removeAllViews();
						  MAINSEARCHLAYOUT.setVisibility(View.GONE);
						  ALLMAINLAYOUT.setVisibility(View.VISIBLE);
						  traineeuserid="";
						myAlertDialog.dismiss();	
					}
					else
						AppUtils.ShowAlertDialog(MatchupActivity.this, "Please Enter new Trainee..");
						
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
			}
		});


		myAlertDialog=popDialog.create();
		myAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    WindowManager.LayoutParams wmlp = myAlertDialog.getWindow().getAttributes();
	    //wmlp.gravity = Gravity.BOTTOM;
	    //wmlp.x = 100;   //x position
	    //wmlp.y = 100;   //y position
		myAlertDialog.show();
       // myAlertDialog.getWindow().setLayout((int)(AddBusinessActivity.devicewidth),(int)(add.deviceheight*40)/100);
	}//end of showdialog


}//end of Activity
