package com.ottokonek.ottokasir.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.tabs.TabLayout
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.ui.adapter.kasbon.KasbonViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_kasbon.*

class KasbonFragment : BaseLocalFragment() {

    /*var kasbonType = false

    companion object {
        const val KEY_FROM_KASBON_LUNAS = "key_kasbon_lunas"
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kasbon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val bundle = this.arguments
        if (bundle != null) {
            kasbonType = bundle.getBoolean(KEY_FROM_KASBON_LUNAS, false)
        }*/


        contentKasbon()
    }


    private fun contentKasbon() {
        val title = activity!!.findViewById<TextView>(R.id.title)
        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)

        title.visibility = View.VISIBLE
        title.setText(R.string.kasbon)

        search.visibility = View.GONE
        scanBarcode.visibility = View.GONE

        val kasbonViewPagerAdapter = KasbonViewPagerAdapter(childFragmentManager, tabLayoutKasbon.tabCount)
        viewPager.adapter = kasbonViewPagerAdapter

        tabLayoutKasbon.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutKasbon))

        /*if (kasbonType) {
            viewTabAktif.visibility = View.GONE
            viewTabLunas.visibility = View.VISIBLE

            val KasbonLunasViewPagerAdapter = KasbonLunasViewPagerAdapter(childFragmentManager, tabLayoutKasbonLunas.tabCount)
            viewPagerLunas.adapter = KasbonLunasViewPagerAdapter

            tabLayoutKasbonLunas.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPagerLunas.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
            viewPagerLunas.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutKasbonLunas))

        } else if (!kasbonType) {
            val kasbonViewPagerAdapter = KasbonViewPagerAdapter(childFragmentManager, tabLayoutKasbon.tabCount)
            viewPager.adapter = kasbonViewPagerAdapter

            tabLayoutKasbon.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutKasbon))
        }*/
    }

}