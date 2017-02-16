<%@ Page Title="" Language="C#"  EnableEventValidation="false" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="videoGamesSearch.aspx.cs" Inherits="TP3.videoGamesSearch" %>
<asp:Content ID="Content1" ContentPlaceHolderID="MainContent" runat="server">
             <br />
     <div id="searchbar">
        <div class="container col-lg-3 col-lg-offset-3" style="display:table; width:100%;" >
            <form id="game-search" style="display:table-cell; text-align:center; vertical-align:middle" class="row stroke header-searchbox" role="search" action="www.google.pt" accept-charset="UTF-8" method="get">
                <input name="utf8" type="hidden" value="&#x2713;" />
                <div class="col-sm-9">
                    <div class="form-group">
                        <div class="input-group">
                            <input type="hidden" name="type" id="search_type" value="1" />
                            <input type="search" style="width:500px;" name="q" id="search" class="form-control header-search" placeholder="Search for Video Games, Companies, Platforms" data-autocomplete="/search_autocomplete_all" autocomplete="off"   style="width:100%"/>
                                    <asp:LinkButton class="btn btn-default" runat="server"  OnClick="changePage">
                                    <i  class="fa fa-search"></i>
                                </asp:LinkButton>
                            </div>
                        <input id="search-type-selected" type="hidden" />
                    </div></div></form></div></div>
     <hr />
     <br />
         <div style ="float:left" class="col-xs-8 col-md-7">
            <asp:DataList ID="SearchResultsList" runat="server" RepeatDirection="Vertical" RepeatLayout="Table">
                <ItemTemplate>
                    <asp:LinkButton ID="Games" runat="server"
                        OnCommand="GamesButton_Click"
                        CommandArgument='<%# XPath("id") %>'>
                        <%# XPath("GameTitle") %>
                    </asp:LinkButton>
                </ItemTemplate>
            </asp:DataList>
        </div>
   <div class="row" >
       <div class="col-xs-8 col-md-6" >
            <img runat="server" ID="channelImage"  class="img-responsive">
            <asp:DetailsView ID="SeriesDetailsView" runat="server" CssClass="table table-bordered table-hover">
                <Fields>
                    <asp:HyperLinkField
                        DataNavigateUrlFields="id"
                        DataNavigateUrlFormatString="videoGameInfo.aspx?id={0}"
                        Text="Full Detail" />
                </Fields>
            </asp:DetailsView>
        </div>
    </div>
               <div class ="row" runat="server" id="SimilarGames" />

</asp:Content>
