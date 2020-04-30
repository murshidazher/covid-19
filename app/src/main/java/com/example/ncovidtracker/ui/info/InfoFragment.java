package com.example.ncovidtracker.ui.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ncovidtracker.LoginActivity;
import com.example.ncovidtracker.MainActivity;
import com.example.ncovidtracker.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.StringUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private FirebaseAuth mAuth;

    FirebaseUser user;
    TextView userName, userEmail;
    ImageView userProfile;

    Button signOutButton;

    ProgressBar progressBar;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        mAuth = FirebaseAuth.getInstance();


        user = mAuth.getCurrentUser();

        userName = root.findViewById(R.id.tv_info_name);
        userEmail = root.findViewById(R.id.tv_info_email);
        userProfile = root.findViewById(R.id.img_info_profile);

        signOutButton = root.findViewById(R.id.signOutButton);

        progressBar = root.findViewById(R.id.progress_circular_info);

        progressBar.setVisibility(View.VISIBLE);

        if(user != null){
            userName.setText(StringUtils.capitalize(user.getDisplayName()));
            Uri photoUrl = user.getPhotoUrl();
            userEmail.setText(StringUtils.capitalize(user.getEmail()));

            Glide.with(this)
                    .load(String.valueOf(photoUrl))
                    .apply(new RequestOptions().override(150, 150))
                    .into(userProfile);

            progressBar.setVisibility(View.GONE);
        }



        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();

            }
        });

        return root;
    }


    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        Intent loginActivity = new Intent(getContext(), LoginActivity.class);
        startActivity(loginActivity);
    }


}
