package com.MWC.chatapp;

import android.app.Activity;


public class SingletonActivity {
	public static SingletonActivity singletonactivity = null;
	static Activity mContext = null;

	public SingletonActivity(Activity context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public static SingletonActivity newInstance(Activity context) {
		if (singletonactivity == null) {
			singletonactivity = new SingletonActivity(context);
		} else {
			mContext = context;
		}
		return singletonactivity;
	}
}
