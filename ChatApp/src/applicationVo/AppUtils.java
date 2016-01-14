package applicationVo;

import com.MWC.chatapp.LoginActivity;
import com.MWC.chatapp.NewRecruitActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class AppUtils {

	
	// For Checking the Internet
		public static boolean isNetworkAvailable(Context context) {
			// Get Connectivity Manager class object from Systems Service
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			// Get Network Info from connectivity Manager
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();

			// if no network is available networkInfo will be null
			// otherwise check if we are connected
			if (networkInfo != null && networkInfo.isConnected()) {
				return true;
			}
			return false;
		}

	
	public static void ShowAlertDialog(Context context,String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		builder.setTitle("BSCPRO");
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								// do things
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
