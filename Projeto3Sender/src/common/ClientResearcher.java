package common;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import classes.AsyncReceiver;
import classes.Sender;
import classes.User;
import classes.Publication;

public class ClientResearcher {
	@PersistenceContext(name = "Loader")
	private EntityManager em;
	public static void main(String[] args) throws NamingException {
		String username,password;
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
				/*System.out.println("****************Register JMS****************");
				System.out.print("Escolha um username: ");
			    username = scanner.nextLine();  // Read user input
				System.out.print("Escolha uma password: ");
				password = scanner.nextLine();  // Read user input
				User utilizador = new User(username,password);*/
				//Enviar msg ao admin para confirmar registo
				sendmessage("SEND");
			}
			
			//LOGIN
			if(option==1) {
				/*System.out.println("****************Login JMS****************");
				System.out.print("Username: ");
			    username = scanner.nextLine();  // Read user input
				System.out.print("Password: ");
				password = scanner.nextLine();  // Read user input
				System.out.println("\n");*/
				//Enviar msg ao admin para login
				printresponse();
				//Ap�s confirmar aceder � app
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
		public void appuser() throws NamingException {
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
					System.out.print("Nome da publicacao: ");
				    bookname = scanner.nextLine();  // Read user input
				    System.out.print("Tipo da publicacao: ");
				    type = scanner.nextLine();  // Read user input
				    System.out.print("Data da publicacao (Month Year): ");
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
		public void appadmin() throws NamingException {
			boolean done  = false;
		    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			String user,bookname;
			//criar queues
			
			
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
		
		//RECEIVE A MESSAGE
		public static void printresponse() throws NamingException {
			AsyncReceiver asyncReceiver = new AsyncReceiver();
			asyncReceiver.launch_and_wait();
		}
		
		//RECEIVE AND REPLY
		public void printandrespond() throws NamingException, JMSException {
			AsyncReceiver asyncReceiver = new AsyncReceiver();
			asyncReceiver.receive_and_reply();
		}
		
		
		//SEND A MESSAGE
		public static void sendmessage(String message) throws NamingException {
			Sender sender = new Sender();
			sender.send(message);
		}
		
		//SEND MESSAGE AND PRINT RESPONSE
		public void sendandreceive(String message) throws NamingException {
			Sender sender = new Sender();
			sender.send_and_reply(message);
		}
		
		//GET ALL PUBS
		public List<Publication> Getall(){
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
}