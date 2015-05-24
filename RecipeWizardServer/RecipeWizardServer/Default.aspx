<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.master" AutoEventWireup="true"
    CodeBehind="Default.aspx.cs" Inherits="RecipeWizardServer._Default" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <h2>
        Welcome to the Recipe Wizard - Dashboard
    </h2>
    <p>
        User Statistics
        <asp:Label Font-Bold="true" ID="labelUsers" runat="server"></asp:Label>
    </p>
    <p>
        Recipe Statistics
        <asp:Label Font-Bold="true" ID="labelRecipes" runat="server"></asp:Label>
    </p>
    <p>
        Ingredient Statistics
        <asp:Label Font-Bold="true" ID="labelIngredients" runat="server"></asp:Label>
    </p>
</asp:Content>
