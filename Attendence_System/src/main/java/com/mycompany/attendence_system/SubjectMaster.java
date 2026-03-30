
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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "subject_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubjectMaster.findAll", query = "SELECT s FROM SubjectMaster s"),
    @NamedQuery(name = "SubjectMaster.findById", query = "SELECT s FROM SubjectMaster s WHERE s.id = :id"),
    @NamedQuery(name = "SubjectMaster.findBySubjectName", query = "SELECT s FROM SubjectMaster s WHERE s.subjectName = :subjectName"),
    @NamedQuery(name = "SubjectMaster.findBySubjectCode", query = "SELECT s FROM SubjectMaster s WHERE s.subjectCode = :subjectCode"),
    @NamedQuery(name = "SubjectMaster.findByCreatedDate", query = "SELECT s FROM SubjectMaster s WHERE s.createdDate = :createdDate")
   })
public class SubjectMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "subject_name")
    private String subjectName;
    @Size(max = 20)
    @Column(name = "subject_code")
    private String subjectCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "semester_id", referencedColumnName = "id")
    @ManyToOne
    private SemesterMaster semesterId;

    public SubjectMaster() {
    }

    public SubjectMaster(Integer id) {
        this.id = id;
    }

    public SubjectMaster(Integer id, String subjectName, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.subjectName = subjectName;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

   
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

  

   

    public SemesterMaster getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(SemesterMaster semesterId) {
        this.semesterId = semesterId;
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
        if (!(object instanceof SubjectMaster)) {
            return false;
        }
        SubjectMaster other = (SubjectMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.SubjectMaster[ id=" + id + " ]";
    }
    
}
