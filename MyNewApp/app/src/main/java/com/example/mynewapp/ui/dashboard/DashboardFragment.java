package com.example.mynewapp.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mynewapp.BirthdayUtils;
import com.example.mynewapp.ProfileUtils;
import com.example.mynewapp.R;
import com.example.mynewapp.databinding.FragmentDashboardBinding;

import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private static final int PICK_IMAGE = 1;
    private ImageView profileImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileImageView = binding.profileImage;
        TextView textBirthday = binding.textBirthday;

        // 생일 정보 받기
        Bundle args = getArguments();
        if (args != null && args.containsKey("BIRTHDAY")) {
            String birthday = args.getString("BIRTHDAY");
            textBirthday.setText("Birthday: " + birthday);
        } else {
            String savedBirthday = BirthdayUtils.getBirthday(getActivity());
            if (savedBirthday != null) {
                textBirthday.setText("Birthday: " + savedBirthday);
            } else {
                textBirthday.setText("Birthday: not set");
            }
        }

        // 프로필 이미지 로드
        String profileImageUri = ProfileUtils.getProfileImageUri(requireContext());
        if (profileImageUri != null) {
            profileImageView.setImageURI(Uri.parse(profileImageUri));
        }

        profileImageView.setOnClickListener(v -> {
            // 프로필 이미지 변경
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profileImageView.setImageBitmap(bitmap);

                // 프로필 이미지 URI 저장
                ProfileUtils.setProfileImageUri(requireContext(), imageUri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
