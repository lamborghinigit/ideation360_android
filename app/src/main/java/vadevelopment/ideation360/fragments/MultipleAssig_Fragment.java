package vadevelopment.ideation360.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import vadevelopment.ideation360.HandyObjects;
import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.MultipleAssign_Skeleton;
import vadevelopment.ideation360.database.ParseOpenHelper;

/**
 * Created by vibrantappz on 6/19/2017.
 */

public class MultipleAssig_Fragment extends Fragment {

    private LinearLayout linearAddbtn;
    private SQLiteDatabase database;
    private ArrayList<MultipleAssign_Skeleton> arraylist;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LoginRegister_Containor containor;
    private int posi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_multiassign, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        containor = (LoginRegister_Containor) getActivity();
        containor.firstLinear.setVisibility(View.VISIBLE);
        containor.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // HandyObjects.showAlert(getActivity(), String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
                   // HandyObjects.showAlert(getActivity(), String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));
                    getActivity().finish();
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        linearAddbtn = (LinearLayout) view.findViewById(R.id.linearAddbtn);

        arraylist = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        database = ParseOpenHelper.getInstance(getActivity()).getWritableDatabase();
        Cursor cursor = database.query(ParseOpenHelper.TABLE_NAME_ASSIGNMENTS, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MultipleAssign_Skeleton mult_assgn = new MultipleAssign_Skeleton();
            mult_assgn.setCompany_name(cursor.getString(cursor.getColumnIndex("company_name")));
            mult_assgn.setClientid(cursor.getString(cursor.getColumnIndex("client_id")));
            mult_assgn.setIdeatorid(cursor.getString(cursor.getColumnIndex("ideator_id")));
            arraylist.add(mult_assgn);
            cursor.moveToNext();
        }
        cursor.close();
        for (int i = 0; i < arraylist.size(); i++) {
            //  posi = i;
            Log.e("btnnnnnnnnnn", arraylist.get(i).getCompany_name());
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View mView = layoutInflater.inflate(R.layout.adddynamic_buttons, null, false);
            Button dynamic_btn = (Button) mView.findViewById(R.id.dynamic_btn);

            dynamic_btn.setText(arraylist.get(i).getCompany_name());
            linearAddbtn.addView(mView);
            int posi = linearAddbtn.indexOfChild(mView);
            linearAddbtn.setTag(posi);

            dynamic_btn.setTag(R.id.dynamic_btn, posi);
            dynamic_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag(R.id.dynamic_btn);
                    //HandyObjects.showAlert(getActivity(), arraylist.get(position).getCompany_name());
                    editor.putString("clientid", arraylist.get(position).getClientid());
                    editor.putString("ideatorid", arraylist.get(position).getIdeatorid());
                    editor.commit();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }


}
