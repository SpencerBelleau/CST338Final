package edu.sbelleau.bookholdsystem;

//import android.support.v7.app.ActionBarActivity;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class PlaceHoldActivity extends Activity implements OnClickListener {
	long d1 = 0, d2 = 0;
	Intent i;
	TextView start, end, username, bookInfo;
	Button confirm;
	Database db;
	Database.book book;
	Date pickup, dropoff;
	int userHashID;
	long dateDifference;
	long diffHours;
	long diffDays;
	int totalHours;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w("", "Entered OnCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_hold);
		System.out.println("Setting pointers");
		Log.w("", "Setting Pointers");
		db = Database.getInstance();
		start = (TextView) findViewById(R.id.txt_date_start);
		end = (TextView) findViewById(R.id.txt_date_end);
		username = (TextView) findViewById(R.id.txt_placehold_username);
		bookInfo = (TextView) findViewById(R.id.txt_book_info_hold);
		confirm = (Button) findViewById(R.id.btn_place_hold_confirm);
		confirm.setOnClickListener(this);
		System.out.println("Setting Intent");
		i = new Intent(this, GetDateActivity.class);
		i.putExtra("Title", "Enter Start Date");
		i.putExtra("Extra", "Start Date and end date should not be more than 7 days apart. Books will be shown that are available during the selected time interval.");
		this.startActivityForResult(i, 1);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            //
	        	if(d1==0)
	        	{
	        		d1 = data.getLongExtra("Date", 0);
	        		i.removeExtra("Title");;
	        		i.putExtra("Title", "Enter End Date");
	        		this.startActivityForResult(i, 1);
	        	}else if(d2==0)
	        	{
	        		d2 = data.getLongExtra("Date", 0);
	        		start.setText(new Date(d1).toString());
	        		end.setText(new Date(d2).toString());
	        		
	        		dateDifference = new Date(d2).getTime() - new Date(d1).getTime();
		        	diffHours = dateDifference / (60 * 60 * 1000) % 24;
		        	diffDays = dateDifference / (24 * 60 * 60 * 1000);
		        	Toast.makeText(this, Long.toString(dateDifference) + "\n" + Long.toString(diffHours) + "\n" + Long.toString(diffDays), Toast.LENGTH_LONG).show();
		        	totalHours = (int) (diffHours + diffDays*24);
		        	if(totalHours > 168)
		        	{
		        		Toast.makeText(this, "Error, cannot reserve for more than 7 days", Toast.LENGTH_SHORT).show();
		        		i = new Intent(this, GetDateActivity.class);
		        		i.putExtra("Title", "Enter Start Date");
		        		i.putExtra("Extra", "Start Date and end date should not be more than 7 days apart. Books will be shown that are available during the selected time interval.");
		        		d1 = 0;
		        		d2 = 0;
		        		this.startActivityForResult(i, 1);
		        	}else{
		        		//Oh christ is this ever messy
		        		i = new Intent(this, SelectBookHoldActivity.class);
		        		i.putExtra("Start", d1);
		        		i.putExtra("End", d2);
		        		startActivityForResult(i, 1);
		        	}
	        	}
	        }
	        if (resultCode == 2) {
	        	userHashID = data.getExtras().getInt("HashID");
	        	username.setText("Customer Username: " + (((Database.user) db.getByHashId(userHashID)).name)); //This looks terrible
	        	SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyy hh:mm a");
	        	pickup = new Date(d1);
	        	start.setText("Pickup date/time: " + dt.format(pickup));
	        	dropoff = new Date(d2);
	        	end.setText("Dropoff date/time: " + dt.format(dropoff));
	        	String bookInfoText = "";
	        	book = db.getBookByTitle(data.getExtras().getString("bookTitle"));
	        	bookInfoText = bookInfoText + "Book Title: " + book.title;
	        	
	        	NumberFormat currency = NumberFormat.getCurrencyInstance();
	        	bookInfoText = bookInfoText + "\nReservation Number: " + Database.getReservationNumber() + "\nTotal amount: " + currency.format(book.fee * totalHours);
	        	
	        	
	        	bookInfo.setText(bookInfoText);
	        }
	    }
	}//onActivityResult

	@Override
	public void onClick(View v) {
		//Place the hold
		db.placeHold(book, pickup, dropoff, (Database.user)db.getByHashId(userHashID));
		Toast.makeText(this, "Hold Placed", Toast.LENGTH_LONG).show();
		finish();
	}
}
