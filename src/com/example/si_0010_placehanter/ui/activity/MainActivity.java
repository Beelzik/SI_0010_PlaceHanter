package com.example.si_0010_placehanter.ui.activity;

import com.example.si_0010_placehanter.R;
import com.example.si_0010_placehanter.R.id;
import com.example.si_0010_placehanter.R.layout;
import com.example.si_0010_placehanter.R.menu;
import com.example.si_0010_placehanter.ui.fragment.CheckInFragment;
import com.example.si_0010_placehanter.ui.fragment.CreateProfileFragment;
import com.example.si_0010_placehanter.ui.fragment.OnCheckInClickListener;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements OnCheckInClickListener,OnClickListener{

	FragmentManager fragmentManager;
	
	final static String LABEL_CHECK_IN_FRAGMET="check_in_fragment";
	final static String LABEL_CREATE_PROFILE_FRAGME="create_profile_fragment";
	
	
	final String LOG_TAG="myLogs";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			replaceActivityFragment(new CheckInFragment(),
					LABEL_CHECK_IN_FRAGMET,false);
		}
	}

	
	public void replaceActivityFragment(Fragment fragment, String label, boolean addToBackStack){
		FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(label);
		}
		fragmentTransaction.replace(R.id.container, fragment, label);
		fragmentTransaction.commit();
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.action_settings:
			break;
			
		case android.R.id.home:
			FragmentManager manager=getSupportFragmentManager();
			Log.d(LOG_TAG, "BackStackEntryCount: "+manager.getBackStackEntryCount());
			manager.popBackStack();
			getSupportActionBar().setHomeButtonEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			break;
			
		default:
			break;
		}
		
		return true;
	}

	
	
	@Override
	public void onCheckInClickListener(int flag) {
		switch(flag){
		case OnCheckInClickListener.FLAG_CHEK_IN_CLICKED:
			
			replaceActivityFragment(new CreateProfileFragment(),
					LABEL_CREATE_PROFILE_FRAGME, true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			break;
		default:
			break;
			
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.home:
		
			break;
		}
	}




}
