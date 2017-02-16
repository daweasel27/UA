
<%@ Page Title="Home Page"  EnableEventValidation="false" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="TP3._Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">
    <br />
    <div > 
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
 
      <div style="text-align:center;">
          <div style="width:49%;float:left">
              <h3>Noticias</h3>
                
                <hr />
                    <asp:XmlDataSource ID="XmlDataSource2" runat="server" DataFile="~/XML/Feeds.xml"></asp:XmlDataSource>
                    <asp:XmlDataSource ID="XmlDataSource1" runat="server" DataFile="~/XML/Publico_feeds.xml" ></asp:XmlDataSource>
                    <div runat="server" ID="news" class="row" />
                </div>
          </div>

          <div style="width:49%;float:right;text-align:center;">
              <h3>Jogos</h3>
                              <hr />

                  <asp:GridView ID="GridView1" runat="server" AutoGenerateColumns="False" DataSourceID="SqlDataSource1" Width="87px">

                  <Columns>
                      <asp:BoundField DataField="id" HeaderText="id" ReadOnly="True" SortExpression="id" />
                  </Columns>
                  </asp:GridView>
                                 <div class ="row" runat="server" id="SimilarGames" />

                  <asp:SqlDataSource ID="SqlDataSource1" runat="server" ConnectionString="<%$ ConnectionStrings:DefaultConnection %>" SelectCommand="select * from ReturnGames()"></asp:SqlDataSource>

          </div>
      </div>
    </div>
</asp:Content>
