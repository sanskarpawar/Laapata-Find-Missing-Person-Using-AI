package com.laapata.findmissingperson.Authentication;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
public class RegisterFragment extends Fragment {
    private static FragmentManager fragmentManager ;
    private EditText et1_name, et2_email, et3_pass ;
    Button registerBtn ;
    TextView loginIntntBtn,register_police_intent_btn;

    String userType ;
    private String typeOfUser ;
    String username, email, password ;

    private FirebaseAuth auth ;
    FirebaseDatabase database;

//    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_register_fragment, null);
        fragmentManager = requireActivity().getSupportFragmentManager();

        et1_name  =  rootView.findViewById(R.id.et1_name_register_activity);
        et2_email =  rootView.findViewById(R.id.et2_email_register_activity);
        et3_pass  =  rootView.findViewById(R.id.et3_password_register_activity);

        registerBtn =  rootView.findViewById(R.id.register_btn_regstr_fragment);
        loginIntntBtn = rootView.findViewById(R.id.login_intent_btn_register);
        register_police_intent_btn = rootView.findViewById(R.id.register_police_intent_btn);


        auth = FirebaseAuth.getInstance();

//        database = FirebaseDatabase.getInstance("https://find-missing-person-71872-default-rtdb.firebaseio.com");
        database = FirebaseDatabase.getInstance();

        userType = requireActivity().getIntent().getStringExtra("userType");


        registerBtn.setOnClickListener(view -> RegisterMethod());

        loginIntntBtn.setOnClickListener(v -> fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.authentication_frame_container, new LoginFragment(),
                        Utils.LoginFragment).commit());

        register_police_intent_btn.setOnClickListener(v -> fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(R.id.authentication_frame_container, new RegisterPoliceFragment(),
                        Utils.LoginFragment).commit());


        return rootView;

    }
    public void RegisterMethod() {

        username  = et1_name.getText().toString();
        email     = et2_email.getText().toString().trim();
        password  = et3_pass.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            et1_name.setError("Enter Full Name");
        } else if (TextUtils.isEmpty(email)) {
            et2_email.setError("Enter Email Address");
        } else if (TextUtils.isEmpty(password)) {
            et3_pass.setError("Enter Phone Number");
        }
        else {
            // data is found
            final ProgressDialog getDialog = new ProgressDialog(getContext());
            getDialog.setMessage("Loading Please wait...");
            getDialog.show();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            sendEmailVerification();
                            getDialog.dismiss();
                        } else {
                            // If sign up fails, display a message to the user.
                            getDialog.dismiss();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    });
        }

    }
    //    ==============================================================================================
    private void sendEmailVerification() {
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            createDatabase(username, email);
                            Toast.makeText(getContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }
    //////////////////////////////////////////////////////////////////////////
//    ---------- Create database----------
    public void createDatabase(String username, String email) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            String userid = firebaseUser.getUid();
            long currentTime = System.currentTimeMillis(); //getting current time in millis
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(currentTime);
            String showTime = String.format("%1$tI:%1$tM:%1$tS %1$Tp" + "\n", cal);
            Date now = new Date();
            long timestamp = now.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            String dateStr = sdf.format(timestamp);
            String dateStamp = showTime + dateStr;
            DatabaseReference ref1 = database.getReference("AllUsersAccount").child(userid).child("Profile_Info");
            final DatabaseReference ref2 = database.getReference("Users_List").child(userid);
            final UsersData data = new UsersData(
                    userid,
                    dateStamp,
                    username,
                    email,
                    "",
                    "",
                    "",
                    "",
                    username.toLowerCase(),
                    "user",
                    "", 0, 0
            );
            ref1.setValue(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ref2.setValue(data);
                } else {
                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



}
