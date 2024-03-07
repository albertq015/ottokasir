package com.ottokonek.ottokasir.ui.activity.product_list.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.ottokonek.ottokasir.R;
import com.ottokonek.ottokasir.dao.cart.CartManager;
import com.ottokonek.ottokasir.model.miscModel.ProductItemModel;
import com.ottokonek.ottokasir.utils.MoneyUtil;
import com.ottokonek.ottokasir.utils.TextUtil;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.katzeholder> {
    private Context mContext;
    private productAdapterListener listener;

    List<ProductItemModel> models = new ArrayList<>();

    public ProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ProductAdapter(Context mContext, List<ProductItemModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    public void setListener(productAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public katzeholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_list_product, viewGroup, false);
        return new katzeholder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull katzeholder holder, int i) {
        holder.prodCount = 0;
        holder.model = models.get(i);
        holder.tvProductname.setText(holder.model.getName());

        int itemCount = CartManager.getProductQty(holder.model.getStock_id());

        if (itemCount > 0) {
            holder.model.setCount(itemCount);
            holder.prodCount = itemCount;
            holder.flNegative.setVisibility(View.VISIBLE);
            holder.tvProdcount.setText(holder.prodCount + "");

        } else {
            holder.prodCount = 0;
            holder.flNegative.setVisibility(View.VISIBLE);
            holder.tvProdcount.setText("");
        }

        if (!holder.model.getBarcode().equals("")) {
            holder.ivBarcodeProduct.setVisibility(View.VISIBLE);
        } else {
            holder.ivBarcodeProduct.setVisibility(View.GONE);
        }


        if (holder.model.getPrice().equals("-1.00")) {
            holder.tvPrice.setText(R.string.belum_ada_harga);
        } else {
            holder.tvPrice.setText(MoneyUtil.Companion.convertCurrencyPHP1(holder.model.getPrice()));
        }

        if (holder.model.getStocks() > holder.model.getStock_alert() && holder.model.isStock_active()) {
            holder.tvProdcount.setFocusable(true);
            holder.tvProdcount.setFocusableInTouchMode(true);
            holder.tvProdcount.setCursorVisible(true);
            holder.tvProdcount.setClickable(true);

            holder.tvStocksAlert.setVisibility(View.GONE);
            holder.tvStocks.setVisibility(View.VISIBLE);
            holder.tvStocks.setText(mContext.getString(R.string.stok_spasi) + holder.model.getStocks());
            holder.tvStocks.setTextColor(mContext.getResources().getColor(R.color.green_two));

        } else if (holder.model.getStocks() > 0 && holder.model.getStocks() <= holder.model.getStock_alert() && holder.model.isStock_active()) {
            holder.tvProdcount.setFocusable(true);
            holder.tvProdcount.setFocusableInTouchMode(true);
            holder.tvProdcount.setCursorVisible(true);
            holder.tvProdcount.setClickable(true);

            holder.tvStocksAlert.setVisibility(View.VISIBLE);
            holder.tvStocksAlert.setText(R.string.stok_menipis);
            holder.tvStocks.setText(mContext.getString(R.string.stok_spasi) + holder.model.getStocks());
            holder.tvStocks.setTextColor(mContext.getResources().getColor(R.color.red));

        } else if (holder.model.getStocks() == 0 && holder.model.isStock_active()) {
            holder.tvProdcount.setFocusable(false);
            holder.tvProdcount.setFocusableInTouchMode(false);
            holder.tvProdcount.setCursorVisible(false);
            holder.tvProdcount.setClickable(false);

            holder.tvStocksAlert.setVisibility(View.VISIBLE);
            holder.tvStocksAlert.setText(R.string.stok_habis);
            holder.tvStocks.setText(mContext.getString(R.string.stok_spasi) + holder.model.getStocks());
            holder.tvStocks.setTextColor(mContext.getResources().getColor(R.color.red));
        } else if (!holder.model.isStock_active() && !holder.model.getPrice().equals("-1.00")) {
            holder.tvProdcount.setFocusable(true);
            holder.tvProdcount.setFocusableInTouchMode(true);
            holder.tvProdcount.setCursorVisible(true);
            holder.tvProdcount.setClickable(true);
            holder.tvStocksAlert.setVisibility(View.GONE);

            holder.tvStocks.setVisibility(View.GONE);
            /*holder.tvStocks.setText(mContext.getString(R.string.stok_));
            holder.tvStocks.setTextColor(mContext.getResources().getColor(R.color.green_two));*/
        } else if (!holder.model.isStock_active() && holder.model.getPrice().equals("-1.00")) {
            holder.tvProdcount.setFocusable(false);
            holder.tvProdcount.setFocusableInTouchMode(false);
            holder.tvProdcount.setCursorVisible(false);
            holder.tvProdcount.setClickable(false);
            holder.tvStocksAlert.setVisibility(View.GONE);

            holder.tvStocks.setVisibility(View.GONE);
            /*holder.tvStocks.setText(mContext.getString(R.string.stok_));
            holder.tvStocks.setTextColor(mContext.getResources().getColor(R.color.green_two));*/
        }


        Glide.with(mContext)
                .load(holder.model.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_product)
                .into(holder.ivProduct);

        holder.tvProdcount.addTextChangedListener(new TextUtil() {
            @Override
            public void onTextChanged(@Nullable CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s.length() != 0) {
                    String prodCountString = (holder.tvProdcount.getText().toString().isEmpty()) ? "0" : holder.tvProdcount.getText().toString();
                    holder.prodCount = Integer.valueOf(prodCountString);
                    holder.model.setCount(holder.prodCount);

                    if (holder.tvProdcount.getText().toString().equals("0") || holder.tvProdcount.getText().toString().equals("")) {
                        CartManager.removeCartItem(holder.model.getStock_id());
                        holder.prodCount = 0;
                        holder.tvProdcount.setText("");
                        listener.onProductCartcountChanged();
                    } else if (holder.prodCount > holder.model.getStocks() && holder.model.isStock_active()) {
                        Toast.makeText(mContext, mContext.getString(R.string.maaf_stok_barang_yang_tersedia) + holder.model.getStocks(), Toast.LENGTH_SHORT).show();
                        CartManager.removeCartItem(holder.model.getStock_id());
                        holder.prodCount = 0;
                        listener.onProductCartcountChanged();
                    } else if (holder.model.getStocks() == 0 && holder.model.getPrice().equals("-1.00") && holder.model.isStock_active()) {
                        Toast.makeText(mContext, R.string.maaf_stok_barang_kosong_harga_dan_belum_ada_harga, Toast.LENGTH_SHORT).show();
                        CartManager.removeCartItem(holder.model.getStock_id());
                        holder.prodCount = 0;
                        listener.onProductCartcountChanged();
                    } else if (holder.model.getPrice().equals("-1.00")) {
                        Toast.makeText(mContext, R.string.maaf_belum_ada_harga, Toast.LENGTH_SHORT).show();
                        CartManager.removeCartItem(holder.model.getStock_id());
                        holder.prodCount = 0;
                        listener.onProductCartcountChanged();
                    } else if (holder.model.getStocks() == 0 && holder.model.isStock_active()) {
                        Toast.makeText(mContext, R.string.maaf_stok_barang_kosong, Toast.LENGTH_SHORT).show();
                        CartManager.removeCartItem(holder.model.getStock_id());
                        holder.prodCount = 0;
                        listener.onProductCartcountChanged();
                    } else {
                        CartManager.putCartItem(holder.model);
                        holder.prodCount = Integer.valueOf(holder.tvProdcount.getText().toString());
                        listener.onProductCartcountChanged();
                    }


                } else {
                    holder.prodCount = 0;
                    CartManager.removeCartItem(holder.model);
                    if (listener != null)
                        listener.onProductCartcountChanged();
                }

            }
        });

        View.OnClickListener listener = v -> {
            switch (v.getId()) {
                case (R.id.fl_negative):
                    if (holder.prodCount > 0) {
                        holder.prodCount -= 1;
                        holder.model.setCount(holder.prodCount);
                        CartManager.putCartItem(holder.model);
                    }

                    if (holder.prodCount == 0) {
                        holder.flNegative.setVisibility(View.VISIBLE);
                        holder.tvProdcount.setText("");
                        CartManager.removeCartItem(holder.model.getStock_id());
                    }

                    if (holder.model.getCount() == 0)
                        CartManager.removeCartItem(holder.model.getStock_id());

                    holder.tvProdcount.setText(holder.prodCount + "");

                    if (this.listener != null)
                        this.listener.onProductCartcountChanged();
                    break;
                case (R.id.fl_positive):

                    if (!holder.model.getPrice().equals("-1.00")) {

                        if (holder.model.isStock_active()) {
                            if (holder.model.getCount() < holder.model.getStocks()) {
                                holder.prodCount += 1;
                                holder.flNegative.setVisibility(View.VISIBLE);
                                holder.tvProdcount.setText(holder.prodCount + "");
                                holder.model.setCount(holder.prodCount);
                                CartManager.putCartItem(holder.model);
                                if (this.listener != null)
                                    this.listener.onProductCartcountChanged();
                            } else {
                                Toast.makeText(mContext, mContext.getString(R.string.maaf_stok_barang_yang_tersedia) + holder.model.getStocks(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            holder.prodCount += 1;
                            holder.flNegative.setVisibility(View.VISIBLE);
                            holder.tvProdcount.setText(holder.prodCount + "");
                            holder.model.setCount(holder.prodCount);
                            CartManager.putCartItem(holder.model);
                            if (this.listener != null)
                                this.listener.onProductCartcountChanged();
                        }

                    } else if (holder.model.getStocks() == 0 && holder.model.getPrice().equals("-1.00") && holder.model.isStock_active()) {
                        Toast.makeText(mContext, R.string.maaf_stok_barang_kosong_harga_dan_belum_ada_harga, Toast.LENGTH_SHORT).show();

                    } else if (holder.model.getPrice().equals("-1.00")) {
                        Toast.makeText(mContext, R.string.maaf_belum_ada_harga, Toast.LENGTH_SHORT).show();

                    } else if (holder.model.getStocks() == 0 && holder.model.isStock_active()) {
                        Toast.makeText(mContext, R.string.maaf_stok_barang_kosong, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        };
        holder.flNegative.setOnClickListener(listener);

        holder.flPositive.setOnClickListener(listener);


    }

    public void fillModels(List<ProductItemModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    public void addModels(List<ProductItemModel> models) {
        this.models.addAll(models);
        notifyDataSetChanged();
    }

    public void removeModels() {
        this.models.clear();
        //CartManager.removeAllCartItem();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class katzeholder extends RecyclerView.ViewHolder {

        int prodCount = 0;
        ProductItemModel model;

        //@BindView(R.id.ll_1)
        //View ll1;
        @BindView(R.id.iv_product)
        ImageView ivProduct;
        @BindView(R.id.tv_productname)
        TextView tvProductname;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        //@BindView(R.id.tv_discount)
        //TextView tvDiscount;
        @BindView(R.id.fl_negative)
        View flNegative;
        @BindView(R.id.fl_positive)
        View flPositive;

        @BindView(R.id.et_prodcount)
        EditText tvProdcount;
        @BindView(R.id.iv_barcode_product)
        ImageView ivBarcodeProduct;
        //@BindView(R.id.cl_hitbox)
        //ConstraintLayout cl_hitbox;
        @BindView(R.id.tv_stock)
        TextView tvStocks;
        @BindView(R.id.tv_stock_alert)
        TextView tvStocksAlert;


        public katzeholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface productAdapterListener {
        void onProductCartcountChanged();
    }
}
