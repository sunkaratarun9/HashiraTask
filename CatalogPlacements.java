import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CatalogPlacements {
    public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(new FileReader("input.json"));

        JSONObject keys = (JSONObject) json.get("keys");
        int n = Integer.parseInt(keys.get("n").toString());
        int k = Integer.parseInt(keys.get("k").toString());

        List<Integer> xVals = new ArrayList<>();
        List<BigInteger> yVals = new ArrayList<>();

        // Read and decode y values
        for (Object keyObj : json.keySet()) {
            String key = keyObj.toString();
            if (key.equals("keys")) continue;

            JSONObject point = (JSONObject) json.get(key);
            int base = Integer.parseInt(point.get("base").toString());
            String value = point.get("value").toString();

            BigInteger yDecoded = new BigInteger(value, base);
            xVals.add(Integer.parseInt(key));
            yVals.add(yDecoded);
        }

        // Sort points by x
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < xVals.size(); i++) indices.add(i);
        indices.sort(Comparator.comparingInt(xVals::get));

        // Take only first k points
        double[][] points = new double[k][2];
        for (int i = 0; i < k; i++) {
            points[i][0] = xVals.get(indices.get(i));
            points[i][1] = yVals.get(indices.get(i)).doubleValue();
        }

        // Lagrange interpolation at x=0
        double secret = 0;
        for (int i = 0; i < k; i++) {
            double term = points[i][1];
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0 - points[j][0]) / (points[i][0] - points[j][0]);
                }
            }
            secret += term;
        }

        // Print only the secret C
        System.out.println(Math.round(secret));
    }
}
