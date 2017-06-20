package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.MultipleAssign_Skeleton;
import vadevelopment.ideation360.Skeleton.SavedIdeas_Skeleton;
import vadevelopment.ideation360.adapter.AdapterHome;
import vadevelopment.ideation360.adapter.AdapterSavedIdeas;
import vadevelopment.ideation360.database.ParseOpenHelper;

/**
 * Created by vibrantappz on 6/20/2017.
 */

public class SavedIdeasFragment extends Fragment {

    private HomeActivity homeactivity;
    private RecyclerView recyclerView;
    private AdapterSavedIdeas adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase database;
    private ArrayList<SavedIdeas_Skeleton> arrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_savedideas, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.savedideas));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);

        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new AdapterSavedIdeas(getContext(), arrayList);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        database = ParseOpenHelper.getInstance(getActivity()).getWritableDatabase();


        Cursor cursor = database.query(ParseOpenHelper.TABLE_NAME_SAVEDIDEA, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SavedIdeas_Skeleton saved_skel = new SavedIdeas_Skeleton();
            saved_skel.setCampaign(cursor.getString(cursor.getColumnIndex("campaign")));
            saved_skel.setIdea_name(cursor.getString(cursor.getColumnIndex("idea_name")));
            saved_skel.setIdea_description(cursor.getString(cursor.getColumnIndex("idea_description")));
            saved_skel.setImage_path(cursor.getString(cursor.getColumnIndex("image_path")));
            saved_skel.setAudio_path(cursor.getString(cursor.getColumnIndex("audio_path")));
            arrayList.add(saved_skel);
            cursor.moveToNext();
        }
        cursor.close();

        recyclerView.setAdapter(adapter);
        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}