/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
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
        trale = trale
                .replace(",,", ",")
                .replace("(,", "(")
                .replace(",]", "]")
                .replace(",)", ")")
                .replace(",\n", ")")
                // Replace isolated empty lists.
                .replace(")[]", ")")
                .replace("][]", "]")
                .replace(" .", ".")
                .replace(",.", ".")
                .replace(", .", ".")
                .replace(")(W", "),(W")
                .replace(")\ncat", "),\ncat") // add commas between child nodes. 
                // TODO.
                // Replace PHON features.
                // TODO: Replace closing bracket immediately followed by character: )[a-z]
                ;

        // Replace uppercase letters by lowercase macro.
        String[] traleLines = trale.split("\\n");
        String abbreviationsRegex = "[A-Z]+";
        Pattern pattern = Pattern.compile(abbreviationsRegex);
        for (String traleLine : traleLines) {
            Matcher matcher = pattern.matcher(traleLine);
            while (matcher.find()) {
                String match = matcher.group(0);
                // Do not replace upper case letters in comments.
                if (!traleLine.startsWith("%")) {
                    String newTraleLine = traleLine.replace(match, "@" + match.toLowerCase());
                    trale = trale.replace(traleLine, newTraleLine);
                }
            }
        }

        return trale;

    }
    
    public static TreeSet<String> sortKeys(JSONObject unsorted) {
        TreeSet<String> sorted = new TreeSet<String>();
        sorted.addAll(unsorted.keySet());
        return sorted;
    }
    
    public static LinkedHashMap<String, String> collectTagsFromBobo(JSONObject bobo) {
        LinkedHashMap<String, String> tagMap = new LinkedHashMap<String, String>();
        JSONArray titles = (JSONArray) bobo.getJSONArray("titles");
        // Accumulate used tags and indices.
        for (int tIdx = 0; tIdx < titles.length(); tIdx++) {
             tagMap.put(String.valueOf(tIdx), String.valueOf(titles.get(tIdx)));
        }
        //System.out.println("Collected tags: " + tagMap);
        return tagMap;
    }
    
    public static LinkedHashMap<String, String> collectTags(JSONObject obj) {
        LinkedHashMap<String, String> tagMap = new LinkedHashMap<String, String>();
        for (String subComp : sortKeys(obj)) {
            switch (subComp) {
                case "borjes_bound":
                    // Get titles and values.
                    JSONObject bobo = obj.getJSONObject(subComp);
                    for (String subsubComp : sortKeys(bobo)) {
                        switch (subsubComp) {
                            case "titles":
                                tagMap = collectTagsFromBobo(bobo);
                        }
                    }
            }
        }
        return tagMap;
    }
    
    
    
    public static String normalizeToTraleString(String toNormalize) {
        return toNormalize.replaceAll("\\s+", "_").replace("-", "_").toLowerCase();
    }
    
    
    public static void call(JSONObject c, LinkedHashMap<String, String> tagMap, StringBuilder rval) {

        TreeSet<String> sorted = new TreeSet<String>();
        sorted.addAll(c.keySet());
        for (String sub : sorted) {
          
            // f
            if (sub.equals("f")) {
                //System.out.println("f: " + c.get(sub));
            }

            if (sub.equals("type")) {
                
                if(c.get(sub) instanceof String) {
                    //System.out.println("-");
                }
                else {
                JSONObject t = (JSONObject) c.get(sub);
                
                if (t.has("e")) {
                    rval.append(t.get("e") + ",");
                } else {
                    //System.out.println("t has no e!");
                    // CAREFUL! Last change made!!!
                    // UNTESTED!
                    call(t, tagMap, rval);
                    
                }
            }
            }

            if (sub.equals("v")) {
                JSONObject v = (JSONObject) c.get(sub);
                
                for (String subtype : v.keySet()) {
                
                    rval.append(",");
                    JSONObject substructure = v.getJSONObject(subtype);
                    rval.append(subtype + ":");
                    
                    // Call function recursively with substructure;
                    call(substructure, tagMap, rval);
                    
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
                    if(tagMap!=null) {
                        // Replace by variable name.
                        rval.append("_" + tagMap.get(index) + ",");
                    }
                    else {
                        rval.append(index + ",");
                    }
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
                call(first, tagMap, rval);
            }
            if (sub.equals("rest")) {
                rval.append("]");
                JSONObject rest = (JSONObject) c.get(sub);
                call(rest, tagMap, rval);
            }
        }
    }
    
}
