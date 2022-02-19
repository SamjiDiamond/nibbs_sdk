package com.nibbs;

public class Datamodel {
    private int id;
    private String title;
    private String surname;
    private String firstname;
    private String middlename;
    private String dateofbirth;
    private String gender;
    private String maritalstatus;
    private String uploadstatus;

    public Datamodel(int id, String title, String surname, String firstname, String middlename, String dateofbirth, String gender, String maritalstatus, String uploadstatus) {
        this.id = id;
        this.title = title;
        this.surname = surname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.maritalstatus = maritalstatus;
        this.uploadstatus = uploadstatus;
    }

    @Override
    public String toString() {
        return "datamodel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", surname='" + surname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", dateofbirth='" + dateofbirth + '\'' +
                ", gender='" + gender + '\'' +
                ", maritalstatus='" + maritalstatus + '\'' +
                ", uploadstatus='" + uploadstatus + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getUploadstatus() {
        return uploadstatus;
    }

    public void setUploadstatus(String uploadstatus) {
        this.uploadstatus = uploadstatus;
    }

}
