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
        View view = mInflater.inflate(R.layout.event_card_view, null);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<EventWithUsers> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, location, language;
        ShapeableImageView p1, p2, p3, p4;  // de momento no
        Button join;

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
        }

            void bindData(final EventWithUsers item) {
            date.setText(item.event.day);
            time.setText(item.event.time);
            location.setText(item.event.location);
            language.setText(item.event.language);
            p1.setColorFilter(Color.parseColor(item.users.get(0).color));

            
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(context, EventInfoActivity.class);
                    context.startActivity(intent);

                }
            });
        }
    }

}
