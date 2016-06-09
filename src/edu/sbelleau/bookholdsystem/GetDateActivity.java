package edu.sbelleau.bookholdsystem;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class GetDateActivity extends Activity implements OnClickListener {
	Intent result = new Intent();
	Bundle startData;
	DatePicker date;
	TimePicker time;
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_date);
		startData = this.getIntent().getExtras();
		date = (DatePicker) findViewById(R.id.dpk_date);
		time = (TimePicker) findViewById(R.id.tpk_time);
		TextView title = (TextView) findViewById(R.id.txt_date_get);
		title.setText(startData.getString("Title"));
		TextView extra = (TextView) findViewById(R.id.txt_date_extra);
		extra.setText(startData.getString("Extra"));
		
		Button ok = (Button) findViewById(R.id.btn_confirm_date);
		ok.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Date d = getDateFromDroid(date, time);
		result.putExtra("Date", d.getTime());
		this.setResult(RESULT_OK, result);
		this.finish();
	}
	
	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(this, SelectionActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	
	public Date getDateFromDroid(DatePicker datePicker, TimePicker timePicker){
	    int day = datePicker.getDayOfMonth();
	    int month = datePicker.getMonth();
	    int year =  datePicker.getYear();
	    int hour = timePicker.getCurrentHour();
	    int minute = timePicker.getCurrentMinute();

	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month, day, hour, minute);

	    return calendar.getTime();
	}
}
