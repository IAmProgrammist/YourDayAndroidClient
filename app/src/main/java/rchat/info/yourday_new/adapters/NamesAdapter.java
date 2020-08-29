package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.Names;

public class NamesAdapter extends BaseAdapter {

    Names names;
    Context ctx;

    public NamesAdapter(Context ctx, Names names) {
        this.names = names;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (position == 0) {
            DisplayMetrics display = ctx.getResources().getDisplayMetrics();
            int height = display.heightPixels;
            View v = View.inflate(ctx, R.layout.empty, null);
            v.setEnabled(false);
            v.setClickable(false);
            v.setPadding(0, 0, 0, ((int) height / 6));
            return v;
        } else if (position == 1) {
            View va = View.inflate(ctx, R.layout.names_header, null);
            va.setEnabled(false);
            va.setClickable(false);
            return va;
        } else if (position == 3) {
            View vs = View.inflate(ctx, R.layout.empty, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            vs.setPadding(0, 0, 0, 100);
            return vs;
        } else {
            view = View.inflate(ctx, R.layout.names_end, null);
            String boys = "";
            for (String a : names.boys) {
                boys += "• " + a + ";" + "\n";
            }
            try {
                boys = boys.substring(0, boys.length() - 2);
            } catch (Exception e) {

            }
            String girls = "";
            for (String a : names.girls) {
                girls += "• " + a + ";" + "\n";
            }
            try {
                girls = girls.substring(0, girls.length() - 2);
            } catch (Exception e) {

            }
            if (boys.equals("")) {
                view.findViewById(R.id.presentName).setVisibility(View.GONE);
                view.findViewById(R.id.boys).setVisibility(View.GONE);
                view.findViewById(R.id.imageView10).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.girls)).setText(girls);
            } else if (girls.equals("")) {
                view.findViewById(R.id.presentName2).setVisibility(View.GONE);
                view.findViewById(R.id.girls).setVisibility(View.GONE);
                view.findViewById(R.id.imageView10).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.boys)).setText(boys);
            } else {
                ((TextView) view.findViewById(R.id.girls)).setText(girls);
                ((TextView) view.findViewById(R.id.boys)).setText(boys);
            }
            return view;
        }
    }
}
