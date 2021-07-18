package com.example.mutualfund;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MyListActivity extends AppCompatActivity {
	
	private RecyclerView recyclerView;
	private RecyclerViewAdapter recyclerViewAdapter;
	
	// Android UI...
	private LinearLayout linearLayoutRecyclerView;
	
	private TypedArray typedArrayBackgrounds;
	private ProgressDialog progressDialogLoadingData;
	
	private Random random;
	
	// Firebase...
	private FirebaseAuth firebaseAuth;
	private FirebaseUser firebaseUser;
	private static DatabaseReference databaseReference;
	
	private static List<FetchData> fetchDataList;
	private FetchData deletedInvestment;
	
	@SuppressLint("NotifyDataSetChanged")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_mylist );
		Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "Mutual Fund" );
		
		linearLayoutRecyclerView = findViewById ( R.id.linearLayoutRecyclerView );
		recyclerView = findViewById ( R.id.recyclerView );
		recyclerView.setHasFixedSize ( true );
		recyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );
		progressDialogLoadingData = new ProgressDialog(MyListActivity.this);
		progressDialogLoadingData.setMessage("Loading Investment, Please wait...");
		progressDialogLoadingData.setCancelable ( false );
		progressDialogLoadingData.setCanceledOnTouchOutside ( false );
		progressDialogLoadingData.show();
		
		Resources resources = getResources ();
		typedArrayBackgrounds = resources.obtainTypedArray ( R.array.image );
		random = new Random (  );
		int randomInt = random.nextInt ( typedArrayBackgrounds.length () );
		int drawableId = typedArrayBackgrounds.getResourceId ( randomInt, -1 );
		linearLayoutRecyclerView.setBackgroundResource ( drawableId );
		
		fetchDataList = new ArrayList<> (  );
		
		// Firebase...
		firebaseAuth = FirebaseAuth.getInstance ();
		firebaseUser = firebaseAuth.getCurrentUser ();
		databaseReference = FirebaseDatabase.getInstance ( ).getReference ( "Users" ).child ( firebaseUser.getUid () ).child ( "Funds" );
		databaseReference.keepSynced ( true );
		
		// Retrieve data from online database/firebase...
		databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					for (DataSnapshot npsnapshot : dataSnapshot.getChildren ( )) {
						Integer investmentId = Integer.parseInt ( Objects.requireNonNull ( npsnapshot.child ( "investment_id" ).getValue ( String.class ) ) );
						Integer installmentDate = Integer.parseInt ( Objects.requireNonNull ( npsnapshot.child ( "installment_date" ).getValue ( String.class ) ) );
						String folioNo = npsnapshot.child ( "folio_no" ).getValue (String.class);
						String name = npsnapshot.child ( "name" ).getValue (String.class);
						String mutualFundName = npsnapshot.child ( "mutualFundName" ).getValue (String.class);
						String schemeName = npsnapshot.child ( "schemeName" ).getValue (String.class);
						String amount = npsnapshot.child ( "amount" ).getValue (String.class);
						String lastInstallmentDate = npsnapshot.child ( "last_installment_date" ).getValue (String.class);
						String blank1 = npsnapshot.child ( "blank1" ).getValue (String.class);
						String blank2 = npsnapshot.child ( "blank2" ).getValue (String.class);
						String createDate = npsnapshot.child ( "create_date" ).getValue (String.class);
						String updateDate = npsnapshot.child ( "update_date" ).getValue ( String.class );
						fetchDataList.add ( new FetchData ( investmentId, installmentDate, folioNo, name, mutualFundName, schemeName, amount, lastInstallmentDate, blank1, blank2, createDate, updateDate ) );
						progressDialogLoadingData.dismiss();
					}
					fetchDataList.sort( Comparator.comparing ( FetchData::getInstallmentDate ) );
					recyclerViewAdapter = new RecyclerViewAdapter (getApplicationContext (), fetchDataList);
					recyclerViewAdapter.notifyDataSetChanged();
					recyclerView.setAdapter(recyclerViewAdapter);
					
					ItemTouchHelper itemTouchHelper = new ItemTouchHelper ( simpleCallback );
					itemTouchHelper.attachToRecyclerView ( recyclerView );
				}
			}
			
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				Log.i ("TAG", databaseError.getMessage());
				progressDialogLoadingData.dismiss();
			}
		});
	}
	
	ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback ( 0, ItemTouchHelper.LEFT ) {
		@Override
		public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
			return false;
		}
		
		@Override
		public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
			if (viewHolder instanceof  RecyclerViewAdapter.ViewHolder) {
				String deleteAlertTitle = "Do you want to delete?";
				String deleteAlertMessage = "Action can't be undone";
				showDeleteAlertDialog ( deleteAlertTitle, deleteAlertMessage, viewHolder.getAdapterPosition () );
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater ( ).inflate ( R.menu.menu_list, menu );
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
			case R.id.action_AddItem:
				sendToInvestmentInfoActivity ();
				break;
//            case R.id.action_settings:
//                String versionName = BuildConfig.VERSION_NAME;
//                FancyToast.makeText ( getApplicationContext (), "Version Name: " + versionName, FancyToast.LENGTH_LONG, FancyToast.INFO, true ).show();
//                break;
			case R.id.action_logout:
				firebaseAuth.signOut ();
				sendUserToLoginEmailActivity();
				break;
		}
		return super.onOptionsItemSelected ( item );
	}
	
	@SuppressLint("NotifyDataSetChanged")
	private void showDeleteAlertDialog(String deleteAlertTitle, String deleteAlertMessage, final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder ( MyListActivity.this );
		builder.setTitle ( deleteAlertTitle );
		builder.setMessage ( deleteAlertMessage );
		builder.setPositiveButton ( "Yes", (dialogInterface, i) -> {
			deletedInvestment = fetchDataList.get(position);
			Integer investmentId = deletedInvestment.getInvestmentId ();
			fetchDataList.remove ( deletedInvestment );
			databaseReference.child ( String.valueOf ( investmentId ) ).removeValue ();
			recyclerViewAdapter.notifyItemRemoved ( investmentId );
			recyclerViewAdapter.notifyDataSetChanged ();
			Snackbar snackbar = Snackbar.make( recyclerView, "Investment is removed!", Snackbar.LENGTH_LONG );
			snackbar.setTextColor( Color.rgb ( 208, 66, 106 ) );
			snackbar.show ();
		} );
		builder.setNegativeButton ( "No", (dialogInterface, i) -> recyclerViewAdapter.notifyItemChanged(position) );
		builder.show ();
	}
	
	private void sendUserToLoginEmailActivity() {
		Intent loginIntent = new Intent ( MyListActivity.this, LoginWithEmailActivity.class );
		loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( loginIntent );
		finish ();
	}
	
	private void sendToMyListActivity() {
		Intent mylistIntent = new Intent ( MyListActivity.this, MyListActivity.class );
		mylistIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( mylistIntent );
		finish ();
	}
	
	private void sendToInvestmentInfoActivity () {
		Intent intentInvestmentInfo = new Intent ( MyListActivity.this, InvestmentInfoActivity.class );
		startActivity ( intentInvestmentInfo );
	}
	
}
