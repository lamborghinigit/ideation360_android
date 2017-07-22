package vadevelopment.ideation360.fragments;

import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vadevelopment.ideation360.Appcontroller;
import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.Comments_Skeleton;
import vadevelopment.ideation360.Skeleton.Ratings_Skeleton;
import vadevelopment.ideation360.adapter.AdapterComment;
import vadevelopment.ideation360.adapter.Rating_Adapter;

/**
 * Created by vibrantappz on 7/12/2017.
 */

public class Ratings_Fragment extends Fragment {

    private static String TAG = "Ratings_Fragment";
    private HomeActivity homeactivity;
    private RecyclerView recyclerView;
    private ArrayList<Ratings_Skeleton> rating_arraylist;
    private Rating_Adapter adpater;
    private String serverstatus, ideaid, ideatorid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_ratings, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.ratings));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);
        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   ((Activity)getActivity()).getFragmentManager().pop BackStack();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //  rating_arraylist = new ArrayList<>();
        if (getArguments() != null) {
            //rating_arraylist = getArguments().getParcelableArrayList("rating_list");
            /*adpater = new Rating_Adapter(getActivity(),rating_arraylist,getFragmentManager(),homeactivity);
            recyclerView.setAdapter(adpater);*/
        }
        if (!HandyObjects.isNetworkAvailable(getActivity())) {
            HandyObjects.showAlert(getActivity(), getResources().getString(R.string.application_network_error));
        } else {
            if (getArguments() != null) {
                ideaid = getArguments().getString("ideaid");
                ideatorid = getArguments().getString("ideatorid");
                IdeaDetail_Task(ideaid, ideatorid);
            }

        }
    }


    private void IdeaDetail_Task(String ideaid, final String ideatorid) {
        String tag_json_arry = "json_array_req";
        HandyObjects.startProgressDialog(getActivity());
        final JsonObjectRequest req = new JsonObjectRequest(HandyObjects.IDEADETAIL + "/" + ideaid + "/" + ideatorid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Idea detail", response.toString());
                try {
                    if (serverstatus.equalsIgnoreCase("200")) {
                        JSONArray jarry = response.getJSONArray("Ratings");
                        rating_arraylist = new ArrayList<>();
                        for (int i = 0; i < jarry.length(); i++) {
                            Ratings_Skeleton rske = new Ratings_Skeleton();
                            rske.setName(jarry.getJSONObject(i).getString("IdeatorName"));
                            rske.setIdeatorid(jarry.getJSONObject(i).getString("IdeatorId"));
                            rske.setValue(jarry.getJSONObject(i).getString("Value"));
                            rating_arraylist.add(rske);
                        }
                        adpater = new Rating_Adapter(getActivity(), rating_arraylist, getFragmentManager(), homeactivity);
                        recyclerView.setAdapter(adpater);
                    }
                } catch (Exception e) {
                }
                HandyObjects.stopProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //   HandyObjects.showAlert(getActivity(), "Error with " + error.networkResponse.statusCode + " status code");
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
