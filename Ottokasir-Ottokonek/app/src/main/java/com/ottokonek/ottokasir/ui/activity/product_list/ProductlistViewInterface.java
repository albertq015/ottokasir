package com.ottokonek.ottokasir.ui.activity.product_list;

import java.util.List;

import com.ottokonek.ottokasir.model.miscModel.ProductItemModel;
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface;

public interface ProductlistViewInterface extends ViewBaseInterface {
    void loadProducts(List<ProductItemModel> models);

    void addProduct(List<ProductItemModel> models);

    void clearProducts();

    void showProgressbar();

    void hideProgressbar();

    void callProductlistNofilter();

    void callProductlistFiltered(String query, String category);

    void showFooter();

    void hideFooter();

    void refreshView();

    void setStatusAPI(Boolean status);

    Boolean getStatusAPI();

    void addFooterData(String itemCount, String total, String discount);

    void showClearSearch();
    void hideClearSearch();
}
