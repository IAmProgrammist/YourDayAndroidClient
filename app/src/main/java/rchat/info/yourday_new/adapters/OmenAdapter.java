package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.Omen;

public class OmenAdapter extends BaseAdapter {

    Omen omen;
    Context ctx;

    public OmenAdapter(Context ctx, Omen omen) {
        this.omen = omen;
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
            View va = View.inflate(ctx, R.layout.prediction_header, null);
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
            view = View.inflate(ctx, R.layout.predictionrow, null);
            String pr = "";
            for (String a : omen.omens) {
                pr += "• " + a + ";" + "\n";
            }
            pr = pr.substring(0, pr.length() - 2);
            ((TextView) view.findViewById(R.id.container)).setText(pr);
            return view;
        }
    }
}
