package universal_taskflow.common.utils.serialization;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshaller;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.thoughtworks.xstream.core.ReferenceByXPathUnmarshaller;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.core.TreeUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.Mapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import universal_taskflow.common.data.ConvertorInfo;
import universal_taskflow.common.data.ElHtmlKey;
import universal_taskflow.common.data.ElHtmlValue;
import universal_taskflow.common.data.MainRecord;
import universal_taskflow.common.data.SecurityData;
import universal_taskflow.common.data.SqlAttribute;
import universal_taskflow.common.data.SqlParameter;
import universal_taskflow.common.data.UserRecord;
import universal_taskflow.common.data.ValidatorInfo;
import universal_taskflow.common.data.VisualControlForSqlAttributeInfo;
import universal_taskflow.common.data.VisualControlForSqlParameterInfo;
import universal_taskflow.common.data.taskflow_type_parameters.TaskFlowParametersBase;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.DiagramTypeParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.FormTypeParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TableAdfParameters;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TaskFlowParametersBaseOnSql;
import universal_taskflow.common.data.taskflow_type_parameters.on_sql.TreeTypeParameters;
import universal_taskflow.common.types.ConvertorType;
import universal_taskflow.common.types.DataChangeType;
import universal_taskflow.common.types.ValidatorType;
import universal_taskflow.common.types.VisualControlForSqlAttributeType;
import universal_taskflow.common.types.VisualControlForSqlParameterType;


public final class XStreamFacade
{
  private static final String BEFORE_MARSHALL_METHOD   = "xstreamBeforeMarshall";
  private static final String AFTER_UN_MARSHALL_METHOD = "xstreamAfterUnMarshall";
  //private static final String PREFIX_FIELDS_DEFAULT_VALUE = "$XS_DEFAULT_";
  private final static Set<Class> CLASSES = new HashSet<Class>();
  static
  {
    registerClass(MainRecord.class);
    registerClass(UserRecord.class);
    registerClass(TaskFlowParametersBase.class);
    registerClass(TaskFlowParametersBaseOnSql.class);
    registerClass(TableAdfParameters.class);
    registerClass(FormTypeParameters.class);
    registerClass(DiagramTypeParameters.class);
    registerClass(TreeTypeParameters.class);
    registerClass(SecurityData.class);
    registerClass(ElHtmlKey.class);
    registerClass(ElHtmlValue.class);
    registerClass(DataChangeType.class);
    registerClass(VisualControlForSqlParameterInfo.class);
    registerClass(VisualControlForSqlParameterType.class);
    registerClass(VisualControlForSqlAttributeInfo.class);
    registerClass(VisualControlForSqlAttributeType.class);
    registerClass(SqlParameter.class);
    registerClass(SqlAttribute.class);
    registerClass(ValidatorInfo.class);
    registerClass(ValidatorType.class);
    registerClass(ConvertorInfo.class);
    registerClass(ConvertorType.class);
    //=================================
  }
  //=========================================================
  private XStreamFacade()
  {
    super();
  }
  //----------------
  public static void registerClass(Class cl)
  {
    CLASSES.add(cl);
  }
  public static void unRegisterClass(Class cl)
  {
    CLASSES.remove(cl);
  }
  public static XStream getXStream()
  {
    //XStream xstream = new XStream();
    
    XStream xstream = new XStream(new XppDriver()
    {
      public HierarchicalStreamWriter createWriter(Writer out)
      {
        return new CustomWriter(out);
      }
    });
    //========================================================
    xstream.processAnnotations(CLASSES.toArray(new Class[]{}));
    xstream.ignoreUnknownElements(); // Отлично!
    xstream.setMarshallingStrategy(new CustomTreeMarshallingStrategy());
    //---
    return xstream;
  }
  
//  public static XStream getXStreamWithoutCustomization()
//  {
//    XStream xstream = new XStream();
//    //========================================================
//    xstream.processAnnotations(CLASSES.toArray(new Class[]{}));
//    xstream.ignoreUnknownElements(); // Отлично!
//    xstream.setMarshallingStrategy(new CustomTreeMarshallingStrategy());
//    //---
//    return xstream;
//  }

//  public static void toXML(Serializable obj, URL url)
//    throws UnsupportedEncodingException, FileNotFoundException, IOException
//  {
//    toXML(obj,url.getFile());
//  }

