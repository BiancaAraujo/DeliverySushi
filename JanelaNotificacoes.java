import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

public class JanelaNotificacoes extends JFrame implements ActionListener{
	
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
	
	private JButton btItemDespesa[];
	private JButton btFonteDespesa[];

	JScrollPane painel=null;
	boolean principal;
	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

	public JanelaNotificacoes(boolean inicio)
	{		
		principal = inicio;
		container = this.getContentPane(); 
	   
		JTable itens = itensNaoPagos();
		container.add(new JScrollPane(itens),BorderLayout.CENTER); 
		
		JTable fontes = fontesNaoPagas();
		container.add(new JScrollPane(fontes),BorderLayout.CENTER); 
		
		JTable itensRec = itensNaoRecebidos();
		container.add(new JScrollPane(itensRec),BorderLayout.CENTER);
		
		JTable fontesRec = fontesNaoRecebidas();
		container.add(new JScrollPane(fontesRec),BorderLayout.CENTER);
		
		

		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Notificações"); 
		this.setSize(800, 800);       
		this.addWindowListener(new WindowAdapter(){
			
		public void windowClosing(WindowEvent e) { 
			if(principal){
				JanelaPrincipal janela = new JanelaPrincipal();
			}
			
			}
		});   
	}


	private JTable fontesNaoRecebidas() {
		// TODO Auto-generated method stub
		initConexao();		
        try {	            
	         String select = "SELECT data_ultimo_recebimento_fonte_r, cod_fonte_r, nome_fonte_r, valor_fonte_r, tipo_valor_fonte_r, periodo_fonte_r FROM Fonte_receita WHERE status_fonte_r = '0'";
	          
	         PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();
	            
	         Object[] colunas = {"Fonte de receita", "Valor previsto", "Data prevista do recebimento", "", "code"};  		
			 DefaultTableModel model = new DefaultTableModel();		 
			 model.setColumnIdentifiers(colunas); 		 
			 Vector<Object[]> linhas = new Vector<Object[]>(); 
			 
			 select = "select DATE_ADD(?,INTERVAL ? day)";
	         while (rs.next()){
	        	 
	        	ptStatement = c.prepareStatement(select);
				ptStatement.setString(1, rs.getString("data_ultimo_recebimento_fonte_r"));
				ptStatement.setString(2, rs.getString("periodo_fonte_r"));
				rs2 = ptStatement.executeQuery();
				rs2.next();
		      linhas.add(new Object[]{rs.getString("nome_fonte_r"),rs.getString("valor_fonte_r"),rs2.getString(1), "Receber", rs.getString("cod_fonte_r")}); 	           	
	    	 }            
	         
			 for (Object[] linha : linhas) {  
		         model.addRow(linha);  
		     }  		 
			 final JTable tarefasTable = new JTable(); 		 
			 tarefasTable.setModel(model); 		
			 
			 ButtonColumn buttonColumn = new ButtonColumn(tarefasTable, 3, "fonteNaoRecebida");
			 
			 tarefasTable.removeColumn(tarefasTable.getColumn("code"));   	           
	         return tarefasTable;            
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
		return null; 
	}

	private JTable itensNaoRecebidos() {
		// TODO Auto-generated method stub
		initConexao();		
        try {	            
	         String select = "SELECT cod_item_r, nome_item_r, valor_item_r FROM Item_receita WHERE recebimento_item_r = '0'";
	         PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();
	            
	         Object[] colunas = {"Item de receita", "Valor total", "", "code"};  		
			 DefaultTableModel model = new DefaultTableModel();		 
			 model.setColumnIdentifiers(colunas); 		 
			 Vector<Object[]> linhas = new Vector<Object[]>(); 
			 
	         while (rs.next()){
		      linhas.add(new Object[]{rs.getString("nome_item_r"),rs.getString("valor_item_r"),"Receber", rs.getString("cod_item_r")}); 	           	
	    	 }            
	         
			 for (Object[] linha : linhas) {  
		         model.addRow(linha);  
		     }  		 
			 final JTable tarefasTable = new JTable(); 		 
			 tarefasTable.setModel(model); 		
			 
			 ButtonColumn buttonColumn = new ButtonColumn(tarefasTable, 2, "itemNaoRecebido");
			 
			 tarefasTable.removeColumn(tarefasTable.getColumn("code"));   	           
	         return tarefasTable;            
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
		return null; 
	}

	private JTable fontesNaoPagas() {
		// TODO Auto-generated method stub
		initConexao();
		
        try {	            
	         String select = "SELECT data_ultimo_vencimento_fonte_r, cod_fonte_d, nome_fonte_d, valor_fonte_d, tipo_valor_fonte_d, data_abertura_fonte_d, periodo_fonte_d FROM Fonte_despesa WHERE vencimento_fonte_d = '0'";
	          
	         PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();
	            
	         Object[] colunas = {"Fonte de despesa", "Valor previsto", "Data do vencimento", "", "code"};  		
			 DefaultTableModel model = new DefaultTableModel();		 
			 model.setColumnIdentifiers(colunas); 		 
			 Vector<Object[]> linhas = new Vector<Object[]>(); 
			 
			 select = "select DATE_ADD(?,INTERVAL ? day)";
	         while (rs.next()){
	        	 
	        	ptStatement = c.prepareStatement(select);
				ptStatement.setString(1, rs.getString("data_ultimo_vencimento_fonte_r"));
				ptStatement.setString(2, rs.getString("periodo_fonte_d"));
				rs2 = ptStatement.executeQuery();
				rs2.next();
		      linhas.add(new Object[]{rs.getString("nome_fonte_d"),rs.getString("valor_fonte_d"),rs2.getString(1), "Pagar", rs.getString("cod_fonte_d")}); 	           	
	    	 }            
	         
			 for (Object[] linha : linhas) {  
		         model.addRow(linha);  
		     }  		 
			 final JTable tarefasTable = new JTable(); 		 
			 tarefasTable.setModel(model); 		
			 
			 ButtonColumn buttonColumn = new ButtonColumn(tarefasTable, 3, "fonteNaoPaga");
			 
			 tarefasTable.removeColumn(tarefasTable.getColumn("code"));   	           
	         return tarefasTable;            
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
		return null; 
	}

	private JTable itensNaoPagos() {
		// TODO Auto-generated method stub
		initConexao();
		
        try {	            
	         String select = "SELECT cod_item_d, nome_item_d, valor_item_d, data_vencimento_item_d FROM Item_despesa WHERE vencimento_item_d = '0'";
	         PreparedStatement ptStatement = c.prepareStatement(select);
	         rs = ptStatement.executeQuery();
	            
	         Object[] colunas = {"Item de despesa", "Valor total", "Data do vencimento", "", "code"};  		
			 DefaultTableModel model = new DefaultTableModel();		 
			 model.setColumnIdentifiers(colunas); 		 
			 Vector<Object[]> linhas = new Vector<Object[]>(); 
			 
			 
	         while (rs.next()){
		      linhas.add(new Object[]{rs.getString("nome_item_d"),rs.getString("valor_item_d"),rs.getString("data_vencimento_item_d"), "Pagar", rs.getString("cod_item_d")}); 	           	
	    	 }            
	         
			 for (Object[] linha : linhas) {  
		         model.addRow(linha);  
		     }  		 
			 final JTable tarefasTable = new JTable(); 		 
			 tarefasTable.setModel(model); 		
			 
			 ButtonColumn buttonColumn = new ButtonColumn(tarefasTable, 3, "itemPedido");
			 
			 tarefasTable.removeColumn(tarefasTable.getColumn("code"));   	           
	         return tarefasTable;            
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        }
		return null; 
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {}

 
	
}
