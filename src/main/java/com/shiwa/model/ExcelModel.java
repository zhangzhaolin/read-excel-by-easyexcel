package com.shiwa.model;

import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data

public class ExcelModel {

  @ExcelProperty(index = 0)
  @Length(max = 10, message = "最大长度不能超过10")
  @Valid
  @NotEmpty
  private String str1;

  @ExcelProperty(index = 1)
  @Valid
  @NotEmpty
  private String str2;

  @ExcelProperty(index = 2)

  private Date date;

}
