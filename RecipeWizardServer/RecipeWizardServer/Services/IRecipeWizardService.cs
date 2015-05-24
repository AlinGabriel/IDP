﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.ServiceModel.Web;

namespace RecipeWizardServer.Services
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IRecipeWizardService" in both code and config file together.
    [ServiceContract]
    public interface IRecipeWizardService
    {
        //[WebGet(UriTemplate = "HelloWorld", ResponseFormat = WebMessageFormat.Json)]
        [OperationContract(Name = "HelloWorld")]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "HelloWorld")]
        string HelloWorld();

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "RegisterUser/?emailAddress={emailAddress}&userName={userName}&password={password}")]
        int RegisterUser(string emailAddress, string userName, string password);

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "LoginUser/?userName={userName}&password={password}")]
        int LoginUser(string userName, string password);

        [OperationContract]
        [WebInvoke(Method = "GET",
            ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Wrapped,
            UriTemplate = "GetAllRecipes")]
        List<RecipeDataResponse> GetAllRecipes();
    }

    [DataContract]
    public class RecipeDataResponse
    {
        [DataMember]
        public int Id { get; set; }
        [DataMember]
        public string Name { get; set; }
        [DataMember]
        public string Description { get; set; }
    }
}
