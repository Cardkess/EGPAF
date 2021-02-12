package com.interview.egpaf;

public class Record {

    private int id;
    private String weight;
    private String height;
    private String temp_reading;
    private String diagnosis;
    private int patient_id;
    private String date;
    private String created_at;
    private String updated_at;

    public Record(int id, String weight, String height, String temp_reading, String diagnosis, int patient_id, String date, String created_at, String updated_at) {
        this.id = id;
        this.weight = weight;
        this.height = height;
        this.temp_reading = temp_reading;
        this.diagnosis = diagnosis;
        this.patient_id = patient_id;
        this.date = date;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTemp_reading() {
        return temp_reading;
    }

    public void setTemp_reading(String temp_reading) {
        this.temp_reading = temp_reading;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
