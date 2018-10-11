/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.webappsintro.jdbc.example.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCExample {
    
    public static void main(String args[]){
        try {
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/bdprueba";
            String driver="com.mysql.jdbc.Driver";
            String user="bdprueba";
            String pwd="bdprueba";
            
            /*String url="jdbc:mysql://HOST:3306/BD";
            String driver="com.mysql.jdbc.Driver";
            String user="USER";
            String pwd="PWD";*/
                        
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,user,pwd);
            con.setAutoCommit(false);
                 
            
            System.out.println("Valor total pedido 1:"+valorTotalPedido(con, 1));
            
            List<String> prodsPedido=nombresProductosPedido(con, 1);
            
            
            System.out.println("Productos del pedido 1:");
            System.out.println("-----------------------");
            for (String nomprod:prodsPedido){
                System.out.println(nomprod);
            }
            System.out.println("-----------------------");
            
            
            int suCodigoECI=2137497;
            registrarNuevoProducto(con, suCodigoECI, "Luis Pizza", 99999999);            
            con.commit();
                        
            
            con.close();
                                   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Agregar un nuevo producto con los parámetros dados
     * @param con la conexión JDBC
     * @param codigo
     * @param nombre
     * @param precio
     * @throws SQLException 
     */
    public static void registrarNuevoProducto(Connection con, int codigo, String nombre,int precio) throws SQLException{
    	System.out.println("INSERT");
    	//Crear preparedStatement
    	String insertTable =
    		    "INSERT INTO  ORD_PRODUCTOS" +"(codigo,nombre,precio) VALUES"+"(?,?,?)";
        //Asignar parámetros

    	PreparedStatement e = con.prepareStatement(insertTable);
    	e.setInt(1, codigo);
    	e.setString(2, nombre);
    	e.setInt(3, precio);
    	
        //usar 'execute'
    	e.executeUpdate();
    	
        
        con.commit();
    	
    }
    
    /**
     * Consultar los nombres de los productos asociados a un pedido
     * @param con la conexión JDBC
     * @param codigoPedido el código del pedido
     * @return 
     */
    public static List<String> nombresProductosPedido(Connection con, int codigoPedido){
    	
    	System.out.println("CONSULTA");
        List<String> np=new LinkedList<>();
      //Crear prepared statement
        String consulta =
        		"SELECT nombre FROM ORD_PRODUCTOS, ORD_DETALLES_PEDIDO,ORD_PEDIDOS "+
        		"WHERE ORD_PEDIDOS.codigo=ORD_DETALLES_PEDIDO.pedido_fk AND "+
        		"ORD_DETALLES_PEDIDO.producto_fk=ORD_PRODUCTOS.codigo "+
        		"AND ORD_PEDIDOS.codigo=?";
        ResultSet rs;
       
        try {
        	//asignar parámetros
			PreparedStatement e= con.prepareStatement(consulta);
			e.setInt(1, codigoPedido);
			//usar executeQuery
			rs=e.executeQuery();
			//Llenar la lista y retornarla
			//Sacar resultados del ResultSet
			while(rs.next()) {
				np.add(rs.getString(1));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return np;
    }

    
    /**
     * Calcular el costo total de un pedido
     * @param con
     * @param codigoPedido código del pedido cuyo total se calculará
     * @return el costo total del pedido (suma de: cantidades*precios)
     */
    public static int valorTotalPedido(Connection con, int codigoPedido){
    	System.out.println("CONSULTA_COSTO");
        //Crear prepared statement
    	String consulta =
    			"SELECT SUM(precio*cantidad) FROM ORD_PRODUCTOS, ORD_DETALLES_PEDIDO,ORD_PEDIDOS "+
    			"WHERE ORD_PEDIDOS.codigo=ORD_DETALLES_PEDIDO.pedido_fk AND "+ 
    			"ORD_DETALLES_PEDIDO.producto_fk=ORD_PRODUCTOS.codigo "+
    			"AND ORD_PEDIDOS.codigo=?";
        ResultSet rs;
        int retorno=0;
        try {
        	//asignar parámetros
			PreparedStatement e= con.prepareStatement(consulta);
			e.setInt(1, codigoPedido);
			//usar executeQuery
			rs=e.executeQuery();
			//Llenar la lista y retornarla
			//Sacar resultados del ResultSet
			rs.next();
			retorno=rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return retorno ;
    }
}
