package applicationVo;

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
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class UserProfileDataAsync {
	
	public String countryresponcestring = "", SAorMDresponcestring = "",
	uplineCEOresponcestring = "", uplineSMDresponcestring = "",UserProfileresponsestring;
	SharedPreferences sharedPreferences;
	public ChatSingleton chatinstance = null;
	public Context context;
	public Typeface founttype;	
	//************************************
//	public static String GCMRegister_Id="";
//    Controller aController;
	
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	//************************************
		 
	private static UserProfileDataAsync profileDataAsync=null;
	
	public UserProfileDataAsync(Context context)
	{
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context.getApplicationContext());
		chatinstance = ChatSingleton.getInstance();
		this.context=context;
		
	}
	
	public Typeface getFountType()
	{
		return founttype = Typeface.createFromAsset(context.getAssets(),"fonts/calibri.ttf");
	}
	
	public static UserProfileDataAsync getInstance(Context context)
	{
		if(profileDataAsync==null)
			profileDataAsync=new UserProfileDataAsync(context);
		
		return profileDataAsync;

	}
		
	
	public void LoadAllUserprofiledata()
	{
		new getCounterData().execute(); 
		new getSAorMDData().execute();
		new getUplineCEOData().execute();
		new getUplineSMDData().execute();
		new getUserProfileData().execute();
	}
	
	
	public void LoadCountryData()
	{
		new getCounterData().execute(); 
	}
	
	public void LoadSAorMdData()
	{
		new getSAorMDData().execute();
	}
	
	public void LoadUplineCEOData()
	{
		new getUplineCEOData().execute();
	}
	
	public void LoadUplineSMDData()
	{
		new getUplineSMDData().execute();
	}
	
	public void LoadUserProfiledata()
	{
		new getUserProfileData().execute();
	}
	// ******************************Async task claass---GetcountryDATA data
		public class getCounterData extends AsyncTask<String, String, String> {

			protected void onPreExecute() {
				super.onPreExecute();
				// onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
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
							"http://bscpro.com/profile_api/getCountry");
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
							"UTF-8"));
					HttpResponse response = httpclient.execute(httppost);
					countryresponcestring = EntityUtils.toString(response
							.getEntity());
					// System.out.println(countryresponcestring
					// + "response is display");

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

					jresponse = new JSONObject(countryresponcestring);

					responseText = jresponse.getString("message");
					status = jresponse.getString("status");
					JSONArray activityArray = null;
					if (responseText.equalsIgnoreCase("success")
							&& status.equals("ok")) {
						chatinstance.countryArrayList.clear();
						activityArray = jresponse.getJSONArray("countrylist");

						for (int i = 0; i < activityArray.length(); i++) {

							JSONObject activityObject = (JSONObject) activityArray
									.get(i);

							if (!activityObject.isNull("USA"))
								chatinstance.countryArrayList.add(activityObject
										.getString("USA"));

						}// end of for

					} else
						AppUtils.ShowAlertDialog(context, "ERROR: "
								+ responseText);

					

				} catch (Exception e) {
					// TODO: handle exception
					// myProgressBar.setVisibility(View.GONE);
					
					AppUtils.ShowAlertDialog(context,
							"Something Is Gone Wrong Wlile Loading Data.");
					e.printStackTrace();
				}

				// new group_chat_message().execute();
				// do stuff after posting data
			}
		}// end of ascnctask getCountry

	
		
		// ******************************Async task claass---GetSA_Or_DADATA data
		public class getSAorMDData extends AsyncTask<String, String, String> {

			protected void onPreExecute() {
				super.onPreExecute();
				// onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
			}

			@Override
			protected String doInBackground(String... strings) {
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

				nameValuePair.add(new BasicNameValuePair("type", "A,TA,SA,MD"));

				try {

					// Defined URL where to send data
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://bscpro.com/profile_api/getUsersByType");
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
							"UTF-8"));
					HttpResponse response = httpclient.execute(httppost);
					SAorMDresponcestring = EntityUtils.toString(response
							.getEntity());
					// System.out.println(SAorMDresponcestring
					// + "response is display");

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

					jresponse = new JSONObject(SAorMDresponcestring);

					responseText = jresponse.getString("message");
					status = jresponse.getString("status");
					JSONArray activityArray = null;
					if (responseText.equalsIgnoreCase("") && status.equals("ok")) {
						chatinstance.SAorMDArrayList.clear();
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

							chatinstance.SAorMDArrayList.add(cVo);

						}// end of for
						chatinstance.SAorMDArrayList.size();

					} else
						AppUtils.ShowAlertDialog(context, "ERROR: "
								+ responseText);

					

				} catch (Exception e) {
					// TODO: handle exception
					// myProgressBar.setVisibility(View.GONE);
					
					//AppUtils.ShowAlertDialog(LoginActivity.this,
					//		"App is Under Maintenance please wait some time.");
					e.printStackTrace();
				}

				// new group_chat_message().execute();
				// do stuff after posting data
			}
		}// end of ascnctask SAorMD
		// ******************************Async task claass---GetUplineCEO_DATA data
		public class getUplineCEOData extends AsyncTask<String, String, String> {

			protected void onPreExecute() {
				super.onPreExecute();
				// onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
			}

			@Override
			protected String doInBackground(String... strings) {
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

				nameValuePair.add(new BasicNameValuePair("type", "CEO,EVC"));

				try {

					// Defined URL where to send data
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://bscpro.com/profile_api/getUsersByType/");
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
							"UTF-8"));
					HttpResponse response = httpclient.execute(httppost);
					uplineCEOresponcestring = EntityUtils.toString(response
							.getEntity());
					// System.out.println(uplineCEOresponcestring
					// + "response is display");

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

					jresponse = new JSONObject(uplineCEOresponcestring);

					responseText = jresponse.getString("message");
					status = jresponse.getString("status");
					JSONArray activityArray = null;
					if (responseText.equalsIgnoreCase("") && status.equals("ok")) {
						chatinstance.UplineCEOArrayList.clear();
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

							chatinstance.UplineCEOArrayList.add(cVo);

						}// end of for

					} else
						AppUtils.ShowAlertDialog(context, "ERROR: "
								+ responseText);


				} catch (Exception e) {
					// TODO: handle exception
					// myProgressBar.setVisibility(View.GONE);
					
					//AppUtils.ShowAlertDialog(LoginActivity.this,
					//		"App is Under Maintenance please wait some time.");
					e.printStackTrace();
				}

				// new group_chat_message().execute();
				// do stuff after posting data
			}
		}// end of ascnctask UplineCEO

		// ******************************Async task claass---GetUplineSMD_DATA data
		public class getUplineSMDData extends AsyncTask<String, String, String> {

			protected void onPreExecute() {
				super.onPreExecute();
				// onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
			}

			@Override
			protected String doInBackground(String... strings) {
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

				nameValuePair
						.add(new BasicNameValuePair("type", "CEO,EVC,SMD,EMD"));

				try {

					// Defined URL where to send data
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://bscpro.com/profile_api/getUsersByType/");
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded;");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
							"UTF-8"));
					HttpResponse response = httpclient.execute(httppost);
					uplineSMDresponcestring = EntityUtils.toString(response
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

					jresponse = new JSONObject(uplineSMDresponcestring);

					responseText = jresponse.getString("message");
					status = jresponse.getString("status");
					JSONArray activityArray = null;
					if (responseText.equalsIgnoreCase("") && status.equals("ok")) {
						chatinstance.uplineSMDArrayList.clear();
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

							chatinstance.uplineSMDArrayList.add(cVo);

						}// end of for

					} else
						AppUtils.ShowAlertDialog(context, "ERROR: "
								+ responseText);


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}// end of ascnctask UplineSMD

		
		// ******************************Async task claass---GetUserprofileList data
				public class getUserProfileData extends AsyncTask<String, String, String> {

					protected void onPreExecute() {
						super.onPreExecute();
						// onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
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
							if (responseText.equalsIgnoreCase("success") && status.equals("ok")) {
								
								activityArray = jresponse.getJSONArray("userdetail");
								chatinstance.userprofileArrayList.clear();

								for (int i = 0; i < activityArray.length(); i++) {

									JSONObject activityObject = (JSONObject) activityArray
											.get(i);
									UserProfileListVO cVo = new UserProfileListVO();

//									if (!activityObject.isNull("id"))
//										cVo.id = activityObject.getString("id");

									if (!activityObject.isNull("username"))
										cVo.username = activityObject.getString("username");

									if (!activityObject.isNull("fname"))
										cVo.firstname = activityObject
												.getString("fname");
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
										cVo.phone = activityObject
												.getString("cellphone");
									if (!activityObject.isNull("email"))
										cVo.email = activityObject.getString("email");

									if (!activityObject.isNull("selet_timezone"))
										cVo.timezone = activityObject.getString("selet_timezone");
									if (!activityObject.isNull("upline"))
										cVo.ceo_evc = activityObject.getString("upline");

									if (!activityObject.isNull("upline_smd"))
										cVo.smd_emd = activityObject.getString("upline_smd");
									if (!activityObject.isNull("country"))
										cVo.country = activityObject.getString("country");

									if (!activityObject.isNull("profile_image"))
										cVo.profileimageUrl = activityObject.getString("profile_image");
									
									if (!activityObject.isNull("language"))
										cVo.language = activityObject.getString("language");
									if (!activityObject.isNull("licenses"))
										cVo.licenses = activityObject.getString("licenses");

									if (!activityObject.isNull("all_SA_MD_user"))
										cVo.uplineSA_MD = activityObject.getString("all_SA_MD_user");
									
									chatinstance.userprofileArrayList.add(cVo);

								}// end of for

							} else
								AppUtils.ShowAlertDialog(context, "ERROR: "
										+ responseText);

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				}// end of ascnctask UserProfileList

		
		
}//end of class
