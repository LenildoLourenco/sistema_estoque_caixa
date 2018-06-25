import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.*;

public class CaixaDia extends JFrame implements ActionListener
{
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	

	
	// menu, menuitem, menuBar
	
	JMenuBar barraMenu;
	JMenuItem iSair;
	JMenu mSair;

	
	// painel
	
	JPanel p1;
	
	// textfield e labels
	
	JLabel lInicioCaixa, lRetiradoCaixa, lTotalDinheiro, lTotalCartaoDebito, lTotalCartaoCredito, lTotalCheque,
		 lTotalVendido,lCaixa;
	
	JTextField tInicioCaixa, tRetiradoCaixa, tTotalDinheiro, tTotalCartaoDebito, tTotalCartaoCredito, tTotalCheque,
		tTotalVendido;
	
	// botoes
	
	JButton bRetirarCaixa, bFinalizarCaixa, bIniciarCaixa;
	
	// Colocando data
	String data;
	JLabel lData, lTextoData;
	Date agora;
	DateFormat df;
	

	
	CaixaDia()
	{
		setTitle("Caixa do Dia");
		setSize(360,375);
		setLocationRelativeTo(null);
		
		// definindo frame visível ja na classe e não redimensionável
		setVisible(true);
		setResizable(false);
		
		
		
		setIconImage(new ImageIcon("Imagens/cx_dia.png").getImage());
		
		// layout nulo
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		
		
		
		
		// menu sair
		barraMenu = new JMenuBar();
		
		mSair = new JMenu("Sair");
		mSair.setMnemonic(KeyEvent.VK_S);
		
		
		iSair = new JMenuItem("Sair");
		iSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		iSair.addActionListener(this);
		
		
		mSair.add(iSair);
		barraMenu.add(mSair);
		setJMenuBar(barraMenu);
		
		// data
		
		agora = new Date();
		df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		data = df.format(agora);
		
		lTextoData = new JLabel("Data");
		lTextoData.setBounds(5,5,50,20);
		p1.add(lTextoData);
		lData = new JLabel(data);
		
		lData.setBounds(5,25,100,20);
		lData.setForeground(Color.white);
		lData.setFont(new Font("Arial",Font.PLAIN,16));
		p1.add(lData);
		
		
		// caixa
		lCaixa = new JLabel("Caixa:1");
		lCaixa.setFont(new Font("Arial",Font.BOLD,13));
		lCaixa.setBounds(110,5,100,20);
		p1.add(lCaixa);
			
		
		// labels e textField
		
		lInicioCaixa = new JLabel("Início do Caixa");
		lInicioCaixa.setBounds(5,50,100,20);
		p1.add(lInicioCaixa);
		
		tInicioCaixa = new JTextField(8);
		tInicioCaixa.setBounds(5,70,100,30);
		tInicioCaixa.setEditable(false);
		p1.add(tInicioCaixa);
		
		lRetiradoCaixa = new JLabel("Retirado do Caixa");
		lRetiradoCaixa.setBounds(115,50,100,20);
		p1.add(lRetiradoCaixa);
		
		tRetiradoCaixa = new JTextField(8);
		tRetiradoCaixa.setEditable(false);
		tRetiradoCaixa.setBounds(115,70,100,30);
		p1.add(tRetiradoCaixa);
		
		lTotalDinheiro = new JLabel("Total em Dinheiro");
		lTotalDinheiro.setBounds(225,50,100,20);
		p1.add(lTotalDinheiro);
		
		tTotalDinheiro = new JTextField(8);
		tTotalDinheiro.setEditable(false);
		tTotalDinheiro.setBounds(225,70,100,30);
		p1.add(tTotalDinheiro);
		
		lTotalCartaoCredito = new JLabel("Total em Cartão de Crédito");
		lTotalCartaoCredito.setBounds(5,110,160,20);
		p1.add(lTotalCartaoCredito);
		
		tTotalCartaoCredito = new JTextField(8);
		tTotalCartaoCredito.setEditable(false);
		tTotalCartaoCredito.setBounds(5,130,100,30);
		p1.add(tTotalCartaoCredito);
		
		
		lTotalCartaoDebito = new JLabel("Total em Cartão de Débito");
		lTotalCartaoDebito.setBounds(175,110,160,20);
		p1.add(lTotalCartaoDebito);
		
		tTotalCartaoDebito = new JTextField(8);
		tTotalCartaoDebito.setEditable(false);
		tTotalCartaoDebito.setBounds(175,130,100,30);
		p1.add(tTotalCartaoDebito);
		
		lTotalCheque = new JLabel("Total em Cheque");
		lTotalCheque.setBounds(5,170,100,20);
		p1.add(lTotalCheque);
		
		tTotalCheque = new JTextField(8);
		tTotalCheque.setEditable(false);
		tTotalCheque.setBounds(5,190,100,30);
		p1.add(tTotalCheque);
		
		lTotalVendido = new JLabel("Total Vendido");
		lTotalVendido.setBounds(175,170,150,20);
		p1.add(lTotalVendido);
		
		tTotalVendido = new JTextField(8);
		tTotalVendido.setEditable(false);
		tTotalVendido.setBounds(175,190,100,30);
		p1.add(tTotalVendido);
		
		// botões
		
		bRetirarCaixa = new JButton("Retirar do Caixa");
		bRetirarCaixa.addActionListener(this);
		bRetirarCaixa.setMnemonic(KeyEvent.VK_R);
		bRetirarCaixa.setBounds(5,230,150,30);
		p1.add(bRetirarCaixa);
		
		bFinalizarCaixa = new JButton("Finalizar Caixa");
		bFinalizarCaixa.addActionListener(this);
		bFinalizarCaixa.setMnemonic(KeyEvent.VK_F);
		bFinalizarCaixa.setBounds(165,230,150,30);
		p1.add(bFinalizarCaixa);
		
		bIniciarCaixa = new JButton("Iniciar Caixa");
		bIniciarCaixa.addActionListener(this);
		bIniciarCaixa.setMnemonic(KeyEvent.VK_I);
		bIniciarCaixa.setBounds(5,270,150,30);
		p1.add(bIniciarCaixa);
		
		//método para ligar o banco de dados com a aplicação
		carregaResultSet();
		carregaCampos();
		
		
		//verifica se o caixa já foi iniciado com base nos campos para setar os botões corretamente
		if(tInicioCaixa.getText().equals("0.00") && tTotalVendido.getText().equals("0.00") &&
			 tRetiradoCaixa.getText().equals("0.00"))
		{  
			setandoBotoes(false,true,false);
		}
		else
		{
			setandoBotoes(true,false,true);
		}
		
			
		
	} // fim do construtor
	
