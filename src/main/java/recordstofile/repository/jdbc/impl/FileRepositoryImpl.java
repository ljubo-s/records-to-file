package recordstofile.repository.jdbc.impl;

import static org.apache.poi.ss.util.CellUtil.createCell;

import java.sql.ResultSetMetaData;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import recordstofile.dto.FileDto;
import recordstofile.exception.CustomException;
import recordstofile.repository.jdbc.FileRepository;
import recordstofile.service.FileHandlerService;

@Repository
public class FileRepositoryImpl implements FileRepository {

    private final NamedParameterJdbcTemplate namedParamJdbcTemplate;
    private final FileHandlerService fileHandlerService;

    @Autowired
    public FileRepositoryImpl(
        @Qualifier("NamedParameterJdbcTemplate") NamedParameterJdbcTemplate namedParamJdbcTemplate,
        FileHandlerService fileHandlerService) {
        this.namedParamJdbcTemplate = namedParamJdbcTemplate;
        this.fileHandlerService = fileHandlerService;
    }

    @Override
    public Optional<FileDto> recordsToFile(String sql, SqlParameterSource parameters) {

        FileDto fileDto = new FileDto();

        this.namedParamJdbcTemplate.query(sql, parameters, resultSet -> {

            try (SXSSFWorkbook workbook = new SXSSFWorkbook(1)) {

                SXSSFSheet sheet = workbook.createSheet("sheetName");
                SXSSFRow row;
                int rowCount = 0;
                ResultSetMetaData rsMetaData = resultSet.getMetaData();
                int columnCount = rsMetaData.getColumnCount();

                fileHandlerService.createHeadersBasedOnResultSet(workbook, rsMetaData, columnCount);

                CellStyle style = fileHandlerService.crateStyleForDataCells(workbook);
                Instant queryStart = Instant.now();

                do {

                    rowCount++;
                    row = sheet.createRow(rowCount);

                    for (int rsIndex = 1, columnIndex = 0; columnIndex < columnCount; rsIndex++, columnIndex++) {
                        createCell(row, columnIndex, resultSet.getString(rsIndex)).setCellStyle(style);
                    }

                } while (resultSet.next());

                long queryDuration = Duration.between(queryStart, Instant.now()).toMillis();
                Instant fileCreatingStart = Instant.now();

                String fileName = fileHandlerService.writeToExcel(workbook);

                long fileCreationDuration = Duration.between(fileCreatingStart, Instant.now()).toMillis();

                fileDto.setRowCount(rowCount);
                fileDto.setColumnCount(columnCount);
                fileDto.setFileName(fileName);
                fileDto.setQueryDuration(queryDuration / 1000.0 + " sec");
                fileDto.setFileCreatingDuration(fileCreationDuration / 1000.0 + " sec");

            } catch (Exception ex) {
                throw new CustomException(ex);
            }
        });

        if (fileDto.getRowCount() > 0) {
            return Optional.of(fileDto);
        } else {
            return Optional.empty();
        }
    }

}
