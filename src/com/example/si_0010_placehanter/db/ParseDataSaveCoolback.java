package com.example.si_0010_placehanter.db;

public interface ParseDataSaveCoolback {
	
	int PROFILE_DATA_ADDED=0;
	int LOGIN_ALREADY_USED=100;
	
	public void parseDataSaved(int what);
}
