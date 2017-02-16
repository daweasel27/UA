using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml;

namespace TP3
{
    public partial class AdminPage : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (HttpContext.Current.User.Identity.Name.ToString() == "admin@admin.pt")
            {
                GridView1.Visible = true;
                XmlDocument reviews = Auxiliar.getReviewsAll();
                
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


            }
            else
            {
                GridView1.Visible = false;
                System.Diagnostics.Debug.WriteLine(HttpContext.Current.User.Identity.Name.ToString());



            }

          
        }

        protected void GridView2_RowDeleting(object sender, GridViewDeleteEventArgs e)
        {
          //  System.Diagnostics.Debug.WriteLine(GridView2.Rows[e.RowIndex].Cells[1].Text);
           // System.Diagnostics.Debug.WriteLine(GridView2.Rows[e.RowIndex].Cells[2].Text);

            Auxiliar.getOrDeleteReview(false, GridView2.Rows[e.RowIndex].Cells[3].Text, Int32.Parse(GridView2.Rows[e.RowIndex].Cells[2].Text));
            XmlDocument reviews = Auxiliar.getReviewsAll();

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
        }
    }
}