package vadevelopment.ideation360.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.Campaign_Skeleton;

import static android.R.attr.max;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "SearchFragment";
    private HomeActivity homeactivity;
    public static TextView ideas, campaigns, people;
    private String serverstatus;
    public static EditText et_search;
    /*public static ArrayList<AllIdeas_Skeleton> arraylist_ideas;
    public static ArrayList<Campaign_Skeleton> arraylist_campaigns;*/
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Gson gson = new Gson();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_search, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.search));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);
        ideas = (TextView) view.findViewById(R.id.ideas);
        campaigns = (TextView) view.findViewById(R.id.campaigns);
        people = (TextView) view.findViewById(R.id.people);
        et_search = (EditText) view.findViewById(R.id.et_search);

        //textview.setPaintFlags(textview.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ideas.setOnClickListener(this);
        campaigns.setOnClickListener(this);
        people.setOnClickListener(this);
        Ideas_Fragment idea_frgm = new Ideas_Fragment();
        getChildFragmentManager().beginTransaction().replace(R.id.childcontainor, idea_frgm).commit();

        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ideas.setPaintFlags(ideas.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ideas:
                ideas.setPaintFlags(ideas.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                campaigns.setPaintFlags(campaigns.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                people.setPaintFlags(people.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                // campaigns.setPaintFlags(0);
                Ideas_Fragment idea_frgm = new Ideas_Fragment();
                getChildFragmentManager().beginTransaction().replace(R.id.childcontainor, idea_frgm).commit();
                break;
            case R.id.campaigns:
                //   ideas.setPaintFlags(0);
                campaigns.setPaintFlags(campaigns.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ideas.setPaintFlags(ideas.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                people.setPaintFlags(people.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

                SeachCampaign_Fragment campaign_frgm = new SeachCampaign_Fragment();
                getChildFragmentManager().beginTransaction().replace(R.id.childcontainor, campaign_frgm).commit();
                break;
            case R.id.people:
                ideas.setPaintFlags(ideas.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                people.setPaintFlags(people.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                campaigns.setPaintFlags(campaigns.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));

                People_Fragment people_frgm = new People_Fragment();
                getChildFragmentManager().beginTransaction().replace(R.id.childcontainor, people_frgm).commit();
                break;
        }
    }


}