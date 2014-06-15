package com.example.si_0010_placehanter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaceHanterDataBase extends BaseInfo{

	final static String DB_NAME="PlaceHanterDataBase";
	final static int DB_VERSION=1;
	
	/*final static String TABLE_PROFILE=DataManager.TABLE_PROFILE;
	
	final static String COLUM_LOGIN=DataManager.COLUM_LOGIN;
	final static String COLUM_PASSWORD=DataManager.COLUM_PASSWORD;
	final static String COLUM_ACTIVITY=DataManager.COLUM_ACTIVITY;
	final static String COLUM_NATURE=DataManager.COLUM_NATURE;
	final static String COLUM_LUST=DataManager.COLUM_LUST;
	final static String COLUM_IMPR=DataManager.COLUM_IMPR;
	final static String COLUM_COUNTRY=DataManager.COLUM_COUNTRY;
	final static String COLUM_HOBBIES=DataManager.COLUM_HOBBIES;
	final static String COLUM_PROFILE_IMG=DataManager.COLUM_PROFILE_IMG;*/
	
	
	final private static String DB_CREATE="create table "+TABLE_PROFILE+" ("
			+COLUM_LOGIN+" text primary key not null, "
			+COLUM_PASSWORD+" text not null, "
			+COLUM_ACTIVITY+" integer  not null, "
			+COLUM_NATURE+" integer  not null, "
			+COLUM_LUST+" integer  not null, "
			+COLUM_IMPR+" integer  not null, "
			+COLUM_COUNTRY+" text, "
			+COLUM_HOBBIES+" text, "
			+COLUM_PROFILE_IMG+" text not null);";		
	
	private static volatile PlaceHanterDataBase instance;
	private SQLiteDatabase dataBase;
	private DBHelper dbh;
	Context context;
	
	
	final String LOG_TAG="myLogs";
	
	public PlaceHanterDataBase(Context ctx) {
		context=ctx;
	}
	
	
	
	public void open(){
		dbh=new DBHelper(context, DB_NAME, null, DB_VERSION);
		dataBase=dbh.getWritableDatabase();
	}
	
	public boolean addProfile(String login, String pass, int active, int nature,
			int lust, int impr, String country, String hobbies, String imgPath){
		boolean rowCanBeAdded=canBeAdded(login);
		if (rowCanBeAdded) {
			ContentValues cv= new ContentValues();
			cv.put(COLUM_LOGIN, login);
			cv.put(COLUM_PASSWORD, pass);
			cv.put(COLUM_ACTIVITY,active);
			cv.put(COLUM_NATURE, nature);
			cv.put(COLUM_LUST, lust);
			cv.put(COLUM_IMPR, impr);
			cv.put(COLUM_COUNTRY, country);
			cv.put(COLUM_HOBBIES, hobbies);
			cv.put(COLUM_PROFILE_IMG, imgPath);
			Log.d(LOG_TAG, "addProfile");
			dataBase.insert(TABLE_PROFILE, null, cv);
			Log.d(LOG_TAG, "added");
			
			Cursor cursor=dataBase.query(TABLE_PROFILE, null, null, null, null, null, null);
			cursor.moveToFirst();
			 do{
		           for (int i = 0; i < cursor.getColumnCount(); i++) {
					Log.d(LOG_TAG, "Cursor column["+i+"] "+cursor.getColumnName(i)+" = "+cursor.getString(i));
		           }
		           }while(cursor.moveToNext());
			 cursor.close();	
		}
		Log.d(LOG_TAG," rowCanBeAdded: "+ rowCanBeAdded );
		 return rowCanBeAdded;
	}
	
	public Cursor findProfileByLoign(String login){
		return dataBase.query(TABLE_PROFILE, null, COLUM_LOGIN+" == '"+login+"'", null, null, null, null);
		
	}
	
	private boolean canBeAdded(String login){
		boolean answer=true;
		Cursor cursor=dataBase.query(TABLE_PROFILE, null, COLUM_LOGIN+" == '"+login+"'", null, null, null, null);
		answer=!cursor.moveToFirst();
		cursor.close();
		return answer;
	}
	
	public void close(){
		if(dbh!=null){
			dbh.close();
		}
	}
	
	private class DBHelper extends SQLiteOpenHelper{

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(LOG_TAG, " onCreate");
			Log.d(LOG_TAG, "DB_CREATE: \n"+DB_CREATE);
			db.execSQL(DB_CREATE);
			Log.d(LOG_TAG, " created");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
