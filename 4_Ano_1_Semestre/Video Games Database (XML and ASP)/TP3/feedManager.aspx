<%@ Page Title="" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="feedManager.aspx.cs" Inherits="TP3.feedManager" %>
<asp:Content ID="Content1" ContentPlaceHolderID="MainContent" runat="server">
    <br />
    <asp:Label ID="Label1" runat="server" Font-Size="XX-Large" Text="Gestão de feeds"></asp:Label>
        <hr />

    <asp:FormView ID="FormView1" runat="server" DataSourceID="XmlDataSource1" AllowPaging="True" Width="100%"
            OnItemDeleting="FormView1_ItemDeleting" OnItemInserting="FormView1_ItemInserting" OnItemUpdating="FormView1_ItemUpdating" >
        <EditItemTemplate>
            <table class="table table-bordered" border="0">
                <tr>
                    <td>Name:</td>
                    <td>
                        <asp:TextBox runat="server" ID="nameFeed"  CssClass="form-control" Text='<%# Bind("nome") %>'></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>URL:</td>
                    <td>
                        <asp:TextBox runat="server" ID="url" CssClass="form-control"  Text='<%# Bind("url") %>'></asp:TextBox>
                    </td>
                </tr>
            </table>
            <asp:LinkButton ID="UpdateButton" runat="server" CausesValidation="True" CommandName="Update" CssClass="btn btn-default glyphicon glyphicon-floppy-disk" />
            &nbsp;<asp:LinkButton ID="UpdateCancelButton" runat="server" CausesValidation="False" CommandName="Cancel" CssClass="btn btn-default glyphicon glyphicon-remove" />

        </EditItemTemplate>
        <InsertItemTemplate>
          <table class="table table-bordered" border="0">
                <tr>
                    <td>Name:</td>
                    <td>
                        <asp:TextBox runat="server" ID="nameInsert" Text='<%# Bind("nome") %>'></asp:TextBox>
                    </td>
                </tr>
                <tr>
                    <td>URL:</td>
                    <td>
                        <asp:TextBox runat="server" ID="urlInsert" CssClass="form-control"  Text='<%# Bind("url") %>'></asp:TextBox>
                    </td>
                </tr>
            </table>
            <asp:LinkButton ID="InsertButton" runat="server" CausesValidation="True" CommandName="Insert" CssClass="btn btn-default glyphicon glyphicon-ok" />
            &nbsp;<asp:LinkButton ID="InsertCancelButton" runat="server" CausesValidation="False" CommandName="Cancel" CssClass="btn btn-default glyphicon glyphicon-remove" />

        </InsertItemTemplate>
        <ItemTemplate>
        <table class="table table-bordered">
                <tr>
                    <td class="col-xs-2">Name:</td>
                    <td class="col-xs-10">
                        <asp:Label runat="server" ID="nameFeed" Text='<%# Bind("nome") %>'></asp:Label>
                    </td>
                </tr>
                <tr>
                    <td>URL:</td>
                    <td>
                        <asp:Label runat="server" ID="urlFeed" Text='<%# Bind("url") %>'></asp:Label>
                    </td>
                </tr>
            </table>
            
            <asp:LinkButton ID="newButton" runat="server" CssClass="btn btn-default glyphicon glyphicon-plus" CommandName="New"  />
            <asp:LinkButton ID="editButton" runat="server" CssClass="btn btn-default glyphicon glyphicon-pencil" CommandName="Edit"  />
            <asp:LinkButton ID="deleteButton" runat="server" CssClass="btn btn-default glyphicon glyphicon-trash"  CommandName="Delete"  />

        </ItemTemplate>
         <FooterTemplate>
        </FooterTemplate>

        <PagerStyle CssClass="pagination-ys" />
    </asp:FormView>
    <asp:XmlDataSource ID="XmlDataSource1" runat="server" DataFile="~/XML/Feeds.xml" ></asp:XmlDataSource>

</asp:Content>
