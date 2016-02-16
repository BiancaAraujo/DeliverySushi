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
        	if(metodo.equals("itemNaoPago")){
        		fireEditingStopped();  
                JanelaPagaItem janela = new JanelaPagaItem((String) table.getModel().getValueAt(table.getSelectedRow(), 4));
        	}
        	else if(metodo.equals("fonteNaoPaga")){
        		fireEditingStopped();  
                JanelaPagaFonte janela = new JanelaPagaFonte((String) table.getModel().getValueAt(table.getSelectedRow(), 4));
        	}
        	else if(metodo.equals("itemNaoRecebido")){
        		fireEditingStopped();  
                JanelaRecebeItem janela = new JanelaRecebeItem((String) table.getModel().getValueAt(table.getSelectedRow(), 3));
        	}
        	else if(metodo.equals("fonteNaoRecebida")){
        		fireEditingStopped();  
                JanelaRecebeFonte janela = new JanelaRecebeFonte((String) table.getModel().getValueAt(table.getSelectedRow(), 4));
        	}
        }  
    
    
    }  
