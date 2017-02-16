using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Xml;
using System.Xml.Xsl;

namespace TP3
{
    public partial class videoGamesSearch : System.Web.UI.Page
    {

        protected void Page_Load(object sender, EventArgs e)
        {
            var xml = Auxiliar.Search(Request.QueryString["name"]);

            Image[] image = new Image[xml.SelectNodes("//Game").Count];
            Label[] label = new Label[xml.SelectNodes("//Game").Count];
            HyperLink[] hyperlink = new HyperLink[xml.SelectNodes("//Game").Count];

            for (int i = 0; i < xml.SelectNodes("//Game").Count; i++)
            {
                label[i] = new Label();

                image[i] = new Image();
                image[i].Attributes.Add("height", "150px");

                XmlDocument x = new XmlDocument();
                x.Load("http://thegamesdb.net/api/GetArt.php?id=" + xml.SelectNodes("//Game/id").Item(i).InnerText);

                System.Diagnostics.Debug.WriteLine(xml.SelectNodes("//Game/id").Item(i).InnerText);
                try
                {
                    image[i].ImageUrl = "http://thegamesdb.net/banners/" + x.SelectNodes("//Images/boxart[@side='front']").Item(0).InnerText;

                }
                catch { }



                

                HtmlGenericControl createDiv = new HtmlGenericControl("DIV");
                createDiv.Attributes.Add("class", "grid-item col-md-3 col-sm-8 sortable");
                createDiv.Attributes.Add("style", "margin-top: 20px;");

                //Centrar image na div
                image[i].Attributes.Add("class", "img-responsive center-block");
                image[i].Attributes.Add("style", "height:200px");

                this.SimilarGames.Controls.Add(createDiv);
                createDiv.Controls.Add(image[i]);
                label[i] = new Label();
                HtmlGenericControl createDivText = new HtmlGenericControl("DIV");
                createDivText.Attributes.Add("class", "titles");
                createDivText.Attributes.Add("style", "text-align: center;");
                createDiv.Controls.Add(createDivText);
 
                hyperlink[i] = new HyperLink();
                hyperlink[i].Text = xml.SelectNodes("//Game/GameTitle").Item(i).InnerText;
                hyperlink[i].NavigateUrl = String.Format("videoGameInfo.aspx?id={0}", xml.SelectNodes("//Game/id").Item(i).InnerText);

                label[i].Controls.Add(hyperlink[i]);
                createDivText.Controls.Add(label[i]);

            }

        }

        protected void GamesButton_Click(object sender, CommandEventArgs e)
        {
              Response.Redirect(String.Format("videoGameInfo.aspx?id={0}", e.CommandArgument.ToString()));

        }

        protected void changePage(object sender, EventArgs e)
        {
            string n = String.Format("{0}", Request.Form["q"]);

            Response.Redirect(String.Format("videoGamesSearch.aspx?name={0}", n));
        }
    }
}