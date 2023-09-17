package org.example;

import java.util.Objects;

public class Employee {

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String id;
    private String firstname;
    private String lastname;

    private String location;

    public Employee(String id, String firstname, String lastname, String location) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.location = location;
    }


    @Override
    public String toString() {
        return "<" + id + ", " + firstname + ", " + lastname + ", " + location + ">";
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(firstname, employee.firstname) && Objects.equals(lastname, employee.lastname) && Objects.equals(location, employee.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, location);
    }
}