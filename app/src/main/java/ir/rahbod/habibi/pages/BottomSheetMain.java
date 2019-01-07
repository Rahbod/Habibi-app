package ir.rahbod.habibi.pages;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterBottomSheet;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.controller.MyApp;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.SubService;
import ir.rahbod.habibi.model.SubServiceItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetMain extends BottomSheetDialogFragment {

    private List<SubServiceItem> list;
    private static BottomSheetMain bottomSheetMain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottom_sheet_main, container, false);
        bottomSheetMain = this;
        final RecyclerView recyclerView = view.findViewById(R.id.recBottomSheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApp.context));
        list = new ArrayList<>();
        MyDialog.show(getActivity());
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        SubServiceItem subServiceItem = new SubServiceItem();
        subServiceItem.parentID = SessionManager.getExtrasPref(getActivity()).getInt(PutKey.PARENT_ID);
        call.getSubService(subServiceItem).enqueue(new Callback<SubService>() {
            @Override
            public void onResponse(Call<SubService> call, Response<SubService> response) {
                if (response.isSuccessful()) {
                    list.addAll(response.body().list);
                    AdapterBottomSheet adapter = new AdapterBottomSheet(MyApp.context, list, bottomSheetMain);
                    recyclerView.setAdapter(adapter);
                    MyDialog.dismiss();
                }
                else {
                    getActivity().finish();
                    MyDialog.dismiss();
                    Toast.makeText(getActivity(), "خطا در اتصال به شبکه، لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SubService> call, Throwable t) {
                getActivity().finish();
                MyDialog.dismiss();
                Toast.makeText(getActivity(), "خطا در اتصال به شبکه، لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
