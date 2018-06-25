import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.text.*;
import java.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.*;
import java.sql.*;

public class CadastroClientes extends JFrame implements ActionListener, DocumentListener, ListSelectionListener
{
	
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	// int usado para contar o n�mero de cadastros no banco de dados
	int numeroCadastros;
	
	
	//string usada para auxiliar o carregamento de dados do banco para JTable
	String dados[] = new String[3];

	
	// string para receber valores de JOptionPane
	String aux;

	
	// tableRowSorter usado para pesquisar na table por text
	TableRowSorter<TableModel> organizador; 
		
	// int para ser usado na pesquisa da table, para ver o item selecionado
	int index ;
	// int usado para auxiliar index
	int index2;
	
	
	// string para replace
	String stringDataDeNascimento, stringCep, stringTelefone, stringCelular;
	
	// int uf, usado para selecionar o estado com base no banco de dados
	int intUf;

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
	
	
	JLabel lDescricaoPesquisa, lResultado;

	
	JLabel lCodigo, lNome, lSexo, lDataDeNascimento, lEndereco, lNumero, lEmail, lBairro,  lCidade, lCep, lUf, 
		lTelefone, lCelular;
	
	JTextField tCodigo, tNome, tEndereco, tCidade, tBairro, tNumero, tEmail;
	
	JTextField tPesquisar; 
		
	// TextField formatado
	
	// data de nascimento
	JFormattedTextField ftDataDeNascimento;
	MaskFormatter mfDataDeNascimento;
	
	// cep
	JFormattedTextField ftCep;
	MaskFormatter mfCep;
	
	//telefone
	JFormattedTextField ftTelefone;
	MaskFormatter mfTelefone;
	
	//celular
	JFormattedTextField ftCelular;
	MaskFormatter mfCelular;
	 	
	 	
	// combobox
	
	JComboBox cUf;
	String estados[] = {"", "Acre(AC)", "Alagoas(AL)", "Amap�(AP)", "Amazonas(AM)", "Bahia(BA)",
		"Cear�(CE)", "Distrito Federal(DF)", "Espirito Santo(ES)", "Goi�s(GO)", "Maranh�o(MA)",
		"Mato Grosso(MT)", "Mato Grosso do Sul(MS)", "Minas Gerais", "Par�(PA)", "Para�ba(PB)", "Paran�(PR)",
		"Pernambuco(PE)", "Piau�(PI)", "Rio de Janeiro(RJ)", "Rio Grande do Sul(RS)", "Rio Grande do Norte(RN)",
		"Rond�nia(RO)", "Roraima(RR)", "Santa Catarina(SC)", "S�o Paulo(SP)", "Sergipe(SE)", "Tocantins(TO)"};
		
	
	
	// botoes
	
	JButton bAlterar, bCancelar, bSalvar, bExcluir, bLimpar, bNovo;
	
	// radioButton
	
	JRadioButton rMasculino, rFeminino;
	
	ButtonGroup bgSexo;
	
	
	
