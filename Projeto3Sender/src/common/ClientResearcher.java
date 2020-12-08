package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import classes.AsyncReceiver;
import classes.Publication;
import classes.Sender;
import classes.User;

public class ClientResearcher extends Thread{
	@PersistenceContext(name = "Loader")
	private EntityManager em;
	public void run() {
		AsyncReceiver asyncReceiver = null;
		try {
			asyncReceiver = new AsyncReceiver();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {

			try {
				//asyncReceiver.receive_and_reply();
				asyncReceiver.give_publications(GetallPubs());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws NamingException {
		String username,password;
		boolean validusername,validpassword;
	    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		boolean done  = false;
		while(!done) {
			System.out.println("****************JMS****************");
			System.out.println("Escolha uma opcao: ");
			System.out.println("(0) Register ");
			System.out.println("(1) Login ");
			System.out.println("(2) Exit ");
			System.out.println("\n");

			int option = lerInt(0, 3);

			//REGISTER
			if(option==0) {
				System.out.println("****************Register JMS****************");
				System.out.print("Escolha um username: ");
			    username = scanner.nextLine();  // Read user input
				System.out.print("Escolha uma password: ");
				password = scanner.nextLine();  // Read user input
				System.out.println("\n");
				validusername = (username != null) && username.matches("[A-Za-z0-9_]+");
				validpassword = (password != null) && password.matches("[A-Za-z0-9_]+");
				if(validusername==false || validpassword==false) {
					System.out.println("Please enter a valid username and password");
				}
				else {
					User utilizador = new User(username,password);
					//Enviar msg ao admin para confirmar registo
				}

			}

			//LOGIN
			if(option==1) {
				System.out.println("****************Login JMS****************");
				System.out.print("Username: ");
			    username = scanner.nextLine();  // Read user input
				System.out.print("Password: ");
				password = scanner.nextLine();  // Read user input
				System.out.println("\n");
				//Enviar msg ao admin para login

				//Ap�s confirmar aceder � app
				appuser();
			}

			//EXIT
			if(option==2) {
				System.out.println("You have successfully exited the Application.");
                done = true;
			}
		}
	}


	//LER INT DE STDIN
		public static int lerInt(int init, int fin ) {
			Scanner scan = new Scanner(System.in);
			int num = 0;
			boolean aux = true;
			System.out.print("Introduza a opcao: ");
			while (aux) {
				if (scan.hasNextInt()) {
					num = scan.nextInt();
					if (num>=init && num< fin) {
						aux = false;
					}
					else {
						System.out.print("Invalid option. Try again: ");
					}
				}
				else {
					System.out.print("Invalid option. Try again: ");
					scan.next();
				}
			}
			return num;
		}


		//MENU DE USER
		public static void appuser() throws NamingException {
			boolean done  = false;
		    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			String bookname,type,date;

			Sender playQueue = new Sender();


			while(!done) {
				System.out.println("****************JMS App****************");
				System.out.println("Escolha uma opcao: ");
				System.out.println("(0) List Publications ");
				System.out.println("(1) Search Publication ");
				System.out.println("(2) Add Publication ");
				System.out.println("(3) Update Publication ");
				System.out.println("(4) Remove Publication ");
				System.out.println("(5) Logout ");
				System.out.println("\n");

				int option = lerInt(0, 6);

				//LIST ALL PUBLICATIONS
				if(option==0) {
					//Enviar msg ao admin
					//printallpubs(playQueue.getPublications());
					//playQueue.send_and_reply("ola");
					playQueue.getPublications();
				}


				//SEARCH PUBLICATION BY NAME
				if(option==1) {
					System.out.println("****************SEARCH JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}

				//ADD PUBLICATION
				if(option==2) {
					System.out.println("****************ADD JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
				    System.out.print("Tipo da publicacao: ");
				    type = scanner.nextLine();  // Read user input
				    System.out.print("Data da publicacao (Month Year): ");
				    date = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}

				//UPDATE PUBLICATION
				if(option==3) {
					System.out.println("****************UPDATE JMS****************");
					System.out.print("Nome da publicacao a alterar: ");
					String booknameold=scanner.nextLine();
					System.out.print("Novo nome: ");
				    bookname = scanner.nextLine();  // Read user input
				    System.out.print("Novo tipo: ");
				    type = scanner.nextLine();  // Read user input
				    System.out.print("Nova Data (Month Year): ");
				    date = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}

				//REMOVE PUBLICATION
				if(option==4) {
					System.out.println("****************REMOVE JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
					System.out.println("\n");
					//Enviar mensagem a pedir info ao admin
				}

				//EXIT
				if(option==5) {
					System.out.println("You have successfully logged out.");
	                done = true;
				}
			}
		}



		//MENU DE ADMIN
		public static void appadmin() throws NamingException {
			boolean done  = false;
		    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		    ArrayList<String> tasks = new ArrayList<String>();
		    tasks=ReadFile();
			String user,bookname;
			//criar queues

			(new ClientResearcher()).start();


			while(!done) {
				System.out.println("****************JMS App****************");
				System.out.println("Escolha uma opcao: ");
				System.out.println("(0) List Users ");
				System.out.println("(1) List Pending Tasks ");
				System.out.println("(2) Deactivate User ");
				System.out.println("(3) List Publications ");
				System.out.println("(4) Search Publication ");
				System.out.println("(5) Logout ");
				System.out.println("\n");

				int option = lerInt(0, 6);

				//LIST ALL USERS
				if(option==0) {

				}


				//LIST PENDING TASKS
				if(option==1) {
					System.out.println("****************TASKS JMS****************");
					PrintTasks(tasks);
					System.out.print("Choose a pending task (Type 0 to exit): ");
					int option2=lerInt(0,tasks.size()+1);
					if(option2!=0) {
						SelectTask(tasks,option2-1);
					}
				}

				//DEACTIVATE USER
				if(option==2) {
					System.out.println("****************DEACTIVATE JMS****************");
					System.out.print("Nome do user: ");
				    user = scanner.nextLine();  // Read user input
					System.out.println("\n");
				}

				//LIST PUBLICATIONS
				if(option==3) {

				}

				//SEARCH PUBLICATION
				if(option==4) {
					System.out.println("****************SEARCH JMS****************");
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
					System.out.println("\n");
				}

				//EXIT
				if(option==5) {
					System.out.println("You have successfully logged out.");
	                done = true;
				}
			}
		}



//=========================JMS AUX=============================================================


		//RECEIVE A MESSAGE
		public static void printresponse() throws NamingException, IOException {
			AsyncReceiver asyncReceiver = new AsyncReceiver();
			asyncReceiver.launch_and_wait();
		}

		//RECEIVE AND REPLY
		public void printandrespond() throws NamingException, JMSException {
			AsyncReceiver asyncReceiver = new AsyncReceiver();
			asyncReceiver.receive_and_reply();
		}


		//SEND A MESSAGE
		public void sendmessage(String message) throws NamingException {
			Sender sender = new Sender();
			sender.send(message);
		}

		//SEND MESSAGE AND PRINT RESPONSE
		public void sendandreceive(String message) throws NamingException {
			Sender sender = new Sender();
			sender.send_and_reply(message);
		}




//=========================GET DATABASE INFO=============================================================



		//GET ALL USERS
		public static List<User> GetallUsers(){
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager em = emfactory.createEntityManager( );
		    // Define query String
		    String jpql = "SELECT u FROM User u";
		    // Create a (typed) query
		    TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
		    // Query and get result
		    List<User> mylist = typedQuery.getResultList();
		    return mylist;
		 }

		//GET ALL PUBS
		public static List<Publication> GetallPubs(){
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager em = emfactory.createEntityManager( );
		    // Define query String
		    String jpql = "SELECT r FROM Publication r";
		    // Create a (typed) query
		    TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
		    // Query and get result
		    List<Publication> mylist = typedQuery.getResultList();
		    return mylist;
		 }

		//GET PUBS BY NOME
		public List<Publication> GetPublicationByNome(String nome){
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager em = emfactory.createEntityManager( );
			// Define query String
			String jpql = "SELECT r FROM Publication r where r.name=:name";
			// Create a (typed) query
			TypedQuery<Publication> typedQuery = em.createQuery(jpql, Publication.class);
			// Set parameter
			typedQuery.setParameter("name", nome);
			// Query and get result
			List<Publication> mylist = typedQuery.getResultList();
			return mylist;
		}

		//ADD USER
		public void AddUser(User st) {
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager entitymanager = emfactory.createEntityManager( );
			entitymanager.getTransaction( ).begin( );
			List<User> mylist = GetallUsers();
			if (mylist.size()==0) {
				st.setAdmin(true);
			}
			entitymanager.persist(st);
			entitymanager.getTransaction( ).commit( );
			entitymanager.close( );
			emfactory.close( );
		}

		//ADD PUBLICATION
		public void AddPublication(Publication st) {
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager entitymanager = emfactory.createEntityManager( );
			entitymanager.getTransaction( ).begin( );
			entitymanager.persist(st);
			entitymanager.getTransaction( ).commit( );
			entitymanager.close( );
			emfactory.close( );
		}

		//UPDATE PUBLICATION
		public void UpdatePublication(Publication st, String old) {
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager em = emfactory.createEntityManager( );
		    em.getTransaction().begin();
		    if(st.getName().compareTo("")!=0) {
		    	Query query = em.createQuery("UPDATE Publication p SET p.name =: nome"
			            + "WHERE p.name = :oldname");
		    	query.setParameter("nome", st.getName());
			    query.setParameter("oldname", old);
			    query.executeUpdate();
		    }
		    if(st.getType().compareTo("")!=0) {
		    	Query query = em.createQuery("UPDATE Publication p SET p.type =: tipo "
			            + "WHERE p.name = :oldname");
		    	query.setParameter("tipo", st.getType());
			    query.setParameter("oldname", old);
			    query.executeUpdate();
		    }
		    if(st.getDate().compareTo("")!=0) {
		    	Query query = em.createQuery("UPDATE Publication p SET p.date =: data "
			            + "WHERE p.name = :oldname");
		    	query.setParameter("data", st.getDate());
			    query.setParameter("oldname", old);
			    query.executeUpdate();
		    }
		    em.getTransaction().commit();
		    em.close();
		}

		//REMOVE A PUBLICATION
		public void RemovePublication(String pubname) {
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager em = emfactory.createEntityManager( );
			em.getTransaction().begin();
		    Query query = em.createQuery("DELETE FROM Publication p WHERE p.name = :Name ");
		    query.setParameter("Name", pubname);
		    query.executeUpdate();
		    em.getTransaction().commit();
		    em.close();
		}

		//ACTIVATE OR DEACTIVATE A USER
		public void ActivateUser(User st) {
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager em = emfactory.createEntityManager( );
			em.getTransaction().begin();
			if(st.isActivated()==false) {
				Query query = em.createQuery("UPDATE User u SET u.activated = TRUE "
			            + "WHERE u.username = :Name");
			    query.setParameter("Name", st.getUsername());
			    query.executeUpdate();
			}
			else {
				Query query = em.createQuery("UPDATE User u SET u.activated = FALSE "
			            + "WHERE u.username = :Name");
			    query.setParameter("Name", st.getUsername());
			    query.executeUpdate();
			}

		    em.getTransaction().commit();
		    em.close();
		}

		//LOGIN
		public User login(String username, String password) {
			EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
			EntityManager em = emfactory.createEntityManager( );
			// Define query String
			String jpql = "SELECT u FROM User u where u.username=:name AND u.password=:pass";
			// Create a (typed) query
			TypedQuery<User> typedQuery = em.createQuery(jpql, User.class);
			// Set parameter
			typedQuery.setParameter("name", username);
			typedQuery.setParameter("pass", password);
			// Query and get result
			List<User> mylist = typedQuery.getResultList();
			User user=mylist.get(0);
			return user;

		}


//=========================PRINT DATABASE INFO=============================================================
		//PRINT ALL USERS INFO
		public static void printallusers() {
			List<User> mylist = GetallUsers();
			for(int i=0;i<mylist.size();i++) {
				System.out.println("Username: " + mylist.get(i).getUsername());
				System.out.println("Activated: " + String.valueOf(mylist.get(i).isActivated()));
			}
			System.out.println("\n");
		}

		//PRINT ALL PUBS INFO
		public static void printallpubs(List<Publication> mylist) {
			for(int i=0;i<mylist.size();i++) {
				System.out.println("Publication " + (i+1) + ":");
				System.out.println("Name: " + mylist.get(i).getName());
				System.out.println("Type: " + mylist.get(i).getType());
				System.out.println("Date: " + mylist.get(i).getDate());
				System.out.print("\n");
			}
			System.out.println("\n");
		}


//=========================PENDING TASKS AUX=================================================================
		//READ PENDING TASKS FROM FILE
		public static ArrayList<String> ReadFile() {
			ArrayList<String> tasks = new ArrayList<String>();
			try {
				File myObj = new File("Tasks.txt");
				Scanner myReader = new Scanner(myObj);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					tasks.add(data);
				}
				myReader.close();
		    	} catch (FileNotFoundException e) {
		    		return tasks;
		    	}
			return tasks;
		 }


		//WRITE PENDING TASKS TO FILE
		public void WriteFile(ArrayList<String> tasks) {
			//CREATE IF IT DOESNT EXIST
			try {
				File myObj = new File("Tasks.txt");
			    myObj.createNewFile();
			} catch (IOException e) {
				 System.out.println("An error occurred.");
			     e.printStackTrace();
			}
			//WRITE TO FILE
			try {
				BufferedWriter outputWriter = null;
				outputWriter = new BufferedWriter(new FileWriter("Tasks.txt"));
				for(int i=0;i<tasks.size();i++) {
					outputWriter.write(tasks.get(i));
					outputWriter.newLine();
				}
				outputWriter.flush();
				outputWriter.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}

		//PRINT ALL TASKS
		public void PrintTasks(ArrayList<String> tasks) {
			for(int i=0;i<tasks.size();i++) {
				System.out.println("("+(i+1)+") " + tasks.get(i));
			}
		}

		//SELECT A TASK
		//RETURNS ARRAYLIST WITH DECLINED TASK OR ACCEPTED
		public ArrayList<String> SelectTask(ArrayList<String> tasks, int choice) {
		    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		    boolean done=false;
			System.out.println("Task selected: " + tasks.get(choice));
			while(!done) {
				System.out.println("Do you want to accept or decline?");
				System.out.println("Type 'exit' to cancel");
			    String answer = scanner.nextLine();  // Read user input
				if(answer.compareTo("accept")==0) {
					DoTask(tasks.get(choice));
					done=true;
				}
				else if(answer.compareTo("decline")==0) {
					tasks.remove(choice);
					done=true;
				}
				else if(answer.compareTo("exit")==0) {
					done=true;
				}
				else {
					System.out.println("Please type 'accept' or 'decline'");
				}
			}
			return tasks;
		}


		//PARSE TASK AND EXECUTE
		public void DoTask(String task) {
			String[] tokens = task.split(":");
			//REGISTAR(Registo:Username:Password)
			if(tokens[0].compareTo("Registo")==0) {
				User novo= new User(tokens[1],tokens[2]);
				AddUser(novo);
				System.out.println("Utilizador " + tokens[1] + " adicionado com sucesso!");
			}
			//ADICIONAR(Adicionar:TestePub:Book:March 2013)
			else if(tokens[0].compareTo("Adicionar")==0) {
				Publication novo = new Publication(tokens[1],tokens[2],tokens[3]);
				AddPublication(novo);
				System.out.println("Publication " + tokens[1] + " adicionada com sucesso!");
			}
			//UPDATE(Update:TestePub:TestePubv2:Book:March 2014)
			else if(tokens[0].compareTo("Update")==0) {
				Publication updated=new Publication(tokens[2],tokens[3],tokens[4]);
				String oldname=tokens[1];
				UpdatePublication(updated,oldname);
				System.out.println("Publication atualizada!");
			}
			//REMOVE(Remover:TestePubv2)
			else if(tokens[0].compareTo("Remover")==0) {
				RemovePublication(tokens[1]);
				System.out.println("Publica��o removida!");
			}
			else {
				System.out.println("Something went wrong with parsing! :(");
			}
		}
}
