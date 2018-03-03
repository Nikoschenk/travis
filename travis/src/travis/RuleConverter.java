/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import static travis.BoldeJSON.call;
import static travis.BoldeJSON.collectTags;
import static travis.BoldeJSON.collectTagsFromBobo;
import static travis.BoldeJSON.normalize;
import static travis.BoldeJSON.readJSON;
import static travis.BoldeJSON.sortKeys;

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

        ArrayList<String> daughterIndices = new ArrayList<String>();
        
        for (String aComp : sortKeys(obj)) {
            // System.out.println("aComp: " + aComp);
            switch (aComp) {
                case "d":
                    // TODO: Get the daughter indices !
                    JSONArray d = obj.getJSONArray(aComp);
                    for(int dIdx = 0; dIdx < d.length(); dIdx++) {
                        JSONObject aDaughter = d.getJSONObject(dIdx);
                        daughterIndices.add(String.valueOf(aDaughter.getInt("index")));
                    }
                    System.out.println("Daughter indices: " + daughterIndices);
                   
                    break;
                case "m":
                    JSONObject m = obj.getJSONObject(aComp);
                    
                    LinkedHashMap tagMap = collectTags(m);
                    call(m, tagMap, rval);
                    
                    rval.append("\n===>\n");
                    
                    // Get the values.
                    for(String subComp : sortKeys(m)) {
                        switch(subComp) {
                            case "borjes_bound":
                                // Get titles and values.
                                JSONObject bobo = m.getJSONObject(subComp);
                                for (String subsubComp : sortKeys(bobo)) {
                                    
                                    
                                    switch (subsubComp) {

                                        case "titles":
                                            tagMap  = collectTagsFromBobo(bobo);
                                            break;

                                        case "values":
                                            JSONArray values = (JSONArray) bobo.getJSONArray(subsubComp);
                                            // for all daughters.
                                            for (int v = 0; v < daughterIndices.size(); v++) {
                                                JSONObject aValue = values.getJSONObject(v);
                                                //System.out.println(aValue);
                                                rval.append("cat> ");
                                                rval.append("(_" + tagMap.get(String.valueOf(v)) + ", ");
                                                call(aValue, tagMap, rval);
                                                if (v != values.length() - 1) {
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
                    rval.insert(0, "% " + ruleName + "\n");
                    break;
            }
        }
        rval.append(".");
        String trale = normalize(rval.toString());
        return trale;
    }

    
}
