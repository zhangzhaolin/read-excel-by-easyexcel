package com.shiwa.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.shiwa.model.ExcelCheckError;
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

  private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory()
      .getValidator();

  /**
   * 错误信息
   */
  private List<ExcelCheckError> errorList = new ArrayList<>();

  /**
   * 正确消息（不能重复），需要记录与第几行数据重复
   */
  private HashMap<T, Integer> dataHashMap = new HashMap<>();

  /**
   * 是否读取成功
   */
  private boolean flag = true;


  @Override
  public void invoke(T data, AnalysisContext context) {
    Set<ConstraintViolation<T>> violationSet = VALIDATOR.validate(data);
    int rowIndex = context.readRowHolder().getRowIndex();
    if (violationSet != null && violationSet.size() > 0) {
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
      // 数据校验通过
      if (!dataHashMap.containsKey(data)) {
        dataHashMap.put(data, rowIndex);
      } else {
        errorList
            .add(new ExcelCheckError().setRow(rowIndex).setColumn(-1).setErrorMessage("该数据重复"));
        flag = false;
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
