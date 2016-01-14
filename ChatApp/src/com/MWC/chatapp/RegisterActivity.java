package com.MWC.chatapp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import applicationVo.AgentListVO;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;
import applicationVo.UplineCEO_VO;
import applicationVo.UplineSMD_VO;
import applicationVo.UserProfileDataAsync;

public class RegisterActivity extends Activity {
	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
	LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,LayoutBPM_Check_in,MAINSEARCHLAYOUT,ALLMAINLAYOUT,menulayout;
	RelativeLayout mainlayout;
	public EditText searchEdittext;
	public AlertDialog myAlertDialog=null;

	public ArrayList<AgentListVO> agentByTypeArrayList = new ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> sortArrayList = new ArrayList<AgentListVO>();
	ScrollView myScrollView;
	public String typeresponseString = "";
	public String CEOuserid = "",SMDCEOuserid = "";
	Button submit, cancel;
	EditText firstname, lastname, agentid, authcode, userName, passWord,
			confirmpass, Email, level, uplineceo, uplinesmd;
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	String responseString = "";
	String responseText = "";
//	ArrayList<UplineCEO_VO> uplineCEOlist = new ArrayList<UplineCEO_VO>();
//	ArrayList<UplineSMD_VO> uplineSMDlist = new ArrayList<UplineSMD_VO>();

	String uplineCEO = "CEO,EVC";
	String uplineSMD = "CEO,EVC,SMD,EMD";

	String currenttextfield;
	SingletonActivity singletonactivity = null;
	ChatSingleton chatSingleton;
	UserProfileDataAsync dataAsync;

	WheelView WheelEvents;
	SlidingDrawer SDEvents;
	Button WheelDone, WheelCancle;
	int WheelPosition;
	boolean scrolling = false;
	String[] eventname = new String[] { "TA", "A", "SA", "MD", "SMD", "EMD",
			"CEO", "EVC" };

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

				if (currenttextfield.equals("level")) {
					if(WheelPosition<eventname.length)
					level.setText(eventname[WheelPosition]);
					
				} else if (currenttextfield.equals("uplineceo")) {
					if(chatSingleton.UplineCEOArrayList!=null&&chatSingleton.UplineCEOArrayList.size()>0&&WheelPosition<chatSingleton.UplineCEOArrayList.size())
					{
						AgentListVO uVo = chatSingleton.UplineCEOArrayList.get(WheelPosition);

						uplineceo.setText(uVo.firstname + " "
								+ uVo.lastname);
						
					}
				} else if (currenttextfield.equals("uplinesmd")) {
					if(chatSingleton.uplineSMDArrayList!=null&&chatSingleton.uplineSMDArrayList.size()>0&&WheelPosition<chatSingleton.uplineSMDArrayList.size())
					{

						AgentListVO cVo = chatSingleton.uplineSMDArrayList.get(WheelPosition);

						uplinesmd.setText( cVo.firstname + " "+ cVo.lastname );
					}

					
				}

