package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import vadevelopment.ideation360.HomeActivity;
import vadevelopment.ideation360.R;
import vadevelopment.ideation360.Skeleton.Comments_Skeleton;
import vadevelopment.ideation360.Skeleton.People_Skeleton;
import vadevelopment.ideation360.fragments.MyProfileFragment;

/**
 * Created by vibrantappz on 6/23/2017.
 */

public class AdapterPeople extends RecyclerView.Adapter<AdapterPeople.ViewHolder> {

    private Context context;
    private ArrayList<People_Skeleton> arraylist;
    private FragmentManager fm;
    private HomeActivity homecontainor;

    public AdapterPeople(Context context, ArrayList<People_Skeleton> arraylist, FragmentManager fm) {
        this.context = context;
        this.arraylist = arraylist;
        this.fm = fm;
        homecontainor = ((HomeActivity) context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterPeople.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowpeople, parent, false);
        AdapterPeople.ViewHolder viewHolder = new AdapterPeople.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AdapterPeople.ViewHolder viewHolder, final int position) {

        viewHolder.name.setText(arraylist.get(position).getName());
        LazyHeaders.Builder builder = new LazyHeaders.Builder()
                .addHeader("Authorization", "Basic c2FBcHA6dWpyTE9tNGVy");
        GlideUrl glideUrl = new GlideUrl(arraylist.get(position).getImage(), builder.build());
        Glide.with(context).load(glideUrl).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.image);
        viewHolder.rl_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProfileFragment myprofile_frg = new MyProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ideatorid", arraylist.get(position).getIdeatorid());
                bundle.putString("name", arraylist.get(position).getName().split("\\s+")[0]);
                myprofile_frg.setArguments(bundle);
                homecontainor.replaceFragmentHome(myprofile_frg);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        private ImageView image;
        public RelativeLayout rl_outer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            image = (ImageView) itemLayoutView.findViewById(R.id.image);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            rl_outer = (RelativeLayout) itemLayoutView.findViewById(R.id.rl_outer);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
}