	CadastroClientes()
	{
		setTitle("Cadastro de Clientes");
		
		
		
		setSize(730,710);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		//setando icone
		
		setIconImage(new ImageIcon("Imagens/cad_cli.png").getImage());
		
		
		//painel
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
		
		// labels e textFields
		
		// codigo cliente	
		lCodigo = new JLabel("C�digo Cliente");
		lCodigo.setBounds(10,10,90,20);
		p1.add(lCodigo);
		
		tCodigo = new JTextField(3);
		tCodigo.setEditable(false);
		tCodigo.setBounds(10,30,70,30);
		p1.add(tCodigo);
		
		// nome
		
		lNome = new JLabel("Nome");
		lNome.setBounds(110,10,70,20);
		p1.add(lNome);
		
		tNome  = new JTextField(30);
		tNome.setEditable(false);
		tNome.setBounds(110,30,395,30);
		p1.add(tNome);
		
		// sexo
		
		lSexo = new JLabel("Sexo");
		lSexo.setBounds(515,10,70,20);
		p1.add(lSexo);
				
		rMasculino = new JRadioButton("M");
		rMasculino.setBounds(515,30,45,30);
		rMasculino.setEnabled(false);
		p1.add(rMasculino);
			
		rFeminino = new JRadioButton("F");
		rFeminino.setBounds(565,30,45,30);
		rFeminino.setEnabled(false);
		p1.add(rFeminino);
		
		bgSexo = new ButtonGroup();
		bgSexo.add(rMasculino);
		bgSexo.add(rFeminino);
	
		
		// data de nascimento
		
		lDataDeNascimento = new JLabel("Data de Nascimento");
		lDataDeNascimento.setBounds(10,70,120,20);
		p1.add(lDataDeNascimento);
		
		// maskFormater
		try
		{
			mfDataDeNascimento = new MaskFormatter("##/##/####");
			mfDataDeNascimento.setPlaceholderCharacter('0');
			
			mfCep = new MaskFormatter("#####-###");
			mfCep.setPlaceholderCharacter('0');
			
			mfTelefone = new MaskFormatter("(##)####-####");
			mfTelefone.setPlaceholderCharacter('0');
			
			mfCelular = new MaskFormatter("(##)#####-####");
			mfCelular.setPlaceholderCharacter('0');
			
			
			
		}
		catch(ParseException excp){}
		
		ftDataDeNascimento = new JFormattedTextField(mfDataDeNascimento);
		ftDataDeNascimento.setEditable(false);
		ftDataDeNascimento.setBounds(10,90,70,30);
		p1.add(ftDataDeNascimento);
		
		// endere�o
		
		lEndereco = new JLabel("Endere�o");
		lEndereco.setBounds(140,70,70,20);
		p1.add(lEndereco);
		
		tEndereco = new JTextField(30);
		tEndereco.setEditable(false);
		tEndereco.setBounds(140,90,390,30);
		p1.add(tEndereco);
		
		// numero
		
		lNumero = new JLabel("N�mero");
		lNumero.setBounds(540,70,70,20);
		p1.add(lNumero);
			
		tNumero = new JTextField();
		tNumero.setEditable(false);
		tNumero.setBounds(540,90,70,30);
		p1.add(tNumero);
		
		// bairro
		
		lBairro = new JLabel("Bairro");
		lBairro.setBounds(10,130,100,20);
		p1.add(lBairro);
		
		tBairro = new JTextField();
		tBairro.setEditable(false);
		tBairro.setBounds(10,150,205,30);
		p1.add(tBairro);
		
		
		// cidade
		
		lCidade = new JLabel("Cidade");
		lCidade.setBounds(225,130,100,20);
		p1.add(lCidade);
		
		tCidade = new JTextField(15);
		tCidade.setEditable(false);
		tCidade.setBounds(225,150,205,30);
		p1.add(tCidade);
		
		// Uf
		
		lUf = new JLabel("UF");
		lUf.setBounds(440,130,100,20);
		p1.add(lUf);
		
		cUf = new JComboBox(estados);
		cUf.setEnabled(false);
		cUf.setBounds(440,150,170,30);
		p1.add(cUf);
		
		
		
		// cep
		
		lCep = new JLabel("Cep");
		lCep.setBounds(10,190,70,20);
		p1.add(lCep);
		
		ftCep= new JFormattedTextField(mfCep);
		ftCep.setEditable(false);
		ftCep.setBounds(10,210,70,30);
		p1.add(ftCep);
		

		
		// Telefone
		
		lTelefone = new JLabel("Telefone");
		lTelefone.setBounds(90,190,90,20);
		p1.add(lTelefone);

		ftTelefone = new JFormattedTextField(mfTelefone);
		ftTelefone.setEditable(false);
		ftTelefone.setBounds(90,210,90,30);
		p1.add(ftTelefone);
		
		// celular
		
		lCelular = new JLabel("Celular");
		lCelular.setBounds(190,190,90,20);
		p1.add(lCelular);
		
		ftCelular = new JFormattedTextField(mfCelular);
		ftCelular.setEditable(false);
		ftCelular.setBounds(190,210,100,30);
		p1.add(ftCelular);
		
		// email
		
		lEmail = new JLabel("Email");
		lEmail.setBounds(300,190,80,20);
		p1.add(lEmail);
		
		tEmail = new JTextField();
		tEmail.setEditable(false);
		tEmail.setBounds(300,210,310,30);
		p1.add(tEmail);
		
		
		// bot�es
		
		bAlterar = new JButton("Alterar");
		bAlterar.setMnemonic(KeyEvent.VK_A);
		bAlterar.setEnabled(false);
		bAlterar.addActionListener(this);
		bAlterar.setBounds(10,250,100,30);
		p1.add(bAlterar);
		
		bCancelar = new JButton("Cancelar");
		bCancelar.setMnemonic(KeyEvent.VK_C);
		bCancelar.setEnabled(false);
		bCancelar.addActionListener(this);
		bCancelar.setBounds(120,250,100,30);
		p1.add(bCancelar); 
			
		bSalvar = new JButton("Salvar");
		bSalvar.setMnemonic(KeyEvent.VK_S);
		bSalvar.setEnabled(false);
		bSalvar.addActionListener(this);
		bSalvar.setBounds(230,250,100,30);
		p1.add(bSalvar);
		
		bExcluir = new JButton("Excluir");
		bExcluir.setMnemonic(KeyEvent.VK_E);
		bExcluir.setEnabled(false);
		bExcluir.addActionListener(this);
		bExcluir.setBounds(340,250,100,30);
		p1.add(bExcluir);
		
		bLimpar = new JButton("Limpar");
		bLimpar.setMnemonic(KeyEvent.VK_L);
		bLimpar.setEnabled(false);
		bLimpar.addActionListener(this);
		bLimpar.setBounds(450,250,100,30);
		p1.add(bLimpar);
		
		bNovo = new JButton("Novo");
		bNovo.setMnemonic(KeyEvent.VK_N);
		bNovo.addActionListener(this);
		bNovo.setBounds(10,290,100,30);
		p1.add(bNovo);
		

	
		// barra pesquisa
		 lDescricaoPesquisa = new JLabel("Pesquisa");
		 lDescricaoPesquisa.setBounds(10,330,150,20);
		 p1.add(lDescricaoPesquisa);
		
		 tPesquisar = new JTextField(6);
		 tPesquisar.setBounds(10,350,300,30);
		 // adicionando documentListener
	 	 tPesquisar.getDocument().addDocumentListener(this);
		 p1.add(tPesquisar);
		 
		 //label resultado
		 
		 lResultado = new JLabel("Resultado");
		 lResultado.setBounds(10,390,150,20);
		 p1.add(lResultado);
		 
		 
		 //table resultado
		 // sobreescrever este m�todo permite a table n�o ser edit�vel, e ainda sim ser selecion�vel
	 	 tableModel = new DefaultTableModel()
	 	 {
	 
			 @Override
		 	 public boolean isCellEditable(int row, int column)
		 	 {
		 		return false;
			 }
	 	 };
		 
		  tableModel.addColumn("C�digo");
		 tableModel.addColumn("Nome");
		 
		 
		 tabela = new JTable(tableModel);
		 


	     
	      // Definindo as celulas do table n�o arrast�veis e n�o redimension�veis
		 tabela.getTableHeader().setResizingAllowed(false);
		 tabela.getTableHeader().setReorderingAllowed(false);
		 
		 // adicionando SelectionListener
		 tabela.getSelectionModel().addListSelectionListener(this);
		
		// definindo apenas uma linha selecion�vel
		 tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 
		 // definindo o valor das c�lulas centralizado
		 DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		 centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	 	
		 tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	 	tabela.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		 
		 // definindo a largura das colunas da table
		 tabela.getColumnModel().getColumn(0).setPreferredWidth(60);
		 tabela.getColumnModel().getColumn(1).setPreferredWidth(500);

		 
		 // tableRowSorter usado para pesquisar na table por text
		 organizador = new TableRowSorter<>(tabela.getModel());
	 	 tabela.setRowSorter(organizador);
		 
		 scrollPane = new JScrollPane(tabela);
		 scrollPane.setBounds(10,410,560,220);
		 p1.add(scrollPane);
		 
		 //m�todo para ligar o banco de dados com a aplica��o
		carregaResultSet();
		
		//m�todo para carregar a table com o dados do banco
		carregaTable();
		 


	
	} // fim construtor
	
	
	
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
		} // fim if sair
		
			
		
		if(e.getSource() == bAlterar)
		{
			// tornando editavel os textField
			setandoEditable(true);
			// habilitando botoes com parametro 2 
			habilitandoBotoes(2);
				
		} // fim if bAlterar
		
		if(e.getSource() == bCancelar)
		{
			// tornando os textFields n�o edit�veis
			setandoEditable(false);	
				
			// setando bot�es com parametro 3
			habilitandoBotoes(3);
				
			// verifica se a table est� ou n�o selecionada, -1 significa n�o selecionada
			// se sim carrega os dados denovo, se n�o limpa tudo
			index = tabela.getSelectedRow();
			
			if(index == -1)
			{
				limpar();
				tCodigo.setText("");
			}
			else
			{
				carregarDados();
			}
			

				
		} // fim if bCancelar
		
		if(e.getSource() == bSalvar)
		{
			String sexo="";
			// usa o m�todo para verificar se todos os campos foram preenchidos
			if(!verificaPreenchimento())
			{
				return;
			}
			
			index = tabela.getSelectedRow ();
			
			//verificando radioButton selecionado e atribuindo a vari�vel sexo
			if(rFeminino.isSelected())
			{
				sexo = "f";
			}
			else if(rMasculino.isSelected())
			{
				sexo = "m";
			}
			


			
			// verifica se salvar veio de um novo cadastro ou de uma altera��o
			// salvando no banco de acordo com a opera��o insert into novo cadastro ou update cadastroAntigo
			// index == -1 significa que JTable n�o est� selecionada, consequentemente indica um novo cadastro
			if(index == -1)
			{

				
				try
				{
					
				   	String sql ="INSERT INTO clientes (nome, sexo, data_nascimento," +
					 	"endereco, numero, bairro, cidade, uf, cep, telefone, celular, email)"+
					 	 "Values ('"+
					 	 tNome.getText()+"','"+
					 	 sexo+"','"+
						stringDataDeNascimento+"','"+
						tEndereco.getText()+"','"+
						tNumero.getText() +"','"+
						tBairro.getText()+"','"+
						tCidade.getText()+"','"+
						cUf.getSelectedIndex()+"','"+
						stringCep+ "','"+
						stringTelefone +"','"+
						stringCelular +"','"+
						tEmail.getText()+"')";

					 
					 statement.executeUpdate(sql);
					 
					 JOptionPane.showMessageDialog(null,"Gravacao realizada com sucesso");
					 // habilitando botoes com parametro 3
				     habilitandoBotoes(3);
					 // tornando os textFields n�o edit�veis
					 setandoEditable(false);
						
					 // limpando e carregando a table novamente 
					 limpaTable();
					 carregaTable();
					 //selecionando ultimo cadastro jTable
					 int linha = tableModel.getRowCount();
					 
					 tabela.setRowSelectionInterval(linha -1,linha -1);
	
					 //carregando novo cadastro
					 carregarDados();
					 
					 
				} // fim try
				catch(SQLException erro)
				{
					JOptionPane.showMessageDialog(null,"Falha no cadastro");
				}
				
				
			} // fim if index == -1
			else
			{
				try
				{
					String sql = "UPDATE clientes SET "+
						"nome = '" + tNome.getText() + "',"+
						"sexo = '" + sexo + "',"+
						"data_nascimento = '" + stringDataDeNascimento +"',"+
						"endereco = '" + tEndereco.getText() +"',"+
						"numero = '"+ tNumero.getText()+"',"+
						"bairro = '"+ tBairro.getText() +"',"+
						"cidade = '"+ tCidade.getText()+"',"+
						"uf = '"+ cUf.getSelectedIndex()+"',"+
						"cep = '"+ stringCep +"',"+
						"telefone = '"+stringTelefone +"',"+
						"celular = '"+stringCelular +"',"+
						"email = '"+ tEmail.getText()+"'"+
						"WHERE codigo = '" +tCodigo.getText()+"'";
					
		
				
					int r = statement.executeUpdate(sql);
				
					if(r==1)
					{
						JOptionPane.showMessageDialog(null, "Altera��o realizada com sucesso");
						// habilitando botoes com parametro 3
						habilitandoBotoes(3);
						// tornando os textFields n�o edit�veis
						setandoEditable(false);
						
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
		
		if(e.getSource() == bExcluir)
		{
			
			
			try
			{	
				String nome = "Deseja excluir o cadastro : "+tCodigo.getText()+"\nNome: "+tNome.getText();
					
				int n = JOptionPane.showConfirmDialog(null,nome,"",JOptionPane.YES_NO_OPTION);
				
				if(n == JOptionPane.YES_OPTION)
				{
					String sql = "DELETE FROM clientes WHERE codigo = '"+tCodigo.getText()+"'";
					int r = statement.executeUpdate(sql);
					
					if(r==1)
					{
						JOptionPane.showMessageDialog(null,"Exclus�o realizada com sucesso");
					}
					else
					{
						JOptionPane.showMessageDialog(null,"N�o foi poss�vel excluir o item");
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
			tCodigo.setText("");	
			limpar();
			habilitandoBotoes(4);
				
				
		} // fim if bExcluir
		
		if(e.getSource() == bLimpar)
		{
			
			limpar();
			
			
		} // fim if bLimpar
		
		if(e.getSource() == bNovo)
		{
			// tornando os textFields edit�veis
			setandoEditable(true);	
			limpar();
			tCodigo.setText("");
	

			// habilitando botoes com parametro 1
			habilitandoBotoes(1);
					
		} // fim if bNovo
		
		
		
	} // fim ActionListener
	
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
	
	
	
	// m�todo usado quando um valor � mudado em JTable
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
				JOptionPane.showMessageDialog(null, "Salve ou cancele antes de selecionar outro item",
				"Aviso", JOptionPane.WARNING_MESSAGE);
				
				//Limpar a sele��o
				tabela.getSelectionModel().clearSelection();
				return;
			}

			// habilitando botoes com parametro 3
			habilitandoBotoes(3);
			
			
			// seta os textField false pois se durante uma altera��o o usuario selecionar a table
			// ele n�o pode mais alterar antes de clicar em bAlterar novamente
			setandoEditable(false);
	
			
			carregarDados();
		
	} // fim valueChanged
	
	/* m�todo para setar os textField editaveis ou n�o
	 *usado quando o bot�o alterar ou novo s�o selecionados
	 *possibilitando altera��o de campos
	 */
	public void setandoEditable(boolean editavel)
	{
		cUf.setEnabled(editavel);
		ftCep.setEditable(editavel);
	    ftDataDeNascimento.setEditable(editavel);
	    tNome.setEditable(editavel);
	    tEndereco.setEditable(editavel);
	    tNumero.setEditable(editavel);
	    tBairro.setEditable(editavel);
	    tCidade.setEditable(editavel);
	    ftTelefone.setEditable(editavel);
	    ftCelular.setEditable(editavel);
	    tEmail.setEditable(editavel);
	    rMasculino.setEnabled(editavel);
	    rFeminino.setEnabled(editavel);
		
		
	}
	
	// m�todo que verifica preenchimento para ser usado na hora de salvar
	public boolean verificaPreenchimento()
	{
		
		//replace
		stringDataDeNascimento = ftDataDeNascimento.getText().replace("/","");

		stringCep = ftCep.getText().replace("-","");
		
		stringTelefone = ftTelefone.getText().replace("(","");
		stringTelefone = stringTelefone.replace(")","");
		stringTelefone = stringTelefone.replace("-","");
		
		stringCelular = ftCelular.getText().replace(")","");
		stringCelular = stringCelular.replace("(","");
		stringCelular = stringCelular.replace("-","");
		
		
		
		// verifica algum preenchimento inv�lido ou vazio e retorna false
		if(tNome.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo nome\nobrigat�rio",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tNome.requestFocus();
			return(false);	
		}	
			
		if(bgSexo.getSelection() == null)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo sexo\nobrigat�rio",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			return(false);
		}
		
		
		if(stringDataDeNascimento.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo data de nascimento\nobrigat�rio",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			ftDataDeNascimento.requestFocus();
			return(false);
		} 
		
			
		if(tCidade.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "Erro!\nPreenchimento do campo cidade\nobrigat�rio",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			tCidade.requestFocus();
			return(false);	
		}
		
		if(cUf.getSelectedIndex() == 0)
		{
			JOptionPane.showMessageDialog(null, "Erro!\nSele��o de estado obrigat�ria ",
				"Aviso", JOptionPane.WARNING_MESSAGE);
			cUf.requestFocus();
			return(false);	
		}
		
		return(true);

	} // fim verificaPreenchimento
	
	// precisa-se limpar campos em mais de um lugar, fica mais facil usar um m�todo para isso
	public void limpar()
	{
		cUf.setSelectedIndex(0);
		ftCep.setValue(null);
	    ftDataDeNascimento.setValue(null);
	    
	    tNome.setText(""); 
	    tEndereco.setText("");
	    tNumero.setText("");
	    tBairro.setText("");
	    tCidade.setText("");
	    ftTelefone.setValue(null);
	    ftCelular.setValue(null);
	    tEmail.setText("");
	    bgSexo.clearSelection();
	    
	}
	
	// m�todo usado para atribuir dados aos textField
	public void carregarDados()
	{
			// verifica o item selecionado
			index = tabela.getSelectedRow();
			
			// verifica se o usuario selecionou alguma coisa, se n�o ele sai do m�todo 
			//  abrir cadastro precisa-se selecionar um produto
			if(index == -1)
			{
				return;
			}
			
			// converte o index, pois uma simples pesquisa ou reorganiza��o da table muda o index
			index = organizador.convertRowIndexToModel(index);
			
			// pegando c�digo da sele��o da table para fazer a consulta no banco
			Object objeto = tabela.getModel().getValueAt(index,0);
			
			
			// transformando c�digo da JTable para string
			aux = objeto.toString();
			// de string para int
			int codigo = Integer.parseInt(aux);
			
			
			
			try
			{
				resultSet = statement.executeQuery("SELECT * FROM clientes WHERE codigo ='"+codigo+"'");
				resultSet.next();
				
				//setando valores
				tCodigo.setText(""+codigo);
				tNome.setText(resultSet.getString("nome"));
				
				String sexo = resultSet.getString("sexo");
				
				//setando radio button
				if(sexo.equals("f"))
				{
					rFeminino.setSelected(true);
				}
				else if(sexo.equals("m"))
				{
					rMasculino.setSelected(true);
				}
					
				ftDataDeNascimento.setText(resultSet.getString("data_nascimento"));
				tEndereco.setText(resultSet.getString("endereco"));
				tNumero.setText(resultSet.getString("numero"));
				tBairro.setText(resultSet.getString("bairro"));
				tCidade.setText(resultSet.getString("cidade"));
				
				
				int selecaoEstado = Integer.parseInt(resultSet.getString("uf"));
				cUf.setSelectedIndex(selecaoEstado);
					
				ftCep.setText(resultSet.getString("cep"));
				ftTelefone.setText(resultSet.getString("telefone"));
				ftCelular.setText(resultSet.getString("celular"));
				tEmail.setText(resultSet.getString("email"));
				
		
							
			} // fim try
			catch(SQLException erro)
			{
				JOptionPane.showMessageDialog(null,"Cliente n�o encontrado");
				return;
			}
		
	} // fim m�todo carregarDados
	
	
	/* m�todo usado para habilitar e desabilitar bot�es
	 *
	 * bNovo ao ser clicado precisa deselecionar a JTable e habilitar bCancelar, bSalvar e bLimpar
	 * ele precisa tamb�m desabilitar,bAlterar,Bnovo, e bExcluir , seu parametro � 1
	 *
	 * bAlterar  ao ser clicado habilita e desabilita os mesmos bot�es que bNovo 
	 * por�m n�o deseleciona a JTable, seu parametro � 2
	 *
	 * bCancelar e bSalvar ao serem clicados habilitam somente bNovo se a JTable n�o estiver selecionada
	 * e habilitam bAlterar, bNovo e bExcluir se a table estiver selecionada,
	 * desabilitam sempre, bSalvar, bCancelar e bLimpar o parametro deles � 3
	 *
	 * quando a table � seleciona ela chama o m�todo com o parametro 3 e faz a mesma coisa que bCancelar e bSalvar
	 * 
	 */
	public void habilitandoBotoes(int parametro)
	{
		// 1 parametro de bNovo e bAlterar
		if(parametro == 1 || parametro == 2)
		{
			// somente bNovo deseleciona JTable
			if(parametro == 1)
				// deselecionando JTable
				tabela.getSelectionModel().clearSelection();
			
			bCancelar.setEnabled(true);
			bSalvar.setEnabled(true);
			bLimpar.setEnabled(true);
			
			bAlterar.setEnabled(false);
			bNovo.setEnabled(false);
			bExcluir.setEnabled(false);
			
			
		} // fim if 1 e 2
		
		
		// 3 parametro de bCancelar e bSalvar
		// e parametro do evento de sele��o da JTable
		if(parametro == 3)
		{
			// verifica se a table est� ou n�o selecionada, -1 significa n�o selecionada
			index = tabela.getSelectedRow();
			
			if(index == -1)
			{
				bNovo.setEnabled(true);
			}
			else
			{
				
				bAlterar.setEnabled(true);
				bNovo.setEnabled(true);
				bExcluir.setEnabled(true);
			}
			
			bSalvar.setEnabled(false);
			bCancelar.setEnabled(false);
			bLimpar.setEnabled(false);
			
		} // fim if 3
		
		if(parametro ==4)
		{
			bSalvar.setEnabled(false);
			bCancelar.setEnabled(false);
			bLimpar.setEnabled(false);
			bAlterar.setEnabled(false);
			bNovo.setEnabled(true);
			bExcluir.setEnabled(false);
		}
		
	} // fim do m�todo habilitandoBotoes
	
	//m�todo usado para ligar o banco de dados com a aplica��o
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
	
	//m�todo usado para carregar a table baseada no banco	
	public void carregaTable()
	{
		try
		{
			resultSet = statement.executeQuery("SELECT count(*) FROM clientes");
			//verifica a quantidade de cadastros no banco
			while(resultSet.next())
			{
				numeroCadastros = resultSet.getInt(1);
			}
			
			
			// for para carregar table
			for(int i =0; i < numeroCadastros; i++)
			{
				resultSet = statement.executeQuery("SELECT * FROM clientes LIMIT "+i+",1");
				resultSet.next();
				
				//atribuindo a pesquisa ao vetor dados para posteriormente atribuir a jTable
				
				dados[0] = resultSet.getString("codigo");
				dados[1] = resultSet.getString("nome");

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