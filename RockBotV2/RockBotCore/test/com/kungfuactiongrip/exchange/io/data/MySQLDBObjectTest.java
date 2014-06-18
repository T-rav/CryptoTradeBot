/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kungfuactiongrip.exchange.io.data;

import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class MySQLDBObjectTest {
    
    public MySQLDBObjectTest() {
    }

    @Test
    public void CanCreateObjectWithoutPropertyFile_ExpectValidObject(){
        IDbDAO obj = DBProviderFactory.GenerateMySQLDBObject();
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CanCreateObjectWithPropertyFile_ExpectValidObject(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        assertEquals("tradebotTest", obj.FetchUser());
    }

    @Test
    public void CanCreateObjectWithNullPropertyFile_ExpectDefaultObject(){
        IDbDAO obj = DBProviderFactory.GenerateMySQLDBObject(null);
        
        assertNotNull(obj);
        assertEquals("username", obj.FetchUser());
    }
    
    @Test
    public void CanCreateConnection_ExpectValidConnectionObject(){
        IDbDAO obj = GenerateTestDB();
        
        assertNotNull(obj);
        Connection con = null;
        try{
            con = obj.CreateConnection();
            assertNotNull(con);
        }finally{
            if(con != null){
                try{
                    con.close();
                }catch(SQLException e){}
            }
        }
    }
   
    private IDbDAO GenerateTestDB() {
        IDbDAO obj = DBProviderFactory.GenerateMySQLDBObject("Test_DB");
        return obj;
    }
    
    
    
}
