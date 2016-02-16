import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ListarDespesas extends JFrame implements ActionListener{
	
	private JButton btExcluir_Item;
	private JButton btEditar_Item;	
	
	private JButton btExcluir_Fonte;
	private JButton btEditar_Fonte;
	
	private Container container;

	private ConnectionFactory con;
	private Connection c;
	
	JPanel dataPanel1 = new JPanel();
	JPanel dataPanel2 = new JPanel();
	JPanel b1Panel = new JPanel();
	JPanel b2Panel = new JPanel();
	
	private final String DELETE_ITEM = "DELETE FROM Item_despesa WHERE cod_item_d =?";
	private final String DELETE_FONTE = "DELETE FROM Fonte_despesa WHERE cod_fonte_d =?";
	private final String UPDATE_ITEM = "UPDATE Item_despesa SET cod_item_d=?, nome_item_d=?, valor_item_d=?, data_cadastro_item_d=?, data_vencimento_item_d=?, vencimento_item_d=?, prioridade_item_d=?, data_pagamento_item_d=?, cod_categoria_d=?";
	
	private String string_categoria;
	private String vencimento;
	private String tipo_valor;
	private String vencimento_fonte;
	
	ResultSet rs = null;
	ResultSet rs2 = null;
	
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}
	
	public ListarDespesas(){
				
		final JTable itens = itensDesp();
		final JTable fontes = fontesDesp();
		
		btExcluir_Item = new JButton("Excluir");
		btExcluir_Item.addActionListener(
	        	new ActionListener(){
	        	    public void actionPerformed(ActionEvent e){ 
	        	    	
	        	    	int linha = -1;
	    				linha = itens.getSelectedRow();
	    				if(linha >= 0) {
	    					
	    			            if(JOptionPane.showConfirmDialog(null, "Deseja excluir este item ?") == 0)
	    			            {
			    					String codItem = (String) itens.getValueAt(linha,0);
			    					removerItemDesp(codItem);
			    					((DefaultTableModel) itens.getModel()).removeRow(linha);
			    					JOptionPane.showMessageDialog(null, "Item removido com sucesso!");
	    			            }
	    					
	    				}
	    				else {
	    					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha!");
	    				}
	        	    	
	        	    }
	        	}
	        	);
		btEditar_Item = new JButton("Editar");  
		btEditar_Item.addActionListener(
	        	new ActionListener(){
	        	    public void actionPerformed(ActionEvent e){ 
	        	    	
	        	    	int linha = -1;
	    				linha = itens.getSelectedRow();
	    				if(linha >= 0) {

		    					String codItem = (String) itens.getValueAt(linha,0);
		    					EditarDespesaItem di = new EditarDespesaItem((DefaultTableModel) itens.getModel(),codItem,linha);
		    					di.setVisible(true);
	    						    					
	    				}
	    				else {
	    					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha!");
	    				}
	        	    	
	        	    }
	        	}
	        	);
		
		btExcluir_Fonte = new JButton("Excluir");
		btExcluir_Fonte.addActionListener(
	        	new ActionListener(){
	        	    public void actionPerformed(ActionEvent e){ 
	        	    	
	        	    	int linha = -1;
	    				linha = fontes.getSelectedRow();
	    				if(linha >= 0) {
	    					if(JOptionPane.showConfirmDialog(null, "Deseja excluir este item ?") == 0)
	    					{
		    					String codFonte = (String) fontes.getValueAt(linha,0);
		    					removerFonteDesp(codFonte);
		    					((DefaultTableModel) fontes.getModel()).removeRow(linha);
		    					JOptionPane.showMessageDialog(null, "Fonte removida com sucesso!");
	    					}
	    					
	    				}
	    				else {
	    					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha!");
	    				}
	        	    	
	        	    }
	        	}
	        	);
		btEditar_Fonte = new JButton("Editar");
		btEditar_Fonte.addActionListener(
	        	new ActionListener(){
	        	    public void actionPerformed(ActionEvent e){ 
	        	    	
	        	    	int linha = -1;
	    				linha = fontes.getSelectedRow();
	    				if(linha >= 0) {
	    					String codFonte = (String) fontes.getValueAt(linha,0);
	    					EditarDespesaFonte df = new EditarDespesaFonte((DefaultTableModel) fontes.getModel(),codFonte,linha);
	    					df.setVisible(true);
	    					
	    				}
	    				else {
	    					JOptionPane.showMessageDialog(null, "É necessário selecionar uma linha!");
	    				}
	        	    	
	        	    }
	        	}
	        	);
		

	    TitledBorder topBorder = BorderFactory.createTitledBorder("Itens");
	    topBorder.setTitlePosition(TitledBorder.TOP);
	   
	    dataPanel1.setLayout( new BorderLayout() ); 
	    dataPanel1.setBorder(topBorder);
	    dataPanel1.add(new JScrollPane(itens),BorderLayout.CENTER);
	    
	    b1Panel.add(btEditar_Item);
	    b1Panel.add(btExcluir_Item);
	    
	    TitledBorder topBorder1 = BorderFactory.createTitledBorder("Fontes");
	    topBorder1.setTitlePosition(TitledBorder.TOP);
		
	    dataPanel2.setLayout( new BoxLayout( dataPanel2, BoxLayout.Y_AXIS ) ); 
	    dataPanel2.setBorder(topBorder1);
	    dataPanel2.add(new JScrollPane(fontes),BorderLayout.CENTER);
	    
	    b2Panel.add(btEditar_Fonte);
	    b2Panel.add(btExcluir_Fonte);
		 
		 fontes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 itens.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		 this.setLayout(new BoxLayout( getContentPane(), BoxLayout.Y_AXIS)); 
		 add(dataPanel1);
		 add(b1Panel);
		 add(dataPanel2);
		 add(b2Panel);
		 this.setVisible(true);
		 this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		 this.setTitle("Despesas Cadastradas"); 
		 this.setSize(800, 800); 
		 
	}

	private JTable fontesDesp() {
		
		initConexao();
		
		
		
		try {
			
			String select = "SELECT cod_fonte_d, nome_fonte_d, valor_fonte_d, data_cadastro_fonte_d, data_abertura_fonte_d, data_vencimento_fonte_d, data_ultimo_vencimento_fonte_r, periodo_fonte_d, vencimento_fonte_d, prioridade_fonte_d, tipo_valor_fonte_d, cod_categoria_d FROM Fonte_despesa";
			
			PreparedStatement ptStatement = c.prepareStatement(select);
	        rs = ptStatement.executeQuery();
	        
	        Object[] colunas = {"Código", "Nome", "Valor", "Categoria","Data de Cadastro", "Data de Abertura", "Data de Vencimento", "Data do último vencimento", "Período", "Vencimento", "Prioridade", "Tipo de Valor"};
			
	        DefaultTableModel model = new DefaultTableModel();
	   	 
	   	 	model.setColumnIdentifiers(colunas); 
	   	 
	   	 	Vector<Object[]> linhas = new Vector<Object[]>();
	        
		   	 while (rs.next()){
		   		 
		   		String selectCategoria = "SELECT * FROM Categoria_despesa";
		        PreparedStatement ptStatement2 = c.prepareStatement(selectCategoria);
	            rs2 = ptStatement2.executeQuery();
	        	rs2.first();
	                do{	                	
	                    if(rs2.getString("cod_categoria_d").equals(rs.getString("cod_categoria_d"))){
	                        string_categoria = rs2.getString("nome_categoriad");
	                    }
	                }while(rs2.next());
	                
	                int i = Integer.parseInt(rs.getString("tipo_valor_fonte_d"));
	                
	                if(i == 0){
	                	tipo_valor = "Fixo";
	                }
	                
	                else{
	                	tipo_valor = "Varia a cada período";
	                }
	                
	                int j = Integer.parseInt(rs.getString("vencimento_fonte_d"));
	                
	                if(j == 0){
	                	vencimento_fonte = "Despesa Não Finalizada";
	                }
	                
	                else{
	                	vencimento_fonte = "Despesa Finalizada";
	                }
					
					linhas.add(new Object[]{rs.getString("cod_fonte_d"),rs.getString("nome_fonte_d"),rs.getString("valor_fonte_d"),string_categoria,rs.getString("data_cadastro_fonte_d"),rs.getString("data_abertura_fonte_d"),rs.getString("data_vencimento_fonte_d"),rs.getString("data_ultimo_vencimento_fonte_r"),rs.getString("periodo_fonte_d"),vencimento_fonte,rs.getString("prioridade_fonte_d"),tipo_valor}); 	           	
		    	 }
	   	 	
	   	 	for (Object[] linha : linhas) {  
	   	 		model.addRow(linha);  
	   	 	}  
		 
	   	 	final JTable Table = new JTable(); 
		 
	   	 	Table.setModel(model);

	   	 
	   	return Table; 
	   	 	
		}  catch (SQLException ex) {
	        System.out.println("ERRO: " + ex);
	    }
		
		return null;
	}

	private JTable itensDesp() {
		
		initConexao();
				
		try {
			
			String select = "SELECT cod_item_d, nome_item_d, valor_item_d, data_cadastro_item_d, data_vencimento_item_d,  vencimento_item_d, prioridade_item_d, data_pagamento_item_d, cod_categoria_d FROM Item_despesa";
			
			PreparedStatement ptStatement = c.prepareStatement(select);
	        rs = ptStatement.executeQuery();
	            
	        Object[] colunas = {"Código", "Nome", "Valor", "Categoria", "Data de Cadastro", "Data de Vencimento", "Vencimento", "Prioridade", "Data de Pagamento"};
			
	        DefaultTableModel model = new DefaultTableModel();
	   	 
	   	 	model.setColumnIdentifiers(colunas); 
	   	 
	   	 	Vector<Object[]> linhas = new Vector<Object[]>();
	        
		   	 while (rs.next()){
		   		 
		   		String selectCategoria = "SELECT * FROM Categoria_despesa";
		        PreparedStatement ptStatement2 = c.prepareStatement(selectCategoria);
	            rs2 = ptStatement2.executeQuery();
	        	rs2.first();
	                do{	                	
	                    if(rs2.getString("cod_categoria_d").equals(rs.getString("cod_categoria_d"))){
	                        string_categoria = rs2.getString("nome_categoriad");
	                    }
	                }while(rs2.next());
	                
	                int j = Integer.parseInt(rs.getString("vencimento_item_d"));
	                
	                if(j == 0){
	                	vencimento = "Não pago";
	                }
	                
	                else{
	                	vencimento = "Pago";
	                }
					
					linhas.add(new Object[]{rs.getString("cod_item_d"),rs.getString("nome_item_d"),rs.getString("valor_item_d"),string_categoria,rs.getString("data_cadastro_item_d"),rs.getString("data_vencimento_item_d"),vencimento,rs.getString("prioridade_item_d"),rs.getString("data_pagamento_item_d")}); 	           	
		    	 }
	   	 	
	   	 	for (Object[] linha : linhas) {  
	   	 		model.addRow(linha);  
	   	 	}  
		 
	   	 	final JTable Table = new JTable(); 
		 
	   	 	Table.setModel(model);
	   	 	
	   	 
	   	return Table; 
	   	 	
		}  catch (SQLException ex) {
	        System.out.println("ERRO: " + ex);
	    }
		return null;
	}
	
	private void removerItemDesp(String codItem)
	{
		initConexao();
		
		try{
			
			PreparedStatement ptStatement = c.prepareStatement(DELETE_ITEM);
			ptStatement.setString(1, codItem);
            ptStatement.executeUpdate();	
			
		} catch (SQLException ex) {
	        System.out.println("ERRO: " + ex);
	    }
	}
	
	private void removerFonteDesp(String codFonte)
	{
		initConexao();
		
		try{
			
			PreparedStatement ptStatement = c.prepareStatement(DELETE_FONTE);
			ptStatement.setString(1, codFonte);
            ptStatement.executeUpdate();	
			
		} catch (SQLException ex) {
	        System.out.println("ERRO: " + ex);
	    }
	}
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
