package dp.ibps.generalawareness.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dp.ibps.generalawareness.Activity.HomeActivity;
import dp.ibps.generalawareness.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentManager fragmentManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
                return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        BottomNavigationView bottom_navigation = view.findViewById(R.id.bottom_navigation);
        fragmentManager = getActivity().getSupportFragmentManager();
        //get fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //set new fragment in fragment_container (FrameLayout)
        fragmentTransaction.replace(R.id.frameLayout2, new MainFragment());
        fragmentTransaction.commit();

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                openFragment(MainFragment.newInstance("", ""));
                                return true;
//                            case R.id.navigation_audio:
//                            openFragment(AudioFragment.newInstance("", ""));
//                            return true;
                            case R.id.navigation_mocktest:
                                openFragment(MockTestFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_weekly_Quiz:
                                openFragment(WeeklyQuizFragment.newInstance("", ""));
                                return true;
                            case R.id.navigation_game:
                                openFragment(GamesFragment.newInstance("", ""));
                                return true;
                        }
                        return false;
                    }
                };

        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        initialise(view);
        return view;

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout2, fragment);
        transaction.commit();
    }

    private void initialise(View view) {

    }

}