package com.laapata.findmissingperson.Authentication;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laapata.findmissingperson.Dashboard.Activities.UpheavMapActivity;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class RegisterPoliceFragment extends Fragment {

    private static FragmentManager fragmentManager ;
    EditText et1_username,et2_email,et3_phone,et4_password;
    Button register_btn;
    TextView backBtn,tv_location;
    private FirebaseAuth auth ;

    public RegisterPoliceFragment() { }

    @Override public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register_police, container, false);

        if (getActivity()!=null){
            fragmentManager = getActivity().getSupportFragmentManager();
            auth = FirebaseAuth.getInstance();
            initViews(rootView);
        }

        return rootView;
    }

    private void initViews(View rootView){
        et1_username = rootView.findViewById(R.id.et1_username);
        et2_email = rootView.findViewById(R.id.et2_email);
        et3_phone = rootView.findViewById(R.id.et3_phone);
        et4_password = rootView.findViewById(R.id.et4_password);
        tv_location = rootView.findViewById(R.id.tv_location);
        register_btn = rootView.findViewById(R.id.register_btn);
        backBtn = rootView.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.authentication_frame_container, new LoginFragment(),
                        Utils.LoginFragment).commit());

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(requireActivity(), UpheavMapActivity.class );
                intent.putExtra( "fromPolice",true);
                startActivityForResult(intent,1155);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et1_username.getText().toString().trim();
                String email = et2_email.getText().toString().trim();
                String phone = et3_phone.getText().toString().trim();
                String password = et4_password.getText().toString().trim();

                if (TextUtils.isEmpty(username)){
                    et1_username.setError("Field required");
                }
                else if (TextUtils.isEmpty(email)){
                    et2_email.setError("Field required");
                }
                else if (TextUtils.isEmpty(phone)){
                    et3_phone.setError("Field required");
                }
                else if (TextUtils.isEmpty(password)){
                    et4_password.setError("Field required");
                }
                else if (TextUtils.isEmpty(tv_location.getText())){
                    showToast("Please enter address or location");
                }
                else{
                    registerMethod(username,email,phone,final_address,password);
                }
            }
        });
    }

    double final_latitude=0;
    double final_longitude=0;
    String final_address;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1155) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!=null) {
                    final_longitude = data.getDoubleExtra("longitude", 0);
                    final_latitude = data.getDoubleExtra("latitude", 0);
                    final_address = data.getStringExtra("address");
                    tv_location.setText(""+final_address);
                }
            }else{
                showToast("Address not Selected");
            }
        }

    }

    public void showToast(String msg){
        Toast toast =  Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT);
        new Handler().postDelayed(() -> {
            if (toast!=null)
                toast.cancel();

        },1500);
        if (toast!=null)
            toast.show();
    }

    public void registerMethod(String username,String email,String phone,String address,String password) {
        // data is found
        final ProgressDialog getDialog = new ProgressDialog(requireContext());
        getDialog.setMessage("Loading Please wait...");
        getDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            sendEmailVerification(username,email,phone,address);
                            getDialog.dismiss();
                        } else {
                            // If sign up fails, display a message to the user.
                            getDialog.dismiss();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if (task.getException()!=null) showToast(task.getException().getMessage());
                            else showToast("May Some network issue.");
                        }

                        // ...
                    }
                });
    }
    //    ==============================================================================================
    private void sendEmailVerification(String username,String email,String phone,String address) {
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        createDatabase(username, email,phone,address);
                        showToast("Verification email sent to " + user.getEmail());
                    }
                    else {
                        Log.e(TAG, "sendEmailVerification", task.getException());
                        if (task.getException()!=null) showToast(task.getException().getMessage());
                        else showToast("May Some network issue.");
                    }
                }
            });
        }
    }
    //////////////////////////////////////////////////////////////////////////
//    ---------- Create database----------
    public void createDatabase(String username, String email, String phone, String address) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        Long currentTime = System.currentTimeMillis(); //getting current time in millis
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        String showTime = String.format("%1$tI:%1$tM:%1$tS %1$Tp" + "\n", cal);
        Date now = new Date();
        long timestamp = now.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateStr = sdf.format(timestamp);
        String dateStamp = showTime + dateStr;
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(userid).child("Profile_Info");
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("PoliceStations").child(userid);
        final UsersData data = new UsersData(
                userid,
                dateStamp,
                username,
                email,
                ""+phone,
                "",
                ""+address,
                "",
                username.toLowerCase(),
                "police",
                "",
                final_latitude,
                final_longitude
        );
        ref1.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ref2.setValue(data) ;
                } else {
                    if (task.getException()!=null) showToast(task.getException().getMessage());
                    else showToast("May Some network issue.");
                }
            }
        });

    }
}