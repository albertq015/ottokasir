package com.ottokonek.ottokasir.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.ottokonek.ottokasir.R

class PanduanFragment : BaseLocalFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panduan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentPanduan()
        actionPanduan()

    }

    private fun contentPanduan() {
        val title = activity!!.findViewById<TextView>(R.id.title)
        val search = activity!!.findViewById<EditText>(R.id.et_search)
        val scanBarcode = activity!!.findViewById<ImageView>(R.id.tvSorting)

        title.visibility = View.VISIBLE
        title.text = "Panduan OttoKasir"

        search.visibility = View.GONE
        scanBarcode.visibility = View.GONE
    }


    private fun actionPanduan() {

    }
}