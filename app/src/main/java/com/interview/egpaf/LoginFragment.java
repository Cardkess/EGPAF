package com.interview.egpaf;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private SharedViewModel viewModel;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private Button loginButon, createAccountButton;
    private EditText edUsername, edPassword;
    private TextView login_state;
    private LinearLayout logginProgressLayout;
    private NavController navController;
    private DatabaseHelper databaseHelper;
    private static int TIME_OUT = 1000;
    private AESCrypt aesCrypt;

    User user = null;

    private Helper helper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        databaseHelper = new DatabaseHelper(getContext());

        logginProgressLayout = (LinearLayout) view.findViewById(R.id.loginStatusLayout);
        navController = Navigation.findNavController(view);
        createAccountButton = (Button) view.findViewById(R.id.btnCreateAccount);
        loginButon = (Button) view.findViewById(R.id.btnLogin);
        edUsername = (EditText) view.findViewById(R.id.edUserFirstName);
        edPassword = (EditText) view.findViewById(R.id.edPassword);
        login_state = (TextView) view.findViewById(R.id.loginAuthStatus);

        helper = new Helper(getActivity());

        loginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginProcess();
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

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.signUpFragment);
                edUsername.setText("");
                edPassword.setText("");


            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


                materialAlertDialogBuilder.show();

            }
        });


    }
    // Login process method 
    private void loginProcess() {

        String username = edUsername.getText().toString().trim().toLowerCase();
        String password = edPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)){

            helper.MakeToast("Enter Username!");

        } else if(TextUtils.isEmpty(password)){

            helper.MakeToast("Enter Password!");

        } else {

            toggleFields(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    try {
                        user = databaseHelper.loginUser(username, aesCrypt.encrypt(password) );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (user == null){

                        helper.MakeToast("Wrong username / password!");

                        toggleFields(true);


                    } else {

                        login_state.setText("Logging in...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //helper.MakeToast("Welcome back "+ user.getFirstname());
                                viewModel.setLogged(user);
                                navController.navigate(R.id.dashboardFragment);
                                toggleFields(true);

                            }
                        }, TIME_OUT);


                    }
                    // mNavigator.navigate(R.id.accountSetupFragment);

                }

            }, TIME_OUT);



        }

    }

    private void toggleFields(Boolean bool){

        if (bool){

            edUsername.setEnabled(true);
            edPassword.setEnabled(true);
            loginButon.setEnabled(true);
            createAccountButton.setEnabled(true);
            logginProgressLayout.setVisibility(View.INVISIBLE);

        } else {

            edUsername.setEnabled(false);
            edPassword.setEnabled(false);
            loginButon.setEnabled(false);
            createAccountButton.setEnabled(false);
            logginProgressLayout.setVisibility(View.VISIBLE);
        }

    }
}