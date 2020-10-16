package com.shiwa.logic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.shiwa.listener.ExcelValidListener;
import com.shiwa.model.ExcelModel;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelTest {

  public static void main(String[] args) {
    ExcelReader reader = null;
    try (InputStream stream = ExcelTest.class.getResourceAsStream("/template.xlsx");) {
      reader = EasyExcel.read(stream).build();
      ExcelValidListener<ExcelModel> validListener = new ExcelValidListener<>();
      ReadSheet readSheet = EasyExcel.readSheet(0).registerReadListener(validListener).build();
      reader.read(readSheet);
      log.error(validListener.getCheckErrorList().toString());
    } catch (IOException ex) {

    } finally {
      if (reader != null) {
        reader.finish();
      }
    }
  }
}
