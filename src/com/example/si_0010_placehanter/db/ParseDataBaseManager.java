package com.example.si_0010_placehanter.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;



import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ParseDataBaseManager extends BaseInfo{

	public static final String PARSE_COLUM_LOGIN="login";
	public static final String PARSE_COLUM_IMG_FILE_NAME="img_file_name";
	
	ParseDataSaveCoolback saveCoolback;
	private static ParseDataBaseManager instance;
	
	private static final int MSG_DONE=0;
	private static final int MSG_LOGIN_ALREADY_USED=100;
	

	final String LOG_TAG="myLogs";
	
	Handler handler= new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (saveCoolback!=null) {
				switch(msg.what){
				case MSG_DONE:
					saveCoolback.parseDataSaved(ParseDataSaveCoolback.PROFILE_DATA_ADDED);
					break;
				case MSG_LOGIN_ALREADY_USED:
					saveCoolback.parseDataSaved(ParseDataSaveCoolback.LOGIN_ALREADY_USED);
				}
				
			}
		}	
	};

	public void setSaveCoolback(ParseDataSaveCoolback saveCoolback) {
		this.saveCoolback = saveCoolback;
	}
	
	
	private  ParseDataBaseManager() {
	}
	
	public static ParseDataBaseManager getInstance(){
		ParseDataBaseManager localeInstance=instance;
		if(localeInstance==null){
			synchronized (PlaceHanterDataBase.class) {
				localeInstance=instance;
				if (localeInstance==null) {
					localeInstance=instance= new ParseDataBaseManager();
				}
			}
		}
		return localeInstance;
	}
	
	public void addProfile(final String login, final String pass, final int active, final int nature,
			final int lust, final int impr, final String country,final String hobbies, final File file){
	
		Log.d(LOG_TAG,"addProfile");
		ParseQuery<ParseObject> query= ParseQuery.getQuery(TABLE_PROFILE);
		query.whereEqualTo(PARSE_COLUM_LOGIN, login);
		
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> result, ParseException exception) {
				
				Log.d(LOG_TAG,"done exception: "+exception);
				
				//ParseException.
				for (ParseObject parseObject : result) {
					Log.d(LOG_TAG, PARSE_COLUM_LOGIN+": "+parseObject.getString(PARSE_COLUM_LOGIN));
				}
				if (exception!=null || result.size()==0) {
					new Thread(new mRunnable(login, pass, active, nature, 
							lust, impr, country, hobbies, file)).start();
				} else{
					handler.sendEmptyMessage(MSG_LOGIN_ALREADY_USED);
				}
				
				
			}
		});	
	}
	
	public void findProfileByLogin(String login, FindCallback<ParseObject> callback){
		ParseQuery<ParseObject> query= ParseQuery.getQuery(TABLE_PROFILE);
		query.whereEqualTo(PARSE_COLUM_LOGIN, login);
		
		query.findInBackground(callback);	
	}
	
	
	private class mRunnable implements Runnable{
		
		String login;
		String pass; 
		int active;
		int nature;
		int lust;
		int impr; 
		String country;
		String hobbies; 
		File file;

	
		
		public mRunnable(String login, String pass, int active, int nature,
				int lust, int impr, String country, String hobbies, File file) {
			this.login=login;
			this.pass=pass;
			this.active=active;
			this.nature=nature;
			this.lust=lust;
			this.impr=impr;
			this.country=country;
			this.hobbies=hobbies;
			this.file=file;
		}

		@Override
		public void run() {
			
			 
			ParseObject profile=new ParseObject(TABLE_PROFILE);
			
			
			byte[] fileInByte=fileToBytes(file);		

			try {

			ParseFile parseFile= new ParseFile(fileInByte);
		
			parseFile.save();

			profile.put(PARSE_COLUM_LOGIN, login);
			profile.put(COLUM_PASSWORD, pass);
			profile.put(COLUM_ACTIVITY, active);
			profile.put(COLUM_NATURE, nature);
			profile.put(COLUM_LUST, lust);
			profile.put(COLUM_IMPR, impr);
			profile.put(COLUM_COUNTRY, country);
			profile.put(COLUM_HOBBIES, hobbies);
			profile.put(PARSE_COLUM_IMG_FILE_NAME, file.getName());
			profile.put(COLUM_PROFILE_IMG,parseFile);
			
		
			profile.save();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(MSG_DONE);
		}
		
		private byte[] fileToBytes(File file){
			
			
			FileInputStream fis=null;
			byte[] bytes=null;
			
			
			
			try {
				
				fis= new FileInputStream(file);
				bytes= new byte[(int) file.length()];
				fis.read(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				try {
				if (fis!=null) {
						fis.close();
				}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return bytes;
		
	}
	}
	
}
