package com.example.Compras.web.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.Compras.web.Models.ListaComprasModel;

@Controller
@RequestMapping("/lista")
public class ListasController {
	
        
	 private static final String DB_URL = "jdbc:sqlite:C:\\PROGRAMAS\\ComprasSpringMvc\\BancoDados\\COMPRAS.db";
//	 private static final String Usuario = "MauricioAlves";  
//	 private static final String Senha = "Banana10";
	 

	@RequestMapping("/listar")
    public ModelAndView redirecionarListaCompras() {
        ModelAndView modelAndView = new ModelAndView("ListaCompras"); // Define o nome da view a ser exibida

        try {
            ArrayList<ListaComprasModel> comprasList = conexaoBancoDados.listaCompras();
            modelAndView.addObject("comprasList", comprasList); // Define os atributos a serem passados para a view
        } catch (SQLException e) {
            e.printStackTrace();
            // Tratar o erro de forma adequada, talvez exibindo uma mensagem de erro na view ou redirecionando para uma página de erro
        }

        return modelAndView;
    }
	
	@GetMapping("/{id}")
    public ModelAndView MercadosDetalhes(@PathVariable String id, Model model) throws ClassNotFoundException 
    {
    		 
	     try {
	    	 
	         Connection conn = DriverManager.getConnection(DB_URL);
	         String sql = "SELECT LISTA_DE_COMPRAS FROM LISTAS_COMPRAS WHERE id = ?";
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         stmt.setString(1,id);
	         ResultSet rs = stmt.executeQuery();

	         if (rs.next()) {
	             String comprasList = rs.getString("LISTA_DE_COMPRAS");

	             model.addAttribute("comprasList", comprasList);
	     
	             return new ModelAndView("detalhesLista", "model", model);
	         } else {
	             return  new ModelAndView("ListaCompras");
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	         return new ModelAndView("error");
	     }

	 }
	@PostMapping("/Salvar")
	public RedirectView SalvarMercado( @RequestParam("id") int id,
            							@RequestParam("comprasList") String comprasList) {
		
		Connection conn = null;
	    PreparedStatement stmt = null;
	    
	    try {
	    	
	    	conn = DriverManager.getConnection(DB_URL);
	    	String sql = "UPDATE LISTAS_COMPRAS SET LISTA_DE_COMPRAS = ? WHERE id = ?";
	    	stmt = conn.prepareStatement(sql);
	    	 stmt.setString(1, 	comprasList);
	         stmt.setInt(2, id);
	         stmt.executeUpdate();
	         
	         return new RedirectView("/lista/listar");
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	 return new RedirectView("detalhesLista");
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	    }
	}
	
	@PostMapping("/Apagar")
	public RedirectView ApagarPoduto(@RequestParam("id") int id) throws ClassNotFoundException {
		
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	

    	try {	
    		
    		conn = DriverManager.getConnection(DB_URL);
    		String sql = "DELETE FROM LISTAS_COMPRAS WHERE id = ?";
    		stmt = conn.prepareStatement(sql);       
    		stmt.setInt(1, id);         
    		stmt.executeUpdate();
    		
    		return new RedirectView("/lista/listar");
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return new RedirectView("/lista/listar");
    	} finally {
    		try {
              if (stmt != null) {
            	  stmt.close();
              }
              if (conn != null) {
                  conn.close();
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
	}
	
	@GetMapping("/Formulario")
	public String FormMercaados() {
		
		return ("AdicionarLista");
	}
	
	@PostMapping("/adicionar")
    public RedirectView AdicionarMercados( @RequestParam("comprasList") String	comprasList) throws ClassNotFoundException {
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	
    	try {
    		
    		conn = DriverManager.getConnection(DB_URL);
    		String sql = "INSERT INTO LISTAS_COMPRAS (LISTA_DE_COMPRAS) VALUES(?)";
    		stmt = conn.prepareStatement(sql);    
    		stmt.setString(1, comprasList);
    		stmt.executeUpdate();
    		stmt.close();
            conn.close();
            
            return new RedirectView("/lista/listar");
    	} catch(SQLException e) {
    		 e.printStackTrace();	
    		 return new RedirectView("/lista/Formulario");
    	}
    	
    }
}
