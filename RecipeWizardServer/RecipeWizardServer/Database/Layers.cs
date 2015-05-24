using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Configuration;

namespace RecipeWizardServer.Database
{
    public partial class User
    {
        public void Detach()
        {
            this.PropertyChanged = null;
            this.PropertyChanging = null;
        }

        public static User LoadById(int id)
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                return dataContext.Users.Where(user => user.Id == id).FirstOrDefault();
            }
        }

        public static IEnumerable<User> LoadAll()
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                return dataContext.Users.ToList();
            }
        }

        public static User LoadByUsernameAndPassword(string username, string password)
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                return dataContext.Users.Where(user => user.Username == username && user.Password == password).FirstOrDefault();
            }
        }

        public void Save()
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                this.Detach();

                if (this.Id <= 0)
                    dataContext.Users.InsertOnSubmit(this);
                else
                    this.Clone(dataContext.Users.SingleOrDefault(user => user.Id == this.Id));

                dataContext.SubmitChanges();
            }
        }

        public User Clone()
        {
            var targetUser = new User();
            return Clone(targetUser);
        }

        public User Clone(User targetUser)
        {
            targetUser.Id = this.Id;
            targetUser.CreatedOn = this.CreatedOn;
            targetUser.Email = this.Email;
            targetUser.LastAccess = this.LastAccess;
            targetUser.Password = this.Password;
            targetUser.Username = this.Username;

            return targetUser;
        }
    }

    public partial class Recipe
    {
        public void Detach()
        {
            this.PropertyChanged = null;
            this.PropertyChanging = null;
        }

        public static Recipe LoadById(int id)
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                return dataContext.Recipes.Where(recipe => recipe.Id == id).FirstOrDefault();
            }
        }

        public static IEnumerable<Recipe> LoadAll()
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                return dataContext.Recipes.ToList();
            }
        }

        public void Save()
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                this.Detach();

                if (this.Id <= 0)
                    dataContext.Recipes.InsertOnSubmit(this);
                else
                    this.Clone(dataContext.Recipes.SingleOrDefault(recipe => recipe.Id == this.Id));

                dataContext.SubmitChanges();
            }
        }

        public Recipe Clone()
        {
            var targetRecipe = new Recipe();
            return Clone(targetRecipe);
        }

        public Recipe Clone(Recipe targetRecipe)
        {
            targetRecipe.Id = this.Id;
            targetRecipe.Description = this.Description;
            targetRecipe.ExternalLink = this.ExternalLink;
            targetRecipe.Name = this.Name;

            return targetRecipe;
        }
    }

    public partial class Ingredient
    {
        public void Detach()
        {
            this.PropertyChanged = null;
            this.PropertyChanging = null;
        }

        public static Ingredient LoadById(int id)
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                return dataContext.Ingredients.Where(ingredient => ingredient.Id == id).FirstOrDefault();
            }
        }

        public static IEnumerable<Ingredient> LoadAll()
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                return dataContext.Ingredients.ToList();
            }
        }

        public void Save()
        {
            using (var dataContext = new LayersDataContext(ConfigurationManager.ConnectionStrings["RecipeWizardDBConnectionString"].ConnectionString))
            {
                this.Detach();

                if (this.Id <= 0)
                    dataContext.Ingredients.InsertOnSubmit(this);
                else
                    this.Clone(dataContext.Ingredients.SingleOrDefault(ingredient => ingredient.Id == this.Id));

                dataContext.SubmitChanges();
            }
        }

        public Ingredient Clone()
        {
            var targetIngredient = new Ingredient();
            return Clone(targetIngredient);
        }

        public Ingredient Clone(Ingredient targetIngredient)
        {
            targetIngredient.Id = this.Id;
            targetIngredient.Name = this.Name;

            return targetIngredient;
        }
    }
}