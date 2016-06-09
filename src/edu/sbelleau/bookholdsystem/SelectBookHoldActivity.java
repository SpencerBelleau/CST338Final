package edu.sbelleau.bookholdsystem;

//import android.support.v7.app.ActionBarActivity;
import java.text.NumberFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectBookHoldActivity extends Activity implements OnClickListener {

	LinearLayout content;
	Bundle dates;
	Database db;
	Database.book selectedBook;
	NumberFormat currency = NumberFormat.getCurrencyInstance();
	Intent result = new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_book_hold);
		
		content = (LinearLayout) findViewById(R.id.lyt_books);
		dates = this.getIntent().getExtras();
		db = Database.getInstance();
		
		String[] booknames = db.booksAvailable(new Date(dates.getLong("Start")), new Date(dates.getLong("End"))).split(":");
		
		if(booknames.length >= 2)
		{
			
			for (String s : booknames)
			{
				if(s.equals(""))
					continue;
				Database.book book = db.getBookByTitle(s);
				
				//-------Dynamic Buttons-------//
				Button b = new Button(this);
				b.setText("Title: " + book.title + "\nAuthor:" + book.author + "\nISBN: " + book.ISBN + "\nFee Per Day: " + currency.format(book.fee));
				b.setOnClickListener(this);
				b.setTag(book);
				content.addView(b);
			}

		}else{
			TextView t = new TextView(this);
			t.setText("No books available");
			content.addView(t);
			Button b = new Button(this);
			b.setText("Exit");
			b.setOnClickListener(this);
			b.setTag("MainMenu");
			content.addView(b);
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
			Database.book book = (Database.book) v.getTag();
			selectedBook = book;
			Intent i = new Intent(this, LoginActivity.class);
			this.startActivityForResult(i, 1);
		}
	}
	
	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(this, SelectionActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode == RESULT_OK)
		{
			Toast.makeText(this, "Login OK", Toast.LENGTH_LONG).show();
			result.putExtra("HashID", data.getExtras().getInt("HashID"));
			result.putExtra("bookTitle", selectedBook.title);
			this.setResult(2, result);
			this.finish();
			//New activity to reserve thing
		}else if(resultCode == RESULT_CANCELED)
		{
			
		}
	}
}
