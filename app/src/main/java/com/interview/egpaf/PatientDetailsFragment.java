package com.interview.egpaf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientDetailsFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private TextView patientDetailsFName, patientDetailsGender, patientDetailsSurname, patientDetailsID, patientDetailsDOB, patientDetailsEDOB, patientDetailsAddress, patientDetailsOccupation;
    private SharedViewModel viewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientDetailsFragment newInstance(String param1, String param2) {
        PatientDetailsFragment fragment = new PatientDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_patient_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        patientDetailsFName = (TextView) view.findViewById(R.id.patientDetailsFName);
        patientDetailsSurname = (TextView) view.findViewById(R.id.patientDetailsSurname);
        patientDetailsGender = (TextView) view.findViewById(R.id.patientDetailsGender);
        patientDetailsDOB = (TextView) view.findViewById(R.id.patientDetailsDOB);
        patientDetailsEDOB = (TextView) view.findViewById(R.id.patientDetailsEDOB);
        patientDetailsAddress = (TextView) view.findViewById(R.id.patientDetailsAddress);
        patientDetailsOccupation = (TextView) view.findViewById(R.id.patientDetailsOccupation);

        Patient patient = viewModel.getPatient();
        patientDetailsFName.setText(patient.getFirstname());
        patientDetailsSurname.setText(patient.getSurname());
        patientDetailsDOB.setText(patient.getDob());
        patientDetailsGender.setText(patient.getGender());

        if (patient.getEstimated_dob() == 1){

            patientDetailsEDOB.setText("Yes");
        } else {
            patientDetailsEDOB.setText("No");
        }

        patientDetailsAddress.setText(patient.getAddress());

        patientDetailsOccupation.setText(patient.getOccupation());
    }
}