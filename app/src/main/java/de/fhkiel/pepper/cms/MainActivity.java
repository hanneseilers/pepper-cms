package de.fhkiel.pepper.cms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.fhkiel.pepper.cms.repository.GithubAPI;
import de.fhkiel.pepper.cms.repository.GithubRelease;

public class MainActivity extends AppCompatActivity {

    private GithubAPI github;
    private static final String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            try {
                // TODO: getting different github repos
                URL repoURL = new URL("https://github.com/hanneseilers/key-genrator.git");

                this.github = new GithubAPI();
                URL repoUrl = this.github.getRespositoryURL(repoURL);
                ArrayList<GithubRelease> releases = this.github.getReleases(repoUrl);

                for (GithubRelease release : releases){

                    Log.i(TAG, release.toString());
                }

            } catch (MalformedURLException | GithubAPI.AuthFailedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}