package com.example.mutualfund;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class LoginWithPhoneActivity extends AppCompatActivity {
	
	// Android UI...
	private CountryCodePicker countryCodePicker;
	private EditText editTextPhoneNumber;
	private Button btnSendOTP;
	private TextView textSignUp;
	private Button btnSignInEmail;
	
	private String phoneNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_login_with_phone );
		Objects.requireNonNull ( getSupportActionBar ( ) ).setDisplayHomeAsUpEnabled ( true );
		getSupportActionBar ().setTitle ( "Login" );
		
		countryCodePicker = findViewById ( R.id.countryCodePicker );
		editTextPhoneNumber = findViewById ( R.id.editTextPhoneNumber );
		btnSendOTP = findViewById ( R.id.btnSendOTP );
		textSignUp = findViewById ( R.id.textSignUp );
		btnSignInEmail = findViewById ( R.id.btnSignInEmail );
		
		btnSendOTP.setOnClickListener ( v -> {
			phoneNumber = editTextPhoneNumber.getText ().toString ().trim ();
			String phoneNo = countryCodePicker.getSelectedCountryCodeWithPlus () + phoneNumber;
			Intent chefSendOTPIntent = new Intent ( LoginWithPhoneActivity.this, SendOTP.class );
			chefSendOTPIntent.putExtra ( "phoneNumber", phoneNo );
			startActivity ( chefSendOTPIntent );
			finish ();
		} );
		
		textSignUp.setOnClickListener ( v -> {
			startActivity ( new Intent ( LoginWithPhoneActivity.this, RegisterActivity.class ) );
			finish ();
		} );
		
		btnSignInEmail.setOnClickListener ( v -> {
			startActivity ( new Intent ( LoginWithPhoneActivity.this, LoginWithEmailActivity.class ) );
			finish ();
		} );
	}
}
