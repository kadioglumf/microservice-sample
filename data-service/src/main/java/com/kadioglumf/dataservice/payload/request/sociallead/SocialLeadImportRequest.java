package com.kadioglumf.dataservice.payload.request.sociallead;

import com.kadioglumf.dataservice.reader.annotations.csv.CsvColumn;
import com.kadioglumf.dataservice.reader.annotations.csv.ImportCsvSettings;
import com.kadioglumf.dataservice.reader.annotations.excel.ExcelColumn;
import com.kadioglumf.dataservice.reader.annotations.excel.ImportExcelSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ImportCsvSettings
@ImportExcelSettings
public class SocialLeadImportRequest {
  @CsvColumn(columnName = "\uFEFFerstellt")
  @ExcelColumn(columnName = "\uFEFFerstellt")
  private String creationDateOfValue;

  @CsvColumn(columnName = "name")
  @ExcelColumn(columnName = "name")
  private String name;

  @CsvColumn(columnName = "e-mail")
  @ExcelColumn(columnName = "e-mail")
  private String email;

  @CsvColumn(columnName = "telefonnummer (empfohlen)")
  @ExcelColumn(columnName = "telefonnummer (empfohlen)")
  private String phone;

  @CsvColumn(columnName = "Formular")
  @ExcelColumn(columnName = "Formular")
  private String formular;
}
