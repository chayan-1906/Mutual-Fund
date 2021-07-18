package com.example.mutualfund;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {
	
	// Android UI...
	private TextInputLayout textInputLayoutOTP;
	private TextInputEditText textInputEditTextOTP;
	private Button btnVerifyOTP;
	private Button btnResendOTP;
	private TextView textView;
	
	private ProgressDialog progressDialog;
	
	// Firebase...
	FirebaseAuth firebaseAuth;
	
	private String verificationId;
	private String phoneNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_send_otp );
		
		// Android UI...
		textInputLayoutOTP = findViewById ( R.id.textInputLayoutOTP );
		textInputEditTextOTP = findViewById ( R.id.textInputEditTextOTP );
		btnVerifyOTP = findViewById ( R.id.btnVerifyOTP );
		btnResendOTP = findViewById ( R.id.btnResendOTP );
		textView = findViewById ( R.id.textView );
		
		// Firebase...
		firebaseAuth = FirebaseAuth.getInstance ();
		
		phoneNo = getIntent ().getStringExtra ( "phoneNumber" ).trim ();
		
		btnResendOTP.setVisibility ( View.INVISIBLE );
		textView.setVisibility ( View.INVISIBLE );
		
		progressDialog = new ProgressDialog ( SendOTP.this );
		progressDialog.setTitle ( "Verifying Phone Number" );
		progressDialog.setMessage ( "Please wait..." );
		progressDialog.setCancelable ( false );
		progressDialog.setCanceledOnTouchOutside ( false );
		sendVerificationCode ( phoneNo );
		
		btnVerifyOTP.setOnClickListener ( v -> {
			String verificationCode = Objects.requireNonNull ( textInputEditTextOTP.getText ( ) ).toString ().trim ();
			btnResendOTP.setVisibility ( View.INVISIBLE );
			if (verificationCode.isEmpty () && verificationCode.length ()<6) {
				textInputEditTextOTP.setError ( "Enter OTP" );
				textInputEditTextOTP.requestFocus ();
				return;
			}
			verifyCode ( verificationCode );
		} );
		
		new CountDownTimer (90000,1000) {
			@SuppressLint("SetTextI18n")
			@Override
			public void onTick(long millisUntilFinished) {
				textView.setVisibility ( View.VISIBLE );
				textView.setText ( "Resend code within " + millisUntilFinished/1000 + " seconds" );
			}
			
			@Override
			public void onFinish() {
				btnResendOTP.setVisibility( View.VISIBLE);
				textView.setVisibility(View.INVISIBLE);
			}
		}.start();
		
		btnResendOTP.setOnClickListener ( v -> {
			btnResendOTP.setVisibility ( View.INVISIBLE );
			resendOTP ( phoneNo );
			new CountDownTimer (60000,1000) {
				@SuppressLint("SetTextI18n")
				@Override
				public void onTick(long millisUntilFinished) {
					textView.setVisibility ( View.VISIBLE );
					textView.setText ( "Resend code within " + millisUntilFinished/1000 + " seconds" );
				}
				
				@Override
				public void onFinish() {
					btnResendOTP.setVisibility(View.VISIBLE);
					textView.setVisibility(View.INVISIBLE);
				}
			}.start();
		} );
	}
	
	private void resendOTP(String phoneNo) {
		sendVerificationCode (phoneNo);
	}
	
	private void sendVerificationCode ( String phoneNo ) {
		PhoneAuthProvider.getInstance ().verifyPhoneNumber ( phoneNo, 90, TimeUnit.SECONDS, SendOTP.this, mCallBack );
	}
	
	private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks ( ) {
		@Override
		public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
			String verificationCode = phoneAuthCredential.getSmsCode ();
			if (verificationCode != null) {
				textInputEditTextOTP.setText ( verificationCode );  // Auto Verification
				verifyCode ( verificationCode );
			}
		}
		
		@SuppressLint("ShowToast")
		@Override
		public void onVerificationFailed(@NonNull FirebaseException e) {
			progressDialog.dismiss();
			FancyToast.makeText ( SendOTP.this, e.getMessage (), FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
		}
		
		@Override
		public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
			super.onCodeSent ( s, forceResendingToken );
			verificationId = s;
		}
	};
	
	private void verifyCode(String verificationCode) {
		PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential ( verificationId, verificationCode );
		progressDialog.show();
		SignInWithPhone ( phoneAuthCredential );
	}
	
	private void SignInWithPhone(PhoneAuthCredential phoneAuthCredential) {
		firebaseAuth.signInWithCredential ( phoneAuthCredential ).addOnCompleteListener ( task -> {
			if (task.isSuccessful()) {
				progressDialog.dismiss();
				startActivity ( new Intent ( SendOTP.this, MainActivity.class ) );
				finish ( );
			} else {
				progressDialog.dismiss();
				FancyToast.makeText ( SendOTP.this, Objects.requireNonNull ( task.getException ( ) ).getMessage (), FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
			}
		} );
	}
}
