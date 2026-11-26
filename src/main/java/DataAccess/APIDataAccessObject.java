package DataAccess;

import Entities.Item;
import UseCases.Buyers.ItemPricing.ItemPricingDataAccessInterface;
import UseCases.Sellers.EstimateNetProfit.EstimateNetProfitDataAccessInterface;
import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/** Main API Data Access Object. Handles eBay OAuth on construct */
public class APIDataAccessObject
    implements ItemPricingDataAccessInterface, EstimateNetProfitDataAccessInterface {
  private final String SCOPE_PRODUCTION = "https://api.ebay.com/oauth/api_scope";
  private final String BASE_URL = "https://api.ebay.com/buy/browse/v1/item_summary/search";

  private final String APIConfigFile;
  private final Optional<AccessToken> ApplicationAccessToken;
  private final OkHttpClient client = new OkHttpClient();

  /** Reads the supplied Config File containing API information and stores the API Token. */
  public APIDataAccessObject(String APIConfigFile) throws IOException {
    this.APIConfigFile = APIConfigFile;
    try {
      CredentialUtil.load(new FileInputStream(APIConfigFile));
    } catch (FileNotFoundException e) {
      // Re-throw as a RuntimeException or log a severe error and throw
      throw new IOException("eBay config file not found at: " + APIConfigFile, e);
    }

    OAuth2Api instancedAPI = new OAuth2Api();
    OAuthResponse response =
        instancedAPI.getApplicationToken(Environment.PRODUCTION, List.of(SCOPE_PRODUCTION));

    // Log the full response to see any error messages from eBay
    System.out.println("OAuth Response: " + response);

    this.ApplicationAccessToken = response.getAccessToken();
    if (this.ApplicationAccessToken.isEmpty()) {
      // If it's empty, log the error description from the response
      System.err.println(
          "Failed to get access token. Response errors: " + response.getErrorMessage());
    }
  }

  /** Searches the eBay API and returns an ArrayList of Item Entities matching the search term. */
  public ArrayList<Item> getItemsForSearchTerm(String searchTerm) throws IOException {
    // 1. Fail fast if the token is missing
    if (this.ApplicationAccessToken.isEmpty()) {
      throw new IllegalStateException(
          "No application access token available. "
              + "Check your eBay config file and OAuth credentials.");
    }

    String accessToken = this.ApplicationAccessToken.get().getToken();

    // 2. Build the request URL
    HttpUrl url =
        Objects.requireNonNull(HttpUrl.parse(this.BASE_URL))
            .newBuilder()
            .addQueryParameter("q", searchTerm)
            .build();

    // 3. Attach the required headers
    Request request =
        new Request.Builder()
            .url(url)
            .header("Authorization", "Bearer " + accessToken)
            .header(
                "X-EBAY-C-MARKETPLACE-ID", "EBAY_US") // Can support selecting region in the app?
            .build();

    // 4. Execute the call
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful() || response.body() == null) {
        throw new IOException(
            "eBay Browse API request failed: " + response.code() + " " + response.message());
      }

      // 5. Parse the JSON response
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(response.body().string());

      // Isolate the RootNode in the JSON object
      JsonNode summaries = root.path("itemSummaries");
      ArrayList<Item> items = new ArrayList<>();

      if (summaries.isArray()) {
        for (JsonNode summary : summaries) {
          // --- itemId ---
          String itemId = summary.path("itemId").asText(null);

          // --- title ---
          String title = summary.path("title").asText(null);

          // --- price & currency (JSON object: { "value": "...", "currency": "..." })
          JsonNode priceNode = summary.path("price");
          double price = 0.0;
          String currency = null;
          if (priceNode.isObject()) {
            price = priceNode.path("value").asDouble(0.0);
            currency = priceNode.path("currency").asText(null);
          }

          // --- condition ---
          String condition = summary.path("condition").asText(null);

          // --- category (primary only) ---
          String category = null;
          JsonNode categories = summary.path("categories");
          if (categories.isArray() && categories.size() > 0) {
            category = categories.get(0).path("categoryName").asText(null);
          }

          // 6. Build the domain entity
          items.add(new Item(itemId, title, price, currency, condition, category));
        }
      }

      return items;
    }
  }
}
