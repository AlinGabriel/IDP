﻿<%@ Page Title="Recipes Page" Language="C#" AutoEventWireup="true" MasterPageFile="~/Site.master" CodeBehind="Recipes.aspx.cs" Inherits="RecipeWizardServer.Recipes" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>

<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <h2>
        Recipes List
    </h2>
    <p>
        <asp:GridView ID="GridView1" runat="server" CellPadding="4" ForeColor="#333333" 
            GridLines="None" AutoGenerateEditButton="True" 
            onrowediting="GridView1_RowEditing" onrowupdating="GridView1_RowUpdating" 
            style="margin-right: 3px">
            <AlternatingRowStyle BackColor="White" />
            <Columns>
                <asp:HyperLinkField DataNavigateUrlFields="Id" 
                    DataNavigateUrlFormatString="RecipeIngredients.aspx?RecipeId={0}" 
                    Text="View Ingredients" />
            </Columns>
            <EditRowStyle BackColor="#2461BF" />
            <FooterStyle BackColor="#507CD1" Font-Bold="True" ForeColor="White" />
            <HeaderStyle BackColor="#507CD1" Font-Bold="True" ForeColor="White" />
            <PagerStyle BackColor="#2461BF" ForeColor="White" HorizontalAlign="Center" />
            <RowStyle BackColor="#EFF3FB" />
            <SelectedRowStyle BackColor="#D1DDF1" Font-Bold="True" ForeColor="#333333" />
            <SortedAscendingCellStyle BackColor="#F5F7FB" />
            <SortedAscendingHeaderStyle BackColor="#6D95E1" />
            <SortedDescendingCellStyle BackColor="#E9EBEF" />
            <SortedDescendingHeaderStyle BackColor="#4870BE" />
        </asp:GridView>
    </p>
</asp:Content>