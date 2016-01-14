package app.tabsample.SmartImageView;

import android.content.Context;
import android.graphics.Bitmap;

public class BitmapImage implements SmartImage {
	private Bitmap bitmap;

	public BitmapImage(Bitmap bitmap) {
		this.bitmap = bitmap;
		bitmap = null;
	}

	public Bitmap getBitmap(Context context) {
		return bitmap;
	}
}