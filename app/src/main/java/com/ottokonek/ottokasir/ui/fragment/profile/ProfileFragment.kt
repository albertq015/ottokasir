package com.ottokonek.ottokasir.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.support.util.CacheUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ottokonek.ottokasir.IConfig.Companion.SESSION_ADDRESS
import com.ottokonek.ottokasir.IConfig.Companion.SESSION_IMAGE
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.sync.SyncManager
import com.ottokonek.ottokasir.model.api.request.ResetStoreTypeRequestModel
import com.ottokonek.ottokasir.model.api.response.ResetStoreTypeResponseModel
import com.ottokonek.ottokasir.presenter.ProfilePresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity
import com.ottokonek.ottokasir.ui.dialog.ExitAppLogoutDialog
import com.ottokonek.ottokasir.ui.dialog.store_type.ResetStoreTypeCallback
import com.ottokonek.ottokasir.ui.dialog.store_type.ResetStoreTypeConfirmationDialog
import com.ottokonek.ottokasir.ui.dialog.store_type.ResetStoreTypeSuccessDialog
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseLocalFragment(), ResetStoreTypeCallback, IView, ProfileIView {

    private val profilePresenter = ProfilePresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        initContent()

    }

    fun initContent() {
        btnLogout.setOnClickListener {
            val dialog = ExitAppLogoutDialog(requireActivity(), R.style.CustomDialog)
            dialog.show()
        }

        tvResetStoreType.setOnClickListener {
            val dialog = ResetStoreTypeConfirmationDialog(requireActivity(), R.style.style_bottom_sheet, this)
            dialog.show()
        }
    }

    override fun resetStoreTypeYes() {
        callApiResetStoreType()
    }

    override fun resetStoreTypeSuccess() {
        SessionManager.clearSessionLogin(requireActivity())
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun setUpUI() {
        val modelSync = SyncManager.getSyncData()
        val presenter: ProfilePresenter = ProfilePresenter(requireActivity() as BaseActivity)
        val title = activity!!.findViewById<TextView>(R.id.title)
        title.visibility = View.VISIBLE
        title.setText(R.string.profil)
        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        search.visibility = View.GONE
        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)
        scanBarcode.visibility = View.GONE

        tvMerchantName.text = SessionManager.getName(context)
        tvMerchantId.text = SessionManager.getMerchantId(context)
        tvName.text = SessionManager.getMerchantName(context)
        tvPhoneNumber.text = SessionManager.getPhone(context)
        tvEmail.text = SessionManager.getEmail(context)
        tvStoreTypeName.text = SessionManager.getStoreTypeName(context)
        tvMerchantName2.text = SessionManager.getName(context)

        /*tvName.text = CacheUtil.getPreferenceString(SESSION_MERCHANT_NAME, context)
        tvEmail.text = CacheUtil.getPreferenceString(IConfig.SESSION_EMAIL, context)
        tvMerchantName.text = CacheUtil.getPreferenceString(SESSION_NAME, context)
        tvPhoneNumber.text = CacheUtil.getPreferenceString(IConfig.SESSION_PHONE, context)
        tvStoreTypeName.text = CacheUtil.getPreferenceString(IConfig.SESSION_BUSINESS_TYPE, context)
        tvMerchantName2.text = CacheUtil.getPreferenceString(SESSION_NAME, context)
        tvMerchantId.text = CacheUtil.getPreferenceString(SESSION_MERCHANT_ID, context)*/


        Glide.with(requireActivity()).load(CacheUtil.getPreferenceString(SESSION_IMAGE, context))
                .apply(RequestOptions().placeholder(R.drawable.ic_profile_placeholder).error(R.drawable
                        .ic_profile_placeholder)).into(ivProfile)


        tvMerchantAddress.text = CacheUtil.getPreferenceString(SESSION_ADDRESS, context)
    }


    /**
     * Start Call Api Reset Store Type
     * */
    private fun callApiResetStoreType() {
        showLoading()

        val data = ResetStoreTypeRequestModel()
        data.phone = SessionManager.getPhone(context)
        profilePresenter.onResetStoreType(data, requireActivity() as BaseActivity)
    }

    override fun onSuccessResetStoreType(result: ResetStoreTypeResponseModel) {
        hideLoading()

        val dialog = ResetStoreTypeSuccessDialog(requireActivity(), R.style.style_bottom_sheet, this)
        dialog.show()
    }

    /**
     * End Call Api Reset Store Type
     * */


    override fun getBaseActivity(): BaseActivity {
        return requireActivity() as BaseActivity
    }


}
