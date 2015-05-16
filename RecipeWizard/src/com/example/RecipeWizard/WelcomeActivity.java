package com.example.RecipeWizard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import controllers.MainController;
import dto.User;


public class WelcomeActivity extends Activity {
	
	private Button btnLogin, btnRegister, btnLoginDialog;
	public static WelcomeActivity welcomeActivity;
	private EditText usernameInputText, passwordInputText;
	
	
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_file);
        
        welcomeActivity=this; // pt a putea incheia activitatea w.a in r.a
        btnLogin = (Button) findViewById(R.id.btnSignIn);
        //btnLogin.getBackground().setColorFilter(0xff00ffff, PorterDuff.Mode.MULTIPLY);
        
        btnRegister = (Button) findViewById(R.id.btnSignUp);
       // btnRegister.getBackground().setColorFilter(0xff00ffff, PorterDuff.Mode.MULTIPLY);
        
        
        btnRegister.setOnClickListener(
        		  new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i = new Intent(WelcomeActivity.this, RegisterActivity.class);
						startActivity(i);
						//Toast.makeText(WelcomeActivity.this, "yesss!!", Toast.LENGTH_LONG).show();
						
					}
				}
           );
        
        
        btnLogin.setOnClickListener(
        		 new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final Dialog loginDialog = new Dialog(WelcomeActivity.this);
						loginDialog.setContentView(R.layout.login);
						loginDialog.setTitle("Login");
						loginDialog.show();
						
						usernameInputText = (EditText) loginDialog.findViewById(R.id.editTextUserNameToLogin);
						passwordInputText= (EditText) loginDialog.findViewById(R.id.editTextPasswordToLogin);
						btnLoginDialog = (Button) loginDialog.findViewById(R.id.buttonSignIn);
						
						
						btnLoginDialog.setOnClickListener(
								  new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										String username = usernameInputText.getText().toString();
										String password = passwordInputText.getText().toString();

												 new MyAsyncTask().execute(username, password);
									}
								}
						  );
						
					}
				}
        	);
    }
    
    



    
    public class MyAsyncTask extends AsyncTask<String, Void, User> {

		@Override
		protected User doInBackground(String... params) {
			
			User user = MainController.getInstance().SignIn(params[0], params[1]);
			
			return user;
		}

		

	     @Override
		protected void onPostExecute(User user) {
	    	 
	    	// if(user!=null)
	    	//Toast.makeText(WelcomeActivity.this, "Userul este:"+user.getUsername(), Toast.LENGTH_LONG).show();
			
	    	 
	    	 try{
			if(user!= null)
			{
				
				if(passwordInputText.getText().toString().equals(user.getPassword())){
				Intent i = new Intent(WelcomeActivity.this, HomeActivity.class);
				
				i.putExtra("user", user); //trimit username=ul catre activitatea principala a aplicatiei
				//pe care il voi obtine din userul curent
				startActivity(i);
				finish();
				}
				else
				{
					//usernameInputText.setText("");
				    passwordInputText.setText("");
				    Toast.makeText(WelcomeActivity.this, "Bad credentials, please try again", Toast.LENGTH_LONG).show();
				}
				
			}
			else
			{
				
				//Toast.makeText(WelcomeActivity.this, "User null-din welcome activity", Toast.LENGTH_LONG).show();
				Toast.makeText(WelcomeActivity.this, "This user is not registered. Please register first", Toast.LENGTH_LONG).show();
				usernameInputText.setText("");
			    passwordInputText.setText("");
			}
	    	 }catch(Exception ex){
	    		 ex.printStackTrace();
	    		 Toast.makeText(WelcomeActivity.this, "This user is not registered. Please register first", Toast.LENGTH_LONG).show();
	    	 }
			super.onPostExecute(user);
			
		}
		
		
		
		
    	
    	
    }

   
}
