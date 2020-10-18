package com.shiwa.listener;

import com.alibaba.excel.write.handler.AbstractSheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.shiwa.model.ExcelCheckError;
import com.shiwa.util.CellStyleUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelValidWriteListener extends AbstractSheetWriteHandler {

  private List<ExcelCheckError> errorList;

  @Override
  public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
      WriteSheetHolder writeSheetHolder) {

  }

  @Override
  public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
      WriteSheetHolder writeSheetHolder) {
    Workbook workbook = writeWorkbookHolder.getWorkbook();
    Sheet sheet = writeSheetHolder.getCachedSheet();
    errorList.parallelStream().forEach(error -> {
      Row row = sheet.getRow(error.getRow());
      if (error.getColumn() >= 0) {
        // 给某一个单元格标红
        Cell cell = CellUtil.getCell(row, error.getColumn());
        cell.setCellStyle(
            CellStyleUtil.createBackGroundColorByCell(workbook, cell, IndexedColors.RED));
      } else {
        // 全部标红处理
        row.cellIterator().forEachRemaining(cell -> {
          cell.setCellStyle(
              CellStyleUtil.createBackGroundColorByCell(workbook, cell, IndexedColors.RED));
        });
      }
    });
  }
}