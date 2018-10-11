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
package edu.eci.pdsw.samples.services.client;



import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.lang.Object;
import java.util.Date;
import edu.eci.pdsw.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.pdsw.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.TipoItem;

/**
 *
 * @author hcadavid
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
	public static void main(String args[]) throws SQLException, ParseException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();

        SqlSession sqlss = sessionfact.openSession();

        
        ClienteMapper cm=sqlss.getMapper(ClienteMapper.class);
        System.out.println(cm.consultarClientes());
        
        
        //3.
        //System.out.println(cm.consultarCliente(100));
        
        SimpleDateFormat fecha1= new SimpleDateFormat("dd-M-yyyy");
        String fechaInicio="09-01-2018";
        Date inicio = fecha1.parse(fechaInicio);
        String fechaFin = "09-02-2018";
        Date fin = fecha1.parse(fechaFin);
        
        //cm.agregarItemRentadoACliente(100, 32, inicio, fin);
        //System.out.println(cm.consultarCliente(100));
        
        //4.
        String fechaLan = "09-02-1997";
        Date lanzamiento = fecha1.parse(fechaFin);
        ItemMapper im=sqlss.getMapper(ItemMapper.class);
        Item it = new Item(new TipoItem(2, "juego"), 1501, "pac-man", "nada", lanzamiento, 1000, "ninguno", "arcade");
        //im.insertarItem(it);
        
        //5.
        
        //System.out.println(im.consultarItems());
        //System.out.println(im.consultarItem(1));
        
        
        sqlss.commit();
        
        
        sqlss.close();

        
        
    }


}
