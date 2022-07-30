package com.laapata.findmissingperson.Authentication;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.laapata.findmissingperson.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
public class PassRecFragment extends Fragment {
    private static FragmentManager fragmentManager ;
    private EditText emailEt ;
    private Button button ;
    private FirebaseAuth auth ;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_pass_rec_fragment, null) ;
        fragmentManager = getActivity().getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_reset_password);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                        .replace(R.id.authentication_frame_container, new LoginFragment(),
                                Utils.LoginFragment).commit();
            }});


        String userType = getActivity().getIntent().getStringExtra("userType");



        emailEt  = (EditText) rootView.findViewById(R.id.email_et_reset_password);
        button   = (Button) rootView.findViewById(R.id.send_btn_reset_password);
        auth     = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEt.getText().toString().trim();

                if (email.equals("")){
                    Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Recovery email has sent", Toast.LENGTH_SHORT).show();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return rootView;
    }
}
