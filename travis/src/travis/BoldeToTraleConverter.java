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
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author niko
 */
public class BoldeToTraleConverter {
    
    
    
    
     public static final String JSON_FILE = "files/formatted.json";
     
     
     public static final boolean PRINT_LEXICALENTRY = false;
     public static final boolean PRINT_PRINCIPLE = false;

    /**
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
        for (String aComp : obj.keySet()) {
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
                        
                        TreeSet<String> sorted = new TreeSet<String>();
                        sorted.addAll(aLexEntry.keySet());
                        for (String aLexComp : sorted) {
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
                    break;
                        
                    
                case "principles":
                    System.out.println("Converting principles...");
                    // Get all lexicon entries.
                    JSONArray principleEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(principleEntries);
                    for(int l = 0; l < principleEntries.length(); l++) {
                        JSONObject aPrincipleEntry = (JSONObject)principleEntries.get(l);
                        //System.out.println(aPrincipleEntry);
                        String trale = PrinciplesConverter.convertPrinciple(aPrincipleEntry);
                        if(PRINT_PRINCIPLE) System.out.println(trale + "\n");
                    }
                break;
                        
                        
            }
            
        }
    }
    
}
