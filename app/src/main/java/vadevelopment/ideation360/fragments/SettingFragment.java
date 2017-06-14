package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.MainActivity;
import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class SettingFragment extends Fragment implements View.OnClickListener{

    private HomeActivity homeactivity;
    private TextView profile,savedideas,compaigns,about,logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_settings, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.settings));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);

        profile = (TextView) view.findViewById(R.id.profile);
        savedideas = (TextView) view.findViewById(R.id.savedideas);
        compaigns = (TextView) view.findViewById(R.id.compaigns);
        about = (TextView) view.findViewById(R.id.about);
        logout = (TextView) view.findViewById(R.id.logout);

        profile.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);
        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile:
                AccountFragment af = new AccountFragment();
                homeactivity.replaceFragmentHome(af);
                break;
            case R.id.savedideas:
                break;
            case R.id.compaigns:
                break;
            case R.id.about:
                AboutFragment aboutfrg = new AboutFragment();
                homeactivity.replaceFragmentHome(aboutfrg);
                break;
            case R.id.logout:
                Intent intent = new Intent(getActivity(), LoginRegister_Containor.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}