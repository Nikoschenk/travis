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
import org.json.JSONArray;
import org.json.JSONObject;
import static travis.BoldeJSON.sortKeys;

/**
 *
 * @author niko
 */
public class BoldeToTraleConverter {
    
    
    // Format command for json pretty print:
    // cat unformatted.json | python -m json.tool > formatted.json
    
    
    
    // Gert's first grammar.
   // public static final String JSON_FILE = "files/GertsClone_reduced_hierarchy/files/spanish.json";
    
    // Gert's second grammar. 
    // public static final String JSON_FILE = "files/G11_fixed/files/spanish_formatted.json";
    // public static final String JSON_FILE = "files/G11.json";
    // public static final String JSON_FILE = "files/f73.json.form.json";
    //public static final String JSON_FILE = "files/p24/p24/files/f24.formatted.json";
    
     //public static final String JSON_FILE = "files/p24_new/F4_formatted.json";
     public static final String JSON_FILE = "files/p24_new/f24_formatted.json";
     
     public static final boolean CONVERT_LEXRULES = true;
     
     public static final boolean PRINT_LEXICALENTRY = true;
     public static final boolean PRINT_PRINCIPLE = true;
     public static final boolean PRINT_ABBREVIATION = true;
     public static final boolean PRINT_LEXRULE = true;
     public static final boolean PRINT_RULE = true;
     public static final boolean PRINT_SIGNATURE = true;

