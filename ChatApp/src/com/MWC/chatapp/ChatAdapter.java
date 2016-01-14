package com.MWC.chatapp;

import java.util.List;

import com.loopj.android.airbrake.AirbrakeNotifier;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import app.tabsample.SmartImageView.RoundSmartImageView;
import app.tabsample.SmartImageView.RoundedImageView;

public class ChatAdapter extends BaseAdapter {

	private final List<ChatMessageVo> chatMessages;
	private Activity context;
	boolean isattachment = false, ismessage = false;

	public ChatAdapter(Activity context, List<ChatMessageVo> chatMessages) {
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
	public ChatMessageVo getItem(int position) {
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
		ViewHolder holder;
		try {
		
		ChatMessageVo chatMessage = getItem(position);
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

		setAlignment(holder, myMsg);

		holder.txtMessage.setText(stripHtml(chatMessage.message));
		holder.profimage.setImageUrl(chatMessage.groupicon);

		if (chatMessage.attach_image.equalsIgnoreCase("")) {
			holder.attachmentimage.setVisibility(View.GONE);
		} else {
			holder.attachmentimage.setImageUrl(chatMessage.attach_image);
		}

		return convertView;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}
		return parent;
		
	}

	public void add(ChatMessageVo message) {
		chatMessages.add(message);
	}

	public void add(List<ChatMessageVo> messages) {
		chatMessages.addAll(messages);
	}

	private void setAlignment(ViewHolder holder, boolean isMe) {
		if (!isMe) {
			RelativeLayout.LayoutParams params1;
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content
					.getLayoutParams();
			// lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			holder.content.setLayoutParams(lp);

			params1 = (RelativeLayout.LayoutParams) holder.profimage
					.getLayoutParams();
			params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			// params1.gravity = Gravity.BOTTOM;
			holder.profimage.setLayoutParams(params1);
			
			if (ismessage)
				holder.contentWithBG.setVisibility(View.GONE);
			else {
				holder.contentWithBG
						.setBackgroundResource(R.drawable.frindchatimage);
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.contentWithBG
						.getLayoutParams();
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
				attachlayoutParams.leftMargin=50;
				attachlayoutParams.topMargin=20;
				
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

			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content
					.getLayoutParams();
			// lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			holder.content.setLayoutParams(lp);

			params1 = (RelativeLayout.LayoutParams) holder.profimage
					.getLayoutParams();
			params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			// params1.gravity = Gravity.BOTTOM;
			holder.profimage.setLayoutParams(params1);

			if (ismessage)
				holder.contentWithBG.setVisibility(View.GONE);
			else {
				holder.contentWithBG
						.setBackgroundResource(R.drawable.new_blue_chatimg);
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.contentWithBG
						.getLayoutParams();
				layoutParams.leftMargin=70;
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
				attachlayoutParams.rightMargin=50;
				attachlayoutParams.topMargin=20;
				
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
			// layoutParams.addRule(RelativeLayout.)
			holder.txtMessage.setLayoutParams(params2);

		}
	}

	private ViewHolder createViewHolder(View v) {
		ViewHolder holder = new ViewHolder();
		holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
		holder.profimage = (RoundedImageView) v.findViewById(R.id.myimage);
		holder.attachmentimage = (NormalSmartImageView) v
				.findViewById(R.id.AttachmentImage);
		holder.content = (RelativeLayout) v.findViewById(R.id.content);
		holder.contentWithBG = (RelativeLayout) v
				.findViewById(R.id.contentWithBackground);
		// holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
		return holder;
	}

	private static class ViewHolder {
		public TextView txtMessage;
		// public TextView txtInfo;
		public RoundedImageView profimage;
		public NormalSmartImageView attachmentimage;
		public RelativeLayout content;
		public RelativeLayout contentWithBG;
	}
	
	public String stripHtml(String html) {
	    return Html.fromHtml(html).toString();
	}
}