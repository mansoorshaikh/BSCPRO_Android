package com.MWC.chatapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	private Controller aController = null;
	private static final int NOTIFY_ME_ID = 5476;
	public static int count = 0;

	public GCMIntentService() {

		// Call extended class Constructor GCMBaseIntentService
		super(GCMConfig.GOOGLE_SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {

		try {
			// Get Global Controller Class object (see application tag in
			// AndroidManifest.xml)
			if (aController == null)
				aController = (Controller) getApplicationContext();

			LoginActivity.GCMRegister_Id = registrationId;

			Log.i(TAG, "Device registered: regId = " + registrationId);
			aController.displayMessageOnScreen(context,
					"Your device registred with GCM");
			Log.d("NAME", "**********USER NAME");
			aController.register(context, "", "", registrationId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Method called on device unregistred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if (aController == null)
			aController = (Controller) getApplicationContext();
		Log.i(TAG, "Device unregistered");
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_unregistered));
		aController.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message from GCM server
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("message"); // *****************************
																	// GCM
																	// INTENT

		aController.displayMessageOnScreen(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		aController.displayMessageOnScreen(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received error: " + errorId);
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {

		try {
			if (aController == null)
				aController = (Controller) getApplicationContext();

			// log message
			Log.i(TAG, "Received recoverable error: " + errorId);
			aController.displayMessageOnScreen(context,
					getString(R.string.gcm_recoverable_error, errorId));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Create a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {

		try {

//			int icon = R.drawable.ic_launcher;
//			long when = System.currentTimeMillis();
//			NotificationManager notificationManager = (NotificationManager) context
//					.getSystemService(Context.NOTIFICATION_SERVICE);
//			Notification notification = new Notification(icon, message, when);

			String title = context.getString(R.string.app_name);
			SharedPreferences sharedPreferences;
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context
							.getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("NOTIFICATIONSTRING", "TRUE");
			editor.commit();

//			Intent notificationIntent = new Intent(context,
//					UserProfileActivity.class); // *****************************************
//			// set intent so it does not start a new activity
////			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
////					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			notification.number += count;
//			
//			PendingIntent intent = PendingIntent.getActivity(context, 0,
//					notificationIntent, 0);
//			notification.setLatestEventInfo(context, title,"\nyou have pending "+notification.number+" Notifications", intent);
//
//			
//
//			notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//			// Play default notification sound
//			notification.defaults |= Notification.DEFAULT_SOUND;
//
//			// notification.sound = Uri.parse("android.resource://" +
//			// context.getPackageName() + "your_sound_file_name.mp3");
//
//			// Vibrate if vibrate is enabled
//			notification.defaults |= Notification.DEFAULT_VIBRATE;
//			notification.ledARGB = 0xff0000ff;
//			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//
//			notificationManager.notify(NOTIFY_ME_ID, notification);

			// //*******************************************************************************
//			Notification n = new Notification(R.drawable.ic_launcher, message,System.currentTimeMillis());
//			PendingIntent i = PendingIntent.getActivity(context, 0, new Intent(
//					context, UserProfileActivity.class), 0);
//			
//			
//			BadgeUtils.setBadge(context, n.number);
//			
//			n.setLatestEventInfo(context.getApplicationContext(),
//					title, message, i);
//			n.number = ++count;
//			n.flags |= Notification.FLAG_AUTO_CANCEL;
//			n.defaults |= Notification.DEFAULT_SOUND;
//			n.flags |= Notification.DEFAULT_VIBRATE;
//			n.ledARGB = 0xff0000ff;
//			n.flags |= Notification.FLAG_SHOW_LIGHTS;
//			
//			
//
//			// Now invoke the Notification Service
//			String notifService = Context.NOTIFICATION_SERVICE;
//			NotificationManager mgr = (NotificationManager)context.getSystemService(notifService);
//			mgr.notify(NOTIFY_ME_ID, n);
			// //*******************************************************************************
			int icon = R.drawable.ic_launcher;
			 
			int mNotificationId = 001;
			 
			PendingIntent resultPendingIntent =
			        PendingIntent.getActivity(
			                context,
			                0,
			                new Intent(
			    				context, UserProfileActivity.class),
			                PendingIntent.FLAG_CANCEL_CURRENT
			        );
			 
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);
			Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
			        .setAutoCancel(true)
			        .setContentTitle(title)
			        .setNumber(++count)
			        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
			        .setSubText("\n "+count+" new messages\n")
			        .setContentIntent(resultPendingIntent)
			        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
			        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
			        .setContentText(message).build();
			 
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(mNotificationId, notification);
			// //*******************************************************************************
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
