using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace RecipeWizardServer
{
    public partial class Ingredients : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            LoadData();
        }

        private void LoadData()
        {
            var ingredientsList = RecipeWizardServer.Database.Ingredient.LoadAll().ToList();

            GridView1.DataSource = ingredientsList;
            GridView1.DataBind();
        }
    }
}