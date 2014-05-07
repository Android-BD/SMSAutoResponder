package com.ridgway.smsautoresponder;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MainActivity extends ActionBarActivity {
	
	public static final String PREFS_NAME = "SMS_SharedPrefs";
	
	String strDrive = "";
	String strBike = "";
	String strRun = "";
	String strHike = "";
	String strDefaultActivity = "";
	String returnMessage = "";
	
	int responsesSent = 0;
	boolean receiverRegistered = false;
	boolean googlePlayAvailable = false;
	boolean googleDialogShown = false;
	int mNotificationId = 42; // Responses Sent Notification Id
	

	IntentFilter intentFilter;
    private BroadcastReceiver intentReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
        	String smsNumber = intent.getExtras().getString("sms_number");
            
        	// Return a response
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(smsNumber, null, returnMessage, null, null);        
            responsesSent++;
            
            createNotification();
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		
		String defaultDrive = getResources().getString(R.string.response_driving);
		strDrive = sharedPref.getString(getString(R.string.saved_response_driving), defaultDrive);
		
		String defaultBike = getResources().getString(R.string.response_cycling);
		strBike = sharedPref.getString(getString(R.string.saved_response_biking), defaultBike);
		
		String defaultRun = getResources().getString(R.string.response_running);
		strRun = sharedPref.getString(getString(R.string.saved_response_running), defaultRun);
		
		String defaultHike = getResources().getString(R.string.response_hiking);
		strHike = sharedPref.getString(getString(R.string.saved_response_hiking), defaultHike);

		String defaultActivity = getResources().getString(R.string.default_activity);
		strDefaultActivity = sharedPref.getString(getString(R.string.saved_activity_option), defaultActivity);

		Spinner spinActivity = (Spinner) findViewById(R.id.spinner1);
		
		// Setup an listener on the spinner, so we can update the response when the user makes
		// a change to their selected activity.
		spinActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
		    	 updateResponse();
		    } 

		    public void onNothingSelected(AdapterView<?> adapterView) {
		        return;
		    } 
		});		

		SelectSpinnerItemByValue(spinActivity, strDefaultActivity);

		//---intent to filter for SMS messages received---
		intentFilter = new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");
		
		Button btnStart = (Button) findViewById(R.id.btnStart);
		Button btnStop = (Button) findViewById(R.id.btnStop);
		
		// Enable Start button, disable Stop button
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		
		updateResponseCount();

	    // Look up the AdView as a resource and load a request.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder()
								    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
								    .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4") // My Galaxy Nexus test phone
								    .build();
	    adView.loadAd(adRequest);
	}

	@Override
	protected void onPause(){
        super.onPause();
        saveSpinner();
	}

	@Override
	protected void onResume(){
        super.onResume();

    	googlePlayAvailable = false;

    	Context context = getApplicationContext();
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS){
        	googlePlayAvailable = true;
        	googleDialogShown = false;
        }
        else{
        	// we only want to show this dialog once per run.
        	if (!googleDialogShown){
	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this, requestCode);
	            dialog.show();
	            googleDialogShown = true;
        	}
        }
	}


	@Override
	protected void onStop(){
        super.onStop();
        saveSpinner();
	}
        
	@Override
	protected void onDestroy(){
        super.onDestroy();
        //---unregister the receiver---  
        if (receiverRegistered){
	    	unregisterReceiver(intentReceiver);   
	    	receiverRegistered = false;
        }
	}

	
	/**
	 * Update the response text based on the spinner selection
	 */
	private void updateResponse(){
		
		String strActDrive = getResources().getString(R.string.activity_driving);
		String strActBike = getResources().getString(R.string.activity_cycling);
		String strActRun = getResources().getString(R.string.activity_running);
		String strActHike = getResources().getString(R.string.activity_hiking);
			
        // Get the selected value from the spinner
		Spinner spinActivity = (Spinner) findViewById(R.id.spinner1);
		String selectedActivity = spinActivity.getItemAtPosition(spinActivity.getSelectedItemPosition()).toString();

			
		TextView responseText = (TextView) findViewById(R.id.TextViewResponseDefault);
		if (selectedActivity.compareToIgnoreCase(strActHike) == 0 ){
			responseText.setText(strHike);
			returnMessage = strHike;
		}
		else if (selectedActivity.compareToIgnoreCase(strActBike) == 0 ){
			responseText.setText(strBike);
			returnMessage = strBike;
		}
		else if (selectedActivity.compareToIgnoreCase(strActRun) == 0 ){
			responseText.setText(strRun);
			returnMessage = strRun;
		}
		else if (selectedActivity.compareToIgnoreCase(strActDrive) == 0 ){
			responseText.setText(strDrive);
			returnMessage = strDrive;
		}
		else{
			responseText.setText("");
			returnMessage = "";
		}

	}
	
	/**
	 * Show the count of recent responses
	 */
	private void updateResponseCount(){
		TextView txtCount = (TextView) findViewById(R.id.TextViewResponseCountDisplay);
		txtCount.setText(String.valueOf(responsesSent));
	}
	
	/**
	 * Save the Spinner Selection to the shared preferences
	 */
    private void saveSpinner(){
    	
        // Get the selected value from the spinner
		Spinner spinActivity = (Spinner) findViewById(R.id.spinner1);
		String selectedActivity = spinActivity.getItemAtPosition(spinActivity.getSelectedItemPosition()).toString();
        
		// Get a handle to the shared preferences and an editor, so we can update them
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

		// Save the option in the preferences
		String option = getString(R.string.saved_activity_option);
        editor.putString(option, selectedActivity);
        editor.apply();
        editor.commit();
		
	}

	/**
	 * Select the Spinner entry by value, rather than position
	 */
	public static void SelectSpinnerItemByValue(Spinner spnr, String value)
	{
		@SuppressWarnings("unchecked")
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnr.getAdapter();
	    spnr.setSelection(adapter.getPosition(value));
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			openSettings();
			return true;
		}
		if (id == R.id.action_exit) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** In response to the Settings Menu Item **/
	public void openSettings() {
		// Open the settings panel
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
		
	}
	
	/**
	 * return the current value of the response message
	 * @return
	 */
	public String getResponseMessage(){
		TextView responseText = (TextView) findViewById(R.id.TextViewResponseDefault);
		String strResponse = responseText.getText().toString();
		return strResponse;
	}
	
    /** Called when the user clicks the Start button */
    public void startResponses(View view) {
        //---register the receiver---
        registerReceiver(intentReceiver, intentFilter);
        receiverRegistered = true;

    	ActivateButtons(true);
		
		responsesSent = 0;
		updateResponseCount();
        createNotification();

    }
    
    /** Called when the user clicks the Stop button */
    public void stopResponses(View view) {
        //---unregister the receiver---    
    	if(receiverRegistered){
    		unregisterReceiver(intentReceiver);
    		receiverRegistered = false;
    	}

    	ActivateButtons(false);
    	
    }
    
    /**
     * Enable/Disable start/stop buttons
     * 
     * @param bStart
     */
    private void ActivateButtons(boolean bStart){
		Button btnStart = (Button) findViewById(R.id.btnStart);
		Button btnStop = (Button) findViewById(R.id.btnStop);
		
		// Enable Start button, disable Stop button
		btnStart.setEnabled(!bStart);
		btnStop.setEnabled(bStart);
    
    	
    }
    
    private void createNotification(){
    	
    	String strNotificationsSent = getResources().getString(R.string.notifications_sent) + " " + String.valueOf(responsesSent);
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_notification)
    	        .setContentTitle(getResources().getString(R.string.notification_title))
    	        .setAutoCancel(true)
    	        .setContentText(strNotificationsSent);
    	
    	// Creates an explicit intent for an Activity in your app
    	Intent resultIntent = new Intent(this, MainActivity.class);

    	// The stack builder object will contain an artificial back stack for the
    	// started Activity.
    	// This ensures that navigating backward from the Activity leads out of
    	// your application to the Home screen.
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	
    	// Adds the back stack for the Intent (but not the Intent itself)
    	stackBuilder.addParentStack(MainActivity.class);
    	
    	// Adds the Intent that starts the Activity to the top of the stack
    	stackBuilder.addNextIntent(resultIntent);
    	
    	PendingIntent resultPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_UPDATE_CURRENT
    	        );
    	mBuilder.setContentIntent(resultPendingIntent);
    	
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	// mId allows you to update the notification later on.
    	mNotificationManager.notify(mNotificationId, mBuilder.build());
    	
    }

}
