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

import com.example.Compras.web.Models.ListaMercadosModel;

@Controller
@RequestMapping("/mercados")
public class MercadoController {

	
	 private static final String DB_URL = "jdbc:sqlite:C:\\PROGRAMAS\\ComprasSpringMvc\\BancoDados\\COMPRAS.db";
	
	 
	@RequestMapping("/listar")
    public ModelAndView redirecionarListaMercados() {
        ModelAndView modelAndView = new ModelAndView("ListaMercados"); // Define o nome da view a ser exibida

        try {
            ArrayList<ListaMercadosModel> mercados = conexaoBancoDados.listaMercados();
            modelAndView.addObject("mercados", mercados); // Define os atributos a serem passados para a view
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
	         String sql = "SELECT MERCADOS FROM TABELA_MERCADOS WHERE id = ?";
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         stmt.setString(1,id);
	         ResultSet rs = stmt.executeQuery();

	         if (rs.next()) {
	             String mercados = rs.getString("MERCADOS");

	             model.addAttribute("mercados", mercados);
	     
	             return new ModelAndView("detalhesMercado", "model", model);
	         } else {
	             return  new ModelAndView("ListaMercados");
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	         return new ModelAndView("error");
	     }

	 }
	
	@PostMapping("/Salvar")
	public RedirectView SalvarMercado( @RequestParam("id") int id,
            							@RequestParam("mercados") String mercados) {
		
		Connection conn = null;
	    PreparedStatement stmt = null;
	    
	    try {
	   
	    	conn = DriverManager.getConnection(DB_URL);
	    	String sql = "UPDATE TABELA_MERCADOS SET MERCADOS = ? WHERE id = ?";
	    	stmt = conn.prepareStatement(sql);
	    	 stmt.setString(1, 	mercados);
	         stmt.setInt(2, id);
	         stmt.executeUpdate();
	         
	         return new RedirectView("/mercados/listar");
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	 return new RedirectView("detalhesMercado");
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
    		String sql = "DELETE FROM TABELA_MERCADOS WHERE id = ?";
    		stmt = conn.prepareStatement(sql);       
    		stmt.setInt(1, id);         
    		stmt.executeUpdate();
    		
    		return new RedirectView("/mercados/listar");
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return new RedirectView("/mercados/listar");
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
		
		return ("AdicionarMercado");
	}
	
	@PostMapping("/adicionar")
    public RedirectView AdicionarMercados( @RequestParam("mercados") String	mercados) throws ClassNotFoundException {
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	
    	try {
    		
    		conn = DriverManager.getConnection(DB_URL);
    		String sql = "INSERT INTO TABELA_MERCADOS (MERCADOS) VALUES(?)";
    		stmt = conn.prepareStatement(sql);    
    		stmt.setString(1, mercados);
    		stmt.executeUpdate();
    		stmt.close();
            conn.close();
            
            return new RedirectView("/mercados/listar");
    	} catch(SQLException e) {
    		 e.printStackTrace();	
    		 return new RedirectView("/mercados/Formulario");
    	}
    	
    }
}
