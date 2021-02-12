package com.interview.egpaf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRecordsFragment extends Fragment {

    private SharedViewModel viewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Record> data;
    private Helper helper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewRecordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewRecordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewRecordsFragment newInstance(String param1, String param2) {
        ViewRecordsFragment fragment = new ViewRecordsFragment();
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
        return inflater.inflate(R.layout.fragment_view_records, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = new Helper(getActivity());
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        int id = viewModel.getPatient().getId();

        databaseHelper = new DatabaseHelper(getActivity());

        data = databaseHelper.getRecords(id);

        adapter = new RecordViewAdapter(data,getActivity());

        viewModel.getCurrentID().observe(getViewLifecycleOwner(), item -> {
            // Update the UI.
            List<Record> newData = databaseHelper.getRecords(id);

            data.clear();

            for (int i = 0; i < newData.size() ; i++) {

                data.add(newData.get(i));
            }

            adapter.notifyDataSetChanged();
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.viewRecordsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}