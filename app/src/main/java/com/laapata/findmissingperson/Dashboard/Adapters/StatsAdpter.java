package com.laapata.findmissingperson.Dashboard.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laapata.findmissingperson.Dashboard.Activities.Edit_fp_cases;
import com.laapata.findmissingperson.Dashboard.Activities.StatsDetailActivity;
import com.laapata.findmissingperson.ModelClasses.StatsModel;
import com.laapata.findmissingperson.ModelClasses.StatsModel;
import com.laapata.findmissingperson.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class StatsAdpter extends RecyclerView.Adapter<StatsAdpter.ViewHolder> {

    private Activity mContext ;
    private List<StatsModel> mUsers ;
    private FirebaseAuth auth ;
    public StatsAdpter(Activity mContext, List<StatsModel> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public StatsAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate( R.layout.statslistrecyler, parent, false);
        return new StatsAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StatsAdpter.ViewHolder holder, int position) {
        final StatsModel stats = mUsers.get(position);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        final String userid = firebaseUser.getUid();
        holder.tvyear.setText(stats.getYear());

        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, StatsDetailActivity.class );
                intent.putExtra( "description",stats.getDescription().trim());
                intent.putExtra( "tvyear",stats.getYear().trim());
                mContext.startActivity( intent );
            //    Toast.makeText( mContext,"clicked",Toast.LENGTH_SHORT ).show();
            }
        } );

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvyear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvyear  = itemView.findViewById(R.id.tvyear);

        }
    }
}
