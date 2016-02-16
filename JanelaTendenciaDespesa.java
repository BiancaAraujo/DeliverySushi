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

public class JanelaTendenciaDespesa extends JFrame implements ActionListener{
	
	private Container container;

	private ConnectionFactory con;
	private Connection c;
	
	String vetor[] = null;
	int tendenciaC[] = null;
	float medias[][] = null;
	String meses[] = null;
	int quantRows;

	//Para a lista de categorias:
	ResultSet rs = null;	

	private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}
	Graficos data = new Graficos();
	
	public JanelaTendenciaDespesa()
	{		
		container = this.getContentPane();
		
		analiseTendenciaD();
	    
		 meses = new String[12];
		 meses[0] = "Janeiro";
		 meses[1] = "Fevereiro";
		 meses[2] = "Março";
		 meses[3] = "Abril";
		 meses[4] = "Maio";
		 meses[5] = "Junho";
		 meses[6] = "Julho";
		 meses[7] = "Agosto";
		 meses[8] = "Setembro";
		 meses[9] = "Outubro";
		 meses[10] = "Novembro";
		 meses[11] = "Dezembro";
		 
		// Isso irá criar o conjunto de dados
		XYDataset dataset = createDatasetTD();
		// com base no conjunto de dados que criamos o gráfico
		JFreeChart chart = data.createChartTD(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		
	    container.add(chartPanel);
	    
	    Object[] colunas = {"Mês", "Categoria(s) de maior tendência de gasto"};  		
		 DefaultTableModel model = new DefaultTableModel();		 
		 model.setColumnIdentifiers(colunas); 		 
		 Vector<Object[]> linhas = new Vector<Object[]>(); 
		 
		 String cat[] = new String[12];
		 int valor;
		 for(int i=0; i<12; i++){
			 int j=tendenciaC[i];
			 cat[i] = "";
			 do{
				 valor = (int) (Math.log(j)/Math.log(2));
				 if(!cat[i].equals(""))
					 cat[i] = cat[i] + ",";
				 cat[i] = cat[i] + (String) vetor[valor];
				 j = j-(int)Math.pow(2,valor);
			 }while(j>=1);
		 }	
		 for(int i=0; i<12; i++){
			 linhas.add(new Object[]{meses[i],cat[i]}); 
		 }		 
		 for (Object[] linha : linhas) {  
	         model.addRow(linha);  
	     }  		 
		 final JTable tarefasTable = new JTable(); 		 
		 tarefasTable.setModel(model); 		
		 
		 TableColumnModel modeloDaColuna = tarefasTable.getColumnModel();  
	     modeloDaColuna.getColumn(0).setMaxWidth(70);
		    
		container.add(new JScrollPane(tarefasTable),BorderLayout.CENTER); 

		this.setLayout(new FlowLayout()); 
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitle("Cadastro de Despesa"); 
		this.setSize(800, 800);       
        
	}


	private void analiseTendenciaD() {
		// TODO Auto-generated method stub
		initConexao();
		
        try {
	        	String select = "SELECT * FROM Categoria_despesa";
	            PreparedStatement ptStatement = c.prepareStatement(select);
	            rs = ptStatement.executeQuery();
	            
	            quantRows = 0;
	            
	            while (rs.next()){
	    	    	quantRows++;
	    	    }
	            vetor = new String[quantRows];
	            rs.beforeFirst();
	            quantRows = 0;
	            while (rs.next()){
	    	    	vetor[quantRows] = rs.getString("nome_categoriad");
	    	    	quantRows++;
	    	    }
	            medias = new float[quantRows][12];
	            int quant[][] = new int[quantRows][12];

	            for(int j=0; j<quantRows; j++){
	            	for(int k=0; k<12; k++){
	            		medias[j][k] = 0;
	            		quant[j][k] = 0;
	            	}
	            }
	            
	            select = "SELECT valor_item_d, data_pagamento_item_d, vencimento_item_d, nome_categoriad FROM Item_despesa NATURAL JOIN Categoria_despesa";
	            ptStatement = c.prepareStatement(select);
	            rs = ptStatement.executeQuery();
	            
	            while (rs.next()){
	            	if(rs.getString("vencimento_item_d").equals("1")){
		            	// Armazena em mes o mês da despesa, em num a coluna da matriz
		            	String data = rs.getString("data_pagamento_item_d");
		            	char mes[] = new char[2];
		            	data.getChars(5, 7, mes, 0);
		            	String m = Character.toString(mes[0]) + Character.toString(mes[1]);
		            	int num =Integer.parseInt(m)-1;
		            	
		            	// Guarda em j a linha, correspondente à categoria
		            	String categoria = rs.getString("nome_categoriad");
		            	int j;
		            	for(j=0; !vetor[j].equals(categoria) && j<vetor.length; j++)
		            		;
		            		
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_item_d");
		            	float valorf = Float.parseFloat(valor);
		            	medias[j][num] = medias[j][num] + valorf;
		            	quant[j][num] = quant[j][num] + 1;
	            	}
	    	    }
	            
	            select = "SELECT valor_fonte_item_d, data_pagamento_fonte_item_d, pago_fonte_item_d, nome_categoriad FROM Fonte_item_despesa NATURAL JOIN Fonte_despesa NATURAL JOIN Categoria_despesa";
	            ptStatement = c.prepareStatement(select);
	            rs = ptStatement.executeQuery();
	            
	            while (rs.next()){
	            	if(rs.getString("pago_fonte_item_d").equals("1")){
		            	// Armazena em mes o mês da despesa, em num a coluna da matriz
		            	String data = rs.getString("data_pagamento_fonte_item_d");
		            	char mes[] = new char[2];
		            	data.getChars(5, 7, mes, 0);
		            	String m = Character.toString(mes[0]) + Character.toString(mes[1]);
		            	int num =Integer.parseInt(m)-1;
		            	
		            	// Guarda em j a linha, correspondente à categoria
		            	String categoria = rs.getString("nome_categoriad");
		            	int j;
		            	for(j=0; !vetor[j].equals(categoria) && j<vetor.length; j++)
		            		;
		            		
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_fonte_item_d");
		            	float valorf = Float.parseFloat(valor);
		            	medias[j][num] = medias[j][num] + valorf;
		            	quant[j][num] = quant[j][num] + 1;
	            	}
	    	    }         
	            
	            for(int j=0; j<quantRows; j++){
	            	for(int k=0; k<12; k++){
	            		if(quant[j][k] > 0)
	            			medias[j][k] = medias[j][k] / quant[j][k];
	            	}
	            }
	            
	            float tendenciaV[] = new float[12];
	            tendenciaC = new int[12];
	            for(int j=0; j<12; j++){
	            	tendenciaV[j] = medias[0][j];
	            	tendenciaC[j] = 0;
	            }
	            for(int j=0; j<quantRows; j++){
	            	for(int k=0; k<12; k++){
	            		if(medias[j][k] > tendenciaV[k]){
	            			tendenciaV[k] = medias[j][k];
	            			tendenciaC[k] = (int) (Math.pow(2,j));
	            		}
	            		else if(medias[j][k] == tendenciaV[k]){
	            			tendenciaC[k] = (int) (tendenciaC[k] + Math.pow(2,j));
	            		}
	            	}	            	
	            }	  	                         
        } catch (SQLException ex) {
            System.out.println("ERRO: " + ex);
        } 
	}

	/**
	 * Cria um conjunto de dados de amostra 
	 */
 
	private XYDataset createDatasetTD() {		
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series[] = new XYSeries[quantRows];
		
		for(int i=0; i<quantRows; i++){
			series[i] = new XYSeries(vetor[i]);
			for(int j=0; j<12; j++){
				series[i].add(j+1,medias[i][j]);
			}	
			dataset.addSeries(series[i]);
		}
	    return dataset;
		
	}	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
