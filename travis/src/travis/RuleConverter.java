/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.FileNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import static travis.BoldeJSON.call;
import static travis.BoldeJSON.normalize;
import static travis.BoldeJSON.readJSON;

/**
 *
 * @author niko
 */
public class RuleConverter {

    public static final String JSON_FILE = "files/G11/files/hcrule.json";
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        System.out.println("Converting JSON to Trale format...");
        
        String json = readJSON(JSON_FILE);
        JSONObject obj = new JSONObject(json);
        String trale = convertRule(obj);
        System.out.println(trale);
    }
    
    
    public static String convertRule(JSONObject obj) throws FileNotFoundException {
        StringBuilder rval = new StringBuilder();

        for (String aComp : obj.keySet()) {
            // System.out.println("aComp: " + aComp);
            switch (aComp) {
                case "d":
                    // TODO: Get the daughter indices !
                    
                   
                    break;
                case "m":
                    JSONObject m = obj.getJSONObject(aComp);
                    call(m, rval);
                    rval.append("\n===>\n");
                    
                    // Get the values.
                    for(String subComp : m.keySet()) {
                        System.out.println(subComp);
                        switch(subComp) {
                            case "borjes_bound":
                                // Get titles and values.
                                
                                JSONObject d = m.getJSONObject(subComp);
                                for(String subsubComp : d.keySet()) {
                                    switch(subsubComp) {
                                    case "values":
                                        JSONArray values = (JSONArray) d.getJSONArray(subsubComp);
                                        for(int v = 0; v < values.length(); v++) {
                                            JSONObject aValue = values.getJSONObject(v);
                                            System.out.println(aValue);
                                            rval.append("cat> ");
                                            call(aValue, rval);
                                            if(v!=values.length()-1) {
                                                rval.append(",\n");
                                            }
                                            rval.append("\n");
                                        }
                                        break;
                                    }
                                }
                                
                                break;
                            
                        }
                    }
                    

                    break;
                case "name":
                    String ruleName = obj.getString(aComp);
                    rval.insert(0, "% "+ruleName + "\n");
                    break;
            }
        }
        rval.append(".");
        String trale = normalize(rval.toString());
        return trale;
    }

    
}
