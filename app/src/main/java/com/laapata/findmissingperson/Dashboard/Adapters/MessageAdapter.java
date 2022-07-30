package com.laapata.findmissingperson.Dashboard.Adapters;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.laapata.findmissingperson.ModelClasses.Chat;
import com.laapata.findmissingperson.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurlReciever;
    private String imageurlSender;

    private FirebaseAuth auth;

    FirebaseUser fuser;


    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurlReciever, String imageurlSender) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurlReciever = imageurlReciever;
        this.imageurlSender = imageurlSender;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);
        auth = FirebaseAuth.getInstance();

        String currentUser = auth.getCurrentUser().getUid();

//        if (userType.equals("doctor")){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                holder.show_message.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_right_doc));
//            }
//        }
//        else if (userType.equals("patient")){
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                holder.show_message.setBackground(ContextCompat.getDrawable(mContext, R.drawable.background_right_pat));
//            }
//        }

        if (chat.getType().equals("text")){
            holder.show_message.setText(chat.getMessage());
            holder.time_stamp.setText(chat.getTime_stamp());
            holder.time_stamp.setVisibility(View.VISIBLE);
            holder.show_message.setVisibility(View.VISIBLE);
            holder.msgImg.setVisibility(View.GONE);
//            holder.msgImg_left.setVisibility(View.GONE);
            holder.time_stamp_with_img.setVisibility(View.GONE);
        }

        else {
            try {
                Glide.with(mContext).load(chat.getImg_message()).into(holder.msgImg);
            } catch (Exception e) { e.printStackTrace();}
            holder.time_stamp_with_img.setText(chat.getTime_stamp());
            holder.time_stamp_with_img.setVisibility(View.VISIBLE);
            holder.msgImg.setVisibility(View.VISIBLE);
            holder.show_message.setVisibility(View.GONE);
            holder.time_stamp.setVisibility(View.GONE);
            /*........*/
            holder.msgImg.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
//                    //        ------- Dialog -----
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_layout_img_message);
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.background_right_doc));

                    ImageView img = dialog.findViewById(R.id.imgview_dialog_img_msg);

                    try {
                        Glide.with(mContext).load(chat.getImg_message()).into(img);
                    } catch (Exception e) { e.printStackTrace();}

                    dialog.show();



                }
            });

//            if (currentUser.equals(chat.getSender())){
//                Glide.with(mContext).load(chat.getImg_message()).into(holder.msgImg);
//                holder.time_stamp_with_img.setText(chat.getTime_stamp());
//                holder.time_stamp_with_img.setVisibility(View.VISIBLE);
//                holder.msgImg.setVisibility(View.VISIBLE);
//                holder.show_message.setVisibility(View.GONE);
//                holder.time_stamp.setVisibility(View.GONE);
//            }
//            else if (currentUser.equals(chat.getReceiver())){
////                Glide.with(mContext).load(chat.getImg_message()).into(holder.msgImg_left);
//                holder.time_stamp_with_img.setText(chat.getTime_stamp());
//                holder.time_stamp_with_img.setVisibility(View.VISIBLE);
////                holder.msgImg_left.setVisibility(View.VISIBLE);
//                holder.show_message.setVisibility(View.GONE);
//                holder.time_stamp.setVisibility(View.GONE);
//            }


        }


        if (currentUser.equals(chat.getReceiver())){
            Glide.with(mContext).load(imageurlReciever).placeholder(R.drawable.sample).into(holder.profile_image);
        }
        else if (currentUser.equals(chat.getSender())){
            Glide.with(mContext).load(imageurlSender).placeholder(R.drawable.sample).into(holder.profileImg_right);
        }



        if (position == mChat.size() - 1) {
            if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
                holder.txt_seen.setVisibility(View.VISIBLE);
            } else {
                holder.txt_seen.setText("Delivered");
                holder.txt_seen.setVisibility(View.VISIBLE);
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message, time_stamp, time_stamp_with_img;
        public ImageView profile_image, msgImg, profileImg_right;
//        private ImageView  msgImg_left ;
        public TextView txt_seen, txt_seen_with_img;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            profileImg_right = itemView.findViewById(R.id.profile_image_right);

            msgImg = itemView.findViewById(R.id.img_msg_right);
//            msgImg_left = itemView.findViewById(R.id.img_msg_left);

            txt_seen = itemView.findViewById(R.id.txt_seen);
            time_stamp = itemView.findViewById(R.id.time_stamp_msg_row);
            txt_seen_with_img = itemView.findViewById(R.id.txt_seen_with_img);
            time_stamp_with_img = itemView.findViewById(R.id.time_stamp_msg_row_with_img);
        }
    }


    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }
}
