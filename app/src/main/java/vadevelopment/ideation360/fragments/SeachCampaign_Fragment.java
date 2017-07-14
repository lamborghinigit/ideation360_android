package vadevelopment.ideation360.fragments;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.Campaign_Skeleton;
import vadevelopment.ideation360.Skeleton.People_Skeleton;
import vadevelopment.ideation360.adapter.AdapterCampaign;

/**
 * Created by vibrantappz on 6/23/2017.
 */

public class SeachCampaign_Fragment extends Fragment {

    private AdapterCampaign adapter;
    private RecyclerView recyclerView;
    private static String TAG = "SeachCampaign_Fragment";
    private String serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_campaign, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        try {
            //SearchFragment.ideas.setText("IDEAS");
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            editor = preferences.edit();
            String json = preferences.getString("MyObject_Campaigns", "");
            Type type = new TypeToken<ArrayList<Campaign_Skeleton>>() {
            }.getType();
            HomeActivity.arraylist_campaigns = gson.fromJson(json, type);

            adapter = new AdapterCampaign(getContext(), HomeActivity.arraylist_campaigns, getFragmentManager());
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
        }
        SearchFragment.et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i(TAG, "Enter pressed");
                    if (!HandyObjects.isNetworkAvailable(getActivity())) {
                        HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
                    } else {
                        if(SearchFragment.et_search.getText().toString().length() == 0){} else {
                            SearchAll(SearchFragment.et_search.getText().toString());
                        }
                    }
                }
                return false;
            }
        });
    }

    private void SearchAll(String gettext) {
        String tag_json_obj = "json_obj_req";
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("IdeatorId", preferences.getString("ideatorid", ""));
        postParam.put("SearchFor", gettext);
        HandyObjects.startProgressDialog(getActivity());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                HandyObjects.SEARCH, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d(TAG, res.toString());
                        try {
                            final JSONObject response = new JSONObject(res.toString());
                            if (serverstatus.equalsIgnoreCase("200")) {
                                JSONArray jarry = response.getJSONArray("Ideas");
                                HomeActivity.arraylist_ideas.clear();
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
                                    allidea_ske.setImage("https://app.ideation360.com/api/getprofileimage/" + jinside.getString("IdeatorId"));
                                    HomeActivity.arraylist_ideas.add(allidea_ske);
                                }
                                String json = gson.toJson(HomeActivity.arraylist_ideas);
                                editor.putString("MyObject_Ideas", json);
                                editor.commit();

                                JSONArray jarry_comp = response.getJSONArray("Campaigns");
                                if (jarry_comp.length() == 0) {
                                    HomeActivity.arraylist_campaigns.clear();
                                    recyclerView.setAdapter(adapter);
                                    HandyObjects.showAlert(getActivity(), getResources().getString(R.string.noresultfound));
                                    String json_camapigns = gson.toJson(HomeActivity.arraylist_campaigns);
                                    editor.putString("MyObject_Campaigns", json_camapigns);
                                    editor.commit();
                                } else {
                                    HomeActivity.arraylist_campaigns.clear();
                                    for (int j = 0; j < jarry_comp.length(); j++) {
                                        Campaign_Skeleton camp_ske = new Campaign_Skeleton();
                                        JSONObject jobj = jarry_comp.getJSONObject(j);
                                        camp_ske.setCompaignid(jobj.getString("CampaignId"));
                                        camp_ske.setCampaign_name(jobj.getString("Name"));
                                        camp_ske.setIdeas_submitted(jobj.getString("NrOfIdeas"));
                                        camp_ske.setDaysleft(jobj.getString("NrOfDaysLeft"));
                                        HomeActivity.arraylist_campaigns.add(camp_ske);
                                    }
                                    recyclerView.setAdapter(adapter);
                                    String json_camapigns = gson.toJson(HomeActivity.arraylist_campaigns);
                                    editor.putString("MyObject_Campaigns", json_camapigns);
                                    editor.commit();
                                }

                                JSONArray jarry_people = response.getJSONArray("Ideators");
                                HomeActivity.arraylist_people.clear();
                                for (int k = 0; k < jarry_people.length(); k++) {
                                    People_Skeleton people_ske = new People_Skeleton();
                                    JSONObject jobj = jarry_people.getJSONObject(k);
                                    people_ske.setName(jobj.getString("FirstName") + " " + jobj.getString("LastName"));
                                    people_ske.setIdeatorid(jobj.getString("IdeatorId"));
                                    people_ske.setImage("https://app.ideation360.com/api/getprofileimage/" + jobj.getString("IdeatorId"));
                                    HomeActivity.arraylist_people.add(people_ske);
                                }
                                String json_people = gson.toJson(HomeActivity.arraylist_people);
                                editor.putString("MyObject_people", json_people);
                                editor.commit();
                            } else if (serverstatus.equalsIgnoreCase("400")) {
                                // HandyObjects.showAlert(getActivity(), getResources().getString(R.string.alreadyaccount));
                                HomeActivity.arraylist_campaigns.clear();
                                recyclerView.setAdapter(adapter);
                                String json_camapigns = gson.toJson(HomeActivity.arraylist_campaigns);
                                editor.putString("MyObject_Campaigns", json_camapigns);
                                editor.commit();
                            }
                            HandyObjects.stopProgressDialog();
                        } catch (Exception e) {
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
                HomeActivity.arraylist_campaigns.clear();
                recyclerView.setAdapter(adapter);
                String json_camapigns = gson.toJson(HomeActivity.arraylist_campaigns);
                editor.putString("MyObject_Campaigns", json_camapigns);
                editor.commit();
                HandyObjects.stopProgressDialog();
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
        Appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
