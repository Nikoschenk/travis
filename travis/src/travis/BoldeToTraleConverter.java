/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import static travis.BoldeJSON.sortKeys;

/**
 *
 * @author niko
 */
public class BoldeToTraleConverter {
    
    
    
    
     //public static final String JSON_FILE = "files/BOLDE_flat_typehiararchy/formatted.json";
     public static final String JSON_FILE = "files/G11_fixed/files/spanish_formatted.json";
     
     public static final boolean PRINT_LEXICALENTRY = true;
     public static final boolean PRINT_PRINCIPLE = true;
     public static final boolean PRINT_RULE = true;

    /**
     * 
     * 
     *
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {

       
        System.out.println("Converting JSON to Trale format...");
        StringBuilder sb = new StringBuilder();
        Scanner s = new Scanner(new File(JSON_FILE));
        while (s.hasNextLine()) {
            String aLine = s.nextLine();
            sb.append(aLine);
        }
        s.close();

        //int a = obj.getInt("age");
        // String s= obj.getString("name");
        String json = sb.toString();
        JSONObject obj = new JSONObject(json);

        //  System.out.println(principles);
        for (String aComp : sortKeys(obj)) {
            switch (aComp) {
                case "lexicon":
                    System.out.println("Converting lexical entries...");
                    // Get all lexicon entries.
                    JSONArray lexiconEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(lexiconEntries);
                    for(int l = 0; l < lexiconEntries.length(); l++) {
                        JSONObject aLexEntry = (JSONObject)lexiconEntries.get(l);
                        //System.out.println(aLexEntry);

                        ArrayList<String> lexemeStrings = new ArrayList<>();
                        
                        
                        for (String aLexComp : sortKeys(aLexEntry)) {
                            switch (aLexComp) {
                                case "common":
                                    break;
                                case "lexemes":
                                    JSONArray lexemes = (JSONArray) aLexEntry.get(aLexComp);
                                    for(int anL = 0; anL < lexemes.length(); anL++) {
                                        JSONArray aLexemeStr = (JSONArray)lexemes.get(anL);
                                        lexemeStrings.add(aLexemeStr.getString(0));
                                    }
                                    //System.out.println("\t" + lexemeStrings);
                                    break;
                                case "name":
                                    //System.out.println("\t" + aLexEntry.get(aLexComp));
                                    break;
                                case "nparams":
                                    break;
                                case "values":
                                    JSONArray values = (JSONArray) aLexEntry.get(aLexComp);
                                    // Hopefully, this structure does not change.
                                    JSONArray item = (JSONArray) values.get(0);
                                    JSONObject lexInfo = (JSONObject) item.get(3);

                                    //System.out.println("\t" + lexInfo);
                                    for(String aLexemeString : lexemeStrings) {
                                        // Get trale.
                                        String lexStrTrale = LexicalEntryConverter.convertLexiconEntry(lexInfo);
                                        // print trale format.
                                        if(PRINT_LEXICALENTRY) System.out.println(aLexemeString.toLowerCase() + " => " + lexStrTrale + "\n");
                                    }
                                    break;
                            }
                        }
                    }
                break;

                case "whatever":
                    break;
                    
                case "rules":
                    System.out.println("Converting rules...");
                    // Get all rule entries.
                    JSONArray ruleEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(ruleEntries);
                    for(int r = 0; r < ruleEntries.length(); r++) {
                        JSONObject aRuleEntry = (JSONObject)ruleEntries.get(r);
                        //System.out.println(aRuleEntry);
                        String trale = RuleConverter.convertRule(aRuleEntry);
                        if(PRINT_RULE) System.out.println(trale + "\n");
                    }
                    break;
                        
                    
                case "principles":
                    System.out.println("Converting principles...");
                    // Get all principle entries.
                    JSONArray principleEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(principleEntries);
                    for(int p = 0; p < principleEntries.length(); p++) {
                        JSONObject aPrincipleEntry = (JSONObject)principleEntries.get(p);
                        //System.out.println(aPrincipleEntry);
                        String trale = PrincipleConverter.convertPrinciple(aPrincipleEntry);
                        if(PRINT_PRINCIPLE) System.out.println(trale + "\n");
                    }
                break;
                        
                        
            }
            
        }
    }
    
}
