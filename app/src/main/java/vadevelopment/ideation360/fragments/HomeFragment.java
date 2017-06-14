package vadevelopment.ideation360.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.adapter.AdapterHome;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class HomeFragment extends Fragment {

    private HomeActivity homeactivity;
    private RecyclerView recyclerView;
    private AdapterHome adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_home, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.homeicon.setVisibility(View.VISIBLE);
        homeactivity.settingicon.setVisibility(View.VISIBLE);
        homeactivity.radiogroup.setVisibility(View.VISIBLE);
        homeactivity.homeicon.setImageResource(R.drawable.homeimg);
        homeactivity.settingicon.setImageResource(R.drawable.setting);
        homeactivity.hometoptext.setText(getResources().getString(R.string.hometoptext));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterHome(getContext(), "pro");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        homeactivity.settingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingFragment settingfrg = new SettingFragment();
                homeactivity.replaceFragmentHome(settingfrg);
            }
        });
        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homefrg = new HomeFragment();
                homeactivity.replaceFragmentHome(homefrg);
            }
        });
    }
}
