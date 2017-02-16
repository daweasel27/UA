using AjaxControlToolkit;
using Microsoft.AspNet.Identity;
using System;
using System.Data;
using System.Data.SqlClient;
using System.Web;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Xml;
using System.Xml.Xsl;

namespace TP3
{
    public partial class videoGameInfo : System.Web.UI.Page
    {
        bool gameReviewed = false;
        static int lastVal = -1;
        string Idplayer = "";
        protected void Page_Load(object sender, EventArgs e)
        {
            GridView1.DataSource = null;
            GridView1.DataBind();
            if (!Page.IsPostBack)
            {
                doLoad();
            }
            if (RadioButton1.Checked == true)
            {
                Rating1.Visible = true;
                TextBox1.Visible = true;
                Button1.Visible = true;

            }
            else
            {
                Rating1.Visible = false;
                TextBox1.Visible = false;
                Button1.Visible = false;
            }
            int score = Auxiliar.GetScore(HttpContext.Current.User.Identity.Name.ToString(), Int32.Parse(Request.QueryString["id"]));
            Label2.Text = Auxiliar.GetAvgScore(Int32.Parse(Request.QueryString["id"])).ToString();

            if (score != 0)
            {
                Rating1.CurrentRating = score;
            }



            try
            {
                XmlDocument reviews = Auxiliar.getReviews(Int32.Parse(Request.QueryString["id"]));

                GridView1.DataSource = null;
                GridView1.DataBind();

                DataSet xmlData = new DataSet();
                var xmlReader1 = new XmlNodeReader(reviews);
                xmlData.ReadXml(xmlReader1);

                GridView1.DataSource = xmlData;
                GridView1.DataBind();


            }
            catch
            {
                XmlDocument asfvafaw = new XmlDocument();
                String xsltFileName1 = Server.MapPath("~/XML/Reviews.xml");

                asfvafaw.Load(xsltFileName1);

                XmlDataSource3.Data = asfvafaw.OuterXml;
                GridView1.DataSource = XmlDataSource3;
                GridView1.DataBind();
            }

        }
        protected void doLoad()
        {
            System.Diagnostics.Debug.WriteLine("USER NOT AUTHENTICATED!!!");

            if (!HttpContext.Current.User.Identity.IsAuthenticated)
            {
                System.Diagnostics.Debug.WriteLine("USER NOT AUTHENTICATED!!!");
                Rating1.Visible = false;
                RadioButton1.Visible = false;
                Label1.Visible = false;
                Label3.Visible = false;
                Label4.Visible = true;
                Label2.Visible = false;
                ola.Visible = false;
                TextBox1.Visible = false;
                Button1.Visible = false;
            }
            else
            {
                Rating1.Visible = true;
                RadioButton1.Visible = true;
                Label1.Visible = true;
                Label3.Visible = true;
                Label4.Visible = false;
                Label2.Visible = true;
                TextBox1.Visible = true;
                Button1.Visible = true;
                ola.Visible = true;

            }

            Label2.Text = Auxiliar.GetAvgScore(Int32.Parse(Request.QueryString["id"])).ToString();

            int score = Auxiliar.GetScore(HttpContext.Current.User.Identity.Name.ToString(), Int32.Parse(Request.QueryString["id"]));
            System.Diagnostics.Debug.WriteLine(score);
            int playedTrue = Auxiliar.Playing(HttpContext.Current.User.Identity.Name.ToString(), Int32.Parse(Request.QueryString["id"]));
            RadioButton1.Checked = false;
            if (playedTrue == 1)
                RadioButton1.Checked = true;
            

            if (score != 0)
            {
                Rating1.CurrentRating = score;
            }

            if (RadioButton1.Checked == true)
            {
                Rating1.Visible = true;
                TextBox1.Visible = true;
                Button1.Visible = true;

            }
            else
            {
                Rating1.Visible = false;
                TextBox1.Visible = false;
                Button1.Visible = false;
            }

            Button1.Visible = true;

            XmlDocument asfvafaw = new XmlDocument();
            String xsltFileName1 = Server.MapPath("~/XML/Reviews.xml");

            asfvafaw.Load(xsltFileName1);

            XmlDataSource3.Data = asfvafaw.OuterXml;
            GridView1.DataSource = XmlDataSource3;
            GridView1.DataBind();


            try
            {
                XmlDocument reviews = Auxiliar.getReviews(Int32.Parse(Request.QueryString["id"]));

                GridView1.DataSource = null;
                GridView1.DataBind();

                DataSet xmlData = new DataSet();
                var xmlReader1 = new XmlNodeReader(reviews);
                xmlData.ReadXml(xmlReader1);

                GridView1.DataSource = xmlData;
                GridView1.DataBind();


            }
            catch
            {

            }

            XmlDocument Games = Auxiliar.GamesInfoPush(Int32.Parse(Request.QueryString["id"]));
            //XmlDocument Games = Auxiliar.GamesInfo(Int32.Parse(Request.QueryString["id"]));
            var trans = new XslTransform();

            String xsltFileName = Server.MapPath("~/XSLT/infoGame.xslt");

            trans.Load(xsltFileName);

            var reader = trans.Transform(Games, null, (XmlResolver)null);
            var transformedDoc = new XmlDocument();
            transformedDoc.Load(reader);
            System.Diagnostics.Debug.WriteLine("olaaaaaaaaaaaaaaaaaaaa");

            var ds = new DataSet();
            var xmlReader = new XmlNodeReader(transformedDoc);

            ds.ReadXml(xmlReader);
            SeriesDetailsView.DataSource = ds;
            SeriesDetailsView.FieldHeaderStyle.Font.Bold = true;
            SeriesDetailsView.RowStyle.BackColor = System.Drawing.Color.White;
            SeriesDetailsView.FieldHeaderStyle.BackColor = System.Drawing.Color.LightGray;
            SeriesDetailsView.FieldHeaderStyle.BorderColor = System.Drawing.Color.Black;
            SeriesDetailsView.FieldHeaderStyle.Width = 90;
            SeriesDetailsView.FieldHeaderStyle.VerticalAlign = VerticalAlign.Middle;
            System.Diagnostics.Debug.WriteLine("olaaaaaaaaaaaaaaaaaaaa");

            SeriesDetailsView.DataBind();

            System.Diagnostics.Debug.WriteLine("olaaaaaaaaaaaaaaaaaaaa");

            for (int i = 0; i < 4; i++)
            {

                SeriesDetailsView.Rows[i].Visible = false;
                System.Diagnostics.Debug.WriteLine(SeriesDetailsView.Rows[i].Visible);


            }

            SeriesDetailsView.Rows[0].Enabled = false;


            Image[] image = new Image[transformedDoc.SelectNodes("//Similar").Count];
            Label[] label = new Label[transformedDoc.SelectNodes("//Similar").Count];
            HyperLink[] hyperlink = new HyperLink[transformedDoc.SelectNodes("//Similar").Count];

            for (int i = 0; i < transformedDoc.SelectNodes("//Similar").Count; i++)
            {
                label[i] = new Label();

                image[i] = new Image();
                image[i].Attributes.Add("class", "img-responsive center-block");
                image[i].Attributes.Add("style", "height:200px");

                XmlDocument x = new XmlDocument();
                x.Load("http://thegamesdb.net/api/GetArt.php?id=" + transformedDoc.SelectNodes("//Similar/@idJogo").Item(i).InnerText);
                image[i].ImageUrl = "http://thegamesdb.net/banners/" + x.SelectNodes("//Images/boxart[@side='front']").Item(0).InnerText;

                HtmlGenericControl createDiv = new HtmlGenericControl("DIV");
                createDiv.Attributes.Add("class", "grid-item col-md-3 col-sm-8 sortable");
                createDiv.Attributes.Add("style", "margin-top: 20px;");

                this.SimilarGames.Controls.Add(createDiv);
                createDiv.Controls.Add(image[i]);
                label[i] = new Label();
                HtmlGenericControl createDivText = new HtmlGenericControl("DIV");
                createDivText.Attributes.Add("class", "titles");
                createDivText.Attributes.Add("style", "text-align: center; margin-top: 5px;");
                createDiv.Controls.Add(createDivText);
                hyperlink[i] = new HyperLink();
                hyperlink[i].NavigateUrl = String.Format("videoGameInfo.aspx?id={0}", transformedDoc.SelectNodes("//Similar/@idJogo").Item(i).InnerText);

                x.Load("http://thegamesdb.net/api/GetGame.php?id=" + transformedDoc.SelectNodes("//Similar/@idJogo").Item(i).InnerText);
                hyperlink[i].Text = x.SelectNodes("//Game/GameTitle").Item(0).InnerText;

                label[i].Controls.Add(hyperlink[i]);
                createDivText.Controls.Add(label[i]);


            }


            Label1.Text = SeriesDetailsView.Rows[2].Cells[1].Text;
            //System.Diagnostics.Debug.WriteLine(SeriesDetailsView.Rows[9].Cells[0].Text);
            channelImage.Src = "http://thegamesdb.net/banners/" + SeriesDetailsView.Rows[7].Cells[1].Text;
            channelImage.DataBind();
            XmlDocument xdoc = XmlDataSource2.GetXmlDocument();
            XmlNodeList list4 = xdoc.GetElementsByTagName("User");
            foreach (XmlNode x in list4)
            {
                if (x.Attributes["id"].Value == HttpContext.Current.User.Identity.Name.ToString())
                {
                    Idplayer = x.Attributes["id"].Value;
                    XmlNodeList list3 = x.ChildNodes;
                    foreach (XmlNode c in list3)
                    {
                        if (c.Attributes["idJogo"].Value == Request.QueryString["id"])
                        {
                            gameReviewed = true;
                        }
                    }
                }
            }
            lastVal = -1;
        }

