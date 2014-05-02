package com.ridgway.smsautoresponder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity {

	String strDrive = "";
	String strBike = "";
	String strRun = "";
	String strHike = "";
	String strDefaultActivity = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences sharedPref = this.getSharedPreferences(
		        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		String defaultDrive = getResources().getString(R.string.response_driving);
		strDrive = sharedPref.getString(getString(R.string.saved_response_driving), defaultDrive);
		
		String defaultBike = getResources().getString(R.string.response_driving);
		strBike = sharedPref.getString(getString(R.string.saved_response_biking), defaultBike);
		
		String defaultRun = getResources().getString(R.string.response_driving);
		strRun = sharedPref.getString(getString(R.string.saved_response_running), defaultRun);
		
		String defaultHike = getResources().getString(R.string.response_driving);
		strHike = sharedPref.getString(getString(R.string.saved_response_hiking), defaultHike);

		String defaultActivity = getResources().getString(R.string.default_activity);
		strDefaultActivity = sharedPref.getString(getString(R.string.saved_activity_option), defaultActivity);

		Spinner spinActivity = (Spinner) findViewById(R.id.spinner1);
		SelectSpinnerItemByValue(spinActivity, strDefaultActivity);
		
	}

	public static void SelectSpinnerItemByValue(Spinner spnr, String value)
	{
		ArrayAdapter adapter = (ArrayAdapter) spnr.getAdapter();
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
	
	/** Called when the user clicks the Start button */
	public void startResponses(View view) {
	    // Do something in response to button
	}
	
	/** Called when the user clicks the Stop button */
	public void stopResponses(View view) {
	    // Do something in response to button
	}
	
	
}
