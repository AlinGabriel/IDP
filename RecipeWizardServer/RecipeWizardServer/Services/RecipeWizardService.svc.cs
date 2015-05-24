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

        public string HelloWorld()
        {
            return "Hello World!";
        }
    }
}
