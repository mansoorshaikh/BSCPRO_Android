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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.airbrake.AirbrakeNotifier;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.TimePicker;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.AgentListVO;
import applicationVo.AppUtils;
import applicationVo.ChatSingleton;

public class AddBusinessActivity extends Activity {

	public RelativeLayout Chatlayout, layoutProfile, LayoutRecruit,
			LayoutBusiness, LayoutGuest, LayoutMatchup, LayoutLocator,
			LayoutLogout, LayoutBPM_Check_in, MAINSEARCHLAYOUT, ALLMAINLAYOUT,menulayout;
	RelativeLayout mainlayout;
	public EditText searchEdittext;
	public AlertDialog myAlertDialog = null;

	public static String MENU_ITEM_SELECTED = "", OPTIONSELECTED = "";
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
	public EditText bussinessdateEdittext, clientFirstnameEdittext,
			clientSecondnameEdittext, traineeEdittext, productEdittext,
			actualpointsEdittext, notesEdittext;
	public TextView agentEdittext;
	Button AddAgentbtn;

	public ArrayList<AgentListVO> agentvoArrayList = new ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> agentByTypeArrayList = new ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> sortArrayList = new ArrayList<AgentListVO>();

	public ArrayList<AgentListVO> SelectedagentvoArrayList = new ArrayList<AgentListVO>();
	public ArrayList<AgentListVO> TempagentvoArrayList = new ArrayList<AgentListVO>();

	public String agentresponseString = "";
	public String typeresponseString = "";
	public String ClickedEdittext = "", responseText = "";
	SharedPreferences sharedPreferences = null;
	public String traineeuserid = "", agentuserid = "";
	public ChatSingleton chatSingleton;

	WheelView WheelEvents;
	SlidingDrawer SDEvents;
	Button WheelDone, WheelCancle;
	int WheelPosition;
	boolean scrolling = false;
	public String AllList = "";
	int i;

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
				// agentEdittext.setText(agentEdittext.getText().toString()+"\n"+agentVo.firstname
				// + " "
				// + agentVo.lastname + " (" + agentVo.agent_id
				// + " )");

