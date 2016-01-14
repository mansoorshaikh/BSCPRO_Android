package com.MWC.chatapp;

import java.util.ArrayList;

import com.loopj.android.airbrake.AirbrakeNotifier;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.tabsample.SmartImageView.NormalSmartImageView;
import applicationVo.MatchupBookVO;

public class MatchbookAdapter extends ArrayAdapter<MatchupBookVO> {
	private final Activity context;
	// View rowview;

	public final ArrayList<MatchupBookVO> values;

	public MatchbookAdapter(Activity context,
			ArrayList<MatchupBookVO> values) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.matchupbooklist_xml, values);

		this.context = context;
		this.values = values;
		AirbrakeNotifier.register(context, "0fff7944c788113f27c1e4202afdf030");
	}

	public View getView(int position, View view, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		View rowview = inflater
				.inflate(R.layout.matchupbooklist_xml, parent, false);

		MatchupBookVO matchvo = values.get(position);

		try {

	  MyTextView submitted_matchup_date=(MyTextView)rowview.findViewById(R.id.MatchInputtextview0);
	  MyTextView counttextview=(MyTextView)rowview.findViewById(R.id.MatchInputtextview1);
	  MyTextView sentfrom=(MyTextView)rowview.findViewById(R.id.MatchInputtextview2);
	  MyTextView day=(MyTextView)rowview.findViewById(R.id.MatchInputtextview3);
	  MyTextView date=(MyTextView)rowview.findViewById(R.id.MatchInputtextview4);
	  MyTextView time=(MyTextView)rowview.findViewById(R.id.MatchInputtextview5);
	  MyTextView place=(MyTextView)rowview.findViewById(R.id.MatchInputtextview6);
	  MyTextView matchupname=(MyTextView)rowview.findViewById(R.id.MatchInputtextview7);
			
	  MyTextView contact=(MyTextView)rowview.findViewById(R.id.MatchInputtextview8);
	  MyTextView profile=(MyTextView)rowview.findViewById(R.id.MatchInputtextview9);
	  MyTextView trainee=(MyTextView)rowview.findViewById(R.id.MatchInputtextview10);
	  MyTextView traineecontact=(MyTextView)rowview.findViewById(R.id.MatchInputtextview11);
	  MyTextView appttype=(MyTextView)rowview.findViewById(R.id.MatchInputtextview12);
//	  MyTextView place=(MyTextView)rowview.findViewById(R.id.MatchInputtextview6);
//	  MyTextView matchupname=(MyTextView)rowview.findViewById(R.id.MatchInputtextview7);	
	  
	  
			submitted_matchup_date.setText(matchvo.submitted_matchup_date);
			counttextview.setText(""+(position+1));
			sentfrom.setText(matchvo.user_id);
			day.setText(matchvo.matchday);
			date.setText(matchvo.date_match);
			time.setText(matchvo.match_time);
			place.setText(matchvo.place_match);
			matchupname.setText(matchvo.match_name);
			
			contact.setText(matchvo.contact_match);
			profile.setText(matchvo.match_profile);
			trainee.setText(matchvo.match_trainee);
			traineecontact.setText(matchvo.trainee_ph);
			appttype.setText(matchvo.appttype);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			AirbrakeNotifier.notify(e);
		}

		return rowview;

	}

}