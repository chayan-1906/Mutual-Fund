package com.example.mutualfund;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

public class LoginWithEmailActivity extends AppCompatActivity {
	
	// Android UI...
	private LinearLayout linearLayout_loginEmail;
	private TextInputEditText editText_login_email;
	private TextInputEditText editText_login_password;
	private TextView textViewForgotPassword;
	private Button btnSignInEmail;
	private TextView textViewDontHaveAccount;
	private Button btnSignInPhone;
	
	private Dialog loadingDialog;
	
	//Firebase...
	private FirebaseUser firebaseUser;
	private FirebaseAuth firebaseAuth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_login_email );
//        Toolbar toolbar = findViewById ( R.id.toolbar );
//        setSupportActionBar ( toolbar );
		Objects.requireNonNull ( getSupportActionBar ( ) ).setDisplayHomeAsUpEnabled ( true );
		getSupportActionBar ().setTitle ( "Login" );
		
		// Android UI...
		linearLayout_loginEmail = findViewById ( R.id.linearLayout_loginEmail );
		editText_login_email = findViewById ( R.id.editText_login_email );
		editText_login_password = findViewById ( R.id.editText_login_password );
		textViewForgotPassword = findViewById ( R.id.textViewForgotPassword );
		btnSignInEmail = findViewById ( R.id.btnSignInEmail );
		textViewDontHaveAccount = findViewById ( R.id.textViewDontHaveAccount );
		btnSignInPhone = findViewById ( R.id.btnSignInPhone );
		
		// Firebase...
		firebaseAuth = FirebaseAuth.getInstance ();
		firebaseUser = firebaseAuth.getCurrentUser ();
		
		textViewDontHaveAccount.setOnClickListener ( v -> sendUserToRegisterActivity() );
		
		btnSignInEmail.setOnClickListener ( v -> loginAccount() );
		
		btnSignInPhone.setOnClickListener ( v -> sendUserToLoginPhoneActivity () );
	}
	
	@SuppressLint("UseCompatLoadingForDrawables")
	private void loginAccount() {
		String email = Objects.requireNonNull ( editText_login_email.getText ( ) ).toString ();
		String password = Objects.requireNonNull ( editText_login_password.getText ( ) ).toString ();
		
		Log.i ( "CREATE_NEW_ACCOUNT", email + " " + password );
		if (TextUtils.isEmpty ( email )) {
			editText_login_email.setError ( "Enter Email!" );
			editText_login_email.requestFocus ();
		} else if (TextUtils.isEmpty ( password )) {
			editText_login_password.setError ( "Enter Password!" );
			editText_login_password.requestFocus ();
		}
		else if (password.length () < 6)
			FancyToast.makeText ( getApplicationContext (), "Password length should be at least 6!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
		else {
			loadingDialog = new Dialog ( this );
			loadingDialog.setContentView ( R.layout.login_loading );
			loadingDialog.getWindow ( ).setBackgroundDrawable ( getDrawable ( R.drawable.loading_rectangular ) );
			loadingDialog.getWindow ( ).setLayout ( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
			loadingDialog.setCanceledOnTouchOutside ( false );
			loadingDialog.show ( );
            /*firebaseAuth.signInWithEmailAndPassword ( email, password ).addOnCompleteListener ( task -> {
                if (task.isSuccessful ()) {
                    sendUserToMainActivity ();
                    FancyToast.makeText ( getApplicationContext ( ), "Login Successful!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true ).show ( );
                    loadingDialog.dismiss ();
                } else {
                    String message = task.getException ().toString ();
                    FancyToast.makeText ( getApplicationContext (), message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
                    loadingDialog.dismiss ();
                }
            } );*/
			firebaseAuth.signInWithEmailAndPassword ( email, password ).addOnCompleteListener ( task -> {
				if (task.isSuccessful ()) {
					loadingDialog.dismiss ();
					if (Objects.requireNonNull ( firebaseAuth.getCurrentUser ( ) ).isEmailVerified ()) {
						loadingDialog.dismiss ();
						sendUserToMainActivity ();
					} else {
						Snackbar.make ( linearLayout_loginEmail, "Verification not done! Please Verify your Email", Snackbar.LENGTH_LONG ).show ();
					}
				} else {
					loadingDialog.dismiss ();
					FancyToast.makeText ( LoginWithEmailActivity.this, Objects.requireNonNull ( task.getException ( ) ).getMessage (), FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
				}
			} );
		}
	}
	
	private void sendUserToMainActivity() {
//        if (firebaseUser != null) {
		Intent mainIntent = new Intent ( LoginWithEmailActivity.this, MainActivity.class );
		mainIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( mainIntent );
		finish ( );
//        } else
//            sendUserToLoginActivity ();
	}
	
	private void sendUserToLoginEmailActivity() {
		Intent loginIntent = new Intent ( LoginWithEmailActivity.this, LoginWithEmailActivity.class );
		loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( loginIntent );
		finish ();
	}
	
	private void sendUserToLoginPhoneActivity() {
		Intent loginIntent = new Intent ( LoginWithEmailActivity.this, LoginWithPhoneActivity.class );
//        loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( loginIntent );
//        finish ();
	}
	
	private void sendUserToRegisterActivity() {
		Intent RegisterIntent = new Intent ( LoginWithEmailActivity.this, RegisterActivity.class );
		startActivity ( RegisterIntent );
//        finish ();
	}
}
