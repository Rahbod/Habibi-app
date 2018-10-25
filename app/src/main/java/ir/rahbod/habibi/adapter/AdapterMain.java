package ir.rahbod.habibi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.model.Categories;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.listViewHolder>{

    List<Categories> list;
    Context context;

    public AdapterMain(Context context, List<Categories> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_main, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        Picasso.with(context).load(list.get(position).getUri()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class listViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView icon;

        public listViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            icon = itemView.findViewById(R.id.imgIcon);
        }
    }
}

