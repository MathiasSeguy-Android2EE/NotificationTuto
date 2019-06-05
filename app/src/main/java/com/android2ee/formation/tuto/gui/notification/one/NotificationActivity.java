/**<ul>
 * <li>NotificationTuto</li>
 * <li>com.android2ee.formation.tuto.gui.notification.one</li>
 * <li>15 oct. 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.formation.tuto.gui.notification.one;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.view.Menu;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to show the code to use to display notifications depending on the version
 *        of the Os that runs the application
 */
public class NotificationActivity extends Activity {
	/**
	 * The ticker message
	 */
	private String tickerMessage;
	/**
	 * The summary and the LongString
	 */
	String contentText, veryLongString, title;
	/**
	 * A bitmap to be displayed in the notification
	 */
	Bitmap aBitmap;
	/**
	 * The notification Manager
	 */
	NotificationManager notifManager;
	/**
	 * The constant used to displays and identify the notification
	 */
	private final int simpleNotifPreJB = 0, vibrateNotifPreJB = 1, pendingIntentNotifPreJB = 2;
	/**
	 * The constant used to displays and identify the notification for JellyBean
	 */
	private final int simpleNotifPostJB = 10, notifPostJBBigPict = 11, notifPostJBBigText = 12, notifPostJBInbox = 13;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// build the view
		setContentView(R.layout.activity_notification);
		// instanciate the notificationManager
		notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Retrieve the strings and the bitmap to use
		contentText = getString(R.string.summary);
		veryLongString = getString(R.string.very_long_string);
		tickerMessage = getString(R.string.ticker);
		title = getString(R.string.title);
		aBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.starwars_logo);
		// now build the notification depending on the version of the system
		// find the system version
		boolean preHC = getResources().getBoolean(R.bool.preHC);
		boolean HC = getResources().getBoolean(R.bool.HoneyComb);
		// and display the notification
		if (preHC) {
			// build a legacy notification
			buildLegacyNotification();
		} else if (HC) {
			// use compatLibrairy
			buildSimpleNotificationPreJB();
			// using vibration and sound
			buildSoundVibarteNotificationPreJB();
			// using a pending intent to wake up a new activity
			buildPendingIntentNotificationPreJB();
		} else {
			// use native JellyBean Librairy
			buildSimpleNotificationPostJB();
			// build an Inbox notification
			buildInboxNotificationPostJB();
			// build a BigPicture notification
			buildPictureNotificationPostJB();
			// build a BigText notification
			buildBigTextNotificationPostJB();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// default methods inherited from Android New Project Wizard
		// Don't care about it
		getMenuInflater().inflate(R.menu.activity_notification, menu);
		return true;
	}

	/******************************************************************************************/
	/** PRE_HoneyComb Notification **************************************************************************/
	/******************************************************************************************/

	/**
	 * Display a legacy notification
	 */
	public void buildLegacyNotification() {
		// Creation of the notification with the specified notification icon and text
		// That appears at the creation of the notification
		final Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
		// Defining the redirect when you click on the notification. In our case the
		// Redirect to our other activity
		final PendingIntent pendingIntent = PendingIntent
				.getActivity(this, 0, new Intent(this, OtherActivity.class), 0);
		// Notification & Vibration
		notification.setLatestEventInfo(this, title, contentText, pendingIntent);
		notification.vibrate = new long[] { 0, 200, 100, 200, 100, 200 };
		// Set the ticker to be disaplayed when the notification is created
		notification.tickerText = tickerMessage;
		// and display it
		notifManager.notify(simpleNotifPreJB, notification);
	}

	/******************************************************************************************/
	/** PRE_JB Notification **************************************************************************/
	/******************************************************************************************/
	/**
	 * Build a simple notification for preJellyBean devices
	 */
	public void buildSimpleNotificationPreJB() {
		// define the notification's builder
		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this).setContentTitle(title)
				.setContentText(contentText).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(aBitmap)
				.setAutoCancel(true).setTicker(tickerMessage);
		// set this notification as a BigText notification
		Notification notif = new NotificationCompat.BigTextStyle(nBuilder).bigText(veryLongString).build();
		// And display it : Be sure to set an unique identifier
		notifManager.notify(simpleNotifPreJB, notif);

	}

	/**
	 * Build a notification with sound and vibration for preJellyBean devices
	 * To vibrate don't forget to declare the permission in the manifest
	 */
	public void buildSoundVibarteNotificationPreJB() {
		// define the Uri od the default ringtone for notification
		Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		// define the vibration n*[vibration time,pause_time] in ms
		// here it's a five seconds vibration : v p v p v
		long[] vibrate = { 1000, 1000, 1000, 1000, 1000 };
		// define the notification's builder
		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this).setContentTitle(title)
				.setContentText(contentText).setSmallIcon(R.drawable.ic_notif_vibrate).setLargeIcon(aBitmap)
				.setAutoCancel(true).setSound(ringUri).setVibrate(vibrate).setAutoCancel(true).setTicker(tickerMessage);
		// set this notification as a BigPicture notification
		Notification notif = new NotificationCompat.BigPictureStyle(nBuilder).bigPicture(aBitmap).build();
		// then display the notification
		notifManager.notify(vibrateNotifPreJB, notif);
	}

	/**
	 * Build a notification when it's clicked launch an intent
	 */
	public void buildPendingIntentNotificationPreJB() {
		// Define the Intent that will be sent by the system
		Intent intentClick1 = new Intent(this, OtherActivity.class);
		// Add element to the intent
		intentClick1.putExtra(getString(R.string.intent_extra_name), pendingIntentNotifPreJB);
		// Create the PendingIntent to be sent.
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentClick1, 0);
		// define the notification's builder
		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this).setContentTitle(title)
				.setContentText(contentText).setSmallIcon(R.drawable.ic_pending_intent_notif).setLargeIcon(aBitmap)
				.setContentIntent(pendingIntent).setAutoCancel(true).setTicker(tickerMessage);
		// set this notification as a InboxStyle notification
		Notification notif = new NotificationCompat.InboxStyle(nBuilder).setBigContentTitle("Hello big content title")
				.build();
		// then display the notification
		notifManager.notify(pendingIntentNotifPreJB, notif);

	}

	/******************************************************************************************/
	/** POST_JB Notification **************************************************************************/
	/******************************************************************************************/
	/**
	 * For post JellyBean devices
	 */
	@SuppressLint("NewApi")
	public void buildSimpleNotificationPostJB() {

		// crab from http://capdroid.wordpress.com/2012/07/15/android-4-1-notification-tutorial/
		// Chitranshu Asthana

		// Get the builder to create notification.
		Builder builder = new Notification.Builder(this);
		// Set the first line of text in the platform notification template.
		builder.setContentTitle(title);
		// Set the second line of text in the platform notification
		// template.
		builder.setContentText(contentText);
		// Set the third line of text in the platform notification template.
		// Don't use if you're also using setProgress(int, int, boolean);
		// they occupy the same location in the standard template.
		builder.setSubText("Sub Text");
		// Set the large number at the right-hand side of the notification.
		// This is equivalent to setContentInfo, although it might show the
		// number in a different font size for readability.
		builder.setNumber(100);
		// Set the "ticker" text which is displayed in the status bar when
		// the notification first arrives.
		builder.setTicker(tickerMessage);
		// Set the small icon resource, which will be used to represent the
		// notification in the status bar. The platform template for the
		// expanded view will draw this icon in the left, unless a large
		// icon has also been specified, in which case the small icon will
		// be moved to the right-hand side.
		builder.setSmallIcon(R.drawable.ic_launcher_green);
		// Add a large icon to the notification (and the ticker on some
		// devices). In the platform template, this image will be shown on
		// the left of the notification view in place of the small icon
		// (which will move to the right side).
		builder.setLargeIcon(aBitmap);
//		builder.setPriority(Notification.PRIORITY_MAX);//==2
		builder.setPriority(Notification.PRIORITY_HIGH);//==1
//		builder.setPriority(Notification.PRIORITY_DEFAULT);//==0
//		builder.setPriority(Notification.PRIORITY_LOW);//==-1
		//icon is not shown in the status bar
//		builder.setPriority(Notification.PRIORITY_MIN);//==-2

		Notification noti = builder.build();
		// then display the notification
		notifManager.notify(simpleNotifPostJB, noti);
	}

	/**
	 * For post JellyBean devices
	 * style:BigText
	 */
	@SuppressLint("NewApi")
	public void buildBigTextNotificationPostJB() {
		Notification noti = new Notification.Builder(this).setContentTitle(title + " Big text Style")
				.setContentText(contentText).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(aBitmap)
				.setTicker(tickerMessage).setStyle(new Notification.BigTextStyle().bigText(veryLongString)).build();
		// then display the notification
		notifManager.notify(notifPostJBBigText, noti);
	}

	/**
	 * For post JellyBean devices
	 * style:BigPicture
	 */
	@SuppressLint("NewApi")
	public void buildPictureNotificationPostJB() {
		Notification noti = new Notification.Builder(this).setContentTitle(title + " Big Picture style")
				.setContentText(contentText).setSmallIcon(R.drawable.ic_launcher)
				.setStyle(new Notification.BigPictureStyle().bigPicture(aBitmap)).setTicker(tickerMessage).build();
		// then display the notification
		notifManager.notify(notifPostJBBigPict, noti);
	}

	/**
	 * For post JellyBean devices
	 * style:Inbox
	 */
	@SuppressLint("NewApi")
	public void buildInboxNotificationPostJB() {
		Notification noti = new Notification.Builder(this)
				.setContentTitle(title + " Inbox style")
				.setContentText(contentText)
				.setTicker(tickerMessage)
				.setSmallIcon(R.drawable.ic_launcher)
				.setStyle(
						new Notification.InboxStyle().addLine("Line1").addLine("Line2").setSummaryText(veryLongString))

				.build();
		// then display the notification
		notifManager.notify(notifPostJBInbox, noti);
	}
}
