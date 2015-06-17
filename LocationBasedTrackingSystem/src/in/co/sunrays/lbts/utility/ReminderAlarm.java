package in.co.sunrays.lbts.utility;

import in.co.sunrays.lbts.ctl.NotifyAlarmCtl;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ReminderAlarm extends BroadcastReceiver {

	private static int NOTIFICATION_ID = 1;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_dialog_alert, "Remainder",System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context,NOTIFICATION_ID, new Intent(context, ReminderAlarm.class), 0);
		Bundle extras = intent.getExtras();
		String title = extras.getString("TaskName");
		notification.setLatestEventInfo(context, "Your Task", title,contentIntent);
		// here we set the default sound for our notification
		notification.defaults |= Notification.DEFAULT_SOUND;
		Toast.makeText(context, "New Notification Received", Toast.LENGTH_LONG)
				.show();
		//Intent notificationIntent = new Intent(context, NotifyAlarmActivity.class);

		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent i = new Intent(context, NotifyAlarmCtl.class);
		i.putExtra("TaskName", title);

		System.out.println("NOTIFICATION_ID========================"
				+ NOTIFICATION_ID);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		// The PendingIntent to launch our activity if the user selects this
		// notification
		manger.notify(NOTIFICATION_ID, notification);

	}

}
