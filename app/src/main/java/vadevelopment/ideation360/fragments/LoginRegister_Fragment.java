package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.MainActivity;
import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/13/2017.
 */

public class LoginRegister_Fragment extends Fragment implements View.OnClickListener {

    private Button login, signup;
    private TextView bottomForgot;
    private LoginRegister_Containor containor;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_first, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        login = (Button) view.findViewById(R.id.login);
        signup = (Button) view.findViewById(R.id.signup);
        bottomForgot = (TextView) view.findViewById(R.id.bottomForgot);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        bottomForgot.setOnClickListener(this);
        containor = (LoginRegister_Containor) getActivity();
        containor.firstLinear.setVisibility(View.INVISIBLE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (preferences.getString("loginwith", "").equalsIgnoreCase("indi")) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else if (preferences.getString("loginwith", "").equalsIgnoreCase("multi")) {
            MultipleAssig_Fragment sf = new MultipleAssig_Fragment();
            containor.replaceWithoutbackFragment(sf);
        }

       /* if (preferences.getBoolean("login", false)) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                LoginFragment lf = new LoginFragment();
                containor.replaceFragment(lf);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginreg_containor, lf).addToBackStack(null).commit();
                break;
            case R.id.signup:
                SignupFragment sf = new SignupFragment();
                containor.replaceFragment(sf);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginreg_containor, sf).addToBackStack(null).commit();
                break;
            case R.id.bottomForgot:
                ForgotPwdFragment fp = new ForgotPwdFragment();
                containor.replaceFragment(fp);
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginreg_containor, fp).addToBackStack(null).commit();
                break;

        }
    }
}
