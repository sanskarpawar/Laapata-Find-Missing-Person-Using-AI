package com.laapata.findmissingperson.Dashboard;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laapata.findmissingperson.ModelClasses.FoundPersonModel;
import com.laapata.findmissingperson.ModelClasses.GigsData;
import com.laapata.findmissingperson.R;

import java.util.List;
public class AdapterSlider extends RecyclerView.Adapter<AdapterSlider.ViewHolder> {

    private final Activity context;
    private final List<FoundPersonModel> gigsDataList;
    private final OnItemClicked onItemClicked;

    public AdapterSlider(Activity context, List<FoundPersonModel> gigsDataList, OnItemClicked onItemClicked) {
        this.context = context;
        this.gigsDataList = gigsDataList;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_card_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoundPersonModel gigsData = gigsDataList.get(position);
        if (gigsData!=null){
            Glide.with(context).load(gigsData.getMpimage()).into(holder.imageView);
            holder.selectBtn.setOnClickListener(v -> {
                onItemClicked.onItemClicked(gigsData);
            });
        }

    }
    @Override
    public int getItemViewType(int position) { return position; }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return gigsDataList.size();
    }


    @SuppressWarnings("InnerClassMayBeStatic")
    public class ViewHolder extends RecyclerView.ViewHolder{
         ImageView imageView;
         Button selectBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_rcs);
            selectBtn = itemView.findViewById(R.id.selectBtn);
        }
    }
}
