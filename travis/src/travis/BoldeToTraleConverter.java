package travis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
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
    // public static final String JSON_FILE = "files/p24/p24/files/f24.formatted.json";
    // public static final String JSON_FILE = "files/p24_new/F4_formatted.json";
    // public static final String JSON_FILE = "files/p24_new/f24_formatted.json";
    // public static final String JSON_FILE = "files/seminar/morakes/g4/F4";
    public static final String JSON_FILE = "files/F4-Louis.formatted.json";
    
    
    
    
    public static final boolean PRINT_TRALE_OUTPUT = true;
    public static final boolean WRITE_TRALE_OUTPUT = true;

    public static PrintWriter signatureW;
    public static PrintWriter lexiconW;
    public static PrintWriter rulesW;
    public static PrintWriter lexicalRules;
    public static PrintWriter macrosW;
    public static PrintWriter principlesW;
    public static PrintWriter theoryW;
            

    /**
     *
     *
     *
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {

        
        System.out.println("Converting JSON to Trale format...\n");
        String inputFile = JSON_FILE;
        String outFile = "trale_files";
        if(args.length != 2) {
            System.out.println("Usage: java -jar travis.jar JSON_INPUT TRALE_OUTPUT\n"
                    + "e.g., java jar travis.jar p24.json grammar24");
        }
        else {
            inputFile = args[0];
            outFile = args[1];
        }
        
        String jsonString = getJSONStringfromFile(inputFile);

        // Setup output files.
        if (WRITE_TRALE_OUTPUT) {
            File f = new File(outFile);
            f.mkdir();
            signatureW = new PrintWriter(f + "/signature");
            lexiconW = new PrintWriter(f + "/lexicon.pl");
            rulesW = new PrintWriter(f + "/rules.pl");
            lexicalRules = new PrintWriter(f + "/lexical_rules.pl");
            macrosW = new PrintWriter(f + "/macros.pl");
            principlesW = new PrintWriter(f + "/principles.pl");
            theoryW = new PrintWriter(f + "/theory.pl");

        }

        convertJSONtoTrale(jsonString);

        // Close all writers.
        if (WRITE_TRALE_OUTPUT) {
            signatureW.close();
            lexiconW.close();
            rulesW.close();
            lexicalRules.close();
            macrosW.close();
            principlesW.close();
            theoryW.close();
        }
        
        System.out.println("... done.");
    }

    
    
    /**
     * 
     * @param json
     * @throws FileNotFoundException 
     */
    private static void convertJSONtoTrale(String json) throws FileNotFoundException {
               
        if(WRITE_TRALE_OUTPUT) {
            theoryW.write("signature(signature).\n");
            theoryW.write(":- [lexicon].\n");
            theoryW.write(":- [rules].\n");
            theoryW.write(":- [lexical_rules].\n");
            theoryW.write(":- [macros].\n");
            theoryW.write(":- [principles].\n");
            theoryW.write("hidden_feat(dtrs).");
        }
        
        
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
                    for (int p = 0; p < lexEntries.length(); p++) {
                        if (!lexEntries.get(p).toString().equals("null")) {
                            JSONObject aLexEntry = (JSONObject) lexEntries.get(p);
                            //System.out.println(aLexEntry);
                            String trale = LexicalEntryConverter.convertLexEntry(aLexEntry);
                            if (PRINT_TRALE_OUTPUT) {
                                System.out.println(trale + "\n");
                            }
                            if (WRITE_TRALE_OUTPUT) {
                                lexiconW.write(trale + "\n");
                            }

                        }
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
                                    if (featsBuilder.toString().length() > 0) {
                                        typeToFeatures.put(typeName, featsBuilder.toString().trim());
                                    }
                                }
                                break;
                        }
                    }

                    // 
                    JSONObject type = obj.getJSONObject(aComp).getJSONObject("signature");//.getJSONObject("type");
                    StringBuilder sigBuilder = new StringBuilder();
                    HashSet<String> typesWhoseFeaturesHaveAlreadyBeenAdded = new  HashSet<String>();
                    SignatureConverter.convertSignature(type, 0, typeToFeatures, sigBuilder, typesWhoseFeaturesHaveAlreadyBeenAdded);
                    String signature = sigBuilder.insert(0, "type_hierarchy\nbot\n") + ".";
                    
                    if (PRINT_TRALE_OUTPUT) {
                        System.out.println(signature + "\n");
                    }
                    if (WRITE_TRALE_OUTPUT) {
                        signatureW.write(signature + "\n");
                    }

                    break;

                case "rules":
                    System.out.println("Converting rules...");
                    // Get all rule entries.
                    JSONArray ruleEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(ruleEntries);
                    for (int r = 0; r < ruleEntries.length(); r++) {
                        JSONObject aRuleEntry = (JSONObject) ruleEntries.get(r);
                        //System.out.println(aRuleEntry);
                        String trale = RuleConverter.convertRule(aRuleEntry);
                        if (PRINT_TRALE_OUTPUT) {
                            System.out.println(trale + "\n");
                        }
                        if (WRITE_TRALE_OUTPUT) {
                            rulesW.write(trale + "\n");
                        }
                    }
                    break;

                case "lexRules":
                        System.out.println("Converting lexical rules...");
                        // Get all lexRule entries.
                        JSONArray lexRuleEntries = (JSONArray) obj.getJSONArray(aComp);
                        //System.out.println(lexRuleEntries);
                        for (int p = 0; p < lexRuleEntries.length(); p++) {
                            JSONObject aLexRuleEntry = (JSONObject) lexRuleEntries.get(p);
                            //System.out.println(aLexRuleEntry);
                            String trale = LexRuleConverter.convertLexRule(aLexRuleEntry);
                            if (PRINT_TRALE_OUTPUT) {
                                System.out.println(trale + "\n");
                            }
                            if (WRITE_TRALE_OUTPUT) {
                                lexicalRules.write(trale + "\n");
                            }
                        
                    }
                    break;

                case "principles":
                    System.out.println("Converting principles...");
                    // Get all principle entries.
                    JSONArray principleEntries = (JSONArray) obj.getJSONArray(aComp);
                    //System.out.println(principleEntries);
                    for (int p = 0; p < principleEntries.length(); p++) {
                        JSONObject aPrincipleEntry = (JSONObject) principleEntries.get(p);
                        //System.out.println(aPrincipleEntry);
                        String trale = PrincipleConverter.convertPrinciple(aPrincipleEntry);
                        if (PRINT_TRALE_OUTPUT) {
                            System.out.println(trale + "\n");
                        }
                        if (WRITE_TRALE_OUTPUT) {
                            principlesW.write(trale + "\n");
                        }
                    }
                    break;

                case "abbreviations":
                    System.out.println("Converting abbreviations...");
                    // Get all abbreviations.
                    JSONArray abbr = (JSONArray) obj.getJSONArray(aComp);
                    // System.out.println(abbr);
                    for (int a = 0; a < abbr.length(); a++) {
                        JSONObject anAbbrEntry = (JSONObject) abbr.get(a);
                        //System.out.println(anAbbrEntry);
                        String trale = AbbreviationConverter.convertAbbreviation(anAbbrEntry);
                        if (PRINT_TRALE_OUTPUT) {
                            System.out.println(trale);
                        }
                        if (WRITE_TRALE_OUTPUT) {
                            macrosW.write(trale + "\n");
                        }
                    }
                    break;

            }

        }
    }

    private static String getJSONStringfromFile(String inputFile) throws FileNotFoundException {
        StringBuilder jsonsb = new StringBuilder();
        Scanner s = new Scanner(new File(inputFile));
        while (s.hasNextLine()) {
            String aLine = s.nextLine();
            jsonsb.append(aLine);
        }
        s.close();
        return jsonsb.toString();
    }

}
