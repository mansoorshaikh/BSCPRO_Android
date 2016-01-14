package com.MWC.chatapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import applicationVo.AppUtils;
import applicationVo.UserProfileDataAsync;

public class ChatActivity extends Activity implements OnClickListener {

	private EditText messageET;
	private ListView messagesContainer;
	private Button sendBtn;
	private Button attachBtn;
	private ChatAdapter adapter;
	private ChatUserAdapter useradapter;
	public ProgressBar myProgressBar = null;

	SharedPreferences sharedPreferences = null;

	private ArrayList<ChatMessageVo> chatHistory = new ArrayList<ChatMessageVo>();
	private ArrayList<ChatUserMessageVO> userchatHistory = new ArrayList<ChatUserMessageVO>();

	ProgressDialog pDialog = null;
	String responseString = "",PostImageURL="";
	String responseString2 = "";
	TextView tx,timetextview;
	String Userid = "";
	String imagepath = "";
	// ***************************************
	String chatuser_id = "", group_id = "", group_name = "", group_icon = "";
	String isGroup = "";
	String delmessage_id = "";
	// ****************************************
	// **********************************************
	private static int RESULT_LOAD_IMG = 2;
	String imgDecodableString = "";
	Bitmap image;
	Bitmap photo = null;

	private String mPath = "";
	private Uri mImageCaptureUri;
	private static final int PICK_FROM_CAMERA = 1;

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
		 
		 try {
			 Intent logIntent = new Intent(getApplicationContext(),
						UserProfileActivity.class);
				startActivity(logIntent);
				finish();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	 }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		try {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		myProgressBar = (ProgressBar) findViewById(R.id.ChatprogressBar);
		
		Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
		
		Button back_Button=(Button)findViewById(R.id.btn_BACK);
		back_Button.setTypeface(font);
		back_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					 Intent logIntent = new Intent(getApplicationContext(),
								UserProfileActivity.class);
						startActivity(logIntent);
						finish();
			
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			group_id = bundle.getString("group_id");
			group_name = bundle.getString("group_name");
			group_icon = bundle.getString("group_icon");
			isGroup = bundle.getString("is_group");
		}
		// sharedPreferences =
		// PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Userid= sharedPreferences.getString("useridkey", "") ;
		//
		
		if(AppUtils.isNetworkAvailable(ChatActivity.this))
		{
		if (isGroup.equals("true")) {
			new group_chat_message().execute();
		} else {
			new user_chat_message().execute();
		}
		}
		else
			AppUtils.ShowAlertDialog(ChatActivity.this, "No Internet Connection Available.");

