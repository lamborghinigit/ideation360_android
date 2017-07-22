package vadevelopment.ideation360.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.LoginRegister_Containor;
import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/14/2017.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {

    private HomeActivity homeactivity;
    private TextView profile, savedideas, compaigns, about, logout, changelanguage;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String lang = "en";
    Locale myLocale;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgm_settings, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        homeactivity = (HomeActivity) getActivity();
        homeactivity.hometoptext.setText(getResources().getString(R.string.settings));
        homeactivity.homeicon.setImageResource(R.drawable.backarrow);
        homeactivity.settingicon.setVisibility(View.INVISIBLE);
        homeactivity.radiogroup.setVisibility(View.GONE);
        homeactivity.counttext_top.setVisibility(View.INVISIBLE);

        profile = (TextView) view.findViewById(R.id.profile);
        savedideas = (TextView) view.findViewById(R.id.savedideas);
        compaigns = (TextView) view.findViewById(R.id.compaigns);
        about = (TextView) view.findViewById(R.id.about);
        logout = (TextView) view.findViewById(R.id.logout);
        changelanguage = (TextView) view.findViewById(R.id.changelanguage);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();

        profile.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);
        savedideas.setOnClickListener(this);
        compaigns.setOnClickListener(this);
        changelanguage.setOnClickListener(this);

        homeactivity.homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile:
                AccountFragment af = new AccountFragment();
                homeactivity.replaceFragmentHome(af);
                break;
            case R.id.savedideas:
                SavedIdeasFragment sf = new SavedIdeasFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "setting");
                sf.setArguments(bundle);
                homeactivity.replaceFragmentHome(sf);
                break;
            case R.id.compaigns:
                CampaignsFragment compaignsfrg = new CampaignsFragment();
                homeactivity.replaceFragmentHome(compaignsfrg);
                break;
            case R.id.about:
                AboutFragment aboutfrg = new AboutFragment();
                homeactivity.replaceFragmentHome(aboutfrg);
                break;
            case R.id.logout:
          /*      editor.putString("loginwith", "noone");
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginRegister_Containor.class);
                startActivity(intent);
                getActivity().finish();*/
                displayMediaPickerDialog();
                break;
            case R.id.changelanguage:
                ChangeLanguageDialog();
                break;
        }
    }


    private void displayMediaPickerDialog() {
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        final Dialog mediaDialog = new Dialog(getActivity());
        mediaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mediaDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mediaDialog.setContentView(R.layout.dialog_logout);
        LinearLayout approx_lay = (LinearLayout) mediaDialog.findViewById(R.id.approx_lay);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w - 80, (h / 4) - 20);
        approx_lay.setLayoutParams(params);

        TextView yes = (TextView) mediaDialog.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaDialog.dismiss();
                editor.putString("loginwith", "noone");
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginRegister_Containor.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        TextView no = (TextView) mediaDialog.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaDialog.dismiss();
            }
        });
        mediaDialog.show();
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        editor.putString(langPref, lang);
        editor.commit();
    }

    private void updateTexts() {
        profile.setText(R.string.profile);
        savedideas.setText(R.string.savedideas);
        compaigns.setText(R.string.campaigns);
        about.setText(R.string.about);
        logout.setText(R.string.logout);
        changelanguage.setText(R.string.changelanguage);
    }

    private void ChangeLanguageDialog() {
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        final Dialog mediaDialog = new Dialog(getActivity());
        mediaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mediaDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mediaDialog.setContentView(R.layout.changelanguage_dialog);
        LinearLayout approx_lay = (LinearLayout) mediaDialog.findViewById(R.id.approx_lay);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w - 30, (h / 3) - 20);
        approx_lay.setLayoutParams(params);

        TextView et_english = (TextView) mediaDialog.findViewById(R.id.et_english);
        et_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaDialog.dismiss();
                lang = "en";
                changeLang(lang);

            }
        });
        TextView et_sweden = (TextView) mediaDialog.findViewById(R.id.et_sweden);
        et_sweden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = "sv";
                changeLang(lang);
                mediaDialog.dismiss();
            }
        });
        TextView et_spanish = (TextView) mediaDialog.findViewById(R.id.et_spanish);
        et_spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = "es";
                changeLang(lang);
                mediaDialog.dismiss();
            }
        });

        TextView options_cancel = (TextView) mediaDialog.findViewById(R.id.options_cancel);
        options_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaDialog.dismiss();
            }
        });
        mediaDialog.show();
    }
}