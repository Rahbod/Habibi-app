package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.Address;

public class AdapterRequestStepThere extends RecyclerView.Adapter<AdapterRequestStepThere.listViewHolder> {

    private List<Address> list;
    private Context context;
    private int lastPosition = -1;

    public AdapterRequestStepThere(Context context, List<Address> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_request_there, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final listViewHolder holder, final int position) {
        holder.txt.setText(list.get(position).getAddress());
        holder.radioButton.setChecked(lastPosition == position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView txt;
        private RadioButton radioButton;
        private CardView linItem;

        private listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            radioButton = itemView.findViewById(R.id.radioButton);
            linItem = itemView.findViewById(R.id.linItem);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastPosition = getAdapterPosition();
                    SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_Address_ID, list.get(getAdapterPosition()).getId());
                    SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_Address, txt.getText().toString());
                    notifyDataSetChanged();
                }
            });

            linItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastPosition = getAdapterPosition();
                    SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_Address_ID, list.get(getAdapterPosition()).getId());
                    SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_Address, txt.getText().toString());
                    notifyDataSetChanged();
                }
            });
        }
    }
}