		initControls();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}//end of onCreate()

	private void initControls() {
		messagesContainer = (ListView) findViewById(R.id.messagesContainer);
		messageET = (EditText) findViewById(R.id.SendmessageEdit);
		sendBtn = (Button) findViewById(R.id.chatSendButton);
		attachBtn = (Button) findViewById(R.id.attachButton);
		
		Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
         attachBtn.setTypeface(font);
		// Bundle extra=getIntent().getExtras();
		// String profusername=extra.getString("profuser");
		try {
			TextView textView=(TextView)findViewById(R.id.friendLabel);
			UserProfileDataAsync async=UserProfileDataAsync.getInstance(ChatActivity.this);
			textView.setTypeface(async.getFountType());
			textView.setText(group_name);	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
//		TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
//		companionLabel.setText(group_name);

		 timetextview = (TextView) findViewById(R.id.Timetextview);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		timetextview.setText(df.format(c.getTime())) ;

		RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

		adapter = new ChatAdapter(ChatActivity.this,
				new ArrayList<ChatMessageVo>());
		messagesContainer.setAdapter(adapter);
//		messagesContainer.setOnItemClickListener(new OnItemClickListener() {
//			  public void onItemClick(AdapterView<?> parent, View view,
//					    int position, long id) {
//				  if(userchatHistory.size()>0)
//	            	{
//	            		ChatUserMessageVO userchatmessage=userchatHistory.get(position);
//	            		Toast.makeText(getApplicationContext(), userchatmessage.message,Toast.LENGTH_LONG).show();
//	            	}
//		             
//					  }
//
//				
//					}); 	

		messagesContainer
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0, View v,
							int position, long arg3) {
						// TODO Auto-generated method stub
						final int pos = position;

						if (isGroup.equals("true")) {

							new AlertDialog.Builder(ChatActivity.this)
									.setTitle("Delete entry")
									.setMessage(
											"Are you sure you want to delete this Message?")
									.setPositiveButton(
											android.R.string.yes,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// continue with delete
													ChatMessageVo chatMessage = chatHistory
															.get(pos);
													delmessage_id = chatMessage.message_id;
													new delete_message()
															.execute();
												}
											})
									.setNegativeButton(
											android.R.string.no,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// do nothing
												}
											})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();
						} else {

							new AlertDialog.Builder(ChatActivity.this)
									.setTitle("Delete entry")
									.setMessage(
											"Are you sure you want to delete this Message?")
									.setPositiveButton(
											android.R.string.yes,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// continue with delete
													ChatUserMessageVO chatMessage = userchatHistory
															.get(pos);
													delmessage_id = chatMessage.message_id;
													new delete_message()
															.execute();
												}
											})
									.setNegativeButton(
											android.R.string.no,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// do nothing
												}
											})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();
						}

						return true;
					}
				});

		sendBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String messageText = messageET.getText().toString();
				if (TextUtils.isEmpty(messageText)&&photo == null) {
					Toast.makeText(getApplicationContext(), "Type Message..",
							Toast.LENGTH_LONG).show();
					return;
				}

				if (AppUtils.isNetworkAvailable(ChatActivity.this))
				{
					if(photo==null)
						new myPost_usertext_message().execute();
					else
						new Upload_image_Async().execute();
				}
					
				else
					AppUtils.ShowAlertDialog(ChatActivity.this,
							"No Internet Connection Available.");

			}
		});

		attachBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImageFromGallery();
			}
		});

	} // *********************initControl

	// ******************************************picking up the image
	public void selectImageFromGallery() {

		AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);

		new AlertDialog.Builder(ChatActivity.this)
				.setMessage("Choose option")
				.setCancelable(false)
				.setPositiveButton("Gallery",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Create intent to Open Image applications like
								// Gallery, Google Photos
								try {
									
								
								Intent galleryIntent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								// Start the Intent
								startActivityForResult(galleryIntent,
										RESULT_LOAD_IMG);

								dialog.dismiss();
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}

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
	try{
		attachBtn = (Button) findViewById(R.id.attachButton);
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

			photo = Bitmap.createScaledBitmap(photo, 200, 200, true);

			image = Bitmap.createScaledBitmap(photo, attachBtn.getWidth(),
					attachBtn.getHeight(), true);
			//attachBtn.setImageBitmap(image);

		}// end of else outer
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	} // end of function

	// *******************************************

	// **************************************************adapter update
	public void displayMessage(ChatMessageVo message) {
		adapter.add(message);
		adapter.notifyDataSetChanged();
		scroll();
	}

	public void displayuserMessage(ChatUserMessageVO message) {
		useradapter.add(message);
		useradapter.notifyDataSetChanged();
		scroll();
	}

	private void scroll() {
		messagesContainer.setSelection(messagesContainer.getCount() - 1);
	}

	// ***************************Async task Class ----1
	class group_chat_message extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		protected String doInBackground(String... aurl) {

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("sec_user",
					sharedPreferences.getString("username", "")));
			nameValuePair.add(new BasicNameValuePair("sec_pass",
					sharedPreferences.getString("password", "")));
			nameValuePair.add(new BasicNameValuePair("groupId", group_id));
			try {

				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://bscpro.com/chat_api/groupMessages/");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return "";
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String str_resp) {
			super.onPostExecute(str_resp);

			chatHistory = new ArrayList<ChatMessageVo>();
			String status = null;
			String responseText = null;
			JSONObject jresponse = null;
			try {
				jresponse = new JSONObject(responseString);
			
				responseText = jresponse.getString("message");
				status = jresponse.getString("status");

			if (responseText.equals("success") && status.equals("ok")) {

				try {

					// responseString = jresponse.getString("chatlist");
					JSONArray activityArray = null;
					try {
						activityArray = jresponse.getJSONArray("groupchatlist");
					} catch (Exception e) {
						// TODO: handle exception
						activityArray = new JSONArray();
						activityArray.put(jresponse
								.getJSONObject("groupchatlist"));
					}// end of catch;
					chatHistory.clear();
					for (int i = 0; i < activityArray.length(); i++) {

						JSONObject jso = (JSONObject) activityArray.get(i);

						ChatMessageVo groupchatmessage = new ChatMessageVo();

						if (!jso.isNull("groupid"))
							groupchatmessage.groupid = jso.getString("groupid");

						if (!jso.isNull("message_id"))
							groupchatmessage.message_id = jso
									.getString("message_id");

						if (!jso.isNull("user_id"))
							groupchatmessage.user_id = jso.getString("user_id");

						if (!jso.isNull("username"))
							groupchatmessage.username = jso
									.getString("username");

						groupchatmessage.groupName = group_name;

						// groupchatmessage.groupName = "TestGroup";

						groupchatmessage.groupicon = group_icon;

						// groupchatmessage.groupicon="http://mybscpro.com/assets/images/profilePic/img_pofile_4.jpg";

						if (!jso.isNull("message"))
							groupchatmessage.message = jso.getString("message");

						if (!jso.isNull("attach_image"))
							groupchatmessage.attach_image = jso
									.getString("attach_image");

						if (!jso.isNull("attach_file"))
							groupchatmessage.attach_file = jso
									.getString("attach_file");

						if (!jso.isNull("message_time")) {
							DateTimeDifference dt = new DateTimeDifference();
							String date = dt.findDifference(jso
									.getString("message_time"));
							groupchatmessage.message_time = date;

						}
						if (!jso.isNull("receiver_id"))
							groupchatmessage.receiver_id = jso
									.getString("receiver_id");

						if (!jso.isNull("receiver"))
							groupchatmessage.receiver = jso
									.getString("receiver");

						if (!jso.isNull("subject"))
							groupchatmessage.subject = jso.getString("subject");

						if (groupchatmessage.user_id.equals(sharedPreferences
								.getString("userid", ""))) {
							groupchatmessage.isMe = true;
							groupchatmessage.groupicon = sharedPreferences
									.getString("profileImage", "");

						}

						chatHistory.add(groupchatmessage);
					}
					
					if (mProgressDialog != null)
						mProgressDialog.dismiss();

					adapter = new ChatAdapter(ChatActivity.this,
							new ArrayList<ChatMessageVo>());
					messagesContainer.setAdapter(adapter);

					for (int i = 0; i < chatHistory.size(); i++) {
						ChatMessageVo message = chatHistory.get(i);
						displayMessage(message);
					}
					

				} catch (Exception e) {
					// TODO: handle exception
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					myProgressBar.setVisibility(View.INVISIBLE);
					e.printStackTrace();
				}
				
				

			}// en dof if
			else if(responseText.equalsIgnoreCase("No messages"))
				AppUtils.ShowAlertDialog(ChatActivity.this,responseText);
			else
				AppUtils.ShowAlertDialog(ChatActivity.this, "ERROR :"
						+ responseText);

			if (mProgressDialog != null)
				mProgressDialog.dismiss();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				if (mProgressDialog != null)
//					mProgressDialog.dismiss();
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(ChatActivity.this,"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				
				e.printStackTrace();
			}

		}// end of postExecute
	}

	// ***************************End Async task Class --1

	
	// ***************************Async task Class ----2
	class user_chat_message extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		protected String doInBackground(String... aurl) {

			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			nameValuePair.add(new BasicNameValuePair("receiver_id", group_id));
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
						"https://bscpro.com/chat_api/chatConversation/");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return "";
		}

		@SuppressWarnings("deprecation")
		protected void onPostExecute(String str_resp) {
			super.onPostExecute(str_resp);
			String status = null;
			String responseText = null;
			JSONObject jresponse = null;
			try {
				jresponse = new JSONObject(responseString);
			
				responseText = jresponse.getString("message");
				status = jresponse.getString("status");

			if (responseText.equals("success") && status.equals("ok")) {

				try {

					// responseString = jresponse.getString("chatlist");
					JSONArray activityArray = null;
					try {
						activityArray = jresponse.getJSONArray("chatlist");
					} catch (Exception e) {
						// TODO: handle exception
						activityArray = new JSONArray();
						activityArray.put(jresponse.getJSONObject("chatlist"));
					}// end of catch
					userchatHistory.clear();

					for (int i = 0; i < activityArray.length(); i++) {

						JSONObject jso = (JSONObject) activityArray.get(i);

						ChatUserMessageVO userchatmessage = new ChatUserMessageVO();

						if (!jso.isNull("user_id"))
							userchatmessage.user_id = jso.getString("user_id");

						if (!jso.isNull("message_id"))
							userchatmessage.message_id = jso
									.getString("message_id");

						if (!jso.isNull("fav_message"))
							userchatmessage.fav_message = jso
									.getString("fav_message");

						if (!jso.isNull("username"))
							userchatmessage.username = jso
									.getString("username");

						// groupchatmessage.groupName = group_name;

						userchatmessage.ChatuserName = group_name;

						// groupchatmessage.groupicon= group_icon;
						userchatmessage.profileimage = group_icon;

						if (!jso.isNull("message"))
							userchatmessage.message = jso.getString("message");

						if (!jso.isNull("attach_image"))
							userchatmessage.attach_image = jso
									.getString("attach_image");

						if (!jso.isNull("attach_file"))
							userchatmessage.attach_file = jso
									.getString("attach_file");

						if (!jso.isNull("message_time")) {
							DateTimeDifference dt = new DateTimeDifference();
							String date = dt.findDifference(jso
									.getString("message_time"));
							userchatmessage.message_time = date;
						}
						if (!jso.isNull("receiver_id"))
							userchatmessage.receiver_id = jso
									.getString("receiver_id");

						if(!jso.isNull("receiver"))
							userchatmessage.receiver = jso
									.getString("receiver");

						if (!jso.isNull("subject"))
							userchatmessage.subject = jso.getString("subject");
						
						if (!jso.isNull("matchup_id"))
							userchatmessage.matchup_id = jso.getString("matchup_id");
						
						if (!jso.isNull("req_response"))
							userchatmessage.req_response = jso.getString("req_response");

						if (userchatmessage.user_id.equals(sharedPreferences
								.getString("userid", ""))) {
							userchatmessage.isMe = true;
							userchatmessage.profileimage = sharedPreferences
									.getString("profileImage", "");

						}

						userchatHistory.add(userchatmessage);
					}

					useradapter = new ChatUserAdapter(ChatActivity.this,
							new ArrayList<ChatUserMessageVO>());
					messagesContainer.setAdapter(useradapter);

					for (int i = 0; i < userchatHistory.size(); i++) {
						ChatUserMessageVO message = userchatHistory.get(i);
						displayuserMessage(message);
					}

					if (mProgressDialog != null)
						mProgressDialog.dismiss();

				} catch (Exception e) {
					// TODO: handle exception
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					e.printStackTrace();
				}
			}// end of if
			else if(responseText.equalsIgnoreCase("No messages"))
					AppUtils.ShowAlertDialog(ChatActivity.this,responseText);
				else
					AppUtils.ShowAlertDialog(ChatActivity.this, "ERROR :"
							+ responseText);
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
			// end of else
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				AppUtils.ShowAlertDialog(ChatActivity.this,"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
				
				e.printStackTrace();
			}
		}// end of postExecute
	}

	// ***************************End Async task Class --2

	// ******************************Async task claass---2.1
	public class myPost_usertext_message extends
			AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected String doInBackground(String... strings) {
			try {

				userpostText();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} // end of doInBackground();

		@Override
		protected void onPostExecute(String lenghtOfFile) {
			messageET.setText("");
			String status = null;
			String responseText = null;
			JSONObject jresponse = null;
			try {
				jresponse = new JSONObject(responseString);

				responseText = jresponse.getString("message");
				status = jresponse.getString("status");

				if (responseText.equalsIgnoreCase("Message Sent.")&& status.equals("ok")||responseText.equalsIgnoreCase("Attachment send successfully.")) {
					photo=null;
					if (isGroup.equalsIgnoreCase("true"))
					{
						if (mProgressDialog != null)
							mProgressDialog.dismiss();
						new group_chat_message().execute();
					}
						
					else
					{
						if (mProgressDialog != null)
							mProgressDialog.dismiss();
						new user_chat_message().execute();
					}
						
				} else
					AppUtils.ShowAlertDialog(ChatActivity.this, "ERROR: "
							+ responseText);

				if (mProgressDialog != null)
					mProgressDialog.dismiss();

			} catch (Exception e) {
				// TODO: handle exception
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
				e.printStackTrace();
			}

			// new group_chat_message().execute();
			// do stuff after posting data
		}
	}

	// this will post our text data
	@SuppressLint("NewApi")
	private void userpostText() {
		try {

			String msg = messageET.getText().toString();

			// msg = msg.replaceAll(" ", "%20");
			// msg=msg.replaceAll("[\\\t|\\\n|\\\r]","%0A");

			// SharedPreferences sharedPreferences = PreferenceManager
			// .getDefaultSharedPreferences(getApplicationContext());
			//
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

			if (isGroup.equalsIgnoreCase("true"))
				nameValuePair.add(new BasicNameValuePair("groupId", group_id));
			else
				nameValuePair.add(new BasicNameValuePair("receiverid", group_id));

			nameValuePair.add(new BasicNameValuePair("subject", "test"));
			nameValuePair.add(new BasicNameValuePair("messages", msg));

			if (photo == null) {
				nameValuePair.add(new BasicNameValuePair("attachment", ""));
			} else {
				
				nameValuePair.add(new BasicNameValuePair("attachment",PostImageURL));
			}

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
						"https://bscpro.com/chat_api/postusermessage/");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseString = EntityUtils.toString(response.getEntity());
				System.out.println(responseString + "response is display");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// ******************************End of Async Task---2.2
	
	
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
				if(PostImageURL!=null||PostImageURL!="")
				{
					if (mProgressDialog != null)
						  mProgressDialog.dismiss();
					new myPost_usertext_message().execute();
				}
				else
				{
					if (mProgressDialog != null)
						  mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(ChatActivity.this, "Image Uploading Fail,Please Try Again.");
				}
					
				
				
//				new myTask_saveUserDetails_call().execute();
					
				
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
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				PostImageURL = EntityUtils.toString(response.getEntity());
				System.out.println( "response is display");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	
	

	// ******************************Async Task---3 delete message
	public class delete_message extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
		}

		@Override
		protected String doInBackground(String... strings) {
			try {

				deletemsg();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} // end of doInBackground();

		@Override
		protected void onPostExecute(String lenghtOfFile) {
			//pd.dismiss();
			try {
				
			if (responseString.equals("success")) {
				// Toast.makeText(getApplicationContext(),
				// "postdata:="+responseString,Toast.LENGTH_LONG).show();

				if (isGroup.equals("true"))
				{
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					new group_chat_message().execute();
				}
					
				else
				{
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					new user_chat_message().execute();
				}
			
					
			} else
				AppUtils.ShowAlertDialog(ChatActivity.this, "ERROR :Delation is Failed." );
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
			AppUtils.ShowAlertDialog(ChatActivity.this,"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");
			
			e.printStackTrace();
		}
		}
	}

	// this will post our text data
	private void deletemsg() {
		try {

			String postReceiverUrl = "https://bscpro.com/chatuser/deleteMessage/?msg_id="
					+ delmessage_id;
			// Log.v(TAG, "postURL: " + postReceiverUrl);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(postReceiverUrl);

			// execute HTTP post request
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {

				responseString = EntityUtils.toString(resEntity).trim();

				// you can add an if statement here and do other actions based
				// on the response
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ******************************End of Async Task---3 delete message

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}// end of class
