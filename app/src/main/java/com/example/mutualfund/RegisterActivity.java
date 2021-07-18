package com.example.mutualfund;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
	
	// Android UI...
	private TextInputEditText editText_register_firstname;
	private TextInputEditText editText_register_lastname;
	private TextInputEditText editText_register_email;
	private TextInputEditText editText_register_password;
	private TextInputEditText editText_register_confirmpassword;
	private CountryCodePicker countryCode;
	private TextInputEditText editText_MobNo;
	private Button btn_register;
	private Button btnSignInEmail;
	private Button btnSignInPhone;
	
	private Dialog loadingDialog;
	
	public static int counter;
	
	// Firebase...
	private FirebaseAuth firebaseAuth;
	private DatabaseReference databaseReference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_register );
//        Toolbar toolbar = findViewById ( R.id.toolbar );
//        setSupportActionBar ( toolbar );
		Objects.requireNonNull ( getSupportActionBar ( ) ).setDisplayHomeAsUpEnabled ( true );
		getSupportActionBar ().setTitle ( "Register" );
		
		// Android UI...
		editText_register_firstname = findViewById ( R.id.editText_register_firstname );
		editText_register_lastname = findViewById ( R.id.editText_register_lastname );
		editText_register_email = findViewById ( R.id.editText_register_email );
		editText_register_password = findViewById ( R.id.editText_register_password );
		editText_register_confirmpassword = findViewById ( R.id.editText_register_confirmpassword );
		countryCode = findViewById ( R.id.countryCode );
		editText_MobNo = findViewById ( R.id.editText_MobNo );
		btn_register = findViewById ( R.id.btn_register );
		btnSignInEmail = findViewById ( R.id.btnSignInEmail );
		btnSignInPhone = findViewById ( R.id.btnSignInPhone );
		
		// Firebase...
		firebaseAuth = FirebaseAuth.getInstance ();
		databaseReference = FirebaseDatabase.getInstance ().getReference ();
		
		btnSignInEmail.setOnClickListener ( v -> sendUserToLoginEmailActivity() );
		
		btnSignInPhone.setOnClickListener ( v -> sendUserToLoginPhoneActivity() );
		
		btn_register.setOnClickListener ( v -> {
			createNewAccount();
			counter = 0;
		} );
	}
	
	@SuppressLint("UseCompatLoadingForDrawables")
	private void createNewAccount() {
		String firstName = Objects.requireNonNull ( editText_register_firstname.getText ( ) ).toString ();
		String lastName = Objects.requireNonNull ( editText_register_lastname.getText ( ) ).toString ();
		String email = Objects.requireNonNull ( editText_register_email.getText ( ) ).toString ();
		String password = Objects.requireNonNull ( editText_register_password.getText ( ) ).toString ();
		String confirmPassword = Objects.requireNonNull ( editText_register_confirmpassword.getText ( ) ).toString ();
		String mobileNo = Objects.requireNonNull ( editText_MobNo.getText ( ) ).toString ();
		
		Log.i ( "CREATE_NEW_ACCOUNT", email + " " + password );
		if (TextUtils.isEmpty ( firstName )) {
			editText_register_firstname.setError ( "Enter First Name!" );
			editText_register_firstname.requestFocus (  );
		} else if (TextUtils.isEmpty ( lastName )) {
			editText_register_lastname.setError ( "Enter Last Name!" );
			editText_register_lastname.requestFocus (  );
		} else if (TextUtils.isEmpty ( email )) {
			editText_register_email.setError ( "Enter Email!" );
			editText_register_email.requestFocus (  );
		} else if (TextUtils.isEmpty ( password )) {
			editText_register_password.setError ( "Enter Password!" );
			editText_register_password.requestFocus (  );
		} else if (TextUtils.isEmpty ( confirmPassword )) {
			editText_register_confirmpassword.setError ( "Enter Confirm Password!" );
			editText_register_confirmpassword.requestFocus (  );
		} else if (!password.equals ( confirmPassword )) {
			editText_register_confirmpassword.setError ( "Password doesn't match!" );
			editText_register_confirmpassword.requestFocus (  );
		} else if (TextUtils.isEmpty ( mobileNo )) {
			editText_MobNo.setError ( "Enter Mobile No!" );
			editText_MobNo.requestFocus (  );
		}
		else if (password.length () < 6)
			FancyToast.makeText ( getApplicationContext (), "Password length should be at least 6!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
		else if (mobileNo.length ()!=10)
			FancyToast.makeText ( getApplicationContext (), "Invalid Mobile No.!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
		else {
			loadingDialog = new Dialog ( this );
			loadingDialog.setContentView ( R.layout.register_loading );
			loadingDialog.getWindow ( ).setBackgroundDrawable ( getDrawable ( R.drawable.loading_rectangular ) );
			loadingDialog.getWindow ( ).setLayout ( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
			loadingDialog.setCanceledOnTouchOutside ( false );
			loadingDialog.setCancelable ( false );
			loadingDialog.show ( );
            /*firebaseAuth.createUserWithEmailAndPassword ( email, password ).addOnCompleteListener ( task -> {
                if (task.isSuccessful ()) { // if user successfully complete the account creation...
                    String firebaseUserID = Objects.requireNonNull ( firebaseAuth.getCurrentUser ( ) ).getUid ();
                    databaseReference.child ( "Users" ).child ( firebaseUserID ).setValue ( "" );
                    sendUserToMainActivity();
                    FancyToast.makeText ( getApplicationContext ( ), "You have successfully created account!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true ).show ( );
                    loadingDialog.dismiss ();
                }
                else { // if user can't complete to create account...
                    String message = Objects.requireNonNull ( task.getException ( ) ).toString ();
                    FancyToast.makeText ( getApplicationContext (), message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
                    loadingDialog.dismiss ();
                }
            } );*/
			firebaseAuth.createUserWithEmailAndPassword ( email, password ).addOnCompleteListener ( task -> {
				if (task.isSuccessful ( )) {
					String currentUserId = Objects.requireNonNull ( firebaseAuth.getCurrentUser ( ) ).getUid ( );
					databaseReference = FirebaseDatabase.getInstance ( ).getReference ( "Users" ).child ( currentUserId );
					final HashMap<String, String> hashMap = new HashMap<> ( );
					databaseReference.setValue ( hashMap ).addOnCompleteListener ( task13 -> {
						HashMap<String, String> hashMap1 = new HashMap<> ( );
						hashMap1.put ( "FirstName", firstName );
						hashMap1.put ( "LastName", lastName );
						hashMap1.put ( "Email", email );
						hashMap1.put ( "Password", password );
						hashMap1.put ( "ConfirmPassword", confirmPassword );
						hashMap1.put ( "MobNo", mobileNo );
						
						FirebaseDatabase.getInstance ( ).getReference ("Users" ).child ( Objects.requireNonNull ( FirebaseAuth.getInstance ( ).getCurrentUser ( ) ).getUid ( ) ).child ( "User_Details" ).setValue ( hashMap1 ).addOnCompleteListener ( task12 -> {
							loadingDialog.dismiss ( );
							firebaseAuth.getCurrentUser ( ).sendEmailVerification ( ).addOnCompleteListener ( task1 -> {
								if (task1.isSuccessful ( )) {
									AlertDialog.Builder builder = new AlertDialog.Builder ( RegisterActivity.this );
									builder.setTitle ( "Registration Successful" );
									builder.setMessage ( "Make sure to verify your email" );
									builder.setPositiveButton ( "OK", (dialog, which) -> {
										dialog.dismiss ();
										String phoneNumber = countryCode.getSelectedCountryCodeWithPlus () + mobileNo;
										Intent loginPhoneIntent = new Intent ( getApplicationContext (), VerifyPhone.class );
										loginPhoneIntent.putExtra ( "phoneNumber", phoneNumber );
										startActivity ( loginPhoneIntent );
										finish ();
									} );
									builder.setCancelable ( false );
									AlertDialog alertDialog = builder.create ( );
									alertDialog.show ( );
								} else {
									loadingDialog.dismiss ( );
									FancyToast.makeText ( RegisterActivity.this, Objects.requireNonNull ( task12.getException ( ) ).getMessage (), FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
								}
							} );
						} );
					} );
				}
			} );
		}
	}
	
	private void sendUserToLoginEmailActivity() {
		Intent loginIntent = new Intent ( RegisterActivity.this, LoginWithEmailActivity.class );
		startActivity ( loginIntent );
		finish ();
	}
	
	private void sendUserToLoginPhoneActivity() {
		Intent loginIntent = new Intent ( RegisterActivity.this, LoginWithPhoneActivity.class );
		startActivity ( loginIntent );
		finish ();
	}
	
	private void sendUserToMainActivity() {
		Intent mainIntent = new Intent ( RegisterActivity.this, MainActivity.class );
		mainIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( mainIntent );
		finish ();
	}
}
