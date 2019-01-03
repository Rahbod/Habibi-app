package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.Factor;
import ir.rahbod.habibi.model.SubServiceItem;
import ir.rahbod.habibi.pages.RequestStepOneActivity;

public class AdapterBottomSheet extends RecyclerView.Adapter<AdapterBottomSheet.listViewHolder> {

    private List<SubServiceItem> list;
    private Context context;

    public AdapterBottomSheet(Context context, List<SubServiceItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_bottom_sheet_main, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RequestStepOneActivity.class);
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_ID, list.get(position).id);
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_TITLE, list.get(position).title);
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_ICON, list.get(position).icon);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ConstraintLayout layout;

        private listViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtSubService);
            layout = itemView.findViewById(R.id.mainLayout);
        }
    }
}

