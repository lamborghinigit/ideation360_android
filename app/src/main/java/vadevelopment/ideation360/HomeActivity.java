package vadevelopment.ideation360;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vadevelopment.ideation360.fragments.AddIdeaFragment;
import vadevelopment.ideation360.fragments.HomeFragment;
import vadevelopment.ideation360.fragments.MyProfileFragment;
import vadevelopment.ideation360.fragments.SearchFragment;
import vadevelopment.ideation360.fragments.SettingFragment;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView hometoptext;
    public LinearLayout radiogroup;
    public RelativeLayout rl_search, rl_addidea, rl_profile;
    public ImageView homeicon, settingicon, searchimg, addideaimg,myprofileimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        initViews();
    }

    private void initViews() {
        HomeFragment hf = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.homefragment, hf).commit();

        radiogroup = (LinearLayout) findViewById(R.id.radiogroup);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_addidea = (RelativeLayout) findViewById(R.id.rl_addidea);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        hometoptext = (TextView) findViewById(R.id.hometoptext);
        homeicon = (ImageView) findViewById(R.id.homeicon);
        settingicon = (ImageView) findViewById(R.id.settingicon);
        searchimg = (ImageView) findViewById(R.id.searchimg);
        addideaimg = (ImageView) findViewById(R.id.addideaimg);
        myprofileimg = (ImageView) findViewById(R.id.myprofileimg);

        rl_search.setOnClickListener(this);
        rl_addidea.setOnClickListener(this);
        rl_profile.setOnClickListener(this);
        settingicon.setOnClickListener(this);
        searchimg.setOnClickListener(this);
        addideaimg.setOnClickListener(this);
        myprofileimg.setOnClickListener(this);
    }

    public void replaceFragmentHome(Fragment fm) {
        getSupportFragmentManager().beginTransaction().replace(R.id.homefragment, fm).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search:
                SearchFragment searchfrg = new SearchFragment();
                replaceFragmentHome(searchfrg);
                break;
            case R.id.rl_addidea:
                AddIdeaFragment addiseaofrg = new AddIdeaFragment();
                replaceFragmentHome(addiseaofrg);
                break;
            case R.id.rl_profile:
                MyProfileFragment myprofile = new MyProfileFragment();
                replaceFragmentHome(myprofile);
                break;
            case R.id.settingicon:
                SettingFragment settingfrg = new SettingFragment();
                replaceFragmentHome(settingfrg);
                break;
            case R.id.searchimg:
                SearchFragment searchfrg_img = new SearchFragment();
                replaceFragmentHome(searchfrg_img);
                break;
            case R.id.addideaimg:
                AddIdeaFragment addiseaofrg_img = new AddIdeaFragment();
                replaceFragmentHome(addiseaofrg_img);
                break;
            case R.id.myprofileimg:
                MyProfileFragment myprofile_img = new MyProfileFragment();
                replaceFragmentHome(myprofile_img);
                break;

        }
    }
}
