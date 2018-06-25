import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*; 
import javax.swing.text.*;
import java.text.*;
import java.sql.*;

public class CadastroUsuario extends JFrame implements ActionListener, DocumentListener, ListSelectionListener
{
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	// int usado para contar o n�mero de cadastros no banco de dados
	int numeroCadastros;
	
	
	
	//string usada para auxiliar o carregamento de dados do banco para JTable
	String dados[] = new String[3];
	
	
	
	
	// tableRowSorter usado para pesquisar na table por text
	TableRowSorter<TableModel> organizador; 
	
	// string para receber valores de JOptionPane
	String aux;

	
	// int para ser usado na pesquisa da table, para ver o item selecionado
	int index ;
	// int usado para auxiliar index
	int index2;
	


	// objeto para evitar a abertura de multiplas janelas
	JFrame objeto = new JFrame();
	
	
	
	// painel
	JPanel p1;
	
	// menu, menuitem, menuBar
	
	JMenuBar barraMenu;
	JMenuItem iSair;
	JMenu mSair;
	
	
	// table
	
	JTable tabela;
	DefaultTableModel tableModel;
	
	
	
	// scroll para table
	
	JScrollPane scrollPane;
	
	// botoes
	
	JButton bAlterar, bExcluir, bAcesso,bCancelar, bSalvar, bLimpar, bNovo; 
	
	// label e textfields

	JLabel lNome, lLogin, lSenha, lRepetirSenha, lDescricaoPesquisa;
	
	JTextField tNome, tLogin, tPesquisar;
	
	// senha
	
	JPasswordField pSenha, pRepetirSenha;
	
	
	
