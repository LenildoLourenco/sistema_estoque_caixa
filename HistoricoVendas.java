import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.text.*;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;


public class HistoricoVendas extends JFrame implements ActionListener
{
	
	
	
	// decimal format para exibir valores monetários
	DecimalFormat df;
	
	
	//string usadas para fazer pesquisa e tratamento de data
	String dataInicio, dataFim;
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;

	// labels e textfield
	JLabel lDataInicioPesquisa, lDataFimPesquisa, lTotalVendidoUnidades, lLucro, lTotalGeralVendido, lHistoricoVendas;
	
	JTextField tTotalVendidoUnidades, tLucro, tTotalGeralVendido;
	
	// formatted
	
	JFormattedTextField ftDataInicioPesquisa,ftDataFimPesquisa;
	// mascara
	MaskFormatter mfData;
	
	// botões
	
	JButton bPesquisar, bLimpar;
		
	// menu, menuitem, menuBar
	
	JMenuBar barraMenu;
	JMenuItem iSair;
	JMenu mSair;
	
	// painel
	
	JPanel p1;
	
	HistoricoVendas()
	{
		setTitle("Histórico de Vendas");
		setSize(300,320);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		setIconImage(new ImageIcon("Imagens/historico.png").getImage());
		
		
		// painel
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		
		
		// menu para sair
		barraMenu = new JMenuBar();
		
		mSair = new JMenu("Sair");
		mSair.setMnemonic(KeyEvent.VK_S);
		
		iSair = new JMenuItem("Sair");
		iSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		iSair.addActionListener(this);
		
		mSair.add(iSair);
		barraMenu.add(mSair);
		setJMenuBar(barraMenu);
		
		// decimalFormat para exibir valores monetários
		df = new DecimalFormat();
		df.applyPattern("#,##0.00");
		
		
		// label historicoVendas
		
		lHistoricoVendas = new JLabel("Histórico de Vendas");
		lHistoricoVendas.setBounds(10,10,130,20);
		p1.add(lHistoricoVendas);
		
	
		// labels e textField
		
		//data inicio pesquisa
		lDataInicioPesquisa = new JLabel("Data Início Pesquisa");
		lDataInicioPesquisa.setBounds(10,40,120,20);
		p1.add(lDataInicioPesquisa);
		
		try
		{
			mfData = new MaskFormatter("##/##/####");
			mfData.setPlaceholderCharacter('_');
		}
		catch(ParseException excp)
		{
		}
		
		ftDataInicioPesquisa = new JFormattedTextField(mfData);
		ftDataInicioPesquisa.setBounds(10,60,70,30);
		p1.add(ftDataInicioPesquisa);
		
		//data fim pesquisa
		
		lDataFimPesquisa = new JLabel("Data Fim Pesquisa");
		lDataFimPesquisa.setBounds(145,40,120,20);
		p1.add(lDataFimPesquisa);
		
		ftDataFimPesquisa = new JFormattedTextField(mfData);
		ftDataFimPesquisa.setBounds(145,60,70,30);	
		ftDataFimPesquisa.addActionListener(this);
		p1.add(ftDataFimPesquisa);
		
		// total vendido em unidades
		
		lTotalVendidoUnidades = new JLabel("Total Vendido em Unidades");
		lTotalVendidoUnidades.setBounds(10,100,160,20);
		p1.add(lTotalVendidoUnidades);
		
		tTotalVendidoUnidades = new JTextField();
		tTotalVendidoUnidades.setEditable(false);
		tTotalVendidoUnidades.setBounds(10,120,80,30);
		p1.add(tTotalVendidoUnidades);
		
		// total lucro
		
		lLucro = new JLabel("Total Lucro");
		lLucro.setBounds(180,100,100,20);
		p1.add(lLucro);
		
		tLucro = new JTextField();
		tLucro.setEditable(false);
		tLucro.setBounds(180,120,80,30);
		p1.add(tLucro);
		
		// total geral vendido
		
		lTotalGeralVendido = new JLabel("Total Geral Vendido");
		lTotalGeralVendido.setBounds(10,160,150,20);
		p1.add(lTotalGeralVendido);
		
		tTotalGeralVendido = new JTextField();
		tTotalGeralVendido.setEditable(false);
		tTotalGeralVendido.setBounds(10,180,80,30);
		p1.add(tTotalGeralVendido);
		
		// botoes
		
		bPesquisar = new JButton("Pesquisar", new ImageIcon("Imagens/pesquisar.png"));
		bPesquisar.addActionListener(this);
		bPesquisar.setMnemonic(KeyEvent.VK_P);
		bPesquisar.setBounds(10,220,120,30);
		p1.add(bPesquisar);
		
		bLimpar = new JButton("Limpar", new ImageIcon("Imagens/limpar.png"));
		bLimpar.addActionListener(this);
		bLimpar.setMnemonic(KeyEvent.VK_L);
		bLimpar.setBounds(150,220,120,30);
		p1.add(bLimpar);
		
		
		carregaResultSet();
		
		
		
		
		
		
		
	} // fim construtor
	
	
	public void actionPerformed(ActionEvent e)
	{
		
		// sair
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
		
		if(e.getSource() == bPesquisar || e.getSource() == ftDataFimPesquisa)
		{
			
			//fazendo tratamento das string para retirar / e inverter o formato de data
			// para ficar igual ao do banco
			dataInicio = ftDataInicioPesquisa.getText();
			dataInicio = dataInicio.replace("/","");
			
			dataFim = ftDataFimPesquisa.getText();
			dataFim = dataFim.replace("/","");
			
			
			
			String diaInicio = dataInicio.substring(0,2);
			String mesInicio = dataInicio.substring(2,4);
			String anoInicio = dataInicio.substring(4);
			dataInicio = anoInicio+mesInicio+diaInicio;
			
			String diaFim = dataFim.substring(0,2);
			String mesFim = dataFim.substring(2,4);
			String anoFim = dataFim.substring(4);
			dataFim = anoFim+mesFim+diaFim;

			
			carregaFiltroDados();
			
		} // fim if bPesquisar
		
		if(e.getSource() == bLimpar)
		{
			ftDataInicioPesquisa.setValue(null);
			ftDataFimPesquisa.setValue(null);
			tTotalGeralVendido.setText("");
			tTotalVendidoUnidades.setText("");
			tLucro.setText("");
		} // fim if bLimpar
		
		
	} // fim actionListener
	
