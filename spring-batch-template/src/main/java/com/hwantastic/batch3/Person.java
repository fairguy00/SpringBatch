package com.hwantastic.batch3;

public class Person {
    private Long personId;
    private String fullName;
    private String firstName;
    private String lastName;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Person [personId=" + personId + ", fullName=" + fullName + ", firstName=" + firstName + ", lastName="
                + lastName + "]";
    }
}

