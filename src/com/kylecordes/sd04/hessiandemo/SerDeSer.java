/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.hessiandemo;

import java.io.*;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;


public class SerDeSer {
    
    public static void main(String[] args) throws Exception {
        Widget[] widgets = { new Widget(1, "blue gizmo"), new Widget(2, "red gizmo") };
        
        OutputStream os = new FileOutputStream("test.data");
        HessianOutput out = new HessianOutput(os);
        out.writeObject(widgets);
        os.close();

        InputStream is = new FileInputStream("test.data");
        HessianInput in = new HessianInput(is);
        Widget[] incomingWidgets = (Widget[]) in.readObject(null);
        is.close();

        new File("test.data").delete();
        
        System.out.println(incomingWidgets.length);
    }
}
