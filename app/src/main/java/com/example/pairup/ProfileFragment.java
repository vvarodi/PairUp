package com.example.pairup;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import 	android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.UserEntity;

public class ProfileFragment extends Fragment {

    private AppDatabase db;

    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = AppDatabase.getInstance(getActivity());

        // Retrieve data passed as intent to PairUpActivity
        PairUpActivity activity = (PairUpActivity) getActivity();
        String email = activity.getCurrentUser();

        UserEntity user = db.userDao().getCurrentUser(email);

        imageView = (ImageView) view.findViewById(R.id.profile_avatar);

        TextView txtView = (TextView) view.findViewById(R.id.profile_username);
        txtView.setText(user.getName());

        TextView txtView2 = (TextView) view.findViewById(R.id.profile_biography);
        txtView2.setText(user.getBiography());

        TextView txtView3 = (TextView) view.findViewById(R.id.profile_languages);
        txtView3.setText(user.getLanguages().toString());

        imageView.setColorFilter(Color.parseColor(user.getColor()));

        return view;
    }

    // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
    public void onResume(){
        super.onResume();
        ((PairUpActivity) getActivity())
                .setActionBarTitle("Profile");
    }
}