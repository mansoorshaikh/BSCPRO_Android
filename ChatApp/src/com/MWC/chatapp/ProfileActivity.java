package com.MWC.chatapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.MWC.chatapp.AddBusinessActivity.WheelAdapter;
import com.MWC.chatapp.MatchupActivity.myTask_saveUserDetails_call;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AgentListVO;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;
import applicationVo.PhoneNumberFilter;
import applicationVo.PhoneNumberTextWatcher;
import applicationVo.UserProfileDataAsync;
import applicationVo.UserProfileListVO;

public class ProfileActivity extends Activity {

	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
			LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout, LayoutBPM_Check_in,menulayout;
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
	private boolean isExpanded, isProfileimageChange = false,
			iscurrentpasschange = false;

	public ChatSingleton chatSingleton = null;
	public UserProfileDataAsync profileDataAsync = null;

	public EditText UserNameEdittext, FirstNameEdittext, LastnameEdittext,
			LevelEdittext, AgentidEdittext, LicensesEdittext, languageEdittext,
			AddressEdittext, CityEdittext, StateEdittext, ZipEdittext,
			EmailEdittext, CountryEdittext, PhoneEdittext,
			SelectTimeZoneEdittext, UplineSAorMDEdittext,
			UplineCEO_EVC_Edittext, UplineSMDEdittext, CurrentPassEdittext,
			NewPassEdittext, NewPass_RepeatEdittext;
	public String countryresponcestring = "", SAorMDresponcestring = "",
			uplineCEOresponcestring = "", uplineSMDresponcestring = "",
			UserProfileresponsestring;
	public NormalSmartImageView profimage = null;

	public ArrayList<UserProfileListVO> userprofileArrayList = new ArrayList<UserProfileListVO>();

	public String ChangedProfileimageUrl = "";
	String[] LevelStringArray = new String[] { "TA", "A", "SA", "MD", "SMD",
			"EMD", "CEO", "EVC", "ADM" };
	String[] TimeZoneStringArray = new String[] { "Eastern", "Central",
			"Mountain", "Pacific", "Alaska", "Hawaii-Aleutian" };

	boolean[] enablearray = new boolean[4];
	SharedPreferences sharedPreferences;

	// **********************************************
	private static int RESULT_LOAD_IMG = 2;
	String imgDecodableString = "";
	Bitmap image;
	Bitmap photo = null;

	private String mPath = "";
	private Uri mImageCaptureUri;
	private static final int PICK_FROM_CAMERA = 1;

	// **********************************************

	WheelView WheelEvents;
	SlidingDrawer SDEvents;
	Button WheelDone, WheelCancle;
	int WheelPosition;
	boolean scrolling = false;
	public String ClickedEdittext = "", responseText = "";

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

