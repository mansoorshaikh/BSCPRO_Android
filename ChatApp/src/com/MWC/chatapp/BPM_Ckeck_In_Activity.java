package com.MWC.chatapp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AppUtils;

public class BPM_Ckeck_In_Activity extends Activity {

	Button btnShowLocation;
	// GPSTracker class
	GPSTracker gps;
	public String office_id="";
	String Latitude="",Longitude="";
	double latitude;
	double longitude;
	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
			LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,
			LayoutBPM_Check_in,menulayout;
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

	SharedPreferences sharedPreferences;
	public String responseText = "";
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bpm__ckeck__in_);

		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");
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

			menulayout=(RelativeLayout)findViewById(R.id.Layoutmenubaar);
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
			Chatlayout = (RelativeLayout) findViewById(R.id.chatlayout);
			//layoutProfile = (RelativeLayout) findViewById(R.id.layoutprofile);
			LayoutRecruit = (RelativeLayout) findViewById(R.id.Layoutrecruit);
			LayoutBusiness = (RelativeLayout) findViewById(R.id.Layoutbusiness);
			LayoutGuest = (RelativeLayout) findViewById(R.id.Layoutguest);
			LayoutMatchup = (RelativeLayout) findViewById(R.id.Layoutmatchup);
			//LayoutLocator = (RelativeLayout) findViewById(R.id.Layoutlocator);
			LayoutLogout = (RelativeLayout) findViewById(R.id.Layoutlogout);
			LayoutBPM_Check_in = (RelativeLayout) findViewById(R.id.LayoutBPM_Check_In);
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
			 TextView username=(TextView)findViewById(R.id.Chatusername);
				username.setText(sharedPreferences.getString("username", ""));

			//getLatLang();
			btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
			
			if(sharedPreferences.getString("checkedin", "").equalsIgnoreCase(""))
		     {
				if(AppUtils.isNetworkAvailable(BPM_Ckeck_In_Activity.this))
				{
					if(getLatLang())
					{
						new myTask_saveUserDetails_call().execute();
						btnShowLocation.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								if(AppUtils.isNetworkAvailable(BPM_Ckeck_In_Activity.this))
								{
									
									new myTask_LoginButtonClick_call().execute();
								}
								else
									AppUtils.ShowAlertDialog(BPM_Ckeck_In_Activity.this,"No Internet Connection Avelable.");
							}
						});
					}//end of if

					else
						AppUtils.ShowAlertDialog(BPM_Ckeck_In_Activity.this,"No Location Dectected,Please try again.");
				}
				else
					AppUtils.ShowAlertDialog(BPM_Ckeck_In_Activity.this,"No Internet Connection Available.");
			
		     }//end of if
			else
			{
				btnShowLocation.setVisibility(View.VISIBLE);
				btnShowLocation.setText(sharedPreferences.getString("checkedin", ""));
				btnShowLocation.setClickable(false);
			}
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
	}// end of Oncreate()

	public boolean getLatLang() {
		// create class object
		gps = new GPSTracker(BPM_Ckeck_In_Activity.this);
		boolean gpsstatus=false;

		// check if GPS enabled
		if (gps.canGetLocation()) {

			NumberFormat formatter = new DecimalFormat("#0.00");
			latitude = gps.getLatitude();
			Latitude=formatter.format(latitude);
			longitude = gps.getLongitude();
			Longitude=formatter.format(longitude);
			gpsstatus=true;

		} else {
			
			gps.showSettingsAlert();
		}
		return gpsstatus;
	}

	// JSON AsyncTask for saveUserDetails upload
	class myTask_saveUserDetails_call extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("user_id",
					sharedPreferences.getString("userid", "")));
			
//			nameValuePair.add(new BasicNameValuePair("user_id",
//					"3"));
			nameValuePair.add(new BasicNameValuePair("sec_user",
					sharedPreferences.getString("username", "")));
			nameValuePair.add(new BasicNameValuePair("sec_pass",
					sharedPreferences.getString("password", "")));

			nameValuePair.add(new BasicNameValuePair("latitude", Latitude));
			nameValuePair.add(new BasicNameValuePair("longitude",Longitude));

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/office_api/findnearoffice");
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

				responseText = jresponse.getString("BPMoffice");
				String status = jresponse.getString("status");
				office_id=jresponse.getString("officeId");

				if (status.equals("ok")) {
					if(!office_id.equalsIgnoreCase(""))
					{
						btnShowLocation.setVisibility(View.VISIBLE);
						btnShowLocation.setText(responseText);	
						btnShowLocation.setClickable(true);
					}
					
				} else {
					AppUtils.ShowAlertDialog(BPM_Ckeck_In_Activity.this,
							"Message :" + responseText);
				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(BPM_Ckeck_In_Activity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");

				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		}// end of onpost()
	}// ends of Async task

	
	// JSON AsyncTask for saveUserDetails upload
		class myTask_LoginButtonClick_call extends AsyncTask<Void, Void, Void> {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
			}

			@Override
			protected Void doInBackground(Void... params) {

				try {

				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

				nameValuePair.add(new BasicNameValuePair("user_id",
						sharedPreferences.getString("userid", "")));
				
				//nameValuePair.add(new BasicNameValuePair("user_id","25"));
				nameValuePair.add(new BasicNameValuePair("sec_user",
						sharedPreferences.getString("username", "")));
				nameValuePair.add(new BasicNameValuePair("sec_pass",
						sharedPreferences.getString("password", "")));
				nameValuePair.add(new BasicNameValuePair("office_id", office_id));

					// Defined URL where to send data
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"https://bscpro.com/office_api/addbpmoffice");
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

					responseText = jresponse.getString("CheckBPMoffice ");
					String status = jresponse.getString("status");

					if (status.equals("ok")) {
						
						SharedPreferences.Editor editor=sharedPreferences.edit();
						editor.putString("checkedin",responseText);
						editor.commit();
						btnShowLocation.setVisibility(View.VISIBLE);
						btnShowLocation.setText(responseText);	
						btnShowLocation.setClickable(false);
					}else if (responseText.equalsIgnoreCase("You already checked in this office location."))
					{
						SharedPreferences.Editor editor=sharedPreferences.edit();
						editor.putString("checkedin",responseText);
						editor.commit();
						btnShowLocation.setVisibility(View.VISIBLE);
						btnShowLocation.setText(responseText);	
						btnShowLocation.setClickable(false);
						
					}else {
						AppUtils.ShowAlertDialog(BPM_Ckeck_In_Activity.this,
								"Message :" + responseText);
					}
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(BPM_Ckeck_In_Activity.this,
							"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");

					e.printStackTrace();
					AirbrakeNotifier.notify(e);
				}
			}// end of onpost()
		}// ends of Async task

	
	
	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
					UserProfileActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.layoutprofile) {
//			MENU_ITEM_SELECTED = "PROFILE";
//			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
//					ProfileActivity.class);
//			startActivity(intent);
//		} 
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
					NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
					AddBusinessActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
					MatchupActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.Layoutlocator) {
//			MENU_ITEM_SELECTED = "OFFICE LOCATOR";
//			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
//					OfficeLocatorActivity.class);
//			startActivity(intent);
//		} 
		else if (view.getId() == R.id.Layoutlogout) {
			MENU_ITEM_SELECTED = "LOGOUT";
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("userid", "");
			editor.putString("password", "");
			editor.putString("username", "");
			editor.putString("profileImage", "");
			editor.commit();
			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
					LoginActivity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			isExpanded=false;
			new CollapseAnimation(slidingPanel, panelWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f,menulayout);

		}
		else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(BPM_Ckeck_In_Activity.this,
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

}// end of activity
