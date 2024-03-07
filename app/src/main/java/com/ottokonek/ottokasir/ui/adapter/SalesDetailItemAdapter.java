package com.ottokonek.ottokasir.ui.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ottokonek.ottokasir.IConfig;
import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.model.pojo.ReportDailyModel;
import com.ottokonek.ottokasir.ui.activity.receipt.ProofOrderActivity;
import com.ottokonek.ottokasir.utils.MoneyUtil;

public class SalesDetailItemAdapter extends RecyclerView.Adapter<SalesDetailItemAdapter.TransaksiViewHolder> {

    private Context context;
    private List<ReportDailyModel> items;

    public SalesDetailItemAdapter(Context context, List<ReportDailyModel> items) {
        this.context = context;
        this.items = items;

    }

    @Override
    public TransaksiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_sales_detail_item, null);
        return new SalesDetailItemAdapter.TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransaksiViewHolder holder, int position) {

        ReportDailyModel model = items.get(position);

        //binding the data with the viewholder views
        holder.textViewNo.setText(String.valueOf("No " + model.getCode()));
        holder.textViewJam.setText(String.valueOf(model.getOrder_date()));
        holder.textViewItm.setText(String.valueOf(model.getTotal_amount()) + " Item");
        holder.textViewHrg.setText(String.valueOf(MoneyUtil.Companion.convertIDRCurrencyFormat(model.getTotal_paid())));

        holder.detilTransactionAction.setOnClickListener(v -> {
            Intent i = new Intent(context, ProofOrderActivity.class);
            i.putExtra(IConfig.Companion.getPRODUCT_ID_KEY(), model.getId());
            i.putExtra("fromdetail", true);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TransaksiViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.txt_no)
        TextView textViewNo;
        @BindView(R.id.txt_hrg)
        TextView textViewHrg;
        @BindView(R.id.txt_itm)
        TextView textViewItm;
        @BindView(R.id.txt_jam)
        TextView textViewJam;
        @BindView(R.id.detil_transaksi)
        TextView detilTransactionAction;

        public TransaksiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

