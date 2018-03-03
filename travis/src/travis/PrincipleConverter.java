/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.FileNotFoundException;
import org.json.JSONObject;
import static travis.BoldeJSON.call;
import static travis.BoldeJSON.normalize;
import static travis.BoldeJSON.readJSON;
import static travis.BoldeJSON.sortKeys;

/**
 *
 * phrase *> (syn:pos:1, head_dtr:syn:pos:1).
 * 
 * or:
 *           (sign,head_dtr:(syn:(pos:4))), syn:(pos:3)
 *
 * (word, syn:(category, pos:(v, vform:fin))) *> (syn:subj:[(pos:case:nom)]).
 *
 *
 *
 * @author niko
 */
public class PrincipleConverter {

    public static final String JSON_FILE = "files/head_feature_principle.json";
    //public static final String JSON_FILE = "files/principle.json";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        System.out.println("Converting JSON to Trale format...");
        
        String json = readJSON(JSON_FILE);
        JSONObject obj = new JSONObject(json);
        String trale = convertPrinciple(obj);
        System.out.println(trale);
    }
    
    
    public static String convertPrinciple(JSONObject obj) throws FileNotFoundException {
        StringBuilder rval = new StringBuilder();

        for (String aComp : sortKeys(obj)) {
            // System.out.println("aComp: " + aComp);
            switch (aComp) {
                case "a":
                    JSONObject a = obj.getJSONObject(aComp);
                    call(a, rval);
                    rval.append(" *> ");
                    break;
                case "c":
                    JSONObject c = obj.getJSONObject(aComp);
                    call(c, rval);
                    rval.append(" ");

                    break;
                case "name":
                    String principleName = obj.getString(aComp);
                    rval.insert(0, "% "+principleName + "\n");
                    break;
            }
        }
        rval.append(".");
        String trale = normalize(rval.toString());
        return trale;
    }

    
}
