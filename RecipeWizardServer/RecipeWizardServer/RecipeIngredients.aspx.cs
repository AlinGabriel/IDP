using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace RecipeWizardServer
{
    public partial class RecipeIngredients : System.Web.UI.Page
    {
        private RecipeWizardServer.Database.Recipe mRecipe = null;

        protected void Page_Load(object sender, EventArgs e)
        {
            string recipeId = Request.QueryString["RecipeId"];
            if (!string.IsNullOrEmpty(recipeId))
            {
                int id;
                if (int.TryParse(recipeId, out id))
                {
                    mRecipe = RecipeWizardServer.Database.Recipe.LoadById(id);
                }
            }

            LoadData();
        }

        private void LoadData()
        {
            if (mRecipe == null)
                return;

            var recipeIngredients = RecipeWizardServer.Database.RecipeIngredient.LoadByRecipeId(mRecipe.Id);

            GridView1.DataSource = recipeIngredients;
            GridView1.DataBind();
        }
    }
}