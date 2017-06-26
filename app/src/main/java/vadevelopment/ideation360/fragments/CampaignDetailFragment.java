package vadevelopment.ideation360.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

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
import vadevelopment.ideation360.adapter.AdapterCampaign;
import vadevelopment.ideation360.adapter.AdapterHome;

/**
 * Created by vibrantappz on 6/23/2017.
 */

public class CampaignDetailFragment extends Fragment {

    private static String TAG = "CampaignDetailFragment";
    private HomeActivity homeactivity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private AdapterHome adapter;
    private ArrayList<AllIdeas_Skeleton> arraylist;
    private String serverstatus, dayleft, campaignid;
    private TextView text_daysleft, campaign_name, ideationname, sponsername, text_description, noof_ideas;
    private Button submitidea;
    Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_campaigndetail, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.campaign));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        arraylist = new ArrayList<>();
        adapter = new AdapterHome(getContext(), "profile", arraylist, getFragmentManager());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        text_daysleft = (TextView) view.findViewById(R.id.text_daysleft);
        campaign_name = (TextView) view.findViewById(R.id.campaign_name);
        ideationname = (TextView) view.findViewById(R.id.ideationname);
        sponsername = (TextView) view.findViewById(R.id.sponsername);
        text_description = (TextView) view.findViewById(R.id.text_description);
        noof_ideas = (TextView) view.findViewById(R.id.noof_ideas);
        submitidea = (Button) view.findViewById(R.id.submitidea);
        recyclerView.setNestedScrollingEnabled(false);
        handler = new Handler();

        if (getArguments() != null) {
            campaignid = getArguments().getString("campaignid");
        }

        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            getCampaignsDetail();
        }

        // showing add idea screen
        submitidea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeactivity.replaceFragmentHome(new AddIdeaFragment());
            }
        });

    }

    private void getCampaignsDetail() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        JsonObjectRequest req = new JsonObjectRequest(HandyObjects.CAMPAIGNS_DETAIL + "/" + campaignid + "/" + preferences.getString("ideatorid", ""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.d(TAG, response.toString());
                if (serverstatus.equalsIgnoreCase("200")) {
                    try {
                        campaign_name.setText(response.getString("Name"));
                        ideationname.setText(response.getString("IdeationName"));
                        text_description.setText(response.getString("Description"));
                        noof_ideas.setText(response.getString("NrOfIdeas"));
                        if (Integer.parseInt(response.getString("NrOfDaysLeft")) > 1) {
                            text_daysleft.setText(response.getString("NrOfDaysLeft") + " " + "days left");
                        } else {
                            text_daysleft.setText(response.getString("NrOfDaysLeft") + " " + "day left");
                        }
                        if (response.getString("SponsorName") != null && response.getString("SponsorName").isEmpty()) {
                            sponsername.setText(response.getString("SponsorName"));
                        } else {
                            sponsername.setText(getResources().getString(R.string.nosponsor_assigned));
                        }
/*
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {*/
                        JSONArray jarry = response.getJSONArray("Ideas");
                        for (int i = 0; i < jarry.length(); i++) {
                            JSONObject jinside = jarry.getJSONObject(i);
                            AllIdeas_Skeleton allidea_ske = new AllIdeas_Skeleton();
                            allidea_ske.setIdeas_id(jinside.getString("IdeaId"));
                            allidea_ske.setIdeator_id(jinside.getString("IdeatorId"));
                            allidea_ske.setMain_title(jinside.getString("Title"));
                            allidea_ske.setCampaign_title(jinside.getString("CampaignTitle"));
                            allidea_ske.setRating_meanvalue(jinside.getString("RatingMeanValue"));
                            allidea_ske.setNoof_rating(jinside.getString("NrOfRatings"));
                            allidea_ske.setNoof_comments(jinside.getString("NrOfComments"));
                            allidea_ske.setDate(jinside.getString("PostedDate"));
                            arraylist.add(allidea_ske);
                        }
                        recyclerView.setAdapter(adapter);
                          /*      }
                                catch (Exception e){}

                            }
                        },1000);*/

                    } catch (Exception e) {
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            HandyObjects.stopProgressDialog();
                        }
                    }, 50);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
           //     HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                serverstatus = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(req, tag_json_arry);
    }
}
