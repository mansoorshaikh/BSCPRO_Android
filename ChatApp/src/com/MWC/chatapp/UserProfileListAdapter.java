package com.MWC.chatapp;

import java.util.ArrayList;

import com.loopj.android.airbrake.AirbrakeNotifier;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;

public class UserProfileListAdapter extends ArrayAdapter<ChatGroupVO> {

	private final Activity context;
	// View rowview;

	public final ArrayList<ChatGroupVO> values;

	public UserProfileListAdapter(Activity context,
			ArrayList<ChatGroupVO> values) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.userprofilelist, values);

		this.context = context;
		this.values = values;
		AirbrakeNotifier.register(context, "0fff7944c788113f27c1e4202afdf030");
	}

	public View getView(int position, View view, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		View rowview = inflater
				.inflate(R.layout.userprofilelist, parent, false);

		ChatGroupVO chat = values.get(position);

		try {

			if (chat.isGroup.equalsIgnoreCase("true")) {
				NormalSmartImageView userimage = (NormalSmartImageView) rowview
						.findViewById(R.id.profileimage);
				userimage.setImageUrl(chat.groupIcon);

				TextView profname = (TextView) rowview
						.findViewById(R.id.profilename);
				Typeface type = Typeface.createFromAsset(context.getAssets(),
						"fonts/calibribold.ttf");
				profname.setTypeface(type);
				profname.setText(chat.groupName);

			}// end of if
			else {
				NormalSmartImageView userimage = (NormalSmartImageView) rowview
						.findViewById(R.id.profileimage);
				userimage.setImageUrl(chat.profileImage);

				TextView profname = (TextView) rowview
						.findViewById(R.id.profilename);
				Typeface type = Typeface.createFromAsset(context.getAssets(),
						"fonts/calibribold.ttf");
				profname.setTypeface(type);
				profname.setText(chat.username);
			}// end of if

			String msgline = chat.message;
			TextView profmessage = (TextView) rowview
					.findViewById(R.id.profilemessage);
			Typeface type2 = Typeface.createFromAsset(context.getAssets(),
					"fonts/calibri.ttf");
			profmessage.setTypeface(type2);
			msgline = msgline.replaceAll("[\\\t|\\\n|\\\r]", " ");
			// String upToNCharacters = msgline.substring(0,
			// Math.min(msgline.length(), 30));
			profmessage.setText(msgline);

			TextView proftime = (TextView) rowview.findViewById(R.id.proftime);
			proftime.setText(chat.msg_time);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

		return rowview;

	}

}
