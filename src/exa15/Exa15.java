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

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException, XMLStreamException {
        getConexion();
        File file = new File("/home/oracle/Desktop/ProbaExer4/platoss");
        FileInputStream leer2 = new FileInputStream(file);
        ObjectInputStream leerOIS = new ObjectInputStream(leer2);
        
        File fil= new File("/home/oracle/Desktop/ProbaExer4/totalgraxas.xml");
	FileWriter escribir = new FileWriter(fil);
        
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(escribir); 
        
        int totgrasas; 
        Platos objj = new Platos();
        //Necesario meter en el bucle para que lea todo
        writer.writeStartDocument("1.0");
        writer.writeStartElement("Platos");
        while ((objj = (Platos) leerOIS.readObject()) != null) {
            System.out.println(objj);
             totgrasas=0;//necesario porque si no en la siguiente se suma lagrasa del anterior

                Statement stm = conexion.createStatement();
                ResultSet rs = stm.executeQuery("select * from composicion where codp='"+objj.getCodigop()+"'");
                Statement stm1= conexion.createStatement();
                
            
                

                while (rs.next()) { //Va de fila en fila
                ResultSet rss = stm1.executeQuery("select graxa from componentes where CODC='"+rs.getString(2)+"'");   
                rss.next();
                
                    System.out.print("codigo do componente : " + rs.getString(2) + "-> graxa por cada 100 gr="+rss.getInt(1) + "\nPeso: " + rs.getInt(3) + "\n");
                    System.out.println("Total de graxa do componente= "+(rss.getInt(1)*rs.getInt(3)/100));
                    totgrasas+=(rss.getInt(1)*rs.getInt(3)/100); // =+ para que lo haga cada vez
                    
                    
           
                }
            System.out.println("TOTAL DE GRASAS "+totgrasas+"\n"+"\n");
            
            
           
            writer.writeStartElement("Plato");
            writer.writeAttribute("codigo", objj.getCodigop());
            writer.writeEndElement();
            writer.writeStartElement("nombreP");
            writer.writeCharacters(objj.getNomep());
            writer.writeEndElement(); 
            writer.writeStartElement("grasaTotal");
            writer.writeCharacters(Integer.toString(totgrasas));
            writer.writeEndElement();
           
            
            
        }
        writer.writeEndElement();      
        writer.writeEndDocument();
        writer.close();  
        leerOIS.close();
        leer2.close();
        closeConexion();
            
        
    }
}