	CadastroUsuario()
	{
		setTitle("Cadastro de Usu�rios");
		setSize(650,620);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		setIconImage(new ImageIcon("Imagens/cad_usu.png").getImage());
		
		
		
		// painel
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		// menus
		
		barraMenu = new JMenuBar();
		
		mSair = new JMenu("Sair");
				
		
		iSair = new JMenuItem("Sair");
		iSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		iSair.addActionListener(this);
		
		
		mSair.add(iSair);
		barraMenu.add(mSair);
		setJMenuBar(barraMenu);
		
		
		
		
		// nome
		lNome = new JLabel("Nome:");
		lNome.setBounds(10,10,100,20);
		p1.add(lNome);
		
		tNome = new JTextField();
		tNome.setBounds(10,30,250,30);
		p1.add(tNome);
		
		// login
		lLogin = new JLabel("Login:");
		lLogin.setBounds(270,10,100,20);
		p1.add(lLogin);
		
		tLogin = new JTextField();
		tLogin.setBounds(270,30,250,30);
		p1.add(tLogin);
		
		// senha
		lSenha = new JLabel("Senha:");
		lSenha.setBounds(10,70,100,20);
		p1.add(lSenha);
		
		pSenha = new JPasswordField();
		pSenha.setEchoChar('*');
		pSenha.setBounds(10,90,250,30);
		p1.add(pSenha);
		
		// repetir senha
		
		lRepetirSenha = new JLabel("Repetir Senha:");
		lRepetirSenha.setBounds(270,70,100,20);
		p1.add(lRepetirSenha);
		
		pRepetirSenha = new JPasswordField();
		pRepetirSenha.setEchoChar('*');
		pRepetirSenha.setBounds(270,90,250,30);
		pRepetirSenha.addActionListener(this);
		p1.add(pRepetirSenha);
		
		
		
		// botoes
		
		bNovo = new JButton("Novo");          
		bNovo.addActionListener(this);
		bNovo.setMnemonic(KeyEvent.VK_N); 	
		bNovo.setBounds(10,130,100,30);
		p1.add(bNovo);
		
		
		bSalvar = new JButton("Salvar");  
		bSalvar.addActionListener(this);
		bSalvar.setMnemonic(KeyEvent.VK_S); 	
		bSalvar.setBounds(120,130,100,30);
		p1.add(bSalvar);
		
		bLimpar = new JButton("Limpar"); 
		bLimpar.addActionListener(this);
		bLimpar.setMnemonic(KeyEvent.VK_L);
		bLimpar.setBounds(230,130,100,30);
		p1.add(bLimpar);
		
		bCancelar = new JButton("Cancelar"); 
		bCancelar.addActionListener(this);
		bCancelar.setMnemonic(KeyEvent.VK_C);
		bCancelar.setBounds(340,130,100,30);
		p1.add(bCancelar);
		

		bAlterar = new JButton("Alterar");
		bAlterar.addActionListener(this);
		bAlterar.setMnemonic(KeyEvent.VK_A);
		bAlterar.setBounds(10,170,100,30);
		p1.add(bAlterar);
		

		
		bExcluir = new JButton("Excluir");
		bExcluir.addActionListener(this);
		bExcluir.setMnemonic(KeyEvent.VK_E);
		bExcluir.setBounds(120,170,100,30);
		p1.add(bExcluir);
		
		
		bAcesso = new JButton("Acessos");
		bAcesso.addActionListener(this);
		bAcesso.setMnemonic(KeyEvent.VK_O);
		bAcesso.setBounds(230,170,100,30);
		p1.add(bAcesso);
		
		

		
		// barra pesquisa
		 lDescricaoPesquisa = new JLabel("Pesquisar");
		 lDescricaoPesquisa.setBounds(10,210,150,20);
		 p1.add(lDescricaoPesquisa);
		
		 tPesquisar = new JTextField();
		 tPesquisar.setBounds(10,230,300,30);
		 // adicionando documentListener
		 tPesquisar.getDocument().addDocumentListener(this);
		 p1.add(tPesquisar);


		// table do resultado
		
		//sobreescrevendo m�todo
		tableModel = new DefaultTableModel(){
	 	// permite a table ser selecion�vel e n�o edit�vel
		@Override
	 	public boolean isCellEditable(int row, int column)
	 	{
	 		return false;
		}
	 	};
		
		
		tableModel.addColumn("Nome");
		tableModel.addColumn("Login");
	

		
	 	tabela = new JTable(tableModel);
	 	
	 	// adicionando SelectionListener
		 tabela.getSelectionModel().addListSelectionListener(this);
	 	
	 	// definindo apenas uma linha selecion�vel
		 tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	 
	 	// Definindo as celulas do table n�o arrast�veis e n�o redimension�veis
	 	tabela.getTableHeader().setResizingAllowed(false);
	 	tabela.getTableHeader().setReorderingAllowed(false);
	 	
	 	// definindo o valor das c�lulas centralizado
		 DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		 centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	 	
		 tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		 tabela.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

	 	
	 	// tableRowSorter usado para pesquisar na table por text
		 organizador = new TableRowSorter<>(tabela.getModel());
	 	 tabela.setRowSorter(organizador);

	 
	 	// scrollpane para a table ficar com barra de rolagem
		scrollPane = new JScrollPane(tabela);
	 	scrollPane.setBounds(10,280,560,220);
	 	p1.add(scrollPane);
	
		
		setandoEditable(false);
		tLogin.setEditable(false);
		setandoBotoes(true,false,false,false,false,false,false);

		
		//carregando banco
		carregaResultSet();
		
		//m�todo para carregar a table com o dados do banco
		carregaTable();
		
	} // fim do construtor
	
	public void actionPerformed(ActionEvent e)
	{
		
		// painel Pesquisar
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
				return;
		} // fim if sair
		
		
		if(e.getSource() == bNovo)
		{
			setandoEditable(true);
			tLogin.setEditable(true);
			limpar();
			tLogin.setText("");
			
			setandoBotoes(false,true,true,true,false,false,false);
			
			//limpando a sele��o da table
			tabela.getSelectionModel().clearSelection();
			
		} // fim bNovo
		
		if(e.getSource() == bAlterar)
		{
			setandoEditable(true);
			setandoBotoes(false,true,true,true,false,false,false);
			
		} // fim bAlterar
		
