import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Inventory {
	
	//Inventory Data Structures
    ArrayList<String> InventoryBook = new ArrayList<String>();
    ArrayList<Integer> NumBook = new ArrayList<Integer>();
    
    //Name to book and id data structure
    HashMap<String, ArrayList<String>> StudentBooks = new HashMap<>();
    
    //ID to Book data structure(index relates to id)
    ArrayList<String> BookID = new ArrayList<String>();
    //ID toe name data structure
    ArrayList<String> NameID = new ArrayList<String>();
    
    int recordId=1;
    
    public Inventory()
    {
    	BookID.add("");
    	NameID.add("");
    }
    
	public void parseInventory(String fileName) throws FileNotFoundException
	{
	    File file = new File(fileName);
	    Scanner sc = new Scanner(file);
	    while (sc.hasNextLine()) 
	    {
	    	String line = sc.nextLine();
	    	String name = line.substring(0, line.lastIndexOf("\"")+1);
	    	InventoryBook.add(name);
	    	String num = line.substring(line.lastIndexOf("\"")+2,line.length());
	    	NumBook.add(Integer.valueOf(num));
	    }	    
	}
	
	
	public synchronized String borrowBook(String name, String book)
	{
		String response = "";
		if(InventoryBook.contains(book) && NumBook.get(InventoryBook.indexOf(book))==0)
			response="Request Failed - Book not available";
		else if(!InventoryBook.contains(book))
			response="Request Failed - We do not have this book";
		else {
			response="Your request has been approved, "+recordId+" "+name+" "+book;
			BookID.add(book);
			NameID.add(name);
			NumBook.set(InventoryBook.indexOf(book), NumBook.get(InventoryBook.indexOf(book))-1);
			
			//Keep track of books per student
			if(StudentBooks.containsKey(name)) {
				ArrayList<String> books = StudentBooks.get(name);
				books.add(recordId+"");
				books.add(book);
				StudentBooks.put(name, books);
			}
			else{
				ArrayList<String> books = new ArrayList<>();
				books.add(recordId+"");
				books.add(book);
				StudentBooks.put(name, books);
			}
			recordId++;
		}
		return response;
	}
	
	public synchronized String returnBook(String recId)
	{
		//NEED MORE ERROR CHECKING OF ID VALUE
		int id = Integer.valueOf(recId);
		String response;
		if(id<recordId && id>0 && !BookID.get(id).equals("$done"))
		{
			int bookIndex = InventoryBook.indexOf(BookID.get(id));
			BookID.set(id,"$done");
			NumBook.set(bookIndex, NumBook.get(bookIndex)+1);
			//NEED TO UPDATE LIST BORROWED???
			ArrayList<String> books = StudentBooks.get(NameID.get(id));
			System.out.println(books);
			for(int i=0;i<books.size();i++)
			{
				if(books.get(i).equals(recId))
				{
					books.remove(i+1);
					books.remove(i);
				}
			}
			
			response = recId+" is returned";
		}
		else
			response = recId+" not found, no such borrow record";
		return response;
	}
	
	public synchronized String listBorrowed(String name)
	{
		String response = "";
		if(StudentBooks.containsKey(name))
		{
			ArrayList<String> books = StudentBooks.get(name);
			for(int i=0;i<books.size();i=i+2)
			{
				if(i!=books.size()-2)
					response = response + books.get(i) + " " + books.get(i+1)+"\n";
				else
					response = response + books.get(i) + " " + books.get(i+1);
			}
		}
		else
			response = "No record found for "+name;
		
		return response;
	}
	
	public synchronized String listAvailable()
	{
		String response="";
		for(int i=0;i<InventoryBook.size();i++)
		{
			if(i!=InventoryBook.size()-1)
				response=response+InventoryBook.get(i)+ " " + NumBook.get(i)+"\n";
			else
				response=response+InventoryBook.get(i)+ " " + NumBook.get(i);
		}
		return response;
	}
	
	
	
	
}
