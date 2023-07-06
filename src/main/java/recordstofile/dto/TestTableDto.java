package recordstofile.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class TestTableDto implements Serializable {

    private Long id;
    private String name;
    private String surname;
    private String col_1;
    private String col_2;
    private String col_3;
    private String col_4;
    private String col_5;
    private String col_6;
    private Timestamp insertDate;
    private Timestamp updateDate;

}