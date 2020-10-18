package com.shiwa.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.util.WorkBookUtil;
import com.shiwa.model.ExcelCheckError;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;


@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class ExcelValidReadListener<T> extends AnalysisEventListener<T> {

  private static final Validator validator = Validation.buildDefaultValidatorFactory()
      .getValidator();

  /**
   * 错误信息
   */
  private List<ExcelCheckError> errorList = new ArrayList<>();

  /**
   * 正确消息（不能重复），需要记录与第几行数据重复
   */
  private HashMap<Integer, HashSet<T>> dataSet = new HashMap<>();

  /**
   * 是否读取成功
   */
  private boolean flag = true;


  @Override
  public void invoke(T data, AnalysisContext context) {
    Set<ConstraintViolation<T>> violationSet = null;
    try {
      violationSet = validator.validate(data);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    if (violationSet != null && violationSet.size() > 0) {
      int rowIndex = context.readRowHolder().getRowIndex();
      violationSet.parallelStream().forEach(error -> {
        try {
          Field field = data.getClass().getDeclaredField(error.getPropertyPath().toString());
          ExcelProperty property = field.getDeclaredAnnotation(ExcelProperty.class);
          errorList.add(
              new ExcelCheckError().setRow(rowIndex).setColumn(property.index())
                  .setErrorMessage(property.value()[0] + " " + error.getMessage()));
        } catch (NoSuchFieldException exception) {
          System.out.println(exception.getMessage());
        }
      });
      flag = false;
    } else {
      if (flag) {
        int rowIndex = context.readRowHolder().getRowIndex();
        HashSet<T> hashSet = dataSet.getOrDefault(rowIndex, new HashSet<>());
        if (!hashSet.contains(data)) {
          hashSet.add(data);
        } else {
          errorList
              .add(new ExcelCheckError().setRow(rowIndex).setColumn(-1).setErrorMessage("该数据重复"));
          flag = false;
        }
      }
    }
  }

  /**
   * 所有数据解析完成后进行的操作
   *
   * @param context
   */
  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {

  }

  @Override
  public void onException(Exception exception, AnalysisContext context) {
    flag = false;
    if (exception instanceof ExcelDataConvertException) {
      ExcelDataConvertException convertException = (ExcelDataConvertException) exception;
      errorList
          .add(new ExcelCheckError().setErrorMessage(convertException.getMessage())
              .setRow(convertException.getRowIndex())
              .setColumn(convertException.getColumnIndex()));
    } else {
      System.out.println(exception.getMessage());
    }
  }
}
