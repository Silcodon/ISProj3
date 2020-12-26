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
import classes.AppUser;

public class ClientResearcher{

	public static void main(String[] args) throws NamingException, InterruptedException {
		String AppUsername,password;
		boolean validAppUsername,validpassword;
	    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		boolean done  = false;
		List<AppUser> user2log;
		Sender playQueue = new Sender();
		
		
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
				System.out.print("Escolha um Username: ");
			    AppUsername = scanner.nextLine();  // Read AppUser input
				System.out.print("Escolha uma password: ");
				password = scanner.nextLine();  // Read AppUser input
				System.out.println("\n");
				validAppUsername = (AppUsername != null) && AppUsername.matches("[A-Za-z0-9_]+");
				validpassword = (password != null) && password.matches("[A-Za-z0-9_]+");
				if(validAppUsername==false || validpassword==false) {
					System.out.println("Please enter a valid Username and password");
				}
				else {
					//AppUser utilizador = new AppUser(AppUsername,password);
					//Enviar msg ao admin para confirmar registo
					Sender sender = new Sender("queue/AddQueue");
					System.out.println("Aguarde pela confirmacao do admin para efetuar login!");
					sender.send_and_reply("Registo:"+AppUsername+":"+password);
					
				}

			}

			//LOGIN
			if(option==1) {
				System.out.println("****************Login JMS****************");
				System.out.print("AppUsername: ");
			    AppUsername = scanner.nextLine();  // Read AppUser input
				System.out.print("Password: ");
				password = scanner.nextLine();  // Read AppUser input
				System.out.println("\n");
				//Enviar msg ao admin para login
				user2log = playQueue.login(AppUsername,password);
				
				if(user2log!=null) {
					if(user2log.size()==0) {
						System.out.println("Username ou password incorretos");
						
					}
					
					else if(!user2log.get(0).isAdmin()){
						
						appAppUser(user2log.get(0));
					}else {
						
						//System.out.println("olha um admin");
						//new ClientAdmin();
						
					}
				}
				
				
			}

			//EXIT
			if(option==2) {
				System.out.println("You have successfully exited the Application.");
                done = true;
			}
		}
	}



		//MENU DE AppUser
		public static void appAppUser(AppUser user) throws NamingException, InterruptedException {
			boolean done  = false;
		    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			String bookname,type,date;

			Sender playQueue = new Sender();
			NotThread thread=new NotThread(user.getUsername());
			thread.start();
			List<Publication> aux = null;
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
					if(playQueue.Verificaativo(user.getUsername(), user.getPassword())) {
						printallpubs(playQueue.getPublications());
					}
					else {
						System.out.println("Você foi desativado pelo administrador!");
					}
					
				}


				//SEARCH PUBLICATION BY NAME
				if(option==1) {
					System.out.println("****************SEARCH JMS****************");
					if(playQueue.Verificaativo(user.getUsername(), user.getPassword())) {
						System.out.print("Nome da publicacao: ");
					    bookname = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
						if(!bookname.equals("")) {
							aux =playQueue.getPublicationByTitle(bookname);
							if(aux.size()>=1) {
								printallpubs(aux);
							}else {
								System.out.println("Não existe essa publicação na base de dados");
							}
						}
						
					}
					else {
						System.out.println("Você foi desativado pelo administrador!");
					}
					
				}

				//ADD PUBLICATION
				if(option==2) {
					System.out.println("****************ADD JMS****************");
					if(playQueue.Verificaativo(user.getUsername(), user.getPassword())) {
						System.out.print("Nome da publicacao: ");
					    bookname = scanner.nextLine();  // Read AppUser input
					    System.out.print("Tipo da publicacao: ");
					    type = scanner.nextLine();  // Read AppUser input
					    System.out.print("Data da publicacao (Month Year): ");
					    date = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
						//Enviar mensagem a pedir info ao admin
						//Adicionar:TestePub:Book:March 2013
						if(bookname.equals("") || type.equals("") || date.equals("")) {
							System.out.println("Um dos parametros é inválido!");
						}
						else {
							Sender sender = new Sender("queue/AddQueue");
							sender.send("Adicionar:"+bookname+":"+type+":"+date);	
						}
					}
					else {
						System.out.println("Você foi desativado pelo administrador!");
					}
				}

				//UPDATE PUBLICATION
				if(option==3) {
					System.out.println("****************UPDATE JMS****************");
					if(playQueue.Verificaativo(user.getUsername(), user.getPassword())) {
						System.out.print("Nome da publicacao a alterar: ");
						String booknameold=scanner.nextLine();
						System.out.print("Novo nome: ");
					    bookname = scanner.nextLine();  // Read AppUser input
					    System.out.print("Novo tipo: ");
					    type = scanner.nextLine();  // Read AppUser input
					    System.out.print("Nova Data (Month Year): ");
					    date = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
						//Enviar mensagem a pedir info ao admin
						//Update:TestePub:TestePubv2:Book:March 2014
						Sender sender = new Sender("queue/AddQueue");
						sender.send("Update:"+booknameold+":"+bookname+":"+type+":"+date);
					}
					else {
						System.out.println("Você foi desativado pelo administrador!");
					}
				}

				//REMOVE PUBLICATION
				if(option==4) {
					System.out.println("****************REMOVE JMS****************");
					if(playQueue.Verificaativo(user.getUsername(), user.getPassword())) {
						System.out.print("Nome da publicacao: ");
					    bookname = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
						//Enviar mensagem a pedir info ao admin
						//Remover:TestePubv2
						if(!bookname.equals("")) {
							Sender sender = new Sender("queue/AddQueue");
							sender.send("Remover:"+bookname);	
						}
					}
					else {
						System.out.println("Você foi desativado pelo administrador!");
					}
				}

				//EXIT
				if(option==5) {
					System.out.println("You have successfully logged out.");
	                done = true;
	                thread.interrupt();
				}
			}
		}



		


//=========================PRINT DATABASE INFO=============================================================

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
}
