package com.example.pairup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventWithUsers;
import com.example.pairup.db.UserEntity;

import java.util.ArrayList;
import java.util.List;


public class UserActivity extends AppCompatActivity {

    private AppDatabase db;
    private UserEntity user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        db = AppDatabase.getInstance(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int id = prefs.getInt("ID_VIEW", 0);
        user = db.userDao().getCurrentUserById((long)id);

        // Change Avatar
        ImageView imageView = (ImageView) findViewById(R.id.profile_avatar);
        imageView.setColorFilter(Color.parseColor(user.getColor()));

        // Change username
        TextView txtView = (TextView) findViewById(R.id.profile_username);
        txtView.setText(user.getName());

        // Change biography
        TextView txtView2 = (TextView) findViewById(R.id.profile_biography);
        txtView2.setText(user.getBiography());

        // Change Languages list
        TextView txtView3 = (TextView) findViewById(R.id.profile_languages);
        txtView3.setText(user.getLanguages());

        // Show a list of card views containing the events the user has joined over time
        showJoinedEvents();
    }

    /**
     * List Joined Events by user current user
     */
    private void showJoinedEvents(){
        //
        List<EventWithUsers> show = new ArrayList<>();
        List<EventWithUsers> all = db.reservationDao().getEventsWithUsers();

        for (int i=0; i < all.size(); i++){
            for (int j = 0; j < all.get(i).users.size(); j++){
                if (all.get(i).users.get(j).getId_user() == user.getId_user()){
                    show.add(all.get(i));
                }
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview_profile);
        LinearLayout empty_txt = findViewById(R.id.empty);

        // User have not joined events yet
        if (show.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            empty_txt.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty_txt.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            EventAdapter adapter = new EventAdapter(show, this);
            recyclerView.setAdapter(adapter);
        }
    }


}
