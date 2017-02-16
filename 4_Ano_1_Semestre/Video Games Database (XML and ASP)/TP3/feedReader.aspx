<%@ Page Title="" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="feedReader.aspx.cs" Inherits="TP3.feedReader" %>
<asp:Content ID="Content1" ContentPlaceHolderID="MainContent" runat="server">
    <asp:XmlDataSource ID="XmlDataSource1" runat="server" DataFile="~/XML/Feeds.xml"></asp:XmlDataSource>
    <asp:XmlDataSource ID="XmlDataSource2" runat="server" DataFile="~/XML/Feeds.xml"></asp:XmlDataSource>
    <asp:XmlDataSource ID="XmlDataSource6" runat="server" DataFile="~/XML/Publico_feeds.xml"></asp:XmlDataSource>
    <asp:XmlDataSource ID="XmlDataSource3" runat="server" DataFile="~/XML/Publico_feeds.xml" TransformFile="~/XSLT/Publico_feed.xslt"></asp:XmlDataSource>
    <asp:XmlDataSource ID="XmlDataSource5" runat="server" DataFile="~/XML/Publico_feeds.xml" TransformFile="~/XSLT/Publico_feed.xslt" XPath="rss/channel/listaDeItems/item"></asp:XmlDataSource>
    <br />
     <h2><i class="fa fa-rss fa-4"></i> My Feed Reader</h2>
    <hr />

    <div class="row">
        <div class="col-md-6" style="text-align: center">
            <asp:DropDownList ID="DropDownList1" AutoPostBack="true" runat="server" DataSourceID="XmlDataSource1" DataTextField="nome" DataValueField="nome" OnSelectedIndexChanged="DropDownList1_SelectedIndexChanged" CssClass="form-control">
    </asp:DropDownList>

        </div>
        <div class="col-md-6" style="text-align: right; margin-top: 0px; top: 0px; left: 0px;">
            <asp:LinkButton ID="ManageFeeds" PostBackUrl="~/feedManager.aspx" runat="server" CssClass="btn btn-primary"><i class="fa fa-rss"></i>&nbsp;Manage feeds</asp:LinkButton>
        </div>
    </div>
   <div class="row">
        <div class="col-md-6">
            <h3>Feed Info</h3>
            <asp:DetailsView HeaderStyle-Font-Bold="true" ID="DetailsView1" runat="server" class="table table-striped" AutoGenerateRows="False" DataSourceID="XmlDataSource3" GridLines="None">
                <Fields>
                    <asp:BoundField HeaderStyle-Font-Bold="true" DataField="title" HeaderText="title" SortExpression="title" />
                    <asp:BoundField HeaderStyle-Font-Bold="true" DataField="link" HeaderText="link" SortExpression="link" />
                    <asp:BoundField HeaderStyle-Font-Bold="true" DataField="description" HeaderText="description" SortExpression="description" />
                    <asp:BoundField HeaderStyle-Font-Bold="true" DataField="copyright" HeaderText="copyright" SortExpression="copyright" />
                    <asp:BoundField HeaderStyle-Font-Bold="true" DataField="language" HeaderText="language" SortExpression="language" />
                    <asp:BoundField HeaderStyle-Font-Bold="true" DataField="lastBuildDate" HeaderText="lastBuildDate" SortExpression="lastBuildDate" />
                    <asp:BoundField HeaderStyle-Font-Bold="true" DataField="category" HeaderText="category" SortExpression="category" />
                </Fields>
                <HeaderStyle Font-Bold="True" />
            </asp:DetailsView>
         </div>
       <div class="col-md-6 text-center">
            <h3>Feed Image</h3>
            <div class="row">
                <div class="col-xs-4"></div>
        		<div class="col-xs-4">
        			<img runat="server" ID="Image2" src="http://placehold.it/160x160" style="width:160px" class="img-responsive img-radio">
        		</div>
                <div class="col-xs-4"></div>
        	</div>
        </div>
    </div>
        <h3>Feed News <small></small></h3>

    <div runat="server" ID="news" class="row">

        </div>

</asp:Content>
