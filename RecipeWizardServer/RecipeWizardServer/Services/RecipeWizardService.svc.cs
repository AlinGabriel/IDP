using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using RecipeWizardServer.Database;

namespace RecipeWizardServer.Services
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "RecipeWizardService" in code, svc and config file together.
    public class RecipeWizardService : IRecipeWizardService
    {
        public int RegisterUser(string emailAddress, string userName, string password)
        {
            User newUser = new User();
            newUser.Email = emailAddress;
            newUser.Username = userName;
            newUser.Password = password; // TODO: hash password
            newUser.CreatedOn = DateTime.Now;

            newUser.Save();

            return newUser.Id;
        }

        public int LoginUser(string userName, string password)
        {
            var resultUser = User.LoadByUsernameAndPassword(userName, password);
            if (resultUser == null)
                return -1;

            //TODO: update LastAccess time and update DB user

            return resultUser.Id;
        }

        public List<RecipeDataResponse> GetAllRecipes()
        {
            var allRecipes = Recipe.LoadAll();
            List<RecipeDataResponse> results = new List<RecipeDataResponse>();

            foreach (var recipe in allRecipes)
            {
                results.Add(new RecipeDataResponse() { Id = recipe.Id, Description = recipe.Description, Name = recipe.Name });
            }
            
            return results;
        }

        public List<RecipeIngredientsDataResponse> GetRecipeIngredients(int recipeId)
        {
            var recipeIngredients = RecipeIngredient.LoadByRecipeId(recipeId);
            List<RecipeIngredientsDataResponse> results = new List<RecipeIngredientsDataResponse>();

            foreach (var recipeIngredient in recipeIngredients)
            {
                results.Add(new RecipeIngredientsDataResponse() { IngredientName = recipeIngredient.Ingredient.Name, RecipeName = recipeIngredient.Recipe.Name, Quantity = recipeIngredient.Quantity });
            }

            return results;
        }

        public RecipeDataResponse GetSuggestedRecipe(Dictionary<int, int> availableIngredients)
        {
            bool foundRecipe = false;
            RecipeDataResponse resultRecipe = new RecipeDataResponse();

            var allRecipes = Recipe.LoadAll();

            for (int missingIngredients = 0; missingIngredients < 5 && !foundRecipe; missingIngredients++) // max 5 missing ingredients
            {
                foreach (var recipe in allRecipes)
                {
                    bool allIngredientsInStock = true;
                    foreach (var recipeIngredient in recipe.RecipeIngredients)
                    {
                        if (availableIngredients.Keys.Contains(recipeIngredient.IngredientId)) // checking if recipe ingredient is in my inventory
                        {
                            int quantity;
                            availableIngredients.TryGetValue(recipeIngredient.Id, out quantity);

                            if (quantity < recipeIngredient.Quantity) // checking if available ingredient is in enough quantity
                            {
                                // available ingredient quantity not enough

                                allIngredientsInStock = false;
                                break; // if not, we can skip the iteration through this recipe's ingredients
                            }
                        }
                        else
                        {
                            // ingredient isn't in my inventory

                            allIngredientsInStock = false;
                            break; // we can skip the iteration through this recipe's ingredients
                        }
                    }

                    if (allIngredientsInStock)
                    {
                        // if all of the recipe ingredients are OK
                        foundRecipe = true;

                        // create response structure
                        resultRecipe.Id = recipe.Id;
                        resultRecipe.Name = recipe.Name;
                        resultRecipe.Description = recipe.Description;

                        break; // we can stop iterating through other recipes as well
                    }
                }
            }

            return resultRecipe;
        }

        public bool RateRecipe(int userId, int recipeId, int rating)
        {
            UserRecipeRating newRating = new UserRecipeRating() { UserId = userId, RecipeId = recipeId, Rating = rating };
            newRating.Save();

            return true;
        }

        public string HelloWorld()
        {
            return "Hello World!";
        }
    }
}
