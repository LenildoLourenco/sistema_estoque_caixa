import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.sql.*;

public class AnaliticoVendas extends JFrame implements ActionListener
{
	

	// decimal format para exibir valores monetários
	DecimalFormat df;
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	//data usada para consultar no banco com base na data atual
	Date agora;
	Date calculaAgora;
	DateFormat formataAgora;
	
	Calendar data;
	
	


	// labels e textfield
	JLabel lAnaliticoVendas;
	JLabel lQuantidadeVendidaMesAtual, lUnidadeVendidaMesAtual, lLucroMesAtual, 
		lVariacaoMesPassado, lQuantidadeVendidaMesPassado, lUnidadeVendidaMesPassado, lLucroMesPassado,
		lVariacaoMesRetrasado, lQuantidadeVendidaMesRetrasado, lUnidadeVendidaMesRetrasado, lLucroMesRetrasado,
		lVariacaoAnoPassado, lQuantidadeVendidaAnoPassado, lUnidadeVendidaAnoPassado, lLucroAnoPassado;
		
	JTextField tQuantidadeVendidaMesAtual, tUnidadeVendidaMesAtual, tLucroMesAtual, 
		 tQuantidadeVendidaMesPassado, tUnidadeVendidaMesPassado, tLucroMesPassado,
		 tQuantidadeVendidaMesRetrasado, tUnidadeVendidaMesRetrasado, tLucroMesRetrasado,
		 tQuantidadeVendidaAnoPassado, tUnidadeVendidaAnoPassado, tLucroAnoPassado;
	
	
		
	// menu, menuitem, menuBar
	
	JMenuBar barraMenu;
	JMenuItem iSair;
	JMenu mSair;
	
	// painel
	
	JPanel p1;
	
