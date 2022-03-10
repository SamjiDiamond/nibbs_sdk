package com.nibbs.database;

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
    private String nationality;
    private String soo;
    private String lga;
    private String residentialaddress;
    private String stateofresidence;
    private String lgaofresidence;
    private String landmarks;
    private String email;
    private String phonenumber;
    private String phonenumber2;
    private String accountlevel;
    private String nin;
    private String selectbank;
    private String lgaofcapture;
    private String signatureimage;
    private String signatureimagename;
    private String faceimage;
    private String faceimagename;
    private String fingerimage;
    private String fingerimagename;

    public Datamodel(int id, String title, String surname,
                     String firstname, String middlename, String dateofbirth,
                     String gender, String maritalstatus,
                     String institutioncode, String institutionname, String agentcode,
                     String ticketid, String capturedate,
                     String stateofcapture, String soo, String nationality, String lga,
                     String residentialaddress, String stateofresidence, String lgaofresidence,
                     String landmarks, String email, String phonenumber, String phonenumber2,
                     String accountlevel, String nin, String selectbank, String lgaofcapture,
                     String signatureimage, String signatureimagename, String faceimage,
                     String faceimagename, String fingerimage, String fingerimagename,
                     String uploadstatus, String validationstatus, String stateofsync) {
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
        this.nationality = nationality;
        this.soo = soo;
        this.lga = lga;
        this.residentialaddress = residentialaddress;
        this.stateofresidence = stateofresidence;
        this.lgaofresidence = lgaofresidence;
        this.landmarks = landmarks;
        this.email = email;
        this.phonenumber = phonenumber;
        this.phonenumber2 = phonenumber2;
        this.accountlevel = accountlevel;
        this.nin = nin;
        this.selectbank = selectbank;
        this.lgaofcapture = lgaofcapture;
        this.signatureimage = signatureimage;
        this.signatureimagename = signatureimagename;
        this.faceimage = faceimage;
        this.faceimagename = faceimagename;
        this.fingerimage = fingerimage;
        this.fingerimagename = fingerimagename;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSoo() {
        return soo;
    }

    public void setSoo(String soo) {
        this.soo = soo;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getResidentialaddress() {
        return residentialaddress;
    }

    public void setResidentialaddress(String residentialaddress) {
        this.residentialaddress = residentialaddress;
    }

    public String getStateofresidence() {
        return stateofresidence;
    }

    public void setStateofresidence(String stateofresidence) {
        this.stateofresidence = stateofresidence;
    }

    public String getLgaofresidence() {
        return lgaofresidence;
    }

    public void setLgaofresidence(String lgaofresidence) {
        this.lgaofresidence = lgaofresidence;
    }

    public String getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(String landmarks) {
        this.landmarks = landmarks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber =phonenumber;
    }

    public String getPhonenumber2() {
        return phonenumber2;
    }

    public void setPhonenumber2(String phonenumber2) {
        this.phonenumber2 =phonenumber2;
    }

    public String getAccountlevel() {
        return accountlevel;
    }

    public void setAccountlevel(String accountlevel) {
        this.accountlevel = accountlevel;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getSelectbanke() {
        return selectbank;
    }

    public void setSelectbank(String selectbank) {
        this.selectbank = selectbank;
    }

    public String getLgaofcapture() {
        return lgaofcapture;
    }

    public void setLgaofcapture(String lgaofcapture) {
        this.lgaofcapture= lgaofcapture;
    }

    public String getSignatureimage() {
        return signatureimage;
    }

    public void setSignatureimage(String signatureimage) {
        this.signatureimage = signatureimage;
    }

    public String getSignatureimagename() {
        return signatureimagename;
    }

    public void setSignatureimagename(String signatureimagename) {
        this.signatureimagename =signatureimagename;
    }

    public String getFaceimage() {
        return faceimage;
    }

    public void setFaceimage(String faceimage) {
        this.faceimage =faceimage;
    }

    public String getFaceimagename() {
        return faceimagename;
    }

    public void setFaceimagename(String faceimagename) {
        this.faceimagename =faceimagename;
    }

    public String getFingerimage() {
        return fingerimage;
    }

    public void setFingerimage(String fingerimage) {
        this.fingerimage =fingerimage;
    }

    public String getFingerimagename() {
        return fingerimagename;
    }

    public void setFingerimagename(String fingerimagename) {
        this.fingerimagename =fingerimagename;
    }


}
