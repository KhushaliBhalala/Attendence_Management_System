package com.mycompany.attendence_system;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "attendence_report")
@XmlRootElement
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

    // Relationships
    @JoinColumn(name = "attendence_id", referencedColumnName = "id")
    @ManyToOne
    private AttendanceMaster attendenceId;

    @JoinColumn(name = "class_id", referencedColumnName = "id")
    @ManyToOne
    private ClassMaster classId;

    public AttendenceReport() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTotalPresent() { return totalPresent; }
    public void setTotalPresent(Integer totalPresent) { this.totalPresent = totalPresent; }

    public Integer getTotalAbsent() { return totalAbsent; }
    public void setTotalAbsent(Integer totalAbsent) { this.totalAbsent = totalAbsent; }

    public AttendanceMaster getAttendenceId() { return attendenceId; }
    public void setAttendenceId(AttendanceMaster attendenceId) { this.attendenceId = attendenceId; }

    public ClassMaster getClassId() { return classId; }
    public void setClassId(ClassMaster classId) { this.classId = classId; }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AttendenceReport)) return false;
        AttendenceReport other = (AttendenceReport) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.mycompany.attendence_system.AttendenceReport[ id=" + id + " ]";
    }
}