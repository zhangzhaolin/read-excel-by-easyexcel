package com.shiwa.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.shiwa.listener.ExcelValidReadListener;
import com.shiwa.model.ExcelModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Excel 读取工具类，如果读取的 POJO 有错误信息，会返回一个标有红色错误的 excel 文件流。
 * <p>
 * 如果读取的文件全部正确，返回空
 */
public class ExcelReadUtil {

  public static <T> OutputStream readExcelAndValid(InputStream excelInputStream,
      ExcelValidReadListener<T> excelValidReadListener, Class<T> tClass) {
    ExcelReader reader;
    reader = EasyExcel.read(excelInputStream).build();
    ReadSheet readSheet = EasyExcel.readSheet(0).head(ExcelModel.class)
        .registerReadListener(excelValidReadListener).build();
    reader.read(readSheet);
    reader.finish();
    // 如果文件导出失败，返回文件流
    if (!excelValidReadListener.isFlag()) {

    }
    return null;
  }

}
