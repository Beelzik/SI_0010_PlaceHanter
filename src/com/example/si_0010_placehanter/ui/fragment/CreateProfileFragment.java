package com.example.si_0010_placehanter.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.example.si_0010_placehanter.PlaceHanterApplication;
import com.example.si_0010_placehanter.R;

import com.example.si_0010_placehanter.db.BaseInfo;
import com.example.si_0010_placehanter.db.ParseDataBaseManager;
import com.example.si_0010_placehanter.db.ParseDataSaveCoolback;
import com.example.si_0010_placehanter.db.PlaceHanterDataBase;
import com.example.si_0010_placehanter.uril.ImageFileDecoder;
import com.example.si_0010_placehanter.uril.ImageFileDecoder.FileDecodeCollback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentProvider;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class CreateProfileFragment extends Fragment implements OnClickListener,OnSeekBarChangeListener,ParseDataSaveCoolback{
	
	
	EditText edProfLogin;
	EditText edProfPass;
	EditText edProfCountry;
	EditText edProfHobbies;
	
	TextView tvProfActivityMin;
	TextView tvProfActivityMax;
	TextView tvProfNatureMin;
	TextView tvProfNatureMax;
	TextView tvProfLustMin;
	TextView tvProfLustMax;
	TextView tvProfImprMin;
	TextView tvProfImprMax;
	
	SeekBar sbProfActivity;
	SeekBar sbProfNature;
	SeekBar sbProfLust;
	SeekBar sbProfImpr;
	
	Button btnProfPhoto;
	Button btnProfConfirm;
	
	ProgressBar pbProfPhotoLoad;
	
	ImageView ivProfPhoto;
	
	String login;
	String password;
	String country;
	String hobbies;
	int progActivity;
	int progNature;
	int progLust;
	int progImpr;
	File imgFile;
	
	PlaceHanterDataBase placeHanterDataBase;
	ParseDataBaseManager parseDataBaseManager;
	ImageFileDecoder imageFileDecoder;
	
	SharedPreferences sharedPreferences;
	
	final static String PREF_LOCALE_PROF_EXIST="locale_prof_exist";
	final static String PREF_LOCALE_PROF_LOGIN="locale_prof_login";
	final static String PREF_PARSE_PROF_EXIST="parse_prof_exist";
	final static String PREF_PARSE_PROF_LOGIN="parse_prof_login";
	
	final String LOG_TAG="myLogs";
	final int SELECT_PICTURE=100;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_create_profile, container,
				false);
		edProfLogin=(EditText) rootView.findViewById(R.id.edProfLogin);
		edProfPass=(EditText) rootView.findViewById(R.id.edProfPass);
		edProfCountry=(EditText) rootView.findViewById(R.id.edProfCountry);
		edProfHobbies=(EditText) rootView.findViewById(R.id.edProfHobbies);
		
		tvProfActivityMin= (TextView) rootView.findViewById(R.id.tvProfActivityMin);
		tvProfActivityMax= (TextView) rootView.findViewById(R.id.tvProfActivityMax);
		tvProfNatureMin= (TextView) rootView.findViewById(R.id.tvProfNatureMin);
		tvProfNatureMax= (TextView) rootView.findViewById(R.id.tvProfNatureMax);
		tvProfLustMin= (TextView) rootView.findViewById(R.id.tvProfLustMin);
		tvProfLustMax= (TextView) rootView.findViewById(R.id.tvProfLustMax);
		tvProfImprMin= (TextView) rootView.findViewById(R.id.tvProfImprMin);
		tvProfImprMax= (TextView) rootView.findViewById(R.id.tvProfImprMax);
		
		
		sbProfActivity=(SeekBar) rootView.findViewById(R.id.sbProfActivity);
		sbProfNature=(SeekBar) rootView.findViewById(R.id.sbProfNature);
		sbProfLust=(SeekBar) rootView.findViewById(R.id.sbProfLust);
		sbProfImpr=(SeekBar) rootView.findViewById(R.id.sbProfImpr);
		
		sbProfActivity.setOnSeekBarChangeListener(this);
		sbProfNature.setOnSeekBarChangeListener(this);
		sbProfLust.setOnSeekBarChangeListener(this);
		sbProfImpr.setOnSeekBarChangeListener(this);
		
		btnProfPhoto=(Button) rootView.findViewById(R.id.btnProfPhoto);
		btnProfConfirm=(Button) rootView.findViewById(R.id.btnProfConfirm);
		
		btnProfPhoto.setOnClickListener(this);
		btnProfConfirm.setOnClickListener(this);
		
		ivProfPhoto=(ImageView) rootView.findViewById(R.id.ivProfPhoto);
		
		pbProfPhotoLoad=(ProgressBar) rootView.findViewById(R.id.pbProfPhotoLoad);
		pbProfPhotoLoad.setVisibility(View.GONE);
		
		progActivity=sbProfActivity.getProgress();
		progNature=sbProfNature.getProgress();
		progLust=sbProfLust.getProgress();
		progImpr=sbProfImpr.getProgress();
		
		textColorVoter(progActivity, tvProfActivityMin, tvProfActivityMax);
		textColorVoter(progNature, tvProfNatureMin, tvProfNatureMax);
		textColorVoter(progLust, tvProfLustMin, tvProfLustMax);
		textColorVoter(progImpr, tvProfImprMin, tvProfImprMax);
		
		return rootView;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		placeHanterDataBase=((PlaceHanterApplication) getActivity().getApplication()).getPlaceHanterDataBase();
		placeHanterDataBase.open();
		parseDataBaseManager=ParseDataBaseManager.getInstance();
		parseDataBaseManager.setSaveCoolback(this);
		
		sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		
		boolean unexpectedData=sharedPreferences.getBoolean(PREF_PARSE_PROF_EXIST, false);
		if (!unexpectedData) {
			SharedPreferences.Editor editor=sharedPreferences.edit();
			editor.putBoolean(PREF_PARSE_PROF_EXIST, true);
			editor.putString(PREF_PARSE_PROF_LOGIN,"Пахом");
			editor.commit();	
		}
		
		
		
		Log.d(LOG_TAG, "PREF_PARSE_PROF_LOGIN: "+sharedPreferences.getString(PREF_PARSE_PROF_LOGIN, null));
		
		boolean localeProfExist=sharedPreferences.getBoolean(PREF_LOCALE_PROF_EXIST, false);
		boolean parseProfExist=sharedPreferences.getBoolean(PREF_PARSE_PROF_EXIST, false);
		
		
		if (parseProfExist || localeProfExist) {
			AlertDialog.Builder adb= new AlertDialog.Builder(getActivity());
			OnDialogLoadData onDialogLoadData= new OnDialogLoadData();
			
			adb.setTitle("Profile data exist");
			adb.setMessage("Do u wanna load profile data?");
			
			if (localeProfExist) {	
				adb.setPositiveButton("Yes, from locale storage", onDialogLoadData);
			}
			if (parseProfExist) {
				adb.setNegativeButton("Yes, from parse storage", onDialogLoadData);
			}
			adb.setNeutralButton("No", onDialogLoadData);
			
			adb.show();
		}
	}
	
	@Override
	public void onStop() {
		
		super.onStop();
		placeHanterDataBase.close();
	}

	public void textColorVoter(int progress, TextView tvMin, TextView tvMax){
		int blackHeavy=getActivity().getApplicationContext().getResources().getColor(R.color.black_heavy);
		int blackLight=getActivity().getApplicationContext().getResources().getColor(R.color.black_light);
		
		if(progress>50){
			tvMax.setTextColor(blackHeavy);
			tvMin.setTextColor(blackLight);
		}else if(progress<50){
			tvMax.setTextColor(blackLight);
			tvMin.setTextColor(blackHeavy);
		}else if(progress==50){
			tvMax.setTextColor(blackLight);
			tvMin.setTextColor(blackLight);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnProfPhoto:
			
			Intent intent = new Intent();
           intent.setType("image/*");
             intent.setAction(Intent.ACTION_PICK);
          
             startActivityForResult(Intent.createChooser(intent,
                     "Select Picture"), SELECT_PICTURE);
			
			
			break;
		case R.id.btnProfConfirm:
			
			AlertDialog.Builder adb=new Builder(getActivity());
			adb.setTitle("Choise");
			adb.setPositiveButton("Locale", new OnDialogConfirm());
			adb.setNegativeButton("Parse", new OnDialogConfirm());
			
			adb.show();
			break;
		}
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch(seekBar.getId()){
		case R.id.sbProfActivity:
			textColorVoter(progress, tvProfActivityMin, tvProfActivityMax);
			progActivity=progress;
			break;
		case R.id.sbProfNature:
			textColorVoter(progress, tvProfNatureMin, tvProfNatureMax);
			progNature=progress;
			break;
		case R.id.sbProfLust:
			textColorVoter(progress, tvProfLustMin, tvProfLustMax);
			progLust=progress;
			break;
		case R.id.sbProfImpr:
			textColorVoter(progress, tvProfImprMin, tvProfImprMax);
			progImpr=progress;
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {	
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {	
	}

	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                
               imgFile=new File(selectedImagePath);
             
            	ivProfPhoto.setImageURI(Uri.fromFile(imgFile));

                
                
            }
        }
    }


	   public String getPath(Uri uri) {
           Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
           cursor.moveToFirst();

          int columnIndex = cursor.getColumnIndex("_data");
         //  int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
           String filePath = cursor.getString(columnIndex);
           do{
           for (int i = 0; i < cursor.getColumnCount(); i++) {
			Log.d(LOG_TAG, "Cursor column["+i+"] "+cursor.getColumnName(i)+" = "+cursor.getString(i));
           }
           }while(cursor.moveToNext());
           
          
           cursor.close();
           return filePath;
	    }

	   class OnDialogLoadData implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which){
			case Dialog.BUTTON_POSITIVE:	
			Cursor cursor=placeHanterDataBase
				.findProfileByLoign(sharedPreferences.getString(PREF_LOCALE_PROF_LOGIN,null));
			if (cursor.moveToFirst()) {
				Log.d(LOG_TAG, "fined");
				login=cursor.getString(cursor.getColumnIndex(BaseInfo.COLUM_LOGIN));
				password=cursor.getString(cursor.getColumnIndex(BaseInfo.COLUM_PASSWORD));
				
				progActivity=cursor.getInt(cursor.getColumnIndex(BaseInfo.COLUM_ACTIVITY));
				progNature=cursor.getInt(cursor.getColumnIndex(BaseInfo.COLUM_NATURE));
				progLust=cursor.getInt(cursor.getColumnIndex(BaseInfo.COLUM_LUST));
				progImpr=cursor.getInt(cursor.getColumnIndex(BaseInfo.COLUM_IMPR));
				
				country=cursor.getString(cursor.getColumnIndex(BaseInfo.COLUM_COUNTRY));
				hobbies=cursor.getString(cursor.getColumnIndex(BaseInfo.COLUM_HOBBIES));
				
				edProfLogin.setText(login);
				edProfPass.setText(password);
				
				sbProfActivity.setProgress(progActivity);
				sbProfNature.setProgress(progNature);
				sbProfLust.setProgress(progLust);
				sbProfImpr.setProgress(progImpr);
				
				edProfCountry.setText(country);
				edProfHobbies.setText(hobbies);
				
				imgFile= new File(cursor.getString(cursor.getColumnIndex(BaseInfo.COLUM_PROFILE_IMG)));
				
				ivProfPhoto.setImageURI(Uri.fromFile(imgFile));
			}
				break;
			case Dialog.BUTTON_NEGATIVE:
				parseDataBaseManager.findProfileByLogin(sharedPreferences
					.getString(PREF_PARSE_PROF_LOGIN,null),new FindCallback<ParseObject>() {
							
						@Override
						public void done(List<ParseObject> objects, ParseException e) {
							ParseObject profile;
							
							for (ParseObject parseObject : objects) {
								Log.d(LOG_TAG, ParseDataBaseManager.PARSE_COLUM_LOGIN+": "
							+parseObject.getString(ParseDataBaseManager.PARSE_COLUM_LOGIN));
							}
							
							if(e==null){
								profile=objects.get(0);
								
								login=profile.getString(ParseDataBaseManager.PARSE_COLUM_LOGIN);
								password=profile.getString(BaseInfo.COLUM_PASSWORD);
								
								progActivity=profile.getInt(BaseInfo.COLUM_ACTIVITY);
								progNature=profile.getInt(BaseInfo.COLUM_NATURE);
								progLust=profile.getInt(BaseInfo.COLUM_LUST);
								progImpr=profile.getInt(BaseInfo.COLUM_IMPR);
								
								country=profile.getString(BaseInfo.COLUM_COUNTRY);
								hobbies=profile.getString(BaseInfo.COLUM_HOBBIES);
								
								edProfLogin.setText(login);
								edProfPass.setText(password);
								
								sbProfActivity.setProgress(progActivity);
								sbProfNature.setProgress(progNature);
								sbProfLust.setProgress(progLust);
								sbProfImpr.setProgress(progImpr);
								
								edProfCountry.setText(country);
								edProfHobbies.setText(hobbies);
								
								ParseFile parseFile= profile.getParseFile(BaseInfo.COLUM_PROFILE_IMG);
								
								final String fileName=profile.getString(ParseDataBaseManager.PARSE_COLUM_IMG_FILE_NAME);
								
								parseFile.getDataInBackground(new GetDataCallback() {
									
									@Override
									public void done(byte[] data, ParseException e) {
										
										 imageFileDecoder=ImageFileDecoder.getInstance();
										 imageFileDecoder.decodeImage(data, pbProfPhotoLoad, ivProfPhoto, fileName, new FileDecodeCollback() {
											
											@Override
											public void fileDecodeCallback(File file) {
											imgFile=file;
												
											}
										});
										 
									}
								});
								
		
								//ivProfPhoto.setImageURI(Uri.fromFile(file));
								}
								
							}
						});
				
				break;
			default:
				break;
			
			}
		}
		   
	   }
	   
	   class OnDialogConfirm implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			
			switch(which){
			case Dialog.BUTTON_POSITIVE:
				try {
					login=edProfLogin.getText().toString();
					password=edProfPass.getText().toString();
					country=edProfCountry.getText().toString();
					hobbies=edProfHobbies.getText().toString();
					
					Log.d(LOG_TAG, "imgFile.getAbsolutePath(): "+imgFile.getAbsolutePath());
						
					if (placeHanterDataBase.addProfile(login, password, progActivity, progNature, progLust, 
							progImpr, country, hobbies, imgFile.getAbsolutePath())) {
					
						Editor editor=sharedPreferences.edit();
						editor.putBoolean(PREF_LOCALE_PROF_EXIST, true);
						editor.putString(PREF_LOCALE_PROF_LOGIN, login);
						editor.commit();
						
						Toast.makeText(getActivity(), "Profile data saved at locale storage", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(getActivity(), "Login like "+login+" already used", Toast.LENGTH_LONG).show();
						edProfLogin.setText("");
					}
				} catch (Exception e) {
					Toast.makeText(getActivity(), "fill in all fields", Toast.LENGTH_LONG).show();
				}
				break;
			case Dialog.BUTTON_NEGATIVE:
				
				try {
					login=edProfLogin.getText().toString();
					password=edProfPass.getText().toString();
					country=edProfCountry.getText().toString();
					hobbies=edProfHobbies.getText().toString();
				
					
					
					parseDataBaseManager.addProfile(login, password, progActivity, progNature,
							progLust, progImpr, country, hobbies, imgFile);
				} catch (Exception e) {
					Toast.makeText(getActivity(), "fill in all fields", Toast.LENGTH_LONG).show();
				}
				
			
			}
			
		}
		   
	   }


	@Override
	public void parseDataSaved(int what) {
		switch(what){
		case ParseDataSaveCoolback.PROFILE_DATA_ADDED:
			Toast.makeText(getActivity(), "Profile data saved at Parse storage", Toast.LENGTH_LONG).show();
			parseDataBaseManager.findProfileByLogin(login, new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					for (ParseObject parseObject : objects) {
						Log.d(LOG_TAG, ParseDataBaseManager.PARSE_COLUM_LOGIN+" :"
					+parseObject.getString(ParseDataBaseManager.PARSE_COLUM_LOGIN));
					}
				 if (e==null) {
					String profileLogin=objects.get(0).getString(ParseDataBaseManager.PARSE_COLUM_LOGIN);
					
					Editor editor=sharedPreferences.edit();
					editor.putBoolean(PREF_PARSE_PROF_EXIST, true);
					editor.putString(PREF_PARSE_PROF_LOGIN, profileLogin);
					editor.commit();
					
				}
				}
			});
			break;
		case ParseDataSaveCoolback.LOGIN_ALREADY_USED:
			Toast.makeText(getActivity(), "Login like "+login+" already used", Toast.LENGTH_LONG).show();
			edProfLogin.setText("");
			break;
		default:
			break;
		}
		
	}
	
}
