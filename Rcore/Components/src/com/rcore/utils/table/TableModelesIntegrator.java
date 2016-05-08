package com.rcore.utils.table;


import java.util.List;

import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.event.SortEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.SortCriterion;


/**
 <pre>
 * Интерфейс для класса, который интегрирует все модели и обработчики
 * событий для таблицы &lt;af:table
 * Эти модели и обработчики должны согласовываться между собой
 * Идея - обеспечить быструю реакцию таблицы на больших объемах данных
 * за счет применения "динамического" списка (DynamicList)
 *
 * Примерное применение подoбного класса:
 * 1) На визуальном уровне (.jspx):
     &lt;af:table id="t1"
                  value="#{viewScope.testTableModelesBean.modeles.validateDataModel}"
                  filterModel="#{viewScope.testTableModelesBean.modeles.filterModel}"
                  sortListener="#{viewScope.testTableModelesBean.modeles.sortListener}"
                  selectionListener="#{viewScope.testTableModelesBean.modeles.selectionListener}"
                  columnSelection="single"
                  var="row"
                  summary="#{''}"
                  filterVisible="true"
                  rowSelection="single"
                  displayRow="selected"
        >
          &lt;af:column id="c1" headerText="ID" sortProperty="ID" filterable="true" sortable="true"&gt;
            &lt;af:outputText value="#{row.ID}" id="ot1"    /&gt;
          &lt;/af:column&gt;
          &lt;af:column id="c2" headerText="VALUE" sortProperty="VALUE" filterable="true" sortable="true"&gt;
            &lt;af:outputText value="#{row.VALUE}" id="ot2" /&gt;
          &lt;/af:column&gt;
        &lt;/af:table&gt;
 * 2) На уровне модели (java):
 <label>@</label>PostConstruct
 public void initBean()
 {
   try
   {
     List<String> listFilterFields = new ArrayList<String>();
     listFilterFields.add("ID");
     listFilterFields.add("VALUE");
     List<String> listSortFields = new ArrayList<String>();
     listSortFields.add("ID");
     listSortFields.add("VALUE");
     modeles = new TableModelesIntegratorImpl(
                 ConnectionFactory.getConnectionInterface(getConnection()),
                 "select ID ID, VALUE VALUE from TEST order by ID",
                 listFilterFields,
                 25
     );
   }
   catch (SQLException e)
   {
     e.printStackTrace();
   }
 }
</pre>

 * @see TableModelesIntegratorImpl
 */
public interface TableModelesIntegrator
{
  /**
   * Модель данных без проверки ее актуальности
   */
  CollectionModel getDataModel();
  /**
   * Модель данных с проверкой ее актуальности
   */
  CollectionModel getValidateDataModel();
  /**
   * Фильтр - модель
   */
  FilterableQueryDescriptor getFilterModel();
  /**
   * Установить модель сортировки
   */
  void setSortModel(List<SortCriterion> value);
  /**
   * Получить модель сортировки
   */
  List<SortCriterion> getSortModel();
  /**
   * Получить сигнатуру данных. Т.е. некоторую "контрольную сумму" по 
   * данным
   */
  String getDataSignature();
  /**
   * Установить сигнатуру данных. Т.е. некоторую "контрольную сумму" по 
   * данным
   */
  void setDataSignature(String value);
  /**
   * Проверить модель данных и модель фильтра.При необходимости изменить их.
   */
  void validate(String newDataSignature);
  /**
   * Обработчик запроса на сортировку
   */
  void sortListener(SortEvent sortEvent);
  /**
   * Обработчик запроса на выделение данных
   */
  void selectionListener(SelectionEvent selectionEvent);
  /**
   * Метод актуализации выделенных данных 
   */
  void makeCurrent(Object rowKey);
}
