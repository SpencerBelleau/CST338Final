//Simple singleton database
package edu.sbelleau.bookholdsystem;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

@SuppressLint({ "SimpleDateFormat", "UseSparseArrays" })
public class Database {

	//DBO declarations for making the Hashmap
	abstract class DBO{
		@SuppressWarnings("unused")
		private int type;
		public abstract int getType();
	}
	class user extends DBO{
		public String name, password;
		private int type = 0;
		protected user()
		{
			name = "";
			password = "";
		}
		public user(String name, String password){
			this.name = name;
			this.password = password;
		}
		public boolean equals(user u) {
			if(u.name.equals(this.name) && u.password.equals(this.password))
			{
				return true;
			}
			return false;
		}
		@Override
		public int getType() {
			return this.type;
		}
	}
	
	class admin extends user
	{
		private int type = 3;
		protected admin()
		{
			super();
		}
		public admin(String username, String password)
		{
			super(username, password);
		}
		@Override
		public int getType() {
			return this.type;
		}
	}
	
	class book extends DBO{
		public String title, author, ISBN;
		public double fee;
		public boolean hold = false;
		public Date start = new Date(), end = new Date();
		public DBO holder = new user("none", "none");
		public int reservationNumber = -1;
		private int type = 1;
		public book(String title, String author, String ISBN, double fee, boolean hold, Date start, Date end, user holder, int reservationNumber)
		{
			this.title = title;
			this.author = author;
			this.ISBN = ISBN;
			this.fee = fee;
			this.hold = hold;
			this.start = start;
			this.end = end;
			this.holder = holder;
			this.reservationNumber = reservationNumber;
		}
		public book(String title, String author, String ISBN, double fee)
		{
			this.title = title;
			this.author = author;
			this.ISBN = ISBN;
			this.fee = fee;
		}
		
		@Override
		public int getType() {
			return this.type;
		}
		
	}
	
	class log extends DBO{
		public String contents;
		public String event;
		public Date logDate;
		private int type = 2;
		public log(String event, String contents)
		{
			this.event = event;
			this.contents = contents;
			this.logDate = new Date(); //current time
		}
		public String getLogMessage()
		{
			SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyy hh:mm a");
			return event + "\n" + contents + "\n" + "Transaction Date/Time: " + dt.format(logDate);
		}
		@Override
		public int getType() {
			return type;
		}
		
	}
	
	class compareLogDates implements Comparator<log> {
	    @Override
	    public int compare(log l1, log l2) {
	        return l1.logDate.compareTo(l2.logDate);
	    }
	}
	
	
	
	//end DBO declarations
	private HashMap<Integer, DBO> table = new HashMap<Integer, DBO>();
	private static int DBOID = 0, reservationNumber = 1;
	private static Database db;
	
	private Database(){
			
	}
	
	public static Database getInstance()
	{
		if(db==null)
		{
			System.out.println("db is null, creating new");
			db = new Database();
			//Initialization Values
			db.addAdmin("!admin2", "!admin2");
	        db.addUser("$brian7", "123abc##");
	        db.addUser("a@lice5", "@csit100");
	        db.addBook("Hot Java", "S. Narayanan", "123-ABC-101", 0.05);
	        db.addBook("Fun Java", "Y. Byun", "ABCDEF-09", 1.00);
	        db.addBook("Algorithm for Java", "K. Alice", "CDE-777-123", 0.25);
		}
		return db;
	}
	
