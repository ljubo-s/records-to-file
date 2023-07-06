package recordstofile.service;

import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.Resource;

public interface FileHandlerService {

    void createHeadersBasedOnResultSet(SXSSFWorkbook workbook, ResultSetMetaData rsMetaData, int numberOfColumns);

    String writeToExcel(SXSSFWorkbook workbook);

    CellStyle crateStyleForDataCells(SXSSFWorkbook workbook);

    Optional<Resource> getFileAsResource(String fileName);

    String fileSize(String fileName);

    List<String> getFileNamesFromDirectory();

}
