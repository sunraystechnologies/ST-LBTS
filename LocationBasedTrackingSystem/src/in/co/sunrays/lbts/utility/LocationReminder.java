package in.co.sunrays.lbts.utility;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationReminder extends BroadcastReceiver {

	private static int NOTIFICATION_ID = 1;
	private boolean flag;
	String taskName;
	String taskLocation;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (flag == false) {
			NotificationManager manger = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(
					R.drawable.ic_dialog_alert, "Remainder",
					System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(context,
					NOTIFICATION_ID,
					new Intent(context, LocationReminder.class), 0);
			Bundle extras = intent.getExtras();
			taskName = extras.getString("TaskName");
			taskLocation = extras.getString("TaskLocation");
			notification.setLatestEventInfo(context, "Your Task", taskName,
					contentIntent);
			// here we set the default sound for our notification
			// Intent notificationIntent = new Intent(context,
			// NotifyAlarmActivity.class);

			notification.flags = Notification.FLAG_AUTO_CANCEL;

			flag = true;

			if (flag == true) {

				String key = LocationManager.KEY_PROXIMITY_ENTERING;

				Boolean entering = intent.getBooleanExtra(key, false);

				if (entering) {
					Log.d(getClass().getSimpleName(), "entering");
				} else {
					Log.d(getClass().getSimpleName(), "exiting");
				}

				NotificationManager notificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);

				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, NOTIFICATION_ID, new Intent(context,
								LocationReminder.class), 0);

				Notification notification1 = createNotification();
				notification.setLatestEventInfo(context,
						"Location Reminder Alert!",
						"You are near your point of interest " + taskLocation,
						pendingIntent);

				notificationManager.notify(NOTIFICATION_ID, notification);
			}
		}

	}

	private Notification createNotification() {
		Notification notification = new Notification();

		notification.icon = R.drawable.ic_dialog_alert;
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;

		notification.ledARGB = Color.WHITE;
		notification.ledOnMS = 1500;
		notification.ledOffMS = 1500;

		return notification;
	}

}
