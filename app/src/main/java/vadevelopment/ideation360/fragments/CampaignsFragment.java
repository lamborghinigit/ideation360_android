package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.Campaign_Skeleton;
import vadevelopment.ideation360.adapter.AdapterCampaign;
import vadevelopment.ideation360.adapter.AdapterHome;

/**
 * Created by vibrantappz on 6/22/2017.
 */

public class CampaignsFragment extends Fragment {

    private static String TAG = "CampaignsFragment";
    private HomeActivity homeactivity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private AdapterCampaign adapter;
    private ArrayList<Campaign_Skeleton> arraylist;
    private String serverstatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_campaign, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.campaigns));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        arraylist = new ArrayList<>();
        adapter = new AdapterCampaign(getContext(), arraylist, getFragmentManager());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            getAllCampaigns();
        }
    }

    private void getAllCampaigns() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        JsonArrayRequest req = new JsonArrayRequest(HandyObjects.MYCAMPAIGNS + "/" + preferences.getString("ideatorid", ""), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if (serverstatus.equalsIgnoreCase("200")) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            Campaign_Skeleton camp_ske = new Campaign_Skeleton();
                            JSONObject jobj = response.getJSONObject(i);
                            camp_ske.setCompaignid(jobj.getString("CampaignId"));
                            camp_ske.setCampaign_name(jobj.getString("Name"));
                            camp_ske.setIdeas_submitted(jobj.getString("NrOfIdeas"));
                            camp_ske.setDaysleft(jobj.getString("NrOfDaysLeft"));
                            arraylist.add(camp_ske);
                        }
                        Collections.reverse(arraylist);
                        recyclerView.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                    HandyObjects.stopProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
                HandyObjects.stopProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
                return headers;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                serverstatus = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(req, tag_json_arry);
    }
}