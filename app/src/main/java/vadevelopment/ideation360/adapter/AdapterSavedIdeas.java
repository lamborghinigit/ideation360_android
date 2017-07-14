package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.AllIdeas_Skeleton;
import vadevelopment.ideation360.Skeleton.SavedIdeas_Skeleton;
import vadevelopment.ideation360.fragments.AddIdeaFragment;

/**
 * Created by vibrantappz on 6/20/2017.
 */

public class AdapterSavedIdeas extends RecyclerView.Adapter<AdapterSavedIdeas.ViewHolder> {

    private Context context;
    private ArrayList<SavedIdeas_Skeleton> arraylist;
    String text;
    private FragmentManager fm;
    private HomeActivity homeActivity;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public AdapterSavedIdeas(Context context, ArrayList<SavedIdeas_Skeleton> arraylist, FragmentManager fm, HomeActivity homeActivity) {
        this.context = context;
        this.text = text;
        this.arraylist = arraylist;
        this.fm = fm;
        this.homeActivity = homeActivity;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterSavedIdeas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowshome, parent, false);
        AdapterSavedIdeas.ViewHolder viewHolder = new AdapterSavedIdeas.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterSavedIdeas.ViewHolder viewHolder, int position) {

        // if (text.equalsIgnoreCase("profile")) {
        final SavedIdeas_Skeleton data = arraylist.get(position);
        viewHolder.date.setVisibility(View.INVISIBLE);
        viewHolder.maintitle.setText(arraylist.get(position).getIdea_name());
        viewHolder.compaign.setText(arraylist.get(position).getCampaign());
        viewHolder.noof_rating.setText("0");
        viewHolder.noof_comment.setText("0");
        viewHolder.ratingBar.setRating(Float.parseFloat("0"));
        //viewHolder.date.setText(arraylist.get(position).getAudio_path());
        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
        GlideUrl glideUrl = new GlideUrl("https://app.ideation360.com/api/getprofileimage/" + preferences.getString("ideatorid", ""), builder.build());
        Glide.with(context).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.image);
        viewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIdeaFragment addidea_frg = new AddIdeaFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("saved_idea", data);
                bundle.putString("from", "savedidea");
                //  bundle.putString("fromsaved_idea", "yup");
                addidea_frg.setArguments(bundle);
                homeActivity.replaceFragmentHome(addidea_frg);
            }
        });

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, maintitle, compaign, noof_rating, noof_comment;
        public RatingBar ratingBar;
        public LinearLayout ll_main;
        ImageView image;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            maintitle = (TextView) itemLayoutView.findViewById(R.id.maintitle);
            compaign = (TextView) itemLayoutView.findViewById(R.id.compaign);
            ratingBar = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar);
            noof_rating = (TextView) itemLayoutView.findViewById(R.id.noof_rating);
            noof_comment = (TextView) itemLayoutView.findViewById(R.id.noof_comment);
            ll_main = (LinearLayout) itemLayoutView.findViewById(R.id.ll_main);
            image = (ImageView) itemLayoutView.findViewById(R.id.image);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}

