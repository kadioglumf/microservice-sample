package com.kadioglumf.dataservice.reader.service;

import com.kadioglumf.dataservice.reader.enums.FileExtension;
import com.kadioglumf.dataservice.reader.util.csv.CsvReaderUtils;
import com.kadioglumf.dataservice.reader.util.excel.ExcelReaderUtil;
import java.io.InputStream;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ReaderService {

  public <T> List<T> readFile(
      @NonNull Class<T> clazz, @NonNull InputStream inputStream, @NonNull String fileExtension)
      throws Exception {
    if (FileExtension.XLS.getValue().equals(fileExtension)
        || FileExtension.XLSX.getValue().equals(fileExtension)) {
      ExcelReaderUtil readerUtil =
          new ExcelReaderUtil(inputStream, FileExtension.getFileExtensionByValue(fileExtension));
      return readerUtil.read(clazz);
    } else if (FileExtension.CSV.getValue().equals(fileExtension)) {
      CsvReaderUtils readerUtil = new CsvReaderUtils(inputStream, clazz);
      return readerUtil.read(clazz);
    }
    return null;
  }
}
