package vadevelopment.ideation360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vadevelopment.ideation360.R;

/**
 * Created by vibrantappz on 6/21/2017.
 */

public class Adpater_AddIdeaSecond extends BaseAdapter {

    public LayoutInflater inflater = null;
    ArrayList<String> ideationname_hashset;
    Context context;

    public Adpater_AddIdeaSecond(Context c, ArrayList<String> ideationname_hashset) {
        context = c;
        inflater = LayoutInflater.from(c);
        this.ideationname_hashset = ideationname_hashset;
    }

    @Override
    public int getCount() {
        return ideationname_hashset.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Adpater_AddIdeaSecond.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new Adpater_AddIdeaSecond.ViewHolder();
            convertView = inflater.inflate(R.layout.row_addidea, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Adpater_AddIdeaSecond.ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(ideationname_hashset.get(i));
        return convertView;
    }

    public class ViewHolder {
        TextView title;
    }
}