		if(e.getSource() == bSalvar || e.getSource() == pRepetirSenha)
		{	
			
			// se retornar false sair do m�todo
			if(!verificaPreenchimento())
			{
				return;
			}
			
			index = tabela.getSelectedRow ();
			
			// verifica se salvar veio de um novo cadastro ou de uma altera��o
			// salvando no banco de acordo com a opera��o insert into novo cadastro ou update cadastroAntigo
			// index == -1 significa que JTable n�o est� selecionada, consequentemente indica um novo cadastro
			if(index == -1)
			{

				
				try
				{
						
					String sql ="INSERT INTO usuarios (nome, login, senha) Values ('"+
				 	 tNome.getText()+"','"+
				 	 tLogin.getText().toLowerCase()+"','"+
				 	 new String (pSenha.getPassword())+"')";
					 	 
				 	 statement.executeUpdate(sql);
					 	 
				 	 JOptionPane.showMessageDialog(null,"Cadastro realizado com sucesso");
				 	 
				 	 setandoBotoes(true,false,false,false,true,true,true);
				 	 setandoEditable(false);
				 	 tLogin.setEditable(false);
				 	 
				 	 // limpando e carregando a table novamente 
					 limpaTable();
					 carregaTable();
					 //selecionando ultimo cadastro jTable
					 int linha = tableModel.getRowCount();
					 
					 tabela.setRowSelectionInterval(linha -1,linha -1);
	
					 //carregando novo cadastro
					 carregarDados();
				 	 
					 	 
				
				}
				catch(SQLException erro) 
				{	// se o campo login que � primary key for igual a algum existente entrada na exe��o
					JOptionPane.showMessageDialog(null,"Login j� cadastrado ou campos com valor excedente");
				}
				
				
			} // fim if index == -1
			else
			{
				try
				{
					String sql = "UPDATE usuarios SET "+
						"nome = '" + tNome.getText() + "',"+
						"senha = '" + new String(pSenha.getPassword()) +"'"+
						"WHERE login = '" +tLogin.getText()+"'";
					
		
				
					int r = statement.executeUpdate(sql);
				
					if(r==1)
					{
						JOptionPane.showMessageDialog(null, "Altera��o realizada com sucesso");
						// habilitando botoes com parametro 3
						setandoBotoes(true,false,false,false,true,true,true);
						// tornando os textFields n�o edit�veis
						setandoEditable(false);
						tLogin.setEditable(false);
						
						// limpando e carregando a table novamente e selecionando-a
						index2 = index;
						limpaTable();
						carregaTable();
		
						//selecionando a jtable aps recri�-la
						tabela.setRowSelectionInterval(index2,index2);
						//carregando novo cadastro
						carregarDados();
						
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Problemas na Altera��o");
					
					}
					
				}
				catch(SQLException erro)
				{
					JOptionPane.showMessageDialog(null,"Atualiza��o n�o realizada");
				}
		
		
	
			} // fim else index ==-1
			
	
				
		} // fim if bSalvar
		
	
		
		if(e.getSource() == bLimpar)
		{
			//verifica se a table esta ou n�o selecionada
			// pois limpar(), limpar� tLogin apenas em novos cadastros	
			index = tabela.getSelectedRow();
			
			if(index == -1)
			{
				limpar();
				tLogin.setText("");
				
			}
			else
			{
				limpar();
				
			}
				
			
		
		} // fim if bLimpar
			
		
		
		
		if(e.getSource() == bCancelar)
		{
			// tornando os textFields n�o edit�veis
			setandoEditable(false);	
			tLogin.setEditable(false);
			
			
			//verifica se a table esta ou n�o selecionada	
			index = tabela.getSelectedRow();
			
			if(index == -1)
			{
				limpar();
				tLogin.setText("");
				setandoBotoes(true,false,false,false,false,false,false);
			}
			else
			{
				carregarDados();
				setandoBotoes(true,false,false,false,true,true,true);	
	
			}
			
				
			// verifica se a table est� ou n�o selecionada, -1 significa n�o selecionada
			// se sim carrega os dados denovo, se n�o limpa tudo e decrementa a vari�vel codigo
			index = tabela.getSelectedRow();
			
			

			
		} // fim if bCancelar
		
		
		if(e.getSource() == bExcluir)
		{
			
			//n�o � permitido excluir o admin
			if(tLogin.getText().equals("admin"))
			{
					JOptionPane.showMessageDialog(null,"N�o � permitido excluir o admin");
					return;
			}
			
			try
			{	
				String nome = "Deseja excluir o cadastro : "+tNome.getText();
					
				int n = JOptionPane.showConfirmDialog(null,nome,"",JOptionPane.YES_NO_OPTION);
				
				if(n == JOptionPane.YES_OPTION)
				{
					String sql = "DELETE FROM usuarios WHERE login = '"+tLogin.getText()+"'";
					int r = statement.executeUpdate(sql);
					
					if(r==1)
					{
						JOptionPane.showMessageDialog(null,"Exclus�o realizada com sucesso");
					}
					else
					{
						JOptionPane.showMessageDialog(null,"N�o foi poss�vel excluir o Cadastro");
					}
					
		
				} // fim if
			
			} // fim try
			catch(SQLException erro)
			{
			
			}
			
			//limpando a sele��o da table para evitar erros
			tabela.getSelectionModel().clearSelection();
			limpaTable();
			carregaTable();
			
			// limpar todos os campos
			tLogin.setText("");	
			limpar();
			setandoBotoes(true,false,false,false,false,false,false);
				
			
			return;
		} // fim if bExcluir
		
		
	

		
		if(e.getSource() == bAcesso)
		{
			if(objeto.isVisible())
			{
				JOptionPane.showMessageDialog(null,"Janela  de acessos j� aberta!",
					"Aviso",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// n�o � permitido alterar acesso de admin
			if(tLogin.getText().equals("admin"))
			{
				JOptionPane.showMessageDialog(null,"N�o � permitido alterar acessos de admin",
					"Aviso",JOptionPane.WARNING_MESSAGE);
				return;
			} // fim if
			
			
			//passa o login e usuario como parametro
			objeto = new Acessos(tLogin.getText(),tNome.getText());
			
		} // fim if bAcesso
		
		
		
	} // fim ActionListener
	
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
	
	
	public boolean verificaPreenchimento()
	{
		// verificando se campos foram preenchidos
		
		if(tNome.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null,"Preenchimento do campo nome obrigat�rio ",
				"Aviso",JOptionPane.WARNING_MESSAGE);
			tNome.requestFocus();
			return(false);
		}
		
		if(tLogin.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null,"Preenchimento do campo Login obrigat�rio ",
				"Aviso",JOptionPane.WARNING_MESSAGE);
			tLogin.requestFocus();
			return(false);	
		}
		
