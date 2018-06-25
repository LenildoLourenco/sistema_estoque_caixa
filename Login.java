import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener
{
	
	
	// objetos para usar banco de dados
	ResultSet resultSet;
	Statement statement;
	
	

	// text fields e labels
	
	JLabel lUsuario, lSenha;
	
	JTextField tUsuario;
	
	//senha
	JPasswordField pSenha;
	
	// botoes
	
	JButton bEntrar, bCancelar;
	
	// painel
	JPanel p1;
	
	//string para ser usada como parametro
	String login, usuario;
	
	
	/* variavel de parametro menuAberto verifica se o frame login está sendo acessado ao entrar no sistema
	   ou ao trocar o usuário, pois quando clicar em cancelar ao acessar o sistema
	   o mesmo será fechado, porém ao clicar em cancelar ao acessar trocando o 
	   usuário apenas o frame será fechado e não o sistema todo.
	   System.exit(0) "fecha toda a aplicação"
	   setVisible(false) "fecha apenas o frame atual"
	 */
	boolean menuAbertoGlobal;
	
	// a variavel de parametro titulo seta o titulo pois o mesmo frame é acessado como login e troca de usuario e pLogin e pUsuario passam parametros ao abrir
	// menuPrincipal
	Login(boolean menuAberto, String titulo,String pLogin, String pUsuario)
	{
		setTitle(titulo);
		setSize(250,202);
		setLocationRelativeTo(null);
		
		menuAbertoGlobal = menuAberto;
		
		
		setIconImage(new ImageIcon("Imagens/troca_usu.png").getImage());
		
		
		// painel
		
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBackground(new Color(192,192,192));
		getContentPane().add(p1);
		
		login = pLogin;
		usuario = pUsuario;
		
		// textfield labels e password
		
		lUsuario = new JLabel("Login");
		lUsuario.setBounds(15,10,100,20);
		p1.add(lUsuario);
		
		tUsuario = new JTextField();
		tUsuario.setBounds(15,30,210,30);
		tUsuario.addActionListener(this);
		p1.add(tUsuario);
		
		lSenha = new JLabel("Senha");
		lSenha.setBounds(15,70,100,20);
		p1.add(lSenha);
		
		pSenha = new JPasswordField();
		pSenha.setEchoChar('*');
		pSenha.setBounds(15,90,210,30);
		pSenha.addActionListener(this);
		p1.add(pSenha);
		
		// botoes
		
		bEntrar = new JButton("Entrar");
		bEntrar.setBounds(15,130,100,30);
		bEntrar.addActionListener(this);
		bEntrar.setMnemonic(KeyEvent.VK_E);
		p1.add(bEntrar);
		
		bCancelar = new JButton("Cancelar");
		bCancelar.setBounds(120,130,100,30);
		bCancelar.addActionListener(this);
		bCancelar.setMnemonic(KeyEvent.VK_C);
		p1.add(bCancelar);
		
		carregaResultSet();
		
		
		
		
	} // fim do construtor
	
	public void actionPerformed(ActionEvent e)
	{
		
		
		
		if(e.getSource() == tUsuario)
		{
			pSenha.requestFocus();
		}
		
		
		
		// se a ação vier de algum campo ou botão verifique
		if(e.getSource() == pSenha || e.getSource() == bEntrar)
		{	
			
			if(!verificarSenha())
			{
				return;
			}
				
	
			JFrame frameMenuPrincipal = new MenuPrincipal(login,usuario);
			frameMenuPrincipal.setUndecorated(true);
			frameMenuPrincipal.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
			frameMenuPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frameMenuPrincipal.setVisible(true);
			frameMenuPrincipal.setResizable(false);
			setVisible(false);
			
	
			
		} // fim if pSenha ou bEntrar
		
		
		
		//verificando se o menuprincipal ja estava aberto para quando cancelar chamar ele denovo
		if(e.getSource() == bCancelar)
		{
			if(menuAbertoGlobal)
			{
				
				JFrame frameMenuPrincipal = new MenuPrincipal(login,usuario);
				frameMenuPrincipal.setUndecorated(true);
				frameMenuPrincipal.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
				frameMenuPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frameMenuPrincipal.setVisible(true);
				frameMenuPrincipal.setResizable(false);
				setVisible(false);
			}
			else
			{
				System.exit(0);
			}	
			
		} // fim bCancelar
		
		
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
	
	
	public boolean verificarSenha()
	{
		try
		{
			resultSet = statement.executeQuery("SELECT * FROM usuarios WHERE login ='"+tUsuario.getText()+"'");
			resultSet.next();
			
			//pegando senha do banco para comparação
			String senha = resultSet.getString("senha");
			
			//verificando senha com base no usuario
			if(!new String(pSenha.getPassword()).equals(senha))
			{
				JOptionPane.showMessageDialog(null,"Usuário ou senha inválidos");
				pSenha.setText("");
				tUsuario.requestFocus();
				return(false);
				
			}
			
			// carregando variavel login que sera usada como parametro
			login = tUsuario.getText();
			
			//pegando nome do usuario, pois em tUsuario esta na verdade o login,
			usuario = resultSet.getString("nome");
			
			
			
	
	
						
		} // fim try
		catch(SQLException erro)
		{
			JOptionPane.showMessageDialog(null,"Usuário ou senha inválidos");
			pSenha.setText("");
			tUsuario.requestFocus();
			return(false);
		}
		
		// se não cair na exceção ou no if é verdadeira a senha
		return(true);
		
	} // fim verificar senha
	
} // fim da classe