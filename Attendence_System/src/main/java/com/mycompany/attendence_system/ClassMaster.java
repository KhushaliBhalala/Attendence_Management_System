/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "class_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClassMaster.findAll", query = "SELECT c FROM ClassMaster c"),
    @NamedQuery(name = "ClassMaster.findById", query = "SELECT c FROM ClassMaster c WHERE c.id = :id"),
    @NamedQuery(name = "ClassMaster.findBySubjectId", query = "SELECT c FROM ClassMaster c WHERE c.subjectId = :subjectId"),
    @NamedQuery(name = "ClassMaster.findByTotalLectures", query = "SELECT c FROM ClassMaster c WHERE c.totalLectures = :totalLectures"),
    @NamedQuery(name = "ClassMaster.findByCreatedBy", query = "SELECT c FROM ClassMaster c WHERE c.createdBy = :createdBy"),
    @NamedQuery(name = "ClassMaster.findByCreatedDate", query = "SELECT c FROM ClassMaster c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "ClassMaster.findByModifiedBy", query = "SELECT c FROM ClassMaster c WHERE c.modifiedBy = :modifiedBy"),
    @NamedQuery(name = "ClassMaster.findByModifiedDate", query = "SELECT c FROM ClassMaster c WHERE c.modifiedDate = :modifiedDate")})
public class ClassMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
//    @Column(name = "subject_id")
//    private Integer subjectId;
    @Column(name = "total_lectures")
    private Integer totalLectures;
    @Column(name = "created_by")
    private Integer createdBy;
    @Basic(optional = false)

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Basic(optional = false)

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne
    private SubjectMaster subjectId; // Integer ની જગ્યાએ SubjectMaster ઓબ્જેક્ટ
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    @ManyToOne
    private FacultyMaster facultyId;
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @ManyToOne
    private SemesterMaster semesterId;
    @JoinColumn(name = "division_id", referencedColumnName = "id")
    @ManyToOne
    private DivisionMaster divisionId;

    public ClassMaster() {
    }

    public ClassMaster(Integer id) {
        this.id = id;
    }

    public ClassMaster(Integer id, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SubjectMaster getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(SubjectMaster subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getTotalLectures() {
        return totalLectures;
    }

    public void setTotalLectures(Integer totalLectures) {
        this.totalLectures = totalLectures;
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

    public FacultyMaster getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(FacultyMaster facultyId) {
        this.facultyId = facultyId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClassMaster)) {
            return false;
        }
        ClassMaster other = (ClassMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.ClassMaster[ id=" + id + " ]";
    }

}