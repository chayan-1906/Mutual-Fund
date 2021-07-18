package com.example.mutualfund;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class InvestmentInfoActivity extends AppCompatActivity {

	private static TextInputEditText editTextInstallmentDate;
	private static TextInputEditText editTextFolioNo;
	private static TextInputEditText editTextName;
	private static TextInputEditText editTextMutualFundName;
	@SuppressLint("StaticFieldLeak")
	private static EditText editTextSchemeName;
	@SuppressLint("StaticFieldLeak")
	private static EditText editTextAmount;
	private static TextInputEditText editTextLastInstallmentDate;
	private ImageButton imageButtonLastInstallmentDate;
	@SuppressLint("StaticFieldLeak")
	private static EditText editTextBlank1;
	@SuppressLint("StaticFieldLeak")
	private static EditText editTextBlank2;
	private ImageButton btnDiscard;
	private ImageButton btnSave;

	private static Dialog savingDialog;
	private Calendar calendar;

	// Firebase...
	private FirebaseAuth firebaseAuth;
	private FirebaseUser firebaseUser;
	private static DatabaseReference databaseReference;

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate ( savedInstanceState );
		setContentView ( R.layout.activity_investment_info );

		// Firebase...
		firebaseAuth = FirebaseAuth.getInstance ();
		firebaseUser = firebaseAuth.getCurrentUser ();
		databaseReference = FirebaseDatabase.getInstance ( ).getReference ( "Users" ).child ( firebaseUser.getUid () );
		databaseReference.keepSynced ( true );

		editTextInstallmentDate = findViewById ( R.id.editTextInstallmentDate );
		editTextFolioNo = findViewById ( R.id.editTextFolioNo );
		editTextName = findViewById ( R.id.editTextName );
		editTextMutualFundName = findViewById ( R.id.editTextMutualFundName );
		editTextSchemeName = findViewById ( R.id.editTextSchemeName );
		editTextAmount = findViewById ( R.id.editTextAmount );
		editTextLastInstallmentDate = findViewById ( R.id.editTextLastInstallmentDate );
		imageButtonLastInstallmentDate = findViewById ( R.id.imageButtonLastInstallmentDate );
		editTextBlank1 = findViewById ( R.id.editTextBlank1 );
		editTextBlank2 = findViewById ( R.id.editTextBlank2 );
		btnDiscard = findViewById ( R.id.btnDiscard );
		btnSave = findViewById ( R.id.btnSave );

		calendar = Calendar.getInstance ();
		final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
			// TODO Auto-generated method stub
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateCalendarLabel();
		};

		imageButtonLastInstallmentDate.setOnClickListener( v -> {
			// TODO Auto-generated method stub
			new DatePickerDialog(InvestmentInfoActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
		} );

		btnDiscard.setOnClickListener ( this::discardInvestment );

		btnSave.setOnClickListener ( view -> {
			Log.i ( "Counter", "107 Counter: " + RegisterActivity.counter );
			saveInvestment ( view );
		} );
	}

	private void updateCalendarLabel() {
		String myFormat = "dd/MM/yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
		editTextLastInstallmentDate.setText(simpleDateFormat.format(calendar.getTime()));
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void discardInvestment (View view ) {
		Log.i ( "AlertDialog", Objects.requireNonNull ( editTextInstallmentDate.getText ( ) ).toString ().isEmpty () + "" + editTextFolioNo.getText ().toString ().isEmpty () + "" + editTextName.getText ().toString ().isEmpty () + editTextMutualFundName.getText ().toString ().isEmpty () + editTextSchemeName.getText ().toString ().isEmpty () + editTextAmount.getText ().toString ().isEmpty () + editTextLastInstallmentDate.getText ().toString ().isEmpty () + editTextBlank1.getText ().toString ().isEmpty () + "" +  editTextBlank2.getText ().toString ().isEmpty () );
		// if any field is not blank, then show alert dialog...
		if (editTextInstallmentDate.getText ().toString ().isEmpty () && editTextFolioNo.getText ().toString ().isEmpty () &&
				editTextName.getText ().toString ().isEmpty () && editTextMutualFundName.getText ().toString ().isEmpty () &&
				editTextSchemeName.getText ().toString ().isEmpty () && editTextAmount.getText ().toString ().isEmpty () &&
				editTextLastInstallmentDate.getText ().toString ().isEmpty () && editTextBlank1.getText ().toString ().isEmpty () &
				editTextBlank2.getText ().toString ().isEmpty ()) {
			sendToMyListActivity ( );
		} else { // if all fields are blank, then move to MyListActivity...
			AlertDialog.Builder builder = new AlertDialog.Builder ( InvestmentInfoActivity.this );
			builder.setTitle ( "Do you want to discard?" );
			builder.setMessage ( "Action can't be undone" );
			builder.setPositiveButton ( "Yes", (dialog, which) -> sendToMyListActivity () );
			builder.setNegativeButton ( "No", (dialog, which) -> {

			} );
			builder.create ();
			builder.show ();
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@SuppressLint("UseCompatLoadingForDrawables")
	private void saveInvestment(View view) {

		// blank field isn't allowed...
		if (Objects.requireNonNull ( editTextInstallmentDate.getText ( ) ).toString ().isEmpty () || Integer.parseInt (editTextInstallmentDate.getText ().toString ()) > 28 || Integer.parseInt (editTextInstallmentDate.getText ().toString ()) <= 0) {
			editTextInstallmentDate.setError ( "Enter installment date between 0 and 28 !" );
			editTextInstallmentDate.requestFocus ();
			return;
		} else if (Objects.requireNonNull ( editTextFolioNo.getText ( ) ).toString ().isEmpty ()) {
			editTextFolioNo.setError ( "Field can't be blank !" );
			editTextFolioNo.requestFocus ();
			return;
		} else if (Objects.requireNonNull ( editTextName.getText ( ) ).toString ().isEmpty ()) {
			editTextName.setError ( "Field can't be blank !" );
			editTextName.requestFocus ();
			return;
		} else if (Objects.requireNonNull ( editTextMutualFundName.getText ( ) ).toString ().isEmpty ()) {
			editTextMutualFundName.setError ( "Field can't be blank !" );
			editTextMutualFundName.requestFocus ();
			return;
		} else if (editTextSchemeName.getText ().toString ().isEmpty ()) {
			editTextSchemeName.setError ( "Field can't be blank !" );
			editTextSchemeName.requestFocus ();
			return;
		} else if (editTextAmount.getText ().toString ().isEmpty ()) {
			editTextAmount.setError ( "Field can't be blank !" );
			editTextAmount.requestFocus ();
			return;
		} else if (Objects.requireNonNull ( editTextLastInstallmentDate.getText ( ) ).toString ().isEmpty ()) {
			editTextLastInstallmentDate.setError ( "" );
			editTextLastInstallmentDate.requestFocus ();
			FancyToast.makeText ( getApplicationContext (), "Field can't be blank !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
			return;
		}
		// Last Installment Date should be greater or equal to today's date...
		try {
			@SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
			LocalDateTime localDateTime = LocalDateTime.now ();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern ( "dd/MM/yyyy" );
			Date lastInstallmentDate = simpleDateFormat.parse ( editTextLastInstallmentDate.getText ().toString () );
			Date today = simpleDateFormat.parse ( dateTimeFormatter.format ( localDateTime ) );
			if (Objects.requireNonNull ( today ).getTime () > Objects.requireNonNull ( lastInstallmentDate ).getTime ()) {
				editTextLastInstallmentDate.setError ( "" );
				editTextLastInstallmentDate.requestFocus ();
				FancyToast.makeText ( getApplicationContext (), "Please enter a valid date !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace ( );
		}

		int investmentId = RegisterActivity.counter;
		Integer installmentDate = Integer.valueOf ( editTextInstallmentDate.getText ().toString ().trim () );
		String folioNo = editTextFolioNo.getText ().toString ().trim ();
		String name = editTextName.getText ().toString ().trim ();
		String mutualFundName = editTextMutualFundName.getText ().toString ().trim ();
		String schemeName = editTextSchemeName.getText ().toString ().trim ();
		String amount = editTextAmount.getText ().toString ().trim ();
		String lastInstallmentDate = editTextLastInstallmentDate.getText ().toString ().trim ();
		String blank1 = editTextBlank1.getText ().toString ().trim ();
		String blank2 = editTextBlank2.getText ().toString ().trim ();

		// Convert timestamp to something readable
		long currentTimeMillis = System.currentTimeMillis ();
		@SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yy" );
		dateFormat.setTimeZone ( TimeZone.getTimeZone ( "UTC" ) );
		String createDate = dateFormat.format ( new Date ( currentTimeMillis ) );

		savingDialog = new Dialog ( InvestmentInfoActivity.this );
		savingDialog.setContentView ( R.layout.saving_investment );
		savingDialog.getWindow ().setBackgroundDrawable ( getDrawable ( R.drawable.loading_progressbar_rectangular ) );
		savingDialog.getWindow ().setLayout ( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
		savingDialog.setCancelable ( false );
		savingDialog.show ();

		// Changed fields to be set into firebase...
//        final String firebaseUserID = firebaseAuth.getCurrentUser ().getUid ();
		HashMap<String, String> hashMap = new HashMap<> (  );
		hashMap.put ( "investment_id", String.valueOf ( investmentId ) );
		hashMap.put ( "installment_date", String.valueOf ( installmentDate ) );
		hashMap.put ( "folio_no", folioNo );
		hashMap.put ( "name", name );
		hashMap.put ( "mutualFundName", mutualFundName );
		hashMap.put ( "schemeName", schemeName );
		hashMap.put ( "amount", amount );
		hashMap.put ( "last_installment_date", lastInstallmentDate );
		hashMap.put ( "blank1", blank1 );
		hashMap.put ( "blank2", blank2 );
		hashMap.put ( "create_date", createDate );
		hashMap.put ( "update_date", "" );
		Log.i ( "Investment_Id", String.valueOf ( investmentId ) );
		databaseReference.child ( "Funds" ).child ( String.valueOf ( RegisterActivity.counter ) ).setValue ( hashMap ).addOnCompleteListener ( new OnCompleteListener<Void> ( ) {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful ()) {
					savingDialog.dismiss ();
					RegisterActivity.counter++;
					Log.i ( "Counter", "199 Counter: " + RegisterActivity.counter );
					sendToMyListActivity();
					FancyToast.makeText ( getApplicationContext ( ), getString ( R.string.savingSuccessful ), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true ).show ( );
				}
				else
					FancyToast.makeText ( getApplicationContext (), "Please try again", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ();
			}
		} );
	}

	private void sendToMyListActivity() {
		Intent mylistIntent = new Intent ( getApplicationContext (), MyListActivity.class);
		mylistIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
		startActivity ( mylistIntent );
	}
}
