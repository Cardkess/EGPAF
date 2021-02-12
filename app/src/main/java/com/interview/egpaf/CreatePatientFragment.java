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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePatientFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String currentDate;
    private SharedViewModel viewModel;
    private int counter = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Patient> data;

    private Snackbar snackbar;
    private static int TIME_OUT = 1000;
    private Helper helper;
    private EditText patientFName, patientSurname, patientDateOfBirth, patientAddress;
    private Spinner patientOccupation;
    private ProgressBar mProgressBar;
    private Button btnAddPatient;
    private RadioGroup patientGenderRadioGroup;
    private RadioButton pMale;
    private RadioButton pFemale;
    private CheckBox patientEstimatedDOBCheckBox;

    private DatabaseHelper databaseHelper;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatePatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePatientFragment newInstance(String param1, String param2) {
        CreatePatientFragment fragment = new CreatePatientFragment();
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
        //return inflater.inflate(R.layout.fragment_create_patient, container, false);

        View view = inflater.inflate(R.layout.fragment_create_patient, container, false);
        setSpinnerContent(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        helper = new Helper(getActivity());
        databaseHelper = new DatabaseHelper(getContext());
        patientFName = (EditText) view.findViewById(R.id.createPatientFirstName);
        patientSurname = (EditText) view.findViewById(R.id.createPatientSurname);
        patientDateOfBirth = (EditText) view.findViewById(R.id.createPatientDOB);
        patientAddress = (EditText) view.findViewById(R.id.createPatientAddress);
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

       snackbar = Snackbar.make(view, "Patient added successfully!", Snackbar.LENGTH_SHORT);

        pMale = (RadioButton) view.findViewById(R.id.rbMale);
        pFemale = (RadioButton) view.findViewById(R.id.rdFemale);

        btnAddPatient = (Button) view.findViewById(R.id.btnAddPatient);
        mProgressBar = (ProgressBar) view.findViewById(R.id.addPatientProgressBar);
        patientOccupation = (Spinner) view.findViewById(R.id.occupationSpinner);

        patientGenderRadioGroup = (RadioGroup) view.findViewById(R.id.patientGenderRadioGroup);
        patientEstimatedDOBCheckBox = (CheckBox) view.findViewById(R.id.patientEstimatedDOBCheckBox);

        btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addPatient();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                counter++;
                String newCounter = String.valueOf(counter);
                viewModel.getCurrentID().setValue(newCounter);

            }
        });
    }

    private void addPatient() throws ParseException {

        String pFName = helper.capitalizeFirstLetter(patientFName.getText().toString().trim());
        String pSurname = helper.capitalizeFirstLetter(patientSurname.getText().toString().trim());
        int pEstimatedDOB = 0;
        String pDateOfBirth = patientDateOfBirth.getText().toString().trim();
        String pAddress = patientAddress.getText().toString().trim();
        String pOccupation = patientOccupation.getSelectedItem().toString().trim();


        String pGender = "";

        if (pMale.isChecked()){

            pGender = "Male";
        }

        if (pFemale.isChecked()){

            pGender = "Female";
        }

        if (patientEstimatedDOBCheckBox.isChecked()){
            pEstimatedDOB = 1;
        }


        if (TextUtils.isEmpty(pFName)){

            helper.MakeToast("Enter First Name!");

        } else if(TextUtils.isEmpty(pSurname)){

            helper.MakeToast("Enter Surname!");

        } else if(TextUtils.isEmpty(pGender)){

            helper.MakeToast("Select Gender!");

        }else if(TextUtils.isEmpty(pDateOfBirth)){

            helper.MakeToast("Enter Date of Birth!");

            //Date format validation
        }else if(!helper.isValidDate(pDateOfBirth) || pDateOfBirth.length() > 10){

            helper.MakeToast("Invalid Date format! eg.: MM/DD/YYYY");

        }else if(!helper.isDateAhead(pDateOfBirth)){

            helper.MakeToast("Invalid Date!");

        }  else if(TextUtils.isEmpty(pAddress)){

            helper.MakeToast("Enter Address!");

        } else if(patientOccupation.getSelectedItemPosition() == 0){

            helper.MakeToast("Select Occupation!");

        }else {

            toggleFields(false);
            mProgressBar.setVisibility(View.VISIBLE);

            Patient patient = new Patient(0,pEstimatedDOB,pFName,pSurname,pGender,pDateOfBirth,pAddress,pOccupation,currentDate,currentDate);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                        if(databaseHelper.registerPatient(patient)){

                            patientFName.setText("");
                            patientSurname.setText("");
                            patientAddress.setText("");
                            patientDateOfBirth.setText("");
                            patientGenderRadioGroup.clearCheck();
                            patientEstimatedDOBCheckBox.setChecked(false);
                            patientOccupation.setSelection(0);
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

    private void toggleFields(boolean b) {

        if (b){

            patientFName.setEnabled(true);
            patientSurname.setEnabled(true);
            patientDateOfBirth.setEnabled(true);
            patientAddress.setEnabled(true);
            patientOccupation.setEnabled(true);
            mProgressBar.setVisibility(View.INVISIBLE);
            btnAddPatient.setEnabled(true);

            // Enable the Radio Buttons of the Radio Group
            for (int i = 0; i < patientGenderRadioGroup.getChildCount(); i++){

                patientGenderRadioGroup.getChildAt(i).setEnabled(true);

            }

            patientEstimatedDOBCheckBox.setEnabled(true);

        } else {

            patientFName.setEnabled(false);
            patientSurname.setEnabled(false);
            patientDateOfBirth.setEnabled(false);
            patientAddress.setEnabled(false);
            patientOccupation.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
            btnAddPatient.setEnabled(false);

            // Disable the Radio Buttons of the Radio Group
            for (int i = 0; i < patientGenderRadioGroup.getChildCount(); i++){

                patientGenderRadioGroup.getChildAt(i).setEnabled(false);

            }

            patientEstimatedDOBCheckBox.setEnabled(false);
        }
    }

    private void setSpinnerContent(View view) {

        Spinner spinner = (Spinner) view.findViewById(R.id.occupationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.occupations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}