package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.historical.HistDay;
import rchat.info.yourday_new.containers.historical.HistEvent;

public class HistoryAdapter extends BaseAdapter {

    private final SparseBooleanArray mCollapsedStatus;
    HistDay histDay;
    Context ctx;

    public HistoryAdapter(Context ctx, HistDay histDay) {
        this.histDay = histDay;
        this.ctx = ctx;
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return histDay.histEvents.size() + 4;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        int a = histDay.histEvents.size() + 3;
        if (position == 0) {
            DisplayMetrics display = ctx.getResources().getDisplayMetrics();
            int height = display.heightPixels;
            View v = View.inflate(ctx, R.layout.empty, null);
            v.setEnabled(false);
            v.setClickable(false);
            v.setPadding(0, 0, 0, ((int) height / 6));
            return v;
        } else if (position == 1) {
            View va = View.inflate(ctx, R.layout.hist_heading, null);
            va.setEnabled(false);
            va.setClickable(false);
            return va;
        } else if (position == a) {
            View vs = View.inflate(ctx, R.layout.empty, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            vs.setPadding(0, 0, 0, 100);
            return vs;
        } else if (position == a - 1) {
            View vs = View.inflate(ctx, R.layout.recipes_end, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            return vs;
        } else {
            final HistoryViewHolder viewHolder;
            boolean isViewNull = convertView == null;
            boolean isTagNull = false;
            try {
                isTagNull = convertView.getTag() == null;
            } catch (NullPointerException e) {
                isTagNull = false;
            }
            if (isViewNull || isTagNull) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.histrow, parent, false);
                viewHolder = new HistoryViewHolder();
                viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                viewHolder.date = convertView.findViewById(R.id.date);
                viewHolder.imageView = convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (HistoryViewHolder) convertView.getTag();
            }
            HistEvent histEvent = histDay.histEvents.get(position - 2);
            String dsc = "";
            for (String aa : histEvent.desc) {
                dsc += aa + "\n" + "\n";
            }
            dsc = dsc.substring(0, dsc.length() - 2);
            viewHolder.expandableTextView.setText(dsc, mCollapsedStatus, position - 2);
            viewHolder.date.setText(histEvent.date);
            Picasso.get().load(histEvent.urlImage).into(viewHolder.imageView);
            return convertView;
        }
    }

    private static class HistoryViewHolder {
        ExpandableTextView expandableTextView;
        TextView date;
        ImageView imageView;
    }
}
