package com.example.pairup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pairup.db.AppDatabase;
import com.example.pairup.db.EventEntity;
import com.example.pairup.db.EventWithUsers;
import com.example.pairup.db.Reservation;
import com.example.pairup.db.UserEntity;
import com.example.pairup.db.UserWithEvent;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;


/**
 * Relates CardView
 * Source: https://www.youtube.com/watch?v=HrZgfoBeams
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventWithUsers> mData;
    private LayoutInflater mInflater;
    private Context context;

    public EventAdapter(List<EventWithUsers> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // How is going to be seen each item
        View view = mInflater.inflate(R.layout.event_card_view, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<EventWithUsers> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, location, language, p1t, p2t, p3t, p4t;
        ShapeableImageView p1, p2, p3, p4;
        Button join;
        LinearLayout h1, h2;

        ViewHolder(View itemView){
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            time = itemView.findViewById(R.id.Time);
            location = itemView.findViewById(R.id.Location);
            language = itemView.findViewById(R.id.Language);
            join = itemView.findViewById(R.id.button_join);
            p1 = itemView.findViewById(R.id.profile1);
            p2 = itemView.findViewById(R.id.profile2);
            p3 = itemView.findViewById(R.id.profile3);
            p4 = itemView.findViewById(R.id.profile4);
            h1 = itemView.findViewById(R.id.hide1);
            h2 = itemView.findViewById(R.id.hide2);
            p1t = itemView.findViewById(R.id.profile1_name);
            p2t = itemView.findViewById(R.id.profile2_name);
            p3t = itemView.findViewById(R.id.profile3_name);
            p4t = itemView.findViewById(R.id.profile4_name);
        }

        void bindData(final EventWithUsers item) {
            date.setText(item.event.day);
            time.setText(item.event.time);
            location.setText(item.event.location);
            language.setText(item.event.language);

            // to inflate in HostActivity correctly
            if (item.users.size() > 0){
                p1.setColorFilter(Color.parseColor(item.users.get(0).color));
                p1t.setText(item.users.get(0).name);
            }

            if (item.event.members == 2){
                h1.setVisibility(View.GONE);
                h2.setVisibility(View.GONE);

                if(item.event.full){
                    p2.setColorFilter(Color.parseColor(item.users.get(1).color));
                    p2t.setText(item.users.get(1).name);
                }
            }
            if (item.event.members == 4){
                if (item.event.joined == 2){
                    p2.setColorFilter(Color.parseColor(item.users.get(1).color));
                    p2t.setText(item.users.get(1).name);
                }
                if (item.event.joined == 3){
                    p2.setColorFilter(Color.parseColor(item.users.get(1).color));
                    p2t.setText(item.users.get(1).name);
                    p3.setColorFilter(Color.parseColor(item.users.get(2).color));
                    p3t.setText(item.users.get(2).name);
                }
                if (item.event.joined == 4){
                    p2.setColorFilter(Color.parseColor(item.users.get(1).color));
                    p2t.setText(item.users.get(1).name);
                    p3.setColorFilter(Color.parseColor(item.users.get(2).color));
                    p3t.setText(item.users.get(2).name);
                    p4.setColorFilter(Color.parseColor(item.users.get(3).color));
                    p4t.setText(item.users.get(3).name);
                }

            }


            /**
            if (item.event.members == 2){
                p2.setColorFilter(Color.parseColor(item.users.get(1).color));
                p2t.setText(item.users.get(1).name);
            }
            if (item.event.members == 3){
                p2.setColorFilter(Color.parseColor(item.users.get(1).color));
                p2t.setText(item.users.get(1).name);
                p3.setColorFilter(Color.parseColor(item.users.get(2).color));
                p3t.setText(item.users.get(2).name);
            }
            if (item.event.members == 4){
                p2.setColorFilter(Color.parseColor(item.users.get(1).color));
                p2t.setText(item.users.get(1).name);
                p3.setColorFilter(Color.parseColor(item.users.get(2).color));
                p3t.setText(item.users.get(2).name);
                p4.setColorFilter(Color.parseColor(item.users.get(3).color));
                p4t.setText(item.users.get(3).name);
            }**/
            
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences prefs = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
                    prefs.edit().putInt("ID_EVENT", (int)item.event.getId_event()).apply();
                    Intent intent = new Intent(context, EventInfoActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
