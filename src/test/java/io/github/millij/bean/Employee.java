package io.github.millij.bean;

import static io.github.millij.poi.ss.model.DateTimeType.DATE;
import static io.github.millij.poi.ss.model.DateTimeType.DURATION;

import java.math.BigDecimal;
import java.util.Date;

import io.github.millij.poi.ss.model.CellType;
import io.github.millij.poi.ss.model.annotations.Sheet;
import io.github.millij.poi.ss.model.annotations.SheetColumn;


@Sheet
public class Employee {

    // Note that Id and Name are annotated at name level
    private String id;
    private String name;

    @SheetColumn(value = "Age", type = CellType.NUMERIC)
    private Integer age;

    @SheetColumn("Gender")
    private String gender;

    @SheetColumn(value = "Height (mts)", type = CellType.NUMERIC)
    private Double height;

    @SheetColumn("Address")
    private String address;

    @SheetColumn(value = "Weight (kg)", type = CellType.NUMERIC)
    private BigDecimal weight;


    // DateTime fields

    @SheetColumn(value = "DOB", datetime = DATE, format = "dd-MM-yyy")
    private Date dateOfBirth; // Date

    @SheetColumn(value = "Last Login Time", datetime = DATE, format = "dd-MM-yyy HH:mm")
    private Long lastLoginTime; // Timestamp

    @SheetColumn(value = "Last Session Duration", datetime = DURATION, format = "HH:mm:ss")
    private Long lastSessionDuration; // Duration


    // Constructors
    // ------------------------------------------------------------------------

    public Employee() {
        // Default
    }

    public Employee(String id, String name, Integer age, String gender, Double height) {
        super();

        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
    }

    public Employee(String id, String name, Integer age, String gender, Double height, BigDecimal weight) {
        super();

        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    @SheetColumn("ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SheetColumn("Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getLastSessionDuration() {
        return lastSessionDuration;
    }

    public void setLastSessionDuration(Long lastSessionDuration) {
        this.lastSessionDuration = lastSessionDuration;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", age=" + age + ", gender=" + gender + ", height=" + height
                + ", address=" + address + ", dateOfBirth=" + dateOfBirth + ", lastLoginTime=" + lastLoginTime
                + ", lastSessionDuration=" + lastSessionDuration + "]";
    }


}
