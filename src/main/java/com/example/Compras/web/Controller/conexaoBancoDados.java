package com.example.Compras.web.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;

import com.example.Compras.web.Models.ListaComprasModel;
import com.example.Compras.web.Models.ListaMercadosModel;
import com.example.Compras.web.Models.ListaProdutosModel;



@Controller
public class conexaoBancoDados {
	public static Connection obterConexao()  {

		String url = "jdbc:sqlite:C:\\PROGRAMAS\\ComprasSpringMvc\\BancoDados\\COMPRAS.db";
		
		Connection conn = null;
		try {
			
			conn = DriverManager.getConnection(url);
		        System.out.println("Conexão com o banco de dados Access estabelecida com sucesso!");
		} 
		catch (Exception e) {
		        System.out.println("Erro ao estabelecer conexão com o banco de dados Access: " + e.toString());
		} 
		    
		return conn;
	}
	
public static ArrayList<ListaProdutosModel> listarProdutos() throws SQLException {
		
		ArrayList<ListaProdutosModel> produtos = new ArrayList<>();
		
		try {
			Connection conn = obterConexao();
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUTOS"); {
				while (resultSet.next()) {
					int id = resultSet.getInt("ID");
					String nome = resultSet.getString("NOME");
					String marca = resultSet.getString("MARCA");
					String descricao = resultSet.getString("DESCRICAO");
					int volume = resultSet.getInt("VOLUME");
					String unidade = resultSet.getString("UNIDADE");
					produtos.add(new ListaProdutosModel(id, nome, marca, descricao, volume, unidade));
				}        
			}
		}	 
		catch (SQLException e) {
			throw new SQLException("Erro ao listar dados da tabela: " + e.getMessage());
		}
		return produtos;
	}	

	public static ArrayList<ListaComprasModel> listaCompras() throws SQLException{
		
		ArrayList<ListaComprasModel> listaCompras = new ArrayList<>();
	    
		try {
			Connection conn = obterConexao();
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM LISTAS_COMPRAS");
			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				String nomelist = resultSet.getString("LISTA_DE_COMPRAS");
				listaCompras.add(new ListaComprasModel(id, nomelist));
			        
			}	
		} 
		catch (SQLException e) {
			throw new SQLException("Erro ao listar dados da tabela: " + e.getMessage());
		}		
		return listaCompras;
	}

	public static ArrayList<ListaMercadosModel> listaMercados() throws SQLException{
			
			ArrayList<ListaMercadosModel> listaMercados = new ArrayList<>();
		    
			try {
				Connection conn = obterConexao();
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM TABELA_MERCADOS");
				while (resultSet.next()) {
					int id = resultSet.getInt("ID");
					String nomeMercado = resultSet.getString("MERCADOS");
					listaMercados.add(new ListaMercadosModel(id, nomeMercado));
				        
				}	
			} 
			catch (SQLException e) {
				throw new SQLException("Erro ao listar dados da tabela: " + e.getMessage());
			}		
			return listaMercados;
		}
}