	AnaliticoVendas()
	{
		setTitle("Analítico de Vendas");
		setSize(820,540);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		
		setIconImage(new ImageIcon("Imagens/analitico.png").getImage());
		
		// painel
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		
		//String dará ao objeto simpleDateFormat o formato de data que o mysql aceita
		String f1 = "yyyyMMdd";
		formataAgora = new SimpleDateFormat(f1);
	
		
		
		//usa-se duas instâncias de Date e uma de Calendar, para fazer o calculo de data
		// por exemplo data de hoje menos 30 dias ou 1 ano
		
		//instanciando agora
		agora = new Date();
		
		//instanciando data, instância de Calendar
		data = Calendar.getInstance();

		// atribuindo a data de agora ao objeto data
		data.setTime(agora);
		

		
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
		
		df = new DecimalFormat();
		df.applyPattern("#,##0.00");
		
		
		// label AnaliticoVendas
		
		lAnaliticoVendas = new JLabel("Analítico de Vendas");
		lAnaliticoVendas.setFont(new Font("Arial",Font.BOLD,16));
		lAnaliticoVendas.setBounds(10,10,200,20);
		p1.add(lAnaliticoVendas);
		
		
		// labels e textField
		
		// quantidade vendida mês atual
		lQuantidadeVendidaMesAtual = new JLabel("Quantidade Vendida Mês Atual em R$");
		lQuantidadeVendidaMesAtual.setBounds(10,40,220,20);
		p1.add(lQuantidadeVendidaMesAtual);
		
		tQuantidadeVendidaMesAtual = new JTextField();
		tQuantidadeVendidaMesAtual.setEditable(false);
		tQuantidadeVendidaMesAtual.setBounds(10,60,100,30);
		p1.add(tQuantidadeVendidaMesAtual);
		
		// unidade vendida mês atual
		
		lUnidadeVendidaMesAtual = new JLabel("Unidades Vendidas Mês Atual"); 
		lUnidadeVendidaMesAtual.setBounds(250,40,180,20);
		p1.add(lUnidadeVendidaMesAtual);
		
		tUnidadeVendidaMesAtual = new JTextField();
		tUnidadeVendidaMesAtual.setEditable(false);
		tUnidadeVendidaMesAtual.setBounds(250,60,100,30);
		p1.add(tUnidadeVendidaMesAtual);
		
		// lucro mês atual
		
		lLucroMesAtual = new JLabel("Lucro Mês Atual");
		lLucroMesAtual.setBounds(440,40,100,20);
		p1.add(lLucroMesAtual);
		
		tLucroMesAtual = new JTextField();
		tLucroMesAtual.setEditable(false);
		tLucroMesAtual.setBounds(440,60,100,30);
		p1.add(tLucroMesAtual);
		
		
		// variação em relação ao mesmo período do mês passado
		
		lVariacaoMesPassado = new JLabel("Variação em Relação ao Mesmo Período do Mês Passado");
		lVariacaoMesPassado.setBounds(10,130,350,20);
		p1.add(lVariacaoMesPassado);
		
		// variação quantidade vendida
		lQuantidadeVendidaMesPassado = new JLabel("Quantidade Vendida em R$");
		lQuantidadeVendidaMesPassado.setBounds(10,155,170,20);
		p1.add(lQuantidadeVendidaMesPassado);
		
		tQuantidadeVendidaMesPassado = new JTextField();
		tQuantidadeVendidaMesPassado.setEditable(false);
		tQuantidadeVendidaMesPassado.setBounds(10,175,100,30);
		p1.add(tQuantidadeVendidaMesPassado);
		
		// variação unidades vendidas
		
		lUnidadeVendidaMesPassado = new JLabel("Unidades Vendidas");
		lUnidadeVendidaMesPassado.setBounds(190,155,120,20);
		p1.add(lUnidadeVendidaMesPassado);
		
		tUnidadeVendidaMesPassado = new JTextField();
		tUnidadeVendidaMesPassado.setEditable(false);
		tUnidadeVendidaMesPassado.setBounds(190,175,100,30);
		p1.add(tUnidadeVendidaMesPassado);
		
		// variação lucro
		
		lLucroMesPassado = new JLabel("Lucro");
		lLucroMesPassado.setBounds(320,155,100,20);
		p1.add(lLucroMesPassado);
		
		tLucroMesPassado = new JTextField();
		tLucroMesPassado.setEditable(false);
		tLucroMesPassado.setBounds(320,175,100,30);
		p1.add(tLucroMesPassado);
		
		
		// variação em relação ao mesmo período do mês retrasado
		
		lVariacaoMesRetrasado = new JLabel("Variação em Relação ao Mesmo Período do Mês Retrasado");
		lVariacaoMesRetrasado.setBounds(10,245,350,20);
		p1.add(lVariacaoMesRetrasado);
		
		// variação quantidade vendida
		lQuantidadeVendidaMesRetrasado = new JLabel("Quantidade Vendida em R$");
		lQuantidadeVendidaMesRetrasado.setBounds(10,270,170,20);
		p1.add(lQuantidadeVendidaMesRetrasado);
		
		tQuantidadeVendidaMesRetrasado = new JTextField();
		tQuantidadeVendidaMesRetrasado.setEditable(false);
		tQuantidadeVendidaMesRetrasado.setBounds(10,290,100,30);
		p1.add(tQuantidadeVendidaMesRetrasado);
		
		// variação unidades vendidas
		
		lUnidadeVendidaMesRetrasado = new JLabel("Unidades Vendidas");
		lUnidadeVendidaMesRetrasado.setBounds(190,270,120,20);
		p1.add(lUnidadeVendidaMesRetrasado);
		
		tUnidadeVendidaMesRetrasado = new JTextField();
		tUnidadeVendidaMesRetrasado.setEditable(false);
		tUnidadeVendidaMesRetrasado.setBounds(190,290,100,30);
		p1.add(tUnidadeVendidaMesRetrasado);
		
		// variação lucro
		
		lLucroMesRetrasado = new JLabel("Lucro");
		lLucroMesRetrasado.setBounds(320,270,100,20);
		p1.add(lLucroMesRetrasado);
		
		tLucroMesRetrasado = new JTextField();
		tLucroMesRetrasado.setEditable(false);
		tLucroMesRetrasado.setBounds(320,290,100,30);
		p1.add(tLucroMesRetrasado);
		
		
		// variação em relação ao mesmo período do ano passado
		
		lVariacaoAnoPassado = new JLabel("Variação em Relação ao Mesmo Período do Ano Passado");
		lVariacaoAnoPassado.setBounds(10,360,350,20);
		p1.add(lVariacaoAnoPassado);
		
		// variação quantidade vendida
		lQuantidadeVendidaAnoPassado = new JLabel("Quantidade Vendida em R$");
		lQuantidadeVendidaAnoPassado.setBounds(10,385,170,20);
		p1.add(lQuantidadeVendidaAnoPassado);
		
		tQuantidadeVendidaAnoPassado = new JTextField();
		tQuantidadeVendidaAnoPassado.setEditable(false);
		tQuantidadeVendidaAnoPassado.setBounds(10,405,100,30);
		p1.add(tQuantidadeVendidaAnoPassado);
		
		// variação unidades vendidas
		
		lUnidadeVendidaAnoPassado = new JLabel("Unidades Vendidas");
		lUnidadeVendidaAnoPassado.setBounds(190,385,120,20);
		p1.add(lUnidadeVendidaAnoPassado);
		
		tUnidadeVendidaAnoPassado = new JTextField();
		tUnidadeVendidaAnoPassado.setEditable(false);
		tUnidadeVendidaAnoPassado.setBounds(190,405,100,30);
		p1.add(tUnidadeVendidaAnoPassado);
		
		// variação lucro
		
		lLucroAnoPassado = new JLabel("Lucro");
		lLucroAnoPassado.setBounds(320,385,100,20);
		p1.add(lLucroAnoPassado);
		
		tLucroAnoPassado = new JTextField();
		tLucroAnoPassado.setEditable(false);
		tLucroAnoPassado.setBounds(320,405,100,30);
		p1.add(tLucroAnoPassado);
		
	
		carregaResultSet();
		
		carregarDados();
	
		
		
		
		
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
		
	
		
	} // fim actionListener
	
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
	
	
	
	
	
