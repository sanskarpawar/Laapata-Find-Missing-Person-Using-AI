package com.laapata.findmissingperson.Dashboard.Adapters;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity mContext;
    private UsersData mUserModel;


    public CustomInfoWindowAdapter(Activity mContext, UsersData mUserModel) {
        this.mContext = mContext;
        this.mUserModel = mUserModel;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        UsersData usersData = (UsersData) marker.getTag();
        View view = mContext.getLayoutInflater().inflate(R.layout.layout_custom_info_window_police, null);

        ImageView userImage = view.findViewById(R.id.iv_image);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        Button phoneBtn = view.findViewById(R.id.phoneBtn);
        Button chatBtn = view.findViewById(R.id.chatBtn);

//        UserModel userModel = (UserModel) marker.getTag();
        String sd = marker.getId().toString();

        if (usersData!=null){

            try {
                tvTitle.setText(usersData.getFull_name());
                tvEmail.setText(usersData.getEmail());
                tvAddress.setText(usersData.getAddress());

                phoneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Clicked 01", Toast.LENGTH_SHORT).show();
                    }
                });
                chatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Clicked 02", Toast.LENGTH_SHORT).show();
                    }
                });


            } catch (Exception e) { e.printStackTrace(); }
        }



        return view;

    }


    @Override
    public View getInfoContents(final Marker marker) {
        return null;
    }
}
