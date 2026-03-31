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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "attendence_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AttendenceReport.findAll", query = "SELECT a FROM AttendenceReport a"),
    @NamedQuery(name = "AttendenceReport.findById", query = "SELECT a FROM AttendenceReport a WHERE a.id = :id"),
    @NamedQuery(name = "AttendenceReport.findByTotalPresent", query = "SELECT a FROM AttendenceReport a WHERE a.totalPresent = :totalPresent"),
    @NamedQuery(name = "AttendenceReport.findByTotalAbsent", query = "SELECT a FROM AttendenceReport a WHERE a.totalAbsent = :totalAbsent")})
public class AttendenceReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "total_present")
    private Integer totalPresent;
    @Column(name = "total_absent")
    private Integer totalAbsent;
    @JoinColumn(name = "attendence_id", referencedColumnName = "id")
    @ManyToOne
    private AttendanceMaster attendenceId;
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    @ManyToOne
    private ClassMaster classId;

    public AttendenceReport() {
    }

    public AttendenceReport(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(Integer totalPresent) {
        this.totalPresent = totalPresent;
    }

    public Integer getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(Integer totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public AttendanceMaster getAttendenceId() {
        return attendenceId;
    }

    public void setAttendenceId(AttendanceMaster attendenceId) {
        this.attendenceId = attendenceId;
    }

    public ClassMaster getClassId() {
        return classId;
    }

    public void setClassId(ClassMaster classId) {
        this.classId = classId;
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
        if (!(object instanceof AttendenceReport)) {
            return false;
        }
        AttendenceReport other = (AttendenceReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.AttendenceReport[ id=" + id + " ]";
    }
    
}
