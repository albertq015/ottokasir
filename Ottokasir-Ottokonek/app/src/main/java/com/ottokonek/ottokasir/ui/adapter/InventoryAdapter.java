package com.ottokonek.ottokasir.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.model.pojo.InventoryModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.BarangViewHolder> {

    private Context mCtz;
    private List<InventoryModel> barangList;

    public InventoryAdapter(Context mCtz, List<InventoryModel> barangList) {
        this.mCtz = mCtz;
        this.barangList = barangList;
    }

    @Override
    public BarangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtz);
        View view = inflater.inflate(R.layout.row_inventory, null);
        return new InventoryAdapter.BarangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BarangViewHolder holder, int position) {

        InventoryModel barang = barangList.get(position);
        //binding the data with the viewholder views
        holder.textViewNamaBarang.setText(barang.getNama_barang());
        holder.textViewHargaBarang.setText(barang.getHarga_barang());
        holder.textViewStok.setText(String.valueOf(barang.getStok()));
        holder.textViewTotalStock.setText(String.valueOf(barang.getTotal_stok()));
    }

    @Override
    public int getItemCount() {
        return barangList.size();
    }


    class BarangViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nama_barang)
        TextView textViewNamaBarang;
        @BindView(R.id.harga_barang)
        TextView textViewHargaBarang;
        @BindView(R.id.stok)
        TextView textViewStok;
        @BindView(R.id.total_stok)
        TextView textViewTotalStock;

        public BarangViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

}
