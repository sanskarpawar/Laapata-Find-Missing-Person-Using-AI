package com.laapata.findmissingperson.Dashboard.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.laapata.findmissingperson.Dashboard.Activities.MessageActivity;
import com.laapata.findmissingperson.ModelClasses.Chat;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<UsersData> mUsers;
    private boolean ischat;

    String theLastMessage;

    public UserAdapter(Context mContext, List<UsersData> mUsers, boolean ischat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final UsersData user = mUsers.get(position);
        holder.username.setText(user.getFull_name());

            Glide.with(mContext).load(user.getImgUrl()).placeholder(R.drawable.sample).into(holder.profile_image);

            if (user.getStatus().equals("typing")){
                holder.last_msg.setText("Typing...");
            }
            else {

        if (ischat){
            lastMessage(user.getIdPush(), holder.last_msg, holder);
        } else {
//            lastMessage(user.getIdPush(), holder.last_msg);
            holder.last_msg.setVisibility(View.GONE);
        }
            }


        if (ischat){

            if (user.getStatus().equals("online") || user.getStatus().equals("typing")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(mContext, MessageActivity.class);
                    intent.putExtra("cruserid", user.getIdPush());
                    intent.putExtra("userType", "patient");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);

                /////////////////////////////////////////

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
                try {
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int unread = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Chat chat = snapshot.getValue(Chat.class);
                                if (chat.getReceiver().equals(chat.getSender()) && !chat.isIsseen()){
                                    unread++;
                                }
                            }

                            if (unread == 0){
                                holder.indicator.setVisibility(View.GONE);
//                        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                            } else {
                                holder.indicator.setVisibility(View.GONE);
                                holder.indicator.setText(String.valueOf(unread));
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }});
                } catch (Exception e) { e.printStackTrace(); }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        private TextView indicator;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            indicator = itemView.findViewById(R.id.new_msg_indicator_tv_user_item);
        }
    }

    //check for last message
    private void lastMessage(final String userid, final TextView last_msg, final ViewHolder holder){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if (firebaseUser != null && chat != null) {
                            if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                    chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                                theLastMessage = chat.getMessage();
//                                if (!chat.isIsseen()){
//                                    holder.indicator.setVisibility(View.VISIBLE);
////
//                                } else {
//                                    holder.indicator.setVisibility(View.GONE);
//                                }

                            }
                        }
                    }

                    if (theLastMessage!=null && theLastMessage.equals("")){
                        last_msg.setText("Sent an Image");
                    }else {
                        last_msg.setText(theLastMessage);
                    }

//                    switch (theLastMessage){
//                        case  "default":
//                            last_msg.setText("No Message");
//                            break;
//
//                        default:
//                            last_msg.setText(theLastMessage);
//                            break;
//                    }
//
//                    theLastMessage = "default";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }
    }
}
