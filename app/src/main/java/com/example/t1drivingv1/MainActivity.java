package com.example.t1drivingv1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.t1drivingv1.databinding.ActivityMainBinding;

import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    Button btn_getBG;
    ListView lv_currentBG;

    private ActivityMainBinding binding;

    private String clientID = "tMzERvJ2R2F9qsrVZNcw1pc7Pqkz0GWl";
    private String clientSecret = "V74ZjB7pCYU4PxI8";
    private String redirectUri = "https://t1drivesafe.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // assign values to each control on the layout
        btn_getBG=findViewById(R.id.btn_getBG);
        lv_currentBG=findViewById(R.id.lv_currentBG);


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sandbox-api.dexcom.com/v2/oauth2/login?client_id=" + clientID + "&redirect_uri=" + redirectUri + "&response_type=code&scope=offline_access"));
                startActivity(intent);


    }
    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith(redirectUri)) {
            String code = uri.getQueryParameter("code");
            Toast.makeText(this, "yay", Toast.LENGTH_SHORT).show();
        }

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "client_secret=" + clientSecret + "&client_id=" + clientID + "&code=" + uri.getQueryParameter("code") + "&grant_type=authorization_code&redirect_uri=" + redirectUri);
        Request request = new Request.Builder()
                .url("https://api.dexcom.com/v2/oauth2/token")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://api.dexcom.com/v2/users/self/egvs?startDate=2017-06-16T15:30:00&endDate=2017-06-16T15:45:00")
            .get()
            .addHeader("authorization", "Bearer {your_access_token}")
            .build();

    Response response;

    {
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}