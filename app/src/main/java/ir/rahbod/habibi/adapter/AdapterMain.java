package ir.rahbod.habibi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.Devices;
import ir.rahbod.habibi.pages.RequestStepOneActivity;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.listViewHolder> {

    private List<Devices> list;
    private Context context;
    private int count;

    public AdapterMain(Context context, List<Devices> list, int count) {
        this.context = context;
        this.list = list;
        this.count = count;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_main, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, final int position) {
        holder.title.setText(list.get(position).title);
        Picasso.with(context).load(list.get(position).icon).into(holder.icon);
        holder.itemRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RequestStepOneActivity.class);
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_ID, list.get(position).id);
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_TITLE, list.get(position).title);
                SessionManager.getExtrasPref(context).putExtra(PutKey.SERVICE_ICON, list.get(position).icon);
                context.startActivity(intent);
            }
        });

        //tuning items to screen
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = context.getResources().getDisplayMetrics().density;
        float width = outMetrics.widthPixels / density;
        int imgSize = (int) (width / count);
        holder.icon.getLayoutParams().width = imgSize;
        holder.icon.getLayoutParams().height = imgSize;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView icon;
        private CardView itemRec;

        private listViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            icon = itemView.findViewById(R.id.imgIcon);
            itemRec = itemView.findViewById(R.id.itemRec);
        }
    }
}

