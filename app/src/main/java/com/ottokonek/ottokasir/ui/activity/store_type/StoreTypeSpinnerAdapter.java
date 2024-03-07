package com.ottokonek.ottokasir.ui.activity.store_type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.model.api.response.StoreTypeListResponseModel;

public class StoreTypeSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private List<StoreTypeListResponseModel.DataBean> listStoreType;
    Context context;

    public StoreTypeSpinnerAdapter(Context context, List<StoreTypeListResponseModel.DataBean> listStoreType) {
        this.context = context;
        this.listStoreType = listStoreType;
    }

    @Override
    public int getCount() {
        return listStoreType.size();
    }

    @Override
    public Object getItem(int position) {
        return listStoreType.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private View getCustomView(int position, View convertView, ViewGroup parent) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_spinner_store_type, parent, false);
        TextView tvStoreType = layout.findViewById(R.id.tvStoreType);

        StoreTypeListResponseModel.DataBean storeType = listStoreType.get(position);
        tvStoreType.setText(storeType.getName());

        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
