using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.IO;
using System.IO.Compression;
using System.Net;
using System.Text;
using System.Web;
using System.Xml;

namespace TP3
{
    public class Auxiliar
    {
        public static readonly string _CONN_STRING = System.Configuration.ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;

        public static XmlDocument Search(string name)
        {
            var doc = new XmlDocument();
            XmlReader reader = XmlReader.Create("http://thegamesdb.net/api/GetGamesList.php?name=" + name);
            doc.Load(reader);
            reader.Close();
            return doc;
        }

        public static XmlDocument ScoredGames(string name)
        {
            var doc = new XmlDocument();
            var conn = new SqlConnection(_CONN_STRING);
            var cmd = new SqlCommand($"select Info.query('//User/@id') as userid, InfoQuery('/User/Game/@idJogo') from Played')", conn);
            conn.Open();
            using (var reader = cmd.ExecuteXmlReader())
            {
                doc.Load(reader);
            }
            conn.Close();
            //System.Diagnostics.Debug.WriteLine(doc.ChildNodes.Count);

            return doc;
        }

        public static XmlDocument GamesInfo(int id)
        {
            var doc = new XmlDocument();
            var conn = new SqlConnection(_CONN_STRING);
            var cmd = new SqlCommand($"select Info.query('/Data/Game') from Games where Info.exist('/Data/Game[id={id}]') = 1", conn);
            conn.Open();
            using (var reader = cmd.ExecuteXmlReader())
            {
                doc.Load(reader);
            }
            conn.Close();
            //System.Diagnostics.Debug.WriteLine(doc.ChildNodes.Count);
            if (doc.ChildNodes.Count != 0) return doc;

            doc.Load("http://thegamesdb.net/api/GetGame.php?id=" + id);
            return doc;
        }

        public static XmlDocument GamesInfoPush(int id)
        {

            XmlDocument doc = new XmlDocument();
            //System.Diagnostics.Debug.WriteLine("                              ");

            var conn = new SqlConnection(_CONN_STRING);
            var cmd = new SqlCommand($"select * from GamesInfo({id})", conn);
            conn.Open();
            using (var reader = cmd.ExecuteXmlReader())
            {
                doc.Load(reader);
            }
            conn.Close();
            //System.Diagnostics.Debug.WriteLine("ola1 - " + doc.OuterXml);

            if (doc.OuterXml != "<Data />") return doc;

            var enDoc = new XmlDocument();
            enDoc.Load("http://thegamesdb.net/api/GetGame.php?id=" + id);

            cmd = new SqlCommand("insert into Games values (@Id, @Info)", conn);
            cmd.Parameters.Add(new SqlParameter("Id", id));
            cmd.Parameters.Add(new SqlParameter("Info", enDoc.OuterXml.Substring(38)));
            conn.Open();
            //System.Diagnostics.Debug.WriteLine("ola2 - ");

            // Push

            cmd.ExecuteNonQuery();

            // Get
            cmd = new SqlCommand($"select * from GamesInfo({id})", conn);
            using (var reader = cmd.ExecuteXmlReader())
            {
                doc.Load(reader);
            }
            conn.Close();
            
            
            return doc;
        }

        public static void Played(string userId, int idJogo, int score)
        {
            var conn = new SqlConnection(_CONN_STRING);
            string procName = "ScoreGame";
            var cmd = new SqlCommand(procName, conn) { CommandType = System.Data.CommandType.StoredProcedure };
            cmd.Parameters.Add(new SqlParameter("UserId", userId));
            cmd.Parameters.Add(new SqlParameter("GameId", idJogo));
            cmd.Parameters.Add(new SqlParameter("Score", score));
            System.Diagnostics.Debug.WriteLine(score);
            conn.Open();
            cmd.ExecuteNonQuery();
            conn.Close();
        }

        public static int GetScore(string userId, int idJogo)
        {
            var conn = new SqlConnection(_CONN_STRING);
            string procName = "getscoreProc";
            var cmd = new SqlCommand(procName, conn) { CommandType = System.Data.CommandType.StoredProcedure };
            cmd.Parameters.Add(new SqlParameter("UserId", userId));
            cmd.Parameters.Add(new SqlParameter("GameId", idJogo));

            SqlParameter returnParameter = cmd.Parameters.Add("total", SqlDbType.Int);
            returnParameter.Direction = ParameterDirection.ReturnValue;

            //cmd.ExecuteNonQuery();

            conn.Open();
            cmd.ExecuteNonQuery();

            int id = (int)returnParameter.Value;
            //System.Diagnostics.Debug.WriteLine(id);
            conn.Close();
            return id;
        }

        public static int Playing(string userId, int idJogo)
        {
            var conn = new SqlConnection(_CONN_STRING);
            string procName = "Playing";
            var cmd = new SqlCommand(procName, conn) { CommandType = System.Data.CommandType.StoredProcedure };
            cmd.Parameters.Add(new SqlParameter("UserId", userId));
            cmd.Parameters.Add(new SqlParameter("GameId", idJogo));

            SqlParameter returnParameter = cmd.Parameters.Add("total", SqlDbType.Int);
            returnParameter.Direction = ParameterDirection.ReturnValue;

            //cmd.ExecuteNonQuery();

            conn.Open();
            cmd.ExecuteNonQuery();

            int id = (int)returnParameter.Value;
            //System.Diagnostics.Debug.WriteLine(id);
            conn.Close();
            return id;
        }

