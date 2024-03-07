package com.ottokonek.ottokasir.ui.fragment.manage_product.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import kotlinx.android.synthetic.main.fragment_kasbon_onboarding.*

class ManageProductOnboardingOne : BaseLocalFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_manage_product_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentOnboarding()
    }

    private fun contentOnboarding() {
        val content = requireActivity().findViewById<CoordinatorLayout>(R.id.content)
        content.visibility = View.GONE

        iv_onboarding.setImageResource(R.drawable.product_onboarding_en_1)
        title_onboarding.setText(R.string.title_onboarding_manage_one)
        description_onboarding.setText(R.string.description_onboarding_manage_one)
        steps_onboarding.setText(R.string.step_onboarding_manage_one)


        back_onboarding.visibility = View.GONE
        /*back_onboarding.setOnClickListener {
            SessionManager.putIsNotFirstTimeOnboardingManageProduct(true, requireActivity())
            val intent = Intent(requireActivity(), ProductListActivity::class.java)
            intent.putExtra(ProductListActivity.KEY_FROM_ONBOARDING_MANAGE_PRODUCT, true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }*/

        lewati_onboarding.setOnClickListener {
            SessionManager.putIsNotFirstTimeOnboardingManageProduct(true, requireActivity())
            val intent = Intent(requireActivity(), ProductListActivity::class.java)
            intent.putExtra(ProductListActivity.KEY_FROM_ONBOARDING_MANAGE_PRODUCT, true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        lanjut_onboarding.setOnClickListener {
            val intent = Intent(requireActivity(), ManageProductOnboardingTwoActivity::class.java)
            startActivity(intent)
        }

    }
}