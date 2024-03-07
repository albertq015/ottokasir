package com.ottokonek.ottokasir.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.utils.LogHelper
import kotlinx.android.synthetic.main.dialog_message.*
import java.lang.Exception

class MessageDialog(context: Context, val message: String, val callback: ActionMessage?): BaseDialogApp(context){

    constructor(context: Context, message: String): this(context, message, null)

    companion object{
        private val TAG: String = MessageDialog::class.java.simpleName

        fun showMessage(context: Context?, message: String){
            context?.let {
                try {
                    MessageDialog(it, message).show()
                }catch (e: Exception){
                    LogHelper(TAG, e.message).run()
                }catch (e: WindowManager.BadTokenException){
                    LogHelper(TAG, e.message).run()
                }catch (e: WindowManager.InvalidDisplayException){
                    LogHelper(TAG, e.message).run()
                }
            }
        }

        fun showMessage(context: Context?, message: String, callback: ActionMessage?){
            context?.let {
                try {
                    MessageDialog(it, message, callback).show()
                }catch (e: Exception){
                    LogHelper(TAG, e.message).run()
                }catch (e: WindowManager.BadTokenException){
                    LogHelper(TAG, e.message).run()
                }catch (e: WindowManager.InvalidDisplayException){
                    LogHelper(TAG, e.message).run()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_message)


        if(message.isNotEmpty()) tv_text.text = message

        // events

        btn_close.setOnClickListener { dismiss() }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        callback?.closeDialog()
    }

    interface ActionMessage{

        fun closeDialog()
    }
}