  public static void toXML(Serializable obj, String file)
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    XStream xstream = getXStream();

    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file), "UTF-8"
        ));

    xstream.toXML(obj,writer);
    writer.close();
  }
  
  public static String toXML(Serializable obj)
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    XStream xstream = getXStream();
    return xstream.toXML(obj);
  }
  
  public static Serializable fromStringXML(String strXml)
  throws UnsupportedEncodingException, FileNotFoundException, IOException 
  {
    XStream xstream = getXStream();
    return (Serializable) xstream.fromXML(strXml);
  }

  public static Serializable fromXML(String file)
    throws UnsupportedEncodingException, FileNotFoundException, IOException
  {
    XStream xstream = getXStream();

    BufferedReader reader = new BufferedReader(
      new InputStreamReader(
        new FileInputStream(file),"UTF-8"));

    Serializable obj = (Serializable) xstream.fromXML(reader);
    reader.close();
    return obj;
  }

//  public static Serializable fromXML(URL url)
//    throws UnsupportedEncodingException, FileNotFoundException, IOException
//  {
//    return fromXML(url.getFile());
//  }

  private static Long getSerialVersionUID(Class cl)
  {
    Long rc = null;
    if (cl != null)
    {
      Field field = null;
      try
      {
        field = cl.getDeclaredField("serialVersionUID");
      }
      catch (NoSuchFieldException e)
      {
        ;
      }
      if (field != null)
      {
        field.setAccessible(true);
        try
        {
          rc = (Long) field.get(null);
        }
        catch (IllegalAccessException e)
        {
          ;
        }
      }
    }
    return rc;
  }

  private static boolean inClasses(Class cl)
  {
    for (Class item : CLASSES)
    {
      if (item.equals(cl))
      {
        return true;
      }
    }
    return false;
  }
//  private static void initNewFields(Object rc, String suid_from_xml)
//    throws IllegalAccessException
//  {
//    Long suid = getSerialVersionUID(rc.getClass());
//    if (suid != null)
//    {
//      if (!suid_from_xml.equals(suid.toString()))
//      {
//        initNewFieldsImpl(rc);
//      }
//    }
//  }

//  private static void initNewFieldsImpl(Object obj)
//    throws IllegalAccessException
//  {
//    Class cl = obj.getClass(); // obj != null
//    Field[] fields = cl.getDeclaredFields();
//    for (Field item : fields)
//    {
//      String itemFieldName = item.getName();
//      if (itemFieldName.startsWith(PREFIX_FIELDS_DEFAULT_VALUE))
//      {
//        String fieldName = itemFieldName.substring(PREFIX_FIELDS_DEFAULT_VALUE.length());
//        Field targetField = null;
//        try
//        {
//          targetField = cl.getDeclaredField(fieldName);
//        }
//        catch (NoSuchFieldException e)
//        {
//          ;
//        }
//        if (targetField != null)
//        {
//          initNewFieldImpl(obj,targetField,item);
//        }
//      }
//    }
//  }

