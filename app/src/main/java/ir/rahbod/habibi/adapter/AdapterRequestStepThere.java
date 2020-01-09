package ir.rahbod.habibi.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.Address;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRequestStepThere extends RecyclerView.Adapter<AdapterRequestStepThere.listViewHolder> {

    public interface EditItemClickListener {
        void editAddress(int ID, String address, String phone);
    }

    private List<Address> list;
    private Context context;
    private int lastPosition = -1;
    private TextView txt;
    private LinearLayout linTitle;
    private EditItemClickListener listener;

    public AdapterRequestStepThere(Context context, List<Address> list, TextView txt, LinearLayout linTitle, EditItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.txt = txt;
        this.linTitle = linTitle;
        this.listener = listener;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_request_there, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final listViewHolder holder, final int position) {
        holder.txt.setText(list.get(position).getAddress());
        holder.radioButton.setChecked(SessionManager.getExtrasPref(context).getInt(PutKey.SERVICE_Address_ID) == list.get(position).getId());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAddress(list.get(position).getId(), position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddress(list.get(position).getId(), list.get(position).getAddress(), list.get(position).getTelephone());
            }
        });
    }

    private void editAddress(int ID, String address, String phone) {
        listener.editAddress(ID, address, phone);
    }

    private void removeAddress(int ID, final int position) {
        MyDialog.show(context);
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        Address address = new Address();
        address.setId(ID);
        call.removeAddress(address).enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                MyDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    if (list.isEmpty()) {
                        txt.setVisibility(View.VISIBLE);
                        linTitle.setVisibility(View.GONE);
                    }
                } else
                    Toast.makeText(context, "خطا در اتصال به شبکه لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                MyDialog.dismiss();
                Toast.makeText(context, "خطا در اتصال به شبکه لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder {

        private TextView txt;
        private RadioButton radioButton;
        private CardView linItem;
        private ImageView remove, edit;

        private listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            radioButton = itemView.findViewById(R.id.radioButton);
            linItem = itemView.findViewById(R.id.linItem);
            remove = itemView.findViewById(R.id.removeAddress);
            edit = itemView.findViewById(R.id.editAddress);
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

