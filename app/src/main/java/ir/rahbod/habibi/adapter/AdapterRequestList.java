package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.DbHelper;
import ir.rahbod.habibi.model.ItemRequest;
import ir.rahbod.habibi.pages.RequestInfoActivity;

public class AdapterRequestList extends RecyclerView.Adapter<AdapterRequestList.listViewHolder> {

    private List<ItemRequest> requestLists;
    private Context context;
    private DbHelper dbHelper;

    public AdapterRequestList(Context context, List<ItemRequest> requestLists) {
        this.context = context;
        this.requestLists = requestLists;
        dbHelper = new DbHelper(context);
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_request_list, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {
        holder.recItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RequestInfoActivity.class);
                context.startActivity(intent);
            }
        });
        holder.txtName.setText(dbHelper.getDevicesTitle(requestLists.get(position).deviceID));
        holder.txtDate.setText(requestLists.get(position).requestedDate);
        switch (requestLists.get(position).status) {
            case "7":
            case "6":
                holder.txtCondition.setText("پرداخت شده");
                holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_status_green));
                break;
            case "5":
                holder.txtCondition.setText("در انتظار پرداخت");
                holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_status_gray));
                break;
            case "4":
            case "3":
                holder.txtCondition.setText("در صف سرویس");
                holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_status_blue));
                break;
            case "2":
            case "1":
                holder.txtCondition.setText("در انتظار بررسی");
                holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_status_gray));
                break;
            case "-1":
                holder.txtCondition.setText("لغو شده");
                holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_status_red));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return requestLists.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtDate, txtCondition;
        private CardView recItem;

        private listViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtCondition = itemView.findViewById(R.id.txtCondition);
            recItem = itemView.findViewById(R.id.recItem);
        }
    }
}

