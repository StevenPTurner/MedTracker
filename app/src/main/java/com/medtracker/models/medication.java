package com.medtracker.models;

/**
 * Created by home on 02/12/2016.
 */

public class medication {
    private String medication_name;
    private String instructions;
    private boolean has_prescription;
    private int dosage;

    public medication() {
    }

    public medication(String medication_name, String instructions, boolean has_prescription, int dosage) {
        this.medication_name = medication_name;
        this.instructions = instructions;
        this.has_prescription = has_prescription;
        this.dosage = dosage;
    }

    public String getMedication_name() {
        return medication_name;
    }

    public void setMedication_name(String medication_name) {
        this.medication_name = medication_name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isHas_prescription() {
        return has_prescription;
    }

    public void setHas_prescription(boolean has_prescription) {
        this.has_prescription = has_prescription;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }
}
