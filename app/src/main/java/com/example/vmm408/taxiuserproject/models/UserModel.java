package com.example.vmm408.taxiuserproject.models;

public class UserModel {
    private String idUser;
    private String avatarUser;
    private String fullNameUser;
    private String genderUser;
    private String ageUser;
    private String phoneUser;
    private Double ratingUser;
    private String ratingNumUser;

    private Double experienceDriver;
    private String carModelDriver;
    private String numPlateCarDriver;

    public UserModel() {
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    public String getAvatarUser() {
        return avatarUser;
    }

    public void setFullNameUser(String fullNameUser) {
        this.fullNameUser = fullNameUser;
    }

    public String getFullNameUser() {
        return fullNameUser;
    }

    public void setGenderUser(String genderUser) {
        this.genderUser = genderUser;
    }

    public String getGenderUser() {
        return genderUser;
    }

    public void setAgeUser(String ageUser) {
        this.ageUser = ageUser;
    }

    public String getAgeUser() {
        return ageUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setRatingUser(Double ratingUser) {
        this.ratingUser = ratingUser;
    }

    public Double getRatingUser() {
        return ratingUser;
    }

    public void setRatingNumUser(String ratingNumUser) {
        this.ratingNumUser = ratingNumUser;
    }

    public String getRatingNumUser() {
        return ratingNumUser;
    }

    public Double getExperienceDriver() {
        return experienceDriver;
    }

    public void setExperienceDriver(Double experienceDriver) {
        this.experienceDriver = experienceDriver;
    }

    public void setCarModelDriver(String carModelDriver) {
        this.carModelDriver = carModelDriver;
    }

    public String getCarModelDriver() {
        return carModelDriver;
    }

    public void setNumPlateCarDriver(String numPlateCarDriver) {
        this.numPlateCarDriver = numPlateCarDriver;
    }

    public String getNumPlateCarDriver() {
        return numPlateCarDriver;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "idUser='" + idUser + '\'' +
                ", avatarUser='" + avatarUser + '\'' +
                ", fullNameUser='" + fullNameUser + '\'' +
                ", genderUser='" + genderUser + '\'' +
                ", ageUser='" + ageUser + '\'' +
                ", phoneUser='" + phoneUser + '\'' +
                ", ratingUser=" + ratingUser +
                ", ratingNumUser='" + ratingNumUser + '\'' +
                ", experienceDriver=" + experienceDriver +
                ", carModelDriver='" + carModelDriver + '\'' +
                ", numPlateCarDriver='" + numPlateCarDriver + '\'' +
                '}';
    }

    public static class SignedUser {
        private static UserModel userModel;

        public static void setUserModel(UserModel userModel) {
            SignedUser.userModel = userModel;
        }

        public static UserModel getUserModel() {
            return userModel;
        }
    }

    public static class OrderAcceptedDriver {
        private static UserModel userModel;

        public static void setUserModel(UserModel userModel) {
            OrderAcceptedDriver.userModel = userModel;
        }

        public static UserModel getUserModel() {
            return userModel;
        }
    }
}
