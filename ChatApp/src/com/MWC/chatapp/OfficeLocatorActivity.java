package com.MWC.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import app.tabsample.SmartImageView.NormalSmartImageView;

public class OfficeLocatorActivity extends Activity {

	RelativeLayout Chatlayout, layoutProfile, LayoutRecruit, LayoutBusiness,
	LayoutGuest, LayoutMatchup, LayoutLocator, LayoutLogout,LayoutBPM_Check_in,menulayout;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.officelocator);

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
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		NormalSmartImageView profileimageview=(NormalSmartImageView)findViewById(R.id.userimage);
		
		try {
			profileimageview.setImageUrl(sharedPreferences.getString("profileImage", ""));	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void onMenuOptionClicked(View view) {
		if (view.getId() == R.id.chatlayout) {
			MENU_ITEM_SELECTED = "CHAT";
			Intent intent = new Intent(OfficeLocatorActivity.this,
					UserProfileActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.layoutprofile) {
//			MENU_ITEM_SELECTED = "PROFILE";
//			Intent intent = new Intent(OfficeLocatorActivity.this,
//					ProfileActivity.class);
//			startActivity(intent);
//		} 
		else if (view.getId() == R.id.Layoutrecruit) {
			MENU_ITEM_SELECTED = "ADD NEW RECRUIT";
			Intent intent = new Intent(OfficeLocatorActivity.this,
					NewRecruitActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutbusiness) {
			MENU_ITEM_SELECTED = "ADD BUSINESS";
			Intent intent = new Intent(OfficeLocatorActivity.this,
					AddBusinessActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutguest) {
			MENU_ITEM_SELECTED = "ADD NEW GUEST";
			Intent intent = new Intent(OfficeLocatorActivity.this,
					AddNewGuestActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.Layoutmatchup) {
			MENU_ITEM_SELECTED = "SUBMIT MATCH UP";
			Intent intent = new Intent(OfficeLocatorActivity.this,
					MatchupActivity.class);
			startActivity(intent);
		} 
//		else if (view.getId() == R.id.Layoutlocator) {
//			MENU_ITEM_SELECTED = "OFFICE LOCATOR";
//			new CollapseAnimation(slidingPanel, panelWidth,
//					TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
//					TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
//		} 
		else if (view.getId() == R.id.Layoutlogout) {
			MENU_ITEM_SELECTED = "LOGOUT";
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences
					.edit();
			editor.putString("userid", "");
			editor.putString("password", "");
			editor.putString("username", "");
			editor.putString("profileImage","");
			editor.commit();
			Intent intent = new Intent(OfficeLocatorActivity.this,
					LoginActivity.class);
			startActivity(intent);

		}else if (view.getId() == R.id.LayoutBPM_Check_In) {
			MENU_ITEM_SELECTED = "BPM_Check_In";
			Intent intent = new Intent(OfficeLocatorActivity .this,
					BPM_Ckeck_In_Activity.class);
			startActivity(intent);

		}
	}

}
