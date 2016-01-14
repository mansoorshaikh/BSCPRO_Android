package com.MWC.chatapp;
import java.util.ArrayList;
import java.util.Timer;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;
import applicationVo.LoginVo;
import applicationVo.UserProfileDataAsync;

import com.google.android.gcm.GCMRegistrar;
import com.loopj.android.airbrake.AirbrakeNotifier;
public class LoginActivity extends Activity {
	private DisplayMetrics metrics;
	private RelativeLayout slidingPanel, menulayout;
	//public static Activity myactivity;
	Button loginbutton, createaccount;
	EditText username, password;
	public String responseString = "";
	String deviceid = "";
	SharedPreferences sharedPreferences;
	ClearableEditText clearableEditText;
	
	VideoView myvid;
	private ImageView _imagView;
	private Timer _timer;
	private int _index;
	// private MyHandler handler;
	Bitmap bmp;
	BitmapDrawable ob;
	//public static String open="login";
	//************************************
		Controller aController;
		// Asyntask
		AsyncTask<Void, Void, Void> mRegisterTask;
		
		public static String GCMRegister_Id="";
		//************************************
	public static String loginimage;
	LoginVo lVo;
	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	private ProgressDialog mProgressDialog;
	public ChatSingleton chatinstance = null;
	public String countryresponcestring = "", SAorMDresponcestring = "",
			uplineCEOresponcestring = "", uplineSMDresponcestring = "";

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			AirbrakeNotifier.register(this, "0fff7944c788113f27c1e4202afdf030");
			ChatSingleton chatSingleton2 = ChatSingleton.getInstance();
			metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			chatSingleton2.Deviceheight = (int) ((metrics.heightPixels));
			chatSingleton2.devicewidth = (int) ((metrics.widthPixels));
		
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		deviceid = TelephonyMgr.getDeviceId();
				
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);
		
		try {
			myvid = (VideoView) findViewById(R.id.myvideoview);
			String path = "android.resource://" + getPackageName() + "/"
					+ R.raw.bscprobackground;
			myvid.setVideoURI(Uri.parse(path));
			//myvid.setMediaController(new MediaController(this));
			myvid.requestFocus();
		//	myvid.start();

//			myvid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//				public void onCompletion(MediaPlayer mp) {
//					mp.reset();
//					String path = "android.resource://" + getPackageName()
//							+ "/" + R.raw.bscprobackground;
//					myvid.setVideoURI(Uri.parse(path));
//					// myvid.setMediaController(new MediaController(this));
//					// myvid.requestFocus();
//					myvid.start();
//				}
//			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
		
		if(GCMRegister_Id.equalsIgnoreCase(""))
		{
			try {
				
			getGSMRegister_Id();
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		}
		
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		SharedPreferences.Editor editor = sharedPreferences
				.edit();
		editor.putString("NOTIFICATIONSTRING", "FALSE");
		editor.commit();
		
		if(sharedPreferences.getString("username", "").equalsIgnoreCase("")&&sharedPreferences.getString("password", "").equalsIgnoreCase(""))
		{
			
		}
		else
		{
			ChatSingleton chatSingleton = ChatSingleton.getInstance();
			UserProfileDataAsync profileDataAsync = UserProfileDataAsync.getInstance(LoginActivity.this);
			if(chatSingleton.countryArrayList.size()<1)
				profileDataAsync.LoadCountryData();
			if(chatSingleton.SAorMDArrayList.size()<1)
				profileDataAsync.LoadSAorMdData();
			if(chatSingleton.UplineCEOArrayList.size()<1)
				profileDataAsync.LoadUplineCEOData();
			if(chatSingleton.uplineSMDArrayList.size()<1)
				profileDataAsync.LoadUplineSMDData();
//			if(chatSingleton.userprofileArrayList.size()<1)
//				profileDataAsync.LoadUserProfiledata();
			
			chatSingleton.open=1;
			Intent logIntent = new Intent(
					getApplicationContext(),
					Dashboard_Activity.class);
			startActivity(logIntent);
			//finish();
		}
		chatinstance = ChatSingleton.getInstance();

		username = (EditText) findViewById(R.id.usereditext);
		password = (EditText) findViewById(R.id.passwordedittext);
		loginbutton = (Button) findViewById(R.id.logbutton);
		createaccount = (Button) findViewById(R.id.submit);
		
		 clearableEditText=new ClearableEditText(this);

			Typeface font = Typeface.createFromAsset(getAssets(),
					"fontawesome-webfont.ttf");
			Typeface font2 = Typeface.createFromAsset(getAssets(),
					"DistProTh.otf");
			username.setTypeface(font2);
			password.setTypeface(font2);
			loginbutton.setTypeface(font2);
			createaccount.setTypeface(font2);
			
			Button btn_clear = (Button) findViewById(R.id.clearable_button_clear);
			btn_clear.setTypeface(font);
			Button btn_clear2 = (Button) findViewById(R.id.clearable_button_clear2);
			btn_clear2.setTypeface(font);
		 
		loginbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (username.getText().toString().equals("")
						|| password.getText().toString().equals("")) {
					final AlertDialog alertDialog = new AlertDialog.Builder(
							LoginActivity.this).create();
					alertDialog.setTitle("BSCPRO");
					alertDialog.setMessage("Please fill Username & Password");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									alertDialog.dismiss();
								}
							});
					alertDialog.show();
				} else {

					if (AppUtils.isNetworkAvailable(LoginActivity.this))
					{
						if(GCMRegister_Id.equalsIgnoreCase(""))
						{
							try {
								
								AppUtils.ShowAlertDialog(LoginActivity.this,
										"Please wait your device is registring with GCM server.\nTry after some time.");
							getGSMRegister_Id();
							
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								AirbrakeNotifier.notify(e);
							}
						}
						else
						{
							try {
								new Login_User_Validate().execute();

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								AirbrakeNotifier.notify(e);
							}
						}

					}
					else
						AppUtils.ShowAlertDialog(LoginActivity.this,
								"No Internet Connection Available.");

				}

			}
		});
		createaccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent account = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(account);
			}
		});
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
	}// *****end of onCreate
	
	
	public void getGSMRegister_Id()
	{
		try{
		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
				aController = (Controller) getApplicationContext();
				
				
				// Check if Internet present
				if (!aController.isConnectingToInternet()) {
					
					// Internet Connection is not present
					aController.showAlertDialog(LoginActivity.this,
							"Internet Connection Error",
							"Please connect to Internet connection", false);
					// stop executing code by return
					return;
				}
				
//				// Getting name, email from intent
//				Intent i = getIntent();
//				
//				name = i.getStringExtra("name");
//				email = i.getStringExtra("email");		
				
				// Make sure the device has the proper dependencies.
				GCMRegistrar.checkDevice(this);

				// Make sure the manifest permissions was properly set 
				GCMRegistrar.checkManifest(this);

				//lblMessage = (TextView) findViewById(R.id.lblMessage);
				
				// Register custom Broadcast receiver to show messages on activity
				
				registerReceiver(mHandleMessageReceiver, new IntentFilter(
						GCMConfig.DISPLAY_MESSAGE_ACTION));
				
				// Get GCM registration id
				final String regId = GCMRegistrar.getRegistrationId(this);

				GCMRegister_Id=regId;
				// Check if regid already presents
				if (regId.equals("")) {
					
					// Register with GCM			
					GCMRegistrar.register(this, GCMConfig.GOOGLE_SENDER_ID);
					
				} else {
					
					// Device is already registered on GCM Server
					if (GCMRegistrar.isRegisteredOnServer(this)) {
						
						// Skips registration.				
						//Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();
					
					} else {
						
						// Try to register again, but not in the UI thread.
						// It's also necessary to cancel the thread onDestroy(),
						// hence the use of AsyncTask instead of a raw thread.
						
						final Context context = this;
						mRegisterTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								
								// Register on our server
								// On server creates a new user
								aController.register(context, "", "", regId);
								
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								mRegisterTask = null;
							}
						};
						
						// execute AsyncTask
						mRegisterTask.execute(null, null, null);
					}
				}//end of else
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}//end of GSM register id ******************************
	

	// Create a broadcast receiver to get message and show on screen 
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			try{
			String newMessage = intent.getExtras().getString(GCMConfig.EXTRA_MESSAGE);
			
			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());
			
			// Display message on the screen
			//lblMessage.append(newMessage + "\n");			
			
			//Toast.makeText(getApplicationContext(), "Got Message: " + newMessage, Toast.LENGTH_LONG).show();
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
//			if(sharedPreferences.getString("username", "").equalsIgnoreCase("")&&sharedPreferences.getString("password", "").equalsIgnoreCase(""))
//			{
//				
//			}
//			else
//			{
//				ChatSingleton chatSingleton = ChatSingleton.getInstance();
//				UserProfileDataAsync profileDataAsync = UserProfileDataAsync.getInstance(LoginActivity.this);
//				if(chatSingleton.countryArrayList.size()<1)
//					profileDataAsync.LoadCountryData();
//				if(chatSingleton.SAorMDArrayList.size()<1)
//					profileDataAsync.LoadSAorMdData();
//				if(chatSingleton.UplineCEOArrayList.size()<1)
//					profileDataAsync.LoadUplineCEOData();
//				if(chatSingleton.uplineSMDArrayList.size()<1)
//					profileDataAsync.LoadUplineSMDData();
////				if(chatSingleton.userprofileArrayList.size()<1)
////					profileDataAsync.LoadUserProfiledata();
//				//chatSingleton.open=1;
//				Intent logIntent = new Intent(
//						getApplicationContext(),
//						UserProfileActivity.class);
//				startActivity(logIntent);
//				return;
//			}
			
			// Releasing wake lock
			//aController.releaseWakeLock();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);
			
			//Clear internal resources.
			GCMRegistrar.onDestroy(this);
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	// ***************************Async task Class
	class Login_User_Validate extends AsyncTask<String, Void, String> {
		@SuppressLint("NewApi")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);

		}

		protected String doInBackground(String... aurl) {

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("username", username
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("password", password
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("device_id", GCMRegister_Id));
			nameValuePair.add(new BasicNameValuePair("device_type","Android"));
			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/auth_api/login");
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

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String str_resp) {
			super.onPostExecute(str_resp);
			JSONObject jresponse = null;
			try {
				jresponse = new JSONObject(responseString);

				if (jresponse != null) {
					responseString = jresponse.getString("message");

					if (responseString.equals("auth_incorrect_password")
							|| responseString.equals("auth_incorrect_login")
							|| responseString.equals("All field are required.")) {
						if (mProgressDialog != null)
							mProgressDialog.dismiss();
						final AlertDialog alertDialog = new AlertDialog.Builder(
								LoginActivity.this).create();
						alertDialog.setTitle("BSCPRO");
						alertDialog
								.setMessage("Your Username & Password is incorrect, please check again !!!");
						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										alertDialog.dismiss();
									}
								});
						alertDialog.show();
					} else {

						// Toast.makeText(getApplicationContext(),
						// "Data" + responseString, Toast.LENGTH_LONG).show();

						try {

							responseString = jresponse
									.getString("user_profile");
							JSONArray activityArray = null;
							try {
								activityArray = jresponse
										.getJSONArray("user_profile");
							} catch (Exception e) {
								// TODO: handle exception
								activityArray = new JSONArray();
								activityArray.put(jresponse
										.getJSONObject("user_profile"));
							}
							for (int i = 0; i < activityArray.length(); i++) {

								JSONObject activityObject = (JSONObject) activityArray
										.get(i);
								lVo = new LoginVo();
								if (!activityObject.isNull("profileImage"))
									lVo.profileImage = activityObject
											.getString("profileImage");
								if (!activityObject.isNull("userid"))
									lVo.userid = activityObject
											.getString("userid");
								if (!activityObject.isNull("username"))
									lVo.username = activityObject
											.getString("username");
								if (!activityObject.isNull("firstname"))
									lVo.fname = activityObject
											.getString("firstname");
								if (!activityObject.isNull("lastname"))
									lVo.lastname = activityObject
											.getString("lastname");
								if (!activityObject.isNull("usertimezone"))
									lVo.usertimezone = activityObject
											.getString("usertimezone");
							}
								SharedPreferences.Editor editor = sharedPreferences
									.edit();
							editor.putString("userid", lVo.userid);
							editor.putString("password", password.getText()
									.toString());
							editor.putString("username", lVo.username);
							editor.putString("profileImage", lVo.profileImage);
							editor.putString("firstname", lVo.fname);
							editor.putString("lastname", lVo.lastname);
							editor.putString("usertimezone", lVo.usertimezone);
							editor.commit();
							
							sharedPreferences.getString("usertimezone", "UTC");

							ChatSingleton chatSingleton = ChatSingleton.getInstance();
							UserProfileDataAsync profileDataAsync = UserProfileDataAsync.getInstance(LoginActivity.this);
							
							if(chatSingleton.countryArrayList.size()<1)
								profileDataAsync.LoadCountryData();
							if(chatSingleton.SAorMDArrayList.size()<1)
								profileDataAsync.LoadSAorMdData();
							if(chatSingleton.UplineCEOArrayList.size()<1)
								profileDataAsync.LoadUplineCEOData();
							if(chatSingleton.uplineSMDArrayList.size()<1)
								profileDataAsync.LoadUplineSMDData();
							if(chatSingleton.userprofileArrayList.size()<1)
								profileDataAsync.LoadUserProfiledata();

							if (mProgressDialog != null)
								mProgressDialog.dismiss();
							chatSingleton.open=1;
							Intent logIntent = new Intent(
									getApplicationContext(),
									Dashboard_Activity.class);
							startActivity(logIntent);

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							AirbrakeNotifier.notify(e);
							if (mProgressDialog != null)
								mProgressDialog.dismiss();
						}
					}// end of else
				}// end of if
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(LoginActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

		}// end of postExecute
	}

	// ***************************End Async task Class

	
}// *****end of loginActivity
