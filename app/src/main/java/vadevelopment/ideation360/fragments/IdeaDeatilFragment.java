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
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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
import vadevelopment.ideation360.Skeleton.Comments_Skeleton;
import vadevelopment.ideation360.adapter.AdapterComment;
import vadevelopment.ideation360.adapter.AdapterHome;


/**
 * Created by vibrantappz on 6/22/2017.
 */

public class IdeaDeatilFragment extends Fragment {

    private static String TAG = "IdeaDeatilFragment";
    private HomeActivity homeactivity;
    private TextView text_ideatitle, text_firstcampaign, text_firstideation, text_description, noof_rating, username, useremail, date;
    private String ideaid, serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RatingBar ratingBarbig, ratingBarsmall;
    private ArrayList<Comments_Skeleton> comments_arraylist;
    private RecyclerView comments_recyclerview;
    private AdapterComment adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_ideadetail, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.idea));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);
        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        homeactivity.settingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        text_ideatitle = (TextView) view.findViewById(R.id.text_ideatitle);
        text_firstcampaign = (TextView) view.findViewById(R.id.text_firstcampaign);
        text_firstideation = (TextView) view.findViewById(R.id.text_firstideation);
        text_description = (TextView) view.findViewById(R.id.text_description);
        noof_rating = (TextView) view.findViewById(R.id.noof_rating);
        username = (TextView) view.findViewById(R.id.username);
        useremail = (TextView) view.findViewById(R.id.useremail);
        date = (TextView) view.findViewById(R.id.date);
        ratingBarbig = (RatingBar) view.findViewById(R.id.ratingBarbig);
        ratingBarsmall = (RatingBar) view.findViewById(R.id.ratingBarsmall);
        comments_arraylist = new ArrayList<>();
        adapter = new AdapterComment(getActivity(), comments_arraylist);
        comments_recyclerview = (RecyclerView) view.findViewById(R.id.comments_recyclerview);
        comments_recyclerview.setHasFixedSize(true);
        comments_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        comments_recyclerview.setLayoutManager(mLayoutManager);
        comments_recyclerview.setItemAnimator(new DefaultItemAnimator());

        if (getArguments() != null) {
            ideaid = getArguments().getString("ideaid");
            date.setText(getArguments().getString("date"));
        }

        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            IdeaDetail_Task();
        }

    }

    private void IdeaDetail_Task() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        JsonObjectRequest req = new JsonObjectRequest(HandyObjects.IDEADETAIL + "/" + ideaid + "/" + preferences.getString("ideatorid", ""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());

                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        text_ideatitle.setText(response.getString("Title"));
                        text_firstcampaign.setText(response.getString("CampaignTitle"));
                        text_firstideation.setText(response.getString("IdeationName"));
                        text_description.setText(response.getString("Description"));
                        ratingBarbig.setRating(Float.parseFloat(response.getString("RatingValueByCurrentIdeator")));
                        ratingBarsmall.setRating(Float.parseFloat(response.getString("RatingMeanValue")));
                        noof_rating.setText(response.getString("NrOfRatings"));

                        if (response.getString("IsEditable").equalsIgnoreCase("true")) {
                            homeactivity.settingicon.setVisibility(View.VISIBLE);
                            homeactivity.settingicon.setImageResource(R.drawable.editicon);
                        } else {
                        }
                        JSONArray jarry = response.getJSONArray("Comments");
                        for (int i = 0; i < jarry.length(); i++) {
                            Comments_Skeleton sske = new Comments_Skeleton();
                            sske.setIdeator_name(jarry.getJSONObject(i).getString("IdeatorName"));
                            sske.setComment(jarry.getJSONObject(i).getString("Comment"));
                            comments_arraylist.add(sske);
                        }
                        comments_recyclerview.setAdapter(adapter);
                        getprofile();
                    }
                } catch (Exception e) {
                }
                HandyObjects.stopProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

    private void getprofile() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        //   HandyObjects.startProgressDialog(getActivity());
        JsonObjectRequest req = new JsonObjectRequest(HandyObjects.GETPROFILE + "/" + preferences.getString("ideatorid", ""), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        username.setText(response.getString("FirstName") + " " + response.getString("LastName"));
                        useremail.setText(response.getString("Email"));
                    }
                } catch (Exception e) {
                }
                //HandyObjects.stopProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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