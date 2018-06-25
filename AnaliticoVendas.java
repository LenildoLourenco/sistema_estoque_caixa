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
	

	// decimal format para exibir valores monet�rios
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
		setTitle("Anal�tico de Vendas");
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
		
		
		//String dar� ao objeto simpleDateFormat o formato de data que o mysql aceita
		String f1 = "yyyyMMdd";
		formataAgora = new SimpleDateFormat(f1);
	
		
		
		//usa-se duas inst�ncias de Date e uma de Calendar, para fazer o calculo de data
		// por exemplo data de hoje menos 30 dias ou 1 ano
		
		//instanciando agora
		agora = new Date();
		
		//instanciando data, inst�ncia de Calendar
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
		
		lAnaliticoVendas = new JLabel("Anal�tico de Vendas");
		lAnaliticoVendas.setFont(new Font("Arial",Font.BOLD,16));
		lAnaliticoVendas.setBounds(10,10,200,20);
		p1.add(lAnaliticoVendas);
		
		
		// labels e textField
		
		// quantidade vendida m�s atual
		lQuantidadeVendidaMesAtual = new JLabel("Quantidade Vendida M�s Atual em R$");
		lQuantidadeVendidaMesAtual.setBounds(10,40,220,20);
		p1.add(lQuantidadeVendidaMesAtual);
		
		tQuantidadeVendidaMesAtual = new JTextField();
		tQuantidadeVendidaMesAtual.setEditable(false);
		tQuantidadeVendidaMesAtual.setBounds(10,60,100,30);
		p1.add(tQuantidadeVendidaMesAtual);
		
		// unidade vendida m�s atual
		
		lUnidadeVendidaMesAtual = new JLabel("Unidades Vendidas M�s Atual"); 
		lUnidadeVendidaMesAtual.setBounds(250,40,180,20);
		p1.add(lUnidadeVendidaMesAtual);
		
		tUnidadeVendidaMesAtual = new JTextField();
		tUnidadeVendidaMesAtual.setEditable(false);
		tUnidadeVendidaMesAtual.setBounds(250,60,100,30);
		p1.add(tUnidadeVendidaMesAtual);
		
		// lucro m�s atual
		
		lLucroMesAtual = new JLabel("Lucro M�s Atual");
		lLucroMesAtual.setBounds(440,40,100,20);
		p1.add(lLucroMesAtual);
		
		tLucroMesAtual = new JTextField();
		tLucroMesAtual.setEditable(false);
		tLucroMesAtual.setBounds(440,60,100,30);
		p1.add(tLucroMesAtual);
		
		
		// varia��o em rela��o ao mesmo per�odo do m�s passado
		
		lVariacaoMesPassado = new JLabel("Varia��o em Rela��o ao Mesmo Per�odo do M�s Passado");
		lVariacaoMesPassado.setBounds(10,130,350,20);
		p1.add(lVariacaoMesPassado);
		
		// varia��o quantidade vendida
		lQuantidadeVendidaMesPassado = new JLabel("Quantidade Vendida em R$");
		lQuantidadeVendidaMesPassado.setBounds(10,155,170,20);
		p1.add(lQuantidadeVendidaMesPassado);
		
		tQuantidadeVendidaMesPassado = new JTextField();
		tQuantidadeVendidaMesPassado.setEditable(false);
		tQuantidadeVendidaMesPassado.setBounds(10,175,100,30);
		p1.add(tQuantidadeVendidaMesPassado);
		
		// varia��o unidades vendidas
		
		lUnidadeVendidaMesPassado = new JLabel("Unidades Vendidas");
		lUnidadeVendidaMesPassado.setBounds(190,155,120,20);
		p1.add(lUnidadeVendidaMesPassado);
		
		tUnidadeVendidaMesPassado = new JTextField();
		tUnidadeVendidaMesPassado.setEditable(false);
		tUnidadeVendidaMesPassado.setBounds(190,175,100,30);
		p1.add(tUnidadeVendidaMesPassado);
		
		// varia��o lucro
		
		lLucroMesPassado = new JLabel("Lucro");
		lLucroMesPassado.setBounds(320,155,100,20);
		p1.add(lLucroMesPassado);
		
		tLucroMesPassado = new JTextField();
		tLucroMesPassado.setEditable(false);
		tLucroMesPassado.setBounds(320,175,100,30);
		p1.add(tLucroMesPassado);
		
		
		// varia��o em rela��o ao mesmo per�odo do m�s retrasado
		
		lVariacaoMesRetrasado = new JLabel("Varia��o em Rela��o ao Mesmo Per�odo do M�s Retrasado");
		lVariacaoMesRetrasado.setBounds(10,245,350,20);
		p1.add(lVariacaoMesRetrasado);
		
		// varia��o quantidade vendida
		lQuantidadeVendidaMesRetrasado = new JLabel("Quantidade Vendida em R$");
		lQuantidadeVendidaMesRetrasado.setBounds(10,270,170,20);
		p1.add(lQuantidadeVendidaMesRetrasado);
		
		tQuantidadeVendidaMesRetrasado = new JTextField();
		tQuantidadeVendidaMesRetrasado.setEditable(false);
		tQuantidadeVendidaMesRetrasado.setBounds(10,290,100,30);
		p1.add(tQuantidadeVendidaMesRetrasado);
		
		// varia��o unidades vendidas
		
		lUnidadeVendidaMesRetrasado = new JLabel("Unidades Vendidas");
		lUnidadeVendidaMesRetrasado.setBounds(190,270,120,20);
		p1.add(lUnidadeVendidaMesRetrasado);
		
		tUnidadeVendidaMesRetrasado = new JTextField();
		tUnidadeVendidaMesRetrasado.setEditable(false);
		tUnidadeVendidaMesRetrasado.setBounds(190,290,100,30);
		p1.add(tUnidadeVendidaMesRetrasado);
		
		// varia��o lucro
		
		lLucroMesRetrasado = new JLabel("Lucro");
		lLucroMesRetrasado.setBounds(320,270,100,20);
		p1.add(lLucroMesRetrasado);
		
		tLucroMesRetrasado = new JTextField();
		tLucroMesRetrasado.setEditable(false);
		tLucroMesRetrasado.setBounds(320,290,100,30);
		p1.add(tLucroMesRetrasado);
		
		
		// varia��o em rela��o ao mesmo per�odo do ano passado
		
		lVariacaoAnoPassado = new JLabel("Varia��o em Rela��o ao Mesmo Per�odo do Ano Passado");
		lVariacaoAnoPassado.setBounds(10,360,350,20);
		p1.add(lVariacaoAnoPassado);
		
		// varia��o quantidade vendida
		lQuantidadeVendidaAnoPassado = new JLabel("Quantidade Vendida em R$");
		lQuantidadeVendidaAnoPassado.setBounds(10,385,170,20);
		p1.add(lQuantidadeVendidaAnoPassado);
		
		tQuantidadeVendidaAnoPassado = new JTextField();
		tQuantidadeVendidaAnoPassado.setEditable(false);
		tQuantidadeVendidaAnoPassado.setBounds(10,405,100,30);
		p1.add(tQuantidadeVendidaAnoPassado);
		
		// varia��o unidades vendidas
		
		lUnidadeVendidaAnoPassado = new JLabel("Unidades Vendidas");
		lUnidadeVendidaAnoPassado.setBounds(190,385,120,20);
		p1.add(lUnidadeVendidaAnoPassado);
		
		tUnidadeVendidaAnoPassado = new JTextField();
		tUnidadeVendidaAnoPassado.setEditable(false);
		tUnidadeVendidaAnoPassado.setBounds(190,405,100,30);
		p1.add(tUnidadeVendidaAnoPassado);
		
		// varia��o lucro
		
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
			Object[]botoes = {"Sim", "N�o"};
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
		 *calcula-se o per�odo decorrido do in�cio do m�s at� agora
		 *calcula-se o mesmo per�odo em rela��o ao m�s passado
		 *calcula-se o mesmo per�odo em rela��o ao m�s retrasado
		 *calcula-se o mesmo per�odo em rela��o ao ano passado
		 *Ex: hoje � dia 17, o calculo dos outros meses e ano � feito em cima de 17 dias tamb�m
		 */
		 
		 
		 //CALCULANDO O HIST�RICO DE VENDAS DO COME�O DO M�S AT� AGORA
		 
		 //o objeto simpleDateFormat 'formataAgora' serve para deixar o formato de dado padr�o mysql
		 
		 //pegando a data atual que ser� a dataFinal da pesquisa deste m�s
		 String dataFinal = formataAgora.format(agora);
		 
		 //pegando o dia do m�s para calcular
		 int diaMes = data.get(Calendar.DAY_OF_MONTH);
		 
		  //calculando a data desejada, neste caso dia primeiro, e adicionando ao Calendar 'data'
		  //Ex: hoje � dia 18, dia primeiro seria (diaAtual) - (diaAtual - 1)
	       data.add(Calendar.DATE, - (diaMes - 1));
		
		 //instanciando outro objeto de Date, baseado no calculo do objeto Calendar data
		 // calculaAgora cont�m a data calculada para uso
		 calculaAgora = data.getTime();
		
		
		 //cont�m a data do dia primeiro deste m�s
		 String dataInicial = formataAgora.format(calculaAgora);
		 
		 
		 // vari�veis para guardar valores do per�odo atual
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
			

			
			// for para carregar as vari�veis
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

	
	
	
		
		//compara��o em rela��o ao m�s passado
		//<--------------------------------------------------------------->
		/* Formula da compara��o 
		*varia��o = valor a comparar / base de compara��o * 100 - 100
		*EX = Valor deste m�s / valor m�s passado * 100 - 100 = Porcentagem de aumento ou queda
		*/
		
		//Variaveis para armazenar valores de compara��o
		double totalUnidadeComparacao = 0.0, totalVendidoComparacao = 0.0, totalLucroComparacao = 0.0;

		 // Zerando o objeto data para fazer novos calculos
	     data.setTime(agora);
		
		
		//subtraindo 1 m�s e adicionando ao Calendar data
	    data.add(Calendar.MONTH, -1);
	    //adicionando de Calendar data para Date calculaAgora
	    calculaAgora = data.getTime();	
	    
	    //formatando a data final da pesquisa do m�s passado	
		dataFinal = formataAgora.format(calculaAgora);
		
		
		//Calculando dia primeiro do m�s passado
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
			

			
			// for para carregar as vari�veis
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

		//previnindo divis�es por 0
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
		

	
		//compara��o em rela��o ao M�s retrasado
		//<----------------------------------------------->
		//zerando vari�veis
		totalUnidadeComparacao = 0.0;
		totalVendidoComparacao = 0.0;
		totalLucroComparacao = 0.0;

	
		 // Zerando o objeto data para fazer novos calculos
		data.setTime(agora);
		
		//subtraindo 2 meses
	    data.add(Calendar.MONTH, -2);
	    calculaAgora = data.getTime();	
		dataFinal = formataAgora.format(calculaAgora);
		
		
		//calculando dia primeiro m�s retrasado
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
			

			
			// for para carregar as vari�veis
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
	
		//previnindo divis�es por 0
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
	




		//compara��o em rela��o ao ano passado
		//<---------------------------------------------------->
		//zerando vari�veis
		totalUnidadeComparacao = 0.0;
		totalVendidoComparacao = 0.0;
		totalLucroComparacao = 0.0;

		// Zerando o objeto data para fazer novos calculos
		data.setTime(agora);
		
		//subtraindo 1 ano
	    data.add(Calendar.YEAR, -1);
	    calculaAgora = data.getTime();	
		dataFinal = formataAgora.format(calculaAgora);
		
		
		// calculando dia primeiro do mesmo m�s do ano passado
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
			

			
			// for para carregar as vari�veis
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
		
		//previnindo divis�es por 0
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

		
	} // fim do m�todo carregarDados
	
	
} // fim da classe