package edu.sbelleau.bookholdsystem;
//Simply takes some input information and checks if you're good to log in.
//Returns value for use in database table


//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	Intent result = new Intent();
	Intent extraData = new Intent();
	Bundle loginData = new Bundle();
	int failCount = 0;
	Database db;
	int u;
	
	EditText username;
	EditText password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.edt_username);
		password = (EditText) findViewById(R.id.edt_password);
		Button OK = (Button) findViewById(R.id.btn_confirm);
		OK.setOnClickListener(this);
		
	}
	
	private void login(String UN, String PW)
	{
		db = Database.getInstance();
		extraData = this.getIntent();
		if(extraData.getBooleanExtra("AdminLogin", false))
		{
			u = db.loginAdmin(UN, PW);
			if (u != -1)
			{
				loginData.putBoolean("Success", true);
				loginData.putInt("HashID", u);
				result.putExtras(loginData);
				this.setResult(RESULT_OK, result);
				this.finish();
			}else{
				Toast.makeText(this, "Admin does not exist", Toast.LENGTH_SHORT).show();
			}
		}else{
			u = db.login(UN, PW);
			if (u != -1)
			{
				loginData.putBoolean("Success", true);
				loginData.putInt("HashID", u);
				result.putExtras(loginData);
				this.setResult(RESULT_OK, result);
				this.finish();
			}else{
				Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(username.getText().toString() != null && password.getText().toString() != null)
		{
			login(username.getText().toString(), password.getText().toString());
		}else{
			Toast.makeText(this, "ERROR: Please enter all information", Toast.LENGTH_SHORT).show();
		}
		if(failCount >= 2)
		{
			Toast.makeText(this, "ERROR: Login Failure", Toast.LENGTH_SHORT).show();
			this.setResult(RESULT_CANCELED);
			this.finish();
		}
		
	}
}
