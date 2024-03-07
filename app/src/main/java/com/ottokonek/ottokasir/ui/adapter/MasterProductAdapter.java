package com.ottokonek.ottokasir.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.model.api.response.MasterProductResponseModel;
import com.ottokonek.ottokasir.ui.activity.edit_product.MasterProductIView;

public class MasterProductAdapter extends RecyclerView.Adapter<MasterProductAdapter.ViewHolder> {

    private int lastCheckedPosition = -1;
    private List<MasterProductResponseModel.DataBean> mItems;
    private Context context;
    private MasterProductIView masterProductIView;

    public MasterProductAdapter(Context context, List<MasterProductResponseModel.DataBean> mItems, MasterProductIView masterProductIView) {
        this.context = context;
        this.mItems = mItems;
        this.masterProductIView = masterProductIView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_suggestion_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MasterProductResponseModel.DataBean suggestion = mItems.get(position);

        viewHolder.tvProductName.setText(suggestion.getName());
        viewHolder.tvBarcode.setText(suggestion.getBarcode());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;
        @BindView(R.id.lyMasterProduct)
        LinearLayout lyMasterProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            lyMasterProduct.setOnClickListener(view -> {
                lastCheckedPosition = getAdapterPosition();
                masterProductIView.onSelectedProduct(mItems.get(lastCheckedPosition));
                notifyDataSetChanged();
            });
        }
    }
}
