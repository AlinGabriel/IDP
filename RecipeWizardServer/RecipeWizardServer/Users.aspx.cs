using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using RecipeWizardServer.Database;

namespace RecipeWizardServer
{
    public partial class Users : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            LoadData();
        }

        private void LoadData()
        {
            var usersList = RecipeWizardServer.Database.User.LoadAll().ToList();

            GridView1.DataSource = usersList;
            GridView1.DataBind();
        }
    }
}