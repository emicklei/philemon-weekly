/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 *
 */
package com.qnh.weekly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import com.philemonworks.typewise.html.HTMLTableBasedRenderer;

/**
 * 
 */
public class HTML {
    public static void main(String[] args) {
        WeekTool tool = new WeekTool(null);
        tool.loadTopics();
        try {
            FileOutputStream out = new FileOutputStream(new File("table.html"));
            HTMLTableBasedRenderer r = new HTMLTableBasedRenderer(new PrintStream(out),"");
            r.handle(tool.mainScreen());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}