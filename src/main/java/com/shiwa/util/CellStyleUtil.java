package com.shiwa.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyleUtil {

  /**
   * 在某个单元格上创建背景色
   *
   * @param workbook WorkBook
   * @param cell     单元格
   * @param colors   背景颜色
   * @return 渲染的样式
   */
  public static CellStyle createBackGroundColorByCell(Workbook workbook, Cell cell,
      IndexedColors colors) {
    CellStyle result = workbook.createCellStyle();
    result.cloneStyleFrom(cell.getCellStyle());
    result.setFillForegroundColor(colors.index);
    result.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return result;
  }

}
