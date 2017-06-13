package vadevelopment.ideation360.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/13/2017.
 */

public class ForgotPwdFragment extends Fragment {

    private LoginRegister_Containor containor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_forgotpwd,container,false);
        initViews();
        return view;
    }

    private void initViews(){
        containor = (LoginRegister_Containor) getActivity();
        containor.firstLinear.setVisibility(View.VISIBLE);

        containor.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
