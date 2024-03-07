//package id.ottopay.kasir.ui.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import id.ottopay.kasir.R;
//import id.ottopay.kasir.ui.activity.salesReport.SalesDetailListActivity;
//import id.ottopay.kasir.utils.DateUtil;
//import id.ottopay.kasir.model.pojo.ReportMonthlyModel;
//import id.ottopay.kasir.utils.MoneyUtil;
//
//import static id.ottopay.kasir.IConfig.BUNDLE_ORDER_DATE_KEY;
//import static id.ottopay.kasir.IConfig.FORMAT_DATE_DD_MMMM_YYYY;
//
//public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ProductViewHolder> {
//    private Context context;
//    private List<ReportMonthlyModel> reportList;
//
//
//    public SalesReportAdapter(Context context, List<ReportMonthlyModel> reportList) {
//        this.context = context;
//        this.reportList = reportList;
//    }
//
//    @Override
//    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //inflating and returning our view holder
//        View view = LayoutInflater.from(context).inflate(R.layout.row_sales_report_item, null);
//        return new ProductViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ProductViewHolder holder, int position) {
//        //getting the product of the specified position
//        ReportMonthlyModel model = reportList.get(position);
//
//        long orderDate = model.getOrder_date();
//
//        //binding the data with the viewholder views
//        holder.salesDate.setText(DateUtil.convertFromEpochToString(model.getOrder_date(), FORMAT_DATE_DD_MMMM_YYYY));
//
//        holder.salesAmount.setText(model.getTotal_amount().intValue() + "");
//        holder.salesPaid.setText(MoneyUtil.Companion.convertIDRCurrencyFormat(model.getTotal_paid()));
//        holder.salesDetailAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, SalesDetailListActivity.class);
//                intent.putExtra(BUNDLE_ORDER_DATE_KEY, orderDate);
//
//                context.startActivity(intent);
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return reportList.size();
//    }
//
//    class ProductViewHolder extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.sales_paid)
//        TextView salesPaid;
//        @BindView(R.id.sales_date)
//        TextView salesDate;
//        @BindView(R.id.sales_amount)
//        TextView salesAmount;
//        @BindView(R.id.sales_detail_action)
//        TextView salesDetailAction;
//
//        public ProductViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//
//            itemView.setTag(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SalesDetailListActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//
//        }
//
//    }
//}
