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
import com.laapata.findmissingperson.Dashboard.Activities.Edit_mp_cases;
import com.laapata.findmissingperson.ModelClasses.GigsData;
import com.laapata.findmissingperson.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Your_mp_cases_adapter extends RecyclerView.Adapter<Your_mp_cases_adapter.ViewHolder> {

    private Activity mContext ;
    private List<GigsData> mUsers ;
    private FirebaseAuth auth ;
    public Your_mp_cases_adapter(Activity mContext, List<GigsData> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public Your_mp_cases_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate( R.layout.recycler_row_gigs, parent, false);
        auth = FirebaseAuth.getInstance();
        return new Your_mp_cases_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Your_mp_cases_adapter.ViewHolder holder, int position) {
        final GigsData msperson = mUsers.get(position);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        final String userid = firebaseUser.getUid();
        holder.tvname.setText(msperson.getMpname());
        holder.tvmissingplace.setText("Missing place:"+msperson.getMpplace());
        holder.casecreateddate.setText(msperson.getCreated_date());
        holder.tvage.setText("Age:"+msperson.getMpage());
        Glide.with(mContext).load(msperson.getMpimage()).placeholder(R.drawable.sample).into(holder.image);

        holder.veiwfull.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String caseid=msperson.getCaseid();
                final ProgressDialog dialogProgress = new ProgressDialog(mContext);
                dialogProgress.setMessage("Please Wait...");
                dialogProgress.setCancelable(false);
                dialogProgress.show();
                final Dialog dialogMain = new Dialog(mContext);
                dialogMain.setContentView(R.layout.dialog_layout_modify_file);
                dialogMain.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));

                final TextView editBtn  = dialogMain.findViewById(R.id.editBtn_dialog_modify_file);
                final TextView delBtn   = dialogMain.findViewById(R.id.delBtn_dialog_modify_file);

                  delBtn.setOnClickListener( new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(userid).child("MissingPersonPosts").child(caseid);
                          final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("MissingUsers").child(caseid);
                          ref1.removeValue().addOnCompleteListener( new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()){
                                      dialogProgress.dismiss();
                                      dialogMain.dismiss();
                                      ref2.removeValue();
                                      Toast.makeText(mContext, "File Removed", Toast.LENGTH_SHORT).show();
                                  }else {
                                      dialogProgress.dismiss();
                                      Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                  }
                              }
                          } );

                      }
                  } );
                  editBtn.setOnClickListener( new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                            Intent intent=new Intent( mContext, Edit_mp_cases.class );
                          intent.putExtra( "caseid",caseid);
                          mContext.startActivity(intent);
                       dialogProgress.dismiss();
                          dialogMain.dismiss();
                      }
                  } );
                dialogMain.show();

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
