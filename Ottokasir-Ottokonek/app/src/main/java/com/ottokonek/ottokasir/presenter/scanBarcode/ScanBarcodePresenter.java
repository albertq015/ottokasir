//package id.ottopay.kasir.presenter.scanBarcode;
//
//import android.content.Context;
//
//import app.beelabs.com.codebase.base.BasePresenter;
//import app.beelabs.com.codebase.base.response.BaseResponse;
//import app.beelabs.com.codebase.support.rx.RxObserver;
//import id.ottopay.kasir.interactor.cart.CartManager;
//import id.ottopay.kasir.interactor.scanBarcode.ScanBarcodeInteractor;
//import id.ottopay.kasir.model.api.response.BarcodeResponseModel;
//import id.ottopay.kasir.model.miscModel.ProductItemModel;
//import id.ottopay.kasir.ui.activity.scanBarcode.ScanGeneralViewInterface;
//
//public class ScanBarcodePresenter extends BasePresenter {
//    Context mContext;
//    ScanGeneralViewInterface view;
//    ScanBarcodeInteractor interactor;
//
//
//    public ScanBarcodePresenter(Context vContext, ScanGeneralViewInterface view) {
//        mContext = vContext;
//        this.view = view;
//        initContent();
//    }
//
//    private void initContent() {
//        interactor = new ScanBarcodeInteractor(mContext);
//    }
//
//
//    public void getItemFromCode(String code) {
//        view.showLoading();
//        interactor.callScanBarcode(code).subscribe(new RxObserver<BarcodeResponseModel>() {
//            @Override
//            public void onNext(Object o) {
//                if ((((BaseResponse) o).getBaseMeta()).getCode() == 200) {
//                    ((BarcodeResponseModel) o).getData();
//                    addAnItem(((BarcodeResponseModel) o).getData());
//                    view.hideLoading();
//                    view.goToProductlist();
//                } else if (((BaseResponse) o).getBaseMeta().getCode() == 404) {
//                    view.hideLoading();
//                    view.onApiFailed("Error", "Barcode Tidak dikenali");
//                    view.startScanFunctionality();
//                } else {
//                    view.hideLoading();
//                    view.onApiFailed("Error", ((BaseResponse) o).getBaseMeta().getMessage());
//                    view.startScanFunctionality();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                view.hideLoading();
//                view.onConnectionFailed(e.getLocalizedMessage());
//                view.startScanFunctionality();
//            }
//        });
//    }
//
//    private void addAnItem(ProductItemModel model) {
//        if (model != null) {
//            if (CartManager.getProductQty(model.getId()) > 0) {
//                CartManager.addAnItemFromCart(model);
//            } else {
//                model.setCount(1);
//                CartManager.putCartItem(model);
//            }
//        }
//    }
//
//
//}
