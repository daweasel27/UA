using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;

namespace TP3
{
    public partial class feedManager : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void FormView1_ItemInserting(object sender, FormViewInsertEventArgs e)
        {
            XmlDocument xdoc = XmlDataSource1.GetXmlDocument();
            XmlElement feed = xdoc.CreateElement("feed");
            XmlAttribute name = xdoc.CreateAttribute("nome");
            XmlAttribute url = xdoc.CreateAttribute("url");

            
            name.Value = ((TextBox)FormView1.Row.Cells[0].FindControl("nameInsert")).Text;

            url.Value = ((TextBox)FormView1.Row.Cells[0].FindControl("urlInsert")).Text; ;

            feed.Attributes.Append(name);
            feed.Attributes.Append(url);

            xdoc.DocumentElement.AppendChild(feed);
            XmlDataSource1.Save();
            e.Cancel = true;
            FormView1.ChangeMode(FormViewMode.ReadOnly);
        }

        protected void FormView1_ItemUpdating(object sender, FormViewUpdateEventArgs e)
        {
            XmlDocument xdoc = XmlDataSource1.GetXmlDocument();
            XmlElement feed = xdoc.SelectSingleNode("feeds/feed[@nome='" + e.OldValues["nome"] + "']") as XmlElement;

            feed.Attributes["nome"].Value = e.NewValues["nome"].ToString();
            feed.Attributes["url"].Value = e.NewValues["url"].ToString();

            XmlDataSource1.Save();
            e.Cancel = true;
            FormView1.ChangeMode(FormViewMode.ReadOnly);
        }
        protected void FormView1_ItemDeleting(object sender, FormViewDeleteEventArgs e)
        {
            XmlDocument xdoc = XmlDataSource1.GetXmlDocument();
            System.Diagnostics.Debug.WriteLine(e.Values["nome"]);
            XmlElement feed = xdoc.SelectSingleNode("feeds/feed[@nome='" + e.Values["nome"] + "']") as XmlElement;
            xdoc.DocumentElement.RemoveChild(feed);
            XmlDataSource1.Save();
            e.Cancel = true;
            FormView1.DataBind();
        }

    }
}