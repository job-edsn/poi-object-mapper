package io.github.millij.poi.ss.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.millij.poi.ss.model.CellType;
import io.github.millij.poi.util.CellData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.millij.poi.ss.model.Column;
import io.github.millij.poi.util.Spreadsheet;


/**
 * Abstract Implementation of {@link SpreadsheetWriter}
 * 
 * @since 3.0
 */
abstract class AbstractSpreadsheetWriter implements SpreadsheetWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpreadsheetWriter.class);


    protected final Workbook workbook;


    // Constructors
    // ------------------------------------------------------------------------

    public AbstractSpreadsheetWriter(final Workbook workbook) {
        super();

        // init
        this.workbook = workbook;
    }


    // Methods
    // ------------------------------------------------------------------------


    // Sheet :: Add

    @Override
    public <T> void addSheet(final Class<T> beanType, final List<T> rowObjects, final String inSheetName,
            final List<String> inHeaders) {
        // Sanity checks
        if (Objects.isNull(beanType)) {
            throw new IllegalArgumentException("AbstractSpreadsheetWriter :: Bean Type is NULL");
        }

        // Sheet config
        final String defaultSheetName = Spreadsheet.getSheetName(beanType);
        final List<String> defaultHeaders = this.getColumnNames(beanType);

        // output config
        final String sheetName = Objects.isNull(inSheetName) ? defaultSheetName : inSheetName;
        final List<String> headers = Objects.isNull(inHeaders) || inHeaders.isEmpty() ? defaultHeaders : inHeaders;

        try {
            final Sheet exSheet = workbook.getSheet(sheetName);
            if (Objects.nonNull(exSheet)) {
                String errMsg = String.format("A Sheet with the passed name already exists : %s", sheetName);
                throw new IllegalArgumentException(errMsg);
            }

            // Create sheet
            final Sheet sheet = Objects.isNull(sheetName) || sheetName.isBlank() //
                    ? workbook.createSheet() //
                    : workbook.createSheet(sheetName);
            LOGGER.debug("Added new Sheet[name] to the workbook : {}", sheet.getSheetName());

            // Header
            final Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
            }

            // Data Rows
            final Map<String, List<CellData>> rowsData = this.prepareSheetRowsData(headers, rowObjects);
            for (int i = 0, rowNum = 1; i < rowObjects.size(); i++, rowNum++) {
                final Row row = sheet.createRow(rowNum);

                int cellNo = 0;
                for (String key : rowsData.keySet()) {
                    Cell cell = row.createCell(cellNo);
                    CellData cellData = rowsData.get(key).get(i);
                    if(cellData.getCellType() == CellType.NUMERIC) {
                        cell.setCellValue(((Number)cellData.getValue()).doubleValue());
                    } else {
                        cell.setCellValue((String) cellData.getValue());
                    }
                    cellNo++;
                }
            }

        } catch (Exception ex) {
            String errMsg = String.format("Error while preparing sheet with passed row objects : %s", ex.getMessage());
            LOGGER.error(errMsg, ex);
        }
    }


    // Sheet :: Append to existing


    // Write

    @Override
    public void write(final String filepath) throws IOException {
        try (final OutputStream outputStrem = new FileOutputStream(new File(filepath))) {
            workbook.write(outputStrem);
            workbook.close();
        } catch (Exception ex) {
            final String errMsg = String.format("Failed to write workbook data to file : %s", filepath);
            LOGGER.error(errMsg);
            throw ex;
        }
    }


    // Private Methods
    // ------------------------------------------------------------------------

    private <T> Map<String, List<CellData>> prepareSheetRowsData(List<String> headers, List<T> rowObjects)
            throws Exception {
        // Sheet data
        final Map<String, List<CellData>> sheetData = new LinkedHashMap<>();

        // Iterate over Objects
        for (final T rowObj : rowObjects) {
            final Map<String, CellData> row = Spreadsheet.asRowDataMap(rowObj, headers);
            for (final String header : headers) {
                final List<CellData> data = sheetData.getOrDefault(header, new ArrayList<>());
                final CellData value = row.getOrDefault(header, new CellData("", CellType.STRING));

                data.add(value);
                sheetData.put(header, data);
            }
        }

        return sheetData;
    }

    private List<String> getColumnNames(Class<?> beanType) {
        // Bean Property to Column Mapping
        final Map<String, Column> propToColumnMap = Spreadsheet.getPropertyToColumnDefMap(beanType);
        final List<Column> colums = new ArrayList<>(propToColumnMap.values());
        Collections.sort(colums);

        final List<String> columnNames = colums.stream().map(Column::getName).collect(Collectors.toList());
        return columnNames;
    }


}
