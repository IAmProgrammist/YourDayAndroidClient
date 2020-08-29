package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.day.Description;
import rchat.info.yourday_new.containers.day.Present;
import rchat.info.yourday_new.containers.day.Type;

public class PresentAdapter extends BaseAdapter {
    Context ctx;
    List<Object> objects = new ArrayList<>();
    List<Boolean> opened = new ArrayList<>();

    public PresentAdapter(Context context, List<Present> presentList) {
        opened.add(false);
        opened.add(false);
        ctx = context;
        List<String> stringList = new ArrayList<>();
        for (Present p : presentList) {
            if (p.hasDesc()) {
                objects.add(p);
                opened.add(false);
            } else {
                stringList.add(p.name);
            }
        }
        objects.add(stringList);
        opened.add(false);
    }

    @Override
    public int getCount() {
        return objects.size() + 3;
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
    public View getView(final int position, View view, ViewGroup parent) {
        try {
            int a = objects.size() + 2;
            if (position == 0) {
                DisplayMetrics display = ctx.getResources().getDisplayMetrics();
                int height = display.heightPixels;
                View v = View.inflate(ctx, R.layout.empty, null);
                v.setEnabled(false);
                v.setClickable(false);
                v.setPadding(0, 0, 0, ((int) height / 6));
                return v;
            } else if (position == 1) {
                View va = View.inflate(ctx, R.layout.present_header, null);
                va.setEnabled(false);
                va.setClickable(false);
                return va;
            } else if (position == a) {
                View vs = View.inflate(ctx, R.layout.empty, null);
                vs.setEnabled(false);
                vs.setClickable(false);
                vs.setPadding(0, 0, 0, 100);
                return vs;
            } else {
                if (objects.get(position - 2) instanceof Present) {
                    Present p = (Present) objects.get(position - 2);
                    view = View.inflate(ctx, R.layout.present_fullitem, null);
                    ((TextView) view.findViewById(R.id.presentName)).setText(p.name);
                    final View finalView = view;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final LinearLayout textView = finalView.findViewById(R.id.desc);
                            final ImageButton btn = finalView.findViewById(R.id.present_extend);
                            if (opened.get(position - 2)) {
                                Animation close = AnimationUtils.loadAnimation(ctx, R.anim.close_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(close);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.down_arrow);
                                textView.setVisibility(View.GONE);
                                opened.set(position - 2, false);
                            } else {
                                Animation open = AnimationUtils.loadAnimation(ctx, R.anim.open_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(open);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.up_arrow);
                                textView.setVisibility(View.VISIBLE);
                                opened.set(position - 2, true);
                            }
                        }
                    });
                    final ImageButton btn = finalView.findViewById(R.id.present_extend);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final LinearLayout textView = finalView.findViewById(R.id.desc);
                            final ImageButton btn = finalView.findViewById(R.id.present_extend);
                            if (opened.get(position - 2)) {
                                Animation close = AnimationUtils.loadAnimation(ctx, R.anim.close_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(close);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.down_arrow);
                                textView.setVisibility(View.GONE);
                                opened.set(position - 2, false);
                            } else {
                                Animation open = AnimationUtils.loadAnimation(ctx, R.anim.open_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(open);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.up_arrow);
                                textView.setVisibility(View.VISIBLE);
                                opened.set(position - 2, true);
                            }
                        }
                    });
                    if (opened.get(position - 2)) {
                        btn.setImageResource(R.drawable.up_arrow);
                        finalView.findViewById(R.id.desc).setVisibility(View.VISIBLE);
                    } else {
                        btn.setImageResource(R.drawable.down_arrow);
                        finalView.findViewById(R.id.desc).setVisibility(View.GONE);
                    }
                    List<View> views = new ArrayList<>();
                    for (Description description : p.description) {
                        View vvv;
                        if (description.type == Type.BOLD_HEADING) {
                            vvv = View.inflate(ctx, R.layout.bold_heading, null);
                            ((TextView) vvv.findViewById(R.id.textView1)).setText(description.desc);
                        } else if (description.type == Type.USUAL_TEXT) {
                            vvv = View.inflate(ctx, R.layout.usual_text, null);
                            ((TextView) vvv.findViewById(R.id.textView1)).setText(description.desc);
                        } else {
                            vvv = View.inflate(ctx, R.layout.list_item, null);
                            ((TextView) vvv.findViewById(R.id.textView1)).setText(description.desc);
                        }
                        views.add(vvv);
                    }
                    LinearLayout layout = finalView.findViewById(R.id.desc);
                    for (View tv : views) {
                        if (tv.getParent() != null) {
                            ((ViewGroup) tv.getParent()).removeView(tv);
                        }
                        layout.addView(tv);
                    }
                } else if (objects.get(position - 2) instanceof List) {
                    List<String> list = (List<String>) objects.get(position - 2);
                    view = View.inflate(ctx, R.layout.present_end, null);
                    String desc = "";
                    for (String h : list) {
                        desc += "• " + h + ";" + "\n";
                    }
                    desc = desc.substring(0, desc.length() - 2);
                    ((TextView) view.findViewById(R.id.desc)).setText(desc);
                    final View finalView = view;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final TextView textView = finalView.findViewById(R.id.desc);
                            final ImageButton btn = finalView.findViewById(R.id.present_extend);
                            if (opened.get(position - 2)) {
                                Animation close = AnimationUtils.loadAnimation(ctx, R.anim.close_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(close);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.down_arrow);
                                textView.setVisibility(View.GONE);
                                opened.set(position - 2, false);
                            } else {
                                Animation open = AnimationUtils.loadAnimation(ctx, R.anim.open_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(open);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.up_arrow);
                                textView.setVisibility(View.VISIBLE);
                                opened.set(position - 2, true);
                            }
                        }
                    });
                    final ImageButton btn = finalView.findViewById(R.id.present_extend);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final TextView textView = finalView.findViewById(R.id.desc);
                            final ImageButton btn = finalView.findViewById(R.id.present_extend);
                            if (opened.get(position - 2)) {
                                Animation close = AnimationUtils.loadAnimation(ctx, R.anim.close_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(close);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.down_arrow);
                                textView.setVisibility(View.GONE);
                                opened.set(position - 2, false);
                            } else {
                                Animation open = AnimationUtils.loadAnimation(ctx, R.anim.open_anim);
                                Animation barrelRoll = AnimationUtils.loadAnimation(ctx, R.anim.do_a_barrel_roll);
                                textView.setAnimation(open);
                                btn.setAnimation(barrelRoll);
                                btn.setImageResource(R.drawable.up_arrow);
                                textView.setVisibility(View.VISIBLE);
                                opened.set(position - 2, true);
                            }
                        }
                    });
                    if (opened.get(position - 2)) {
                        btn.setImageResource(R.drawable.up_arrow);
                        finalView.findViewById(R.id.desc).setVisibility(View.VISIBLE);
                    } else {
                        btn.setImageResource(R.drawable.down_arrow);
                        finalView.findViewById(R.id.desc).setVisibility(View.GONE);
                    }
                }
            }
            return view;
        } catch (IndexOutOfBoundsException e) {
            return new View(ctx);
        }
    }
}
