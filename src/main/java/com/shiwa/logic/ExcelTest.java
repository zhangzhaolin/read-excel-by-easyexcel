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
      ExcelValidListener<ExcelModel> validListener = new ExcelValidListener<>();
      reader = EasyExcel.read(stream).build();
      ReadSheet readSheet = EasyExcel.readSheet(0).head(ExcelModel.class)
          .registerReadListener(validListener).build();
      reader.read(readSheet);
      System.out.println(validListener.getCheckErrorList());
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    } finally {
      if (reader != null) {
        reader.finish();
      }
    }
  }
}
