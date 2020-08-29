package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.day.Description;
import rchat.info.yourday_new.containers.day.Present;

public class PresentDescriptionAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<Description> modelList;
    ArrayList<Description> arrayList;

    public PresentDescriptionAdapter(Context mContext, Present p) {
        this.mContext = mContext;
        modelList = new ArrayList<>();
        modelList.addAll(p.description);
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(modelList);
    }

    public PresentDescriptionAdapter(Context mContext, List<Description> p) {
        this.mContext = mContext;
        modelList = new ArrayList<>();
        modelList = p;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(p);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        switch (modelList.get(position).type) {
            case BOLD_HEADING:
                view = inflater.inflate(R.layout.bold_heading, null);
                ((TextView) view.findViewById(R.id.textView1)).setText(modelList.get(position).desc);
                view.setClickable(false);
                view.setEnabled(false);
                return view;
            case LIST_ITEM:
                view = inflater.inflate(R.layout.list_item, null);
                ((TextView) view.findViewById(R.id.textView1)).setText(modelList.get(position).desc);
                view.setClickable(false);
                view.setEnabled(false);
                return view;
            case USUAL_TEXT:
                view = inflater.inflate(R.layout.usual_text, null);
                ((TextView) view.findViewById(R.id.textView1)).setText(modelList.get(position).desc);
                view.setClickable(false);
                view.setEnabled(false);
                return view;
            default:
                return null;
        }
    }
}