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
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.adapter.AdapterHome;

import static android.content.ContentValues.TAG;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class HomeFragment extends Fragment {

    private static String TAG = "HomeFragment";
    private HomeActivity homeactivity;
    private RecyclerView recyclerView;
    private AdapterHome adapter;
    private RelativeLayout rl_forblankarray;
    private String serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<AllIdeas_Skeleton> arraylist;
    private Gson gson = new Gson();

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
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        homeactivity.counttext_top.setVisibility(View.INVISIBLE);
        editor = preferences.edit();
        arraylist = new ArrayList<>();
        rl_forblankarray = (RelativeLayout) view.findViewById(R.id.rl_forblankarray);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterHome(getContext(), "pro", arraylist, getFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        HomeActivity.arraylist_ideas.clear();
        HomeActivity.arraylist_campaigns.clear();
        HomeActivity.arraylist_people.clear();
        String json = gson.toJson(HomeActivity.arraylist_ideas);
        editor.putString("MyObject_Ideas", json);
        editor.commit();
        String json_camapigns = gson.toJson(HomeActivity.arraylist_campaigns);
        editor.putString("MyObject_Campaigns", json_camapigns);
        editor.commit();
        String json_people = gson.toJson(HomeActivity.arraylist_people);
        editor.putString("MyObject_people", json_people);
        editor.commit();


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

        rl_forblankarray.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            getIdeas();
        }
    }


    private void getIdeas() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        JsonArrayRequest req = new JsonArrayRequest(HandyObjects.CLIENTIDEAS + "/" + preferences.getString("clientid", "") + "/" + preferences.getString("ideatorid", ""), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());

                if (serverstatus.equalsIgnoreCase("200")) {
                    if (response.length() == 0) {
                        rl_forblankarray.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        try {
                            rl_forblankarray.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jinside = response.getJSONObject(i);
                                AllIdeas_Skeleton allidea_ske = new AllIdeas_Skeleton();
                                allidea_ske.setIdeas_id(jinside.getString("IdeaId"));
                                allidea_ske.setIdeator_id(jinside.getString("IdeatorId"));
                                allidea_ske.setMain_title(jinside.getString("Title"));
                                allidea_ske.setCampaign_title(jinside.getString("CampaignTitle"));
                                allidea_ske.setRating_meanvalue(jinside.getString("RatingMeanValue"));
                                allidea_ske.setNoof_rating(jinside.getString("NrOfRatings"));
                                allidea_ske.setNoof_comments(jinside.getString("NrOfComments"));
                                //  allidea_ske.setDate(jinside.getString("PostedDate"));

                                String[] d = jinside.getString("PostedDate").split("T");
                                String[] datesplit = d[0].split("-");
                                //  viewHolder.date.setText(datesplit[0] + "." + datesplit[1] + "." + datesplit[2]);
                                allidea_ske.setDate(datesplit[0] + "." + datesplit[1] + "." + datesplit[2]);
                                DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
                                Date date = (Date) formatter.parse(datesplit[0] + "." + datesplit[1] + "." + datesplit[2] + " " + d[1]);
                                allidea_ske.setDatetimestamp(String.valueOf(date.getTime()));
                                allidea_ske.setImage("https://app.ideation360.com/api/getprofileimage/" + jinside.getString("IdeatorId"));
                                arraylist.add(allidea_ske);
                            }
                            Collections.sort(arraylist, new Comparator<AllIdeas_Skeleton>() {
                                @Override
                                public int compare(AllIdeas_Skeleton publicao_skeleton, AllIdeas_Skeleton t1) {
                                    return t1.getDatetimestamp().compareTo(publicao_skeleton.getDatetimestamp());
                                }
                            });
                            // Collections.reverse(arraylist);
                            recyclerView.setAdapter(adapter);
                            getprofile();
                        } catch (Exception e) {
                        }
                    }
                    HandyObjects.stopProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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

    private void getprofile() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        JsonObjectRequest req = new JsonObjectRequest(HandyObjects.GETPROFILE + "/" + preferences.getString("ideatorid", ""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        if (Integer.parseInt(response.getString("NrOfNewNotifications")) > 0) {
                            homeactivity.counttext.setText(response.getString("NrOfNewNotifications"));
                            homeactivity.counttext.setVisibility(View.VISIBLE);
                        } else {
                            homeactivity.counttext.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (Exception e) {
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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
