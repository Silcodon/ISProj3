package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import classes.AppUser;
import classes.AsyncReceiver;
import classes.Publication;
import classes.Sender;

public class ClientAdmin {
	//MENU DE ADMIN
			public static void main(String[] args) throws NamingException {
				boolean done  = false;
			    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			    ArrayList<String> tasks = new ArrayList<String>();
			    tasks=ReadFile();
				String AppUser,bookname;
				//criar queues

				


				while(!done) {
					System.out.println("****************JMS App****************");
					System.out.println("Escolha uma opcao: ");
					System.out.println("(0) List AppUsers ");
					System.out.println("(1) List Pending Tasks ");
					System.out.println("(2) Deactivate AppUser ");
					System.out.println("(3) List Publications ");
					System.out.println("(4) Search Publication ");
					System.out.println("(5) Logout ");
					System.out.println("\n");

					int option = lerInt(0, 6);

					//LIST ALL AppUserS
					if(option==0) {
						printallAppUsers();
					}


					//LIST PENDING TASKS
					if(option==1) {
						String task="";
						AsyncReceiver asyncReceiver = new AsyncReceiver("queue/AddQueue");
						while (task!=null) {
							task = asyncReceiver.receive();
							if(task!=null) {
								tasks.add(task);	
							}
						}
						System.out.println("****************TASKS JMS****************");
						PrintTasks(tasks);
						if(tasks.size()==0) {
							System.out.println("No tasks!");
						}
						System.out.print("Choose a pending task (Type 0 to exit): ");
						int option2=lerInt(0,tasks.size()+1);
						if(option2!=0) {
							SelectTask(tasks,option2-1);
						}
						//Se não fizer nada voltar a mandar para a queue para não se perderem e outro admin puder executa-las
						for(int j=0;j<tasks.size();j++) {
							Sender sender=new Sender("queue/AddQueue");
							sender.send(tasks.get(j));
						}
						tasks.clear();
						
					}

					//DEACTIVATE AppUser
					if(option==2) {
						System.out.println("****************DEACTIVATE JMS****************");
						System.out.print("Nome do AppUser: ");
					    AppUser = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
						ActivateAppUser(GetAppUser(AppUser).get(0));
						System.out.println("O User foi desativado!");
					}

					//LIST PUBLICATIONS
					if(option==3) {
						printallpubs(GetallPubs());
					}

					//SEARCH PUBLICATION
					if(option==4) {
						System.out.println("****************SEARCH JMS****************");
						System.out.print("Nome da publicacao: ");
					    bookname = scanner.nextLine();  // Read AppUser input
						System.out.println("\n");
						printallpubs(GetPublicationByNome(bookname));
					}

					//EXIT
					if(option==5) {
						System.out.println("You have successfully logged out.");
		                done = true;
					}
				}
			}
			
//=========================AUX=============================================================

			
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
			
//=========================GET DATABASE INFO=============================================================



			//GET ALL AppUserS
			public static List<AppUser> GetallAppUsers(){
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager em = emfactory.createEntityManager( );
			    // Define query String
			    String jpql = "SELECT u FROM AppUser u";
			    // Create a (typed) query
			    TypedQuery<AppUser> typedQuery = em.createQuery(jpql, AppUser.class);
			    // Query and get result
			    List<AppUser> mylist = typedQuery.getResultList();
			    return mylist;
			 }
			
