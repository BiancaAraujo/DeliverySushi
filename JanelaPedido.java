import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class JanelaPedido extends JFrame implements ActionListener{
	
	private Container container;

	private ConnectionFactory con;
	private Connection c;
	
	String vetor[] = null;
	int tendenciaC[] = null;
	float medias[][] = null;
	String meses[] = null;
	int quantRows;

	ResultSet rs = null;
	ResultSet rs2 = null;
	
	private JButton btPedir;
	private JButton btMaisPedidos;
	private JTable itens;

	JScrollPane painel=null;
	boolean principal;
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

	public JanelaPedido()
	{		
		container = this.getContentPane(); 
	   
		itens = itensCardapio();
		container.add(new JScrollPane(itens),BorderLayout.CENTER); 
		
		btPedir = new JButton("Pedir Itens");
		btMaisPedidos = new JButton("Mais Pedidos");
		
		container.add(btPedir);
		container.add(btMaisPedidos);
		
		btPedir.addActionListener(this);
		btMaisPedidos.addActionListener(this);
		
		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Cardápio"); 
		this.setSize(800, 800);       
		
		/*this.addWindowListener(new WindowAdapter(){
			
		/*public void windowClosing(WindowEvent e) { 
			if(principal){
				JanelaPrincipal janela = new JanelaPrincipal();
			}
			
			}
		}); */  
	}

	private JTable itensCardapio() {
		// TODO Auto-generated method stub
		initConexao();
		
        try {	            
	         String select = "SELECT nome_item, preco, pontos_fidelidade_item, ingredientes FROM Item_Cardapio WHERE disponibilidade = '1' AND a_venda = '1'";
	         PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();
	         
	         Object[] colunas = {"Item do Cardápio", "Preço unitário", "Ingredientes", "Pontos de fidelidade","Quantidade a pedir", "code"};  		
			 DefaultTableModel model = new DefaultTableModel();		 
			 model.setColumnIdentifiers(colunas); 		 
			 Vector<Object[]> linhas = new Vector<Object[]>(); 
			 
			 
	         while (rs.next()){	        	 
	        	 linhas.add(new Object[]{rs.getString("nome_item"),rs.getString("preco"),rs.getString("ingredientes"),rs.getString("pontos_fidelidade_item"),"0", rs.getString("nome_item")}); 	           	
	    	 }            
	         
			 for (Object[] linha : linhas) {  
		         model.addRow(linha);  
		     }  		 
			 final JTable tarefasTable = new JTable(); 		 
			 tarefasTable.setModel(model); 		
			// tarefasTable.setCellEditor();
			 
			TextColumn textColumn = new TextColumn(tarefasTable, 4, "itemCardapio");
			 
			 tarefasTable.removeColumn(tarefasTable.getColumn("code"));  
			 tarefasTable.setRowHeight(25);		 
			 
	         return tarefasTable;            
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
		return null; 
	}	

	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==btPedir){	        
	        Object quantia = itens.getValueAt(0, 4);
	        System.out.println(quantia.toString());			
		}		
		else if (e.getSource()==btMaisPedidos){
			//MaisPedidos mPed = new MaisPedidos();
		}
		
	}

 
	
}
