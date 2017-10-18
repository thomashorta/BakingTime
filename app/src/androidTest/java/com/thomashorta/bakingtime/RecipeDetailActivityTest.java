package com.thomashorta.bakingtime;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.thomashorta.bakingtime.model.Recipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class, false, false);

    private static final String DESCRIPTION = "Combine the cake flour, 400 grams (2 cups) of sugar, baking powder, and 1 teaspoon of salt in the bowl of a stand mixer. Using the paddle attachment, beat at low speed until the dry ingredients are mixed together, about one minute";

    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailActivity() {
        Recipe mockRecipe = mockRecipe();
        Intent intent = new Intent();
        intent.putExtra(RecipeDetailFragment.EXTRA_RECIPE, mockRecipe);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.rv_recipe_detail))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        // check if activity title (in action bar) matches the recipe name
        onView(withId(R.id.tv_step_detail_description)).check(matches(withText(DESCRIPTION)));
    }

    private Recipe mockRecipe() {
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(mockRecipeJson, Recipe.class);
        return recipe;
    }
    private final String mockRecipeJson = "{\n" +
            "  \"id\": 3,\n" +
            "  \"name\": \"Yellow Cake\",\n" +
            "  \"ingredients\": [\n" +
            "    {\n" +
            "      \"quantity\": 400,\n" +
            "      \"measure\": \"G\",\n" +
            "      \"ingredient\": \"sifted cake flour\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 700,\n" +
            "      \"measure\": \"G\",\n" +
            "      \"ingredient\": \"granulated sugar\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 4,\n" +
            "      \"measure\": \"TSP\",\n" +
            "      \"ingredient\": \"baking powder\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 1.5,\n" +
            "      \"measure\": \"TSP\",\n" +
            "      \"ingredient\": \"salt\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 2,\n" +
            "      \"measure\": \"TBLSP\",\n" +
            "      \"ingredient\": \"vanilla extract, divided\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 8,\n" +
            "      \"measure\": \"UNIT\",\n" +
            "      \"ingredient\": \"egg yolks\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 323,\n" +
            "      \"measure\": \"G\",\n" +
            "      \"ingredient\": \"whole milk\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 961,\n" +
            "      \"measure\": \"G\",\n" +
            "      \"ingredient\": \"unsalted butter, softened and cut into 1 in. cubes\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 6,\n" +
            "      \"measure\": \"UNIT\",\n" +
            "      \"ingredient\": \"egg whites\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"quantity\": 283,\n" +
            "      \"measure\": \"G\",\n" +
            "      \"ingredient\": \"melted and cooled bittersweet or semisweet chocolate\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"steps\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"shortDescription\": \"Recipe Introduction\",\n" +
            "      \"description\": \"Recipe Introduction\",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"shortDescription\": \"Starting prep\",\n" +
            "      \"description\": \"1. Preheat the oven to 350\\u00b0F. Butter the bottoms and sides of two 9\\\" round pans with 2\\\"-high sides. Cover the bottoms of the pans with rounds of parchment paper, and butter the paper as well.\",\n" +
            "      \"videoURL\": \"\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 2,\n" +
            "      \"shortDescription\": \"Combine dry ingredients.\",\n" +
            "      \"description\": \"2. Combine the cake flour, 400 grams (2 cups) of sugar, baking powder, and 1 teaspoon of salt in the bowl of a stand mixer. Using the paddle attachment, beat at low speed until the dry ingredients are mixed together, about one minute\",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde28_1-mix-all-dry-ingredients-yellow-cake/1-mix-all-dry-ingredients-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"shortDescription\": \"Prepare wet ingredients.\",\n" +
            "      \"description\": \"3. Lightly beat together the egg yolks, 1 tablespoon of vanilla, and 80 grams (1/3 cup) of the milk in a small bowl.\",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde36_2-mix-all-wet-ingredients-yellow-cake/2-mix-all-wet-ingredients-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 4,\n" +
            "      \"shortDescription\": \"Add butter and milk to dry ingredients.\",\n" +
            "      \"description\": \"4. Add 283 grams (20 tablespoons) of butter and 243 grams (1 cup) of milk to the dry ingredients. Beat at low speed until the dry ingredients are fully moistened, using a spatula to help with the incorporation if necessary. Then beat at medium speed for 90 seconds.\",\n" +
            "      \"videoURL\": \"\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 5,\n" +
            "      \"shortDescription\": \"Add egg mixture to batter.\",\n" +
            "      \"description\": \"5. Scrape down the sides of the bowl. Add the egg mixture to the batter in three batches, beating for 20 seconds each time and then scraping down the sides.\",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde36_2-mix-all-wet-ingredients-yellow-cake/2-mix-all-wet-ingredients-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 6,\n" +
            "      \"shortDescription\": \"Pour batter into pans.\",\n" +
            "      \"description\": \"6. Pour the mixture in two even batches into the prepared pans. Bake for 25 minutes or until a tester comes out of the cake clean. The cake should only start to shrink away from the sides of the pan after it comes out of the oven.\",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde43_5-add-mixed-batter-to-baking-pans-yellow-cake/5-add-mixed-batter-to-baking-pans-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 8,\n" +
            "      \"shortDescription\": \"Begin making buttercream.\",\n" +
            "      \"description\": \"8. Once the cake is cool, it's time to make the buttercream. You'll start by bringing an inch of water to a boil in a small saucepan. You'll want to use a saucepan that is small enough that when you set the bowl of your stand mixer in it, the bowl does not touch the bottom of the pot.\",\n" +
            "      \"videoURL\": \"\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 9,\n" +
            "      \"shortDescription\": \"Prepare egg whites.\",\n" +
            "      \"description\": \"9. Whisk together the egg whites and remaining 300 grams (1.5 cups) of sugar in the bowl of a stand mixer until combined. Set the bowl over the top of the boiling water and continue whisking the egg white mixture until it feels hot to the touch and the sugar is totally dissolved (if you have a reliable thermometer, it should read 150\\u00b0F). \",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/5901299d_6-srir-egg-whites-for-frosting-yellow-cake/6-srir-egg-whites-for-frosting-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10,\n" +
            "      \"shortDescription\": \"Beat egg whites to stiff peaks.\",\n" +
            "      \"description\": \"10. Remove the bowl from the pot, and using the whisk attachment of your stand mixer, beat the egg white mixture on medium-high speed until stiff peaks form and the outside of the bowl reaches room temperature.\",\n" +
            "      \"videoURL\": \"\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 11,\n" +
            "      \"shortDescription\": \"Add butter to egg white mixture.\",\n" +
            "      \"description\": \"11. Keeping the mixer at medium speed, add the butter one piece at a time to the egg white mixture, waiting 5 to 10 seconds between additions. If the mixture starts to look curdled, just keep beating it! It will come together once it has been mixed enough and has enough butter added. \",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/590129a3_9-mix-in-butter-for-frosting-yellow-cake/9-mix-in-butter-for-frosting-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 12,\n" +
            "      \"shortDescription\": \"Finish buttercream icing.\",\n" +
            "      \"description\": \"12. With the mixer still running, pour the melted chocolate into the buttercream. Then add the remaining tablespoon of vanilla and 1/2 teaspoon of salt. Beat at high speed for 30 seconds to ensure the buttercream is well-mixed.\",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/590129a5_10-mix-in-melted-chocolate-for-frosting-yellow-cake/10-mix-in-melted-chocolate-for-frosting-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 13,\n" +
            "      \"shortDescription\": \"Frost cakes.\",\n" +
            "      \"description\": \"13. Frost your cake! Use a serrated knife to cut each cooled cake layer in half (so that you have 4 cake layers). Frost in between the layers, the sides of the cake, and the top of the cake. Then eat it!\",\n" +
            "      \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/590129ad_17-frost-all-around-cake-yellow-cake/17-frost-all-around-cake-yellow-cake.mp4\",\n" +
            "      \"thumbnailURL\": \"\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"servings\": 8,\n" +
            "  \"image\": \"\"\n" +
            "}";
}
