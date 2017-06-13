package vadevelopment.ideation360.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/13/2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{

    private LoginRegister_Containor containor;
    private TextView bottomForgot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        containor = (LoginRegister_Containor) getActivity();
        bottomForgot = (TextView) view.findViewById(R.id.bottomForgot);
        bottomForgot.setOnClickListener(this);
        containor.firstLinear.setVisibility(View.VISIBLE);

        containor.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bottomForgot:
                ForgotPwdFragment fp = new ForgotPwdFragment();
                containor.replaceFragment(fp);
                break;
        }
    }
}
