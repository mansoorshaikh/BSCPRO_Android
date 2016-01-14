package com.MWC.chatapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.airbrake.AirbrakeNotifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.TimePicker;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;
import applicationVo.PhoneNumberFilter;
import applicationVo.PhoneNumberTextWatcher;
import applicationVo.UserProfileDataAsync;

public class NewRecruitActivity extends Activity {

	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
			LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,menulayout;
	public static String MENU_ITEM_SELECTED = "";
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
	public EditText hiredateedittext, nameedittext, inviteredittext,
			contactedittext, codeedittext, followupedittext,fildtraineeEdittext;
	public CheckBox completeamachkbox, meetspousechkbox, submitlicchkbox,
			prospectlistchkbox, fieldtreaningchkbox, _three_threechkbox,
			fanchkbox, faststartchkbox;
	private ProgressDialog mProgressDialog;
	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	String responseString = "";
	String responseText = "", status = "";
	String[] trainee=new String[]{"Field Training","1","2","3","4","5","6","7","8","9","10"};
	SharedPreferences sharedPreferences;
	WheelView WheelEvents;
	SlidingDrawer SDEvents;
	Button WheelDone, WheelCancle;
	int WheelPosition;
	boolean scrolling = false;
	
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

						 
						 if(WheelPosition<(trainee.length))
							 fildtraineeEdittext.setText(trainee[WheelPosition]);
						 else
							 fildtraineeEdittext.setText("Field Training");
						   fildtraineeEdittext.setFocusable(false);
							//agentuserid=agentVo.userid;
				}
			});
		}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newrecruit);
		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");
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
			RelativeLayout LayoutBPM_Check_in = (RelativeLayout) findViewById(R.id.LayoutBPM_Check_In);
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
			NormalSmartImageView profileimageview = (NormalSmartImageView) findViewById(R.id.userimage);

			try {
				profileimageview.setImageUrl(sharedPreferences.getString(
						"profileImage", ""));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
			hiredateedittext = (EditText) findViewById(R.id.hiredateedt);
			nameedittext = (EditText) findViewById(R.id.nameedt);
			inviteredittext = (EditText) findViewById(R.id.inviteredt);
			contactedittext = (EditText) findViewById(R.id.newreccontactedt);
			codeedittext = (EditText) findViewById(R.id.codeedt);
			followupedittext = (EditText) findViewById(R.id.followedt);
			fildtraineeEdittext = (EditText) findViewById(R.id.Fildtrianee);

			completeamachkbox = (CheckBox) findViewById(R.id.complete);
			meetspousechkbox = (CheckBox) findViewById(R.id.meet);
			submitlicchkbox = (CheckBox) findViewById(R.id.submti);
			prospectlistchkbox = (CheckBox) findViewById(R.id.prospect);
			//fieldtreaningchkbox = (CheckBox) findViewById(R.id.field);
			_three_threechkbox = (CheckBox) findViewById(R.id.number);
			fanchkbox = (CheckBox) findViewById(R.id.fna);
			faststartchkbox = (CheckBox) findViewById(R.id.faststart);

			try {
				contactedittext
						.addTextChangedListener(new PhoneNumberTextWatcher());
				contactedittext.setFilters(new InputFilter[] {
						new PhoneNumberFilter(),
						new InputFilter.LengthFilter(12) });
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
			
			 TextView username=(TextView)findViewById(R.id.Chatusername);
				username.setText(sharedPreferences.getString("username", ""));

				addValueintoEventList();
				fildtraineeEdittext.setFocusable(false);
				fildtraineeEdittext.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
							WheelEvents.setViewAdapter(new WheelAdapter(
									NewRecruitActivity.this));

							if (!SDEvents.isOpened()) {
								SDEvents.animateOpen();
								SDEvents.setVisibility(View.VISIBLE);
							}
					}
				});
				
				ChatSingleton chatSingleton=ChatSingleton.getInstance();
				hiredateedittext.setText(chatSingleton.getDate(sharedPreferences.getString("usertimezone", "UTC")));
			hiredateedittext.setFocusable(false);
			hiredateedittext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DATETIMEPICKER();
				}
			});

			Button submitbtn = (Button) findViewById(R.id.submitbtn);
			submitbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (AppUtils.isNetworkAvailable(NewRecruitActivity.this)) {
						if (emptyCheck(hiredateedittext)
								&& emptyCheck(nameedittext)
								&& emptyCheck(inviteredittext)
								&& emptyCheck(contactedittext)
								&& emptyCheck(codeedittext)
								&& emptyCheck(followupedittext)) {
							if (isValidMobile(contactedittext.getText()
									.toString()))
								new myTask_saveUserDetails_call().execute();
							else
								AppUtils.ShowAlertDialog(
										NewRecruitActivity.this,
										"Contact must be 10 digits length");

						}// end of if
						else
							AppUtils.ShowAlertDialog(NewRecruitActivity.this,
									"All field are mandatory!!!");

					}// end of if for internet check
					else
						AppUtils.ShowAlertDialog(NewRecruitActivity.this,
								"No Internet Connection Available.");

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}// end of oncreate()

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

			nameValuePair.add(new BasicNameValuePair("hire_date",
					hiredateedittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("name", nameedittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("inviter", inviteredittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("contact", contactedittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("code", codeedittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("follow_up",
					followupedittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("complete_ama",
					completeamachkbox.isChecked() ? "1" : "0"));
			nameValuePair.add(new BasicNameValuePair("submit_lic",
					submitlicchkbox.isChecked() ? "1" : "0"));
			nameValuePair.add(new BasicNameValuePair("meet_spouse",
					meetspousechkbox.isChecked() ? "1" : "0"));
			nameValuePair.add(new BasicNameValuePair("prospect_list",
					prospectlistchkbox.isChecked() ? "1" : "0"));
			nameValuePair.add(new BasicNameValuePair("field_trainings",
					fildtraineeEdittext.getText().toString().equalsIgnoreCase("Field Training") ? "0" :fildtraineeEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("FNA", fanchkbox
					.isChecked() ? "1" : "0"));
			nameValuePair.add(new BasicNameValuePair("Three_3_30",
					_three_threechkbox.isChecked() ? "1" : "0"));
			nameValuePair.add(new BasicNameValuePair("Fast_school",
					faststartchkbox.isChecked() ? "1" : "0"));

			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/speedfilter_api/");
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
				status = jresponse.getString("status");

				if (responseText.equals("success") && status.equals("ok")) {
					final AlertDialog alertDialog = new AlertDialog.Builder(
							NewRecruitActivity.this).create();
					alertDialog.setTitle("BSCPRO");
					alertDialog.setMessage(nameedittext.getText().toString()
							+ " has been added to the 8 filters.");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									clearData();
									alertDialog.dismiss();
								}
							});
					alertDialog.show();
				} else {
					final AlertDialog alertDialog = new AlertDialog.Builder(
							NewRecruitActivity.this).create();
					alertDialog.setTitle("BSCPRO");
					alertDialog.setMessage("Error: " + responseText);
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									alertDialog.dismiss();
								}
							});
					alertDialog.show();
				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(NewRecruitActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				AirbrakeNotifier.notify(e);
				e.printStackTrace();
			}
		}// end of onpost()
	}// ends of Async task
	
	
	public void clearData()
	{
		ChatSingleton chatSingleton=ChatSingleton.getInstance();
		hiredateedittext.setText(chatSingleton.getDate(sharedPreferences.getString("usertimezone", "UTC")));
		nameedittext.setText("");
		inviteredittext.setText("");
		contactedittext.setText("");
		codeedittext.setText("");
		followupedittext.setText("");
		fildtraineeEdittext.setText(trainee[0]);
		
		completeamachkbox.setChecked(false);
		meetspousechkbox.setChecked(false);
		submitlicchkbox.setChecked(false);
		prospectlistchkbox.setChecked(false);
		//fieldtreaningchkbox.setChecked(false);
		_three_threechkbox.setChecked(false);
		fanchkbox.setChecked(false);
		faststartchkbox.setChecked(false);
		
	}

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(NewRecruitActivity.this,
					UserProfileActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.layoutprofile) {
//			MENU_ITEM_SELECTED = "PROFILE";
//			Intent intent = new Intent(NewRecruitActivity.this,
//					ProfileActivity.class);
//			startActivity(intent);
//		} 
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			isExpanded=false;
			new CollapseAnimation(slidingPanel, panelWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f,menulayout);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(NewRecruitActivity.this,
					AddBusinessActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			Intent intent = new Intent(NewRecruitActivity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(NewRecruitActivity.this,
					MatchupActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.Layoutlocator) {
//			MENU_ITEM_SELECTED = "OFFICE LOCATOR";
//			Intent intent = new Intent(NewRecruitActivity.this,
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
			Intent intent = new Intent(NewRecruitActivity.this,
					LoginActivity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(NewRecruitActivity.this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		}
		else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(NewRecruitActivity.this,
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
	}

	public void DATETIMEPICKER() {

		final View dialogView = View.inflate(this, R.layout.date_time_picker,
				null);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

		final DatePicker datePicker = (DatePicker) dialogView
				.findViewById(R.id.date_picker);
		if(!hiredateedittext.getText().toString().equalsIgnoreCase(""))
		  {
			try {
			
			String temp2=hiredateedittext.getText().toString();
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
		
	final	TimePicker timePicker = (TimePicker) dialogView
				.findViewById(R.id.time_picker);
		timePicker.setVisibility(View.GONE);
		dialogView.findViewById(R.id.date_time_set).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {


						Calendar calendar = new GregorianCalendar(datePicker
								.getYear(), datePicker.getMonth(), datePicker
								.getDayOfMonth(), timePicker.getCurrentHour(),
								timePicker.getCurrentMinute());

						Date time = calendar.getTime();

						DateFormat writeFormat2 = new SimpleDateFormat(
								"MM/dd/yyyy");

						hiredateedittext.setText(writeFormat2.format(time));
						hiredateedittext.setFocusable(false);
						alertDialog.dismiss();
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
			
			return trainee.length;

		}

		@Override
		protected CharSequence getItemText(int index) {
			
			return trainee[index];
		}
	}//end of wheel adapter

}// end of class
