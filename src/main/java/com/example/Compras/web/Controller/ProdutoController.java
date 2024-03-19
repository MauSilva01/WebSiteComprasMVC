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

import com.example.Compras.web.Models.ListaProdutosModel;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {
	
	
	 private static final String DB_URL = "jdbc:sqlite:C:\\PROGRAMAS\\ComprasSpringMvc\\BancoDados\\COMPRAS.db";


	@RequestMapping("/listar")
    public ModelAndView redirecionarParaListaProdutos() {
        ModelAndView modelAndView = new ModelAndView("listaProdutos"); // Define o nome da view a ser exibida

        try {
            ArrayList<ListaProdutosModel> produtos = conexaoBancoDados.listarProdutos();
            modelAndView.addObject("produtos", produtos); // Define os atributos a serem passados para a view
        } catch (SQLException e) {
            e.printStackTrace();
            // Tratar o erro de forma adequada, talvez exibindo uma mensagem de erro na view ou redirecionando para uma página de erro
        }

        return modelAndView;
    }
	
    @GetMapping("/{id}")
    public ModelAndView getProdutoDetalhes(@PathVariable String id, Model model) throws ClassNotFoundException 
    {
    		 
	     try {
	    	 
	         Connection conn = DriverManager.getConnection(DB_URL);
	         String sql = "SELECT NOME, MARCA, DESCRICAO, VOLUME, UNIDADE FROM PRODUTOS WHERE id = ?";
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         stmt.setString(1,id);
	         ResultSet rs = stmt.executeQuery();

	         if (rs.next()) {
	             String nome = rs.getString("nome");
	             String marca = rs.getString("marca");
	             String descricao = rs.getString("descricao");
	             int volume = rs.getInt("volume");
	             String unidade = rs.getString("unidade");

	             model.addAttribute("nome", nome);
	             model.addAttribute("marca", marca);
	             model.addAttribute("descricao", descricao);
	             model.addAttribute("volume", volume);
	             model.addAttribute("unidade", unidade);

	             return new ModelAndView("detalhesProduto", "model", model);
	         } else {
	             return  new ModelAndView("ListaDeProduto");
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	         return new ModelAndView("error");
	     }

	 }
    
    
    @PostMapping("/Salvar")
    public RedirectView SalvarProduto(@RequestParam("id") int id,
						            @RequestParam("nome") String nome,
						            @RequestParam("marca") String marca,
						            @RequestParam("descricao") String descricao,
						            @RequestParam("volume") int volume,
						            @RequestParam("unidade") String unidade) {	
    Connection conn = null;
    PreparedStatement stmt = null;
    
    try {
    	
    	conn = DriverManager.getConnection(DB_URL);
    	String sql = "UPDATE PRODUTOS SET NOME = ?, MARCA = ?, DESCRICAO = ?, VOLUME = ?, UNIDADE = ? WHERE id = ?";
    	stmt = conn.prepareStatement(sql);
    	 stmt.setString(1, nome);
         stmt.setString(2, marca);
         stmt.setString(3, descricao);
         stmt.setInt(4, volume);
         stmt.setString(5, unidade);
         stmt.setInt(6, id);
         stmt.executeUpdate();
         
         return new RedirectView("/produtos/listar");
    	
    } catch (Exception e) {
    	e.printStackTrace();
    	 return new RedirectView("detalhesProdutos");
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
    public RedirectView ApagarProduto(@RequestParam("id") int id) throws ClassNotFoundException  {
    	
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	
    	try {	
    		
    		conn = DriverManager.getConnection(DB_URL);
    		String sql = "DELETE FROM PRODUTOS WHERE id = ?";
    		stmt = conn.prepareStatement(sql);       
    		stmt.setInt(1, id);         
    		stmt.executeUpdate();
    		
    		return new RedirectView("/produtos/listar");
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return new RedirectView("/produtos/listar");
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
       
    @PostMapping("/adicionar")
    public RedirectView AdicionarProduto( @RequestParam("nome") String nome,
						            @RequestParam("marca") String marca,
						            @RequestParam("descricao") String descricao,
						            @RequestParam("volume") int volume,
						            @RequestParam("unidade") String unidade) throws ClassNotFoundException {
    	Connection conn = null;
    	PreparedStatement stmt = null;
    	
    	try {
    		
    		conn = DriverManager.getConnection(DB_URL);
    		String sql = "INSERT INTO PRODUTOS (NOME,MARCA,DESCRICAO,VOLUME,UNIDADE) VALUES(?,?,?,?,?)";
    		stmt = conn.prepareStatement(sql);    
    		stmt.setString(1, nome);
    		stmt.setString(2, marca);
    		stmt.setString(3, descricao);
    		stmt.setInt(4, volume);
    		stmt.setString(5, unidade);
    		stmt.executeUpdate();
    		stmt.close();
            conn.close();
            
            return new RedirectView("/produtos/listar");
    	} catch(SQLException e) {
    		 e.printStackTrace();	
    		 return new RedirectView("/produtos/listar");
    	}
    	
    }
    
    @GetMapping("/Formulario")
    public String FormProduto() {
    	
    	return ("AdicionarProduto");
    	
    }
   }