	public void actionPerformed(ActionEvent e)
	{
		
		//variaveis para receber valores de joptionpane
		String aux = "";
		double valorConversao = 0.0;
		
		//usado para receber valor de JoptionDialog
		int n;
		
		
		if(e.getSource() == iSair)
		{
			int opcao;
			Object[]botoes = {"Sim", "Não"};
			opcao = JOptionPane.showOptionDialog(null,
				"Deseja mesmo Sair?", "Fechar",
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,
				null,botoes,botoes[0]);
			if(opcao == JOptionPane.YES_OPTION)
				setVisible(false);
		} // fim if sair
		
		if(e.getSource() == bRetirarCaixa)
		{
		
			do
             {
             
	             aux = JOptionPane.showInputDialog("Digite o Valor");
	             aux = aux.replace(",",".");
	             // selecionou cancelar? se sim saia do método
	             if(aux == null)
	             {
	             	return;
	             }
	    
	             try
	             {
	             	valorConversao = Double.parseDouble(aux);
	             }
	             catch(NumberFormatException erro)
	             {
	             	
	             	// atribui-se valor negativo apenas para voltar no loop
					valorConversao = -1;	
	             }
	             
	             // verifica se o valor esta negativo por inserção ou se caiu na exceção acima e recebeu -1
	             if(valorConversao < 0) 
	             {
	             	JOptionPane.showMessageDialog(null,"Valor Inválido", "Aviso",JOptionPane.WARNING_MESSAGE);
	             }
             
             }while(valorConversao < 0);
             
            try
			{
				//retirando dinheiro do caixa
				String sql = "UPDATE caixa SET total_dinheiro = total_dinheiro - "+valorConversao+
					",retirado_caixa = retirado_caixa +"+valorConversao+" WHERE codigo = '1'";
				statement.executeUpdate(sql);
				
			}
			catch(SQLException erro)
			{
							
			}
			
			carregaCampos();

			
			
			
		} // fim if bRetirarCaixa
		
		if(e.getSource() == bFinalizarCaixa)
		{
			 n = JOptionPane.showConfirmDialog(null,"Deseja mesmo finalizar caixa?","",JOptionPane.YES_NO_OPTION);
				
			if(n == JOptionPane.YES_OPTION)
			{
				try
				{
					String sql = "UPDATE caixa SET "+
						"total_vendido = 0," + 
						"total_cartao_credito = 0,"+
						"total_cartao_debito = 0," + 
						"total_cheque = 0," +
						"total_dinheiro = 0,"+
						"retirado_caixa = 0,"+
						"inicio_caixa = 0 "+
						"WHERE codigo = 1 ";
							
					statement.executeUpdate(sql);
		
				}
				catch(SQLException erro)
				{
					
				}
				
				carregaCampos();
				setandoBotoes(false,true,false);
					
		
			} // fim if
			
			
			
		} // fim finalizar caixa
		
		if(e.getSource() == bIniciarCaixa)
		{
			do
             {
             
	             aux = JOptionPane.showInputDialog("Digite o Valor de início do caixa");
	             aux = aux.replace(",",".");
	             
	             // selecionou cancelar? se sim saia do método
	             if(aux == null)
	             {
	             	return;
	             }
	    
	             try
	             {
	             	valorConversao = Double.parseDouble(aux);
	             }
	             catch(NumberFormatException erro)
	             {
	             	
	             	// atribui-se valor negativo apenas para voltar no loop
					valorConversao = -1;	
	             }
	             
	             // verifica se o valor esta negativo por inserção ou se caiu na exceção acima e recebeu -1
	             if(valorConversao < 0) 
	             {
	             	JOptionPane.showMessageDialog(null,"Valor Inválido", "Aviso",JOptionPane.WARNING_MESSAGE);
	             }
             
             }while(valorConversao < 0);
             
            try
			{
				//Iniciando valor do caixa
				String sql = "UPDATE caixa SET inicio_caixa = "+valorConversao+
					", total_dinheiro = "+valorConversao+" WHERE codigo = '1'";
				statement.executeUpdate(sql);
				
			}
			catch(SQLException erro)
			{
							
			}
			
			carregaCampos();
			setandoBotoes(true,false,true);
			
			
		} // fim bIniciarCaixa
		
			
	
		
		
		
	} // fim ActionListener
	
