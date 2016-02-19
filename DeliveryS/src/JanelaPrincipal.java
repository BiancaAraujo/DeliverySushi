
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class JanelaPrincipal extends JFrame implements ActionListener {

	    // Itens da janela para funções não implementadas
		private JMenuBar barraMenu;
		private JMenu menuCardapio;
		private JMenuItem cardapioGerenciar;
		private JMenuItem fechaCaixa;
		private JButton btCliente;
		
		// Itens para as funções implementadas
		private Container container;		
		private JButton btPedido;
		private JButton btPagamento;
			
		public JanelaPrincipal()
		{ 
			// Itens da janela para funções não implementadas
			barraMenu = new JMenuBar();
			menuCardapio = new JMenu ("Cardápio"); 
			cardapioGerenciar = new JMenuItem("Gerenciar");
			fechaCaixa = new JMenuItem("Fechamento do Caixa");
			btCliente = new JButton("Cadastrar/Alterar Cliente");
			barraMenu.add(menuCardapio);	
			barraMenu.add(fechaCaixa);
			menuCardapio.add(cardapioGerenciar);
			fechaCaixa.addActionListener(this);
			btCliente.addActionListener(this);
			cardapioGerenciar.addActionListener(this);
			
			// Itens para as funções implementadas
			container = this.getContentPane();
			btPedido = new JButton("Novo Pedido");
			btPagamento = new JButton("Confirmar Pagamento");	
			
	        container.add(btPedido);
	        container.add(btPagamento);
	        container.add(btCliente);  
			
			btPedido.addActionListener(this);
			btPagamento.addActionListener(this);			
			
			// Propriedades da Janela Principal
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLayout(new FlowLayout()); 
			this.setJMenuBar(barraMenu);
			this.pack();
			//this.setResizable(false);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setVisible(true);
			this.setTitle("Delivery"); 
			this.setSize(800, 800); 			 
		}
			
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == fechaCaixa)
			{
				//JanelaFechaCaixa jFechaC = new JanelaFechaCaixa();
			}
			else if (e.getSource() == btCliente)
			{
				//JanelaCliente jCli = new JanelaCliente();
			}
			else if (e.getSource() == btPedido)
			{
				JanelaCardapio jCardapio = new JanelaCardapio();
			}
			else if (e.getSource() == btPagamento)
			{
				JanelaPagamento jPagamento = new JanelaPagamento();
			}
			else if (e.getSource() == cardapioGerenciar)
			{
				//JanelaCardapioG jCard = new JanelaCardapioG();
			}
		}

}