			//GET AppUser By name
			public static List<AppUser> GetAppUser(String nome){
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager em = emfactory.createEntityManager( );
			    // Define query String
			    String jpql = "SELECT u FROM AppUser u where u.username=:name";
			    // Create a (typed) query
			    TypedQuery<AppUser> typedQuery = em.createQuery(jpql, AppUser.class);
			    // Set parameter
			 	typedQuery.setParameter("name", nome);
			    // Query and get result
			    List<AppUser> mylist = typedQuery.getResultList();
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
			public static List<Publication> GetPublicationByNome(String nome){
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

			//ADD AppUser
			public static void AddAppUser(AppUser st) {
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager entitymanager = emfactory.createEntityManager( );
				entitymanager.getTransaction( ).begin( );
				List<AppUser> mylist = GetallAppUsers();
				if (mylist.size()==0) {
					st.setAdmin(true);
				}
				entitymanager.persist(st);
				entitymanager.getTransaction( ).commit( );
				entitymanager.close( );
				emfactory.close( );
			}

			//ADD PUBLICATION
			public static void AddPublication(Publication st) {
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager entitymanager = emfactory.createEntityManager( );
				entitymanager.getTransaction( ).begin( );
				entitymanager.persist(st);
				entitymanager.getTransaction( ).commit( );
				entitymanager.close( );
				emfactory.close( );
			}

			//UPDATE PUBLICATION
			public static void UpdatePublication(Publication st, String old) {
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager em = emfactory.createEntityManager( );
			    em.getTransaction().begin();
			    
			    if(st.getType().compareTo("")!=0) {
			    	Query query = em.createQuery("UPDATE Publication p SET p.type =: tipo "
				            + "WHERE p.name =: oldname");
			    	query.setParameter("tipo", st.getType());
				    query.setParameter("oldname", old);
				    query.executeUpdate();
			    }
			    if(st.getDate().compareTo("")!=0) {
			    	Query query = em.createQuery("UPDATE Publication p SET p.date =: data "
				            + "WHERE p.name =: oldname");
			    	query.setParameter("data", st.getDate());
				    query.setParameter("oldname", old);
				    query.executeUpdate();
			    }
			    if(st.getName().compareTo("")!=0) {
			    	Query query = em.createQuery("UPDATE Publication p SET p.name =: nome "
				            + "WHERE p.name =: oldname");
			    	query.setParameter("nome", st.getName());
				    query.setParameter("oldname", old);
				    query.executeUpdate();
			    }
			    em.getTransaction().commit();
			    em.close();
			}

			//REMOVE A PUBLICATION
			public static void RemovePublication(String pubname) {
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager em = emfactory.createEntityManager( );
				em.getTransaction().begin();
			    Query query = em.createQuery("DELETE FROM Publication p WHERE p.name = :Name ");
			    query.setParameter("Name", pubname);
			    query.executeUpdate();
			    em.getTransaction().commit();
			    em.close();
			}

			//ACTIVATE OR DEACTIVATE A AppUser
			public static void ActivateAppUser(AppUser st) {
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager em = emfactory.createEntityManager( );
				em.getTransaction().begin();
				if(st.isActivated()==false) {
					Query query = em.createQuery("UPDATE AppUser u SET u.activated = TRUE "
				            + "WHERE u.AppUsername = :Name");
				    query.setParameter("Name", st.getUsername());
				    query.executeUpdate();
				}
				else {
					Query query = em.createQuery("UPDATE AppUser u SET u.activated = FALSE "
				            + "WHERE u.AppUsername = :Name");
				    query.setParameter("Name", st.getUsername());
				    query.executeUpdate();
				}

			    em.getTransaction().commit();
			    em.close();
			}

			//LOGIN
			public List<AppUser> login(String AppUsername, String password) {
				EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Loader" );
				EntityManager em = emfactory.createEntityManager( );
				// Define query String
				String jpql = "SELECT u FROM AppUser u where u.username=:name AND u.password=:pass";
				// Create a (typed) query
				TypedQuery<AppUser> typedQuery = em.createQuery(jpql, AppUser.class);
				// Set parameter
				typedQuery.setParameter("name", AppUsername);
				typedQuery.setParameter("pass", password);
				// Query and get result
				List<AppUser> mylist = typedQuery.getResultList();
				return mylist;
				

			}


	//=========================PRINT DATABASE INFO=============================================================
			//PRINT ALL AppUserS INFO
			public static void printallAppUsers() {
				List<AppUser> mylist = GetallAppUsers();
				for(int i=0;i<mylist.size();i++) {
					System.out.println("Utilizador " + (i+1) +":");
					System.out.println("AppUsername: " + mylist.get(i).getUsername());
					System.out.println("Activated: " + String.valueOf(mylist.get(i).isActivated()));
					System.out.println("\n");
				}
				System.out.println("\n\n");
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
			public static void PrintTasks(ArrayList<String> tasks) {
				for(int i=0;i<tasks.size();i++) {
					String[] tokens = tasks.get(i).split(":");
					System.out.println("("+(i+1)+") " + tokens[0] + ": " + tokens[1]);
				}
			}

			//SELECT A TASK
			//RETURNS ARRAYLIST WITH DECLINED TASK OR ACCEPTED
			public static ArrayList<String> SelectTask(ArrayList<String> tasks, int choice) throws NamingException {
			    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
			    boolean done=false;
			    Sender a = null;
				System.out.println("Task selected: " + tasks.get(choice));
				while(!done) {
					System.out.println("Do you want to accept or decline?");
					System.out.println("Type 'exit' to cancel");
				    String answer = scanner.nextLine();  // Read AppUser input
					if(answer.compareTo("accept")==0) {
						a = new Sender("topic/playTopic");
						DoTask(tasks.get(choice));
						
						
						if(tasks.get(choice).startsWith("Registo")) {
							a.note_ALL("USER REGISTADO");
						}else {
							a.note_ALL( tasks.get(choice) + " ACEITE PELO ADMIN!\n");
						}
						tasks.remove(choice);
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
			public static void DoTask(String task) {
				String[] tokens = task.split(":");
				//REGISTAR(Registo:AppUsername:Password)
				if(tokens[0].compareTo("Registo")==0) {
					AppUser novo= new AppUser(tokens[1],tokens[2]);
					AddAppUser(novo);
					System.out.println("Utilizador " + tokens[1] + " adicionado com sucesso!");
					//Mandar mensagem de volta ao AppUser
				}
				//ADICIONAR(Adicionar:TestePub:Book:March 2013)
				else if(tokens[0].compareTo("Adicionar")==0) {
					Publication novo = new Publication(tokens[1],tokens[2],tokens[3]);
					AddPublication(novo);
					System.out.println("Publication " + tokens[1] + " adicionada com sucesso!");
					//Mandar notificacao para todos
				}
				//UPDATE(Update:TestePub:TestePubv2:Book:March 2014)
				else if(tokens[0].compareTo("Update")==0) {
					Publication updated=new Publication(tokens[2],tokens[3],tokens[4]);
					String oldname=tokens[1];
					UpdatePublication(updated,oldname);
					System.out.println("Publication atualizada!");
					//Mandar notificacao para todos
				}
				//REMOVE(Remover:TestePubv2)
				else if(tokens[0].compareTo("Remover")==0) {
					RemovePublication(tokens[1]);
					System.out.println("Publication removida!");
					//Mandar notificacao para todos
				}
				else {
					System.out.println("Something went wrong with parsing! :(");
				}
			}
	}



