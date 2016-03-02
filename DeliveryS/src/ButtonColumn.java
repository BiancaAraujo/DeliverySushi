import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener{  

private ConnectionFactory con;
private Connection c;
private void initConexao(){if(con == null){ con = new ConnectionFactory(); c = con.getConnection();}}

        JTable table;  
        JButton renderButton;  
        JButton editButton;  
        String text;  
        String metodo;
        
    	ResultSet rs = null;
    	ResultSet rs2 = null;
    	ResultSet rs3 = null;
    	ResultSet rs4 = null;
   
        public ButtonColumn(JTable table, int column, String tabela){  
            super();  
            metodo = tabela;
            this.table = table;  
            renderButton = new JButton();  
   
            editButton = new JButton();  
            editButton.setFocusPainted(false);  
            editButton.addActionListener(this);  
   
            TableColumnModel columnModel = table.getColumnModel();  
            columnModel.getColumn(column).setCellRenderer(this);  
            columnModel.getColumn(column).setCellEditor(this);  
        }  
   
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)  {  
            if (hasFocus){  
                renderButton.setForeground(table.getForeground());  
                renderButton.setBackground(UIManager.getColor("Button.background"));  
            }  
            else if (isSelected){  
                renderButton.setForeground(table.getSelectionForeground());  
                 renderButton.setBackground(table.getSelectionBackground());  
            }  
            else{  
                renderButton.setForeground(table.getForeground());  
                renderButton.setBackground(UIManager.getColor("Button.background"));  
            }  
   
            renderButton.setText( (value == null) ? "" : value.toString() );  
            return renderButton;  
        }  
   
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            text = (value == null) ? "" : value.toString();  
            editButton.setText( text );  
            return editButton;  
        }  
   
        public Object getCellEditorValue(){  
            return text;  
        }  
   
        public void actionPerformed(ActionEvent e){  
        	if(metodo.equals("pedidosAbertos")){
        		fireEditingStopped();  
        		
        		String code;
        		float valor = 0;
        		float valorAntigo = 0;
        		
        		code = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
        		
        		((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow()); 
        		
        		initConexao();
        		
        		
				String select = "UPDATE Pedido SET status=? WHERE pedido_Id=?";
				String select2 = "SELECT Pedido.pedido_Id, valor_total FROM Pedido WHERE pedido_Id=?";
				String select3 = "SELECT Empresa.caixa FROM Empresa";
				String select4 = "UPDATE Empresa SET caixa=?";
			
				

				try {
					PreparedStatement ptStatement = c.prepareStatement(select);
					ptStatement.setString(1, "1");
					ptStatement.setString(2, code);
					ptStatement.executeUpdate();
					
					
					PreparedStatement ptStatement2 = c.prepareStatement(select2);
					ptStatement2.setString(1, code);
			        rs2 = ptStatement2.executeQuery();
			        
			        while(rs2.next())
			        {
			        	valor = Float.parseFloat(rs2.getString("valor_total"));
			        }
			         
			         PreparedStatement ptStatement3 = c.prepareStatement(select3);
				     rs3 = ptStatement3.executeQuery();
				     rs3.next();
				     
				     valorAntigo = Float.parseFloat(rs3.getString("caixa"));
			         
			         PreparedStatement ptStatement4 = c.prepareStatement(select4);
						ptStatement4.setFloat(1, valor+valorAntigo);
						ptStatement4.executeUpdate();
			            
					JOptionPane.showMessageDialog(editButton, this, "Pedido confirmado com sucesso!", 0);
		        	} catch (SQLException ex) {
		        		System.out.println("ERRO: " + ex);
		        } 			
        		
                //ConfirmaPagamento janela = new ConfirmaPagamento((String) table.getModel().getValueAt(table.getSelectedRow(), 6));
        	}
        }  
    
    
    }  
