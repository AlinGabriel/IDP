package com.example.RecipeWizard;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import controllers.MainController;


public class RegisterActivity extends Activity {
	
	private Button btnRegister, btnCancel;
	private EditText  etEmailAddress, etUsername, etPassword;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		etEmailAddress = (EditText) findViewById(R.id.txtEmaillAddress);
		etUsername = (EditText) findViewById(R.id.txtUsername);
		etPassword = (EditText) findViewById(R.id.txtPassword);
		
	    
		
		btnCancel.setOnClickListener(
				  new View.OnClickListener(
						  ) {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//Intent i = new Intent(RegisterActivity.this, WelcomeActivity.class);
						//startActivity(i);
						//System.out.println("buton 1 executat!!!!");
						//Toast.makeText(RegisterActivity.this, "Flag1", Toast.LENGTH_LONG).show();
					  finish();  //incheiem activitatea de register
					 
					}
				}
				  
				  
		  );
		
		
		
		
		btnRegister.setOnClickListener(
				  new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						

						String emailAddress = etEmailAddress.getText().toString();
						String username = etUsername.getText().toString();
						String password = etPassword.getText().toString();
						
					
					new MyAsyncTask().execute(username, password, emailAddress);
							
					}
				}
		  );
	}
	
	
	



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return super.onOptionsItemSelected(item);
	}
	
	
	public class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean ok = MainController.getInstance().signUp(params[0], params[1], params[2]);
			return ok;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if(result)
			{
				Toast.makeText(RegisterActivity.this, "Registration process successfully completed!", Toast.LENGTH_LONG).show();

				etEmailAddress.setText("");
				etUsername.setText("");
				etPassword.setText("");
			}else
			{
				Toast.makeText(RegisterActivity.this, "User already registered with that username. Please pick another one", Toast.LENGTH_LONG).show();
			     etUsername.setText("");
			     etPassword.setText("");
			}super.onPostExecute(result);
		}
		
	}
	
	
	
	
}
