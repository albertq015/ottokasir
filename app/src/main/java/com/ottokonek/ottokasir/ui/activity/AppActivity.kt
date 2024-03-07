//package id.ottopay.kasir.ui.activity
//
//import app.beelabs.com.codebase.base.BaseActivity
//import id.ottopay.kasir.presenter.manager.SessionManager
//
//open class AppActivity : BaseActivity() {
//    private var screenLocked: Boolean = false
//
//
//    override fun onStart() {
//        super.onStart()
//        //        if (SessionManager.getCredential(this).equals("") && !(this instanceof LoginActivity)) {
//        //            // session token expired
//        //            ActionUtil.Companion.logoutAction(this);
//        //        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        screenLocked = true
//    }
//
//    override fun onBackPressed() {
//        screenLocked = false
//        isBack = true
//        super.onBackPressed()
//    }
//
//    override fun finish() {
//        screenLocked = false
//        isBack = true
//        super.finish()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val isLogin = SessionManager.isLogin(this)
//
//        if (isLogin) {
//            if (!screenLocked) return
//            if (isBack) {
//                isBack = false
//                return
//            }
//
//            //TODO input pin dialog - taken out by meeting request 3/15/2019 16:30
//            //            InputPinDialog dialog = new InputPinDialog(this, R.style.CoconutDialogFullScreen);
//            //            dialog.show();
//        }
//    }
//
//    companion object {
//        private var isBack: Boolean = false
//    }
//}
