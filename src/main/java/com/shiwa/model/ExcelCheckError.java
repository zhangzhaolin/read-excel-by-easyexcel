package com.shiwa.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExcelCheckError {

  /**
   * excel 的第几行，从第 0 行开始
   */
  private int row;

  /**
   * excel 的第几列，从第 0 列开始；如果整行都有错误（都会标红），该值为 -1
   */
  private int column;

  /**
   * 具体的错误信息
   */
  private String errorMessage;

}
