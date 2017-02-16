using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;
using System.Xml.Linq;
using System.Xml.Xsl;

namespace TP3
{
    public partial class feedAgre : System.Web.UI.Page
    {

        protected void Page_Load(object sender, EventArgs e)
        {
            XmlDocument xdoc = XmlDataSource2.GetXmlDocument();

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
            proc.Load(@"C:\Users\JotaP\Desktop\UA\EDC\TP3\TP3\TP3\XSLT\ItemSort.xslt", settings, new XmlUrlResolver());
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
                System.Diagnostics.Debug.WriteLine(nodeAuthor.InnerText);

                if (nodeCat == null)
                {
                    //nodeCat = nodeTitle.Clone();
                    nodeCat.InnerText = "";
                }
                String node_html = "<div class=\"col-xs-12 col-md-6 col-lg-4\"> <div class=\"well\" style=\"min-height: 300px\"> <div class=\"media\"> <div class=\"media-body\"> <h4 class=\"media-heading\">" + nodeAuthor.InnerText + "</h4> <h4 class=\"media-heading\"><a target=\"_blank\" href=\"" + nodeLink.InnerText + "\">" + nodeTitle.InnerText + "</a></h4> <div class=\"row\"><div class=\"col-md-6\"><small><i class=\"fa fa-tag\"></i> " + nodeCat.InnerText + "</small></div><div class=\"col-md-6\" style=\"text-align: right\"><small><i class=\"fa fa-calendar - check - o\"></i> " + nodeDate.InnerText + "</small></div></div><p>" + nodeDesc.InnerText + "</p></div></div></div></div>";
                innerHtml += node_html;
            }

            news.InnerHtml = innerHtml;
        }
        protected void CheckBoxList1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }
    }
}