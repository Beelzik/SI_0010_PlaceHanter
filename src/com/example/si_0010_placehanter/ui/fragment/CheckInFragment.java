package com.example.si_0010_placehanter.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.si_0010_placehanter.PlaceHanterApplication;
import com.example.si_0010_placehanter.R;


public class CheckInFragment extends Fragment implements OnClickListener {

	ImageButton ibtnChechIn;
	OnCheckInClickListener onCheckInClickListener;
	
	public CheckInFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		ibtnChechIn=(ImageButton) rootView.findViewById(R.id.ibtnCheckIn);
		ibtnChechIn.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		onCheckInClickListener=(OnCheckInClickListener) activity;
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ibtnCheckIn:		
			onCheckInClickListener.onCheckInClickListener(OnCheckInClickListener.FLAG_CHEK_IN_CLICKED);
			break;
		default:
			break;
			
		}
		
	}
}
