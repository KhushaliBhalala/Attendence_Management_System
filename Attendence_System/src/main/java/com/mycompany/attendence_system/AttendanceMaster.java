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
@Table(name = "attendance_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AttendanceMaster.findAll", query = "SELECT a FROM AttendanceMaster a"),
    @NamedQuery(name = "AttendanceMaster.findById", query = "SELECT a FROM AttendanceMaster a WHERE a.id = :id"),
    @NamedQuery(name = "AttendanceMaster.findByAttendanceDate", query = "SELECT a FROM AttendanceMaster a WHERE a.attendanceDate = :attendanceDate"),
    @NamedQuery(name = "AttendanceMaster.findByStatus", query = "SELECT a FROM AttendanceMaster a WHERE a.status = :status"),
    @NamedQuery(name = "AttendanceMaster.findByCreatedBy", query = "SELECT a FROM AttendanceMaster a WHERE a.createdBy = :createdBy"),
    @NamedQuery(name = "AttendanceMaster.findByCreatedDate", query = "SELECT a FROM AttendanceMaster a WHERE a.createdDate = :createdDate"),
    @NamedQuery(name = "AttendanceMaster.findByModifiedBy", query = "SELECT a FROM AttendanceMaster a WHERE a.modifiedBy = :modifiedBy"),
    @NamedQuery(name = "AttendanceMaster.findByModifiedDate", query = "SELECT a FROM AttendanceMaster a WHERE a.modifiedDate = :modifiedDate")})
public class AttendanceMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "attendance_date")
    @Temporal(TemporalType.DATE)
    private Date attendanceDate;
    @Column(name = "status")
    private Character status;
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
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private StudentMaster studentId;
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne
    private SubjectMaster subjectId;
    @JoinColumn(name = "faculty_id", referencedColumnName = "id")
    @ManyToOne
    private FacultyMaster facultyId;

    public AttendanceMaster() {
    }

    public AttendanceMaster(Integer id) {
        this.id = id;
    }

    public AttendanceMaster(Integer id, Date createdDate, Date modifiedDate) {
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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
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

    public StudentMaster getStudentId() {
        return studentId;
    }

    public void setStudentId(StudentMaster studentId) {
        this.studentId = studentId;
    }

    public SubjectMaster getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(SubjectMaster subjectId) {
        this.subjectId = subjectId;
    }

    public FacultyMaster getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(FacultyMaster facultyId) {
        this.facultyId = facultyId;
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
        if (!(object instanceof AttendanceMaster)) {
            return false;
        }
        AttendanceMaster other = (AttendanceMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.AttendanceMaster[ id=" + id + " ]";
    }
    
}
