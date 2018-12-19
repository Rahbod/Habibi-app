package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.model.Factor;

public class AdapterFactor extends RecyclerView.Adapter<AdapterFactor.listViewHolder> {

    private List<Factor> list;
    private Context context;

    public AdapterFactor(Context context, List<Factor> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_factor, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.cost.setText(list.get(position).cost);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView title, cost;

        private listViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            cost = itemView.findViewById(R.id.txtCost);
        }
    }
}

