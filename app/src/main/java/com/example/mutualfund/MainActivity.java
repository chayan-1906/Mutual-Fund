package com.example.mutualfund;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

	// Android UI...
	private TextView txtViewAddItem;
	
	private ProgressDialog progressDialogLoadingData;
	
	// Firebase...
	private FirebaseAuth firebaseAuth;
	private FirebaseUser firebaseUser;
	private DatabaseReference databaseReference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_main );
		Toolbar toolbar = findViewById ( R.id.toolbar );
		setSupportActionBar ( toolbar );
		Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "Mutual Fund" );
		
		FloatingActionButton fab = findViewById ( R.id.fab );
		
		txtViewAddItem = findViewById ( R.id.txtViewAddItem );
		
		progressDialogLoadingData = new ProgressDialog (MainActivity.this);
		progressDialogLoadingData.setMessage("Loading Investment, Please wait...");
		progressDialogLoadingData.setCancelable ( false );
		progressDialogLoadingData.setCanceledOnTouchOutside ( false );
		progressDialogLoadingData.show();
		
		// Firebase...
		firebaseAuth = FirebaseAuth.getInstance ();
		try {
			firebaseUser = firebaseAuth.getCurrentUser ( );
		} catch (Exception e) {
			sendUserToLoginActivity ( );
		}
		
		databaseReference = FirebaseDatabase.getInstance ( ).getReference ( "Users" );
		databaseReference.keepSynced ( true );
		
		Animation animationTxt = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.blink );
		txtViewAddItem.startAnimation ( animationTxt );
		Animation animationFab = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.clockwise );
		fab.startAnimation ( animationFab );
		
		fab.setOnClickListener ( view -> {
			if (!isConnected()) {
				Intent noInternetIntent = new Intent ( getApplicationContext (), NoInternetActivity.class );
				startActivity ( noInternetIntent );
				finish ();
			} else
				//createPopupDialog();
				sendToInvestmentInfoActivity ();
			//Snackbar.make ( view, "Replace with your own action", Snackbar.LENGTH_LONG ).setAction ( "Action", null ).show ( );
		} );
		
		if (!isConnected()) {
			Intent noInternetIntent = new Intent ( getApplicationContext (), NoInternetActivity.class );
			startActivity ( noInternetIntent );
			finish ();
		}
		
		try {
			databaseReference.child ( firebaseUser.getUid () ).child ( "Funds" ).addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
					if (dataSnapshot.exists()) {
						Log.i ( "MainActivity", "abcd" + dataSnapshot.getChildrenCount () + "" );
						if (dataSnapshot.getChildrenCount () > 0) {
							String max = null;
							for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren ()) {
								List<String> list = Arrays.asList(dataSnapshot1.getKey ());
								max = Collections.max(list);
							}
							Log.i ( "MaximumId", max );
							RegisterActivity.counter = Integer.parseInt ( max ) + 1;
							sendToMyListActivity ( );
							Log.i ( "Counter", RegisterActivity.counter + "" );
						}
					}
//                    FancyToast.makeText ( getApplicationContext (), "No Investments to show, Please Add Investments", FancyToast.LENGTH_LONG, FancyToast.INFO, false ).show ();
					progressDialogLoadingData.dismiss();
				}
				
				@Override
				public void onCancelled(@NonNull DatabaseError databaseError) {
					Log.i("TAG", databaseError.getMessage());
//                progressDialogLoadingData.dismiss();
				}
			});
		} catch (Exception e) {
			sendUserToLoginActivity ();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater ( ).inflate ( R.menu.menu_main, menu );
		return true;
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId ( );
		
		//noinspection SimplifiableIfStatement
		switch (id) {
//            case R.id.action_settings:
//                String versionName = BuildConfig.VERSION_NAME;
//                FancyToast.makeText ( getApplicationContext (), "Version Name: " + versionName, FancyToast.LENGTH_LONG, FancyToast.INFO, true ).show();
//                break;
			case R.id.action_logout:
				firebaseAuth.signOut ();
				sendUserToLoginActivity();
				break;
		}
		return super.onOptionsItemSelected ( item );
	}
	
	private void sendUserToLoginActivity() {
		Intent loginIntent = new Intent ( MainActivity.this, LoginActivity.class );
		loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( loginIntent );
		finish ();
	}
	
	private void sendToMyListActivity() {
		Intent mylistIntent = new Intent ( MainActivity.this, MyListActivity.class );
		mylistIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( mylistIntent );
		finish ();
	}
	
	private void sendToMainActivity() {
		Intent mainIntent = new Intent ( MainActivity.this, MainActivity.class );
		mainIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( mainIntent );
		finish ();
	}
	
	private void sendToInvestmentInfoActivity () {
		Intent intentInvestmentInfo = new Intent ( MainActivity.this, InvestmentInfoActivity.class );
		startActivity ( intentInvestmentInfo );
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
}