				if (ClickedEdittext.equalsIgnoreCase("country")) {
					if (chatSingleton.countryArrayList != null
							&& chatSingleton.countryArrayList.size() > 0) {
						if (WheelPosition < chatSingleton.countryArrayList
								.size()) {
							String Country = chatSingleton.countryArrayList
									.get(WheelPosition);
							CountryEdittext.setText(Country);
						}
						CountryEdittext.setFocusable(false);

					}
				} else if (ClickedEdittext.equalsIgnoreCase("SAorMD")) {
					if (chatSingleton.SAorMDArrayList != null
							&& chatSingleton.SAorMDArrayList.size() > 0) {
						if (WheelPosition < chatSingleton.SAorMDArrayList
								.size()) {
							AgentListVO agentVo = chatSingleton.SAorMDArrayList
									.get(WheelPosition);
							UplineSAorMDEdittext.setText(agentVo.username
									+ " ( " + agentVo.firstname + " "
									+ agentVo.lastname + " )");
						}
						UplineSAorMDEdittext.setFocusable(false);

					}
				} else if (ClickedEdittext.equalsIgnoreCase("CEO")) {
					if (chatSingleton.UplineCEOArrayList != null
							&& chatSingleton.UplineCEOArrayList.size() > 0) {
						if (WheelPosition < chatSingleton.UplineCEOArrayList
								.size()) {
							AgentListVO agentVo = chatSingleton.UplineCEOArrayList
									.get(WheelPosition);
							UplineCEO_EVC_Edittext.setText(agentVo.username
									+ " ( " + agentVo.firstname + " "
									+ agentVo.lastname + " )");
						}
						UplineCEO_EVC_Edittext.setFocusable(false);

					}
				} else if (ClickedEdittext.equalsIgnoreCase("SMD")) {
					if (chatSingleton.uplineSMDArrayList != null
							&& chatSingleton.uplineSMDArrayList.size() > 0) {
						if (WheelPosition < chatSingleton.uplineSMDArrayList
								.size()) {
							AgentListVO agentVo = chatSingleton.uplineSMDArrayList
									.get(WheelPosition);
							UplineSMDEdittext.setText(agentVo.username + " ( "
									+ agentVo.firstname + " "
									+ agentVo.lastname + " )");
						}
						UplineSMDEdittext.setFocusable(false);

					}
				} else if (ClickedEdittext.equalsIgnoreCase("level")) {
					if (LevelStringArray != null && LevelStringArray.length > 0) {
						if (WheelPosition < LevelStringArray.length) {
							String level = LevelStringArray[WheelPosition];
							LevelEdittext.setText(level);

							if (level.equalsIgnoreCase("SMD")
									|| level.equalsIgnoreCase("EMD")) {
								UplineSMDEdittext.setText("I an an SMD");
								edittextDisable(UplineSMDEdittext);
								enablearray[2] = true;

							} else if (level.equalsIgnoreCase("CEO")
									|| level.equalsIgnoreCase("EVC")) {
								UplineSMDEdittext.setText("I an an SMD");
								edittextDisable(UplineSMDEdittext);

								UplineCEO_EVC_Edittext
										.setText("My CEO or EVC is not listed");
								edittextDisable(UplineCEO_EVC_Edittext);

								UplineSAorMDEdittext
										.setText("I an an SA and MD");
								edittextDisable(UplineSAorMDEdittext);

								enablearray[0] = true;
								enablearray[1] = true;
								enablearray[2] = true;
							} else {
								if (enablearray[0]) {
									UplineCEO_EVC_Edittext
											.setHint("Upline CEO/EVC");
									UplineCEO_EVC_Edittext.setText("");
									edittextEnable(UplineCEO_EVC_Edittext);
								}
								if (enablearray[2]) {
									UplineSMDEdittext
											.setHint("Select your SMD");
									UplineSMDEdittext.setText("");
									edittextEnable(UplineSMDEdittext);
								}

								if (enablearray[1]) {
									UplineSAorMDEdittext
											.setHint("Select upline SA or MD");
									UplineSAorMDEdittext.setText("");
									edittextEnable(UplineSAorMDEdittext);
								}
							}

						}
						LevelEdittext.setFocusable(false);

					}
				} else if (ClickedEdittext.equalsIgnoreCase("timezone")) {
					if (TimeZoneStringArray != null
							&& TimeZoneStringArray.length > 0) {
						if (WheelPosition < TimeZoneStringArray.length) {
							SelectTimeZoneEdittext
									.setText(TimeZoneStringArray[WheelPosition]);
						}
						SelectTimeZoneEdittext.setFocusable(false);

					}
				}// end of else

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
		setContentView(R.layout.profile);

