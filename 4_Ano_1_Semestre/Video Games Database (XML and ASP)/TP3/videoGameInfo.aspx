<%@ Page Title="" Language="C#"  EnableEventValidation="false" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="videoGameInfo.aspx.cs" Inherits="TP3.videoGameInfo" %>
<asp:Content ID="Content1" ContentPlaceHolderID="MainContent" runat="server" style="overflow-x: hidden">
    <div style="overflow-x: hidden">
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
    <div style="text-align:center;">
        <asp:Label ID="Label1"  runat="server" Text="Label" Font-Bold="True" Font-Overline="False" Font-Size="50pt"></asp:Label>
    </div>
    <hr />

           <div class="row">

            <div class="col-md-3 col-md-offset-4" style="margin-left:450px;" >

                <style>
                .starRating{
                    width:50px;
                    height:50px;
                    cursor:pointer;
                    background-repeat: no-repeat;
            
                    display: block;
                }
                .FilledStar{
                    background-image:url("Images/full.png");
                }
                .EmptyStar{
                    background-image:url('Images/empty.png');
                }
                .WaitingStar{
                    background-image:url("Images/pressed.png");
                }

            </style>

                <asp:UpdatePanel ID="UpdatePanel1" runat="server">
                    <ContentTemplate>
                        <ajaxToolkit:Rating ID="Rating1" runat="server" 
                            StarCssClass="starRating" 
                            FilledStarCssClass="FilledStar" 
                            EmptyStarCssClass="EmptyStar" 
                            WaitingStarCssClass="WaitingStar" OnChanged="Rating1_Click1" 
                            >
                        </ajaxToolkit:Rating>
                    </ContentTemplate>
                 </asp:UpdatePanel>
                <br />
                <br />
                <asp:Label ID="Label4" runat="server" style="font-size:x-large;margin-top:10px;" Text="Por favor faça Log in"></asp:Label>

                <asp:Label ID="Label3" runat="server" style="font-size:x-large;margin-top:10px;" Text="Score médio"></asp:Label>
                <asp:Label ID="Label2" runat="server" style="font-size:x-large" Text="Label"></asp:Label>
                <br />
                <section runat="server" id="ola">
                        <style>
                            .mydatagrid
{
	width: 80%;
	border: solid 2px black;
	min-width: 80%;
}
.header
{
	background-color: #000;
	font-family: Arial;
	color: White;
	height: 25px;
	text-align: center;
	font-size: 16px;
}

.rows
{
	background-color: #fff;
	font-family: Arial;
	font-size: 14px;
	color: #000;
	min-height: 25px;
	text-align: left;
}
.rows:hover
{
	background-color: #5badff;
	color: #fff;
}

.mydatagrid a /** FOR THE PAGING ICONS  **/
{
	background-color: Transparent;
	padding: 5px 5px 5px 5px;
	color: #fff;
	text-decoration: none;
	font-weight: bold;
}

.mydatagrid a:hover /** FOR THE PAGING ICONS  HOVER STYLES**/
{
	background-color: #000;
	color: #fff;
}
.mydatagrid span /** FOR THE PAGING ICONS CURRENT PAGE INDICATOR **/
{
	background-color: #fff;
	color: #000;
	padding: 5px 5px 5px 5px;
}
.pager
{
	background-color: #5badff;
	font-family: Arial;
	color: White;
	height: 30px;
	text-align: left;
}

