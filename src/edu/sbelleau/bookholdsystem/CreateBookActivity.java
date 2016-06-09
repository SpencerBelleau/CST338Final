package edu.sbelleau.bookholdsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateBookActivity extends Activity implements OnClickListener{
	EditText title, author, isbn, fee;
	String title_s, author_s, isbn_s;
	double fee_d;
	Button create;
	Database db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_book);
		
		db = Database.getInstance();
		
		title = (EditText) findViewById(R.id.edt_newbook_title);
		author = (EditText) findViewById(R.id.edt_newbook_author);
		isbn = (EditText) findViewById(R.id.edt_newbook_isbn);
		fee = (EditText) findViewById(R.id.edt_newbook_fee);
		
		create = (Button) findViewById(R.id.btn_create_book_confirm);
		create.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		//Make sure the boxes are filled
		if(title.getText().toString() != null 
				&& author.getText().toString() != null
				&& isbn.getText().toString() != null
				&& fee.getText().toString() != null)
		{
			title_s = title.getText().toString();
			author_s = author.getText().toString();
			isbn_s = isbn.getText().toString();
			fee_d = Double.parseDouble(fee.getText().toString());
			if(db.getBookByTitle(title_s) == null)
			{
				db.addBook(title_s, author_s, isbn_s, fee_d);
				
				//Exit to main menu
				Intent i = new Intent(this, SelectionActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}else{
				Toast.makeText(this, "Information is not valid: Duplicate Book", Toast.LENGTH_SHORT).show();
				//Also exit to main menu
				Intent i = new Intent(this, SelectionActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		}else{
			Toast.makeText(this, "Information is not valid", Toast.LENGTH_SHORT).show();
			//Also exit to main menu
			Intent i = new Intent(this, SelectionActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
		
	}
}
