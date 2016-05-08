package com.rcore.taskflow.view;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestExternalizable implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final String SER_FILE = "c:/temp/_ser_0.bin";
  
  private int a = 1;
  private String b = "B";
  public TestExternalizable()
  {
    super();
  }

  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeInt(a);
    out.writeObject(b);
  }

  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    a = in.readInt();
    b = (String) in.readObject();
  }
  //-----------------------
  public static void main(String[] args) throws Exception
  {
    test1();
  }

  private static void test1() throws Exception
  {
    TestExternalizable obj = new TestExternalizable();
    obj.a = 101;
    serialization(obj,SER_FILE);
    obj = deserialization(SER_FILE);
    System.out.println(obj.a);
    System.out.println(obj.b);
  }

  private static void serialization(
      TestExternalizable obj, String file)
    throws Exception
  {
    FileOutputStream fos   = new FileOutputStream(file);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(obj);
    oos.flush();
    oos.close();
  }

  private static TestExternalizable deserialization(String file)
    throws Exception
  {
    FileInputStream fis = new FileInputStream(file);
    ObjectInputStream oin = new ObjectInputStream(fis);
    TestExternalizable ts = (TestExternalizable) oin.readObject();
    oin.close();
    return ts;
  }
}