	public DBO getByHashId(int hashID)
	{
		return table.get(hashID);
	}
	
	
	public boolean userExists(String s)
	{
		for(int i : table.keySet())
		{
			if(table.get(i).getType() == 0)
			{
				user u = (user)table.get(i);
				if(u.name.equals(s))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getReservationNumber()
	{
		return reservationNumber;
	}
	
	public void addAdmin(String username, String password)
	{
		db.table.put(DBOID, new admin(username, password));
		DBOID++;
		logEvent("Transaction type: New Admin", "username: " + username);
	}
	
	public void addUser(String username, String password)
	{
		db.table.put(DBOID, new user(username, password));
		DBOID++;
		logEvent("Transaction type: New User", "Customer's Username: " + username);
	}
	
	public int login(String un, String pw)
	{
		for(int i : table.keySet())
		{
			if(table.get(i).getType() == 0)
			{
				user u = (user) table.get(i);
				if(u.name.equals(un) && u.password.equals(pw))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public int loginAdmin(String un, String pw)
	{
		for(int i : table.keySet())
		{
			if(table.get(i).getType() == 3)
			{
				admin u = (admin) table.get(i);
				if(u.name.equals(un) && u.password.equals(pw))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public void addBook(String title, String author, String ISBN, double fee)
	{
		db.table.put(DBOID,  new book(title, author, ISBN, fee));
		DBOID++;
		logEvent("Transaction type: New Book", "title: " + title + "\nauthor:" + author + "\nISBN:" + ISBN + "\nfee:" + fee);
	}
	
	
	private void logEvent(String event, String contents)
	{
		db.table.put(DBOID, new log(event, contents));
		DBOID++;
	}
	
	
	public String getAllLogs() //Sorted by date
	{
		String t = "";
		ArrayList <log> logs = new ArrayList<log>();
		for(int i : table.keySet())
		{
			if(table.get(i).getType() == 2)
			{
				logs.add((log)table.get(i));
			}
		}
		
		Collections.sort(logs, new compareLogDates());
		
		for(int i = 0; i < logs.size(); i++)
		{
			t = t + logs.get(i).getLogMessage() + "\n-------------------\n";
		}
		
		return t;
	}
	
	public void placeHold(book B, Date d1, Date d2, user holder)
	{
		if((d2.getTime() - d1.getTime()) >= (24*60*60*1000*7)) //7 days
		{
			//Error
			return;
		}
		if(B.hold)
		{
			//Error
			return;
		}
		SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyy hh:mm a");
		B.start = d1;
		B.end = d2;
		B.hold = true;
		B.holder = holder;
		B.reservationNumber = reservationNumber;
		logEvent("Transaction type: New hold", "Customer's Username: " + holder.name + "\nBook title: " + B.title + "\nPickup date/time: " + dt.format(d1) + "\nReturn date/time: " + dt.format(d2) + "\nReservation Number: " + reservationNumber);
		reservationNumber++;
	}
	
	//100% not finished
	public String displayHolds(user holder)
	{
		String heldBooks = "";
		for (int i : table.keySet())
		{
			if (table.get(i).getType() == 1)
			{
				if (((user)((book)table.get(i)).holder).equals(holder)) //loads of casts and parentheses
				{
					heldBooks = heldBooks + ":" + ((book)(table.get(i))).title;
				}
			}
		}
		return heldBooks;
	}
	
	public boolean bookAvailable(book b, Date d1)
	{
		if(b.hold==false)
			return true;
		//if(b.end.getTime() < d1.getTime())
			//return true;
		return false;
	}
	
	public String booksAvailable(Date d1, Date d2)
	{
		String availableBooks = "";
		for (int i : table.keySet())
		{
			if (table.get(i).getType() == 1)
			{
				if(bookAvailable((book)(table.get(i)),  d1))
				{
					availableBooks = availableBooks + ":" + ((book)(table.get(i))).title;
				}
			}
		}
		return availableBooks;
	}
	
	public book getBookByTitle(String title)
	{
		for (int i : table.keySet())
		{
			if (table.get(i).getType() == 1)
			{
				if(((book)(table.get(i))).title.equals(title))
				{
					return (book)(table.get(i));
				}
			}
		}
		return null;
	}
	
	public user getUserByName(String name)
	{
		for (int i : table.keySet())
		{
			if (table.get(i).getType() == 0)
			{
				if(((user)(table.get(i))).name.equals(name))
				{
					return (user)(table.get(i));
				}
			}
		}
		return null;
	}
	
	public void cancelHold(book B, user holder)
	{
		if(B.holder.equals(holder))
		{
			SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyy hh:mm a");
			logEvent("Transaction type: Cancel hold", "Customer's Username: " + holder.name + "\nBook title: " + B.title + "\nPickup date/time: " + dt.format(B.start) + "\nReturn date/time: " + dt.format(B.end) + "\nReservation Number: " + B.reservationNumber);
			B.holder = new user("none", "none");
			B.hold = false;
			B.start = null;
			B.end = null;
			B.reservationNumber = -1;
		}else{
			//Error
		}
	}
	
	public void listUsers()
	{
		for (int i : table.keySet())
		{
			if (table.get(i).getType() == 0)
			{
				System.out.println(((user)table.get(i)).name);
			}
		}
		System.out.println("Users Listed");
	}
	
	
	
	public user getUserByUsername(String username)
	{
		for(int i : table.keySet())
		{
			if(table.get(i).getType() == 0)
			{
				if(((user)table.get(i)).name.equals(username))
				{
					return (user)table.get(i);
				}
			}
		}
		return new user("none", "none");
	}
	
	/*
	 * Import and Export functions
	 * Exports database to a pseudo CSV file
	 * Doesn't seem to work on Droid for some reason, probably a filesystem issue
	 * Originally the database could be successfully exported and imported from a file
	 */
	
	/*
	private void serializeDatabase()
	{
		//Terrible awful hacky nonsense to make sure the database doesn't expand infinitely
		try {
			File database = new File("C:/database.sav");
			database.delete();
			database = new File("C:/database.sav");
			database.createNewFile();
		} catch (IOException e1) {
			//e1.printStackTrace();
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter("C:/database.sav");
			for(int i : table.keySet())
			{
				DBO object = table.get(i);
				if(object.getType() == 0)
				{
					user currentObject = (user) object;
					writer.println("user," + currentObject.name + "," + currentObject.password);
				}
			}
			for(int i : table.keySet())
			{
				DBO object = table.get(i);
				if (object.getType() == 1){
					book currentObject = (book) object;
					writer.println("book," + currentObject.title + "," + currentObject.author + "," + currentObject.ISBN + "," + currentObject.fee + "," + currentObject.hold + "," + currentObject.start.getTime() + "," + currentObject.end.getTime() + "," + ((user)currentObject.holder).name);
				}
			}
			for(int i : table.keySet())
			{
				DBO object = table.get(i);
				if (object.getType() == 2){
					log currentObject = (log) object;
					writer.println("log," + currentObject.event + "," + currentObject.contents);
				}
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void importDatabase()
	{
		try {
			File savedDatabase = new File("C:/database.sav");
			Scanner importer = new Scanner(savedDatabase);
			importer.useDelimiter(",|(\r\n|\n)");
			while(importer.hasNext())
			{
				String type = importer.next();
				if(type.equals("user"))
				{
					addUserWithoutSerializing(importer.next(), importer.next());
				}else if(type.equals("book")){
					addBookComplete(importer.next(), importer.next(), importer.next(), importer.nextDouble(), importer.nextBoolean(), new Date(importer.nextLong()), new Date(importer.nextLong()), getUserByUsername(importer.next()));
				}else if(type.equals("log")){
					logEventWithoutSerializing(importer.next(), importer.next());
				}else{
					System.out.println("I don't even");
				}
			}
			importer.close();
			System.out.println("database imported");
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	*/
}