		if(new String(pSenha.getPassword()).equals("")) 
		{
			JOptionPane.showMessageDialog(null,"Preenchimento do campo senha obrigat�rio ",
				"Aviso",JOptionPane.WARNING_MESSAGE);
			pSenha.requestFocus();
			return(false);
		}
		
		
		//verifica se a senha possui mais de 5 caracteres
		if(new String(pSenha.getPassword()).length() < 5 )
		{
			JOptionPane.showMessageDialog(null,"Senha precisa ter pelo menos 5 caracteres",
				"Aviso",JOptionPane.WARNING_MESSAGE);
			pSenha.setText("");
			pRepetirSenha.setText("");
			pSenha.requestFocus();
			return(false);
		}
		
		if(new String(pRepetirSenha.getPassword()).equals("")) 
		{
			JOptionPane.showMessageDialog(null,"Preenchimento do campo repetir senha obrigat�rio ",
				"Aviso",JOptionPane.WARNING_MESSAGE);
			pRepetirSenha.requestFocus();
			return(false);
		}
		
		// se senha e repetirSenha forem diferentes retornar false
		if(!new String(pSenha.getPassword()).equals(new String(pRepetirSenha.getPassword())))
		{
			
			// senha n�o confere
			JOptionPane.showMessageDialog(null,"Senha n�o confere","Aviso",JOptionPane.WARNING_MESSAGE);
			pSenha.setText("");
			pRepetirSenha.setText("");
			pSenha.requestFocus();					
			return(false);
		}
		