.mydatagrid td
{
	padding: 5px;
}
.mydatagrid th
{
	padding: 5px;
}
                            /**
                             * Checkbox Two
                             */
                            .checkboxTwo {
                                width: 120px;
                                height: 40px;
                                background: #333;
                                margin: 20px 60px;
                                border-radius: 50px;
                                position: relative;
                            }
                            .checkboxTwo:before {
                                content: '';
                                position: absolute;
                                top: 19px;
                                left: 14px;
                                height: 2px;
                                width: 90px;
                                background: #111;
                            }
                            .checkboxTwo label {
                                display: block;
                                width: 22px;
                                height: 22px;
                                border-radius: 50%;
                                -webkit-transition: all .5s ease;
                                -moz-transition: all .5s ease;
                                -o-transition: all .5s ease;
                                -ms-transition: all .5s ease;
                                transition: all .5s ease;
                                cursor: pointer;
                                position: absolute;
                                top: 9px;
                                z-index: 1;
                                left: 12px;
                                background: #fff;
                            }
                            .checkboxTwo input[type=checkbox]:checked + label {
                                left: 84px;
                                background: #2600ea;
                            }
                            input[type=checkbox] {
                                visibility: hidden;
                            }
                        </style>
                      <h3 id="jogado" >Marcar como jogado</h3>
                      <div  id="jogado2" class="checkboxTwo">
                        <asp:CheckBox ID="RadioButton1" runat="server"  Text=" " OnCheckedChanged="Played" AutoPostBack="true" />
                      </div>
                    </section>

             </div>
           </div>

   <div class="row">
        <div class="col-md-6">
            <div style="text-align:center"><h3>Info</h3></div>
                 <asp:DetailsView ID="SeriesDetailsView" runat="server" CssClass="table table-bordered table-hover">
            </asp:DetailsView>
         </div>
       <div class="col-md-6 text-center">
            <div class="row" style="text-align:center;">
                <img runat="server" ID="channelImage" style="height:500px"  class="img-responsive center-block">
        	</div>
        </div>
    </div>

    <br />

    <br />
        <h5> Criar Review </h5>
        <asp:TextBox Visible = "true" style="width:100%" TextMode="MultiLine" ID="TextBox1" runat="server"></asp:TextBox>
        <asp:Button Visible = "true" ID="Button1" runat="server" Text="Submeter Review" class="btn btn-primary" OnClick="enterReview" />

        <h3> Ver Reviews </h3>

        <asp:GridView ID="GridView1"  CssClass="mydatagrid" PagerStyle-CssClass="pager"
 HeaderStyle-CssClass="header" RowStyle-CssClass="rows" AllowPaging="True" style="width:100%" runat="server"></asp:GridView>

        <h3>Jogos Similares</h3>
               <div class ="row span4 offset4" runat="server" id="SimilarGames" />
              <asp:XmlDataSource ID="XmlDataSource3" runat="server" DataFile="~/XML/Reviews.xml" TransformFile="~/XSLT/Reviews.xslt"></asp:XmlDataSource>

      <asp:XmlDataSource ID="XmlDataSource1" runat="server"></asp:XmlDataSource>
    <asp:XmlDataSource ID="XmlDataSource2" runat="server" DataFile="~/XML/Ratings.xml" ></asp:XmlDataSource>
          <style>
                          /* .slideTwo */
            .slideTwo {
              width: 80px;
              height: 30px;
              background: #333;
              margin: 20px auto;
              position: relative;
              border-radius: 50px;
              box-shadow: inset 0px 1px 1px rgba(0, 0, 0, 0.5), 0px 1px 0px rgba(255, 255, 255, 0.2);
              &:after {
                content: '';
                position: absolute;
                top: 14px;
                left: 14px;
                height: 2px;
                width: 52px;
                background: #111;
                border-radius: 50px;
                box-shadow: inset 0px 1px 1px rgba(0, 0, 0, 0.5), 0px 1px 0px rgba(255, 255, 255, 0.2);
              }
              label {
                display: block;
                width: 22px;
                height: 22px;
                cursor: pointer;
                position: absolute;
                top: 4px;
                z-index: 1;
                left: 4px;
                background: #fcfff4;
                border-radius: 50px;
                transition: all 0.4s ease;
                box-shadow: 0px 2px 5px 0px rgba(0,0,0,0.3);
                background: linear-gradient(top, #fcfff4 0%, #dfe5d7 40%, #b3bead 100%);
                &:after {
                  content: '';
                  width: 10px;
                  height: 10px;
                  position: absolute;
                  top: 6px;
                  left: 6px;
                  background: #333;
                  border-radius: 50px;
                  box-shadow: inset 0px 1px 1px rgba(0,0,0,1), 0px 1px 0px rgba(255,255,255,0.9);
                }
              }
              input[type=checkbox] {
                visibility: hidden;
                &:checked + label {
                  left: 54px;
                  &:after {
                    background: $activeColor; /*activeColor*/
                  }
                }
              }    
            }
          </style>
</div> 

</asp:Content>
