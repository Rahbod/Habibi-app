package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.model.Bill;

public class AdapterBill extends RecyclerView.Adapter<AdapterBill.listViewHolder>{

    List<Bill> list;
    Context context;

    public AdapterBill(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_bill, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.cost.setText(list.get(position).getCost());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView cost;

        public listViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            cost = itemView.findViewById(R.id.txtCost);
        }
    }
}

