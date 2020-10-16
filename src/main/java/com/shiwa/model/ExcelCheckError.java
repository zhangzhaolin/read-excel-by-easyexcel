package com.shiwa.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExcelCheckError {

  private int row;

  private int column;

  private String errorMessage;

}
