package com.interview.egpaf;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import androidx.viewpager.widget.ViewPager;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowPatientDetails extends AppCompatActivity {

    private SharedViewModel viewModel;

    private DatabaseHelper databaseHelper;
    private TextView patientDetailsID;
    private Helper helper;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdaper viewPagerAdaper;

    private PatientDetailsFragment patientDetailsFragment;
    private PatientLogsFragment patientLogsFragment;
    private AddRecordFragment addRecordFragment;
    private ViewRecordsFragment viewRecordsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_patient_details);
        Toolbar toolbar = findViewById(R.id.pDetailsToolBar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        int patientID = intent.getIntExtra("patientID", -1);

        patientDetailsID = (TextView) findViewById(R.id.patientDetailsID);
        patientDetailsID.setText(String.valueOf(patientID));

        databaseHelper = new DatabaseHelper(this);

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        viewModel.setPatient(databaseHelper.getPatient(patientID));

        helper = new Helper(this);

        viewPager = (ViewPager) findViewById(R.id.pDetailsViewPager);
        tabLayout = (TabLayout) findViewById(R.id.pDetailsTabLayout);

        patientDetailsFragment = new PatientDetailsFragment();
        addRecordFragment = new AddRecordFragment();
        viewRecordsFragment = new ViewRecordsFragment();
        patientLogsFragment = new PatientLogsFragment();

        tabLayout.setupWithViewPager(viewPager);
        viewPagerAdaper = new ViewPagerAdaper(this.getSupportFragmentManager(),0);
        viewPagerAdaper.addFragment(addRecordFragment,"Add Record");
        viewPagerAdaper.addFragment(viewRecordsFragment,"View Records");

        viewPager.setAdapter(viewPagerAdaper);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_add_record);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_view_records);

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