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
            RecipeWizardServer.Database.User newUser = RecipeWizardServer.Database.User.LoadById(1);
        }
    }
}
