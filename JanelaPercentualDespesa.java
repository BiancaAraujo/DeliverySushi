//"Permitir que o usu�rio visualize os gastos e ganhos mensais total e percentual em cada 
//categoria de despesa e receita a fim de comparar esses valores com os totais."		
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Paint;
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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.util.Rotation;

public class JanelaPercentualDespesa extends JFrame implements ActionListener{
    
    private Container container;
    private ConnectionFactory con;
    private Connection c;
    
    private JButton btPesquisar;
    private JButton btLimpar;
    private JTextField tfMes;
    private JTextField tfAno;
    private JLabel lMes;
    private JLabel lAno;
    
    int quantRows = 0;
    float total = 0;
    float categorias[] = new float[quantRows];
    
    String nomeCategoria[] = new String[quantRows];
    ResultSet rs = null;    
    
    int mesIn;
    int anoIn;
    
    ChartPanel chartPanel;
	JFreeChart chart;
        
    private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}
    
    Verificacao formatos = new Verificacao();
    Graficos data = new Graficos();
    
    public JanelaPercentualDespesa()
    {
        btPesquisar = new JButton("Pesquisar");
        btLimpar = new JButton("Limpar");
        
        tfMes = new JTextField(10);
        tfAno = new JTextField(10);
        
        lMes = new JLabel("Mês (número correspondente ao mês)");
        lAno = new JLabel("Ano");
        
        container = this.getContentPane();
		
        container.add(lMes);
        container.add(tfMes);
        
        container.add(lAno);
        container.add(tfAno);
        
        container.add(btPesquisar);
        container.add(btLimpar);
        
        btPesquisar.addActionListener(this);
        btLimpar.addActionListener(this);

		XYDataset dataset = createDatasetPercDesp();
		chart = data.createChart(dataset);
		chartPanel = new ChartPanel(chart);
        container.add(chartPanel);
     	    
        this.setLayout(new FlowLayout()); 
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setTitle("Grafico de Comparação de Despesas"); 
        this.setSize(800, 800);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == btLimpar)
        {
            limparPercDesp();
        }
        else if(e.getSource() == btPesquisar)
        {
            initConexao();
            try{
            	if(tfMes.getText().equals("")){
    	        	JOptionPane.showMessageDialog(this, "Entre com o mês!");
    	        }
    	        else if(tfAno.getText().equals("")){
    	        	JOptionPane.showMessageDialog(this, "Entre com o ano!");
    	        }
    	        else if(!formatos.isInteger(tfMes.getText())){
    	        	JOptionPane.showMessageDialog(this, "Entre com um número inteiro como mês!");
    	        }
    	        else if(Integer.parseInt(tfMes.getText())>12 || Integer.parseInt(tfMes.getText())<1){
    	        	JOptionPane.showMessageDialog(this, "Entre com um mês válido!");
    	        }
    	        else if(!formatos.isInteger(tfAno.getText())){
    	        	JOptionPane.showMessageDialog(this, "Entre com um número inteiro como ano!");
    	        }
    	        else{	
    	        	String select = "SELECT * FROM Categoria_despesa";
    	        	PreparedStatement ptStatement = c.prepareStatement(select);
			        rs = ptStatement.executeQuery();	            
			        quantRows = 0;	            
			        while (rs.next()){
		                    quantRows++;
			    	}
			        categorias = new float[quantRows];
			        nomeCategoria = new String[quantRows];
			        
			        rs.beforeFirst();
			        int i = 0;
			        while (rs.next()){
			        	nomeCategoria[i] = rs.getString("nome_categoriad");
			        	categorias[i] = 0;
			        	i++;
			    	}

			        mesIn = Integer.parseInt(tfMes.getText());
			        anoIn = Integer.parseInt(tfAno.getText());
			        select = "SELECT * FROM Item_despesa NATURAL JOIN Categoria_despesa WHERE MONTH(data_pagamento_item_d) = (?) AND YEAR(data_pagamento_item_d) = (?)";
			        ptStatement = c.prepareStatement(select);
			        ptStatement.setInt(1,mesIn);
			        ptStatement.setInt(2,anoIn);
			        rs = ptStatement.executeQuery();
		                
			        while (rs.next()){		
		            	// Guarda em j a posição de categorias, correspondente à categoria
		            	String categoria = rs.getString("nome_categoriad");
		            	int j;
		            	for(j=0; !nomeCategoria[j].equals(categoria) && j<nomeCategoria.length; j++)
		            		;
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_item_d");
		            	float valorf = Float.parseFloat(valor);
		            	categorias[j] = categorias[j] + valorf;
		            	total = total + valorf;
		             }
			        select = "SELECT * FROM Fonte_item_despesa NATURAL JOIN Fonte_despesa NATURAL JOIN Categoria_despesa WHERE MONTH(data_pagamento_fonte_item_d) = (?) AND YEAR(data_pagamento_fonte_item_d) = (?)";
			        ptStatement = c.prepareStatement(select);
			        ptStatement.setInt(1,mesIn);
			        ptStatement.setInt(2,anoIn);
			        rs = ptStatement.executeQuery();
			        
			        while (rs.next()){		      		            	
		            	// Guarda em j a posição de categorias, correspondente à categoria
		            	String categoria = rs.getString("nome_categoriad");
		            	int j;
		            	for(j=0; !nomeCategoria[j].equals(categoria) && j<nomeCategoria.length; j++)
		            		;		            		
		            	// Guarda em valorf o valor do item
		            	String valor = rs.getString("valor_fonte_item_d");
		            	float valorf = Float.parseFloat(valor);
		            	categorias[j] = categorias[j] + valorf;
		            	total = total + valorf;
		             }
			        for(i=0; i<quantRows; i++){
						 categorias[i] = categorias[i] * 100 / total; 
					 }
			        XYDataset dataset = createDatasetPercDesp();
			        chart = data.createChart(dataset);
			        chartPanel.setChart(chart);
		        }
            }
            catch(SQLException ex){
                System.out.println("ERRO: " + ex);
            }
        }
        
    }
    
    public void limparPercDesp()
    {
        tfMes.setText("");
        tfAno.setText("");
    }
    
	// Cria um conjunto de dados de amostra  
	private XYDataset createDatasetPercDesp() {		
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series[] = new XYSeries[quantRows+1];
                int i;
                int j;
                float cat = 0;
		for(i=0; i<quantRows; i++){
			series[i] = new XYSeries(nomeCategoria[i]);
			if(categorias[i] != 0){ 
				cat = cat + categorias[i];
				for(j = 0; j < 2; j++){                            
					series[i].add(j+1,cat);
				}
			}
			dataset.addSeries(series[i]);
		}
	    return dataset;
		
	}
 
}