//  private static void initNewFieldImpl(Object obj,
//                                       Field targetField,
//                                       Field metaFieldStatic)
//    throws IllegalAccessException
//  {
//    targetField.setAccessible(true);
//    Object currentValue = targetField.get(obj);
//    if (currentValue == null)
//    {
//      metaFieldStatic.setAccessible(true);
//      Object defaultValue = metaFieldStatic.get(null);
//      targetField.setAccessible(true);
//      targetField.set(obj, defaultValue);
//    }
//  }
  //================================================

  public static class CustomTreeMarshallingStrategy extends ReferenceByXPathMarshallingStrategy
      //TreeMarshallingStrategy
  {
    public CustomTreeMarshallingStrategy()
    {
      super(ReferenceByXPathMarshallingStrategy.RELATIVE);
    }
    
    protected TreeUnmarshaller createUnmarshallingContext(Object root,
        HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) 
    {
        //return new ReferenceByXPathUnmarshaller(root, reader, converterLookup, mapper);
        return new CustomTreeUnmarshaller(root, reader, converterLookup, mapper);
    }

    protected TreeMarshaller createMarshallingContext(
        HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) 
    {
        //return new ReferenceByXPathMarshaller(writer, converterLookup, mapper, ReferenceByXPathMarshallingStrategy.RELATIVE);
        return new CustomTreeMarshaller(writer, converterLookup, mapper, ReferenceByXPathMarshallingStrategy.RELATIVE);
    }
  }

  public static class CustomTreeMarshaller extends ReferenceByXPathMarshaller
  {
    public CustomTreeMarshaller(
            HierarchicalStreamWriter writer, 
            ConverterLookup converterLookup, 
            Mapper mapper, 
            int mode)
    {
      super(writer,converterLookup,mapper,mode);
    }

    public void convert(Object item, Converter converter)
    {
      // Вызов метода beforeMarshal
      if (item != null)
      {
        if (inClasses(item.getClass()))
        {
          Long serialVersionUID = getSerialVersionUID(item.getClass());
          if (serialVersionUID != null) {
            this.writer.addAttribute("serialVersionUID", serialVersionUID.toString());
          }
        }
        try
        {
          runPrivateMethods(item, BEFORE_MARSHALL_METHOD,
                            new Class[]{HierarchicalStreamWriter.class},
                            new Object[]{writer});
        }
        catch (Exception e)
        {
          ;
        }
      }
      super.convert(item,converter);
    }
  }

  private static void runPrivateMethods(Object obj, String methodName, Class[] types, Object[] params)
  {
    Class current = obj.getClass();
    Method method = null;
    while (!current.equals(Object.class))
    {
      try
      {
        method = current.getDeclaredMethod(methodName, types);
        method.setAccessible(true);
        method.invoke(obj, params);
      }
      catch (NoSuchMethodException ex)
      {
        ;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      current = current.getSuperclass();
    }
  }


  //=================================================================
  public static class CustomTreeUnmarshaller extends ReferenceByXPathUnmarshaller
        // TreeUnmarshaller
  {
    public CustomTreeUnmarshaller(Object p1,
                                 HierarchicalStreamReader p2,
                                 ConverterLookup p3,
                                 Mapper p4)
    {
      super(p1,p2,p3,p4);
    }
    
    protected Object convert(Object parent, Class type, Converter converter)
    {
      List<Method> methods = getMethodList(AFTER_UN_MARSHALL_METHOD,type,new Class[]{Map.class});
      Map<String,String> attributes = null;
      if (methods.size() > 0) 
      {
        attributes = new HashMap<String,String>();
        Iterator it = reader.getAttributeNames();
        while (it.hasNext())
        {
          String name = (String)it.next();
          attributes.put(name, reader.getAttribute(name));
        }
      }
      String suid_from_xml = null;
      if (inClasses(type))
      {
        suid_from_xml = reader.getAttribute("serialVersionUID");
      }
      Object rc = super.convert(parent,type,converter);
      
      if (rc != null)
      {
//        if (suid_from_xml != null) 
//        {
//          try
//          {
//            initNewFields(rc,suid_from_xml);
//          }
//          catch (IllegalAccessException e)
//          {
//            e.printStackTrace();
//          }
//        }
        
        // Call:
        for(int i=0; i < methods.size();i++) 
        {
          Method item = methods.get(i);
          item.setAccessible(true);
          try
          {
            item.invoke(rc, new Object[]{attributes});
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        } // for
      } // if
      //---------------------
      return rc;
    }

//    protected Object convert(Object parent, Class type, Converter converter)
//    {
//      Method xstreamAfterUnMarshall = null;
//      Map<String,String> attributes = null;
//      if (type != null)
//      {
//        try
//        {
//          xstreamAfterUnMarshall = type.getDeclaredMethod(AFTER_UN_MARSHALL_METHOD, new Class[]{Map.class});
//        }
//        catch (NoSuchMethodException e)
//        {
//          ;
//        }
//      }
//      if (xstreamAfterUnMarshall != null)
//      {
//        attributes = new HashMap<String,String>();
//        Iterator it = reader.getAttributeNames();
//        while (it.hasNext())
//        {
//          String name = (String)it.next();
//          attributes.put(name, reader.getAttribute(name));
//        }
//      }
//      String suid_from_xml = null;
//      if (inClasses(type))
//      {
//        suid_from_xml = reader.getAttribute("serialVersionUID");
//      }
//      Object rc = super.convert(parent,type,converter);
//      // вызов метода afterUnMarshal
//      if (rc != null)
//      {
//        if (suid_from_xml != null) {
//          try
//          {
//            initNewFields(rc,suid_from_xml);
//          }
//          catch (IllegalAccessException e)
//          {
//            e.printStackTrace();
//          }
//        }
//        if (xstreamAfterUnMarshall != null)
//        {
//          try
//          {
//            xstreamAfterUnMarshall.setAccessible(true);
//            xstreamAfterUnMarshall.invoke(rc, new Object[]{attributes});
//          }
//          catch (Exception e)
//          {
//            // System.err.println(e.getMessage())
//            ;
//          }
//        }
//      }
//      return rc;
//    }

    private List<Method> getMethodList(String methodName, Class type, Class[] types)
    {
      Class current = type;
      List<Method> rc = new ArrayList<Method>();
      Method method = null;
      while (!current.equals(Object.class))
      {
        try
        {
          method = current.getDeclaredMethod(methodName, types);
          rc.add(method);
        }
        catch (NoSuchMethodException ex)
        {
          ;
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
        current = current.getSuperclass();
      }
      return rc;
    }
  }
}


