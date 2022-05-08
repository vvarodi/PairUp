package com.example.pairup;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventEntity;
import com.example.pairup.db.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private AppDatabase db;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.floatingHost).setOnClickListener(view1 -> openHostActivity());

        db = AppDatabase.getInstance(getActivity().getApplicationContext());

        List<EventEntity> allEvents= db.eventDao().getAllEvents();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        EventAdapter adapter = new EventAdapter(allEvents, getActivity());
        recyclerView.setAdapter(adapter);



    }

    public void openHostActivity(){
        PairUpActivity activity = (PairUpActivity) getActivity();
        String email = activity.getCurrentUser();

        Intent intent = new Intent(getContext() , HostActivity.class);
        intent.putExtra("GMAIL", email);
        startActivity(intent);
    }

    // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
    public void onResume(){
        super.onResume();
        ((PairUpActivity) getActivity())
                .setActionBarTitle("Join a meeting");

        List<EventEntity> allEvents= db.eventDao().getAllEvents();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        EventAdapter adapter = new EventAdapter(allEvents, getActivity());
        recyclerView.setAdapter(adapter);


    }


}