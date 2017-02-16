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

namespace TP3.Account
{
    public partial class Manage : System.Web.UI.Page
    {

        protected void Page_Load()
        {

          


            XmlDocument reviews = Auxiliar.getReviewsUser(HttpContext.Current.User.Identity.Name.ToString());
            System.Diagnostics.Debug.WriteLine(reviews.OuterXml);

            GridView2.DataSource = null;
            GridView2.DataBind();
            XmlDataSource3.Data = "";
            XmlDataSource3.DataBind();

            DataSet xmlData = new DataSet();
            var xmlReader1 = new XmlNodeReader(reviews);
            xmlData.ReadXml(xmlReader1);

            XmlDataSource3.Data = reviews.OuterXml;
            GridView2.DataSource = xmlData;
            GridView2.DataBind();



            XmlDocument Games = Auxiliar.GetGamesUser(HttpContext.Current.User.Identity.Name.ToString());
            //XmlDocument Games = Auxiliar.GamesInfo(Int32.Parse(Request.QueryString["id"]));
            var trans = new XslTransform();
            String xsltFileName = Server.MapPath("~/XSLT/Game.xslt");

            trans.Load(xsltFileName);

            var reader = trans.Transform(Games, null, (XmlResolver)null);
            var transformedDoc = new XmlDocument();
            transformedDoc.Load(reader);

            var ds = new DataSet();
            var xmlReader = new XmlNodeReader(transformedDoc);

            ds.ReadXml(xmlReader);
            GridView1.DataSource = ds;
            GridView1.DataBind();

            Image[] image = new Image[GridView1.Rows.Count];
            Label[] label = new Label[GridView1.Rows.Count];
            HyperLink[] hyperlink = new HyperLink[GridView1.Rows.Count];
            GridView1.Visible = false;
            for (int i = 1; i < GridView1.Rows.Count; i++)
            {
                label[i] = new Label();

                image[i] = new Image();
                image[i].Attributes.Add("height", "150px");

                XmlDocument x = new XmlDocument();
                x.Load("http://thegamesdb.net/api/GetArt.php?id=" + GridView1.Rows[i].Cells[0].Text);

                image[i].ImageUrl = "http://thegamesdb.net/banners/" + x.SelectNodes("//Images/boxart[@side='front']").Item(0).InnerText;
                HtmlGenericControl createDiv = new HtmlGenericControl("DIV");
                createDiv.Attributes.Add("class", "grid-item col-md-2 col-sm-12 sortable");
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
                hyperlink[i].NavigateUrl = "~/" + String.Format("videoGameInfo.aspx?id={0}", xml.SelectNodes("//Game/id").Item(0).InnerText);

                label[i].Controls.Add(hyperlink[i]);
                createDivText.Controls.Add(label[i]);
            }
        }
        protected void GridView2_RowDeleting(object sender, GridViewDeleteEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine(GridView2.Rows[e.RowIndex].Cells[3].Text);
            System.Diagnostics.Debug.WriteLine(GridView2.Rows[e.RowIndex].Cells[2].Text);

            Auxiliar.getOrDeleteReview(false,  GridView2.Rows[e.RowIndex].Cells[3].Text, Int32.Parse(GridView2.Rows[e.RowIndex].Cells[2].Text));

        }


    }
}