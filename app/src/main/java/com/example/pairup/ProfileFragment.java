package com.example.pairup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = AppDatabase.getUserDatabase(getActivity());

        // Retrieve data passed as intent to PairUpActivity
        PairUpActivity activity = (PairUpActivity) getActivity();
        String email = activity.getCurrentUser();

        UserEntity user = db.userDao().getCurrentUser(email);


        TextView txtView = (TextView) view.findViewById(R.id.text);
        txtView.setText(user.getName());





        return view;
    }

    // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
    public void onResume(){
        super.onResume();
        ((PairUpActivity) getActivity())
                .setActionBarTitle("Profile");

    }
}