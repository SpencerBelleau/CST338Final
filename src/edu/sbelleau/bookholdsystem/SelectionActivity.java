package edu.sbelleau.bookholdsystem;

//import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class SelectionActivity extends Activity implements OnClickListener {
	Button createAccount, placeHold, cancelHold, manageSystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        
        createAccount = (Button) findViewById(R.id.btn_create_account);
        createAccount.setOnClickListener(this);
        
        placeHold = (Button) findViewById(R.id.btn_place_hold);
        placeHold.setOnClickListener(this);
        
        cancelHold = (Button) findViewById(R.id.btn_cancel_hold);
        cancelHold.setOnClickListener(this);
        
        manageSystem = (Button) findViewById(R.id.btn_manage_system);
        manageSystem.setOnClickListener(this);
        
    }


	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_create_account)
		{
			Intent i = new Intent(this, CreateAccountActivity.class);
			startActivity(i);
		}else if(v.getId() == R.id.btn_place_hold)
		{
			Intent i = new Intent(this, PlaceHoldActivity.class);
			startActivity(i);
		}else if(v.getId()==R.id.btn_cancel_hold)
		{
			Intent i = new Intent(this, CancelHoldActivity.class);
			startActivity(i);
		}else if(v.getId()==R.id.btn_manage_system)
		{
			Intent i = new Intent(this, ManageSystemActivity.class);
			startActivity(i);
		}
	}
}
