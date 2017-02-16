<%@ Page Title="" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="feedAgre.aspx.cs" Inherits="TP3.feedAgre" %>
<asp:Content ID="Content1" ContentPlaceHolderID="MainContent" runat="server">
    <br />
        <br />
     <h2><i class="fa fa-rss fa-4"></i> My Feed Aggregator</h2>
    <hr />
        <asp:XmlDataSource ID="XmlDataSource2" runat="server" DataFile="~/XML/Feeds.xml"></asp:XmlDataSource>
        <asp:XmlDataSource ID="XmlDataSource1" runat="server" DataFile="~/XML/Publico_feeds.xml" ></asp:XmlDataSource>
        <div runat="server" ID="news" class="row" />
    </div>
</asp:Content>
