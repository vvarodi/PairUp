package com.example.pairup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pairup.db.EventEntity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;


/**
 * Relates CardView
 * Source: https://www.youtube.com/watch?v=HrZgfoBeams
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventEntity> mData;
    private LayoutInflater mInflater;
    private Context context;

    public EventAdapter(List<EventEntity> itemList, Context context){
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

    public void setItems(List<EventEntity> items){ mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, location, language;
        ShapeableImageView p1, p2, p3, p4;  // de momento no

        ViewHolder(View itemView){
            super(itemView);
            date = itemView.findViewById(R.id.Date);
            time = itemView.findViewById(R.id.Time);
            location = itemView.findViewById(R.id.Location);
            language = itemView.findViewById(R.id.Language);
        }

        void bindData(final EventEntity item) {
            date.setText(item.day);
            time.setText(item.time);
            location.setText(item.location);
            language.setText(item.language);
        }
    }

}
