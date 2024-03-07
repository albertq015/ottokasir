//package id.ottopay.kasir.ui.dialog
//
//import android.content.Context
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.support.v7.widget.CardView
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import app.beelabs.com.codebase.base.BaseDialog
//import butterknife.BindView
//import butterknife.ButterKnife
////import com.bumptech.glide.Glide
//import id.ottopay.kasir.R
//import id.ottopay.kasir.model.miscModel.PaymentMethodModel
//import kotlinx.android.synthetic.main.dialog_payment_method.*
//import java.util.*
//
//class PaymentMethodDialog(context: Context, style: Int, paymentMethods: MutableList<PaymentMethodModel>) : BaseDialog(context, style) {
//
//    lateinit var listener: PaymentMethodDialogListener
//    var adapter: PaymentMethodAdapter = PaymentMethodAdapter(context)
//    var paymentMethods: MutableList<PaymentMethodModel> = paymentMethods
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.setGravity(Gravity.BOTTOM)
//        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
//        setContentView(R.layout.dialog_payment_method)
//        initContent()
//    }
//
//    fun setLisetener(listener: PaymentMethodDialogListener) {
//        this.listener = listener
//    }
//
//    fun initContent() {
//        window.setGravity(Gravity.BOTTOM)
//        bt_cancel.transformationMethod = null
//        bt_cancel.setOnClickListener {
//            dismiss()
//        }
//
//        adapter.listener = (object : PaymentMethodAdapter.PaymentMethodAdapterListener {
//            override fun onItemSelected(code: String?, name: String?, desc: String?, isShowNominal: Boolean) {
//                listener.onClick(code!!, name!!, desc!!, isShowNominal)
//                dismiss()
//            }
//
//        })
//
//        rv_paymentMethods.layoutManager = LinearLayoutManager(context)
//        rv_paymentMethods.adapter = this.adapter
//        adapter.replaceModel(paymentMethods)
//    }
//
//    interface PaymentMethodDialogListener {
//        fun onClick(code: String, name: String, desc: String, isShowNominal: Boolean)
//    }
//
//}
//
//class PaymentMethodViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//    @BindView(R.id.iv_logo)
//    lateinit var ivLogo: ImageView
//    @BindView(R.id.method_name)
//    lateinit var tvName: TextView
//    @BindView(R.id.method_description)
//    lateinit var tvDesc: TextView
//    @BindView(R.id.status_t)
//    lateinit var statusT: TextView
//    @BindView(R.id.status_p)
//    lateinit var statusP: TextView
//    @BindView(R.id.cv_tunai)
//    lateinit var cvClickarea: CardView
//
//    init {
//        ButterKnife.bind(this, itemView)
//    }
//}
//
//class PaymentMethodAdapter() : RecyclerView.Adapter<PaymentMethodViewholder>() {
//
//    private lateinit var mContext: Context
//    var models: MutableList<PaymentMethodModel> = ArrayList()
//
//    var lastSelected: Int = 0
//
//    var listener: PaymentMethodAdapterListener? = null
//
//    constructor(context: Context) : this() {
//        this.mContext = context
//    }
//
//    constructor(context: Context, listener: PaymentMethodAdapterListener) : this() {
//        this.mContext = context
//        this.listener = listener
//    }
//
//    constructor(context: Context, models: MutableList<PaymentMethodModel>) : this() {
//        this.mContext = context
//        this.models = models
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewholder {
//        val layout = LayoutInflater.from(parent.context).inflate(R.layout.partial_payment_cash, parent, false)
//        return PaymentMethodViewholder(layout)
//    }
//
//    fun setListen(listener: PaymentMethodAdapterListener) {
//        this.listener = listener
//    }
//
//    override fun getItemCount(): Int {
//        return models!!.size
//    }
//
//    fun replaceModel(models: MutableList<PaymentMethodModel>) {
//        this.models = models
//        notifyDataSetChanged()
//    }
//
//    fun addModels(models: MutableList<PaymentMethodModel>) {
//        this.models.addAll(models)
//        notifyDataSetChanged()
//    }
//
//    fun removeModels() {
//        this.models.clear()
//        notifyDataSetChanged()
//        View.OnClickListener { }
//    }
//
//    override fun onBindViewHolder(holder: PaymentMethodViewholder, pos: Int) {
//        var model: PaymentMethodModel = models!![pos]
//        holder.tvDesc.text = model.description
//        holder.tvName.text = model.name
////        Glide.with(mContext).load(R.drawable.ic_money).into(holder.ivLogo)
//
//        if (pos == lastSelected) {
//            holder.statusP.visibility = View.GONE
//            holder.statusT.visibility = View.VISIBLE
//        } else {
//            holder.statusP.visibility = View.VISIBLE
//            holder.statusT.visibility = View.GONE
//        }
//
//        holder.cvClickarea.setOnClickListener {
//            val tempint: Int = lastSelected
//            listener?.onItemSelected(model.code, model.name, model.description, model.name.equals("cash", true))
//            holder.statusP.visibility = View.GONE
//            holder.statusT.visibility = View.VISIBLE
//            lastSelected = pos
//            notifyItemChanged(tempint)
//        }
//    }
//
//    interface PaymentMethodAdapterListener {
//        fun onItemSelected(code: String?, name: String?, desc: String?, showNominalSelector: Boolean)
//    }
//
//}
