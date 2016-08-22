package example.springdata.jpa.eclipselink;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;


/**
 * Converts between domain object (Joda DateTime) and databases Timestamp
 *
 * @author Christopher M. Jansen
 */
@Converter(autoApply = true)
public class JodaDateTimeConverter implements AttributeConverter<DateTime, Timestamp> {
    private static final long serialVersionUID = 1L;

    @Override
    public Timestamp convertToDatabaseColumn(DateTime attribute) {
        if (attribute == null) {
            return null;
        } else {
            return new Timestamp(attribute.getMillis());
        }
    }

    @Override
    public DateTime convertToEntityAttribute(Timestamp dbData) {
        if (dbData == null) {
            return null;
        } else {
            return new DateTime(dbData.getTime());
        }
    }
}
