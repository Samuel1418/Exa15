/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exa15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author oracle
 */
public class Exa15 {

    public static Connection conexion = null;
    Connection conn;

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost";
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }

    public static void closeConexion() throws SQLException {
        conexion.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
        getConexion();
        File file = new File("/home/oracle/Desktop/ProbaExer4/platoss");
        FileInputStream leer2 = new FileInputStream(file);
        ObjectInputStream leerOIS = new ObjectInputStream(leer2);

        Platos objj = new Platos();
        //Necesario meter en el bucle para que lea todo
        while ((objj = (Platos) leerOIS.readObject()) != null) {
            System.out.println(objj);
             

                Statement stm = conexion.createStatement();
                ResultSet rs = stm.executeQuery("select * from composicion where codp='"+objj.getCodigop()+"'");
                Statement stm1= conexion.createStatement();
                
                

                while (rs.next()) {
                ResultSet rss = stm1.executeQuery("select graxa from componentes where CODC='"+rs.getString(2)+"'");   
                rss.next();
                
                    System.out.print("codigo do componente : " + rs.getString(2) + "-> graxa por cada 100 gr="+rss.getInt(1) + "\nPeso: " + rs.getInt(3) + "\n");

                }


        }
        leerOIS.close();
        leer2.close();
        closeConexion();

    }
}