    /**
     * 
     * 
     *
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {

       
        System.out.println("Converting JSON to Trale format...\n\n");
        StringBuilder sb = new StringBuilder();
        Scanner s = new Scanner(new File(JSON_FILE));
        while (s.hasNextLine()) {
            String aLine = s.nextLine();
            sb.append(aLine);
        }
        s.close();

        
        String json = sb.toString();
        JSONObject obj = new JSONObject(json);

        //  System.out.println(principles);
        for (String aComp : sortKeys(obj)) {
            switch (aComp) {
                
                // Gert refactored "principles" to serve the purpose of a 
                // lexical entry.
                case "lexicon":
                    System.out.println("Converting lexical entries...");
                    // Get all lex entries.
                    JSONArray lexEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(lexEntries);
                    for(int p = 0; p < lexEntries.length(); p++) {
                        JSONObject aLexEntry = (JSONObject)lexEntries.get(p);
                        //System.out.println(aLexEntry);
                        String trale = LexicalEntryConverter.convertLexEntry(aLexEntry);
                        if(PRINT_LEXICALENTRY) System.out.println(trale + "\n");
                    }
                break;
                
                
                    // Original Bolde Lexicon Entry Converter.
//                case "lexicon":
//                    System.out.println("Converting lexical entries (BOLDE ORIGINAL)...");
//                    // Get all lexicon entries.
//                    JSONArray lexiconEntries = (JSONArray) obj.getJSONArray(aComp);
//                    //System.out.println(lexiconEntries);
//                    for(int l = 0; l < lexiconEntries.length(); l++) {
//                        JSONObject aLexEntry = (JSONObject)lexiconEntries.get(l);
//                        //System.out.println(aLexEntry);
//
//                        ArrayList<String> lexemeStrings = new ArrayList<>();
//                        
//                        
//                        for (String aLexComp : sortKeys(aLexEntry)) {
//                            switch (aLexComp) {
//                                case "common":
//                                    break;
//                                case "lexemes":
//                                    JSONArray lexemes = (JSONArray) aLexEntry.get(aLexComp);
//                                    for(int anL = 0; anL < lexemes.length(); anL++) {
//                                        JSONArray aLexemeStr = (JSONArray)lexemes.get(anL);
//                                        lexemeStrings.add(aLexemeStr.getString(0));
//                                    }
//                                    //System.out.println("\t" + lexemeStrings);
//                                    break;
//                                case "name":
//                                    //System.out.println("\t" + aLexEntry.get(aLexComp));
//                                    break;
//                                case "nparams":
//                                    break;
//                                case "values":
//                                    JSONArray values = (JSONArray) aLexEntry.get(aLexComp);
//                                    // Hopefully, this structure does not change.
//                                    JSONArray item = (JSONArray) values.get(0);
//                                    JSONObject lexInfo = (JSONObject) item.get(3);
//
//                                    //System.out.println("\t" + lexInfo);
//                                    for(String aLexemeString : lexemeStrings) {
//                                        // Get trale.
//                                        String lexStrTrale = LexicalEntryConverterBoldeOriginal.convertLexiconEntry(lexInfo);
//                                        // print trale format.
//                                        if(PRINT_LEXICALENTRY) System.out.println(aLexemeString.toLowerCase() + " ---> " + lexStrTrale + "\n");
//                                    }
//                                    break;
//                            }
//                        }
//                    }
//                break;

                case "whatever":
                    break;
                    
                // signature
                case "global":
                    System.out.println("Converting signature...");
                    LinkedHashMap<String, String> typeToFeatures = new LinkedHashMap<>();
                                
                    // Get the features that the user specified in the GUI.
                    for (String anotherComp : sortKeys(obj)) {
                        switch (anotherComp) {
                            case "declarations":
                                JSONArray declarationEntries = (JSONArray) obj.getJSONArray(anotherComp);
                                for (int d = 0; d < declarationEntries.length(); d++) {
                                    JSONObject aDeclarationEntry = (JSONObject) declarationEntries.get(d);
                                    //System.out.println(aDeclarationEntry);
                                    // Get the type.
                                    String typeName = aDeclarationEntry.getString("name");
                                    
                                    // Get its features and their types.
                                    JSONObject v = aDeclarationEntry.getJSONObject("fstr").getJSONObject("v");
                                    StringBuilder featsBuilder = new StringBuilder();
                                    for (String aFeature : sortKeys(v)) {
                                        featsBuilder.append(aFeature + ":");
                                        JSONObject typeOfFeatureObj = (JSONObject) v.get(aFeature);
                                        String typeOfFeature = (String) typeOfFeatureObj.get("e");
                                        featsBuilder.append(typeOfFeature + " ");
                                    }
                                    if(featsBuilder.toString().length() > 0) {
                                        typeToFeatures.put(typeName, featsBuilder.toString().trim());
                                    }
                                }
                                break;
                        }
                    }
                    
                    // 
                    JSONObject type = obj.getJSONObject(aComp).getJSONObject("signature").getJSONObject("type");
                    String tralesig = SignatureConverter.convertSignature(type, -1, typeToFeatures);
                    if(PRINT_SIGNATURE) System.out.println(tralesig + "\n");
                    
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
                    
                     
                case "lexRules":
                    if(CONVERT_LEXRULES) {
                    System.out.println("Converting lexical rules...");
                    // Get all lexRule entries.
                    JSONArray lexRuleEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(lexRuleEntries);
                    for(int p = 0; p < lexRuleEntries.length(); p++) {
                        JSONObject aLexRuleEntry = (JSONObject)lexRuleEntries.get(p);
                        //System.out.println(aLexRuleEntry);
                        String trale = LexRuleConverter.convertLexRule(aLexRuleEntry);
                        if(PRINT_LEXRULE) System.out.println(trale + "\n");
                    }
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
                
                case "abbreviations":
                    System.out.println("Converting abbreviations...");
                    // Get all lex entries.
                    JSONArray abbr = (JSONArray) obj.getJSONArray(aComp);
                   // System.out.println(abbr);
                    for(int a = 0; a < abbr.length(); a++) {
                        JSONObject anAbbrEntry = (JSONObject) abbr.get(a);
                        //System.out.println(anAbbrEntry);
                        String trale = AbbreviationConverter.convertAbbreviation(anAbbrEntry);
                        if(PRINT_ABBREVIATION) System.out.println(trale);
                    }
                    System.out.println();
                    break;

            }
            
        }
    }
    
}
