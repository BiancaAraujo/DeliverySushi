import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JanelaCardapio extends JFrame implements ActionListener{
	
	private Container container;

	private JButton btMaisPedidos;//Função não implementada
	private JButton btPedir;
	
	// Tabelas do cardápio
	private JTable itensEntrada;
	private JTable itensPrincipal;
	private JTable itensSobremesa;
	private JTable itensBebida;
	
	// Para conectar com o BD
	private ConnectionFactory con;
	private Connection c;
	ResultSet rs = null;	
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

	public JanelaCardapio()
	{		
		container = this.getContentPane();	   
		
		// Colocar tabelas do cardápio na janela
		container.add(new JLabel("Cardápio"));
		// Entrada
		container.add(new JLabel("Entrada"));
		itensEntrada = itensCardapio("SELECT Item_Cardapio.nome_item, preco, pontos_fidelidade_item, ingredientes FROM Item_Cardapio INNER JOIN Entrada ON Item_Cardapio.nome_item=Entrada.nome_item WHERE disponibilidade = '1' AND a_venda = '1'");
		container.add(new JScrollPane(itensEntrada),BorderLayout.CENTER); 
		// Principal
		container.add(new JLabel("Principal"));
		itensPrincipal = itensCardapio("SELECT Item_Cardapio.nome_item, preco, pontos_fidelidade_item, ingredientes FROM Item_Cardapio INNER JOIN Principal ON Item_Cardapio.nome_item=Principal.nome_item WHERE disponibilidade = '1' AND a_venda = '1'");
		container.add(new JScrollPane(itensPrincipal),BorderLayout.CENTER); 
		// Sobremesa
		container.add(new JLabel("Sobremesa"));
		itensSobremesa = itensCardapio("SELECT Item_Cardapio.nome_item, preco, pontos_fidelidade_item, ingredientes FROM Item_Cardapio INNER JOIN Sobremesa ON Item_Cardapio.nome_item=Sobremesa.nome_item WHERE disponibilidade = '1' AND a_venda = '1'");
		container.add(new JScrollPane(itensSobremesa),BorderLayout.CENTER); 
		// Bebida
		container.add(new JLabel("Bebida"));
		itensBebida = itensCardapio("SELECT Item_Cardapio.nome_item, preco, pontos_fidelidade_item, ingredientes FROM Item_Cardapio INNER JOIN Bebida ON Item_Cardapio.nome_item=Bebida.nome_item WHERE disponibilidade = '1' AND a_venda = '1'");
		container.add(new JScrollPane(itensBebida),BorderLayout.CENTER); 
		
		// Botões
		btPedir = new JButton("Pedir Itens");
		container.add(btPedir);
		btPedir.addActionListener(this);
		
		btMaisPedidos = new JButton("Mais Pedidos");
		container.add(btMaisPedidos);
		btMaisPedidos.addActionListener(this);
		
		// Propriedades da Janela
		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Cardápio"); 
		this.setSize(800,800);       
		
	}

	// Essa função é para montar a tabela do cardápio. Ela receb o select SQL e retorna a JTable graciosa
	private JTable itensCardapio(String select) {
		initConexao();		
        try {
        	//Prepara tabela
        	 Object[] colunas = {"Item do Cardápio", "Preço unitário", "Ingredientes", "Pontos de fidelidade","Quantidade a pedir"};  		
			 DefaultTableModel model = new DefaultTableModel();		 
			 model.setColumnIdentifiers(colunas); 		 
			 Vector<Object[]> linhas = new Vector<Object[]>();
			 
        	 //Pesquisa no BD e monta tabela
        	 PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();			 
	         while (rs.next()){	        	 
	        	 linhas.add(new Object[]{rs.getString("Item_Cardapio.nome_item"),rs.getString("preco"),rs.getString("ingredientes"),rs.getString("pontos_fidelidade_item"),"0"}); 	           	
	    	 }	         
			 for (Object[] linha : linhas) {  
		         model.addRow(linha);  
		     }
			 
			 //Ajusta altura da linha e colunas 0,1,2,3 como não editáveis
			 final JTable tarefasTable = new JTable(){
				 @Override
				 public boolean isCellEditable(int row, int column) {
					 return column == 4 ? true : false;
				 }
			 };			 
			 tarefasTable.setRowHeight(25);	
			 
			 tarefasTable.setModel(model); 
			 
	         return tarefasTable; 
	         
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
		return null; 
	}	

	@Override
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==btPedir){
			// Envia todas as tabelas do cardápio (para a janela seguinte contar quantos de cada iten foram pedidos)
			JanelaPedido JPed = new JanelaPedido(itensEntrada,itensPrincipal,itensSobremesa,itensBebida);	
		}		
		else if (e.getSource()==btMaisPedidos){
			//MaisPedidos mPed = new MaisPedidos();
		}
		
	}

 
	
}
