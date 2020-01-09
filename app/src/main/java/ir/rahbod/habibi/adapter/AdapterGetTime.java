package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.GetTime;

public class AdapterGetTime extends RecyclerView.Adapter<AdapterGetTime.listViewHolder> {

    private List<GetTime> list;
    private Context context;
    private int lastPosition = -1;
    private String nowTime;
    private boolean checkTime;

    public AdapterGetTime(Context context, List<GetTime> list, boolean checkTime) {
        this.context = context;
        this.list = list;
        this.checkTime = checkTime;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        nowTime = formatter.format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_get_time, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final listViewHolder holder, final int position) {
        if (checkTime) {
            if (Integer.valueOf(nowTime) >= 8)
                list.get(2).setCheckTime(true);
            if (Integer.valueOf(nowTime) >= 12) {
                list.get(2).setCheckTime(true);
                list.get(1).setCheckTime(true);
            }
            if (Integer.valueOf(nowTime) >= 20) {
                list.get(2).setCheckTime(true);
                list.get(1).setCheckTime(true);
                list.get(0).setCheckTime(true);
            }
        }
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
        if (list.get(position).isCheckTime()) {
            holder.item.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_rec_time_disable));
            holder.startTime.setTextColor(context.getResources().getColor(R.color.gray2));
            holder.endTime.setTextColor(context.getResources().getColor(R.color.gray2));
            holder.txt.setTextColor(context.getResources().getColor(R.color.gray2));
            holder.item.setEnabled(false);
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