		return(true);
		
	
	} // fim m�todo verifica preenchimento	
	
	public void limpar()
	{
		tNome.setText("");
		pSenha.setText("");
		pRepetirSenha.setText("");
	}
	
	
	
	// m�todo da interface documentListener esse aqui � s� para funcionar n�o esta sendo usado
	public void changedUpdate(DocumentEvent e)
	{
		
	}
	
	
	/* m�todo da interface documentListener, quando um caractere � inserido
	* usando TableRowSorter para organizar a table conforme pesquisa
	*/
	public void insertUpdate(DocumentEvent e)
	{
		String text = tPesquisar.getText();
		if(text.trim().length() == 0)
		{
			organizador.setRowFilter(null);
		}
		else
		{
			organizador.setRowFilter(RowFilter.regexFilter("(?i)" +text));
		}
			
		
		
		
	} // fim insertUpdate
	
	
	/* m�todo da interface documentListener, quando um caractere � apagado
	 * usando TableRowSorter para organizar a table conforme pesquisa
	 */
	public void removeUpdate(DocumentEvent e)
	{
		String text = tPesquisar.getText();
		
		if(text.trim().length() == 0)
		{
			organizador.setRowFilter(null);
		}
		else
		{
			organizador.setRowFilter(RowFilter.regexFilter("(?i)" + text));
		}
		
	} // fim removeUpdate
	
	
	
	//m�todo usado quando um valor � mudado em jtable
	public void valueChanged(ListSelectionEvent e)
	{		
		// verifica se a table esta selecionada ou se o m�todo valueChanged
		// foi acionado por uma desele��o, e retorna
		if(tabela.getSelectedRow() == -1 )
		{
			return;
		}
		
		// verifica se a table foi selecionada durante uma inser��o ou altera��o de cadastro
		// e n�o permite o carregamento de dados para o usu�rio n�o perder suas altera��es
		// antes de salvar ou cancelar
		
		if(!bNovo.isEnabled())
			
		{
			JOptionPane.showMessageDialog(null, "Salve ou cancele antes de selecionar outro cadastro",
			"Aviso", JOptionPane.WARNING_MESSAGE);
			
			//Limpar a sele��o
			tabela.getSelectionModel().clearSelection();
			return;
		}
	
		setandoBotoes(true,false,false,false,true,true,true);
		
		
		// seta os textField false pois se durante uma altera��o o usuario selecionar a table
		// ele n�o poder� mais alterar antes de clicar em bAlterar novamente
		setandoEditable(false);

		
		carregarDados();
		
	} // fim valueChanged
	
	
	public void setandoEditable(boolean parametro)
	{
		tNome.setEditable(parametro);
		pSenha.setEditable(parametro);
		pRepetirSenha.setEditable(parametro);
		
		
	}
	
	public void carregarDados()
	{
		
			//limpando campos de senha, pois eles n�o s�o carregados do banco, sendo assim
			//devem ser sempre limpos
			pSenha.setText("");
			pRepetirSenha.setText("");
		
		
			// verifica o item selecionado
			index = tabela.getSelectedRow();
			
			// verifica se o usuario selecionou alguma coisa, se n�o ele sai do m�todo sem carregar dados
			// para carregar precisa-se selecionar um produto
			if(index == -1)
			{
				return;
			}
			
			// converte o index, pois uma simples pesquisa ou reorganiza��o da table muda o index
			index = organizador.convertRowIndexToModel(index);
			
			// pegando login da sele��o da table para fazer consulta no banco
			Object objeto = tabela.getModel().getValueAt(index,1);
			
			
			// transformando login da JTable para string
			aux = objeto.toString();
			
			
			
			try
			{
				resultSet = statement.executeQuery("SELECT * FROM usuarios WHERE login ='"+aux+"'");
				resultSet.next();
				
				//setando valores
				tLogin.setText(""+aux);
				tNome.setText(resultSet.getString("nome"));

							
			} // fim try
			catch(SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"Usu�rio n�o encontrado");
				return;
			}
			
	} // fim m�todo carregarDados
	
	
	
	public void setandoBotoes(boolean pBNovo, boolean pBSalvar, boolean pBLimpar,
	 boolean pBCancelar, boolean pBAlterar, boolean pBExcluir, boolean pBAcesso)
	{
		bNovo.setEnabled(pBNovo);
		bSalvar.setEnabled(pBSalvar);
		bLimpar.setEnabled(pBLimpar);
		bCancelar.setEnabled(pBCancelar);
		bAlterar.setEnabled(pBAlterar);
		bExcluir.setEnabled(pBExcluir);
		bAcesso.setEnabled(pBAcesso);
	
	}
	
	
	
	public void carregaTable()
	{
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM usuarios");
			//verifica a quantidade de cadastros no banco
			while(resultSet.next())
			{
				numeroCadastros = resultSet.getInt(1);
			}
			
			
			// for para carregar table
			for(int i =0; i < numeroCadastros; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM usuarios LIMIT "+i+",1");
				resultSet.next();
				
				//atribuindo a pesquisa ao vetor dados para posteriormente atribuir a jTable
				
				dados[0] = resultSet.getString("nome");
				dados[1] = resultSet.getString("login");

				tableModel.addRow(dados);
				
			} // fim for
			
			
		} // fim try
		catch(SQLException erro)
		{
			
		}
	
	} // fim do m�todo carregaTable
	
	public void limpaTable()
	{

		numeroCadastros = tableModel.getRowCount();
			
		// for para limpar a table
		for(int i =0; i < numeroCadastros; i++)
		{
			
			tableModel.removeRow(0);
				
		} 
	} // fim m�todo limpaTable
	
	
	
	
} // fim da classe