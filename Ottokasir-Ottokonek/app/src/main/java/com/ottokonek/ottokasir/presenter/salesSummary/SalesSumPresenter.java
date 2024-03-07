//package id.ottopay.kasir.presenter.salesSummary;
//
//import android.content.Context;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import app.beelabs.com.codebase.base.BasePresenter;
//import app.beelabs.com.codebase.base.response.BaseResponse;
//import app.beelabs.com.codebase.support.rx.RxObserver;
//import id.ottopay.kasir.interactor.misc.MiscellaneousInteractor;
//import id.ottopay.kasir.interactor.cart.CartManager;
//import id.ottopay.kasir.model.api.request.StoreDiscountRequestModel;
//import id.ottopay.kasir.model.api.response.StoreDiscountResponseModel;
//import id.ottopay.kasir.model.db.ProductitemRealmModel;
//import id.ottopay.kasir.ui.activity.salesSummary.SalesSumViewInterface;
//import id.ottopay.kasir.utils.MoneyUtil;
//import io.realm.Realm;
//
//public class SalesSumPresenter extends BasePresenter {
//
//    private Context vContext;
//    private SalesSumViewInterface view;
//    private MiscellaneousInteractor miscInteractor;
//
//    double total;
//    double discount;
//    double discontedTotal;
//    int storeDiscount;
//
//    public SalesSumPresenter(Context vContext, SalesSumViewInterface view) {
//        this.vContext = vContext;
//        this.view = view;
//        miscInteractor = new MiscellaneousInteractor();
//    }
//
//    public void removeCartData() {
//        CartManager.removeAllCartItem();
//    }
//
//    public void loadCartData() {
//        List<Integer> productIds = new ArrayList<>();
//        List<ProductitemRealmModel> realmModels = Realm.getDefaultInstance().where(ProductitemRealmModel.class).findAll();
//        view.removeCartData();
//        total = discount = discontedTotal = storeDiscount = 0;
//        for (ProductitemRealmModel realmModel : realmModels) {
//            view.addCartDatum(realmModel.convertToPojo());
//            productIds.add(realmModel.getId());
//            total += Double.parseDouble(realmModel.getPrice()) * realmModel.getCount();
//            discount += Double.parseDouble(realmModel.getDiscount()) * realmModel.getCount();
//        }
//        discontedTotal = total - discount;
//        loadData(productIds);
//    }
//
//    private void loadData(List<Integer> data) {
////        view.showLoading();
//        StoreDiscountRequestModel requestModel = new StoreDiscountRequestModel();
//        requestModel.setItemCodes(data);
//        miscInteractor.getStoreDicount(requestModel).subscribe(new RxObserver<StoreDiscountResponseModel>() {
//            @Override
//            public void onNext(Object o) {
//                if ((((BaseResponse) o).getBaseMeta()).getCode() == 200) {
//                    storeDiscount = ((StoreDiscountResponseModel) o).getData().getStoreDiscount();
//                    if (discount > 0 && storeDiscount > 0)
//                        view.addPaymentData(MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal),
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(total),
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal - storeDiscount),
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(storeDiscount));
//                    else if (discount > 0)
//                        view.addPaymentData(MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal),
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(total),
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal - storeDiscount),
//                                null);
//                    else if (storeDiscount > 0)
//                        view.addPaymentData(MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal),
//                                null,
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal - storeDiscount),
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(storeDiscount));
//                    else
//                        view.addPaymentData(MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal),
//                                null,
//                                MoneyUtil.Companion.convertIDRCurrencyFormat(discontedTotal - storeDiscount),
//                                null);
//
//                    view.hideLoading();
//                } else {
//                    view.onApiFailed("Error", ((BaseResponse) o).getBaseMeta().getMessage());
//                    view.hideLoading();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                view.hideLoading();
//                view.onConnectionFailed(e.getLocalizedMessage());
//            }
//        });
//    }
//
//    public double getTotal() {
//        return total;
//    }
//
//    public double getDiscount() {
//        return discount;
//    }
//
//    public double getDiscontedTotal() {
//        return discontedTotal;
//    }
//
//    public int getStoreDiscount() {
//        return storeDiscount;
//    }
//
//}