        protected void Checked(object sender, EventArgs e)
        {
            //System.Diagnostics.Debug.WriteLine(((CheckBox)sender).Checked);
            if (((CheckBox)sender).Checked)
            {
                Auxiliar.PlayedOrNotPlayed(true, Int32.Parse(Request.QueryString["id"]));
            }
            else
            {
                Auxiliar.PlayedOrNotPlayed(false, Int32.Parse(Request.QueryString["id"]));
            }

        }

        protected void TextBox1_TextChanged(object sender, EventArgs e)
        {
            XmlDocument xdoc = XmlDataSource2.GetXmlDocument();
            XmlNodeList list = xdoc.GetElementsByTagName("User");
            XmlElement feed;
            XmlElement game = null;
            XmlAttribute name = null;
            XmlAttribute score = null;
            XmlElement game1;
            if (Idplayer == "")
            {
                feed = xdoc.CreateElement("User");
                name = xdoc.CreateAttribute("id");
                name.Value = HttpContext.Current.User.Identity.Name.ToString();
                feed.Attributes.Append(name);

                game = xdoc.CreateElement("Rating");
                name = xdoc.CreateAttribute("idJogo");
                score = xdoc.CreateAttribute("Rating");

                name.Value = Request.QueryString["id"];
                TextBox textBox = sender as TextBox;
                score.Value = textBox.Text;
                game.Attributes.Append(name);
                game.Attributes.Append(score);
                feed.AppendChild(game);
            }
            else
            {
                feed = xdoc.SelectSingleNode("Ratings/User[@id='" + Idplayer + "']") as XmlElement;
                if (gameReviewed == false)
                {
                    game = xdoc.CreateElement("Rating");
                    name = xdoc.CreateAttribute("idJogo");
                    score = xdoc.CreateAttribute("Rating");

                    name.Value = Request.QueryString["id"];
                    TextBox textBox = sender as TextBox;
                    score.Value = textBox.Text;
                    game.Attributes.Append(name);
                    game.Attributes.Append(score);
                    feed.AppendChild(game);

                }
                else
                {
                    game1 = xdoc.SelectSingleNode("Ratings/User[@id='" + Idplayer + "']/Rating[@idJogo='" + Request.QueryString["id"] + "']") as XmlElement;
                    TextBox textBox = sender as TextBox;
                    game1.Attributes[1].Value = textBox.Text;
                    game1.Attributes[0].Value = Request.QueryString["id"];
                    feed.AppendChild(game1);
                }

            }
           


            xdoc.DocumentElement.AppendChild(feed);

            XmlDataSource2.Save();
        }


