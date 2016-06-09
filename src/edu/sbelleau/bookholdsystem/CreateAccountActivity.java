package edu.sbelleau.bookholdsystem;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends Activity implements OnClickListener {
	EditText UN, PW1, PW2;
	int failCount = 0;
	Database db;
	String username, pw1, pw2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		UN = (EditText) findViewById(R.id.edt_username_new);
		PW1 = (EditText) findViewById(R.id.edt_passwordnew_1);
		PW2 = (EditText) findViewById(R.id.edt_passwordnew_2);
		db = Database.getInstance();
		
		Button confirm = (Button) findViewById(R.id.btn_confirm_new_account);
		confirm.setOnClickListener(this);
	}
	
	private boolean valid(String s)
	{
		if(db.userExists(s))
		{
			Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(s.length() >= 5){
			if(s.contains("!") || s.contains("#") || s.contains("@") || s.contains("$"))
			{
				if(s.matches(".*[0-9].*")){
					return true;
				}
			}
			Toast.makeText(this, "Error, needs 1 special character and 1 number", Toast.LENGTH_SHORT).show();
			return false;
		}
		Toast.makeText(this, "Error, not enough characters", Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_confirm_new_account)
		{
			username = UN.getText().toString();
			pw1 = PW1.getText().toString();
			pw2 = PW2.getText().toString();
			if(valid(username) && valid(pw1) && pw1.equals(pw2))
			{
				//if user is duplicate then display message and increment counter
				db.addUser(username, pw1);
				Intent i = new Intent(this, MessageActivity.class);
				i.putExtra("Message", "New Account Created, Username: " + username);
				startActivity(i);
			}else{
				failCount++;
			}
		}
		if(failCount >= 2)
		{
			Toast.makeText(this, "Error, too many tries", Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}
}
