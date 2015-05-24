using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using RecipeWizardServer.Database;

namespace RecipeWizardServer
{
    public partial class _Default : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            labelUsers.Text = RecipeWizardServer.Database.User.LoadAll().Count().ToString() + " registered user(s)";
            labelRecipes.Text = RecipeWizardServer.Database.Recipe.LoadAll().Count().ToString() + " registered recipe(s)";
            labelIngredients.Text = RecipeWizardServer.Database.Ingredient.LoadAll().Count().ToString() + " registered ingredient(s)";
        }
    }
}
