package com.ottokonek.ottokasir.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import kotlinx.android.synthetic.main.dialog_sorting.ivClose
import kotlinx.android.synthetic.main.dialog_sorting_customer_kasbon.*

class SortingCustomerKasbonDialog(@NonNull context: Context, style: Int, private val callbackSorting: ISortingView) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sorting_customer_kasbon)
        setCancelable(false)

        tvTertinggi.setOnClickListener {
            callbackSorting.onSelectedSorting("DESC")
            this.dismiss()
        }

        tvTerendah.setOnClickListener {
            callbackSorting.onSelectedSorting("ASC")
            this.dismiss()
        }

        ivClose.setOnClickListener {
            this.dismiss()
        }

    }

    override fun onBackPressed() {
        this.dismiss()
        super.onBackPressed()
    }

    /**
     * Interface
     * */
    interface ISortingView : ViewBaseInterface {
        fun onSelectedSorting(sortingBy: String)
    }
}