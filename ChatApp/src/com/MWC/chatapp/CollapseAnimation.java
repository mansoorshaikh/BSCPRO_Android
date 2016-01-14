/*  Created by Edward Akoto on 12/31/12.
 *  Email akotoe@aua.ac.ke
 * 	Free for modification and distribution
 */

package com.MWC.chatapp;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.FrameLayout.LayoutParams;

public class CollapseAnimation extends TranslateAnimation implements
		TranslateAnimation.AnimationListener {

	private RelativeLayout slidingLayout,menulayout;
	int panelWidth;

	public CollapseAnimation(RelativeLayout layout, int width, int fromXType,
			float fromXValue, int toXType, float toXValue, int fromYType,
			float fromYValue, int toYType, float toYValue,RelativeLayout menulayout) {

		super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue,
				toYType, toYValue);

		// Initialize
		this.menulayout=menulayout;
		slidingLayout = layout;
		panelWidth = width;
		setDuration(400);
		setFillAfter(false);
		setInterpolator(new AccelerateDecelerateInterpolator());
		setAnimationListener(this);

		// Clear left and right margins
		LayoutParams params = (LayoutParams) slidingLayout.getLayoutParams();
		params.rightMargin = 0;
		params.leftMargin = 0;
		slidingLayout.setLayoutParams(params);
		slidingLayout.requestLayout();
		slidingLayout.startAnimation(this);

	}

	public void onAnimationEnd(Animation animation) {
		menulayout.setVisibility(View.GONE);
	}

	public void onAnimationRepeat(Animation animation) {

	}

	public void onAnimationStart(Animation animation) {

	}

}