				if (level.getText().toString().equals("SMD")
						|| (level.getText().toString().equals("EMD"))) {

					uplinesmd.setText("I am an SMD");
					uplinesmd.setEnabled(false);
					uplineceo.setEnabled(true);

				} else if (level.getText().toString().equals("CEO")
						|| (level.getText().toString().equals("EVC"))) {

					uplineceo.setText("I am an EVC/CEO");
					uplinesmd.setText("I am an SMD/EMD");
					
					uplineceo.setEnabled(false);
					uplinesmd.setEnabled(false);
					
				}
				else
				{
					uplineceo.setEnabled(true);
					uplinesmd.setEnabled(true);
				}
				
			}
		});
	}
	
	public void SearchTrainee(ArrayList<AgentListVO> agentListArrayList)
	{
		  mainlayout = (RelativeLayout) findViewById(R.id.TRAINEELIST_LAYOUT);
		  mainlayout.removeAllViews();
		  
		 mainlayout.setVisibility(View.VISIBLE);
		 MAINSEARCHLAYOUT.setVisibility(View.VISIBLE);
		 ALLMAINLAYOUT.setVisibility(View.INVISIBLE);
		    Typeface type = Typeface.createFromAsset(this.getAssets(),"fonts/calibribold.ttf");	
		    Typeface type2 = Typeface.createFromAsset(this.getAssets(),"fonts/calibri.ttf");	
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
					tv1.setText(agentVo.firstname + " "+ agentVo.lastname);
				
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
								if (currenttextfield.equals("uplineceo")) {
								uplineceo.setText(tv1.getText().toString());
								CEOuserid = agentVo.userid;
								}
								else if (currenttextfield.equals("uplinesmd")) {
								 uplinesmd.setText(tv1.getText().toString());
								 SMDCEOuserid = agentVo.userid;
								}
								
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
	 @Override
	 public void onBackPressed() {
		 
	 }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");
		
		chatSingleton=ChatSingleton.getInstance();
	   dataAsync=UserProfileDataAsync.getInstance(RegisterActivity.this);

		firstname = (EditText) findViewById(R.id.nameedt);
		lastname = (EditText) findViewById(R.id.lastnameedt);
		agentid = (EditText) findViewById(R.id.agentcodeedt);
		authcode = (EditText) findViewById(R.id.authocodeedt);
		userName = (EditText) findViewById(R.id.usernameedt);
		passWord = (EditText) findViewById(R.id.passwordedt);
		confirmpass = (EditText) findViewById(R.id.confirmpasswordedt);
		Email = (EditText) findViewById(R.id.emailedt);
		level = (EditText) findViewById(R.id.leveledt);
		uplineceo = (EditText) findViewById(R.id.uplineceoedt);
		uplinesmd = (EditText) findViewById(R.id.uplinesmdedt);

		submit = (Button) findViewById(R.id.createaccbtn);
		cancel = (Button) findViewById(R.id.cancelbtn);
         Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
         cancel.setTypeface(font);
         
		level.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				uplineceo.setText("");
				uplinesmd.setText("");

				currenttextfield = "level";

				WheelEvents.setViewAdapter(new WheelAdapter(
						RegisterActivity.this));

				if (!SDEvents.isOpened()) {
					SDEvents.animateOpen();
					SDEvents.setVisibility(View.VISIBLE);

				}
			}

		});
		
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
				if (currenttextfield.equals("uplineceo"))
				{
				for (int i = 0; i < chatSingleton.UplineCEOArrayList.size(); i++) {
					AgentListVO agentVo=chatSingleton.UplineCEOArrayList.get(i);
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
				
				}//end of if
				else if (currenttextfield.equals("uplinesmd")) {
					
					for (int i = 0; i < chatSingleton.uplineSMDArrayList.size(); i++) {
						AgentListVO agentVo=chatSingleton.uplineSMDArrayList.get(i);
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
					

				}//end of else
				
				
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
			//ShowDialog();	
			}
		});
		
		uplineceo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				currenttextfield = "uplineceo";
				
				if (chatSingleton.UplineCEOArrayList != null
						&& chatSingleton.UplineCEOArrayList.size() > 0) {
					sortArrayList.addAll(chatSingleton.UplineCEOArrayList);
                    searchEdittext.setText("");
                    myScrollView.smoothScrollTo(0, 0);
					SearchTrainee(sortArrayList);
				} else
					AppUtils.ShowAlertDialog(RegisterActivity.this,
							"Please Reload the contents");
				