	public void carregaFiltroDados()
	{
		int numeroFiltro = 0;
		
		double totalVendidoUnidade = 0.0, totalLucro = 0.0, totalGeralVendido = 0.0;
		
		
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM vendas WHERE data_venda >="+
				dataInicio + " AND data_venda <= "+dataFim);
			//verifica a quantidade de cadastros no banco de acordo com o filtro
			while(resultSet.next())
			{
				numeroFiltro = resultSet.getInt(1);
			}
			

			
			// for para carregar as variáveis
			for(int i =0; i < numeroFiltro; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM vendas WHERE data_venda >= "+
				dataInicio +" AND data_venda <=" +dataFim+" LIMIT "+i+",1");
				resultSet.next();
				
					
				totalVendidoUnidade += resultSet.getFloat("total_unidades");
				totalLucro += resultSet.getFloat("total_lucro");
				totalGeralVendido +=resultSet.getFloat("total_vendido");
				
			} // fim for

		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Nenhum dado encontrado na pesquisa","Aviso",JOptionPane.PLAIN_MESSAGE);
		}
		
		
		//setando valores
		tTotalGeralVendido.setText("R$"+df.format(totalGeralVendido));
		tLucro.setText("R$"+df.format(totalLucro));
		tTotalVendidoUnidades.setText(df.format(totalVendidoUnidade));
		
	
	
	} // fim do método carrega filtro de dados
	
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
	
	
} // fim da classe