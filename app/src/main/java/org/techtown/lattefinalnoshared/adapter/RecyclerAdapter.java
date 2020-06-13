package org.techtown.lattefinalnoshared.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.lattefinalnoshared.R;
import org.techtown.lattefinalnoshared.VO.RoomListData;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private Fragment fragment;

    public RecyclerAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    private ArrayList<RoomListData> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(RoomListData data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDate;
        private TextView textViewContent;
        private TextView textViewTitle;
        private ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void onBind(RoomListData data) {
            textViewDate.setText(data.getStartDate());
            textViewContent.setText(data.getEndDate());
            textViewTitle.setText(data.getRoomName());
            Glide.with(fragment).load(data.getImgUrl()).into(imageView);
        }
    }
}
