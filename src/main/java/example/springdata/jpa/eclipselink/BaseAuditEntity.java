package example.springdata.jpa.eclipselink;



import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Handles Hibernate versioning (for optimistic locking) and audit fields used
 * by Spring JPA
 *
 * @author Christopher M. Jansen
 * @see oxm.xml - this file is required in order to use this option.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditEntity implements Serializable {

    private static final long serialVersionUID = -930617752434886500L;

    @Version
    @Column(name = "version")
    private int version;

    // Audit fields
    @CreatedBy
    private String createdBy;

    @javax.persistence.Convert(converter = JodaDateTimeConverter.class)
    @CreatedDate
    private DateTime createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @javax.persistence.Convert(converter = JodaDateTimeConverter.class)
    @LastModifiedDate
    private DateTime lastModifiedDate;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
