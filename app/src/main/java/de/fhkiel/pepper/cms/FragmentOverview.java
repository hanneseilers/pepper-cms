package de.fhkiel.pepper.cms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import de.fhkiel.pepper.cms_lib.apps.PepperApp;
import de.fhkiel.pepper.cms_lib.apps.PepperAppInterface;
import de.fhkiel.pepper.cms_lib.apps.PepperCMSControllerInterface;
import de.fhkiel.pepper.cms_lib.repository.PepperCMSRepository;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentOverview extends Fragment implements PepperAppInterface {

    private static final String TAG = FragmentOverview.class.getName();

    private MainActivity activity;
    private PepperCMSControllerInterface pepperCMS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get activity and cms
        activity = MainActivity.getINSTANCE();
        pepperCMS = activity.getPepperCMS();

        // set button actions
        view.findViewById(R.id.btnSettings).setOnClickListener(v -> {
            // TODO
        });

        // register to cms listener
        pepperCMS.addPepperAppInterfaceListener(this);
    }

    // TODO: callbacks

    @Override
    public void onPepperAppsLoaded(HashMap<String, PepperApp> apps, boolean isRemote) {

    }

    @Override
    public void onAppStarted(PepperApp app) { }

    @Override
    public void onAppUpdateAvailable(PepperApp app) {

    }

    @Override
    public void onAppUpdate(PepperApp app) {}

    @Override
    public void onAppUpdated(PepperApp app) {}

    @Override
    public void onRepositoryError(PepperCMSRepository repository, Exception e) {

    }

    @Override
    public void onPendingTasksChanged(ArrayList<Thread> threads, Thread newThread) {
    }
}