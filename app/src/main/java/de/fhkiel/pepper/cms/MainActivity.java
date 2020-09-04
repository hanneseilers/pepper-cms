package de.fhkiel.pepper.cms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GithubAPI github;
    private static final String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            try {
                URL repoURL = new URL("https://github.com/hanneseilers/key-genrator.git");

                this.github = new GithubAPI();
                URL repoUrl = this.github.getRespositoryURL(repoURL);
                ArrayList<GithubRepository> repositories = this.github.getReleases(repoUrl);
                for (GithubRepository repo : repositories){
                    Log.i(TAG, repo.toString());
                }

            } catch (MalformedURLException | GithubAPI.AuthFailedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}