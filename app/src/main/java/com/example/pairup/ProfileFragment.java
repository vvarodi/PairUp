package com.example.pairup;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import 	android.graphics.Color;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventEntity;
import com.example.pairup.db.EventWithUsers;
import com.example.pairup.db.UserEntity;
import com.example.pairup.db.UserWithEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for Profile View
 */
public class ProfileFragment extends Fragment {

    private AppDatabase db;
    private UserEntity user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //db = AppDatabase.getInstance(getActivity());
        db = AppDatabase.getInstance(getActivity().getApplicationContext());

        // Retrieve current user information from shared preferences
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", getContext().MODE_PRIVATE);
        int id = prefs.getInt("ID", 0);
        user = db.userDao().getCurrentUserById((long)id);

        // Change Avatar
        ImageView imageView = (ImageView) view.findViewById(R.id.profile_avatar);
        imageView.setColorFilter(Color.parseColor(user.getColor()));

        // Change username
        TextView txtView = (TextView) view.findViewById(R.id.profile_username);
        txtView.setText(user.getName());

        // Change biography
        TextView txtView2 = (TextView) view.findViewById(R.id.profile_biography);
        txtView2.setText(user.getBiography());

        // Change Languages list
        TextView txtView3 = (TextView) view.findViewById(R.id.profile_languages);
        txtView3.setText(user.getLanguages());

        // Show a list of card views containing the events the user has joined over time
        showJoinedEvents(view);

        return view;
    }

    /**
     * Set Action Bar Title from fragment
     * // https://stackoverflow.com/questions/28389841/change-actionbar-title-using-fragments
     */
    public void onResume(){
        super.onResume();
        ((PairUpActivity) getActivity())
                .setActionBarTitle("Profile");
    }


    /**
     * List Joined Events by user current user
     */
    private void showJoinedEvents(View view){
        List<UserWithEvent> userEvents= db.reservationDao().getUsersWithEvents(user.getId_user());
        List<EventEntity> events = userEvents.get(0).events;
        List<EventWithUsers> my = new ArrayList<EventWithUsers>();

        List<EventWithUsers> all = db.reservationDao().getEventsWithUsers();
        Log.d("Assert", "my_event---"+all.get(0).event.day);
        for (int i=0; i < all.size(); i++){
            for (int j = 0; j < all.get(i).users.size(); j++){
                if (all.get(i).users.get(j).getId_user() == user.getId_user()){
                    my.add(all.get(i));
                    Log.d("Assert", "my_event"+my.get(i).event.day);
                }
            }
        }


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_profile);
        LinearLayout empty_txt = view.findViewById(R.id.empty);

        if (my.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            empty_txt.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty_txt.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            EventAdapter adapter = new EventAdapter(my, getActivity());
            recyclerView.setAdapter(adapter);
        }
    }


}