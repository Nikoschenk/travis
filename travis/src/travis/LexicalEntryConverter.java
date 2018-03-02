/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONObject;
import static travis.BoldeJSON.call;
import static travis.BoldeJSON.normalize;
import static travis.BoldeJSON.readJSON;

/**
 *
 * @author niko
 */
public class LexicalEntryConverter {

    public static final String JSON_FILE = "files/intrans_verbs.json";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        System.out.println("Converting JSON to Trale format...");
    
        String json = readJSON(JSON_FILE);
        JSONObject obj = new JSONObject(json);
        String trale = convertLexiconEntry(obj);
        System.out.println(trale);
    }
    
    public static String convertLexiconEntry(JSONObject obj) throws FileNotFoundException {
        StringBuilder rval = new StringBuilder();
        call(obj, rval);
        rval.append(".");
        String trale = normalize(rval.toString());
        return trale;
    }
}
