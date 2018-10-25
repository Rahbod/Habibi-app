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
import ir.rahbod.habibi.model.Request;

public class AdapterRequestList extends RecyclerView.Adapter<AdapterRequestList.listViewHolder>{

    List<Request> list;
    Context context;

    public AdapterRequestList(Context context, List<Request> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_request_list, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {
        holder.txtCondition.setText(list.get(position).getCondition());
        //change background color condition
        if (list.get(position).getCondition().equals("انجام شده"))
            holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_done));
        else if (list.get(position).getCondition().equals("بررسی شده"))
            holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_reviewed));
        else if (list.get(position).getCondition().equals("در حال بررسی"))
            holder.txtCondition.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_pending));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtModel;
        TextView txtCondition;

        public listViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtModel = itemView.findViewById(R.id.txtModel);
            txtCondition = itemView.findViewById(R.id.txtCondition);
        }
    }
}

