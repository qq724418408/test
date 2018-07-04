package com.bocop.xfjr.bean.pretrial;

import java.io.Serializable;

public class PretrialInfoBean implements Serializable {
	 /**
     * customerType : 2
     * maritalStatus : 0
     * education : 本科
     * address : 江西省xx市xx区xx路10号
     * companyName : 长久世达
     * companyType : 2
     * file : http://192.168.2.8/fqt/business/images/photo.jpg
     * contactName1 : 张翠山
     * contactTelephone1 : 138xxxx2333
     * contactName2 : 张三丰
     * contactTelephone2 : 138xxxx3388
     * fund : {"companyName":"四方精创","monthPay":"5000.00","startPayTime":"1513165664","validatedStatus":"2"}
     * house : {"housesAddress":"江西省xx市xx区xx路10号对面","housesType":"3","housesArea":"120","validatedStatus":"3"}
     * normal : {"workYears":"5","duty":"客户经理","income":"21000.00","spouse":{"companyName":"四方精创","workYears":"3","duty":"产品经理","income":"15000.00"},"house":{"housesAddress":"江西省xx市xx区xx路10号傍边","housesArea":"200","housesValue":"3000000","housesOwner":"李防冬","relation":"兄弟"},"guarantor":{"companyName":"四方精创","workYears":"3","duty":"项目经理","income":"18000.00","maritalStatus":"0","guarantorSpouse":{"companyName":"四方精创","workYears":"3","duty":"产品经理","income":"15000.00"},"guarantorHouse":{"housesAddress":"江西省xx市xx区xx路10号对面","housesArea":"180","validatedStatus":"3"}}}
     */

    private String customerType;
    private String maritalStatus;
    private String education;
    private String address;
    private String companyName;
    private String companyType;
    private String file;
    private String contactName1;
    private String contactTelephone1;
    private String contactName2;
    private String contactTelephone2;
    private ComonBean fund;
    private HouseBean house;
    private NormalBean normal;

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getContactName1() {
        return contactName1;
    }

    public void setContactName1(String contactName1) {
        this.contactName1 = contactName1;
    }

    public String getContactTelephone1() {
        return contactTelephone1;
    }

    public void setContactTelephone1(String contactTelephone1) {
        this.contactTelephone1 = contactTelephone1;
    }

    public String getContactName2() {
        return contactName2;
    }

    public void setContactName2(String contactName2) {
        this.contactName2 = contactName2;
    }

    public String getContactTelephone2() {
        return contactTelephone2;
    }

    public void setContactTelephone2(String contactTelephone2) {
        this.contactTelephone2 = contactTelephone2;
    }

    public ComonBean getFund() {
        return fund;
    }

    public void setFund(ComonBean fund) {
        this.fund = fund;
    }

    public HouseBean getHouse() {
        return house;
    }

    public void setHouse(HouseBean house) {
        this.house = house;
    }

    public NormalBean getNormal() {
        return normal;
    }

    public void setNormal(NormalBean normal) {
        this.normal = normal;
    }

    

   

    
}
