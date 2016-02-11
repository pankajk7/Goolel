package com.goolel;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.goolel.R;
import com.goolel.Entities.NotifyList;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		// displayMessage(context, "Your device registred with GCM");
		// Log.d("NAME", MainActivity.name);
		Intent intent = new Intent(Constants.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(Constants.EXTRA_MESSAGE, registrationId);
		context.sendBroadcast(intent);

		// String extraParameters = new DevicePreferences().getString(context,
		// Constants.PREF_USER_ID, "");
		//
		// ServerUtilities.register(context, extraParameters, "",
		// registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		// Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		// intent.putExtra(EXTRA_MESSAGE, registrationId);
		// context.sendBroadcast(intent);
		// ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("message");
		if (message != null) {
			String messagesJson = new DevicePreferences().getString(context,
					Constants.PREF_NOTIFICATION_MESSAGE, "");

			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();
			if (messagesJson.equalsIgnoreCase("")) {
				jsonObject.addProperty("notify_msg", message);
				jsonArray.add(jsonObject);
			} else {
				NotifyList[] messageArray = new Gson().fromJson(messagesJson,
						NotifyList[].class);
				ArrayList<NotifyList> arrayList = new ArrayList<NotifyList>(
						Arrays.asList(messageArray));
				arrayList.add(new NotifyList(message));
				jsonArray = new Gson().toJsonTree(arrayList).getAsJsonArray();
			}
			Log.d("Messages====>", jsonArray.toString());
			new DevicePreferences().addKey(context,
					Constants.PREF_NOTIFICATION_MESSAGE, jsonArray.toString());
			generateNotification(context, message);
		}
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		// String message = getString(R.string.gcm_deleted, total);
		// displayMessage(context, message);
		// // notifies user
		// generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.i(TAG, "Received recoverable error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_recoverable_error,
		// errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {
		// int icon = R.drawable.ic_launcher;
		// long when = System.currentTimeMillis();
		// NotificationManager notificationManager = (NotificationManager)
		// context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification notification = new Notification(icon, message, when);
		//
		// String title = context.getString(R.string.app_name);
		//
		Intent notificationIntent = new Intent(context, MainActivity.class);
		// // set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		// notification.setLatestEventInfo(context, title, message, intent);
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//
		// // Play default notification sound
		// notification.defaults |= Notification.DEFAULT_SOUND;
		//
		// // Vibrate if vibrate is enabled
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		// notificationManager.notify(0, notification);

		final boolean isKitKat = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
		final boolean isGingerBread = Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1;

		NotificationCompat.Builder notification;

		// soundFileName = soundFileName.substring(0,
		// soundFileName.lastIndexOf("."));
		Uri resourceUri;
		resourceUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		if (isKitKat) {
			if (isGingerBread) {
				notification = new NotificationCompat.Builder(context)
						.setContentTitle("Goolel").setContentText(message)
						.setContentIntent(pendingIntent)
						.setContentText(message).setAutoCancel(true)
						.setPriority(NotificationCompat.PRIORITY_HIGH);

			} else {
				notification = new NotificationCompat.Builder(context)
						.setContentTitle("Goolel")
						.setContentIntent(pendingIntent)
						.setContentText(message)
						.setStyle(
								new NotificationCompat.BigTextStyle()
										.bigText(message))
						.setSmallIcon(R.drawable.ic_launcher).setOngoing(true)
						.setAutoCancel(true);
			}

			notification.setSound(resourceUri);
			long[] pattern = { 500, 500, 500, 500 };
			notification.setVibrate(pattern);

			// boolean isVibrate = new DevicePreferences().getBoolean(context,
			// Constants.NOTIFICATION_VIBRATE, true);
			// boolean isSound = new DevicePreferences().getBoolean(context,
			// Constants.NOTIFICATION_SOUND, true);
			// if (isVibrate && isSound) {
			// // notification.setDefaults(Notification.DEFAULT_SOUND
			// // | Notification.DEFAULT_VIBRATE);
			//
			// } else if (isVibrate && !isSound) {
			// notification.setDefaults(Notification.DEFAULT_VIBRATE);
			// } else if (!isVibrate && isSound) {
			// // notification.setDefaults(Notification.DEFAULT_SOUND);
			// notification.setSound(resourceUri);
			// }
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(1, notification.build());
		} else {
			NotificationCompat.Builder notification1 = new NotificationCompat.Builder(
					context)
					.setCategory(NotificationCompat.CATEGORY_MESSAGE)
					.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Goolel")
					.setContentText(message)
					.setContentIntent(pendingIntent)
					.setStyle(
							new NotificationCompat.BigTextStyle()
									.bigText(message)).setAutoCancel(true);

			// notification.setAutoCancel(true);
			// boolean isVibrate = new DevicePreferences().getBoolean(context,
			// Constants.NOTIFICATION_VIBRATE, true);
			// boolean isSound = new DevicePreferences().getBoolean(context,
			// Constants.NOTIFICATION_SOUND, true);
			//
			// if (isVibrate && isSound) {
			// notification1.setDefaults(Notification.DEFAULT_SOUND
			// | Notification.DEFAULT_VIBRATE);
			// notification1.setSound(getNotificationUri(context,soundFileName));
			notification1.setSound(resourceUri);
			long[] pattern = { 500, 500, 500, 500 };
			notification1.setVibrate(pattern);
			// } else if (isVibrate && !isSound) {
			// notification1.setDefaults(Notification.DEFAULT_VIBRATE);
			// } else if (!isVibrate && isSound) {
			// notification1.setSound(resourceUri);
			// }
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(1, notification1.build());
		}

	}

}
