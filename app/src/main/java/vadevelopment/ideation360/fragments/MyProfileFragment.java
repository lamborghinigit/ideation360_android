package vadevelopment.ideation360.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.JsonObject;

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
import vadevelopment.ideation360.adapter.AdapterHome;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class MyProfileFragment extends Fragment {

    private static String TAG = "MyProfileFragment";
    private HomeActivity homeactivity;
    private RecyclerView recyclerView;
    private AdapterHome adapter;
    ArrayList<AllIdeas_Skeleton> arraylist;
    private String serverstatus, ideatorid;
    private ImageView imageview;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView name, email, myind_idea, compaign, myidea;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_myprofile, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();

        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.radiogroup.setVisibility(View.GONE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        arraylist = new ArrayList<>();

        name = (TextView) view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);
        myind_idea = (TextView) view.findViewById(R.id.myind_idea);
        compaign = (TextView) view.findViewById(R.id.compaign);
        myidea = (TextView) view.findViewById(R.id.myidea);
        imageview = (ImageView) view.findViewById(R.id.imageview);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterHome(getContext(), "profile", arraylist, getFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setAdapter(adapter);


        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        homeactivity.settingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment nf = new NotificationFragment();
                homeactivity.replaceFragmentHome(nf);
            }
        });
        if (getArguments() != null) {
            ideatorid = getArguments().getString("ideatorid");
            if (ideatorid.equalsIgnoreCase(preferences.getString("ideatorid", ""))) {
                homeactivity.hometoptext.setText(getResources().getString(R.string.myprofile));
                homeactivity.settingicon.setVisibility(View.VISIBLE);
                homeactivity.settingicon.setImageResource(R.drawable.bell);
            } else {
                homeactivity.settingicon.setVisibility(View.GONE);
                homeactivity.hometoptext.setText(getArguments().getString("name") + "'s" + " Profile");
                myidea.setText(getArguments().getString("name") + "'s" + " ideas");
            }
            LazyHeaders.Builder builder = new LazyHeaders.Builder()
                    .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
            GlideUrl glideUrl = new GlideUrl("https://app.ideation360.com/api/getprofileimage/" + ideatorid, builder.build());
            Glide.with(getActivity()).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageview);
        } else {
            // HandyObjects.showAlert(getActivity(), preferences.getString("ideatorid", ""));
            ideatorid = preferences.getString("ideatorid", "");
            homeactivity.hometoptext.setText(getResources().getString(R.string.myprofile));
            homeactivity.settingicon.setVisibility(View.VISIBLE);
            homeactivity.settingicon.setImageResource(R.drawable.bell);
            LazyHeaders.Builder builder = new LazyHeaders.Builder()
                    .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
            GlideUrl glideUrl = new GlideUrl("https://app.ideation360.com/api/getprofileimage/" + preferences.getString("ideatorid", ""), builder.build());
            Glide.with(getActivity()).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageview);
        }

        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            getprofile();
        }

    }

    private void getprofile() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        JsonObjectRequest req = new JsonObjectRequest(HandyObjects.GETPROFILE + "/" + ideatorid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  Log.d(TAG, response.toString());

                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        name.setText(response.getString("FirstName") + " " + response.getString("LastName"));
                        email.setText(response.getString("Email"));
                        getAllMyideas();
                    }
                } catch (Exception e) {
                }
                //HandyObjects.stopProgressDialog();

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
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                serverstatus = String.valueOf(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private void getAllMyideas() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        //HandyObjects.startProgressDialog(getActivity());
        JsonArrayRequest req = new JsonArrayRequest(HandyObjects.MYIDEAS + "/" + ideatorid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());

                if (serverstatus.equalsIgnoreCase("200")) {
                    try {
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
                            allidea_ske.setDate(jinside.getString("PostedDate"));
                            allidea_ske.setImage("https://app.ideation360.com/api/getprofileimage/" + jinside.getString("IdeatorId"));
                            arraylist.add(allidea_ske);
                        }
                        myind_idea.setText(String.valueOf(arraylist.size()) + " " + "Ideas");
                        recyclerView.setAdapter(adapter);

                    } catch (Exception e) {
                    }
                    // HandyObjects.stopProgressDialog();
                    getAllCampaigns();
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

    private void getAllCampaigns() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        //  HandyObjects.startProgressDialog(getActivity());
        JsonArrayRequest req = new JsonArrayRequest(HandyObjects.MYCAMPAIGNS + "/" + ideatorid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if (serverstatus.equalsIgnoreCase("200")) {
                    try {
                        compaign.setText(String.valueOf(response.length()) + " " + "Campaign");
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