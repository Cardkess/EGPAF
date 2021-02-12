package com.interview.egpaf;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecordFragment extends Fragment {

    private SharedViewModel viewModel;

    private TextView textViewAddRecordWeight, textViewAddRecordHeight, textViewAddRecordTemp;
    private Spinner spinnerAddRecordDiagnosis;
    private Button btnAddRecord;
    private Helper helper;
    private ProgressBar mProgressBar;
    private Snackbar snackbar;
    private int patient_id;
    private DatabaseHelper databaseHelper;
    private static int TIME_OUT = 1000;
    private String currentDate;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecordFragment newInstance(String param1, String param2) {
        AddRecordFragment fragment = new AddRecordFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_record, container, false);
        setSpinnerContent(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper = new Helper(getActivity());
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        patient_id = viewModel.getPatient().getId();

        databaseHelper = new DatabaseHelper(getContext());

        snackbar = Snackbar.make(view, "Record added successfully!", Snackbar.LENGTH_SHORT);

        textViewAddRecordWeight = (TextView) view.findViewById(R.id.addRecordWeight);
        textViewAddRecordHeight = (TextView) view.findViewById(R.id.addRecordHeight);
        textViewAddRecordTemp = (TextView) view.findViewById(R.id.addRecordTemp);
        spinnerAddRecordDiagnosis = (Spinner) view.findViewById(R.id.diagnosisSpinner);
        btnAddRecord = (Button) view.findViewById(R.id.btnAddRecord);
        mProgressBar = (ProgressBar) view.findViewById(R.id.addRecordProgressBar);


        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddRecord();
            }
        });


    }

    public void AddRecord(){

        String rWeight = textViewAddRecordWeight.getText().toString().trim();
        String rHeight = textViewAddRecordHeight.getText().toString().trim();
        String rTempReading = textViewAddRecordTemp.getText().toString().trim();
        String rDiagnosis = spinnerAddRecordDiagnosis.getSelectedItem().toString().trim();



        if (TextUtils.isEmpty(rWeight)){

            helper.MakeToast("Enter Weight!");

        } else if(TextUtils.isEmpty(rHeight)){

            helper.MakeToast("Enter Height!");

        } else if(TextUtils.isEmpty(rTempReading)){

            helper.MakeToast("Select Temp Reading!");

        } else if(spinnerAddRecordDiagnosis.getSelectedItemPosition() == 0){

            helper.MakeToast("Select Diagnosis!");

        }else {

            toggleFields(false);
            mProgressBar.setVisibility(View.VISIBLE);

            Record record = new Record(0,rWeight,rHeight,rTempReading,rDiagnosis,patient_id,currentDate,currentDate,currentDate);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    if(databaseHelper.addRecord(record)){

                        textViewAddRecordWeight.setText("");
                        textViewAddRecordHeight.setText("");
                        textViewAddRecordTemp.setText("");
                        spinnerAddRecordDiagnosis.setSelection(0);
                        toggleFields(true);
                        snackbar.show();

                    } else {

                        helper.MakeToast("Operation failed!");
                        toggleFields(true);
                    }


                }

            }, TIME_OUT);


        }

    }

    private void setSpinnerContent(View view) {

        Spinner spinner = (Spinner) view.findViewById(R.id.diagnosisSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.diagnostics_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void toggleFields(boolean b) {

        if (b){

            textViewAddRecordWeight.setEnabled(true);
            textViewAddRecordHeight.setEnabled(true);
            textViewAddRecordTemp.setEnabled(true);
            spinnerAddRecordDiagnosis.setEnabled(true);
            mProgressBar.setVisibility(View.INVISIBLE);
            btnAddRecord.setEnabled(true);


        } else {

            textViewAddRecordWeight.setEnabled(false);
            textViewAddRecordHeight.setEnabled(false);
            textViewAddRecordTemp.setEnabled(false);
            spinnerAddRecordDiagnosis.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
            btnAddRecord.setEnabled(false);
        }
    }




}