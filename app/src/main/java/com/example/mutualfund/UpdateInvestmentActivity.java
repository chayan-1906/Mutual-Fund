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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class UpdateInvestmentActivity extends AppCompatActivity {

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
    private ImageButton btnUpdate;

    private static Dialog savingDialog;
    private Calendar calendar;

    // Firebase...
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static DatabaseReference databaseReferenceUpdate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_update_investment );

        int investmentId = getIntent ( ).getIntExtra ( "investmentId", -1 );
        // Firebase...
        firebaseAuth = FirebaseAuth.getInstance ( );
        firebaseUser = firebaseAuth.getCurrentUser ( );
        databaseReferenceUpdate = FirebaseDatabase.getInstance ( ).getReference ( "Users" ).child ( firebaseUser.getUid ( ) );
        databaseReferenceUpdate.keepSynced ( true );

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
        btnUpdate = findViewById ( R.id.btnSave );

        calendar = Calendar.getInstance ( );
        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            calendar.set ( Calendar.YEAR, year );
            calendar.set ( Calendar.MONTH, monthOfYear );
            calendar.set ( Calendar.DAY_OF_MONTH, dayOfMonth );
            updateCalendarLabel ( );
        };

        updateInvestment ( investmentId );

        imageButtonLastInstallmentDate.setOnClickListener ( v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog ( UpdateInvestmentActivity.this, date, calendar.get ( Calendar.YEAR ), calendar.get ( Calendar.MONTH ), calendar.get ( Calendar.DAY_OF_MONTH ) ).show ( );
        } );

        btnDiscard.setOnClickListener ( this::discardInvestment );
    }

    private void updateCalendarLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( myFormat, Locale.US );
        editTextLastInstallmentDate.setText ( simpleDateFormat.format ( calendar.getTime ( ) ) );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void discardInvestment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder ( UpdateInvestmentActivity.this );
        builder.setTitle ( "Do you want to discard?" );
        builder.setMessage ( "Action can't be undone" );
        builder.setPositiveButton ( "Yes", (dialog, which) -> sendToMyListActivity() );
        builder.setNegativeButton ( "No", (dialog, which) -> {

        } );
        builder.create ( );
        builder.show ( );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateInvestment(int investmentId) {
        databaseReferenceUpdate.child ( "Funds" ).child ( String.valueOf ( investmentId ) ).addValueEventListener ( new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String installmentDate = Objects.requireNonNull ( snapshot.child ( "installment_date" ).getValue ( ) ).toString ( );
                String folioNo = Objects.requireNonNull ( snapshot.child ( "folio_no" ).getValue ( ) ).toString ();
                String name = Objects.requireNonNull ( snapshot.child ( "name" ).getValue ( ) ).toString ();
                String mutualFundName = Objects.requireNonNull ( snapshot.child ( "mutualFundName" ).getValue ( ) ).toString ();
                String schemeName = Objects.requireNonNull ( snapshot.child ( "schemeName" ).getValue ( ) ).toString ();
                String amount = Objects.requireNonNull ( snapshot.child ( "amount" ).getValue ( ) ).toString ();
                String lastInstallmentDate = Objects.requireNonNull ( snapshot.child ( "last_installment_date" ).getValue ( ) ).toString ();
                String blank1 = Objects.requireNonNull ( snapshot.child ( "blank1" ).getValue ( ) ).toString ();
                String blank2 = Objects.requireNonNull ( snapshot.child ( "blank2" ).getValue ( ) ).toString ();
                editTextInstallmentDate.setText ( installmentDate );
                editTextFolioNo.setText ( folioNo );
                editTextName.setText ( name );
                editTextMutualFundName.setText ( mutualFundName );
                editTextSchemeName.setText ( schemeName );
                editTextAmount.setText ( amount );
                editTextLastInstallmentDate.setText ( lastInstallmentDate );
                editTextBlank1.setText ( blank1 );
                editTextBlank2.setText ( blank2 );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

        btnUpdate.setOnClickListener ( v -> {
            String installmentDate = Objects.requireNonNull ( editTextInstallmentDate.getText ( ) ).toString ();
            Log.i ( "UpdateInvestment", Objects.requireNonNull ( editTextInstallmentDate.getText ( ) ).toString () );
            String folioNo = Objects.requireNonNull ( editTextFolioNo.getText ( ) ).toString ( ).trim ( );
            String name = Objects.requireNonNull ( editTextName.getText ( ) ).toString ( ).trim ( );
            String mutualFundName = Objects.requireNonNull ( editTextMutualFundName.getText ( ) ).toString ( ).trim ( );
            String schemeName = editTextSchemeName.getText ( ).toString ( ).trim ( );
            String amount = editTextAmount.getText ( ).toString ( ).trim ( );
            String lastInstallmentDateString = Objects.requireNonNull ( editTextLastInstallmentDate.getText ( ) ).toString ( ).trim ( );
            String blank1 = editTextBlank1.getText ( ).toString ( ).trim ( );
            String blank2 = editTextBlank2.getText ( ).toString ( ).trim ( );

            // Convert timestamp to something readable
            long currentTimeMillis = System.currentTimeMillis ( );
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat ( "dd/MM/yy" );
            dateFormat.setTimeZone ( TimeZone.getTimeZone ( "UTC" ) );
            String updateDate = dateFormat.format ( new Date ( currentTimeMillis ) );
            Log.i ( "UpdateDate", updateDate );

            // blank field isn't allowed...
            if (Objects.requireNonNull ( editTextInstallmentDate.getText ( ) ).toString ( ).isEmpty ( ) || Integer.parseInt ( editTextInstallmentDate.getText ( ).toString ( ) ) > 28 || Integer.parseInt ( editTextInstallmentDate.getText ( ).toString ( ) ) <= 0) {
                editTextInstallmentDate.setError ( "Enter installment date between 0 and 28 !" );
                Log.i ( "UpdateInvestment", "Line199" );
                editTextInstallmentDate.requestFocus ( );
                return;
            }
            else if (Objects.requireNonNull ( editTextFolioNo.getText ( ) ).toString ( ).isEmpty ( )) {
                editTextFolioNo.setError ( "Field can't be blank !" );
                editTextFolioNo.requestFocus ( );
                return;
            } else if (Objects.requireNonNull ( editTextName.getText ( ) ).toString ( ).isEmpty ( )) {
                editTextName.setError ( "Field can't be blank !" );
                editTextName.requestFocus ( );
                return;
            } else if (Objects.requireNonNull ( editTextMutualFundName.getText ( ) ).toString ( ).isEmpty ( )) {
                editTextMutualFundName.setError ( "Field can't be blank !" );
                editTextMutualFundName.requestFocus ( );
                return;
            } else if (editTextSchemeName.getText ( ).toString ( ).isEmpty ( )) {
                editTextSchemeName.setError ( "Field can't be blank !" );
                editTextSchemeName.requestFocus ( );
                return;
            } else if (editTextAmount.getText ( ).toString ( ).isEmpty ( )) {
                editTextAmount.setError ( "Field can't be blank !" );
                editTextAmount.requestFocus ( );
                return;
            } else if (Objects.requireNonNull ( editTextLastInstallmentDate.getText ( ) ).toString ( ).isEmpty ( )) {
                editTextLastInstallmentDate.setError ( "" );
                editTextLastInstallmentDate.requestFocus ( );
                FancyToast.makeText ( getApplicationContext ( ), "Field can't be blank !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ( );
                return;
            }
            // Last Installment Date should be greater or equal to today's date...
            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "dd/MM/yyyy" );
                LocalDateTime localDateTime = LocalDateTime.now ( );
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern ( "dd/MM/yyyy" );
                Date lastInstallmentDate = simpleDateFormat.parse ( editTextLastInstallmentDate.getText ( ).toString ( ) );
                Date today = simpleDateFormat.parse ( dateTimeFormatter.format ( localDateTime ) );
                if (Objects.requireNonNull ( today ).getTime ( ) > Objects.requireNonNull ( lastInstallmentDate ).getTime ( )) {
                    editTextLastInstallmentDate.setError ( "" );
                    editTextLastInstallmentDate.requestFocus ( );
                    FancyToast.makeText ( getApplicationContext ( ), "Please enter a valid date !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ( );
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace ( );
            }

            savingDialog = new Dialog ( UpdateInvestmentActivity.this );
            savingDialog.setContentView ( R.layout.saving_investment );
            savingDialog.getWindow ( ).setBackgroundDrawable ( getDrawable ( R.drawable.loading_progressbar_rectangular ) );
            savingDialog.getWindow ( ).setLayout ( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
            savingDialog.setCancelable ( false );
            savingDialog.show ( );

            // Changed fields to be set into firebase...
            HashMap<String, Object> hashMap = new HashMap<> ( );
            hashMap.put ( "investment_id", String.valueOf ( investmentId ) );
            hashMap.put ( "installment_date", installmentDate );
            hashMap.put ( "folio_no", folioNo );
            hashMap.put ( "name", name );
            hashMap.put ( "mutualFundName", mutualFundName );
            hashMap.put ( "schemeName", schemeName );
            hashMap.put ( "amount", amount );
            hashMap.put ( "last_installment_date", lastInstallmentDateString );
            hashMap.put ( "blank1", blank1 );
            hashMap.put ( "blank2", blank2 );
            hashMap.put ( "update_date", updateDate );
            databaseReferenceUpdate.child ( "Funds" ).child ( String.valueOf ( investmentId ) ).updateChildren ( hashMap ).addOnCompleteListener ( task -> {
                if (task.isSuccessful ( )) {
                    savingDialog.dismiss ( );
                    sendToMyListActivity();
                    FancyToast.makeText ( getApplicationContext ( ), getString ( R.string.savingSuccessful ), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true ).show ( );
                } else
                    FancyToast.makeText ( getApplicationContext ( ), "Please try again", FancyToast.LENGTH_LONG, FancyToast.ERROR, false ).show ( );
            } );
        } );
    }

    private void sendToMyListActivity() {
        Intent mylistIntent = new Intent ( getApplicationContext (), MyListActivity.class);
        mylistIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( mylistIntent );
    }
}