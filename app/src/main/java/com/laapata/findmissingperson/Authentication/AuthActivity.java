package com.laapata.findmissingperson.Authentication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.laapata.findmissingperson.R;
public class AuthActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        fragmentManager = getSupportFragmentManager();
        context = this ;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
                window.setStatusBarColor( ContextCompat.getColor( getApplicationContext(), R.color.colorPrimary ) );
            }


        fragmentManager.beginTransaction().replace(R.id.authentication_frame_container,
                new LoginFragment(), Utils.LoginFragment).commit();

    }


    public void replaceLoginFragment() {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.authentication_frame_container, new LoginFragment(),
                        Utils.LoginFragment).commit();
    }
    @Override
    public void onBackPressed() {

        Fragment SignUp_Fragment = fragmentManager.findFragmentByTag(Utils.RegisterFragment);
        Fragment ForgotPassword_Fragment = fragmentManager.findFragmentByTag(Utils.PassRecFragment);
        Fragment registerPolice = fragmentManager.findFragmentByTag(Utils.RegisterPoliceFragment);

        if (SignUp_Fragment != null)
            replaceLoginFragment();
        else if (ForgotPassword_Fragment != null)
            replaceLoginFragment();
        else if (registerPolice != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }

}
