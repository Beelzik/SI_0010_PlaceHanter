package com.example.si_0010_placehanter;


import com.example.si_0010_placehanter.db.ParseDataBaseManager;
import com.example.si_0010_placehanter.db.PlaceHanterDataBase;
import com.example.si_0010_placehanter.ui.activity.MainActivity;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.app.Application;

public final class PlaceHanterApplication extends Application {
	
	PlaceHanterDataBase placeHanterDataBase= new PlaceHanterDataBase(this);
	
	@Override
	public void onCreate() {
		super.onCreate();
		 Parse.initialize(this, "GxMYCdUxBAG5CIynWhXN5ro5aNtfsLCwGTvabulU",
				 "qUpbwMozePi62ag9nfm4VOkCYfOQ5dhnDzqDSpYB");
		 PushService.setDefaultPushCallback(this, MainActivity.class);
		 ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
	public PlaceHanterDataBase getPlaceHanterDataBase() {
		return placeHanterDataBase;
	}

}
