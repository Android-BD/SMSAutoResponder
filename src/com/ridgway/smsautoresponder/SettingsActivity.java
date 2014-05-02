package com.ridgway.smsautoresponder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SettingsActivity extends ActionBarActivity {

	String strDrive = "";
	String strBike = "";
	String strRun = "";
	String strHike = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

/*		PlaceholderFragment pf = new PlaceholderFragment();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, pf).commit();
		}
*/
		
		SharedPreferences sharedPref = this.getSharedPreferences(
		        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		String defaultDrive = getResources().getString(R.string.response_driving);
		strDrive = sharedPref.getString(getString(R.string.saved_response_driving), defaultDrive);
		
		String defaultBike = getResources().getString(R.string.response_cycling);
		strBike = sharedPref.getString(getString(R.string.saved_response_biking), defaultBike);
		
		String defaultRun = getResources().getString(R.string.response_running);
		strRun = sharedPref.getString(getString(R.string.saved_response_running), defaultRun);
		
		String defaultHike = getResources().getString(R.string.response_hiking);
		strHike = sharedPref.getString(getString(R.string.saved_response_hiking), defaultHike);

		EditText editDrivingText = (EditText) findViewById(R.id.editDrivingText);
		editDrivingText.setText(strDrive);

		EditText editBikingText = (EditText) findViewById(R.id.editBikingText);
		editBikingText.setText(strBike);
		
		EditText editRunningText = (EditText) findViewById(R.id.editRunningText);
		editRunningText.setText(strRun);
		
		EditText editHikingText = (EditText) findViewById(R.id.editHikingText);
		editHikingText.setText(strHike);


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);
			

			return rootView;
		}
	}
	 */
}
