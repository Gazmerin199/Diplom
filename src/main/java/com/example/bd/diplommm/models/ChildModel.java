package com.example.bd.diplommm.models;

import java.sql.Date;
import java.time.LocalDate;

public class ChildModel {
    private Integer id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Date birthDate;
    private String gender;
    private String residence;
    private String actualResidence;
    private String representativeName;
    private String representativeResidence;
    private String representativeActualResidence;
    private String representativePhone;
    private Integer examinationNumber;
    private Date examinationDate;
    private String educationPlace;
    private String educationProgram;
    private String healthLossDegree;
    private Date healthLossTerm;
    private String disabilityReason;
    private String expertDecisionAddition;
    private String educationConditions;
    private Boolean firstTime;
    private Boolean repeat;
    private Boolean untilEighteen;
    private Date createdDate;

    // Пустой конструктор
    public ChildModel() {
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getResidence() {
        return residence;
    }
    public void setResidence(String residence) {
        this.residence = residence;
    }
    public String getActualResidence() {
        return actualResidence;
    }
    public void setActualResidence(String actualResidence) {
        this.actualResidence = actualResidence;
    }
    public String getRepresentativeName() {
        return representativeName;
    }
    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }
    public String getRepresentativeResidence() {
        return representativeResidence;
    }
    public void setRepresentativeResidence(String representativeResidence) { this.representativeResidence = representativeResidence; }
    public String getRepresentativeActualResidence() {
        return representativeActualResidence;
    }
    public void setRepresentativeActualResidence(String representativeActualResidence) { this.representativeActualResidence = representativeActualResidence; }
    public String getRepresentativePhone() {
        return representativePhone;
    }
    public void setRepresentativePhone(String representativePhone) {
        this.representativePhone = representativePhone;
    }
    public Integer getExaminationNumber() {
        return examinationNumber;
    }
    public void setExaminationNumber(Integer examinationNumber) {
        this.examinationNumber = examinationNumber;
    }
    public Date getExaminationDate() {
        return examinationDate;
    }
    public void setExaminationDate(Date examinationDate) {
        this.examinationDate = examinationDate;
    }
    public String getEducationPlace() {
        return educationPlace;
    }
    public void setEducationPlace(String educationPlace) {
        this.educationPlace = educationPlace;
    }
    public String getEducationProgram() {
        return educationProgram;
    }
    public void setEducationProgram(String educationProgram) {
        this.educationProgram = educationProgram;
    }
    public String getHealthLossDegree() {
        return healthLossDegree;
    }
    public void setHealthLossDegree(String healthLossDegree) {
        this.healthLossDegree = healthLossDegree;
    }
    public Date getHealthLossTerm() {
        return healthLossTerm;
    }
    public void setHealthLossTerm(Date healthLossTerm) {
        this.healthLossTerm = healthLossTerm;
    }
    public String getDisabilityReason() {
        return disabilityReason;
    }
    public void setDisabilityReason(String disabilityReason) {
        this.disabilityReason = disabilityReason;
    }
    public String getExpertDecisionAddition() {
        return expertDecisionAddition;
    }
    public void setExpertDecisionAddition(String expertDecisionAddition) { this.expertDecisionAddition = expertDecisionAddition; }
    public String getEducationConditions() {
        return educationConditions;
    }
    public void setEducationConditions(String educationConditions) {
        this.educationConditions = educationConditions;
    }
    public Boolean isFirstTime() {
        return firstTime != null ? firstTime : false;
    }
    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }
    public Boolean isRepeat() {
        return repeat != null ? repeat : false;
    }
    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }
    public Boolean isUntilEighteen() {
        return untilEighteen != null ? untilEighteen : false;
    }
    public void setUntilEighteen(Boolean untilEighteen) {
        this.untilEighteen = untilEighteen;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    // Метод для получения полного ФИО
    public String getFullName() {
        return String.format("%s %s %s",
                lastName != null ? lastName : "",
                firstName != null ? firstName : "",
                patronymic != null ? patronymic : "").trim();
    }
    public Integer getAge() {
        if (birthDate == null) return null;
        LocalDate birth = birthDate.toLocalDate();
        LocalDate now = LocalDate.now();
        return java.time.Period.between(birth, now).getYears();
    }
    @Override
    public String toString() {
        return "ChildModel{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", residence='" + residence + '\'' +
                ", actualResidence='" + actualResidence + '\'' +
                ", representativeName='" + representativeName + '\'' +
                ", representativePhone='" + representativePhone + '\'' +
                '}';
    }
    public static class Builder {
        private final ChildModel childModel;
        public Builder() {
            childModel = new ChildModel();
        }
        public ChildModel build() {
            return childModel;
        }
    }
    public ChildModel copy() {
        ChildModel copy = new ChildModel();
        copy.setId(this.id);
        copy.setLastName(this.lastName);
        copy.setFirstName(this.firstName);
        copy.setPatronymic(this.patronymic);
        copy.setBirthDate(this.birthDate);
        copy.setGender(this.gender);
        copy.setResidence(this.residence);
        copy.setActualResidence(this.actualResidence);
        copy.setRepresentativeName(this.representativeName);
        copy.setRepresentativeResidence(this.representativeResidence);
        copy.setRepresentativeActualResidence(this.representativeActualResidence);
        copy.setRepresentativePhone(this.representativePhone);
        copy.setExaminationNumber(this.examinationNumber);
        copy.setExaminationDate(this.examinationDate);
        copy.setEducationPlace(this.educationPlace);
        copy.setEducationProgram(this.educationProgram);
        copy.setHealthLossDegree(this.healthLossDegree);
        copy.setHealthLossTerm(this.healthLossTerm);
        copy.setDisabilityReason(this.disabilityReason);
        copy.setExpertDecisionAddition(this.expertDecisionAddition);
        copy.setEducationConditions(this.educationConditions);
        copy.setFirstTime(this.firstTime);
        copy.setRepeat(this.repeat);
        copy.setUntilEighteen(this.untilEighteen);
        copy.setCreatedDate(this.createdDate);
        return copy;
    }
}