        protected void FormView1_ItemUpdating(object sender, FormViewUpdateEventArgs e)
        {
            XmlDocument xdoc = XmlDataSource1.GetXmlDocument();
            XmlElement feed = xdoc.SelectSingleNode("feeds/feed[@nome='" + e.OldValues["nome"] + "']") as XmlElement;

            feed.Attributes["nome"].Value = e.NewValues["nome"].ToString();
            feed.Attributes["url"].Value = e.NewValues["url"].ToString();

            XmlDataSource1.Save();
        }

        protected void changePage(object sender, EventArgs e)
        {
            string n = String.Format("{0}", Request.Form["q"]);

            Response.Redirect(String.Format("videoGamesSearch.aspx?name={0}", n));
        }

        protected void score(object sender, EventArgs e)
        {
           

        }

        protected void Played(object sender, EventArgs e)
        {
            System.Diagnostics.Debug.WriteLine(RadioButton1.Checked);
            if (RadioButton1.Checked == true)
            {
                Auxiliar.PlayedOrNotPlayed(true, Int32.Parse(Request.QueryString["id"]));
            }
            else
            {
                
                Auxiliar.PlayedOrNotPlayed(false, Int32.Parse(Request.QueryString["id"]));
                Rating1.CurrentRating = 0;
            }
        }

        protected void Rating1_Click1(object sender, RatingEventArgs e)
        {
            if(lastVal != Int32.Parse(e.Value))
            {
                lastVal = Int32.Parse(e.Value);
                System.Diagnostics.Debug.WriteLine("entrei na merda do botao" + e.Value);
                Auxiliar.Played(HttpContext.Current.User.Identity.Name.ToString(), Int32.Parse(Request.QueryString["id"]),Int32.Parse(e.Value));
                Label2.Text = Auxiliar.GetAvgScore(Int32.Parse(Request.QueryString["id"])).ToString();
            }
        }

        protected void enterReview(object sender, EventArgs e)
        {
            Auxiliar.getOrDeleteReview(true, TextBox1.Text, Int32.Parse(Request.QueryString["id"]));
            XmlDocument reviews = Auxiliar.getReviews(Int32.Parse(Request.QueryString["id"]));

            XmlDocument asfvafaw = new XmlDocument();
            String xsltFileName1 = Server.MapPath("~/XML/Reviews.xml");

            asfvafaw.Load(xsltFileName1);

            XmlDataSource3.Data = asfvafaw.OuterXml;
            GridView1.DataSource = XmlDataSource3;
            GridView1.DataBind();

            DataSet xmlData = new DataSet();
            var xmlReader1 = new XmlNodeReader(reviews);
            xmlData.ReadXml(xmlReader1);

            GridView1.DataSource = xmlData;
            GridView1.DataBind();


        }
    }

}