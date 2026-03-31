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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "semester_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SemesterMaster.findAll", query = "SELECT s FROM SemesterMaster s"),
    @NamedQuery(name = "SemesterMaster.findById", query = "SELECT s FROM SemesterMaster s WHERE s.id = :id"),
    @NamedQuery(name = "SemesterMaster.findBySemesterNo", query = "SELECT s FROM SemesterMaster s WHERE s.semesterNo = :semesterNo"),
    @NamedQuery(name = "SemesterMaster.findByCreatedDate", query = "SELECT s FROM SemesterMaster s WHERE s.createdDate = :createdDate")
})
public class SemesterMaster implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "semester_no")
    private int semesterNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @OneToMany(mappedBy = "semesterId")
    private Collection<StudentMaster> studentMasterCollection;
    @OneToMany(mappedBy = "semesterId")
    private Collection<ClassMaster> classMasterCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
   
    @OneToMany(mappedBy = "semesterId")
     @JsonbTransient
    private Collection<SubjectMaster> subjectMasterCollection;

    @JsonbTransient
    public Collection<SubjectMaster> getSubjectMasterCollection() {
        return subjectMasterCollection;
    }

    public void setSubjectMasterCollection(Collection<SubjectMaster> subjectMasterCollection) {
        this.subjectMasterCollection = subjectMasterCollection;
    }

    public SemesterMaster() {
    }

    public SemesterMaster(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSemesterNo() {
        return semesterNo;
    }

    public void setSemesterNo(int semesterNo) {
        this.semesterNo = semesterNo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
        if (!(object instanceof SemesterMaster)) {
            return false;
        }
        SemesterMaster other = (SemesterMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.SemesterMaster[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<StudentMaster> getStudentMasterCollection() {
        return studentMasterCollection;
    }

    public void setStudentMasterCollection(Collection<StudentMaster> studentMasterCollection) {
        this.studentMasterCollection = studentMasterCollection;
    }

    @XmlTransient
    public Collection<ClassMaster> getClassMasterCollection() {
        return classMasterCollection;
    }

    public void setClassMasterCollection(Collection<ClassMaster> classMasterCollection) {
        this.classMasterCollection = classMasterCollection;
    }

}
