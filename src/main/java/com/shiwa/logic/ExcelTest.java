package com.shiwa.logic;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.shiwa.listener.ExcelValidReadListener;
import com.shiwa.listener.ExcelValidWriteListener;
import com.shiwa.model.ExcelCheckError;
import com.shiwa.model.ExcelModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelTest {

  public static void main(String[] args) {
    ExcelReader reader = null;
    ExcelWriter writer = null;
    try (InputStream inputStream = ExcelTest.class.getResourceAsStream("/template.xlsx")) {
      ExcelValidReadListener<ExcelModel> readListener = new ExcelValidReadListener<>();
      reader = EasyExcel.read(inputStream).build();
      ReadSheet readSheet = EasyExcel.readSheet(0).head(ExcelModel.class)
          .registerReadListener(readListener).build();

      reader.read(readSheet);
      List<ExcelCheckError> errorList = readListener.getErrorList();
      if (errorList.size() > 0) {
        // 输出
        ExcelValidWriteListener writeListener = new ExcelValidWriteListener();
        // ByteArrayOutputStream excelArrayOutputStream = new ByteArrayOutputStream();
        FileOutputStream excelArrayOutputStream = new FileOutputStream("/output.xlsx");
        writeListener.setErrorList(errorList);

        writer = EasyExcel
            .write(excelArrayOutputStream)
            .withTemplate(ExcelTest.class.getResourceAsStream("/template.xlsx")).build();

        WriteSheet writeSheet = EasyExcel.writerSheet(0)
            .registerWriteHandler(writeListener)
            .build();

        writer.write(null, writeSheet);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (reader != null) {
        reader.finish();
      }
      if (writer != null) {
        writer.finish();
      }
    }
  }
}
