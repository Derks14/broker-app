package invoice.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

public class BaseDocument {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID )
    @Indexed
    protected String id;

    @Version
    protected Long version;

    @CreatedDate
    @Field("_created")
    @Indexed
    protected LocalDateTime created;

    @LastModifiedDate
    @Field("_modified")
    @Indexed
    protected LocalDateTime modified;

    @Transient
    public ObjectId oid() {
        return new ObjectId(this.id);
    }
}
