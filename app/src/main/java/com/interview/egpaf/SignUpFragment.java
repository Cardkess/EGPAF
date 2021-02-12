package com.interview.egpaf;

import android.database.Cursor;
import android.os.Bundle;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private LinearLayout signUpProgressLayout;
    private EditText signUpFName, signUpSurname, signUpEmail, signUpPassword, signUpConfirmPassword;
    private Button signUpButton;
    private TextView signUpStatus;
    private NavController mNavigator;
    private static int TIME_OUT = 1000;
    private Helper helper;
    private SharedViewModel viewModel;
    private String currentDate;

    private AESCrypt aesCrypt;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getContext());
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        signUpProgressLayout = (LinearLayout) view.findViewById(R.id.signUpStatusLayout);
        signUpFName = (EditText) view.findViewById(R.id.signup_firstname);
        signUpSurname = (EditText) view.findViewById(R.id.signup_surname);
        signUpEmail = (EditText) view.findViewById(R.id.signup_email);
        signUpPassword = (EditText) view.findViewById(R.id.signup_password);
        signUpConfirmPassword = (EditText) view.findViewById(R.id.signup_confirm_password);
        signUpButton = (Button) view.findViewById(R.id.btnSignup);
        signUpStatus = (TextView) view.findViewById(R.id.signUpStatus);
        mNavigator = Navigation.findNavController(view);
        helper = new Helper(getActivity());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startRegistration();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // Register new User
    private void startRegistration() throws Exception {

        //Get input from User
        String firstname = helper.capitalizeFirstLetter(signUpFName.getText().toString().trim());
        String surname = helper.capitalizeFirstLetter(signUpSurname.getText().toString().trim());
        String email = signUpEmail.getText().toString().trim().toLowerCase();
        String password = signUpPassword.getText().toString().trim();
        String confirm_password = signUpConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstname)){

            helper.MakeToast("Please enter your first name!");

        } else if (TextUtils.isEmpty(surname)){

            helper.MakeToast("Please enter your surname!");

        } else if (TextUtils.isEmpty(email)){

            helper.MakeToast("Please enter your email!");

        } else if (TextUtils.isEmpty(password)){

            helper.MakeToast("Please enter password!");

        } else if (TextUtils.isEmpty(confirm_password)){

            helper.MakeToast("Please enter confirm password!");

        } else if (!password.equals(confirm_password)){

            helper.MakeToast("Password not matching!");

        } else {

            toggleFields(false);
            ;
            User user = new User(firstname,surname,email,AESCrypt.encrypt(password),currentDate,currentDate);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Cursor cursor = databaseHelper.searchUser(user.getEmail());
                            if (cursor.getCount() == 0){

                                if(databaseHelper.registerUser(user)){

                                    signUpStatus.setText("Setting up Account...");

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            viewModel.setLogged(user);
                                            mNavigator.navigate(R.id.successfulSignUpFragment);

                                            signUpFName.setText("");
                                            signUpSurname.setText("");
                                            signUpEmail.setText("");
                                            signUpPassword.setText("");
                                            signUpConfirmPassword.setText("");

                                        }
                                    }, TIME_OUT);


                                } else {

                                    helper.MakeToast("Operation failed!");
                                    toggleFields(true);
                                }


                            } else {

                                helper.MakeToast("Username already exists!");
                                toggleFields(true);
                            }


                        }

                    }, TIME_OUT);

                }

            }, TIME_OUT);
        }
    }

    private void toggleFields(Boolean bool){

        if (bool){

            signUpFName.setEnabled(true);
            signUpSurname.setEnabled(true);
            signUpEmail.setEnabled(true);
            signUpPassword.setEnabled(true);
            signUpConfirmPassword.setEnabled(true);
            signUpButton.setEnabled(true);
            signUpProgressLayout.setVisibility(View.INVISIBLE);

        } else {

            signUpFName.setEnabled(false);
            signUpSurname.setEnabled(false);
            signUpEmail.setEnabled(false);
            signUpPassword.setEnabled(false);
            signUpConfirmPassword.setEnabled(false);
            signUpButton.setEnabled(false);

            signUpProgressLayout.setVisibility(View.VISIBLE);
        }
    }
}