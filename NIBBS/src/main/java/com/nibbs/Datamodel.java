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
    private String institutioncode;
    private String institutionname;
    private String agentcode;
    private String ticketid;
    private String validationstatus;
    private String capturedate;
    private String syncdate;
    private String validationdate;
    private String stateofcapture;
    private String stateofsync;

    public Datamodel(int id, String title, String surname,
                     String firstname, String middlename, String dateofbirth,
                     String gender, String maritalstatus, String uploadstatus,
                     String institutioncode, String institutionname, String agentcode,
                     String ticketid, String validationstatus, String capturedate,
                     String syncdate, String validationdate, String stateofcapture,
                     String stateofsync) {
        this.id = id;
        this.title = title;
        this.surname = surname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.maritalstatus = maritalstatus;
        this.uploadstatus = uploadstatus;
        this.institutioncode = institutioncode;
        this.institutionname = institutionname;
        this.agentcode = agentcode;
        this.ticketid = ticketid;
        this.validationstatus = validationstatus;
        this.capturedate = capturedate;
        this.syncdate = syncdate;
        this.validationdate = validationdate;
        this.stateofcapture = stateofcapture;
        this.stateofsync = stateofsync;
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

    public String getInstitutioncode() {
        return institutioncode;
    }

    public void setInstitutioncode(String institutioncode) {
        this.institutioncode = institutioncode;
    }

    public String getInstitutionname() {
        return institutionname;
    }

    public void setInstitutionname(String institutionname) {
        this.institutionname = institutionname;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public String getValidationstatus() {
        return validationstatus;
    }

    public void setValidationstatus(String validationstatus) {
        this.validationstatus = validationstatus;
    }

    public String getCapturedate() {
        return capturedate;
    }

    public void setCapturedate(String capturedate) {
        this.capturedate = capturedate;
    }

    public String getSyncdate() {
        return syncdate;
    }

    public void setSyncdate(String syncdate) {
        this.syncdate = syncdate;
    }

    public String getValidationdate() {
        return validationdate;
    }

    public void setValidationdate(String validationdate) {
        this.validationdate = validationdate;
    }

    public String getStateofcapture() {
        return stateofcapture;
    }

    public void setStateofcapture(String stateofcapture) {
        this.stateofcapture = stateofcapture;
    }

    public String getStateofsync() {
        return stateofsync;
    }

    public void setStateofsync(String stateofsync) {
        this.stateofsync =stateofsync;
    }


}
