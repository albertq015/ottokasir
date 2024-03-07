package com.ottokonek.ottokasir.ui.fragment.kasbon.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import kotlinx.android.synthetic.main.fragment_kasbon_onboarding.*

class KasbonOnboardingTwoActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_kasbon_onboarding)

        contentOnboarding()
    }

    private fun contentOnboarding() {

        iv_onboarding.setImageResource(R.drawable.kasbon_onboarding_en_2)
        title_onboarding.setText(R.string.title_onboarding_kasbon_two)
        description_onboarding.setText(R.string.description_onboarding_kasbon_two)
        steps_onboarding.setText(R.string.step_onboarding_two)


        back_onboarding.setOnClickListener {
            onBackPressed()
        }

        lewati_onboarding.setOnClickListener {
            SessionManager.putIsNotFirstTimeOnboardingKasbon(true, this)
            val intent = Intent(this, ProductListActivity::class.java)
            intent.putExtra(ProductListActivity.KEY_FROM_ONBOARDING_KASBON, true)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        lanjut_onboarding.setOnClickListener {
            val intent = Intent(this, KasbonOnboardingThreeActivity::class.java)
            startActivity(intent)
        }

    }
}