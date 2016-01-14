package com.MWC.chatapp;

import java.util.ArrayList;
import java.util.List;

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

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import app.tabsample.SmartImageView.RoundedImageView;
import applicationVo.AppUtils;

public class ChatUserAdapter extends BaseAdapter {

	private final List<ChatUserMessageVO> chatMessages;
	private Activity context;
	boolean isattachment = false, ismessage = false, ismatchup = false;
	public View view;
	SharedPreferences sharedPreferences;
	public String responseText = "",UserResponse="",message_id="";
	public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
	private ProgressDialog mProgressDialog;
	public RadioButton YESradioButton,NoradioButton,MAYBEradioButton;
	public int pos;
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS1:
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Please wait ...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();

			return mProgressDialog;

		default:
			return null;
		}
	}
	public ChatUserAdapter(Activity context,
			List<ChatUserMessageVO> chatMessages) {
		this.context = context;
		this.chatMessages = chatMessages;
		AirbrakeNotifier.register(context, "0fff7944c788113f27c1e4202afdf030");
	}

	@Override
	public int getCount() {
		if (chatMessages != null) {
			return chatMessages.size();
		} else {
			return 0;
		}
	}

	@Override
	public ChatUserMessageVO getItem(int position) {
		if (chatMessages != null) {
			return chatMessages.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {

			final ViewHolder holder;
		     final ChatUserMessageVO chatMessage= getItem(position);
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// if (convertView == null) {
			// holder = (ViewHolder) convertView.getTag();

			if (chatMessage.isMe) {
				convertView = vi.inflate(R.layout.list_item_chat_message, null);
				holder = createViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				convertView = vi.inflate(R.layout.frind_chat_messagelist, null);
				holder = createViewHolder(convertView);
				convertView.setTag(holder);
			} // end of inner else

			boolean myMsg = chatMessage.isMe;// Just a dummy check
			// to simulate whether it me or other sender

			if (chatMessage.attach_image.equalsIgnoreCase(""))
				isattachment = false;
			else
				isattachment = true;

			if (chatMessage.message.equalsIgnoreCase(""))
				ismessage = true;
			else
				ismessage = false;

			if (chatMessage.matchup_id.equalsIgnoreCase(""))
				ismatchup = false;
			else
				ismatchup = true;

			setAlignment(holder, myMsg);

			holder.txtMessage.setText(stripHtml(chatMessage.message));
			holder.profimage.setImageUrl(chatMessage.profileimage);

			if (chatMessage.attach_image.equalsIgnoreCase("")) {
				holder.attachmentimage.setVisibility(View.GONE);
			} else {
				holder.attachmentimage.setImageUrl(chatMessage.attach_image);
			}
			
			if(!chatMessage.req_response.equalsIgnoreCase(""))
			{
//				holder.MAYBEradioButton.setClickable(false);
//				holder.YESradioButton.setClickable(false);
//				holder.NoradioButton.setClickable(false);
				
				if(chatMessage.req_response.equalsIgnoreCase("YES"))
					holder.YESradioButton.setChecked(true);
				else if(chatMessage.req_response.equalsIgnoreCase("NO"))
					holder.NoradioButton.setChecked(true);
				else if(chatMessage.req_response.equalsIgnoreCase("MAYBE"))
					{
					holder.MAYBEradioButton.setChecked(true);
//					
//					holder.MAYBEradioButton.setClickable(true);
//					holder.YESradioButton.setClickable(true);
//					holder.NoradioButton.setClickable(true);
					}
			}
			
			holder.matchupradiogroup
			.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					RadioButton rb = (RadioButton) view
							.findViewById(checkedId);
					pos=position;
					UserResponse=rb.getText().toString();
					message_id=chatMessage.message_id;
					YESradioButton=holder.YESradioButton;
					NoradioButton=holder.NoradioButton;
					MAYBEradioButton=holder.MAYBEradioButton;
					
					new myTask_saveUserDetails_call().execute();
					
				}
			});

			// holder.txtInfo.setText(chatMessage.message_time);
			return convertView;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
		return convertView;
	}

	public void add(ChatUserMessageVO message) {
		chatMessages.add(message);
	}

	public void add(List<ChatUserMessageVO> messages) {
		chatMessages.addAll(messages);
	}

	private void setAlignment(ViewHolder holder, boolean isMe) {

		try {

			if (!isMe) {
				RelativeLayout.LayoutParams params1;
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams matchupparams = (RelativeLayout.LayoutParams) holder.matchupradiogroup
						.getLayoutParams();
				

				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content
						.getLayoutParams();
				// lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				holder.content.setLayoutParams(lp);

				params1 = (RelativeLayout.LayoutParams) holder.profimage
						.getLayoutParams();
				params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				// params1.gravity = Gravity.BOTTOM;
				
				if (ismatchup) {
					params1.height = 1;
					params1.width = 1;

					matchupparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
					matchupparams.addRule(RelativeLayout.CENTER_VERTICAL);
					holder.matchupradiogroup.setVisibility(View.VISIBLE);
					holder.matchupradiogroup.setLayoutParams(matchupparams);
				} else
					holder.matchupradiogroup.setVisibility(View.GONE);
				
				holder.profimage.setLayoutParams(params1);

				if (ismessage)
					holder.contentWithBG.setVisibility(View.GONE);
				else {
					holder.contentWithBG
							.setBackgroundResource(R.drawable.frindchatimage);
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.contentWithBG
							.getLayoutParams();
					if (ismatchup)
					{
						layoutParams.rightMargin = 6;
						holder.contentWithBG
						.setBackgroundResource(R.drawable.new_blue_chatimg);
					}
					else
						layoutParams.rightMargin = 40;
					
					layoutParams.addRule(RelativeLayout.RIGHT_OF,
							holder.profimage.getId());
					holder.contentWithBG.setLayoutParams(layoutParams);

				}

				if (isattachment) {
					RelativeLayout.LayoutParams attachlayoutParams = new RelativeLayout.LayoutParams(
							200, 200);
					attachlayoutParams.addRule(RelativeLayout.RIGHT_OF,
							holder.profimage.getId());
					attachlayoutParams.addRule(RelativeLayout.BELOW,
							holder.contentWithBG.getId());
					attachlayoutParams.leftMargin = 50;
					attachlayoutParams.topMargin = 20;
					holder.attachmentimage.setLayoutParams(attachlayoutParams);
					holder.attachmentimage.setVisibility(View.VISIBLE);
				} else {

					RelativeLayout.LayoutParams attachlayoutParams = (RelativeLayout.LayoutParams) holder.attachmentimage
							.getLayoutParams();
					attachlayoutParams.addRule(RelativeLayout.RIGHT_OF,
							holder.profimage.getId());
					attachlayoutParams.addRule(RelativeLayout.BELOW,
							holder.contentWithBG.getId());
					holder.attachmentimage.setLayoutParams(attachlayoutParams);
					holder.attachmentimage.setVisibility(View.INVISIBLE);
				}
				
				if(!isattachment&&ismessage)
					holder.profimage.setVisibility(View.GONE);

				params2 = (RelativeLayout.LayoutParams) holder.txtMessage
						.getLayoutParams();
				if (ismatchup)
					holder.txtMessage.setTextColor(Color.parseColor("#FFFFFF"));
				else
					holder.txtMessage.setTextColor(Color.parseColor("#000000"));
				// layoutParams.addRule(RelativeLayout.)
				holder.txtMessage.setLayoutParams(params2);
			}// Frind layout

			else {

				RelativeLayout.LayoutParams params1;
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams matchupparams = (RelativeLayout.LayoutParams) holder.matchupradiogroup
						.getLayoutParams();

				RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content
						.getLayoutParams();
				// lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				holder.content.setLayoutParams(lp);

				params1 = (RelativeLayout.LayoutParams) holder.profimage
						.getLayoutParams();
				params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

//				if (ismatchup) {
//					params1.height = 1;
//					params1.width = 1;
//
//					matchupparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//					matchupparams.addRule(RelativeLayout.CENTER_VERTICAL);
//					holder.matchupradiogroup.setVisibility(View.VISIBLE);
//					holder.matchupradiogroup.setLayoutParams(matchupparams);
//				} else
//					holder.matchupradiogroup.setVisibility(View.GONE);

				holder.profimage.setLayoutParams(params1);

				if (ismessage) {
					holder.contentWithBG.setVisibility(View.GONE);
					// holder.matchupradiogroup.setVisibility(View.GONE);
				} else {
					holder.contentWithBG
							.setBackgroundResource(R.drawable.new_blue_chatimg);
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.contentWithBG
							.getLayoutParams();
//					if (ismatchup)
//						layoutParams.leftMargin = 6;
//					else
//						layoutParams.leftMargin = 70;

					layoutParams.addRule(RelativeLayout.LEFT_OF,
							holder.profimage.getId());
					holder.contentWithBG.setLayoutParams(layoutParams);
				}

				if (isattachment) {
					RelativeLayout.LayoutParams attachlayoutParams = new RelativeLayout.LayoutParams(
							200, 200);
					attachlayoutParams.addRule(RelativeLayout.LEFT_OF,
							holder.profimage.getId());
					attachlayoutParams.addRule(RelativeLayout.BELOW,
							holder.contentWithBG.getId());
					attachlayoutParams.rightMargin = 50;
					attachlayoutParams.topMargin = 20;
					holder.attachmentimage.setLayoutParams(attachlayoutParams);
					holder.attachmentimage.setVisibility(View.VISIBLE);
				} else {

					RelativeLayout.LayoutParams attachlayoutParams = (RelativeLayout.LayoutParams) holder.attachmentimage
							.getLayoutParams();
					attachlayoutParams.addRule(RelativeLayout.LEFT_OF,
							holder.profimage.getId());
					attachlayoutParams.addRule(RelativeLayout.BELOW,
							holder.contentWithBG.getId());
					holder.attachmentimage.setLayoutParams(attachlayoutParams);
					holder.attachmentimage.setVisibility(View.INVISIBLE);
				}
				if(!isattachment&&ismessage)
					holder.profimage.setVisibility(View.GONE);

				params2 = (RelativeLayout.LayoutParams) holder.txtMessage
						.getLayoutParams();
				holder.txtMessage.setLayoutParams(params2);

			}// end of else

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
	}

	private ViewHolder createViewHolder(View v) {
		ViewHolder holder = new ViewHolder();
		view=v;
		holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
		holder.profimage = (RoundedImageView) v.findViewById(R.id.myimage);
		holder.attachmentimage = (NormalSmartImageView) v
				.findViewById(R.id.AttachmentImage);
		holder.content = (RelativeLayout) v.findViewById(R.id.content);
		holder.contentWithBG = (RelativeLayout) v
				.findViewById(R.id.contentWithBackground);
		holder.matchupradiogroup = (RadioGroup) v
				.findViewById(R.id.BubbleRadioGroup);
		holder.YESradioButton=(RadioButton) v.findViewById(R.id.YES_Radiobtn);
		holder.NoradioButton=(RadioButton) v.findViewById(R.id.No_Radiobtn);
		holder.MAYBEradioButton=(RadioButton) v.findViewById(R.id.MAYBE_Radiobtn);
		return holder;
	}
	private static class ViewHolder {
		public TextView txtMessage;
		// public TextView txtInfo;
		public RoundedImageView profimage;
		public NormalSmartImageView attachmentimage;
		public RelativeLayout content;
		public RelativeLayout contentWithBG;
		public RadioGroup matchupradiogroup;
		public RadioButton YESradioButton,NoradioButton,MAYBEradioButton;
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

					sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(context.getApplicationContext());
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
				
				nameValuePair.add(new BasicNameValuePair("sec_user",
						sharedPreferences.getString("username", "")));
				nameValuePair.add(new BasicNameValuePair("sec_pass",
						sharedPreferences.getString("password", "")));

				nameValuePair.add(new BasicNameValuePair("user_response",UserResponse));
				nameValuePair.add(new BasicNameValuePair("message_id",message_id));
				
				// Defined URL where to send data
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://www.bscpro.com/chat_api/matchupReqReponse");
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						"UTF-8"));
				HttpResponse response = httpclient.execute(httppost);
				responseText = EntityUtils.toString(response.getEntity());
				System.out.println(responseText + "response is display");
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

					responseText = jresponse.getString("message");
					String status = jresponse.getString("status");
					//office_id=jresponse.getString("officeId");

					if (status.equals("ok")) {
						
						if(chatMessages.size()>0)
						{
							chatMessages.get(pos).req_response=UserResponse;
						}
						
//						MAYBEradioButton.setClickable(false);
//						YESradioButton.setClickable(false);
//						NoradioButton.setClickable(false);
						
						if(UserResponse.equalsIgnoreCase("MAYBE"))
							{
							MAYBEradioButton.setChecked(true);
							
							MAYBEradioButton.setClickable(true);
							YESradioButton.setClickable(true);
							NoradioButton.setClickable(true);
							}
						AppUtils.ShowAlertDialog(context, responseText);
						
					} else {
						AppUtils.ShowAlertDialog(context,
								"Message :" + responseText);
					}
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					if (mProgressDialog != null)
						mProgressDialog.dismiss();
					AppUtils.ShowAlertDialog(context,
							"Oops, we encountered an error or the site may be down for maintenance.  Please try again in a few minutes.");

					e.printStackTrace();
					AirbrakeNotifier.notify(e);
				}
			}// end of onpost()
		}// ends of Async task

		public String stripHtml(String html) {
		    return Html.fromHtml(html).toString();
		}
}//end of class
