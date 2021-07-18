package com.example.mutualfund;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
	
	@SuppressLint("StaticFieldLeak")
	private static Context context;
	
	private static List<FetchData> fetchDataList = null;
	
	public RecyclerViewAdapter(Context context, List<FetchData> fetchDataList) {
		RecyclerViewAdapter.context = context;
		RecyclerViewAdapter.fetchDataList = fetchDataList;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from ( viewGroup.getContext ( ) ).inflate ( R.layout.list_row, viewGroup, false );
		return new ViewHolder ( view, context );
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
		viewHolder.txtInstallmentDate.setText ( MessageFormat.format ( "Installment Date: {0}", fetchDataList.get(position).getInstallmentDate () ));
		viewHolder.txtFolioNo.setText ( MessageFormat.format ( "Folio No.: {0}", fetchDataList.get(position).getFolioNo () ));
		viewHolder.txtName.setText ( MessageFormat.format ( "Name: {0}", fetchDataList.get(position).getName () ) );
		viewHolder.txtMutualFundName.setText ( MessageFormat.format ( "Mutual Fund Name: {0}", fetchDataList.get(position).getMutualFundName () ) );
		viewHolder.txtSchemeName.setText ( MessageFormat.format ( "Scheme Name: {0}", fetchDataList.get(position).getSchemeName () ) );
		viewHolder.txtAmount.setText ( MessageFormat.format ( "Amount: ₹ "+ "{0}", fetchDataList.get(position).getLastInstallmentDate () ) );
		viewHolder.txtLastInstallmentDate.setText ( MessageFormat.format ( "Last Installment Date: {0}", fetchDataList.get(position).getAmount () ) );
		viewHolder.txtBlank1.setText ( MessageFormat.format ( "{0}", fetchDataList.get(position).getBlank1 () ) );
		if (viewHolder.txtBlank1.getText ().toString ().isEmpty ())
			viewHolder.txtBlank1.setVisibility(View.GONE);
		viewHolder.txtBlank2.setText ( MessageFormat.format ( "{0}", fetchDataList.get(position).getBlank2 () ) );
		if (viewHolder.txtBlank2.getText ().toString ().isEmpty ())
			viewHolder.txtBlank2.setVisibility(View.GONE);
		ViewHolder.txtCreateDate.setText ( MessageFormat.format ( "Added on: {0}", fetchDataList.get ( position ).getCreateDate () ) );

		viewHolder.setItemLongClickListener ( (view, i) -> {
			Log.i ( "OnItemLongClick", String.valueOf ( fetchDataList.get ( i ).getInvestmentId () ) );
			Intent intent = new Intent(context, UpdateInvestmentActivity.class);
			intent.putExtra ( "investmentId", fetchDataList.get ( position ).getInvestmentId () );
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} );
		String updateDate = fetchDataList.get ( position ).getUpdateDate ();
		if (updateDate.isEmpty ())
			ViewHolder.txtUpdateDate.setVisibility ( View.GONE );
		else {
			ViewHolder.txtUpdateDate.setVisibility ( View.VISIBLE );
			ViewHolder.txtUpdateDate.setText ( MessageFormat.format ( "Updated on: {0}", updateDate ) );
		}
	}
	
	@Override
	public int getItemCount() {
		return fetchDataList.size ( );
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
		
		public TextView txtInstallmentDate;
		public TextView txtFolioNo;
		public TextView txtName;
		public TextView txtMutualFundName;
		public TextView txtSchemeName;
		public TextView txtAmount;
		public TextView txtLastInstallmentDate;
		public TextView txtBlank1;
		public TextView txtBlank2;
		@SuppressLint("StaticFieldLeak")
		public static LinearLayout linearLayoutDate;
		@SuppressLint("StaticFieldLeak")
		public static TextView txtCreateDate;
		@SuppressLint("StaticFieldLeak")
		public static TextView txtUpdateDate;
		public int id;
		
		ItemLongClickListener itemLongClickListener;
		
		public ViewHolder(@NonNull View itemView, final Context context1) {
			super ( itemView );
			context = context1;
			
			itemView.setOnLongClickListener ( this );
			
			txtInstallmentDate = itemView.findViewById ( R.id.txtInstallmentDate );
			txtFolioNo = itemView.findViewById ( R.id.txtFolioNo );
			txtName = itemView.findViewById ( R.id.txtName );
			txtMutualFundName = itemView.findViewById ( R.id.txtMutualFundName );
			txtSchemeName = itemView.findViewById ( R.id.txtSchemeName );
			txtAmount = itemView.findViewById ( R.id.txtAmount );
			txtLastInstallmentDate = itemView.findViewById ( R.id.txtLastInstallmentDate );
			txtBlank1 = itemView.findViewById ( R.id.txtBlank1 );
			txtBlank2 = itemView.findViewById ( R.id.txtBlank2 );
			linearLayoutDate = itemView.findViewById ( R.id.linearLayoutDate );
			txtCreateDate = itemView.findViewById ( R.id.txtCreateDate );
			txtUpdateDate = itemView.findViewById ( R.id.txtUpdateDate );
		}
		
		public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
			this.itemLongClickListener = itemLongClickListener;
		}
		
		@Override
		public boolean onLongClick(View view) {
			this.itemLongClickListener.onItemLongClick(view, getLayoutPosition());
			return false;
		}
	}
}
