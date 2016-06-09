package edu.sbelleau.bookholdsystem;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CancelHoldActivity extends Activity implements OnClickListener {
	Database db;
	Button b;
	Intent i;
	Bundle requestedData;
	Database.book book;
	Database.user user;
	String[] heldBooks;
	LinearLayout content;
	int userKey;
	NumberFormat currency = NumberFormat.getCurrencyInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cancel_hold);
		content = (LinearLayout) findViewById(R.id.lyt_cancel);
		
		db = Database.getInstance();
		i = new Intent(this, LoginActivity.class);
		startActivityForResult(i, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		db = Database.getInstance();
		//Toast.makeText(this, (Integer.toString(requestCode) + " " + Integer.toString(resultCode)), Toast.LENGTH_SHORT).show();
		if(data != null)
		{
			requestedData = data.getExtras();
			if(requestCode==1 && requestedData.getBoolean("Success"))
			{
				userKey = requestedData.getInt("HashID");
				user = (Database.user)db.getByHashId(userKey);
				if(db.displayHolds(user).equals(""))
				{
					//Toast.makeText(this, "No Books Held", Toast.LENGTH_SHORT).show();
					TextView t = new TextView(this);
					t.setText("No reservations under this username.");
					content.addView(t);
					Button b = new Button(this);
					b.setText("OK");
					b.setTag("MainMenu");
					b.setOnClickListener(this);
					content.addView(b);
				}else{
					heldBooks = db.displayHolds(user).split(":");
					
					for (String s : heldBooks)
					{
						if(s.equals(""))
							continue;
						System.out.println(s);
						book = db.getBookByTitle(s);
						
						//-------Dynamic Buttons-------//
						b = new Button(this);
						b.setText("Title: " + book.title + "\nAuthor:" + book.author + "\nISBN: " + book.ISBN + "\nFee Per Day: " + currency.format(book.fee));
						b.setOnClickListener(this);
						b.setTag(s);
						content.addView(b);
					}
				}
			}
		}else{
			Toast.makeText(this, "Error: Data is null", Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getTag().equals("MainMenu"))
		{
			Intent i = new Intent(this, SelectionActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}else{
			book = db.getBookByTitle((String)v.getTag());
			db.cancelHold(book, (Database.user)db.getByHashId(userKey));
			
			this.finish();
		}
	}
}
