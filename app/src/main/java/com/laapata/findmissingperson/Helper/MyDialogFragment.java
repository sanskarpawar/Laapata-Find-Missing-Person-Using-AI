package com.laapata.findmissingperson.Helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

@SuppressLint("ValidFragment")
public class MyDialogFragment extends DialogFragment {
	private int timeHour;
	private int timeMinute;
	private Handler handler;

	public MyDialogFragment(Handler handler){
		this.handler = handler;
	}

	//Use to open the DialogBox of Time Picker
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		timeHour = bundle.getInt(MyConstants.HOUR);
		timeMinute = bundle.getInt(MyConstants.MINUTE);
		TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				timeHour = hourOfDay;
				timeMinute = minute;
				Bundle b = new Bundle();
				b.putInt(MyConstants.HOUR, timeHour);
				b.putInt(MyConstants.MINUTE, timeMinute);
				Message msg = new Message();
				msg.setData(b);
				handler.sendMessage(msg);
			}
		};
		return new TimePickerDialog(getActivity(), listener, timeHour, timeMinute, false);
	}
}