		try {

			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			chatSingleton = ChatSingleton.getInstance();
			profileDataAsync = UserProfileDataAsync
					.getInstance(ProfileActivity.this);

			if (chatSingleton.countryArrayList.size() < 1)
				profileDataAsync.LoadCountryData();
			if (chatSingleton.SAorMDArrayList.size() < 1)
				profileDataAsync.LoadSAorMdData();
			if (chatSingleton.UplineCEOArrayList.size() < 1)
				profileDataAsync.LoadUplineCEOData();
			if (chatSingleton.uplineSMDArrayList.size() < 1)
				profileDataAsync.LoadUplineSMDData();
			// if(chatSingleton.userprofileArrayList.size()<1)
			// profileDataAsync.LoadUserProfiledata();

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
			NormalSmartImageView profileimageview = (NormalSmartImageView) findViewById(R.id.userimage);

			try {
				profileimageview.setImageUrl(sharedPreferences.getString(
						"profileImage", ""));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			 TextView username=(TextView)findViewById(R.id.Chatusername);
				username.setText(sharedPreferences.getString("username", ""));
			
			FirstNameEdittext = (EditText) findViewById(R.id.profnameedt);
			LastnameEdittext = (EditText) findViewById(R.id.proflastnameedt);
			LevelEdittext = (EditText) findViewById(R.id.profleveledt);
			AgentidEdittext = (EditText) findViewById(R.id.profagentcodeedt);
			LicensesEdittext = (EditText) findViewById(R.id.proflicensesedt);
			languageEdittext = (EditText) findViewById(R.id.proflanguageedt);
			AddressEdittext = (EditText) findViewById(R.id.profaddressedt);

			CityEdittext = (EditText) findViewById(R.id.profcityedt);
			StateEdittext = (EditText) findViewById(R.id.profstateedt);
			ZipEdittext = (EditText) findViewById(R.id.profzipcodeedt);
			EmailEdittext = (EditText) findViewById(R.id.profemailedt);
			CountryEdittext = (EditText) findViewById(R.id.profcountryedt);
			PhoneEdittext = (EditText) findViewById(R.id.profphoneedt);

			SelectTimeZoneEdittext = (EditText) findViewById(R.id.proftimezoneedt);
			UplineSAorMDEdittext = (EditText) findViewById(R.id.profuplinesaedt);
			UplineCEO_EVC_Edittext = (EditText) findViewById(R.id.profuplineceoedt);
			UplineSMDEdittext = (EditText) findViewById(R.id.profuplinesmdedt);
			UserNameEdittext = (EditText) findViewById(R.id.profusernameedt);
			CurrentPassEdittext = (EditText) findViewById(R.id.profcurrentpasswordedt);
			NewPassEdittext = (EditText) findViewById(R.id.profnewpasswordedt);
			NewPass_RepeatEdittext = (EditText) findViewById(R.id.profnewpasswordrepeatedt);
			profimage = (NormalSmartImageView) findViewById(R.id.profuserphoto);

			for (int k = 0; k < 4; k++) {
				enablearray[k] = false;
			}

			if (AppUtils.isNetworkAvailable(ProfileActivity.this)) {
				new getUserProfileData().execute();
			}

			profimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					selectImageFromGallery();
				}
			});

			try {
				PhoneEdittext
						.addTextChangedListener(new PhoneNumberTextWatcher());
				PhoneEdittext.setFilters(new InputFilter[] {
						new PhoneNumberFilter(),
						new InputFilter.LengthFilter(12) });

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			addValueintoEventList();// *****************Call to will fill data

			CountryEdittext.setFocusable(false);
			CountryEdittext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (chatSingleton.countryArrayList != null
							&& chatSingleton.countryArrayList.size() > 0) {
						ClickedEdittext = "country";
						WheelEvents.setViewAdapter(new WheelAdapter(
								ProfileActivity.this));

						if (!SDEvents.isOpened()) {
							SDEvents.animateOpen();
							SDEvents.setVisibility(View.VISIBLE);
						}
					} else
						AppUtils.ShowAlertDialog(ProfileActivity.this,
								"Please Reload the contents");
				}
			});

			SelectTimeZoneEdittext.setFocusable(false);
			SelectTimeZoneEdittext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					ClickedEdittext = "timezone";
					WheelEvents.setViewAdapter(new WheelAdapter(
							ProfileActivity.this));

					if (!SDEvents.isOpened()) {
						SDEvents.animateOpen();
						SDEvents.setVisibility(View.VISIBLE);
					}

					else
						AppUtils.ShowAlertDialog(ProfileActivity.this,
								"Please Reload the contents");
				}
			});

			UplineSAorMDEdittext.setFocusable(false);
			UplineSAorMDEdittext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (chatSingleton.SAorMDArrayList != null
							&& chatSingleton.SAorMDArrayList.size() > 0) {
						if (LevelEdittext.getText().toString()
								.equalsIgnoreCase("CEO")
								|| LevelEdittext.getText().toString()
										.equalsIgnoreCase("EVC")) {
						} else {
							ClickedEdittext = "SAorMD";
							WheelEvents.setViewAdapter(new WheelAdapter(
									ProfileActivity.this));

							if (!SDEvents.isOpened()) {
								SDEvents.animateOpen();
								SDEvents.setVisibility(View.VISIBLE);
							}
						}

					} else
						AppUtils.ShowAlertDialog(ProfileActivity.this,
								"Please Reload the contents");
				}
			});

			UplineCEO_EVC_Edittext.setFocusable(false);
			UplineCEO_EVC_Edittext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (chatSingleton.UplineCEOArrayList != null
							&& chatSingleton.UplineCEOArrayList.size() > 0) {
						if (LevelEdittext.getText().toString()
								.equalsIgnoreCase("CEO")
								|| LevelEdittext.getText().toString()
										.equalsIgnoreCase("EVC")) {
						} else {

							ClickedEdittext = "CEO";
							WheelEvents.setViewAdapter(new WheelAdapter(
									ProfileActivity.this));

							if (!SDEvents.isOpened()) {
								SDEvents.animateOpen();
								SDEvents.setVisibility(View.VISIBLE);
							}
						}
					} else
						AppUtils.ShowAlertDialog(ProfileActivity.this,
								"Please Reload the contents");
				}
			});

			UplineSMDEdittext.setFocusable(false);
			UplineSMDEdittext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (chatSingleton.uplineSMDArrayList != null
							&& chatSingleton.uplineSMDArrayList.size() > 0) {

						if (LevelEdittext.getText().toString()
								.equalsIgnoreCase("CEO")
								|| LevelEdittext.getText().toString()
										.equalsIgnoreCase("EVC")
								|| LevelEdittext.getText().toString()
										.equalsIgnoreCase("SMD")
								|| LevelEdittext.getText().toString()
										.equalsIgnoreCase("EMD")) {
						} else {
							ClickedEdittext = "SMD";
							WheelEvents.setViewAdapter(new WheelAdapter(
									ProfileActivity.this));

							if (!SDEvents.isOpened()) {
								SDEvents.animateOpen();
								SDEvents.setVisibility(View.VISIBLE);
							}
						}
					} else
						AppUtils.ShowAlertDialog(ProfileActivity.this,
								"Please Reload the contents");
				}
			});

			LevelEdittext.setFocusable(false);
			LevelEdittext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (LevelStringArray != null && LevelStringArray.length > 0) {
						ClickedEdittext = "level";
						WheelEvents.setViewAdapter(new WheelAdapter(
								ProfileActivity.this));

						if (!SDEvents.isOpened()) {
							SDEvents.animateOpen();
							SDEvents.setVisibility(View.VISIBLE);
						}
					} else
						AppUtils.ShowAlertDialog(ProfileActivity.this,
								"Please Reload the contents");
				}
			});

			Button submitbtn = (Button) findViewById(R.id.updateprofilebtn);
			submitbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (AppUtils.isNetworkAvailable(ProfileActivity.this)) {
						if (emptyCheck(FirstNameEdittext)
								&& emptyCheck(LastnameEdittext)
								&& emptyCheck(LevelEdittext)
								&& emptyCheck(AgentidEdittext)
								&& emptyCheck(LicensesEdittext)
								&& emptyCheck(languageEdittext)
								&& emptyCheck(AddressEdittext)
								&& emptyCheck(CityEdittext)
								&& emptyCheck(StateEdittext)
								&& emptyCheck(ZipEdittext)
								&& emptyCheck(EmailEdittext)
								&& emptyCheck(CountryEdittext)
								&& emptyCheck(PhoneEdittext)
								&& emptyCheck(SelectTimeZoneEdittext)
								&& emptyCheck(UplineSAorMDEdittext)
								&& emptyCheck(UplineCEO_EVC_Edittext)
								&& emptyCheck(UplineSMDEdittext)
								&& emptyCheck(UserNameEdittext)) {

							if (emptyCheck(NewPassEdittext)) {
								iscurrentpasschange = true;
							}

							if (!NewPassEdittext
									.getText()
									.toString()
									.equals(NewPass_RepeatEdittext.getText()
											.toString())
									&& iscurrentpasschange)
								AppUtils.ShowAlertDialog(ProfileActivity.this,
										"Password Does Not Match.");
							else {
								if (isValidMobile(PhoneEdittext.getText()
										.toString()))

									if (CurrentPassEdittext
											.getText()
											.toString()
											.equalsIgnoreCase(
													sharedPreferences
															.getString(
																	"password",
																	""))) {
										if (photo == null)
											new myTask_saveUserDetails_call()
													.execute();
										else
											new Upload_image_Async().execute();
									} else
										AppUtils.ShowAlertDialog(
												ProfileActivity.this,
												"Password Does Not Match.");

								else
									AppUtils.ShowAlertDialog(
											ProfileActivity.this,
											"Contact must be 10 digits length");

							}

						}// end of if
						else
							AppUtils.ShowAlertDialog(ProfileActivity.this,
									"All field are mandatory!!!");

					}// end of if for internet check
					else
						AppUtils.ShowAlertDialog(ProfileActivity.this,
								"No internet connection");

				}
			});

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}// end of oncreate()

	// ******************************************picking up the image
	public void selectImageFromGallery() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ProfileActivity.this);

		new AlertDialog.Builder(ProfileActivity.this)
				.setMessage("Choose option")
				.setCancelable(false)
				.setPositiveButton("Gallery",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Create intent to Open Image applications like
								// Gallery, Google Photos
								Intent galleryIntent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								// Start the Intent
								startActivityForResult(galleryIntent,
										RESULT_LOAD_IMG);

								dialog.dismiss();

							}
						})
				// end of positive attribute
				.setNegativeButton("Camera",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {

									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									File file = new File(
											Environment
													.getExternalStorageDirectory(),
											"aaa_"
													+ String.valueOf(System
															.currentTimeMillis())
													+ ".jpg");
									mImageCaptureUri = Uri.fromFile(file);

									intent.putExtra(
											android.provider.MediaStore.EXTRA_OUTPUT,
											mImageCaptureUri);
									intent.putExtra("return-data", true);

									startActivityForResult(intent,
											PICK_FROM_CAMERA);
									dialog.dismiss();
								} catch (Exception e) {
									// TODO: handle exception
								}

							}
						})
				.setNeutralButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Perform Your Task Here--When No is pressed
								dialog.dismiss();
							}
						}).show();

		// ****************************************************
	} // end of select image from gallery

	// ***************************image code and URI

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ImageView imageview = (NormalSmartImageView) findViewById(R.id.profuserphoto);
		if (resultCode != RESULT_OK)
			return;
		else {

			if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
				mPath = mImageCaptureUri.getPath();
				photo = BitmapFactory.decodeFile(mPath);
			}// end of if
			else {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				photo = BitmapFactory.decodeFile(picturePath);

			}// end of else

			photo = Bitmap.createScaledBitmap(photo, 60, 60, true);

			image = Bitmap.createScaledBitmap(photo, imageview.getWidth(),
					imageview.getHeight(), true);
			imageview.setImageBitmap(image);

		}// end of else outer

	} // end of function

	// *******************************************

	// JSON AsyncTask for image upload
	class Upload_image_Async extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				userpostImage();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void args) {

			try {
				if (responseText != null || responseText != "") {
					isProfileimageChange = true;
					ChangedProfileimageUrl = responseText;
				} else {
					isProfileimageChange = false;
				}

				if (mProgressDialog != null)
					mProgressDialog.dismiss();

				new myTask_saveUserDetails_call().execute();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}// end of onpost()
	}// ends of Async task

	// this will post our text data
	@SuppressLint("NewApi")
	private void userpostImage() {
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Bitmap bi = photo;
			bi.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] data = baos.toByteArray();
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("image", Base64
					.encodeToString(data, 0)));

			// Defined URL where to send data
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://millionairesorg.com/chatapp/android_fileupload.php");
			httppost.setHeader("Content-Type",
					"application/x-www-form-urlencoded;");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
			HttpResponse response = httpclient.execute(httppost);
			responseText = EntityUtils.toString(response.getEntity());
			// System.out.println(responseText + "response is display");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

			String spoustext = "", btnarray = "";

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("user_id",
					sharedPreferences.getString("userid", "")));
			nameValuePair.add(new BasicNameValuePair("sec_user",
					sharedPreferences.getString("username", "")));
			nameValuePair.add(new BasicNameValuePair("sec_pass",
					sharedPreferences.getString("password", "")));

			nameValuePair.add(new BasicNameValuePair("fname", FirstNameEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("lname", LastnameEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("level", LevelEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("agent_id",
					AgentidEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("city", CityEdittext
					.getText().toString()));

			nameValuePair.add(new BasicNameValuePair("state", StateEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("address", AddressEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("zip", ZipEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("cellphone", PhoneEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("email", EmailEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("selet_timezone",
					SelectTimeZoneEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("upline",
					UplineCEO_EVC_Edittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("upline_smd",
					UplineSMDEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("password",
					iscurrentpasschange ? CurrentPassEdittext.getText()
							.toString() : ""));
			nameValuePair.add(new BasicNameValuePair("new_password",
					iscurrentpasschange ? NewPassEdittext.getText().toString()
							: ""));
			nameValuePair.add(new BasicNameValuePair("repeat_password",
					iscurrentpasschange ? NewPass_RepeatEdittext.getText()
							.toString() : ""));
			nameValuePair.add(new BasicNameValuePair("country", CountryEdittext
					.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("profile_image",
					isProfileimageChange ? ChangedProfileimageUrl : ""));
			nameValuePair.add(new BasicNameValuePair("language",
					languageEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("licenses",
					LicensesEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("all_SA_MD_user",
					UplineSAorMDEdittext.getText().toString()));

			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://bscpro.com/profile_api/");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseText = EntityUtils.toString(response.getEntity());
				// System.out.println(responseText + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
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

				if (responseText
						.equalsIgnoreCase("Profile update successfully")
						&& status.equals("ok")) {

					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(ProfileActivity.this,
							"Profile updated successfully");

					// chatSingleton.userprofileArrayList.clear();
					// //profileDataAsync.LoadAllUserprofiledata();
					// chatSingleton.userprofileArrayList.size();
					// new getUserProfileData().execute();

					if (isProfileimageChange) {
						SharedPreferences.Editor editor = sharedPreferences
								.edit();

						editor.putString("profileImage", ChangedProfileimageUrl);
						editor.commit();
					}

					if (iscurrentpasschange) {
						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.putString("password", NewPassEdittext.getText()
								.toString());
						editor.commit();
					}

				} else {
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(ProfileActivity.this, "Error :"
							+ responseText);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(ProfileActivity.this,
						"App is Under Maintenance please wait some time.");

				e.printStackTrace();
			}
		}// end of onpost()
	}// ends of Async task

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(ProfileActivity.this,
					UserProfileActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.layoutprofile) {
//			MENU_ITEM_SELECTED = "PROFILE";
//			new CollapseAnimation(slidingPanel, panelWidth,
//					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
//					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
//		} 
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			Intent intent = new Intent(ProfileActivity.this,
					NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(ProfileActivity.this,
					AddBusinessActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			Intent intent = new Intent(ProfileActivity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(ProfileActivity.this,
					MatchupActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.Layoutlocator) {
//			MENU_ITEM_SELECTED = "OFFICE LOCATOR";
//			Intent intent = new Intent(ProfileActivity.this,
//					OfficeLocatorActivity.class);
//			startActivity(intent);
//		}
		else if (view.getId() == R.id.Layoutlogout) {
			MENU_ITEM_SELECTED = "LOGOUT";SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences
					.edit();
			editor.putString("userid", "");
			editor.putString("password", "");
			editor.putString("username", "");
			editor.putString("profileImage","");
			editor.commit();
			Intent intent = new Intent(ProfileActivity.this,
					LoginActivity.class);
			startActivity(intent);

		}else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(ProfileActivity.this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		}
	}// end of menu

	public void edittextEnable(EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true); // user touches widget on phone
												// with touch screen
		editText.setClickable(true); // user navigates with wheel and selects
										// widget

	}

	public void edittextDisable(EditText editText) {
		editText.setFocusable(false);
		editText.setFocusableInTouchMode(false); // user touches widget on phone
													// with touch screen
		editText.setClickable(false); // user navigates with wheel and selects
										// widget
		// editText.setText("");
	}

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

			if (ClickedEdittext.equalsIgnoreCase("country")) {
				return chatSingleton.countryArrayList.size();
			} else if (ClickedEdittext.equals("SAorMD")) {
				return chatSingleton.SAorMDArrayList.size();
			} else if (ClickedEdittext.equalsIgnoreCase("CEO")) {
				return chatSingleton.UplineCEOArrayList.size();
			} else if (ClickedEdittext.equals("SMD")) {
				return chatSingleton.uplineSMDArrayList.size();
			} else if (ClickedEdittext.equals("level")) {
				return LevelStringArray.length;
			} else if (ClickedEdittext.equals("timezone")) {
				return TimeZoneStringArray.length;
			}
			return WheelPosition;

		}

		@Override
		protected CharSequence getItemText(int index) {

			if (ClickedEdittext.equalsIgnoreCase("country")) {
				if (chatSingleton.countryArrayList != null
						&& chatSingleton.countryArrayList.size() > 0) {
					String Country = chatSingleton.countryArrayList.get(index);
					return Country;

				}
			} else if (ClickedEdittext.equalsIgnoreCase("SAorMD")) {
				if (chatSingleton.SAorMDArrayList != null
						&& chatSingleton.SAorMDArrayList.size() > 0) {
					AgentListVO agentVo = chatSingleton.SAorMDArrayList
							.get(index);
					return (agentVo.username + " ( " + agentVo.firstname + " "
							+ agentVo.lastname + " )");

				}
			} else if (ClickedEdittext.equalsIgnoreCase("CEO")) {
				if (chatSingleton.UplineCEOArrayList != null
						&& chatSingleton.UplineCEOArrayList.size() > 0) {
					AgentListVO agentVo = chatSingleton.UplineCEOArrayList
							.get(index);
					return (agentVo.username + " ( " + agentVo.firstname + " "
							+ agentVo.lastname + " )");

				}
			} else if (ClickedEdittext.equalsIgnoreCase("SMD")) {
				if (chatSingleton.uplineSMDArrayList != null
						&& chatSingleton.uplineSMDArrayList.size() > 0) {
					AgentListVO agentVo = chatSingleton.uplineSMDArrayList
							.get(index);
					return (agentVo.username + " ( " + agentVo.firstname + " "
							+ agentVo.lastname + " )");

				}
			}// end of else
			else if (ClickedEdittext.equalsIgnoreCase("level")) {
				return LevelStringArray[index];
			}// end of else
			else if (ClickedEdittext.equalsIgnoreCase("timezone")) {
				return TimeZoneStringArray[index];
			}// end of else

			return "";
		}
	}// end of wheel adapter

	// ******************************Async task claass---GetUserprofileList data
	public class getUserProfileData extends AsyncTask<String, String, String> {

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
						"http://bscpro.com/profile_api/getUserProfile/");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				UserProfileresponsestring = EntityUtils.toString(response
						.getEntity());

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		} // end of doInBackground();

		@Override
		protected void onPostExecute(String lenghtOfFile) {
			String status = null;
			String responseText = null;
			JSONObject jresponse = null;
			try {

				jresponse = new JSONObject(UserProfileresponsestring);

				responseText = jresponse.getString("message");
				status = jresponse.getString("status");
				JSONArray activityArray = null;
				if (responseText.equalsIgnoreCase("success")
						&& status.equals("ok")) {

					activityArray = jresponse.getJSONArray("userdetail");
					userprofileArrayList.clear();

					for (int i = 0; i < activityArray.length(); i++) {

						JSONObject activityObject = (JSONObject) activityArray
								.get(i);
						UserProfileListVO cVo = new UserProfileListVO();

						// if (!activityObject.isNull("id"))
						// cVo.id = activityObject.getString("id");

						if (!activityObject.isNull("username"))
							cVo.username = activityObject.getString("username");

						if (!activityObject.isNull("fname"))
							cVo.firstname = activityObject.getString("fname");
						if (!activityObject.isNull("lname"))
							cVo.lastname = activityObject.getString("lname");

						if (!activityObject.isNull("agent_id"))
							cVo.agentid = activityObject.getString("agent_id");
						if (!activityObject.isNull("level"))
							cVo.level = activityObject.getString("level");

						if (!activityObject.isNull("city"))
							cVo.city = activityObject.getString("city");
						if (!activityObject.isNull("state"))
							cVo.state = activityObject.getString("state");

						if (!activityObject.isNull("address"))
							cVo.address = activityObject.getString("address");

						if (!activityObject.isNull("zip"))
							cVo.zip = activityObject.getString("zip");

						if (!activityObject.isNull("cellphone"))
							cVo.phone = activityObject.getString("cellphone");
						if (!activityObject.isNull("email"))
							cVo.email = activityObject.getString("email");

						if (!activityObject.isNull("selet_timezone"))
							cVo.timezone = activityObject
									.getString("selet_timezone");
						if (!activityObject.isNull("upline"))
							cVo.ceo_evc = activityObject.getString("upline");

						if (!activityObject.isNull("upline_smd"))
							cVo.smd_emd = activityObject
									.getString("upline_smd");
						if (!activityObject.isNull("country"))
							cVo.country = activityObject.getString("country");

						if (!activityObject.isNull("profile_image"))
							cVo.profileimageUrl = activityObject
									.getString("profile_image");

						if (!activityObject.isNull("language"))
							cVo.language = activityObject.getString("language");
						if (!activityObject.isNull("licenses"))
							cVo.licenses = activityObject.getString("licenses");

						if (!activityObject.isNull("all_SA_MD_user"))
							cVo.uplineSA_MD = activityObject
									.getString("all_SA_MD_user");

						userprofileArrayList.add(cVo);

					}// end of for

					if (userprofileArrayList.size() > 0) {
						UserProfileListVO listVO = userprofileArrayList.get(0);

						FirstNameEdittext.setText(listVO.firstname);
						LastnameEdittext.setText(listVO.lastname);
						LevelEdittext.setText(listVO.level);
						AgentidEdittext.setText(listVO.agentid);
						LicensesEdittext.setText(listVO.licenses);
						languageEdittext.setText(listVO.language);
						AddressEdittext.setText(listVO.address);
						CityEdittext.setText(listVO.city);
						StateEdittext.setText(listVO.state);
						ZipEdittext.setText(listVO.zip);
						EmailEdittext.setText(listVO.email);
						CountryEdittext.setText(listVO.country);
						PhoneEdittext.setText(listVO.phone);
						SelectTimeZoneEdittext.setText(listVO.timezone);
						UplineSAorMDEdittext.setText(listVO.uplineSA_MD);
						UplineCEO_EVC_Edittext.setText(listVO.ceo_evc);
						UplineSMDEdittext.setText(listVO.smd_emd);
						UserNameEdittext.setText(listVO.username);

						edittextDisable(EmailEdittext);
						edittextDisable(UserNameEdittext);

						if (chatSingleton.SAorMDArrayList.size() > 0) {
							for (int i = 0; i < chatSingleton.SAorMDArrayList
									.size(); i++) {
								AgentListVO agentVo = chatSingleton.SAorMDArrayList
										.get(i);
								if (agentVo.userid
										.equalsIgnoreCase(listVO.uplineSA_MD))
									UplineSAorMDEdittext
											.setText(agentVo.username + " ( "
													+ agentVo.firstname + " "
													+ agentVo.lastname + " )");
							}// end of for

						}
						if (chatSingleton.UplineCEOArrayList.size() > 0) {
							for (int i = 0; i < chatSingleton.UplineCEOArrayList
									.size(); i++) {
								AgentListVO agentVo = chatSingleton.UplineCEOArrayList
										.get(i);
								if (agentVo.userid
										.equalsIgnoreCase(listVO.uplineSA_MD))
									UplineCEO_EVC_Edittext
											.setText(agentVo.username + " ( "
													+ agentVo.firstname + " "
													+ agentVo.lastname + " )");
							}// end of for
						}
						if (chatSingleton.uplineSMDArrayList.size() > 0) {
							for (int i = 0; i < chatSingleton.uplineSMDArrayList
									.size(); i++) {
								AgentListVO agentVo = chatSingleton.uplineSMDArrayList
										.get(i);
								if (agentVo.userid
										.equalsIgnoreCase(listVO.uplineSA_MD))
									UplineSMDEdittext.setText(agentVo.username
											+ " ( " + agentVo.firstname + " "
											+ agentVo.lastname + " )");
							}// end of for
						}

						if (LevelEdittext.getText().toString()
								.equalsIgnoreCase("SMD")
								|| LevelEdittext.getText().toString()
										.equalsIgnoreCase("EMD")) {
							UplineSMDEdittext.setText("I an an SMD");
							edittextDisable(UplineSMDEdittext);

							enablearray[2] = true;
						} else if (LevelEdittext.getText().toString()
								.equalsIgnoreCase("CEO")
								|| LevelEdittext.getText().toString()
										.equalsIgnoreCase("EVC")) {
							UplineSMDEdittext.setText("I an an SMD");
							edittextDisable(UplineSMDEdittext);

							UplineCEO_EVC_Edittext
									.setText("My CEO or EVC is not listed");
							edittextDisable(UplineCEO_EVC_Edittext);

							UplineSAorMDEdittext.setText("I an an SA and MD");
							edittextDisable(UplineSAorMDEdittext);
							enablearray[0] = true;
							enablearray[1] = true;
							enablearray[2] = true;

						}

						CurrentPassEdittext.setText(sharedPreferences
								.getString("password", ""));
						profimage.setImageUrl(listVO.profileimageUrl.trim());
						PhoneEdittext.setText(listVO.phone.substring(0, 3)
								+ "-" + listVO.phone.substring(3, 6) + "-"
								+ listVO.phone.substring(6, 10));

						if (mProgressDialog != null)
							mProgressDialog.dismiss();
					}// end of if userfrofile values

				} else {
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(ProfileActivity.this, "ERROR: "
							+ responseText);
				}

			} catch (Exception e) {
				// TODO: handle exception
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				e.printStackTrace();
			}

		}
	}// end of ascnctask UserProfileList

}// end of class