//				if (level.getText().toString().equals("CEO")
//						|| (level.getText().toString().equals("EVC"))) {
//
//				} else {
//					WheelEvents.setViewAdapter(new WheelAdapter(
//							RegisterActivity.this));
//					if (!SDEvents.isOpened()) {
//						SDEvents.animateOpen();
//						SDEvents.setVisibility(View.VISIBLE);
//					}
//				}
			}

		});
		uplinesmd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				currenttextfield = "uplinesmd";
			
				if (chatSingleton.uplineSMDArrayList != null
						&& chatSingleton.uplineSMDArrayList.size() > 0) {
					sortArrayList.addAll(chatSingleton.uplineSMDArrayList);
                    searchEdittext.setText("");
                    myScrollView.smoothScrollTo(0, 0);
					SearchTrainee(sortArrayList);
				} else
					AppUtils.ShowAlertDialog(RegisterActivity.this,
							"Please Reload the contents");
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean fname = validateText(firstname);
				boolean lname = validateText(lastname);
				boolean agent_id = validateText(agentid);
				//boolean authorization_code = validateText(authcode);
				boolean username = validateText(userName);
				boolean password = validateText(passWord);
				boolean confirm_password = validateText(confirmpass);
				boolean email = validateText(Email);
				boolean usertype = validateText(level);
				boolean upline = validateText(uplineceo);
				boolean upline_smd = validateText(uplinesmd);

				final String mail = Email.getText().toString();
				final String Pwd = passWord.getText().toString();
				final String ConfPwd = confirmpass.getText().toString();
				final String uName = userName.getText().toString();
				final String ageCode = agentid.getText().toString();

				if (!isValidEmail(mail)) {
					Email.setError("Invalid email!!!");

				}
				if (!isValidPassword(Pwd, ConfPwd)) {
					passWord.setError("At least 4 character!!!");
					confirmpass
							.setError("Password do not match please try again!!!");

				}
				if (!isValidateuserName(uName)) {
					userName.setError("At least 4 character!!!");
				}
				if (!isValidateagentcode(ageCode)) {
					agentid.setError("At least 4 character!!!");
				}
				if (firstname.getText().toString().equals("")
						|| lastname.getText().toString().equals("")
						|| agentid.getText().toString().equals("")
						//|| authcode.getText().toString().equals("")
						|| userName.getText().toString().equals("")
						|| passWord.getText().toString().equals("")
						|| confirmpass.getText().toString().equals("")
						|| Email.getText().toString().equals("")
						|| level.getText().toString().equals("")
						|| uplineceo.getText().toString().equals("")
						|| uplinesmd.getText().toString().equals("")) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							RegisterActivity.this);
					builder.setTitle("BSCPRO");
					builder.setMessage("All field are mandatory!!!")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// do things
										}
									});
					AlertDialog alert = builder.create();
					alert.show();

				} else if (isValidEmail(mail) && isValidPassword(Pwd, ConfPwd)
						&& isValidateuserName(uName)
						&& isValidateagentcode(ageCode) && fname && lname
						&& usertype && upline
						&& upline_smd) {
                    if(AppUtils.isNetworkAvailable(RegisterActivity.this))
					new myTask_saveUserDetails_call().execute();
                    else
                    	AppUtils.ShowAlertDialog(RegisterActivity.this,"No Internet Connection Available.");
				}
			}
		});

		
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent cancel = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(cancel);
			}
		});
		
		Button newcancel = (Button) findViewById(R.id.newcancelbtn);
		newcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent cancel = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(cancel);
			}
		});

		addValueintoEventList();
		
		if(AppUtils.isNetworkAvailable(RegisterActivity.this))
		{
			if(chatSingleton.UplineCEOArrayList.size()<1||chatSingleton.uplineSMDArrayList.size()<1)
				new myTask_uplineCEODetails_call().execute();
		}
            else
            	AppUtils.ShowAlertDialog(RegisterActivity.this,"No Internet Connection Available.");

		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

		
	}//end of Async

	// JSON AsyncTask for saveUserDetails upload
	class myTask_saveUserDetails_call extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			
			if (level.getText().toString().equals("SMD")
					|| (level.getText().toString().equals("EMD"))) {
				SMDCEOuserid="";

			} else if (level.getText().toString().equals("CEO")
					|| (level.getText().toString().equals("EVC"))) {

				SMDCEOuserid="";
				CEOuserid="";
				
			}

			nameValuePair.add(new BasicNameValuePair("fname", firstname
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("lname", lastname
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("agent_id", agentid
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("authorization_code",
					authcode.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("username", userName
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("password", passWord
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("confirm_password",
					confirmpass.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("email", Email.getText()
					.toString()));
			nameValuePair.add(new BasicNameValuePair("usertype", level
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("upline",CEOuserid));
			nameValuePair.add(new BasicNameValuePair("upline_smd",SMDCEOuserid));
			
//			
//			nameValuePair.add(new BasicNameValuePair("upline", uplineceo
//					.getText().toString()));
//			nameValuePair.add(new BasicNameValuePair("upline_smd", uplinesmd
//					.getText().toString()));

			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("https://bscpro.com/auth_api");
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
			 
			 responseText = jresponse.getString("messgae");
			 String status = jresponse.getString("status");
			
			 if (status.equals("ok")) {
				 
					if (mProgressDialog != null)
					mProgressDialog.dismiss();
					clearData();

				 final AlertDialog alertDialog = new AlertDialog.Builder(
							RegisterActivity.this).create();
					alertDialog.setTitle("BSCPRO");
					alertDialog
							.setMessage(responseText+"\nPlease check your registered email for verification!!!");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									Intent i = new Intent(RegisterActivity.this,
											LoginActivity.class);
									startActivity(i);
								}
							});
					alertDialog.show();


			 
			 } else {
				 AppUtils.ShowAlertDialog(RegisterActivity.this,"Alert:"+responseText);
			 }
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
			 } catch (JSONException e) {
				 // TODO Auto-generated catch block
				 if (mProgressDialog != null)
						mProgressDialog.dismiss();
				 AppUtils.ShowAlertDialog(RegisterActivity.this,"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
					
				 e.printStackTrace();
				 AirbrakeNotifier.notify(e);
				 }
		}//end of on post Execute
	}//end of async

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
			if (currenttextfield.equals("level")) {
				return eventname.length;
			}
			if (currenttextfield.equals("uplineceo")) {
				return chatSingleton.UplineCEOArrayList.size();
			}
			if (currenttextfield.equals("uplinesmd")) {
				return chatSingleton.uplineSMDArrayList.size();
			}
			return WheelPosition;

		}

		@Override
		protected CharSequence getItemText(int index) {
			if (currenttextfield.equals("level")) {
				return eventname[index];
			}
			if (currenttextfield.equals("uplineceo")) {
				AgentListVO uVo = chatSingleton.UplineCEOArrayList.get(index);
				return  uVo.firstname + " " + uVo.lastname;

			}
			if (currenttextfield.equals("uplinesmd")) {
				AgentListVO cVo = chatSingleton.uplineSMDArrayList.get(index);
				return  cVo.firstname + " " + cVo.lastname;
			}
			return "";
		}
	}
	// JSON AsyncTask for saveUserDetails upload
	class myTask_uplineCEODetails_call extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("type", uplineCEO));

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
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
				AirbrakeNotifier.notify(ex);
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				if (!responseString.equals("ok")) {
					JSONObject jso = new JSONObject(responseString);
					JSONArray activityArray = null;
					try {
						activityArray = jso.getJSONArray("userlist");
					} catch (Exception e) {
						// TODO: handle exception
						activityArray = new JSONArray();
						activityArray.put(jso.getJSONObject("userlist")
								.getJSONObject("activity"));
					}

					for (int i = 0; i < activityArray.length(); i++) {

						JSONObject activityObject = (JSONObject) activityArray
								.get(i);
						AgentListVO uVo = new AgentListVO();
						if (!activityObject.isNull("id"))
							uVo.userid = activityObject.getString("id");
						if (!activityObject.isNull("username"))
							uVo.username = activityObject.getString("username");
						if (!activityObject.isNull("firstname"))
							uVo.firstname = activityObject
									.getString("firstname");
						if (!activityObject.isNull("lastname"))
							uVo.lastname = activityObject.getString("lastname");
						if (!activityObject.isNull("agent_id"))
							uVo.agent_id = activityObject.getString("agent_id");
						chatSingleton.UplineCEOArrayList.add(uVo);
					}

				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				

				if(AppUtils.isNetworkAvailable(RegisterActivity.this))
				{
					if(chatSingleton.uplineSMDArrayList.size()<1)
						new myTask_uplineCMDDetails_call().execute();
				}
		            else
		            	AppUtils.ShowAlertDialog(RegisterActivity.this,"No Internet Connection Available.");

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();

			}

		}

	}

	// JSON AsyncTask for saveUserDetails upload
	class myTask_uplineCMDDetails_call extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected Void doInBackground(Void... params) {
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("type", uplineSMD));

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
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
				AirbrakeNotifier.notify(ex);
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void args) {
			try {
				if (!responseString.equals("ok")) {
					JSONObject jso = new JSONObject(responseString);
					JSONArray activityArray = null;
					try {
						activityArray = jso.getJSONArray("userlist");
					} catch (Exception e) {
						// TODO: handle exception
						activityArray = new JSONArray();
						activityArray.put(jso.getJSONObject("userlist")
								.getJSONObject("activity"));
					}

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
						chatSingleton.uplineSMDArrayList.add(cVo);
					}
				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			}
		}
	}
	
	public void clearData()
	{
		firstname.setText("");
		lastname.setText("");
		agentid.setText("");
		authcode.setText("");
		userName.setText("");
		passWord.setText("");
		confirmpass.setText("");
		Email.setText("");
		level.setText("");
		uplineceo.setText("");
		uplinesmd.setText("");
	}

	// validating email id
	private boolean isValidEmail(String mail) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(mail);
		return matcher.matches();
	}

	// validating password with retype password
	private boolean isValidPassword(String Pwd, String ConfPwd) {
		if (Pwd.length() >= 4 && Pwd.equals(ConfPwd)) {
			return true;
		}
		return false;
	}

	public boolean validateText(EditText editText) {
		if (editText.getText().toString().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isValidateuserName(String uName) {
		if (uName.length() >= 4) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidateagentcode(String ageCode) {
		if (ageCode.length() >= 4) {
			return true;
		} else {
			return false;
		}
	}
}
