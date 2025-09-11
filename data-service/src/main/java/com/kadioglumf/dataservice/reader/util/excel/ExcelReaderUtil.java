package com.kadioglumf.dataservice.reader.util.excel;

import com.kadioglumf.dataservice.reader.annotations.excel.ExcelColumn;
import com.kadioglumf.dataservice.reader.annotations.excel.ImportExcelSettings;
import com.kadioglumf.dataservice.reader.enums.FileExtension;
import com.kadioglumf.dataservice.reader.util.BaseReaderUtils;
import com.kadioglumf.dataservice.reader.util.DateUtils;
import com.kadioglumf.dataservice.reader.util.NumberUtils;
import com.kadioglumf.dataservice.reader.util.ReflectionUtil;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.i18n.LocaleContextHolder;

@Getter
@Setter
public final class ExcelReaderUtil extends BaseReaderUtils {

  private final Workbook workbook;
  private final FormulaEvaluator formulaEvaluator;
  private final DataFormatter dataFormatter;

  public ExcelReaderUtil(InputStream inputStream, FileExtension fileExtension) throws IOException {
    if (FileExtension.XLS.equals(fileExtension)) {
      this.workbook = new HSSFWorkbook(inputStream);
      this.formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) this.workbook);
    } else {
      this.workbook = new XSSFWorkbook(inputStream);
      this.formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) this.workbook);
    }
    this.dataFormatter = new DataFormatter(LocaleContextHolder.getLocale());
  }

  public <T> List<T> read(Class<T> clazz) throws Exception {
    List<T> list = new ArrayList<>();

    ImportExcelSettings excelSettings = clazz.getAnnotation(ImportExcelSettings.class);
    if (excelSettings == null) {
      throw new RuntimeException("");
    }

    /*for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        //TODO sadece ilk sheet okunacak. diğer sheetlerin de okunması için for ile dönülmesi lazım.
        //TODO ikinci sheet komple boş olduğunda haa alınıyor.
    }*/

    Sheet sheet = workbook.getSheetAt(0);
    Iterator<Row> rowIterator = sheet.rowIterator();

    Map<String, Integer> headers = new CaseInsensitiveMap<>();
    boolean isFirstRowHeader = excelSettings.isFirstRowHeader();
    if (isFirstRowHeader && rowIterator.hasNext()) {

      Row row = rowIterator.next();
      Iterator<Cell> cellIterator = row.cellIterator();

      while (cellIterator.hasNext()) {
        Cell cell = cellIterator.next();
        headers.put(cell.getStringCellValue(), cell.getColumnIndex());
      }
    }

    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      addRow(clazz, list, row, headers, isFirstRowHeader);
    }
    return list;
  }

  private <T> void addRow(
      Class<T> clazz, List<T> list, Row row, Map<String, Integer> headers, boolean isFirstRowHeader)
      throws Exception {
    T instance = clazz.getDeclaredConstructor().newInstance();
    List<Field> fields = ReflectionUtil.getSortedFields(instance.getClass(), ExcelColumn.class);
    boolean isAllCellsEmpty = true;
    for (Field field : fields) {
      ExcelColumn ec = field.getAnnotation(ExcelColumn.class);
      if (ec != null) {
        Cell cell = row.getCell(getColumnIndex(ec, headers, isFirstRowHeader));
        if (cell != null) {
          Object value = getCellValue(cell, field);
          if (value != null) {
            isAllCellsEmpty = false;
            field.setAccessible(true);
            field.set(instance, value);
          }
        }
      }
    }

    if (!isAllCellsEmpty) {
      list.add(instance);
    }
  }

  private Object getCellValue(Cell cell, Field field) throws Exception {
    String cellValue = this.dataFormatter.formatCellValue(cell, formulaEvaluator);
    if (StringUtils.isBlank(cellValue)) {
      return null;
    }
    switch (cell.getCellType()) {
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell, null)) {
          return DateUtils.parseStringToDateFormatted(field.getType(), cellValue);
        } else if (isCellNumericFormatted(field.getType())) {
          return NumberUtils.parseNumericValue(cellValue, field.getType());
        } else if (String.class.isAssignableFrom(field.getType())) {
          return cellValue;
        }
        return cell.getNumericCellValue();
      case STRING:
      case FORMULA:
        if (isCellNumericFormatted(field.getType())) {
          return NumberUtils.parseNumericValue(cellValue, field.getType());
        } else if (isCellDateFormatted(field.getType())) {
          return DateUtils.parseStringToDateFormatted(field.getType(), cellValue);
        } else if (Boolean.class.isAssignableFrom(field.getType())) {
          return Boolean.valueOf(cellValue);
        }

        return cell.getRichStringCellValue().getString();
      case BOOLEAN:
        return cell.getBooleanCellValue();
      case BLANK:
        return null;
      case ERROR:
        return FormulaError.forInt(cell.getErrorCellValue()).getString();
      default:
        throw new RuntimeException("Unexpected celltype (" + cell.getCellType() + ")");
    }
  }

  private boolean isCellNumericFormatted(Class<?> type) {
    return Number.class.isAssignableFrom(type)
        || type == int.class
        || type == double.class
        || type == float.class
        || type == long.class
        || type == short.class
        || type == byte.class;
  }

  private boolean isCellDateFormatted(Class<?> type) {
    return Temporal.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type);
  }

  private int getColumnIndex(
      ExcelColumn csvColumn, Map<String, Integer> headers, boolean isFirstRowHeader) {
    if (csvColumn.columnIndex() != -1) {
      return csvColumn.columnIndex();
    }
    if (headers == null
        || StringUtils.isBlank(csvColumn.columnName())
        || headers.get(csvColumn.columnName()) == null
        || !isFirstRowHeader) {
      throw new RuntimeException("cannot have both column name and column order!");
    }
    return headers.get(csvColumn.columnName());
  }
}
