package com.interview.egpaf;

import android.content.DialogInterface;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private SharedViewModel viewModel;
    private TextView userFName, userSurname, userEmail;
    private User loggeduser = null;
    private Helper helper;

    private ViewPagerAdaper viewPagerAdaper;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CreatePatientFragment createPatientFragment;
    private ViewPatientsFragment viewPatientsFragment;
    private ViewUsersFragment viewUsersFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = new Helper(getActivity());
        viewPager = (ViewPager) view.findViewById(R.id.dashViewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.dashTabLayout);

        createPatientFragment = new CreatePatientFragment();
        viewPatientsFragment = new ViewPatientsFragment();
        viewUsersFragment = new ViewUsersFragment();

        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdaper = new ViewPagerAdaper(getActivity().getSupportFragmentManager(),0);
        viewPagerAdaper.addFragment(createPatientFragment,"Create Patient");
        viewPagerAdaper.addFragment(viewPatientsFragment,"View Patients");
        viewPagerAdaper.addFragment(viewUsersFragment, "View Users");


        viewPager.setAdapter(viewPagerAdaper);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_create_patient);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_view_patients);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_view_users);

        userFName = (TextView) view.findViewById(R.id.logged_user_fname);
        userSurname = (TextView) view.findViewById(R.id.logged_user_surname);
        userEmail = (TextView) view.findViewById(R.id.logged_user_email);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        loggeduser = viewModel.getLogged();

        userFName.setText(loggeduser.getFirstname());
        userSurname.setText(loggeduser.getSurname());
        userEmail.setText(loggeduser.getEmail());



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                viewModel.getCurrentID().setValue("newCounter");
            }

            @Override
            public void onPageSelected(int position) {

                viewModel.getCurrentID().setValue("newCounter");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
        materialAlertDialogBuilder.setTitle("Heads Up!");
        materialAlertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getActivity().finish();
            }
        });
        materialAlertDialogBuilder.setNegativeButton("Cancel", null);

        materialAlertDialogBuilder.setMessage("Exit Application?");


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


                materialAlertDialogBuilder.show();

            }
        });
    }

    private class ViewPagerAdaper extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdaper(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

}