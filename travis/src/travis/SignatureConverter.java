/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travis;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import org.json.JSONObject;
import static travis.BoldeJSON.sortKeys;

/**
 *
 *
 *
 * @author niko
 */
public class SignatureConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {

    }

    public static void convertSignature(JSONObject obj, int indent,
            LinkedHashMap<String, String> typeToFeatures,
            StringBuilder sigBuilder,
            HashSet<String> typesWhoseFeaturesHaveAlreadyBeenAdded) throws FileNotFoundException {
        indent++;
        for (String aType : sortKeys(obj)) {
            StringBuilder indentB = new StringBuilder();
            for (int i = 0; i < indent; i++) {
                indentB.append("   ");
            }
            sigBuilder.append(indentB.toString() + aType.toLowerCase());
            if (typeToFeatures.containsKey(aType)) {
                if (!typesWhoseFeaturesHaveAlreadyBeenAdded.contains(aType)) {
                    sigBuilder.append(" " + typeToFeatures.get(aType).toLowerCase());
                    typesWhoseFeaturesHaveAlreadyBeenAdded.add(aType);
                } else {
                    //System.out.println("Sorry, the features for type: " + aType
                    //        + " have already been added to the signature at some other place.");
                }
            }
            sigBuilder.append("\n");
            if (obj.get(aType) instanceof JSONObject) {
                convertSignature(obj.getJSONObject(aType), indent, typeToFeatures, sigBuilder, typesWhoseFeaturesHaveAlreadyBeenAdded);
            }
        }
    }
}
