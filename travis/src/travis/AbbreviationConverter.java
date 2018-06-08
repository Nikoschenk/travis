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
import static travis.BoldeJSON.normalize;
import static travis.BoldeJSON.readJSON;
import static travis.BoldeJSON.sortKeys;

/**
 *
 *
 * @author niko
 */
public class AbbreviationConverter {
    
    //public static final String JSON_FILE = "files/p24/p24/files/abbr.json";
    public static final String JSON_FILE = "files/p24/p24/files/abbr2.json";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        System.out.println("Converting JSON to Trale format...");
        
        String json = readJSON(JSON_FILE);
        JSONObject obj = new JSONObject(json);
        String trale = convertAbbreviation(obj);
        System.out.println(trale);
    }
    
    
    public static String convertAbbreviation(JSONObject obj) throws FileNotFoundException {
        
        StringBuilder template = new StringBuilder();

        for (String aComp : sortKeys(obj)) {
            // System.out.println("aComp: " + aComp);
            switch (aComp) {
                // AVM of abbreviation entry.
                case "fstr":
                    JSONObject a = obj.getJSONObject(aComp);
                    LinkedHashMap<String, String> aTags = collectTags(a);
                    call(a, aTags, template);
                    break;
                    
                case "symbol":
                    String abbreviationSymbol = obj.getString(aComp);
                    //System.out.println(lexEntryTemplateName);
                    template.insert(0, "% "+abbreviationSymbol + "\n" + abbreviationSymbol.toLowerCase() + " := ");
                    break;
            }
        }
        
        String trale = normalize(template.toString());
        return trale;
    }

    
}
