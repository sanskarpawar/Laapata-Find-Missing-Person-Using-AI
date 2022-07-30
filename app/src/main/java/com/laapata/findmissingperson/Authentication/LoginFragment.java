package com.laapata.findmissingperson.Authentication;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.laapata.findmissingperson.Dashboard.Dashboard;
import com.laapata.findmissingperson.Dashboard.MainActivity;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
public class LoginFragment extends Fragment {
    private static FragmentManager fragmentManager ;

    private EditText usernameEt, passEt ;
    private Button submitBtn ;
    private TextView regsterBtn, forgetPass ;
    private ProgressDialog dialog ;
    private String email ;
    private FirebaseAuth auth ;
    private DatabaseReference reference ;
    private Context context ;

//    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_login_fragment, null);
        fragmentManager = requireActivity().getSupportFragmentManager();
//        StatusBarUtil.setTransparent(getActivity());
        context = getActivity() ;
        usernameEt = (EditText) rootView.findViewById(R.id.email_et_login);
        passEt = (EditText) rootView.findViewById(R.id.password_et_login);
        submitBtn = (Button) rootView.findViewById(R.id.submitBtn_id_login);
        regsterBtn = (TextView) rootView.findViewById(R.id.register_intent_btn_login);
        forgetPass = (TextView) rootView.findViewById(R.id.forget_btn_login);
        auth = FirebaseAuth.getInstance();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*.................................................*/

                email = usernameEt.getText().toString();
                String password = passEt.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    usernameEt.setError("Email?");
                } else if (TextUtils.isEmpty(password)) {
                    passEt.setError("Enter Password");
                }
                else {
                    signinMethod(email, password);
                }

            }});

        regsterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.authentication_frame_container, new RegisterFragment(),
                                Utils.RegisterFragment).commit();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.authentication_frame_container, new PassRecFragment(),
                                Utils.PassRecFragment).commit();
            }
        });
        return rootView;
    }

    /*......Signin Method......*/
    public void signinMethod(String email, String password) {
            // data is found
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            ((ProgressDialog) dialog).setMessage("Please Wait...");
            dialog.show();
            auth.signInWithEmailAndPassword(email , password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user.isEmailVerified()){
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    dialog.dismiss();

                                }
                                else {
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Email is not Verified ", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                dialog.dismiss();

                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }

}
