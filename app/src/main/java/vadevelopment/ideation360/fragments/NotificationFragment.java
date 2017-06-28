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
import vadevelopment.ideation360.Skeleton.NotificationNew_Skeleton;
import vadevelopment.ideation360.Skeleton.NotificationSeen_Skeleton;
import vadevelopment.ideation360.adapter.AdapterHome;
import vadevelopment.ideation360.adapter.AdapterNotificationNew;
import vadevelopment.ideation360.adapter.AdapterNotificationSeen;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class NotificationFragment extends Fragment {

    private static String TAG = "NotificationFragment";
    private HomeActivity homeactivity;
    private String serverstatus;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView_new, recyclerView_seen;
    private AdapterNotificationNew adapter_new;
    private AdapterNotificationSeen adapter_seen;
    private ArrayList<NotificationNew_Skeleton> notinew_arraylist;
    private ArrayList<NotificationSeen_Skeleton> notiseen_arraylist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.notifications));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        notinew_arraylist = new ArrayList<>();
        notiseen_arraylist = new ArrayList<>();

        recyclerView_new = (RecyclerView) view.findViewById(R.id.recyclerView_new);
        recyclerView_new.setHasFixedSize(true);
        recyclerView_new.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_new = new AdapterNotificationNew(getContext(), notinew_arraylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_new.setLayoutManager(mLayoutManager);
        recyclerView_new.setItemAnimator(new DefaultItemAnimator());

        recyclerView_seen = (RecyclerView) view.findViewById(R.id.recyclerView_seen);
        recyclerView_seen.setHasFixedSize(true);
        recyclerView_seen.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter_seen = new AdapterNotificationSeen(getContext(), notiseen_arraylist);
        RecyclerView.LayoutManager mmLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_seen.setLayoutManager(mmLayoutManager);
        recyclerView_seen.setItemAnimator(new DefaultItemAnimator());

        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            NotificationTask();
        }
    }

    private void NotificationTask() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        JsonArrayRequest req = new JsonArrayRequest(HandyObjects.NOTIFICATION + "/" + preferences.getString("ideatorid", ""), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        for (int i = 0; i < response.length(); i++) {
                            if (response.getJSONObject(i).getString("IsRead").equalsIgnoreCase("true")) {
                                NotificationSeen_Skeleton seen_ske = new NotificationSeen_Skeleton();
                                seen_ske.setIdeaId(response.getJSONObject(i).getString("IdeaId"));
                                seen_ske.setIdeatorId(response.getJSONObject(i).getString("IdeatorId"));
                                seen_ske.setIdeaTitle(response.getJSONObject(i).getString("IdeaTitle"));
                                seen_ske.setIdeatorName(response.getJSONObject(i).getString("IdeatorName"));
                                seen_ske.setActivityDate(response.getJSONObject(i).getString("ActivityDate"));
                                seen_ske.setActivityType(response.getJSONObject(i).getString("ActivityType"));
                                seen_ske.setActivityId(response.getJSONObject(i).getString("ActivityId"));
                                seen_ske.setIdeaCommentId(response.getJSONObject(i).getString("IdeaCommentId"));
                                notiseen_arraylist.add(seen_ske);
                            } else {
                                NotificationNew_Skeleton new_ske = new NotificationNew_Skeleton();
                                new_ske.setIdeaId(response.getJSONObject(i).getString("IdeaId"));
                                new_ske.setIdeatorId(response.getJSONObject(i).getString("IdeatorId"));
                                new_ske.setIdeaTitle(response.getJSONObject(i).getString("IdeaTitle"));
                                new_ske.setIdeatorName(response.getJSONObject(i).getString("IdeatorName"));
                                new_ske.setActivityDate(response.getJSONObject(i).getString("ActivityDate"));
                                new_ske.setActivityType(response.getJSONObject(i).getString("ActivityType"));
                                new_ske.setActivityId(response.getJSONObject(i).getString("ActivityId"));
                                new_ske.setIdeaCommentId(response.getJSONObject(i).getString("IdeaCommentId"));
                                notinew_arraylist.add(new_ske);
                            }
                        }
                        recyclerView_new.setAdapter(adapter_new);
                        recyclerView_seen.setAdapter(adapter_seen);
                    }
                } catch (Exception e) {
                }
                HandyObjects.stopProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
              //  HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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
