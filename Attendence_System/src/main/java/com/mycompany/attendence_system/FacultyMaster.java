/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.attendence_system;

import jakarta.json.bind.annotation.JsonbTransient;
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
@Table(name = "faculty_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacultyMaster.findAll", query = "SELECT f FROM FacultyMaster f"),
    @NamedQuery(name = "FacultyMaster.findById", query = "SELECT f FROM FacultyMaster f WHERE f.id = :id"),
    @NamedQuery(name = "FacultyMaster.findByName", query = "SELECT f FROM FacultyMaster f WHERE f.name = :name"),
    @NamedQuery(name = "FacultyMaster.findByMobileNo", query = "SELECT f FROM FacultyMaster f WHERE f.mobileNo = :mobileNo"),
    @NamedQuery(name = "FacultyMaster.findByEmail", query = "SELECT f FROM FacultyMaster f WHERE f.email = :email"),
    @NamedQuery(name = "FacultyMaster.findByPassword", query = "SELECT f FROM FacultyMaster f WHERE f.password = :password"),
    @NamedQuery(name = "FacultyMaster.findByFacultyId", query = "SELECT f FROM FacultyMaster f WHERE f.facultyId = :facultyId"),
    @NamedQuery(name = "FacultyMaster.findByCreatedBy", query = "SELECT f FROM FacultyMaster f WHERE f.createdBy = :createdBy"),
    @NamedQuery(name = "FacultyMaster.findByCreatedDate", query = "SELECT f FROM FacultyMaster f WHERE f.createdDate = :createdDate"),
    @NamedQuery(name = "FacultyMaster.findByModifiedBy", query = "SELECT f FROM FacultyMaster f WHERE f.modifiedBy = :modifiedBy"),
    @NamedQuery(name = "FacultyMaster.findByModifiedDate", query = "SELECT f FROM FacultyMaster f WHERE f.modifiedDate = :modifiedDate")})
public class FacultyMaster implements Serializable {

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
    @Column(name = "faculty_id")
    private Integer facultyId;
    @Column(name = "created_by")
    private Integer createdBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)

    private SubjectMaster subjectId;

    //  User 
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER) // EAGER add
// @JsonbTransient  
    private UserMaster userId;
    @OneToMany(mappedBy = "facultyId")
    @JsonbTransient
    private Collection<AttendanceMaster> attendanceMasterCollection;
    @OneToMany(mappedBy = "facultyId")
    @JsonbTransient
    private Collection<ClassMaster> classMasterCollection;

    public FacultyMaster() {
    }

    public FacultyMaster(Integer id) {
        this.id = id;
    }

    public FacultyMaster(Integer id, String name, Date createdDate, Date modifiedDate) {
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

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public SubjectMaster getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(SubjectMaster subjectId) {
        this.subjectId = subjectId;
    }

    public UserMaster getUserId() {
        return userId;
    }

    public void setUserId(UserMaster userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<AttendanceMaster> getAttendanceMasterCollection() {
        return attendanceMasterCollection;
    }

    public void setAttendanceMasterCollection(Collection<AttendanceMaster> attendanceMasterCollection) {
        this.attendanceMasterCollection = attendanceMasterCollection;
    }

    @XmlTransient
    public Collection<ClassMaster> getClassMasterCollection() {
        return classMasterCollection;
    }

    public void setClassMasterCollection(Collection<ClassMaster> classMasterCollection) {
        this.classMasterCollection = classMasterCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacultyMaster)) {
            return false;
        }
        FacultyMaster other = (FacultyMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.FacultyMaster[ id=" + id + " ]";
    }
    
    
    

}
