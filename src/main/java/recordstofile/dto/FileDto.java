package recordstofile.dto;

import lombok.Data;

@Data
public class FileDto {

    private int rowCount;
    private int columnCount;
    private String fileName;
    private String fileSize;
    private String queryDuration;
    private String fileCreatingDuration;

}
