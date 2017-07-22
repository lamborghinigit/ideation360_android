package vadevelopment.ideation360;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.Campaign_Skeleton;
import vadevelopment.ideation360.Skeleton.People_Skeleton;
import vadevelopment.ideation360.fragments.AddIdeaFragment;
import vadevelopment.ideation360.fragments.HomeFragment;
import vadevelopment.ideation360.fragments.MyProfileFragment;
import vadevelopment.ideation360.fragments.SavedIdeasFragment;
import vadevelopment.ideation360.fragments.SearchFragment;
import vadevelopment.ideation360.fragments.SettingFragment;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class HomeActivity extends CameraActivity implements View.OnClickListener {

    public TextView hometoptext, counttext, counttext_top;
    public LinearLayout radiogroup;
    public RelativeLayout rl_search, rl_addidea, rl_profile;
    public ImageView homeicon, settingicon, searchimg, addideaimg, myprofileimg;
    public static ArrayList<AllIdeas_Skeleton> arraylist_ideas;
    public static ArrayList<Campaign_Skeleton> arraylist_campaigns;
    public static ArrayList<People_Skeleton> arraylist_people;
    Gson gson = new Gson();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        initViews();
    }

    private void initViews() {
        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor = preferences.edit();
        HomeFragment hf = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.homefragment, hf).commit();
        //Go to settings, accessibility and turn off "high contrast text".
        radiogroup = (LinearLayout) findViewById(R.id.radiogroup);
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        rl_addidea = (RelativeLayout) findViewById(R.id.rl_addidea);
        rl_profile = (RelativeLayout) findViewById(R.id.rl_profile);
        hometoptext = (TextView) findViewById(R.id.hometoptext);
        counttext = (TextView) findViewById(R.id.counttext);
        counttext_top = (TextView) findViewById(R.id.counttext_top);
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
        arraylist_ideas = new ArrayList<>();
        arraylist_campaigns = new ArrayList<>();
        arraylist_people = new ArrayList<>();
        String json = gson.toJson(arraylist_ideas);
        editor.putString("MyObject_Ideas", json);
        editor.commit();

        String json_camapigns = gson.toJson(arraylist_campaigns);
        editor.putString("MyObject_Campaigns", json_camapigns);
        editor.commit();

        String json_people = gson.toJson(arraylist_people);
        editor.putString("MyObject_people", json_people);
        editor.commit();
    }

    public void replaceFragmentHome(Fragment fm) {
        getSupportFragmentManager().beginTransaction().replace(R.id.homefragment, fm).addToBackStack(null).commit();
    }

    public void replaceFragmentHomeWithoutback(Fragment fm) {
        getSupportFragmentManager().beginTransaction().replace(R.id.homefragment, fm).commit();
    }

    public void replaceChilFragmentHome(Fragment fm, FragmentManager fmm) {
        fmm.beginTransaction().replace(R.id.homefragment, fm).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search:
                arraylist_ideas = new ArrayList<>();
                arraylist_campaigns = new ArrayList<>();
                arraylist_people = new ArrayList<>();
                arraylist_ideas.clear();
                arraylist_campaigns.clear();
                arraylist_people.clear();
                String json = gson.toJson(arraylist_ideas);
                editor.putString("MyObject_Ideas", json);
                editor.commit();
                String json_camapigns = gson.toJson(arraylist_campaigns);
                editor.putString("MyObject_Campaigns", json_camapigns);
                editor.commit();
                String json_people = gson.toJson(arraylist_people);
                editor.putString("MyObject_people", json_people);
                editor.commit();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (SavedIdeasFragment.onsavedidea == true) {
           /* FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }*/
            //replaceFragmentHomeWithoutback(new HomeFragment());
            finish();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

        }
    }
}