				if (ClickedEdittext.equals("agent")) {
					if (TempagentvoArrayList != null
							&& TempagentvoArrayList.size() > 0) {

						if (WheelPosition < TempagentvoArrayList.size()) {
							SelectedagentvoArrayList.add(TempagentvoArrayList
									.get(WheelPosition));
							agentEdittext.setFocusable(false);
							agentEdittext.setVisibility(View.GONE);
							addFields(SelectedagentvoArrayList);
						}

					}
				} else if (ClickedEdittext.equals("trainee")) {
					if (agentByTypeArrayList != null
							&& agentByTypeArrayList.size() > 0) {

						if (WheelPosition < agentByTypeArrayList.size()) {
							AgentListVO agentVo = agentByTypeArrayList
									.get(WheelPosition);
							traineeuserid = agentVo.userid;
							traineeEdittext.setText(agentVo.firstname + " "
									+ agentVo.lastname + " ("
									+ agentVo.agent_id + ")");

						}

						traineeEdittext.setFocusable(false);

					}
				}// end of else

			}

		});
	}

	public void addFields(ArrayList<AgentListVO> agentListArrayList) {
		final RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.AGENTDYNAMICLAYOUT);
		mainlayout.setVisibility(View.VISIBLE);
		mainlayout.removeAllViews();
		Typeface type = Typeface.createFromAsset(
				AddBusinessActivity.this.getAssets(), "fonts/calibribold.ttf");
		Typeface type2 = Typeface.createFromAsset(
				AddBusinessActivity.this.getAssets(), "fonts/calibri.ttf");

		for (int i = 0; i < agentListArrayList.size(); i++) {
			// final ChatGroupVO groupVO=usergroupArrayList.get(i);
			AgentListVO agentVo = agentListArrayList.get(i);

			// ******************************************
			RelativeLayout layout = new RelativeLayout(this);
			layout.setId(100 + i);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// layoutParams.topMargin=20;
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			// layoutParams.bottomMargin=100;

			if (i != 0)
				layoutParams.addRule(RelativeLayout.BELOW, 99 + i);

			// layout.setBackgroundResource(R.drawable.lightborder);
			layout.setLayoutParams(layoutParams);
			// layout.setBackgroundResource(R.drawable.lightborder);

			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
					40, 40);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			final ImageView cancleimage = new ImageView(
					AddBusinessActivity.this);
			cancleimage.setId(9 + i);
			// params1.rightMargin=130;
			// params1.addRule(RelativeLayout.LEFT_OF,AddAgentbtn.getId());
			cancleimage.setImageResource(R.drawable.canclebtn);
			params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			final TextView tv1 = new TextView(this);
			params2.addRule(RelativeLayout.LEFT_OF, cancleimage.getId());
			tv1.setId(4 + i);
			tv1.setTypeface(type2);
			tv1.setTextSize(13);
			tv1.setTextColor(Color.parseColor("#000000"));
			if(agentVo.agent_id.equalsIgnoreCase(""))
			tv1.setText(agentVo.firstname + " " + agentVo.lastname + agentVo.agent_id);
			else
				tv1.setText(agentVo.firstname + " " + agentVo.lastname + " ("
						+ agentVo.agent_id + " )");

			layout.addView(cancleimage, params1);
			layout.addView(tv1, params2);
			// *******************************************
			mainlayout.addView(layout, layoutParams);

			final int k = i;
			cancleimage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						tv1.setText("");
						tv1.setVisibility(View.GONE);
						cancleimage.setVisibility(View.GONE);
						// mainlayout.removeView(tv1);
						// mainlayout.removeView(cancleimage);
						// mainlayout.removeViewAt(k);
						SelectedagentvoArrayList.remove(k);

						if (SelectedagentvoArrayList.size() < 1) {
							mainlayout.removeAllViews();
							agentEdittext.setVisibility(View.VISIBLE);
							SelectedagentvoArrayList.clear();
						}

					} catch (Exception e) {
						// TODO: handle exception
						mainlayout.removeAllViews();
						agentEdittext.setVisibility(View.VISIBLE);
						SelectedagentvoArrayList.clear();
						e.printStackTrace();
					}

				}
			});

		}// end of for
	}// end of add fields

	@Override
	public void onBackPressed() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbusiness);
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
			// layoutProfile = (RelativeLayout)
			// findViewById(R.id.layoutprofile);
			LayoutRecruit = (RelativeLayout) findViewById(R.id.Layoutrecruit);
			LayoutBusiness = (RelativeLayout) findViewById(R.id.Layoutbusiness);
			LayoutGuest = (RelativeLayout) findViewById(R.id.Layoutguest);
			LayoutMatchup = (RelativeLayout) findViewById(R.id.Layoutmatchup);
			// LayoutLocator = (RelativeLayout)
			// findViewById(R.id.Layoutlocator);
			LayoutLogout = (RelativeLayout) findViewById(R.id.Layoutlogout);

			bussinessdateEdittext = (EditText) findViewById(R.id.dateedt);
			clientFirstnameEdittext = (EditText) findViewById(R.id.clientnameedt);
			agentEdittext = (TextView) findViewById(R.id.agentedt);
			traineeEdittext = (EditText) findViewById(R.id.traineeedt);
			productEdittext = (EditText) findViewById(R.id.productedt);
			actualpointsEdittext = (EditText) findViewById(R.id.actualpointedt);
			clientSecondnameEdittext = (EditText) findViewById(R.id.lastnameedt);
			notesEdittext = (EditText) findViewById(R.id.notesedt);
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
			//Button button9 = (Button) findViewById(R.id.btn_MATCHUPBOOK);
			//button9.setTypeface(font);
			NormalSmartImageView profileimageview = (NormalSmartImageView) findViewById(R.id.userimage);

			try {
				profileimageview.setImageUrl(sharedPreferences.getString(
						"profileImage", ""));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

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
					sortArrayList.clear();

					if (OPTIONSELECTED.equalsIgnoreCase("TRAINEE")) {
						for (int i = 0; i < agentByTypeArrayList.size(); i++) {
							AgentListVO agentVo = agentByTypeArrayList.get(i);
							String compairstr = agentVo.firstname + " "
									+ agentVo.lastname + "(" + agentVo.agent_id
									+ ")";
							if (textlength <= compairstr.length()) {
								/***
								 * If you want to highlight the countries which
								 * start with entered letters then choose this
								 * block. And comment the below If condition
								 * Block
								 */
								if (searchEdittext
										.getText()
										.toString()
										.equalsIgnoreCase(
												(String) compairstr
														.subSequence(0,
																textlength))) {
									sortArrayList.add(agentVo);
									// image_sort.add(listview_images[i]);
								}

								/***
								 * If you choose the below block then it will
								 * act like a Like operator in the Mysql
								 */

								/*
								 * if(listview_names[i].toLowerCase().contains(
								 * et
								 * .getText().toString().toLowerCase().trim()))
								 * { array_sort.add(listview_names[i]); }
								 */
							}
						}// end of for
					}// end of if
					else if (OPTIONSELECTED.equalsIgnoreCase("AGENT")) {
						for (int i = 0; i < TempagentvoArrayList.size(); i++) {
							AgentListVO agentVo = TempagentvoArrayList.get(i);
							String compairstr = agentVo.firstname + " "
									+ agentVo.lastname + "(" + agentVo.agent_id
									+ ")";
							if (textlength <= compairstr.length()) {
								/***
								 * If you want to highlight the countries which
								 * start with entered letters then choose this
								 * block. And comment the below If condition
								 * Block
								 */
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

					}// end of else
					SearchTrainee(sortArrayList);
				}
			});

			// ***********************************************
			Button canclebtn = (Button) findViewById(R.id.TRAINEE_CANCLE_BTN);
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

			ImageButton addnewtrainee = (ImageButton) findViewById(R.id.TRAINEE_ADD_BTN);
			addnewtrainee.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ShowDialog();
				}
			});

			TextView username = (TextView) findViewById(R.id.Chatusername);
			username.setText(sharedPreferences.getString("username", ""));

			bussinessdateEdittext
					.setText(chatSingleton.getDate(sharedPreferences.getString(
							"usertimezone", "UTC")));
			bussinessdateEdittext.setFocusable(false);
			bussinessdateEdittext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DATETIMEPICKER();
				}
			});

			productEdittext
					.setFilters(new InputFilter[] { new InputFilter.AllCaps() });

			AddAgentbtn = (Button) findViewById(R.id.AddAgentButton);
			AddAgentbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (agentvoArrayList != null && agentvoArrayList.size() > 0) {
						ClickedEdittext = "agent";

						// TempagentvoArrayList=agentvoArrayList;
						if (SelectedagentvoArrayList.size() < 2) {
							TempagentvoArrayList.clear();
							TempagentvoArrayList.addAll(agentvoArrayList);

							for (int i = 0; i < TempagentvoArrayList.size(); i++) {
								for (int j = 0; j < SelectedagentvoArrayList
										.size(); j++) {
									if (TempagentvoArrayList.get(i).equals(
											SelectedagentvoArrayList.get(j))) {
										TempagentvoArrayList.remove(i);
									}

								}
							}
							OPTIONSELECTED="AGENT";

							if (TempagentvoArrayList != null
									&& TempagentvoArrayList.size() > 0) {
								sortArrayList.clear();
								sortArrayList.addAll(TempagentvoArrayList);
								searchEdittext.setText("");
								SearchTrainee(sortArrayList);

							} else
								AppUtils.ShowAlertDialog(
										AddBusinessActivity.this,
										"Please Reload the contents");
						}// end of if
						else
							AppUtils.ShowAlertDialog(AddBusinessActivity.this,
									"Select up to two Agents");

					} else
						AppUtils.ShowAlertDialog(AddBusinessActivity.this,
								"Please Reload the contents");
				}
			});

			traineeEdittext.setFocusable(false);
			traineeEdittext.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (agentByTypeArrayList != null
							&& agentByTypeArrayList.size() > 0) {
						OPTIONSELECTED="TRAINEE";
						sortArrayList.clear();
						sortArrayList.addAll(agentByTypeArrayList);
						searchEdittext.setText("");
						SearchTrainee(sortArrayList);
					} else
						AppUtils.ShowAlertDialog(AddBusinessActivity.this,
								"Please Reload the contents");
				}
			});

			if (AppUtils.isNetworkAvailable(AddBusinessActivity.this)) {
				addValueintoEventList();
				new getAgentData().execute();
				new getAgentData_By_Type().execute();
			} else
				AppUtils.ShowAlertDialog(AddBusinessActivity.this,
						"No Internet Connection Available.");

			Button submitbtn = (Button) findViewById(R.id.Bussinesssubmitbtn);
			submitbtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (AppUtils.isNetworkAvailable(AddBusinessActivity.this)) {
						if (emptyCheck(bussinessdateEdittext)
								&& emptyCheck(clientFirstnameEdittext)
								&& emptyCheck(traineeEdittext)
								&& emptyCheck(productEdittext)
								&& emptyCheck(actualpointsEdittext)
								&& emptyCheck(clientSecondnameEdittext)
								&& emptyCheck(notesEdittext)) {

							if (SelectedagentvoArrayList.size() > 0)
								new myTask_saveUserDetails_call().execute();
							else
								AppUtils.ShowAlertDialog(
										AddBusinessActivity.this,
										"All field are mandatory!!!");

						}// end of if
						else
							AppUtils.ShowAlertDialog(AddBusinessActivity.this,
									"All field are mandatory!!!");

					}// end of if for internet check
					else
						AppUtils.ShowAlertDialog(AddBusinessActivity.this,
								"No Internet Connection Available.");

				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

	}// end of on create()

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

			agentuserid = SelectedagentvoArrayList.get(0).userid.equals("")?SelectedagentvoArrayList.get(0).firstname:SelectedagentvoArrayList.get(0).userid;

			for (int i = 1; i < SelectedagentvoArrayList.size(); i++)
			{
				String str=SelectedagentvoArrayList.get(i).userid.equals("")?SelectedagentvoArrayList.get(i).firstname:SelectedagentvoArrayList.get(i).userid;
				agentuserid = agentuserid + ","+ str;
			}
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("user_id",
					sharedPreferences.getString("userid", "")));
			nameValuePair.add(new BasicNameValuePair("sec_user",
					sharedPreferences.getString("username", "")));
			nameValuePair.add(new BasicNameValuePair("sec_pass",
					sharedPreferences.getString("password", "")));

			nameValuePair.add(new BasicNameValuePair("product_date",
					bussinessdateEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("client_fname",
					clientFirstnameEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("client_lname",
					clientSecondnameEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("agent_value[]",
					agentuserid));
			nameValuePair.add(new BasicNameValuePair("product_name",
					productEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("actual_point",
					actualpointsEdittext.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("trainee", traineeuserid
					.equals("") ? traineeEdittext.getText().toString()
					: traineeuserid));
			nameValuePair.add(new BasicNameValuePair("notes", notesEdittext
					.getText().toString()));

			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/product_api");
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
					AppUtils.ShowAlertDialog(AddBusinessActivity.this,
							"Saved Successfully.");
					clearData();
				} else {
					AppUtils.ShowAlertDialog(AddBusinessActivity.this,
							"Error :" + responseText);
				}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(AddBusinessActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");

				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}
		}// end of onpost()
	}// ends of Async task

	public void clearData() {
		bussinessdateEdittext.setText(chatSingleton.getDate(sharedPreferences
				.getString("usertimezone", "UTC")));
		clientFirstnameEdittext.setText("");
		clientSecondnameEdittext.setText("");
		agentEdittext.setText("");
		traineeEdittext.setText("");
		productEdittext.setText("");
		actualpointsEdittext.setText("");
		notesEdittext.setText("");
		RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.AGENTDYNAMICLAYOUT);
		mainlayout.removeAllViews();
		SelectedagentvoArrayList.clear();
		agentEdittext.setVisibility(View.GONE);
	}

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
						"https://bscpro.com/profile_api/getUserlist/");
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
			String responseText = null;
			JSONObject jresponse = null;
			try {

				jresponse = new JSONObject(agentresponseString);

				responseText = jresponse.getString("message");
				status = jresponse.getString("status");
				JSONArray activityArray = null;
				if (responseText.equalsIgnoreCase("") && status.equals("ok")) {
					agentvoArrayList.clear();
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
					AppUtils.ShowAlertDialog(AddBusinessActivity.this,
							"ERROR: " + responseText);

				if (mProgressDialog != null)
					mProgressDialog.dismiss();

			} catch (Exception e) {
				// TODO: handle exception
				// myProgressBar.setVisibility(View.GONE);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(AddBusinessActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

			// new group_chat_message().execute();
			// do stuff after posting data
		}
	}

	// ******************************Async task claass---GetAgent data BY TYPE
	public class getAgentData_By_Type extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			// onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected String doInBackground(String... strings) {
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("type", "TA"));
//			nameValuePair.add(new BasicNameValuePair("user_id",
//						 sharedPreferences.getString("userid", "")));
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
					AppUtils.ShowAlertDialog(AddBusinessActivity.this,
							"ERROR: " + responseText);

				if (mProgressDialog != null)
					mProgressDialog.dismiss();

			} catch (Exception e) {
				// TODO: handle exception
				// myProgressBar.setVisibility(View.GONE);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(AddBusinessActivity.this,
						"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes. ");
				e.printStackTrace();
				AirbrakeNotifier.notify(e);
			}

			// new group_chat_message().execute();
			// do stuff after posting data
		}
	}// end of getby Type

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(AddBusinessActivity.this,
					UserProfileActivity.class);
			startActivity(intent);
		}
		// else if (view.getId() == R.id.layoutprofile) {
		// MENU_ITEM_SELECTED = "PROFILE";
		// Intent intent = new Intent(AddBusinessActivity.this,
		// ProfileActivity.class);
		// startActivity(intent);
		// }
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			Intent intent = new Intent(AddBusinessActivity.this,
					NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			isExpanded=false;
			new CollapseAnimation(slidingPanel, panelWidth,
					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f,menulayout);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			Intent intent = new Intent(AddBusinessActivity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(AddBusinessActivity.this,
					MatchupActivity.class);
			startActivity(intent);
		}
		// else if (view.getId() == R.id.Layoutlocator) {
		// MENU_ITEM_SELECTED = "OFFICE LOCATOR";
		// Intent intent = new Intent(AddBusinessActivity.this,
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
			Intent intent = new Intent(AddBusinessActivity.this,
					LoginActivity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(AddBusinessActivity.this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		} else if (view.getId() == R.id.DashboardLayout) {
			MENU_ITEM_SELECTED = "Dashboard";
			Intent intent = new Intent(AddBusinessActivity.this,
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
	}// end of menu

	public void DATETIMEPICKER() {

		final View dialogView = View.inflate(this, R.layout.date_time_picker,
				null);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		final DatePicker datePicker = (DatePicker) dialogView
				.findViewById(R.id.date_picker);
		if(!bussinessdateEdittext.getText().toString().equalsIgnoreCase(""))
		  {
			try {
			
			String temp2=bussinessdateEdittext.getText().toString();
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

						bussinessdateEdittext.setText(writeFormat2.format(time));
						bussinessdateEdittext.setFocusable(false);
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

			if (ClickedEdittext.equals("agent")) {
				return TempagentvoArrayList.size();
			}
			if (ClickedEdittext.equals("trainee")) {
				return agentByTypeArrayList.size();
			}
			return WheelPosition;

		}

		@Override
		protected CharSequence getItemText(int index) {

			if (ClickedEdittext.equals("agent")) {
				AgentListVO uVo = TempagentvoArrayList.get(index);
				return uVo.firstname + " " + uVo.lastname + " (" + uVo.agent_id
						+ " )";

			}
			if (ClickedEdittext.equals("trainee")) {
				AgentListVO uVo = agentByTypeArrayList.get(index);
				return uVo.firstname + " " + uVo.lastname + " (" + uVo.agent_id
						+ " )";

			}
			return "";
		}
	}// end of wheel adapter

	public void SearchTrainee(ArrayList<AgentListVO> agentListArrayList) {
		mainlayout = (RelativeLayout) findViewById(R.id.TRAINEELIST_LAYOUT);
		mainlayout.removeAllViews();

		mainlayout.setVisibility(View.VISIBLE);
		MAINSEARCHLAYOUT.setVisibility(View.VISIBLE);
		ALLMAINLAYOUT.setVisibility(View.INVISIBLE);
		Typeface type = Typeface.createFromAsset(
				AddBusinessActivity.this.getAssets(), "fonts/calibribold.ttf");
		Typeface type2 = Typeface.createFromAsset(
				AddBusinessActivity.this.getAssets(), "fonts/calibri.ttf");
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
						// tv1.setText("");
						// tv1.setVisibility(View.GONE);
                        if(OPTIONSELECTED.equalsIgnoreCase("TRAINEE"))
                        {
						traineeEdittext.setText(tv1.getText().toString());
						traineeuserid = agentVo.userid;
                        }
                        else if(OPTIONSELECTED.equalsIgnoreCase("AGENT"))
                        {
                        	SelectedagentvoArrayList.add(agentVo);
							agentEdittext.setFocusable(false);
							agentEdittext.setVisibility(View.GONE);
							addFields(SelectedagentvoArrayList);
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
		if (OPTIONSELECTED.equalsIgnoreCase("TRAINEE"))
		{
			newtrainee.setHint("Add new Trainee");
			popDialog.setTitle("Add new Trainee");
		
		}else if (OPTIONSELECTED.equalsIgnoreCase("AGENT"))
		{
			newtrainee.setHint("Add new Agent");
			popDialog.setTitle("Add new Agent");
		}

		popDialog.setCancelable(false);
		popDialog.setView(Viewlayout);
		// txtPerc.setText(url);
		donebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {

					if (!newtrainee.getText().toString().equals("")) {
						if (OPTIONSELECTED.equalsIgnoreCase("TRAINEE")) {
							traineeEdittext.setText(newtrainee.getText()
									.toString());
							mainlayout.removeAllViews();
							MAINSEARCHLAYOUT.setVisibility(View.GONE);
							ALLMAINLAYOUT.setVisibility(View.VISIBLE);
							traineeuserid = "";
						} else if (OPTIONSELECTED.equalsIgnoreCase("AGENT")) {
							
							AgentListVO agentListVO=new AgentListVO();
							agentListVO.firstname=newtrainee.getText().toString();
							SelectedagentvoArrayList.add(agentListVO);
							agentEdittext.setFocusable(false);
							agentEdittext.setVisibility(View.GONE);
							addFields(SelectedagentvoArrayList);
							
							mainlayout.removeAllViews();
							MAINSEARCHLAYOUT.setVisibility(View.GONE);
							ALLMAINLAYOUT.setVisibility(View.VISIBLE);
							//traineeuserid = "";
						}

						myAlertDialog.dismiss();
					} else
						AppUtils.ShowAlertDialog(AddBusinessActivity.this,
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
			}
		});

		myAlertDialog = popDialog.create();
		myAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams wmlp = myAlertDialog.getWindow()
				.getAttributes();
		myAlertDialog.show();
	}// end of showdialog

}// end of class
