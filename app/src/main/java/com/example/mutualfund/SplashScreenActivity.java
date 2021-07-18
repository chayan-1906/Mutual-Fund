package com.example.mutualfund;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
	
	private boolean splashLoaded = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView ( R.layout.activity_splash_screen );
		
		if (!splashLoaded) {
			setContentView(R.layout.activity_splash_screen);
			int secondsDelayed = 1;
			new Handler ().postDelayed( () -> {
				Intent intent = new Intent (getApplicationContext (), MainActivity.class);
				startActivity(intent);
				finish();
			},secondsDelayed * 1000);
			splashLoaded = true;
		} else {
			Intent goToMainActivity = new Intent ( getApplicationContext (), MainActivity.class );
			goToMainActivity.setFlags ( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			startActivity ( goToMainActivity );
			finish ();
		}
	}
}