/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author niko
 */
public class BoldeJSON {
    
    public static String readJSON(String fileName) {
        StringBuilder sb = new StringBuilder();
        Scanner s = null;
        try {
            s = new Scanner(new File(fileName));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BoldeJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (s.hasNextLine()) {
            String aLine = s.nextLine();
            sb.append(aLine);
        }
        s.close();
        return sb.toString();
    }
    
    
    public static String normalize(String trale) {
         return trale
                .replace(",,", ",")
                .replace("(,", "(")
                .replace(",]", "]")
                .replace(",)", ")")
                .replace(",\n", ")")
                // Replace isolated empty lists.
                .replace(")[]", ")")
                .replace("][]", "]");
    }
    
    public static TreeSet<String> sortKeys(JSONObject unsorted) {
        TreeSet<String> sorted = new TreeSet<String>();
        sorted.addAll(unsorted.keySet());
        return sorted;
    }
    
    
    
    public static void call(JSONObject c, StringBuilder rval) {

        TreeSet<String> sorted = new TreeSet<String>();
        sorted.addAll(c.keySet());
        for (String sub : sorted) {
          
            // f
            if (sub.equals("f")) {
                //System.out.println("f: " + c.get(sub));
            }

            if (sub.equals("type")) {
                JSONObject t = (JSONObject) c.get(sub);
                rval.append(t.get("e") + ",");
                
            }

            if (sub.equals("v")) {
                JSONObject v = (JSONObject) c.get(sub);
                int children = 0;
                for (String subtype : v.keySet()) {
                    children++;
                    if(children == v.keySet().size()) {
                        rval.append(",");
                    }
                    JSONObject substructure = v.getJSONObject(subtype);
                    rval.append(subtype + ":");
                    
                    // Call function recursively with substructure;
                    call(substructure, rval);
                    
                }
                rval.append(")");
                    
            }

            if (c.get(sub) instanceof String) {
                String str = (String) c.get(sub);
                if (str.equals("tfstruct")) {
                    rval.append("(");
                }
                if (str.equals("fstruct")) {
                    rval.append("(");
                }
                if (str.equals("variable")) {
                    String index = String.valueOf(c.get("index"));
                    rval.append(index + ",");
                }
                if (sub.equals("e")) {
                    rval.append(c.get("e"));
                }
                if (str.equals("list_empty")) {
                    rval.append("[],");
                }

            }
            if (sub.equals("first")) {
                rval.append("[");
                JSONObject first = (JSONObject) c.get(sub);
                call(first, rval);
            }
            if (sub.equals("rest")) {
                rval.append("]");
                // UNTESTED: recursive call if multiple elements on rest list.
                JSONObject rest = (JSONObject) c.get(sub);
                call(rest, rval);
            }
        }
    }
    
}