	public void carregarDados()
	{
		/* calculando data
		 *calcula-se o período decorrido do início do mês até agora
		 *calcula-se o mesmo período em relação ao mês passado
		 *calcula-se o mesmo período em relação ao mês retrasado
		 *calcula-se o mesmo período em relação ao ano passado
		 *Ex: hoje é dia 17, o calculo dos outros meses e ano é feito em cima de 17 dias também
		 */
		 
		 
		 //CALCULANDO O HISTÓRICO DE VENDAS DO COMEÇO DO MÊS ATÉ AGORA
		 
		 //o objeto simpleDateFormat 'formataAgora' serve para deixar o formato de dado padrão mysql
		 
		 //pegando a data atual que será a dataFinal da pesquisa deste mês
		 String dataFinal = formataAgora.format(agora);
		 
		 //pegando o dia do mês para calcular
		 int diaMes = data.get(Calendar.DAY_OF_MONTH);
		 
		  //calculando a data desejada, neste caso dia primeiro, e adicionando ao Calendar 'data'
		  //Ex: hoje é dia 18, dia primeiro seria (diaAtual) - (diaAtual - 1)
	       data.add(Calendar.DATE, - (diaMes - 1));
		
		 //instanciando outro objeto de Date, baseado no calculo do objeto Calendar data
		 // calculaAgora contém a data calculada para uso
		 calculaAgora = data.getTime();
		
		
		 //contém a data do dia primeiro deste mês
		 String dataInicial = formataAgora.format(calculaAgora);
		 
		 
		 // variáveis para guardar valores do período atual
		 double totalVendidoUnidadeAtual = 0.0;
		 double totalLucroAtual = 0.0;
		 double totalGeralVendidoAtual = 0.0;
		 
		 
		 int numero = 0;
		 
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM vendas WHERE data_venda >="+
				dataInicial + " AND data_venda <= "+dataFinal);
			//verifica a quantidade de cadastros no banco de acordo com o filtro
			while(resultSet.next())
			{
				numero = resultSet.getInt(1);
			}
			

			
			// for para carregar as variáveis
			for(int i =0; i < numero; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM vendas WHERE data_venda >= "+
				dataInicial +" AND data_venda <=" +dataFinal+" LIMIT "+i+",1");
				resultSet.next();
				
					
				totalVendidoUnidadeAtual += resultSet.getFloat("total_unidades");
				totalLucroAtual += resultSet.getFloat("total_lucro");
				totalGeralVendidoAtual +=resultSet.getFloat("total_vendido");
				
			} // fim for

		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Nenhum dado encontrado na pesquisa","Aviso",JOptionPane.PLAIN_MESSAGE);
		}
		
		
		//setando valores
		tQuantidadeVendidaMesAtual.setText("R$"+df.format(totalGeralVendidoAtual));
		tLucroMesAtual.setText("R$"+df.format(totalLucroAtual));
		tUnidadeVendidaMesAtual.setText(""+df.format(totalVendidoUnidadeAtual));

	
	
	
		
		//comparação em relação ao mês passado
		//<--------------------------------------------------------------->
		/* Formula da comparação 
		*variação = valor a comparar / base de comparação * 100 - 100
		*EX = Valor deste mês / valor mês passado * 100 - 100 = Porcentagem de aumento ou queda
		*/
		
		//Variaveis para armazenar valores de comparação
		double totalUnidadeComparacao = 0.0, totalVendidoComparacao = 0.0, totalLucroComparacao = 0.0;

		 // Zerando o objeto data para fazer novos calculos
	     data.setTime(agora);
		
		
		//subtraindo 1 mês e adicionando ao Calendar data
	    data.add(Calendar.MONTH, -1);
	    //adicionando de Calendar data para Date calculaAgora
	    calculaAgora = data.getTime();	
	    
	    //formatando a data final da pesquisa do mês passado	
		dataFinal = formataAgora.format(calculaAgora);
		
		
		//Calculando dia primeiro do mês passado
		data.add(Calendar.DATE, - (diaMes - 1));
		calculaAgora = data.getTime();
		dataInicial = formataAgora.format(calculaAgora);
		

		 
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM vendas WHERE data_venda >="+
				dataInicial + " AND data_venda <= "+dataFinal);
			//verifica a quantidade de cadastros no banco de acordo com o filtro
			while(resultSet.next())
			{
				numero = resultSet.getInt(1);
			}
			

			
			// for para carregar as variáveis
			for(int i =0; i < numero; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM vendas WHERE data_venda >= "+
				dataInicial +" AND data_venda <=" +dataFinal+" LIMIT "+i+",1");
				resultSet.next();
				
					
				totalUnidadeComparacao += resultSet.getFloat("total_unidades");
				totalLucroComparacao += resultSet.getFloat("total_lucro");
				totalVendidoComparacao +=resultSet.getFloat("total_vendido");
				
			} // fim for

		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Nenhum dado encontrado na pesquisa","Aviso",JOptionPane.PLAIN_MESSAGE);
		}
		
		//calculando valores

		//previnindo divisões por 0
		if(totalUnidadeComparacao != 0 && totalVendidoComparacao != 0 && totalLucroComparacao != 0)
		{
			totalUnidadeComparacao = totalVendidoUnidadeAtual / totalUnidadeComparacao * 100 - 100;
			totalVendidoComparacao = totalGeralVendidoAtual / totalVendidoComparacao * 100 - 100;
			totalLucroComparacao = totalLucroAtual / totalLucroComparacao * 100 - 100;
			
		}
		else
		{
			tUnidadeVendidaMesPassado.setText("Sem dados");
			tQuantidadeVendidaMesPassado.setText("Sem dados");
			tLucroMesPassado.setText("Sem dados");
		
		}
		

		//setando valores e cor nos textfields
		
		//totalUnidade
		if(totalUnidadeComparacao < 0  )
		{
			tUnidadeVendidaMesPassado.setBackground(new Color(245,105,105));
			tUnidadeVendidaMesPassado.setText(df.format(totalUnidadeComparacao)+"%");
			
		}
		else if (totalUnidadeComparacao > 0 )
		{
			tUnidadeVendidaMesPassado.setBackground(new Color(100,247,51));	
			tUnidadeVendidaMesPassado.setText("+"+df.format(totalUnidadeComparacao)+"%");
		}
		else
		{
			tUnidadeVendidaMesPassado.setBackground(new Color(238,238,238));
		}
		
		
		
		//total vendido
		if(totalVendidoComparacao < 0  )
		{
			tQuantidadeVendidaMesPassado.setBackground(new Color(245,105,105));	
			tQuantidadeVendidaMesPassado.setText(df.format(totalVendidoComparacao)+"%");	
		}
		else if (totalVendidoComparacao > 0 )
		{
			tQuantidadeVendidaMesPassado.setBackground(new Color(100,247,51));	
				tQuantidadeVendidaMesPassado.setText("+"+df.format(totalVendidoComparacao)+"%");
		}
		else
		{
			tQuantidadeVendidaMesPassado.setBackground(new Color(238,238,238));
		}
		
		
		//total Lucro
		if(totalLucroComparacao < 0  )
		{
			tLucroMesPassado.setBackground(new Color(245,105,105));
			tLucroMesPassado.setText(df.format(totalLucroComparacao)+"%");
			
		}
		else if (totalLucroComparacao > 0 )
		{
				tLucroMesPassado.setText("+"+df.format(totalLucroComparacao)+"%");
			tLucroMesPassado.setBackground(new Color(100,247,51));
	
		}
		else
		{
			tLucroMesPassado.setBackground(new Color(238,238,238));

		}
		

	
		//comparação em relação ao Mês retrasado
		//<----------------------------------------------->
		//zerando variáveis
		totalUnidadeComparacao = 0.0;
		totalVendidoComparacao = 0.0;
		totalLucroComparacao = 0.0;

	
		 // Zerando o objeto data para fazer novos calculos
		data.setTime(agora);
		
		//subtraindo 2 meses
	    data.add(Calendar.MONTH, -2);
	    calculaAgora = data.getTime();	
		dataFinal = formataAgora.format(calculaAgora);
		
		
		//calculando dia primeiro mês retrasado
		data.add(Calendar.DATE, - (diaMes - 1));

		calculaAgora = data.getTime();
		
		dataInicial = formataAgora.format(calculaAgora);
		
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM vendas WHERE data_venda >="+
				dataInicial + " AND data_venda <= "+dataFinal);
			//verifica a quantidade de cadastros no banco de acordo com o filtro
			while(resultSet.next())
			{
				numero = resultSet.getInt(1);
			}
			

			
			// for para carregar as variáveis
			for(int i =0; i < numero; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM vendas WHERE data_venda >= "+
				dataInicial +" AND data_venda <=" +dataFinal+" LIMIT "+i+",1");
				resultSet.next();
				
					
				totalUnidadeComparacao += resultSet.getFloat("total_unidades");
				totalLucroComparacao += resultSet.getFloat("total_lucro");
				totalVendidoComparacao +=resultSet.getFloat("total_vendido");
				
			} // fim for

		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Nenhum dado encontrado na pesquisa","Aviso",JOptionPane.PLAIN_MESSAGE);
		}
		
		//calculando valores
	
		//previnindo divisões por 0
		if(totalUnidadeComparacao != 0 && totalVendidoComparacao != 0 && totalLucroComparacao != 0)
		{
			totalUnidadeComparacao = totalVendidoUnidadeAtual / totalUnidadeComparacao * 100 - 100;
			totalVendidoComparacao = totalGeralVendidoAtual / totalVendidoComparacao * 100 - 100;
			totalLucroComparacao = totalLucroAtual / totalLucroComparacao * 100 - 100;
			
		}
		else
		{
			tUnidadeVendidaMesRetrasado.setText("Sem dados");
			tQuantidadeVendidaMesRetrasado.setText("Sem dados");
			tLucroMesRetrasado.setText("Sem dados");
			
		}
		
		
		
		
		//setando valores e cor nos textfields
		//totalUnidade

		if(totalUnidadeComparacao < 0  )
		{
			tUnidadeVendidaMesRetrasado.setBackground(new Color(245,105,105));
			tUnidadeVendidaMesRetrasado.setText(df.format(totalUnidadeComparacao)+"%");

		}
		else if (totalUnidadeComparacao > 0 )
		{
			tUnidadeVendidaMesRetrasado.setText("+"+df.format(totalUnidadeComparacao)+"%");
			tUnidadeVendidaMesRetrasado.setBackground(new Color(100,247,51));
		}
		else
		{
			tUnidadeVendidaMesRetrasado.setBackground(new Color(238,238,238));
		}
		
		
		
		//total vendido
		if(totalVendidoComparacao < 0  )
		{
			tQuantidadeVendidaMesRetrasado.setBackground(new Color(245,105,105));
			tQuantidadeVendidaMesRetrasado.setText(df.format(totalVendidoComparacao)+"%");

		}
		else if (totalVendidoComparacao > 0 )
		{
			tQuantidadeVendidaMesRetrasado.setText("+"+df.format(totalVendidoComparacao)+"%");
			tQuantidadeVendidaMesRetrasado.setBackground(new Color(100,247,51));
		}
		else
		{
			tQuantidadeVendidaMesRetrasado.setBackground(new Color(238,238,238));
		}
		
		
		//total Lucro
		if(totalLucroComparacao < 0  )
		{
			tLucroMesRetrasado.setBackground(new Color(245,105,105));
			tLucroMesRetrasado.setText(df.format(totalLucroComparacao)+"%");
		}
		else if (totalLucroComparacao > 0 )
		{
			tLucroMesRetrasado.setText("+"+df.format(totalLucroComparacao)+"%");
			tLucroMesRetrasado.setBackground(new Color(100,247,51));	
		}
		else
		{
			tLucroMesRetrasado.setBackground(new Color(238,238,238));
		}
	




		//comparação em relação ao ano passado
		//<---------------------------------------------------->
		//zerando variáveis
		totalUnidadeComparacao = 0.0;
		totalVendidoComparacao = 0.0;
		totalLucroComparacao = 0.0;

		// Zerando o objeto data para fazer novos calculos
		data.setTime(agora);
		
		//subtraindo 1 ano
	    data.add(Calendar.YEAR, -1);
	    calculaAgora = data.getTime();	
		dataFinal = formataAgora.format(calculaAgora);
		
		
		// calculando dia primeiro do mesmo mês do ano passado
		data.add(Calendar.DATE, - (diaMes - 1));
		calculaAgora = data.getTime();
		dataInicial = formataAgora.format(calculaAgora);
		
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM vendas WHERE data_venda >="+
				dataInicial + " AND data_venda <= "+dataFinal);
			//verifica a quantidade de cadastros no banco de acordo com o filtro
			while(resultSet.next())
			{
				numero = resultSet.getInt(1);
			}
			

			
			// for para carregar as variáveis
			for(int i =0; i < numero; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM vendas WHERE data_venda >= "+
				dataInicial +" AND data_venda <=" +dataFinal+" LIMIT "+i+",1");
				resultSet.next();
				
					
				totalUnidadeComparacao += resultSet.getFloat("total_unidades");
				totalLucroComparacao += resultSet.getFloat("total_lucro");
				totalVendidoComparacao +=resultSet.getFloat("total_vendido");
				
			} // fim for

		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Nenhum dado encontrado na pesquisa","Aviso",JOptionPane.PLAIN_MESSAGE);
		}
		
		//calculando valores
		
		//previnindo divisões por 0
		if(totalUnidadeComparacao != 0 && totalVendidoComparacao != 0 && totalLucroComparacao != 0)
		{
			totalUnidadeComparacao = totalVendidoUnidadeAtual / totalUnidadeComparacao * 100 - 100;
			totalVendidoComparacao = totalGeralVendidoAtual / totalVendidoComparacao * 100 - 100;
			totalLucroComparacao = totalLucroAtual / totalLucroComparacao * 100 - 100;
			
		}
		else
		{
			tUnidadeVendidaAnoPassado.setText("Sem dados");
			tQuantidadeVendidaAnoPassado.setText("Sem dados");
			tLucroAnoPassado.setText("Sem dados");

			
		}
	
		
	

	
		//setando valores e cor nos textfields
		//totalUnidade

		if(totalUnidadeComparacao < 0  )
		{
			tUnidadeVendidaAnoPassado.setBackground(new Color(245,105,105));
			tUnidadeVendidaAnoPassado.setText(df.format(totalUnidadeComparacao)+"%");
		}
		else if (totalUnidadeComparacao > 0 )
		{
			tUnidadeVendidaAnoPassado.setText("+"+df.format(totalUnidadeComparacao)+"%");
			tUnidadeVendidaAnoPassado.setBackground(new Color(100,247,51));	
		}
		else
		{
			tUnidadeVendidaAnoPassado.setBackground(new Color(238,238,238));
		}
		
		
		
		//total vendido
		if(totalVendidoComparacao < 0  )
		{
			tQuantidadeVendidaAnoPassado.setBackground(new Color(245,105,105));
			tQuantidadeVendidaAnoPassado.setText(df.format(totalVendidoComparacao)+"%");
		}
		else if (totalVendidoComparacao > 0 )
		{
			tQuantidadeVendidaAnoPassado.setText("+"+df.format(totalVendidoComparacao)+"%");
			tQuantidadeVendidaAnoPassado.setBackground(new Color(100,247,51));	
		}
		else
		{
			tQuantidadeVendidaAnoPassado.setBackground(new Color(238,238,238));
		}
		
		
		//total Lucro
		if(totalLucroComparacao < 0  )
		{
			tLucroAnoPassado.setBackground(new Color(245,105,105));
			tLucroAnoPassado.setText(df.format(totalLucroComparacao)+"%");
		}
		else if (totalLucroComparacao > 0 )
		{
			tLucroAnoPassado.setText("+"+df.format(totalLucroComparacao)+"%");
			tLucroAnoPassado.setBackground(new Color(100,247,51));
		}
		else
		{
			tLucroAnoPassado.setBackground(new Color(238,238,238));
		}

		
	} // fim do método carregarDados
	
	
} // fim da classe