package com.mycompany.attendence_system;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "student_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentMaster.findAll", query = "SELECT s FROM StudentMaster s"),
    @NamedQuery(name = "StudentMaster.findById", query = "SELECT s FROM StudentMaster s WHERE s.id = :id"),
    @NamedQuery(name = "StudentMaster.findByName", query = "SELECT s FROM StudentMaster s WHERE s.name = :name"),
    @NamedQuery(name = "StudentMaster.findByMobileNo", query = "SELECT s FROM StudentMaster s WHERE s.mobileNo = :mobileNo"),
    @NamedQuery(name = "StudentMaster.findByEmail", query = "SELECT s FROM StudentMaster s WHERE s.email = :email"),
    @NamedQuery(name = "StudentMaster.findByPassword", query = "SELECT s FROM StudentMaster s WHERE s.password = :password"),
    @NamedQuery(name = "StudentMaster.findByRollNo", query = "SELECT s FROM StudentMaster s WHERE s.rollNo = :rollNo"),
    @NamedQuery(name = "StudentMaster.findByCreatedDate", query = "SELECT s FROM StudentMaster s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "StudentMaster.findByModifiedDate", query = "SELECT s FROM StudentMaster s WHERE s.modifiedDate = :modifiedDate")})
public class StudentMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 15)
    @Column(name = "mobile_no")
    private String mobileNo;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 100)
    @Column(name = "password")
    private String password;
    @Size(max = 50)
    @Column(name = "roll_no")
    private String rollNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @ManyToOne
    private SemesterMaster semesterId;
    @JoinColumn(name = "division_id", referencedColumnName = "id")
    @ManyToOne
    private DivisionMaster divisionId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private UserMaster userId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private UserMaster createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private UserMaster modifiedBy;
    @OneToMany(mappedBy = "studentId")
    private Collection<AttendanceMaster> attendanceMasterCollection;

    @Transient
    private boolean isPresent;

    @Transient
private double attendancePercentage;
    
    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public StudentMaster() {
    }

    public StudentMaster(Integer id) {
        this.id = id;
    }

    public StudentMaster(Integer id, String name, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public SemesterMaster getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(SemesterMaster semesterId) {
        this.semesterId = semesterId;
    }

    public DivisionMaster getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(DivisionMaster divisionId) {
        this.divisionId = divisionId;
    }

    public UserMaster getUserId() {
        return userId;
    }

    public void setUserId(UserMaster userId) {
        this.userId = userId;
    }

    public UserMaster getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserMaster createdBy) {
        this.createdBy = createdBy;
    }

    public UserMaster getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserMaster modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @XmlTransient
    public Collection<AttendanceMaster> getAttendanceMasterCollection() {
        return attendanceMasterCollection;
    }

    public void setAttendanceMasterCollection(Collection<AttendanceMaster> attendanceMasterCollection) {
        this.attendanceMasterCollection = attendanceMasterCollection;
    }
    //
    //    @OneToMany(mappedBy = "semesterId")
    //    @JsonbTransient // આ હોવું જ જોઈએ, નહીંતર 500 Error આવશે
    //    private Collection<StudentMaster> studentMasterCollection;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentMaster)) {
            return false;
        }
        StudentMaster other = (StudentMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.StudentMaster[ id=" + id + " ]";
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
    
    public double getAttendancePercentage() { return attendancePercentage; }
public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }
}
