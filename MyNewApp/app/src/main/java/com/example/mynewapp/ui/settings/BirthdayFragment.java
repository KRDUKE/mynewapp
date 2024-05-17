package com.example.mynewapp.ui.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mynewapp.BirthdayUtils;
import com.example.mynewapp.R;
import com.example.mynewapp.databinding.FragmentBirthdayBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BirthdayFragment extends Fragment {

    private FragmentBirthdayBinding binding;
    private static final String DATE_FORMAT = "yyyyMMdd";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBirthdayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        EditText birthdayEditText = binding.editTextDate;
        birthdayEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String yyyymmdd = "YYYYMMDD";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    if (clean.length() < 8) {
                        clean = clean + yyyymmdd.substring(clean.length());
                    } else {
                        int year = Integer.parseInt(clean.substring(0, 4));
                        int month = Integer.parseInt(clean.substring(4, 6));
                        int day = Integer.parseInt(clean.substring(6, 8));

                        if (month > 12) month = 12;
                        cal.set(Calendar.MONTH, month - 1);
                        cal.set(Calendar.YEAR, year);
                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        cal.set(Calendar.DAY_OF_MONTH, day);

                        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
                        clean = sdf.format(cal.getTime());
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 4), clean.substring(4, 6), clean.substring(6, 8));
                    current = clean;
                    birthdayEditText.setText(current);
                    birthdayEditText.setSelection(current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        Button submitBirthdayButton = binding.buttonSubmit;
        submitBirthdayButton.setOnClickListener(v -> {
            String birthday = birthdayEditText.getText().toString().replaceAll("-", "");
            if (isValidBirthday(birthday)) {
                BirthdayUtils.setBirthday(getActivity(), birthday);
                Toast.makeText(getActivity(), "생일이 저장되었습니다", Toast.LENGTH_SHORT).show();

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                Bundle bundle = new Bundle();
                bundle.putString("BIRTHDAY", birthday);
                navController.navigate(R.id.action_birthdayFragment_to_dashboardFragment, bundle);
                navController.popBackStack(R.id.navigation_settings, true);
            } else {
                Toast.makeText(getActivity(), "올바른 생일을 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private boolean isValidBirthday(String birthday) {
        if (birthday.length() != 8) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(birthday);
            return !date.after(new Date()); // 미래 날짜를 방지
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.popBackStack(); // 이전 프래그먼트로 돌아감
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
