package com.ottokonek.ottokasir.ui.activity.customer

import android.os.Bundle
import android.view.MenuItem
import app.beelabs.com.codebase.support.util.CacheUtil
import com.google.android.material.tabs.TabLayout
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.adapter.CustomerViewPagerAdapter
import kotlinx.android.synthetic.main.activity_customer_main.*

class CustomerMainActivity : BaseLocalActivity() {

    var customerId = 0

    companion object {
        const val KEY_DATA = "key_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_main)

        getDataIntent()
        contentCustomerMain()
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            if (it.containsKey(KEY_DATA)) {
                customerId = it.getInt(KEY_DATA)

                CacheUtil.putPreferenceInteger(IConfig.KEY_CUSTOMER_ID,
                        customerId, this)
            }
        }
    }

    private fun contentCustomerMain() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.detail_pelanggan)
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        val customerViewPager = CustomerViewPagerAdapter(supportFragmentManager,
                tabLayoutCustomer.tabCount)
        viewPager.adapter = customerViewPager

        tabLayoutCustomer.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutCustomer))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressed()// close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}