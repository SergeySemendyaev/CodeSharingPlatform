package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeSnippet {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "platform.IdGenerator")
    @Column(columnDefinition = "varchar(36)")
    @JsonIgnore
    private String id;
    @Column(columnDefinition = "varchar(8000)")
    private String code;
    @Column
    private String date;
    @JsonIgnore
    @Column
    private String expiryDate;
    @Column
    private long time;
    @Column
    private int views;
    @JsonIgnore
    @Column
    private boolean timeRestricted;
    @JsonIgnore
    @Column
    private boolean viewsRestricted;

    @JsonIgnore
    public boolean isRestricted() {
        return timeRestricted || viewsRestricted;
    }
}
