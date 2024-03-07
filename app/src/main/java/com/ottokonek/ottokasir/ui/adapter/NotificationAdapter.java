package com.ottokonek.ottokasir.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.model.api.response.NotificationResponseModel;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifViewHolder> {

    private Context context;
    private List<NotificationResponseModel.DataResponseModel> notifList;
    private notificationAdapterListner listener;

    public NotificationAdapter(Context context, List<NotificationResponseModel.DataResponseModel> notifList) {
        this.context = context;
        this.notifList = notifList;
    }

    public void setListener(notificationAdapterListner listener) {
        this.listener = listener;
    }

    @Override
    public NotifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_notification, null);
        return new NotificationAdapter.NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotifViewHolder holder, int position) {

        NotificationResponseModel.DataResponseModel notif = notifList.get(position);

        holder.textViewNotif.setText(notif.getTitle());
        holder.textViewKet.setText(notif.getBody());
        holder.textViewLink.setText(notif.getNotif_type());

        holder.logoNotif.setImageResource(getItemLogo(notif.getNotif_type()));
        holder.textViewLink.setOnClickListener(v -> {
            if (listener != null)
                listener.onFunctionClicked(notif.getNotif_type());
        });
    }

    private int getItemLogo(String notifType) {
        switch (notifType) {
            case "delivery":
                return R.drawable.ic_pengiriman;

            case "withdrawal":
                return R.drawable.ic_catat;

            case "transactions":
                return R.drawable.ic_catat;

            case "stocks":
                return R.drawable.ic_stock;
        }

        return R.drawable.ic_pengiriman;
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    class NotifViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.logo_notif)
        ImageView logoNotif;

        @BindView(R.id.txt_notif)
        TextView textViewNotif;

        @BindView(R.id.ket_notif)
        TextView textViewKet;

        @BindView(R.id.link_notif)
        TextView textViewLink;

        public NotifViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface notificationAdapterListner {
        void onFunctionClicked(String type);
    }


}
