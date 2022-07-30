package com.laapata.findmissingperson.Dashboard.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.laapata.findmissingperson.Dashboard.Activities.Fp_Report_Details;
import com.laapata.findmissingperson.ModelClasses.FoundPersonModel;
import com.laapata.findmissingperson.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter_Fp_Reports extends RecyclerView.Adapter<RecyclerAdapter_Fp_Reports.ViewHolder> {

    private Context mContext ;
    private List<FoundPersonModel> mUsers ;
    private FirebaseAuth auth ;
    public RecyclerAdapter_Fp_Reports(Context mContext, List<FoundPersonModel> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public RecyclerAdapter_Fp_Reports.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate( R.layout.recycler_row_gigs, parent, false);
        auth = FirebaseAuth.getInstance();
        return new RecyclerAdapter_Fp_Reports.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter_Fp_Reports.ViewHolder holder, int position) {
        final FoundPersonModel msperson = mUsers.get(position);
        auth = FirebaseAuth.getInstance();

        holder.tvname.setText(msperson.getMpname());
        holder.tvmissingplace.setText("Missing place:"+msperson.getMpplace());
        holder.tvage.setText("Age:"+msperson.getMpage());
        holder.casecreateddate.setText(msperson.getCreated_date());
        Glide.with(mContext).load(msperson.getMpimage()).placeholder(R.drawable.sample).into(holder.image);

        holder.veiwfull.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caseid=msperson.getCaseid();
                Intent intent=new Intent( mContext, Fp_Report_Details.class );
                intent.putExtra( "caseid",caseid);
                mContext.startActivity(intent);
            }
        } );

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvmissingplace,tvage,veiwfull,casecreateddate;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            casecreateddate=itemView.findViewById( R.id.casecreateddate );
            tvname  = itemView.findViewById(R.id.tvname);
            veiwfull = itemView.findViewById(R.id.veiwfull);
            tvmissingplace = itemView.findViewById(R.id.tvmissingplace);
            tvage  = itemView.findViewById(R.id.tvage);
            image  = itemView.findViewById(R.id.image);

        }
    }
}
