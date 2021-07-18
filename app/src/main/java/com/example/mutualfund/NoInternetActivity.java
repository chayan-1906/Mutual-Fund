package com.example.mutualfund;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NoInternetActivity extends AppCompatActivity {
	
	private Button btn_tryAgain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_no_internet );
		getSupportActionBar ( ).setTitle ( "Mutual Fund" );
		
		btn_tryAgain = findViewById ( R.id.btn_tryAgain );
		btn_tryAgain.setOnClickListener ( v -> {
			if (!isConnected())
				sendUserToNoInternetActivity();
			else
				sendUserToMainActivity ();
		} );
	}
	
	// Check Internet Connection...
	public boolean isConnected() {
		boolean connected = false;
		try {
			ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE);
			NetworkInfo nInfo = cm.getActiveNetworkInfo();
			connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
			return connected;
		} catch (Exception e) {
			Log.e("Connectivity Exception", e.getMessage());
		}
		return connected;
	}
	
	private void sendUserToNoInternetActivity() {
		Intent noInternetIntent = new Intent ( getApplicationContext (), NoInternetActivity.class );
		startActivity ( noInternetIntent );
		finish ();
	}
	
	private void sendUserToMainActivity() {
		Intent mainIntent = new Intent ( getApplicationContext (), MainActivity.class );
		mainIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( mainIntent );
		finish ();
	}
}