		//método usado para ligar o banco de dados com a aplicação
	public void carregaResultSet()
	{
		
		String url="jdbc:mysql://127.0.0.1:3306/sistema_gerenciamento_estoque";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection minhaConexao=DriverManager.getConnection(url,"root","1234");
			statement=minhaConexao.createStatement(resultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);;
			
			
		}
		
		catch(ClassNotFoundException erro)
		{
			System.out.println("Driver nao encontrado");
		}
		catch(SQLException ero)
		{
			System.out.println("Problemas na conexao com a fonte de dados");
		}
	} // fim carregaResultSet
	
	
	public void carregaCampos()
	{	

		try
		{
			resultSet = statement.executeQuery("SELECT * FROM caixa WHERE codigo = '1'");
			resultSet.next();
	
			tInicioCaixa.setText(resultSet.getString("inicio_caixa"));
			tRetiradoCaixa.setText(resultSet.getString("retirado_caixa"));
			tTotalDinheiro.setText(resultSet.getString("total_dinheiro"));
			tTotalCartaoDebito.setText(resultSet.getString("total_cartao_debito"));
			tTotalCartaoCredito.setText(resultSet.getString("total_cartao_credito"));
			tTotalCheque.setText(resultSet.getString("total_cheque"));
			tTotalVendido.setText(resultSet.getString("total_vendido"));
	
			
		} // fim try
		catch(SQLException erro)
		{
		}
	} // fim carregaCampos()
	
	
	public void	setandoBotoes(boolean pBFinalizar, boolean pBIniciar, boolean pBRetirar)
	{
		bRetirarCaixa.setEnabled(pBRetirar);
		bFinalizarCaixa.setEnabled(pBFinalizar);
		bIniciarCaixa.setEnabled(pBIniciar);
	} // fim setandoBotoes
	

	
	
} // fim da classe