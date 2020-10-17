package com.shiwa.logic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.shiwa.listener.ExcelValidReadListener;
import com.shiwa.model.ExcelCheckError;
import com.shiwa.model.ExcelModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelTest {

  public static void main(String[] args) {
    ExcelReader reader = null;
    try (InputStream stream = ExcelTest.class.getResourceAsStream("/template.xlsx");) {
      ExcelValidReadListener<ExcelModel> validListener = new ExcelValidReadListener<>();
      reader = EasyExcel.read(stream).build();
      ReadSheet readSheet = EasyExcel.readSheet(0).head(ExcelModel.class)
          .registerReadListener(validListener).build();
      reader.read(readSheet);
      List<ExcelCheckError> errorList = validListener.getErrorList();
      if (errorList.size() > 0) {
        // 输出

      }
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    } finally {
      if (reader != null) {
        reader.finish();
      }
    }
  }
}
