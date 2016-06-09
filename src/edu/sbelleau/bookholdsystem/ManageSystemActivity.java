package edu.sbelleau.bookholdsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ManageSystemActivity extends Activity implements OnClickListener{
	LinearLayout content;
	Button createBook, mainMenu;
	TextView t;
	Database db = Database.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_system);
		
		content = (LinearLayout) findViewById(R.id.lyt_logs);
		TextView t = new TextView(this);
		t.setText(db.getAllLogs());
		content.addView(t);
		
		createBook = (Button) findViewById(R.id.btn_create_book);
		createBook.setOnClickListener(this);
		
		mainMenu = (Button) findViewById(R.id.btn_main_menu);
		mainMenu.setOnClickListener(this);
		
		Intent i = new Intent(this, LoginActivity.class);
		i.putExtra("AdminLogin", true);
		startActivityForResult(i, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode == RESULT_CANCELED)
		{
			finish();
		}
		if(data != null)
		{
			if(data.getExtras().getBoolean("Success"))
			{
				//Don't do anything
				//Login Success
				return;
			}
		}
		Toast.makeText(this, "Admin Login Failure", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_create_book)
		{
			Intent i = new Intent(this, CreateBookActivity.class);
			startActivity(i);
		}
		
		if(v.getId() == R.id.btn_main_menu)
		{
			Intent i = new Intent(this, SelectionActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
		
	}
}
