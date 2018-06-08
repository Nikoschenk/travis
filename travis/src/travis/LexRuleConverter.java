/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import static travis.BoldeJSON.call;
import static travis.BoldeJSON.collectTags;
import static travis.BoldeJSON.normalize;
import static travis.BoldeJSON.normalizeToTraleString;
import static travis.BoldeJSON.readJSON;
import static travis.BoldeJSON.sortKeys;

/**
 *
 *
 * @author niko
 */
public class LexRuleConverter {

    public static final String JSON_FILE = "files/BOLDE_flat_typehierarchy/head_feature_principle.json";
    //public static final String JSON_FILE = "files/BOLDE_flat_typehierarchy/principle.json";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        System.out.println("Converting JSON to Trale format...");
        
        String json = readJSON(JSON_FILE);
        JSONObject obj = new JSONObject(json);
        String trale = convertLexRule(obj);
        System.out.println(trale);
    }
    
    
    public static String convertLexRule(JSONObject obj) throws FileNotFoundException {
        StringBuilder rval = new StringBuilder();

        for (String aComp : sortKeys(obj)) {
            // System.out.println("aComp: " + aComp);
            switch (aComp) {
                case "a":
                    JSONObject a = obj.getJSONObject(aComp);
                    LinkedHashMap<String, String> aTags = collectTags(a);
                    call(a, aTags, rval);
                    rval.append(" **> ");
                    break;
                    
                case "c":
                    JSONObject c = obj.getJSONObject(aComp);
                    LinkedHashMap<String, String> cTags = collectTags(c);
                    call(c, cTags, rval);
                    rval.append(" ");
                    break;
                    
                case "name":
                    String lexRuleName = obj.getString(aComp);
                    rval.insert(0, normalizeToTraleString(lexRuleName) + " lex_rule \n");
                    rval.insert(0, "% " + lexRuleName + "\n");
                    break;
                    
                case "f":
                    rval.append("\nmorphs\n");
                    JSONArray f = obj.getJSONArray(aComp);
                    for (int tIdx = 0; tIdx < f.length(); tIdx++) {
                        JSONObject tuple = (JSONObject) f.get(tIdx);
                        rval.append(tuple.get("in") + " becomes " + tuple.get("out") + ", ");
                    }
                    rval.append(" ");
                    break;
                
                    // (W,y) becomes (W,ied).
                case "p":
                    JSONArray p = obj.getJSONArray(aComp);
                    for (int tIdx = 0; tIdx < p.length(); tIdx++) {
                        JSONObject tuple = (JSONObject) p.get(tIdx);
                        rval.append("(W," + tuple.get("in") + ") becomes (W," + tuple.get("out") + ")");
                    }
                    rval.append(" ");
                    break;
                    
            }
        }
        rval.append(".");
        String trale = normalize(rval.toString());
        return trale;
    }

    
}
