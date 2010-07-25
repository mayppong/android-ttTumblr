package com.tacticalnuclearstrike.tttumblr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tacticalnuclearstrike.tttumblr.activites.AccountActivity;
import com.tacticalnuclearstrike.tttumblr.activites.Dashboard;
import com.tacticalnuclearstrike.tttumblr.activites.PostConversationActivity;
import com.tacticalnuclearstrike.tttumblr.activites.PostLinkActivity;
import com.tacticalnuclearstrike.tttumblr.activites.PostQuoteActivity;
import com.tacticalnuclearstrike.tttumblr.activites.PostTextActivity;
import com.tacticalnuclearstrike.tttumblr.activites.Preferences;
import com.tacticalnuclearstrike.tttumblr.activites.UploadImageActivity;
import com.tacticalnuclearstrike.tttumblr.activites.UploadVideoActivity;

public class MainActivity extends Activity {

	final int MENU_ACCOUNT = 1;
	final int MENU_ABOUT = 2;
	final int MENU_SETTINGS = 3;
	
	GoogleAnalyticsTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start("UA-9100060-3", 20, this);
		regularStartup();
		
		if(showDashBoard())
			startDashboardActivity();
	}

	private void regularStartup() {
		setContentView(R.layout.main);

		setupButtons();

		CheckIsUserNameAndPasswordCorrect();
	}
	
	private boolean showDashBoard()
	{
		return getSharePreferences().getBoolean("DASHBOARD_STARTUP", false);
	}
	
	private SharedPreferences getSharePreferences() {
		SharedPreferences settings = this.getSharedPreferences("tumblr", 0);
		return settings;
	}

	private void setupButtons() {
		findViewById(R.id.postTextBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent startPostText = new Intent(MainActivity.this,
								PostTextActivity.class);
						startActivity(startPostText);
						tracker.trackPageView("/PostTextActivity");
					}
				});

		findViewById(R.id.postImageBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								UploadImageActivity.class);
						startActivity(intent);
						tracker.trackPageView("/UploadImageActivity");
					}
				});

		findViewById(R.id.postVideoBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								UploadVideoActivity.class);
						startActivity(intent);
						tracker.trackPageView("/UploadVideoActivity");
					}
				});

		findViewById(R.id.postQuoteBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								PostQuoteActivity.class);
						startActivity(intent);
						tracker.trackPageView("/PostQuoteActivity");
					}
				});

		findViewById(R.id.postLinkBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								PostLinkActivity.class);
						startActivity(intent);
						tracker.trackPageView("/PostLinkActivity");
					}
				});

		findViewById(R.id.postConversationBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								PostConversationActivity.class);
						startActivity(intent);
						tracker.trackPageView("/PostConversationActivity");
					}
				});

		findViewById(R.id.dashboardBtn).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startDashboardActivity();
						tracker.trackPageView("/DashboardActivity");
					}
				});
	}
	
	@Override
	  protected void onDestroy() {
	    super.onDestroy();
	    tracker.stop();
	  }

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_account:
			startActivityForResult(new Intent(MainActivity.this,
					AccountActivity.class), 0);
			tracker.trackPageView("/AccountActivity");
			return true;
		case R.id.menu_about:
			createAboutDialog();
			tracker.trackPageView("/AboutDialog");
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(MainActivity.this, Preferences.class));
			tracker.trackPageView("/Preferences");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		CheckIsUserNameAndPasswordCorrect();
	}

	private void createAboutDialog() {
		PackageManager pm = getPackageManager();
		String version = "r0";
		try {
			PackageInfo pi = pm.getPackageInfo(
					"com.tacticalnuclearstrike.tttumblr", 0);
			version = pi.versionName;
		} catch (NameNotFoundException e) {

		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
				.setMessage(
						"ttTumblr "
								+ version
								+ "\n\nIf you find any errors please contact me so that I can fix them.")
				.setCancelable(true).setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void CheckIsUserNameAndPasswordCorrect() {
		TextView infoView = (TextView) findViewById(R.id.labelAuthStatus);

		TumblrApi api = new TumblrApi(this);
		if (!api.isUserNameAndPasswordStored()) {
			infoView
					.setText("Please press menu and select Account to enter email and password.");
			infoView.setVisibility(View.VISIBLE);
		} else {
			infoView.setVisibility(View.GONE);
		}
	}

	private void startDashboardActivity() {
		startActivity(new Intent(MainActivity.this,
				Dashboard.class));
	}
}
