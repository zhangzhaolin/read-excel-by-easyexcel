package com.shiwa.model;

import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangzhaolin
 */
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExcelModel that = (ExcelModel) o;
    return Objects.equals(str1, that.str1) &&
        Objects.equals(str2, that.str2) &&
        Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(str1, str2, date);
  }
}
