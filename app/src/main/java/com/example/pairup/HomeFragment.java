package com.example.pairup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventEntity;
import com.example.pairup.db.EventWithUsers;
import com.example.pairup.db.Reservation;
import com.example.pairup.db.UserEntity;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private AppDatabase db;
    RecyclerView recyclerView;
    String email;
    private String selectedLanguages;
    private List<EventWithUsers> filtered;
    LinearLayout empty_txt;
    Button filter;
    List<EventWithUsers> all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.floatingHost).setOnClickListener(view1 -> openHostActivity());
        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(view1 -> languagePicker());

        db = AppDatabase.getInstance(getActivity().getApplicationContext());

        all = db.reservationDao().getEventsWithUsers();

        recyclerView = view.findViewById(R.id.recyclerview);
        empty_txt = view.findViewById(R.id.empty);

        // User have not joined events yet
        if (all.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            empty_txt.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty_txt.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            EventAdapter adapter = new EventAdapter(all, getActivity());
            recyclerView.setAdapter(adapter);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        EventAdapter adapter = new EventAdapter(all, getActivity());
        recyclerView.setAdapter(adapter);


        // Retrieve data passed as intent to PairUpActivity
        PairUpActivity activity = (PairUpActivity) getActivity();
        email = activity.getCurrentUser();

    }


    public void openHostActivity(){
        PairUpActivity activity = (PairUpActivity) getActivity();
        String email = activity.getCurrentUser();

        Intent intent = new Intent(getContext() , HostActivity.class);
        intent.putExtra("GMAIL", email);
        startActivity(intent);
    }

    private void languagePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by:");

        // MultiChoice OPTIONS
        String[] languages_list = {"Italian", "French", "English", "Spanish", "Portuguese", "Japanese", "Mandarin", "Russian", "Arabic", "Dutch"};
        int checkedItem = -1;
        builder.setSingleChoiceItems(languages_list, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedLanguages = languages_list[i];
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                filtered = db.reservationDao().getEventsWithUsersByLanguage("%"+selectedLanguages+"%");

                //Toast.makeText(getContext(), filtered.get(0).event.language, Toast.LENGTH_LONG);
                filter.setText(getString(R.string.filter_by)+ ": "+selectedLanguages);
                if (filtered.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    empty_txt.setVisibility(View.VISIBLE);

                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    empty_txt.setVisibility(View.GONE);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    EventAdapter adapter = new EventAdapter(filtered, getActivity());
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        //builder.setPositiveButton(R.string.save, (dialogInterface, id) -> filtered = db.reservationDao().getEventsWithUsersByLanguage(selectedLanguages));
        builder.setNegativeButton(R.string.cancel, (dialogInterface, id) -> dialogInterface.dismiss());
        // Create and show the Alert Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
    public void onResume(){
        super.onResume();
        ((PairUpActivity) getActivity())
                .setActionBarTitle("Join a meeting");

        recyclerView.setVisibility(View.VISIBLE);
        empty_txt.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        EventAdapter adapter = new EventAdapter(all, getActivity());
        recyclerView.setAdapter(adapter);


    }


}