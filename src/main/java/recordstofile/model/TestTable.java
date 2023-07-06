package recordstofile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Table(name = "TEST_TABLE")
@Entity
public class TestTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 30)
    private String name;

    @Size(max = 50)
    @Column(name = "SURNAME", length = 50)
    private String surname;

    @Size(max = 30)
    @Column(name = "COL_1", length = 30)
    private String col_1;

    @Size(max = 30)
    @Column(name = "COL_2", length = 30)
    private String col_2;

    @Size(max = 30)
    @Column(name = "COL_3", length = 30)
    private String col_3;

    @Size(max = 30)
    @Column(name = "COL_4", length = 30)
    private String col_4;

    @Size(max = 30)
    @Column(name = "COL_5", length = 30)
    private String col_5;

    @Size(max = 30)
    @Column(name = "COL_6", length = 30)
    private String col_6;

    @Column(name = "INSERT_DATE", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp insertDate;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;

}
