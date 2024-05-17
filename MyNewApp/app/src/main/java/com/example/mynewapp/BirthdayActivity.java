package com.example.mynewapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BirthdayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        EditText editTextDate = findViewById(R.id.editTextDate);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        editTextDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String yyyymmdd = "YYYYMMDD";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }

                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + yyyymmdd.substring(clean.length());
                    } else {
                        int day = Integer.parseInt(clean.substring(6, 8));
                        int mon = Integer.parseInt(clean.substring(4, 6));
                        int year = Integer.parseInt(clean.substring(0, 4));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%04d%02d%02d", year, mon, day);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 4),
                            clean.substring(4, 6),
                            clean.substring(6, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editTextDate.setText(current);
                    editTextDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonSubmit.setOnClickListener(v -> {
            String input = editTextDate.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                Date date = sdf.parse(input);
                if (date.after(new Date())) {
                    Toast.makeText(this, "미래 날짜를 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 생일이 올바르게 입력된 경우 저장하고 메인 화면으로 이동
                    BirthdayUtils.setBirthday(BirthdayActivity.this, input);
                    Intent intent = new Intent(BirthdayActivity.this, MainActivity.class);
                    intent.putExtra("BIRTHDAY", input);
                    startActivity(intent);
                    finish();
                }
            } catch (ParseException e) {
                Toast.makeText(this, "잘못된 날짜 형식입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
