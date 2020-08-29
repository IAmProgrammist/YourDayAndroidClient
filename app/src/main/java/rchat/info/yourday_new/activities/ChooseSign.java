package rchat.info.yourday_new.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.Goroscope;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class ChooseSign extends AppCompatActivity {
    int DIALOG_DATE = 1;
    int year = 2004;
    int month = 1;
    int day = 27;
    String sign;
    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            ChooseSign.this.year = year;
            month = monthOfYear + 1;
            day = dayOfMonth;
            updateSign();
            redrawSign();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_sign);
        sign = SaveSharedPreferences.getSign(this);
        redrawSign();
        ImageView btn = findViewById(R.id.picked_sign);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign == null) {
                    onBackPressed();
                } else {
                    if (sign.equals("")) {
                        SaveSharedPreferences.setSign(ChooseSign.this, "");
                        onBackPressed();
                    } else if (!sign.equals("")) {
                        SaveSharedPreferences.setSign(ChooseSign.this, sign);
                        onBackPressed();
                    }
                }
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, year, month - 1, day);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    private void redrawSign() {
        ImageView sign = findViewById(R.id.picked_sign);
        TextView text = findViewById(R.id.picked_dialog);
        if (this.sign != null) {
            if (this.sign.equals("Сapricorn")) {
                sign.setImageResource(R.drawable.kozerog);
                text.setText("Козерог");
            } else if (this.sign.equals("Aquarius")) {
                sign.setImageResource(R.drawable.vodichka);
                text.setText("Водолей");
            } else if (this.sign.equals("Pisces")) {
                sign.setImageResource(R.drawable.ryby);
                text.setText("Рыбы");
            } else if (this.sign.equals("Aries")) {
                sign.setImageResource(R.drawable.oven);
                text.setText("Овен");
            } else if (this.sign.equals("Taurus")) {
                sign.setImageResource(R.drawable.telec);
                text.setText("Телец");
            } else if (this.sign.equals("Gemini")) {
                sign.setImageResource(R.drawable.bliznecy);
                text.setText("Близнецы");
            } else if (this.sign.equals("Cancer")) {
                sign.setImageResource(R.drawable.rak);
                text.setText("Рак");
            } else if (this.sign.equals("Leo")) {
                sign.setImageResource(R.drawable.lev);
                text.setText("Лев");
            } else if (this.sign.equals("Virgo")) {
                sign.setImageResource(R.drawable.deva);
                text.setText("Дева");
            } else if (this.sign.equals("Libra")) {
                sign.setImageResource(R.drawable.vesy);
                text.setText("Весы");
            } else if (this.sign.equals("Scorpio")) {
                sign.setImageResource(R.drawable.scorpion);
                text.setText("Скорпион");
            } else if (this.sign.equals("Sagittarius")) {
                sign.setImageResource(R.drawable.strelec);
                text.setText("Стрелец");
            } else {
                sign.setImageResource(R.drawable.calendar);
                text.setText("Введите дату Вашего Дня рождения");
            }
        } else {
            sign.setImageResource(R.drawable.calendar);
            text.setText("Введите дату Вашего Дня рождения");
        }
    }

    private void updateSign() {
        if ((month == 1) && (day <= 20) || (month == 12) && (day >= 22)) {
            sign = "Сapricorn";
        } else if ((month == 1) || (month == 2) && (day <= 19)) {
            sign = "Aquarius";
        } else if ((month == 2) || (month == 3) && (day <= 20)) {
            sign = "Pisces";
        } else if ((month == 3) || (month == 4) && (day <= 19)) {
            sign = "Aries";
        } else if ((month == 4) || (month == 5) && (day <= 21)) {
            sign = "Taurus";
        } else if ((month == 5) || (month == 6) && (day <= 21)) {
            sign = "Gemini";
        } else if ((month == 6) || (month == 7) && (day <= 23)) {
            sign = "Cancer";
        } else if ((month == 7) || (month == 8) && (day <= 23)) {
            sign = "Leo";
        } else if ((month == 8) || (month == 9) && (day <= 23)) {
            sign = "Virgo";
        } else if ((month == 9) || (month == 10) && (day <= 23)) {
            sign = "Libra";
        } else if ((month == 10) || (month == 11) && (day <= 22)) {
            sign = "Scorpio";
        } else if (month == 12) {
            sign = "Sagittarius";
        }
    }

    @Override
    public void onBackPressed() {
        Connection connection = Connection.getConnection();
        for (int i = 0; i < connection.props.size(); i++) {
            if (connection.props.get(i) instanceof Goroscope) {
                connection.props.remove(i);
                break;
            }
        }
        if (sign != null) {
            if (sign.equals("")) {
                SaveSharedPreferences.setSign(this, "unchosen");
            } else if (!sign.equals("")) {
                SaveSharedPreferences.setSign(this, sign);
            }
        } else {
            SaveSharedPreferences.setSign(this, "");
        }
        super.onBackPressed();
    }
}