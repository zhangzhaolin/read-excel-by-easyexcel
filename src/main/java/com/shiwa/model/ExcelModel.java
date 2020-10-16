package com.shiwa.model;

import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ExcelModel {

  @ExcelProperty(index = 0, value = "str1")
  @Size(max = 10, message = "最大长度不能超过 {max}")
  @NotEmpty
  private String str1;

  @ExcelProperty(index = 1)
  @NotEmpty
  private String str2;

  @ExcelProperty(index = 2)
  @NotNull
  private Date date;

}
