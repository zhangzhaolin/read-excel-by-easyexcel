package com.shiwa.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.shiwa.model.ExcelCheckError;
import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class ExcelValidListener<T> extends AnalysisEventListener<T> {

  private static final Validator validator = Validation.buildDefaultValidatorFactory()
      .getValidator();


  /**
   * 错误信息
   */
  private List<ExcelCheckError> checkErrorList = new ArrayList<>();

  /**
   * 正确消息（不能重复）
   */
  private HashSet<T> dataSet = new HashSet<>();

  /**
   * 是否读取成功
   */
  private boolean flag = true;


  @Override
  public void invoke(T data, AnalysisContext context) {
    Set<ConstraintViolation<T>> errorSet = null;
    try {
      errorSet = validator.validate(data);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    if (errorSet != null && errorSet.size() > 0) {
      Integer rowIndex = context.readRowHolder().getRowIndex();
      errorSet.parallelStream().forEach(error -> {
        try {
          Field field = data.getClass().getDeclaredField(error.getPropertyPath().toString());
          ExcelProperty property = field.getDeclaredAnnotation(ExcelProperty.class);
          checkErrorList.add(
              new ExcelCheckError().setRow(rowIndex).setColumn(property.index())
                  .setErrorMessage(property.value()[0] + " " + error.getMessage()));
        } catch (NoSuchFieldException exception) {
          System.out.println(exception.getMessage());
        }
      });
      flag = false;
    } else {
      if (flag) {
        if (!dataSet.contains(data)) {
          dataSet.add(data);
        } else {
          flag = false;
        }
      }
    }
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {

  }

  @Override
  public void onException(Exception exception, AnalysisContext context) throws Exception {
    System.out.println(exception.getMessage());
  }
}
