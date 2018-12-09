package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.GetTime;

public class AdapterGetTime extends RecyclerView.Adapter<AdapterGetTime.listViewHolder> {

    private List<GetTime> list;
    private Context context;
    private int lastPosition = -1;

    public AdapterGetTime(Context context, List<GetTime> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_get_time, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final listViewHolder holder, final int position) {
        holder.startTime.setText(list.get(position).startTime + "");
        holder.endTime.setText(list.get(position).endTime + "");
        if (lastPosition == position) {
            holder.item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_rec_time_selected));
            holder.startTime.setTextColor(Color.WHITE);
            holder.endTime.setTextColor(Color.WHITE);
            holder.txt.setTextColor(Color.WHITE);
            if (position == 0)
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_TIME, "night");
            else if (position == 1)
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_TIME, "pm");
            else
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_TIME, "am");

        } else {
            holder.item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_rec_time));
            holder.startTime.setTextColor(context.getResources().getColor(R.color.mainColor));
            holder.endTime.setTextColor(context.getResources().getColor(R.color.mainColor));
            holder.txt.setTextColor(context.getResources().getColor(R.color.mainColor));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView startTime, endTime, txt;
        private LinearLayout item;

        private listViewHolder(View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            item = itemView.findViewById(R.id.recItem);
            txt = itemView.findViewById(R.id.txt);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}

