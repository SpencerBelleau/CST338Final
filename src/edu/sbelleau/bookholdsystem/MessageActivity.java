package edu.sbelleau.bookholdsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MessageActivity extends Activity implements OnClickListener{
	Bundle message;
	TextView t;
	Button b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		message = this.getIntent().getExtras();
		t = (TextView) findViewById(R.id.txt_msg);
		t.setText(message.getString("Message"));
		b = (Button) findViewById(R.id.btn_msg);
		b.setOnClickListener(this);
	}
	
	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(this, SelectionActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, SelectionActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
}
