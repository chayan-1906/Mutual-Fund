package com.example.mutualfund;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
	
	private LinearLayout linearLayout_activity_main_menu;
	
	private Button btnSignWithEmail;
	private Button btnSignWithPhone;
	private Button btnSignUpEmail;
	
	private Animation zoomIn;
	private Animation zoomOut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_login );
		
		linearLayout_activity_main_menu = findViewById ( R.id.linearLayout_activity_main_menu );
		btnSignWithEmail = findViewById ( R.id.btnSignWithEmail );
		btnSignWithPhone = findViewById ( R.id.btnSignWithPhone );
		btnSignUpEmail = findViewById ( R.id.btnSignUpEmail );
		
		zoomIn = AnimationUtils.loadAnimation ( this, R.anim.zoom_in );
		zoomOut = AnimationUtils.loadAnimation ( this, R.anim.zoom_out );
		
		linearLayout_activity_main_menu.setAnimation ( zoomIn );
		linearLayout_activity_main_menu.setAnimation ( zoomOut );
		
		zoomOut.setAnimationListener ( new Animation.AnimationListener ( ) {
			@Override
			public void onAnimationStart(Animation animation) {
			
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				linearLayout_activity_main_menu.setAnimation ( zoomIn );
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			
			}
		} );
		
		zoomIn.setAnimationListener ( new Animation.AnimationListener ( ) {
			@Override
			public void onAnimationStart(Animation animation) {
			
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				linearLayout_activity_main_menu.setAnimation ( zoomOut );
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			
			}
		} );
		
		btnSignWithEmail.setOnClickListener ( v -> {
			Intent chooseOneIntent = new Intent ( getApplicationContext (), LoginWithEmailActivity.class );
			chooseOneIntent.putExtra ( "Home", "Email" );
			startActivity ( chooseOneIntent );
//            finish ();
		} );
		
		btnSignWithPhone.setOnClickListener ( v -> {
			Intent chooseOneIntent = new Intent ( getApplicationContext (), LoginWithPhoneActivity.class );
			chooseOneIntent.putExtra ( "Home", "Phone" );
			startActivity ( chooseOneIntent );
//            finish ();
		} );
		
		btnSignUpEmail.setOnClickListener ( v -> {
			Intent chooseOneIntent = new Intent ( getApplicationContext (), RegisterActivity.class );
			chooseOneIntent.putExtra ( "Home", "SignUp" );
			startActivity ( chooseOneIntent );
//            finish ();
		} );
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy ( );
		System.gc ();
	}
}