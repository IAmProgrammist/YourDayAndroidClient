package rchat.info.yourday_new.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.Joke;

public class JokeAdapter extends BaseAdapter {
    Context ctx;
    Joke joke;

    public JokeAdapter(Context ctx, Joke joke) {
        this.ctx = ctx;
        this.joke = joke;
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
            View va = View.inflate(ctx, R.layout.joke_header, null);
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
            view = View.inflate(ctx, R.layout.jokerow, null);
            ((TextView) view.findViewById(R.id.joke)).setText(joke.joke);
            final String shareText = joke.joke + "\n" + "Источник: https://randstuff.ru/joke/" + "\n" + "Скачать Ежедневник: https://play.google.com/store/apps/details?id=rchat.info.yourday_new";
            view.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    ctx.startActivity(sendIntent);
                }
            });
            return view;

        }
    }
}
