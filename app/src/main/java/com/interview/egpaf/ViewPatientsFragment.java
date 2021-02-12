package com.interview.egpaf;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPatientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPatientsFragment extends Fragment {

    private SharedViewModel viewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DatabaseHelper databaseHelper;
    private Helper helper;
    private List<Patient> data;

    private EditText edSearch;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewPatientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPatientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPatientsFragment newInstance(String param1, String param2) {
        ViewPatientsFragment fragment = new ViewPatientsFragment();
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
        return inflater.inflate(R.layout.fragment_view_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        databaseHelper = new DatabaseHelper(getActivity());

        helper = new Helper(getActivity());

        edSearch = (EditText) view.findViewById(R.id.edSearch);

        recyclerView = (RecyclerView) view.findViewById(R.id.viewPatientsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        data = databaseHelper.getAllPatients();

        adapter = new PatientViewAdapter(data,getActivity());

        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getCurrentID().observe(getViewLifecycleOwner(), item -> {
            // Update the UI.
            List<Patient> newData = databaseHelper.getAllPatients();

            data.clear();

            for (int i = 0; i < newData.size() ; i++) {

                data.add(newData.get(i));
            }
            
            adapter.notifyDataSetChanged();
        });


        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String searchString = String.valueOf(s);
                if(searchString.length() < 1){

                    List<Patient> newData = databaseHelper.getAllPatients();

                    data.clear();

                    for (int i = 0; i < newData.size() ; i++) {

                        data.add(newData.get(i));
                    }

                    adapter.notifyDataSetChanged();

                } else {

                    List<Patient> newData = databaseHelper.searchPatients(searchString);

                    data.clear();

                    for (int i = 0; i < newData.size() ; i++) {

                        data.add(newData.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }

    public class PatientViewAdapter extends RecyclerView.Adapter<PatientViewAdapter.ViewHolder> {

        private List<Patient> patientsList;
        private Context context;
        private Intent intent;



        public PatientViewAdapter(List<Patient> patientsList, Context context) {
            this.patientsList = patientsList;
            this.context = context;
            this.intent =  new Intent(context, ShowPatientDetails.class);

        }

        @NonNull
        @Override
        public PatientViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_item, parent,false);

            return new PatientViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PatientViewAdapter.ViewHolder holder, int position) {
            Patient patient = patientsList.get(position);

            holder.textViewID.setText(String.valueOf(patient.getId()));
            holder.textViewFName.setText(patient.getFirstname());
            holder.textViewSurname.setText(patient.getSurname());
            holder.textViewGender.setText(patient.getGender());
            holder.textViewDOB.setText(patient.getDob());

            if (patient.getEstimated_dob() == 1){
                holder.textViewEDOB.setText("Yes");
            } else {
                holder.textViewEDOB.setText("No");
            }

            holder.textViewAddress.setText(patient.getAddress());
            holder.textViewOccupation.setText(patient.getOccupation());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), ShowPatientDetails.class);
                    intent.putExtra("patientID",patient.getId());
                    startActivity(intent);

                    //context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return patientsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView textViewID, textViewFName, textViewSurname, textViewGender, textViewDOB, textViewEDOB, textViewAddress, textViewOccupation;
            public MaterialCardView cardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewFName = (TextView) itemView.findViewById(R.id.patientItemFName);
                textViewSurname = (TextView) itemView.findViewById(R.id.patientItemSurname);
                textViewID = (TextView) itemView.findViewById(R.id.patientItemID);
                textViewGender = (TextView) itemView.findViewById(R.id.patientItemGender);
                textViewDOB = (TextView) itemView.findViewById(R.id.patientItemDOB);
                textViewEDOB = (TextView) itemView.findViewById(R.id.patientItemEDOB);
                textViewAddress = (TextView) itemView.findViewById(R.id.patientItemAddress);
                textViewOccupation = (TextView) itemView.findViewById(R.id.patientItemOccupation);


                cardView = (MaterialCardView) itemView.findViewById(R.id.patiendCardView);

            }
        }
    }

}