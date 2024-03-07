package com.ottokonek.ottokasir.ui.activity.product_list.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.model.miscModel.DrawerUiModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.hundHolder> {

    Context mContext;
    List<DrawerUiModel> models;
    DrawerAdapterListener listener;

    public DrawerAdapter(Context mContext, List<DrawerUiModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    public void setListener(DrawerAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DrawerAdapter.hundHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.recycleritem_drawer, viewGroup, false);
        return new hundHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerAdapter.hundHolder holder, int i) {
        DrawerUiModel model = models.get(i);
        holder.ivLogo.setImageResource(model.getImg());
        holder.tvTitle.setText(model.getTitle());

        holder.layoutHitbox.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(i);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class hundHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_hitbox)
        View layoutHitbox;

        @BindView(R.id.iv_1)
        ImageView ivLogo;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public hundHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface DrawerAdapterListener {
        void onItemClick(int pos);
    }

}