        public static float GetAvgScore(int idJogo)
        {
            float total = 0;
            float count = 0;
            var OLA = "";
            using (SqlConnection conn = new SqlConnection(_CONN_STRING))
            using (SqlCommand cmd = new SqlCommand($"select dbo.returngamesscore3({idJogo})", conn))
            {
                conn.Open();
                OLA = (string)cmd.ExecuteScalar();
                conn.Close();
            }
            
            XmlDocument doc = new XmlDocument();
            doc.LoadXml("<root>"+OLA+"</root>");
            XmlNodeList x1 = doc.SelectNodes("//Game");
            foreach(XmlNode x2 in x1)
            {
                try
                {
                    total += float.Parse(x2.Attributes["score"].InnerText);
                    count++;
                }
               catch { }
              
            }
            if (count == 0)
                return 0;
            return total / count;
        }
        public static XmlDocument GetGamesUser(string id)
        {
            var OLA = "";
            var conn = new SqlConnection(_CONN_STRING);
            var cmd = new SqlCommand("select dbo.returngamesplayed5(@GameId)", conn);
            cmd.Parameters.Add(new SqlParameter("GameId", id));
            conn.Open();
            OLA = (string)cmd.ExecuteScalar();
            conn.Close();
            

            XmlDocument doc = new XmlDocument();
            doc.LoadXml("<root>" + OLA + "</root>");
            XmlNodeList x1 = doc.SelectNodes("//Game");

            return doc;
        }

        public static void PlayedOrNotPlayed(bool a, int idJogo)
        {
            if (a)
            {
                using (SqlConnection con = new SqlConnection(_CONN_STRING))
                {
                    using (SqlCommand cmd = new SqlCommand("InsertPlayed", con))
                    {
                        cmd.CommandType = CommandType.StoredProcedure;

                        cmd.Parameters.Add("@UserId", SqlDbType.VarChar).Value = HttpContext.Current.User.Identity.Name.ToString();
                        cmd.Parameters.Add("@GameId", SqlDbType.Int).Value = idJogo;

                        con.Open();
                        cmd.ExecuteNonQuery();
                    }
                }
            }
            else
            {
                using (SqlConnection con = new SqlConnection(_CONN_STRING))
                {
                    using (SqlCommand cmd = new SqlCommand("DeletePlayed", con))
                    {
                        cmd.CommandType = CommandType.StoredProcedure;

                        cmd.Parameters.Add("@UserId", SqlDbType.VarChar).Value = HttpContext.Current.User.Identity.Name.ToString();
                        cmd.Parameters.Add("@GameId", SqlDbType.Int).Value = idJogo;

                        con.Open();
                        cmd.ExecuteNonQuery();
                    }
                }
            }
        }

        public static XmlDocument getReviews(int idJogo)
        {
            var OLA = "";
            var conn = new SqlConnection(_CONN_STRING);
            var cmd = new SqlCommand("select dbo.returnReviewsGame(@GameId)", conn);
            cmd.Parameters.Add(new SqlParameter("GameId", idJogo));
            conn.Open();
            OLA = (string)cmd.ExecuteScalar();
            conn.Close();


            XmlDocument doc = new XmlDocument();
            doc.LoadXml("<Reviews>" + OLA + "</Reviews>");
            XmlNodeList x1 = doc.SelectNodes("//User");

            return doc;

        }
        
        public static XmlDocument getReviewsUser(string UserId)
        {

            var OLA = "";
            var conn = new SqlConnection(_CONN_STRING);
            var cmd = new SqlCommand("select dbo.returnReviewsUser(@GameId)", conn);
            cmd.Parameters.Add(new SqlParameter("GameId", UserId));
            conn.Open();
            OLA = (string)cmd.ExecuteScalar();
            conn.Close();


            XmlDocument doc = new XmlDocument();
            doc.LoadXml("<Reviews>" + OLA + "</Reviews>");
            XmlNodeList x1 = doc.SelectNodes("//User");

            return doc;

            

        }

        public static XmlDocument getReviewsAll()
        {
            var doc = new XmlDocument();
            var conn = new SqlConnection(_CONN_STRING);
            XmlElement ola = doc.CreateElement("Reviews");
            doc.AppendChild(ola);
            var cmd = new SqlCommand($"select Reviews.query('//User') from Reviews FOR XML AUTO", conn);
            conn.Open();
            using (var reader = cmd.ExecuteXmlReader())
            {
                doc.Load(reader);
            }
            conn.Close();
            return doc;

        }

        public static void getOrDeleteReview(bool a, string review, int gameId)
        {
            if (a)
            {
                using (SqlConnection con = new SqlConnection(_CONN_STRING))
                {
                    using (SqlCommand cmd = new SqlCommand("InsertReview", con))
                    {
                        cmd.CommandType = CommandType.StoredProcedure;

                        cmd.Parameters.Add("@UserId", SqlDbType.VarChar).Value = HttpContext.Current.User.Identity.Name.ToString();
                        cmd.Parameters.Add("@Review", SqlDbType.VarChar).Value = review;
                        cmd.Parameters.Add("@GameId", SqlDbType.Int).Value = gameId;

                        con.Open();
                        cmd.ExecuteNonQuery();
                    }
                }
            }
            else
            {
                using (SqlConnection con = new SqlConnection(_CONN_STRING))
                {
                    using (SqlCommand cmd = new SqlCommand("DeleteReview", con))
                    {
                        cmd.CommandType = CommandType.StoredProcedure;

                        cmd.Parameters.Add("@UserId", SqlDbType.VarChar).Value = HttpContext.Current.User.Identity.Name.ToString();
                        cmd.Parameters.Add("@Review", SqlDbType.VarChar).Value = review;
                        cmd.Parameters.Add("@GameId", SqlDbType.Int).Value = gameId;

                        con.Open();
                        cmd.ExecuteNonQuery();
                    }
                }
            }
        }

        
    }
}