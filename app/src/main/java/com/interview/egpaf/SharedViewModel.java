package com.interview.egpaf;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<String> currentID;

    public MutableLiveData<String> getCurrentID() {
        if (currentID == null) {
            currentID = new MutableLiveData<String>();
        }
        return currentID;
    }

    private User loggedUser = null;

    private Patient patient = null;

    public User getLogged(){
        return this.loggedUser;
    }

    public void setLogged(User user){
        this.loggedUser = user;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
