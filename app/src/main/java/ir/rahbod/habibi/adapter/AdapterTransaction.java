package ir.rahbod.habibi.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.model.Bill;
import ir.rahbod.habibi.model.Transaction;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.listViewHolder> {

    private List<Transaction> list;
    private Context context;

    public AdapterTransaction(Context context, List<Transaction> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_bill, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, final int position) {
        if (list.get(position).status.equals("paid")) {
            holder.title.setText("پرداخت شده");
            holder.title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.title.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_status_green));
        } else {
            holder.title.setText("پرداخت نشده");
            holder.title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.title.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_status_red));
        }
        holder.cost.setText(list.get(position).amount);
        holder.cost.setTextColor(context.getResources().getColor(R.color.mainColor));
        holder.date.setText(list.get(position).date);
        holder.recItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_transaction, null);
                dialog.setContentView(view);
                TextView txtStatus = view.findViewById(R.id.txtStatus);
//                TextView txtCode = view.findViewById(R.id.txtCode);
                TextView txtAmount = view.findViewById(R.id.txtAmount);
                ImageView btnCancel = view.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (list.get(position).status.equals("paid")) {
                    txtStatus.setText("پرداخت شده");
                    txtStatus.setTextColor(context.getResources().getColor(R.color.green));
                } else {
                    txtStatus.setText("پرداخت نشده");
                    txtStatus.setTextColor(context.getResources().getColor(R.color.secondColor));
                }
//                txtCode.setText(list.get(position).code);
                txtAmount.setText(list.get(position).amount);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView title, cost, date;
        private CardView recItem;

        private listViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            date = itemView.findViewById(R.id.txtDate);
            cost = itemView.findViewById(R.id.txtCost);
            recItem = itemView.findViewById(R.id.recItem);
        }
    }
}

