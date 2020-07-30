package org.techtown.lattefinalnoshared.adapter;

import android.util.Log;
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
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private Fragment fragment;

    public RecyclerAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    private ArrayList<RoomListData> listData = new ArrayList<>();
    private HashMap<String, RoomListData> checkMap = new HashMap<>();

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
        if (!checkMap.containsKey(data.getRoomName())) {
            checkMap.put(data.getRoomName(), data);
            listData.add(data);
        }
//        Log.i("addItem", data.toString());
//        Log.i("addItem", "" + listData.size());
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
            if (!listData.isEmpty()) {
//                Log.i("onBind", "start");
                textViewDate.setText(data.getRoomName());
                textViewContent.setText(data.getStartDate());
                textViewTitle.setText(data.getEndDate());
                Glide.with(fragment).load(data.getImgUrl()).into(imageView);
//                Log.i("onBind", "end");
            }else{

            }

        }
    }
}
