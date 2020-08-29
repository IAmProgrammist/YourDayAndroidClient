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

import java.util.ArrayList;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.Human;

public class BirthdaysAdapter extends BaseAdapter {
    private final SparseBooleanArray mCollapsedStatus;
    Context ctx;
    List<Human> objects = new ArrayList<>();

    public BirthdaysAdapter(Context context, List<Human> presentList) {
        ctx = context;
        objects = presentList;
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return objects.size() + 4;
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
        try {
            int a = objects.size() + 3;
            if (position == 0) {
                DisplayMetrics display = ctx.getResources().getDisplayMetrics();
                int height = display.heightPixels;
                View v = View.inflate(ctx, R.layout.empty, null);
                v.setEnabled(false);
                v.setClickable(false);
                v.setPadding(0, 0, 0, ((int) height / 6));
                return v;
            } else if (position == 1) {
                View va = View.inflate(ctx, R.layout.birthday_heading, null);
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
                final BirthdayViewHolder viewHolder;
                boolean isViewNull = convertView == null;
                boolean isTagNull = false;
                try {
                    isTagNull = convertView.getTag() == null;
                } catch (NullPointerException e) {
                    isTagNull = false;
                }
                if (isViewNull || isTagNull) {
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.birthday_row, parent, false);
                    viewHolder = new BirthdayViewHolder();
                    viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                    viewHolder.name = convertView.findViewById(R.id.name);
                    viewHolder.imageView = convertView.findViewById(R.id.img);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (BirthdayViewHolder) convertView.getTag();
                }
                Picasso.get().load(objects.get(position - 2).getImageUrl()).into(viewHolder.imageView);
                viewHolder.name.setText(objects.get(position - 2).getName());
                viewHolder.expandableTextView.setText(objects.get(position - 2).getDescription(), mCollapsedStatus, position - 2);
            }
            return convertView;
        } catch (IndexOutOfBoundsException e) {
            return new View(ctx);
        }
    }

    private class BirthdayViewHolder {
        ImageView imageView;
        TextView name;
        ExpandableTextView expandableTextView;
    }
}
