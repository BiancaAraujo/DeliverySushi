
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class JanelaPagamento extends JFrame{
	
	String code;
	
	private Container container;
	
	private JTable pedidos;
    
    // Para conectar com o BD
 	private ConnectionFactory con;
 	private Connection c;
 	ResultSet rs = null;	
 	ResultSet rs2 = null;
 	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

    public JanelaPagamento()
    {
    	
    	container = this.getContentPane();	 
    	
    	
    	
    	container.add(new JLabel("Pedidos"));
		pedidos = pedidosAbertos("SELECT Pedido.pedido_Id, cpf, valor_total, forma_pagamento, entregador, status FROM Pedido WHERE status = '0'");
		container.add(new JScrollPane(pedidos),BorderLayout.CENTER); 
		
		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Pagamento"); 
		this.setSize(800,800);    

    	
    }
    
    private JTable pedidosAbertos(String select) {
		initConexao();		
        try {
        	//Prepara tabela
        	 Object[] colunas = {"code", "CPF do cliente", "Valor", "Forma de Pagamento","Entregador", "Status", ""};  		
			 DefaultTableModel model = new DefaultTableModel();		 
			 model.setColumnIdentifiers(colunas); 		 
			 Vector<Object[]> linhas = new Vector<Object[]>();
			 
        	 //Pesquisa no BD e monta tabela
        	 PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();			 
	         while (rs.next()){	        	 
	        	 linhas.add(new Object[]{rs.getString("pedido_Id"), rs.getString("cpf"),rs.getString("valor_total"),rs.getString("forma_pagamento"),rs.getString("entregador"), rs.getString("status"), "Confirmar"}); 	           	
	         }	         
			 for (Object[] linha : linhas) {  
		         model.addRow(linha);  
		     }
			 
			 final JTable tarefasTable = new JTable();
			 
			 tarefasTable.setModel(model); 
			 tarefasTable.setFillsViewportHeight(true);
			 
			 ButtonColumn buttonColumn = new ButtonColumn(tarefasTable, 6, "pedidosAbertos");

			 
	         return tarefasTable; 
	         
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
		return null; 
	}

}
