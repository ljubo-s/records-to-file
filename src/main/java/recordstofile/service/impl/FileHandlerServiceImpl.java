package recordstofile.service.impl;

import static org.apache.poi.ss.util.CellUtil.createCell;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import recordstofile.exception.CustomException;
import recordstofile.service.FileHandlerService;

@Service
public class FileHandlerServiceImpl implements FileHandlerService {

    @Value("${files.directory}")
    String directory;
    @Value("${files.font.name}")
    String fontName;
    @Value("${files.font.size}")
    int fontSize;
    @Value("${files.properties.description.application}")
    String company;
    @Value("${files.properties.description.company}")
    String application;

    private Path foundFile;

    @Override
    public void createHeadersBasedOnResultSet(SXSSFWorkbook workbook, ResultSetMetaData rsMetaData,
        int numberOfColumns) {

        SXSSFSheet sheet = workbook.getSheetAt(0);
        SXSSFRow row = sheet.createRow(0);

        List<?> headerList = getHeadersFromResultSet(rsMetaData);

        for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            createCell(row, columnIndex, String.valueOf(headerList.get(columnIndex)));
        }

        setHeaderStyle(workbook, row, numberOfColumns);
    }

    void setHeaderStyle(SXSSFWorkbook workbook, SXSSFRow row, int numberOfColumns) {

        row.setHeight((short) (row.getHeight() * 2));

        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setBold(true);
        font.setFontHeight((short) (fontSize * 20));

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.DOUBLE);
        style.setBottomBorderColor(IndexedColors.DARK_RED.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        SXSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 0; i < numberOfColumns; i++) {
            row.getCell(i).setCellStyle(style);
            workbook.getSheetAt(0).setColumnWidth(i, 7000);
        }

        //filter on columns
        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, numberOfColumns - 1));
        sheet.createFreezePane(0, 1);
    }

    public List<String> getHeadersFromResultSet(ResultSetMetaData rsMetaData) {

        List<String> headerList = new ArrayList<>();

        try {

            int numberOfColumns = rsMetaData.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                headerList.add(rsMetaData.getColumnName(i));
            }

            return headerList;

        } catch (SQLException ex) {
            throw new CustomException("getHeadersFromResultSet, SQLException", ex);
        }
    }

    @Override
    public String writeToExcel(SXSSFWorkbook workbook) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        String fileName = "file-" + dateFormat.format(System.currentTimeMillis()) + ".xlsx";

        try (FileOutputStream out = new FileOutputStream(directory + fileName)) {

            POIXMLProperties xmlProps = workbook.getXSSFWorkbook().getProperties();
            setFileDetails(xmlProps);

            workbook.write(out);

            out.flush();
            out.close();
            workbook.dispose();

            return fileName;

        } catch (Exception ex) {
            throw new CustomException("writeToExcel, Exception", ex);
        }
    }

    public void setFileDetails(POIXMLProperties pOIXMLProperties) {

        POIXMLProperties.CoreProperties coreProps = pOIXMLProperties.getCoreProperties();
        POIXMLProperties.ExtendedProperties extendedProperties = pOIXMLProperties.getExtendedProperties();

        //Description
        coreProps.setTitle("This goes to Title {report title}");
        coreProps.setSubjectProperty("This goes to Subject property");
        coreProps.setCategory("This goes to Category {report category}");

        //Comments
        coreProps.setDescription("This goes to Description");

        //Origin
        coreProps.setCreator("This goes to Author{user}");
        extendedProperties.setApplication(application);
        extendedProperties.setCompany(company);

        //Content
        coreProps.setContentStatus("This goes to Content status");
    }

    @Override
    public CellStyle crateStyleForDataCells(SXSSFWorkbook workbook) {

        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeight((short) (fontSize * 20));

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(false);

        style.setFont(font);

        return style;
    }

    @Override
    public Optional<Resource> getFileAsResource(String fileName) {

        if (fileName != null && !fileName.isEmpty()) {

            Path dirPath = Paths.get(directory);

            try (Stream<Path> walk = Files.list(dirPath)) {

                walk.forEach(file -> {

                    if (file.getFileName().toString().equals(fileName)) {
                        foundFile = file;
                    }
                });

            } catch (IOException ex) {
                throw new CustomException("getFileAsResource, IOException", ex);
            }

            try {

                if (foundFile != null) {
                    UrlResource urlResource = new UrlResource(foundFile.toUri());
                    foundFile = null;

                    return Optional.of(urlResource);
                }

            } catch (MalformedURLException ex) {
                throw new CustomException("getFileAsResource, MalformedURLException", ex);
            }
        }

        return Optional.empty();
    }

    @Override
    public String fileSize(String fileName) {

        try {

            Path path = Paths.get(directory + fileName);
            long bytes = Files.size(path);

            return bytes / 1024 + " KB";

        } catch (IOException ex) {
            throw new CustomException("fileSize, IOException", ex);
        }
    }

    @Override
    public List<String> getFileNamesFromDirectory() {

        try (Stream<Path> stream = Files.walk(Paths.get(directory))) {

            return stream.filter(file -> !Files.isDirectory(file))
                         .map(Path::getFileName)
                         .map(Path::toString)
                         .toList();

        } catch (IOException ex) {
            throw new CustomException("listFiles, IOException", ex);
        }
    }

}
