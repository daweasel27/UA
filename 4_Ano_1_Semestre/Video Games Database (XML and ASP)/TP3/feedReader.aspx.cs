using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;

namespace TP3
{
    public partial class feedReader : System.Web.UI.Page
    {
        static XmlDataSource a;
        protected void Page_Load(object sender, EventArgs e)
        {
            XmlDocument xdoc = XmlDataSource2.GetXmlDocument();

            XmlNodeList elemList = xdoc.GetElementsByTagName("feed");


            for (int i = 0; i < elemList.Count; i++)
            {
                if(elemList[i].Attributes["nome"].Value == DropDownList1.SelectedValue)
                {
                    string attrVal = elemList[i].Attributes["url"].Value;
                    XmlReader reader = XmlReader.Create(attrVal);
                    XmlDocument doc = new XmlDocument();
                    doc.Load(reader);
                    reader.Close();

                    doc.Save("C:/Users/JotaP/Desktop/UA/EDC/TP3/TP3/TP3/XML/" + elemList[i].Attributes["nome"].Value + "_feed.xml");
                    XmlDataSource6.DataFile = "~/XML/" + elemList[i].Attributes["nome"].Value + "_feed.xml";

                }
            }
            
            XmlDocument xdoc1 = XmlDataSource6.GetXmlDocument();
            elemList = xdoc1.GetElementsByTagName("url");
            if (elemList.Count > 0)
            {
                XmlNode x1 = elemList[0].ChildNodes.Item(0);
                Image2.Src = x1.InnerText;
            }
            else
            {
                Image2.Src = "default-placeholder.png";
            }


            XmlNodeList nodes_items = xdoc1.SelectNodes("rss/channel/item");
            XmlNode nodeTitle = xdoc1.SelectSingleNode("rss/channel/item/title");
            XmlNode nodeCat = xdoc1.SelectSingleNode("rss/channel/item/category");
            XmlNode nodeDate = xdoc1.SelectSingleNode("rss/channel/item/date");
            XmlNode nodeDesc = xdoc1.SelectSingleNode("rss/channel/item/description");
            XmlNode nodeLink = xdoc1.SelectSingleNode("rss/channel/item/link");


            String innerHtml = "";

            foreach (XmlNode node in nodes_items)
            {
                nodeTitle = node.SelectSingleNode("title");
                nodeCat = node.SelectSingleNode("category");
                nodeDate = node.SelectSingleNode("pubDate");
                nodeDesc = node.SelectSingleNode("description");
                nodeLink = node.SelectSingleNode("link");
                if(nodeCat == null)
                {
                    nodeCat = nodeTitle.Clone();
                    nodeCat.InnerText = "";
                }
                //System.Diagnostics.Debug.WriteLine(elemList[0].InnerText);
                String node_html = "<div class=\"col-xs-12 col-md-6 col-lg-4\"> <div class=\"well\" style=\"min-height: 300px\"> <div class=\"media\"> <div class=\"media-body\"> <h4 class=\"media-heading\"><a target=\"_blank\" href=\"" + nodeLink.InnerText + "\">" + nodeTitle.InnerText + "</a></h4> <div class=\"row\"><div class=\"col-md-6\"><small><i class=\"fa fa-tag\"></i> " + nodeCat.InnerText + "</small></div><div class=\"col-md-6\" style=\"text-align: right\"><small><i class=\"fa fa-calendar - check - o\"></i> " + nodeDate.InnerText + "</small></div></div><p>" + nodeDesc.InnerText + "</p></div></div></div></div>";
                innerHtml += node_html;
            }

            news.InnerHtml = innerHtml;

        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            Response.Redirect("feedmanager.aspx");
        }

        protected void DropDownList1_SelectedIndexChanged(object sender, EventArgs e)
        {
            XmlDataSource3.DataFile = "~/XML/" + DropDownList1.SelectedValue + "_feed.xml";
            XmlDataSource5.DataFile = "~/XML/" + DropDownList1.SelectedValue + "_feed.xml";
            XmlDataSource6.DataFile = "~/XML/" + DropDownList1.SelectedValue + "_feed.xml";
            a = XmlDataSource6;
            XmlDocument xdoc = XmlDataSource6.GetXmlDocument();
            XmlNodeList elemList = xdoc.GetElementsByTagName("channel");
            //System.Diagnostics.Debug.WriteLine(elemList[0].Attributes["category"].Value);

        }

        protected void ListView1_SelectedIndexChanged(object sender, EventArgs e)
        {

        }
    }
}