using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Xml;
using System.Xml.Xsl;

namespace TP3
{
    public partial class _Default : Page
    {
        private int o = 0;
        protected void Page_Load(object sender, EventArgs e)
        {
            XmlDocument xdoc = XmlDataSource2.GetXmlDocument();
            Image[] image = new Image[10];
            Label[] label = new Label[10];
            HyperLink[] hyperlink = new HyperLink[10];
            GridView1.Visible = false;
            int x4;
            if(GridView1.Rows.Count > 0) { 
                if(GridView1.Rows.Count <= 9)
                {
                    x4 = GridView1.Rows.Count;
                }
                else
                {
                   x4 = 9;
                }
                for (int i = 1; i <x4; i++)
                {
                    label[i] = new Label();

                    image[i] = new Image();
                    image[i].Attributes.Add("height", "150px");

                    XmlDocument x = new XmlDocument();
                    x.Load("http://thegamesdb.net/api/GetArt.php?id=" + GridView1.Rows[i].Cells[0].Text);

                    image[i].ImageUrl = "http://thegamesdb.net/banners/" + x.SelectNodes("//Images/boxart[@side='front']").Item(0).InnerText;
                    HtmlGenericControl createDiv = new HtmlGenericControl("DIV");
                    createDiv.Attributes.Add("class", "grid-item col-md-5 col-sm-12 sortable");
                    createDiv.Attributes.Add("style", "margin-top: 30px;margin-left:30px;");

                    //Centrar image na div
                    image[i].Attributes.Add("class", "img-responsive center-block");
                    image[i].Attributes.Add("style", "height:250px");

                    this.SimilarGames.Controls.Add(createDiv);
                    createDiv.Controls.Add(image[i]);
                    label[i] = new Label();
                    HtmlGenericControl createDivText = new HtmlGenericControl("DIV");
                    createDivText.Attributes.Add("class", "titles");
                    createDivText.Attributes.Add("style", "text-align: center;");
                    createDiv.Controls.Add(createDivText);

                    hyperlink[i] = new HyperLink();
                    XmlDocument xml = new XmlDocument();
                    xml.Load("http://thegamesdb.net/api/GetGame.php?id=" + GridView1.Rows[i].Cells[0].Text);

                    hyperlink[i].Text = xml.SelectNodes("//Game/GameTitle").Item(0).InnerText;
                    hyperlink[i].NavigateUrl = String.Format("videoGameInfo.aspx?id={0}", xml.SelectNodes("//Game/id").Item(0).InnerText);

                    label[i].Controls.Add(hyperlink[i]);
                    createDivText.Controls.Add(label[i]);
                }
            }

            var feedList = xdoc.DocumentElement.SelectNodes("feed");
            XmlDocument all = new XmlDocument();
            XmlElement parent = all.CreateElement("all");
            all.AppendChild(parent);
            foreach (XmlNode node in feedList)
            {
                XmlDocument feed = new XmlDocument();
                feed.Load(node.Attributes["url"].Value);
                var nodes = feed.SelectNodes("rss/channel/item");
      
                foreach (XmlNode innerNode in nodes)
                {

                    var author = all.CreateElement("author");
                    author.InnerText = feed.SelectSingleNode("rss/channel/title").InnerText;
                    var importNode = all.ImportNode(innerNode, true);
                    importNode.AppendChild(author);
                    all.DocumentElement.AppendChild(importNode);
                }
            }

            list(order(all));
        }

        protected XmlDocument order(XmlDocument all)
        {
            XmlDocument sortedXml = new XmlDocument();
            XsltSettings settings = new XsltSettings(true, false);
            XslCompiledTransform proc = new XslCompiledTransform();
            String xsltFileName = Server.MapPath("~/XSLT/ItemSort.xslt");

            proc.Load(xsltFileName, settings, new XmlUrlResolver());
            using (XmlWriter writer = sortedXml.CreateNavigator().AppendChild())
            {
                proc.Transform(all, writer);


            }
            return sortedXml;
        }

        protected void list(XmlDocument all)
        {
            XmlNodeList nodes_items = all.SelectNodes("all/item");

            XmlAttribute nodeTitle;
            XmlAttribute nodeCat;
            XmlAttribute nodeDate;
            XmlAttribute nodeDesc;
            XmlAttribute nodeLink;
            XmlAttribute nodeAuthor;
            String innerHtml = "";

            foreach (XmlNode node in nodes_items)
            {
                nodeTitle = node.Attributes["title"];

                nodeCat = node.Attributes["category"];
                nodeDate = node.Attributes["pubDate"];
                nodeDesc = node.Attributes["description"];
                nodeLink = node.Attributes["link"];
                nodeAuthor = node.Attributes["author"];
                o++;
                if (o == 10) { news.InnerHtml = innerHtml; return; } 
                if (nodeCat == null)
                {
                    //nodeCat = nodeTitle.Clone();
                    nodeCat.InnerText = "";
                }
                String node_html = "<div class=\"col-xs-12 col-md-1 col-lg-6\"> <div class=\"well\" style=\"min-height: 300px\"> <div class=\"media\"> <div class=\"media-body\"> <h6 class=\"media-heading\" style=\"font-weight:bold;\">" + nodeAuthor.InnerText + "</h6> <h6 class=\"media-heading\"><a target=\"_blank\" href=\"" + nodeLink.InnerText + "\">" + nodeTitle.InnerText + "</a></h6> <div class=\"row\"><div class=\"col-md-6\"><small style=\"font-size:10px;\"><i class=\"fa fa-tag\"></i> " + nodeCat.InnerText + "</small></div><div class=\"col-md-6\" style=\"text-align: right\"><small><i class=\"fa fa-calendar - check - o\"></i> " + nodeDate.InnerText + "</small></div></div><p>" + nodeDesc.InnerText + "</p></div></div></div></div>";
                innerHtml += node_html;
            }

            news.InnerHtml = innerHtml;
        }

        protected void changePage(object sender, EventArgs e)
        {
            string n = String.Format("{0}", Request.Form["q"]);

            Response.Redirect(String.Format("videoGamesSearch.aspx?name={0}", n));
        }
    }
}