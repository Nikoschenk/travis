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
public class LexicalEntryConverter {

    public static final String JSON_FILE = "files/BOLDE_flat_typehierarchy/head_feature_principle.json";
    //public static final String JSON_FILE = "files/BOLDE_flat_typehierarchy/principle.json";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        System.out.println("Converting JSON to Trale format...");
        
        String json = readJSON(JSON_FILE);
        JSONObject obj = new JSONObject(json);
        String trale = convertLexEntry(obj);
        System.out.println(trale);
    }
    
    
    public static String convertLexEntry(JSONObject obj) throws FileNotFoundException {
        StringBuilder allWords = new StringBuilder();
        ArrayList<String> words = new ArrayList<String>();
        
        StringBuilder template = new StringBuilder();

        for (String aComp : sortKeys(obj)) {
            // System.out.println("aComp: " + aComp);
            switch (aComp) {
                // AVM of lexicon entry.
                case "a":
                    JSONObject a = obj.getJSONObject(aComp);
                    LinkedHashMap<String, String> aTags = collectTags(a);
                    call(a, aTags, template);
                    break;
                    
                // List of word realizations, e.g., "fido", "john", "mary",...
                case "c":
                    JSONArray f = obj.getJSONArray(aComp);
                    for (int tIdx = 0; tIdx < f.length(); tIdx++) {
                        String aLexiconWord = (String) f.get(tIdx);
                        words.add(aLexiconWord.toLowerCase());
                    }
                    break;
                    
                case "name":
                    String lexEntryTemplateName = obj.getString(aComp);
                    allWords.insert(0, "% "+lexEntryTemplateName + "\n");
                    break;
            }
        }
        String trale = normalize(template.toString());
        
        // For each realization of the template, i.e. for all words in the list.
        for(String w : words) {
            // Ignore empty words (bug in Gert's UI).
            if(w.trim().length() > 0)
            allWords.append(w + " ---> " + trale + ".\n");
        }
        return allWords.toString();
    }

    
}
