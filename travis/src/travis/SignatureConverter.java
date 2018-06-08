/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import org.json.JSONObject;
import static travis.BoldeJSON.sortKeys;

/**
 *
 *
 *
 * @author niko
 */
public class SignatureConverter {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
      
    }
    
       public static String convertSignature(JSONObject obj, int indent, LinkedHashMap<String, String> typeToFeatures) throws FileNotFoundException {
        indent++;
        for (String aType : sortKeys(obj)) {
            StringBuilder indentB = new StringBuilder();
            for (int i = 0; i < indent; i++) {
                indentB.append("   ");
            }
            System.out.print(indentB.toString() + aType);
            if(typeToFeatures.containsKey(aType)) {
                System.out.print(" " + typeToFeatures.get(aType));
            }
            System.out.println();
            if (obj.get(aType) instanceof JSONObject) {
                convertSignature(obj.getJSONObject(aType), indent, typeToFeatures);
            }
        }
        return "";
    }
}
