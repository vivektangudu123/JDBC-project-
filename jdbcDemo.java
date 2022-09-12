//STEP 1. Import required packages
import java.time.*;
import java.sql.*;
import java.io.*; 
import java.util.*; 
import java.lang.*; 
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;  
import java.sql.SQLException;
 

public class jdbcDemo {

//Set JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
//static final String DB_URL = "jdbc:mysql://localhost/companydb";
   static final String DB_URL = "jdbc:mysql://localhost:3306/test1?allowPublicKeyRetrieval=true&useSSL=false";
//  Database credentials
   static final String USER = "root";// add your user 
   static final String PASS = "admin";// add password
   

   static int oper_id_c=10;
   static int ssn_id_c=10;
   static int plan_id_c=10;
   static int sim_id_c=10;
   static int sim_and_cus_id_c=10;

   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;


// STEP 2. Connecting to the Database
   try{
      //STEP 2a: Register JDBC driver
      Class.forName(JDBC_DRIVER);
      //STEP 2b: Open a connection
      System.out.println("Connecting to database...");
      
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      //STEP 2c: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      Scanner input = new Scanner(System.in);
      main_menu2(stmt,input,conn);

      stmt.close();
      conn.close();
	}catch(SQLException se){    	 //Handle errors for JDBC
      	se.printStackTrace();
   	}catch(Exception e){        	//Handle errors for Class.forName
      e.printStackTrace();
   }finally{				//finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }					//end finally try
   }					//end try
   System.out.println("End of Code");
}					//end main
				//end class


static void main_menu2(Statement stmt,Scanner input,Connection conn)
{
   while(1!=0){
   System.out.println("\nLogin as:");
   System.out.println("1: Government Officer");
   System.out.println("2: Network Operator");
   System.out.println("3: Local center");
   System.out.println("0: Exit");
   int choice = Integer.parseInt(input.nextLine());
   clearScreen();

   if(choice==1)
   {
      govern(stmt,input,conn);
   }
   else if(choice ==2)
   {
      oper(stmt,input,conn);
   }
   else if(choice==3)
   {
      local(stmt,input,conn);
   }
   else if(choice==0)
   {
      return;
   }
}
}
static void govern(Statement stmt,Scanner input,Connection conn)
{
   while(1!=0){
   System.out.println("Choice: ");
   System.out.println("1. Add people");
   System.out.println("2. Add Network");
   System.out.println("3. Delete a Network");
   System.out.println("0. Logout");
   int choice = Integer.parseInt(input.nextLine());
   clearScreen();
   if(choice==1)
   {
      add_people(stmt,input);
   }
   else if(choice==2)
   {
      add_network(stmt,input);
   }
   else if(choice==3)
   {
      delete_network(stmt,input);
   }
   else if(choice==0)
   {
      return;
      //main_menu2(stmt,input,conn);
   }
}
}

static void local(Statement stmt,Scanner input,Connection conn)
{
   System.out.println("Choice: ");
   System.out.println("1. Recharge");
   System.out.println("2. New Sim");
   System.out.println("3. Change Phone_number");
   System.out.println("4. Update SSN details");
   System.out.println("0. Exit");
   
   int choice = Integer.parseInt(input.nextLine());
   clearScreen();
   if(choice==1)
   {
      recharge(stmt,input,conn);
   }
   else if(choice==2)
   {
      new_sim(stmt,input,conn);
   }
   else if(choice==3)
   {
      change_phone_nummber(stmt,input);
   }
   else if(choice==4)
   {
      update_ssn_details(stmt,input);
   }
   else if(choice==0)
   {
      return;
      //main_menu2(stmt,input,conn);
   }
}
static void oper(Statement stmt,Scanner input,Connection conn)
{
   System.out.println("Choice: ");
   System.out.println("1. Add plans to a Network");
   System.out.println("2. Delete a phone_number");
   System.out.println("0. Logout");
   
   int choice = Integer.parseInt(input.nextLine());
   clearScreen();
   if(choice==1)
   {
      add_plan(stmt,input);
   }
   else if(choice==2)
   {
      delete_phone(stmt,input);
   }
   else if(choice==0)
   {
      return;
      //main_menu2(stmt,input,conn);
   }
}
static void delete_phone(Statement stmt,Scanner input)
{
   System.out.println("Enter Phone_number:");
   String choice = input.nextLine();
   String sql=String.format("select phone_number,sim_id from sim_details");
   ResultSet rs=executeSqlStmt(stmt,sql);
   String sim="";
   int vo=0;
   try{
      while(rs.next())
      {
         String num1=rs.getString("phone_number");
         sim=rs.getString("sim_id");
         if(num1.equals(choice))
         {
            vo++;
            break;
         } 
      }
      if(vo==0){
      System.out.println("Phone number is invalid\nTry again");
      return;
      }
   }
   catch(SQLException e){

  }
   sql=String.format("DELETE from sim_and_cus where simm_id= %s ",sim);
   int result=updateSqlStmt(stmt,sql);
   sql=String.format("DELETE from sim_details where sim_id= %s",sim);
   int result1=updateSqlStmt(stmt,sql); 
   if(result!=0  && result1!=0)
   {
      System.out.println("sim details are deleted");
   }
   else{
      System.out.println("Went Wrong!!");
   }
   return;
}
static void delete_network(Statement stmt,Scanner input)
{
   list_of_Network(stmt);

   System.out.println("Enter the network id to delete:");
   String ope_id=input.nextLine();

   String sql=String.format("select ope_id,sim_id from sim_details;");
   ResultSet rs=executeSqlStmt(stmt,sql);

   int[] arr = new int[20];
   int co=0,result;

   try{
      while(rs.next())
      {
         String num1=rs.getString("ope_id");
         int sim=rs.getInt("sim_id");
         if(num1.equals(ope_id))
         {
            arr[co]=sim;
            co++;
         } 
      }
   }
   catch(SQLException e){

  }


  int cot=0;
  for(int i=0;i<co;i++)
   {
      sql=String.format("DELETE from sim_and_cus where simm_id= %s",arr[i]);
      result=updateSqlStmt(stmt,sql);
      if(result!=0)
      {
         cot++;
      }
   }  
   System.out.println(cot+ " sim_and_cus deleted");


   if(cot!=0){
   sql=String.format("DELETE from sim_details where ope_id= %s",ope_id);
   result=updateSqlStmt(stmt,sql); 
   if(result!=0)
   {
      System.out.println("sim details are deleted");
   }
   else{
      System.out.println("Went Wrong!!");
   }
   }


   sql=String.format("DELETE from plans where oper_id= %s",ope_id);
   result=updateSqlStmt(stmt,sql); 
   if(result!=0)
   {
      System.out.println("plans are deleted");
   }
   else{
      System.out.println("Went Wrong!!");
   }

   

   sql=String.format("DELETE from telecom_operators where operator_id= %s",ope_id);
   result=updateSqlStmt(stmt,sql); 
   if(result!=0)
   {
      System.out.println("details in telecom_operators deleted\n");
   }
   else{
      System.out.println("Went Wrong!!");
   }
}

static void update_ssn_details(Statement stmt,Scanner input)
{
   System.out.println("1. Update pin code \n 2: Update DOB");
   String a=input.nextLine();
   if(a.equals("1"))
   {
      System.out.println("Enter Your ssn:");
      String ssn_id=input.nextLine();
      String name=find_ssn(stmt,input,ssn_id);
      if(name.equals(""))
      {
         System.out.println("SSN_Details not found\n Try again\n");
         return;
      }
      System.out.println("Hello!! "+name);
      System.out.println("Enter Your pin_code:");
      String pin_code=input.nextLine();
      if(pin_code.length()!=6)
      {
         System.out.println("Number should have only 6 digits");
         System.out.println("Enter your pin_code again:");
         pin_code=input.nextLine();  
      }
      String sql=String.format("UPDATE ssn  set pin_code='%s'  where ssn_id='%s';",pin_code,ssn_id);
      int result=updateSqlStmt(stmt,sql);
      if(result!=0)
      {
         System.out.println("Update was done");
      }
      else{
         System.out.println("update was Unsuccessfull,Try again ");
      }
      return;      
   }
   else if(a.equals("2"))
   {
      System.out.println("Enter Your ssn:");
      String ssn_id=input.nextLine();
      String name=find_ssn(stmt,input,ssn_id);
      if(name.equals(""))
      {
         System.out.println("SSN_Details not found\n Try again\n");
         return;
      }
      System.out.println("Hello!! "+name);
      System.out.println("Enter Your DOB: (yyyy-mm-dd");
      String DOB=input.nextLine();
      
      String sql=String.format("UPDATE ssn  set DOB='%s'  where ssn_id='%s';",DOB,ssn_id);
      int result=updateSqlStmt(stmt,sql);
      if(result!=0)
      {
         System.out.println("Update was done");
      }
      else{
         System.out.println("Recharge was Unsuccessfull,Try again ");
      }
      return;
   }
}
static void change_phone_nummber(Statement stmt,Scanner input)
{
   System.out.println("Enter Your Number:");
   String phone_num=input.nextLine();   
   if(phone_num.length()!=4)
   {
      System.out.println("Number should have only 4 digits");
      System.out.println("Enter your 4 digit number again:");
      phone_num=input.nextLine();  
   }


   String sql=String.format("SELECT phone_number,sim_id from sim_details");
   ResultSet rs=executeSqlStmt(stmt,sql);
   try{
      while(rs.next())
      {
         String num1=rs.getString("phone_number");
         String sim_id=rs.getString("sim_id");
         if(phone_num.equals(num1))
         {
            System.out.println("Enter your new number:");
            String new_num=input.nextLine();
            new_num=check_unique(stmt,new_num,input);
            sql=String.format("UPDATE sim_details  set phone_number='%s'  where sim_id='%s';",new_num,sim_id);
            int result=updateSqlStmt(stmt,sql);
            System.out.println("\n Yay!! Your mobile_number updated :) ");
            return ;
         } 
      }
      System.out.println("Phone number not found!! try again :)");
      return;
   }
   catch(SQLException e){
   }   
}


static void recharge(Statement stmt,Scanner input,Connection conn)
{
   System.out.println("Enter Your Number:");
   String phone_num=input.nextLine();
   if(phone_num.length()!=4)
   {
      System.out.println("Number should have only 4 digits");
      System.out.println("Enter your 4 digit number again:");
      phone_num=input.nextLine();  
   }
   ChecK_number(stmt,phone_num,input,conn);
   return;
}

static void ChecK_number(Statement stmt,String num,Scanner input,Connection conn)
{
   String sql=String.format("SELECT phone_number,ope_id,exp_date from sim_details");
   ResultSet rs=executeSqlStmt(stmt,sql);
   try{
      while(rs.next())
      {
         String num1=rs.getString("phone_number");
         String ope_id=rs.getString("ope_id");
         java.sql.Date date= rs.getDate("exp_date");
         //System.out.println(date);
         if(num.equals(num1))
         {
            
            recharge_next(stmt,date,ope_id,input,num,conn);
            return ;
         } 
      }
   }
   catch(SQLException e){
  }
 System.out.println("Number is Invalid");
 return ;
}

static void recharge_next(Statement stmt,java.sql.Date datebef,String ope_id,Scanner input,String num,Connection conn)
{
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   java.util.Date date = new java.util.Date();
   java.sql.Date today = new java.sql.Date(date.getTime());
   if(datebef.compareTo(today)>0)
   {
      System.out.println("No need to recharge as your expiry date is "+datebef+"\n If you want to continue press 1:");
      String a=input.nextLine();
      if(!a.equals("1"))
      {
         return;
      }
   }

   System.out.println("Select you plan and enter the id:");
   list_of_plans(stmt,ope_id);
   String aa=input.nextLine();
   int days=get_days(stmt,aa);
   today=sqlDatePlusDays(today,days);

   String sql=String.format("UPDATE sim_details  set exp_date='%s'  where PHONE_NUMBER='%s';",today,num);
   int result=updateSqlStmt(stmt,sql);
   if(result!=0)
   {
      System.out.println("Recharge was done and the expiry date is "+ today);
   }
   else{
      System.out.println("Recharge was Unsuccessfull,Try again ");
   }
   return;
}
static java.sql.Date sqlDatePlusDays(java.sql.Date date, int days) {
   return java.sql.Date.valueOf(date.toLocalDate().plusDays(days));
}
static String find_ssn(Statement stmt,Scanner input,String id)
{
   String sql=String.format("SELECT * from ssn");
  
   ResultSet rs=executeSqlStmt(stmt,sql);
   String name="";
   int flag=0;
   try{
      while(rs.next())
      {
         String id_e=rs.getString("ssn_id");
         
         name=rs.getString("fname");
         if(id.equals(id_e))
         {
            flag=1;
            return name;
         }
      }    
      
  }
  catch(SQLException e){
  }
  return "";
}

static int Check_no_of_sims(Statement stmt,String id){

   String sql=String.format("SELECT f_ssn_id,count(*) as 'count' from sim_and_cus group by f_ssn_id");
   ResultSet rs=executeSqlStmt(stmt,sql);
   try{
      while(rs.next())
      {
         String id_e=rs.getString("f_ssn_id");
         int co=rs.getInt("count");
         if(id.equals(id_e))
         {
            if(co>=2)
            {
               System.out.println("You already have 2 sim card.So,you cant have one more\n");
               return 0;
            }
            else{
               return 1;
            }
         }
      }    
  }
  catch(SQLException e){
  }   
  
  return 1;
}

static int Check_age(Statement stmt,String id){
   String sql=String.format("SELECT ssn_id, dob, TIMESTAMPDIFF(YEAR, dob, CURDATE()) AS age FROM ssn;");
   ResultSet rs=executeSqlStmt(stmt,sql);
   try{
      while(rs.next())
      {
         String id_e=rs.getString("ssn_id");
         int co=rs.getInt("age");
         if(id.equals(id_e))
         {
            if(co<18)
            {
               System.out.println("Your age is "+co+" it is  less than 18,So you can't be a owner\n");
               return 0;
            }
            else{
               return 1;
            }
         }
      }    
  }
  catch(SQLException e){
  }   
  
  return 0;
}

static void list_of_Network(Statement stmt){
  String sql=String.format("SELECT operator_id,Operator_name from telecom_operators");
  ResultSet rs=executeSqlStmt(stmt,sql);
  try{
     while(rs.next())
     {
        String id=rs.getString("Operator_id");
        String name=rs.getString("Operator_name");

        System.out.println(" "+id+"."+"  "+name);
     }
  }
  catch(SQLException e){

}
}
static int list_of_plans(Statement stmt,String ope_id){
   String sql=String.format("SELECT plan_id,plan_name,days from plans where oper_id="+ope_id);
   ResultSet rs=executeSqlStmt(stmt,sql);
   int count=0;
   try{
      while(rs.next())
      {
         String id=rs.getString("plan_id");
         String name=rs.getString("plan_name");
         String days=rs.getString("days");
         count++;
         System.out.println("plan_id "+id+".  "+"name: "+name+"days: "+days);
      }
     
   }
   catch(SQLException e){
 
 }
 return count;
 }
public static java.sql.Date convertToDateUsingDate(LocalDate date) {
   return java.sql.Date.valueOf(date);
}
static void new_sim(Statement stmt,Scanner input,Connection conn)
{
   try{
      System.out.print("Enter ssn: ");
      String id=input.nextLine();
      String name=find_ssn(stmt,input,id);
      if(name.equals(""))
      {
         System.out.println("SSN_details are not found\n");
         return;
      }
      System.out.println("Hello "+name+"!!");
      int flag=Check_no_of_sims(stmt,id);
      if(flag==0)
      {
         return;
      }
      flag=Check_age(stmt,id);
      if(flag==0)
      {
         return;
      }
      System.out.println("\nSelect a opertor and enter their id: ");
      list_of_Network(stmt);

      String ope_id=input.nextLine();      
      System.out.print("\nselect a plan_id: \n");                         
      int aqw=list_of_plans(stmt,ope_id);
      if(aqw==0){
         System.out.println("There are no plans in this Network");
         return ;
      }
      String plan_id=input.nextLine();
       
      int days=get_days(stmt,plan_id);
      String dateBefore = "2022-04-18"; 

      LocalDate date = LocalDate.parse("2022-04-18");
      LocalDate date2 = date.plusDays(days);   
      System.out.println("Enter a 4 digit number:");
      String phone_num=input.nextLine();
      if(phone_num.length()!=4)
      {
         System.out.println("Number should have only 4 digits");
         System.out.println("Enter a 4 digit number again:");
         phone_num=input.nextLine();  
      }
      phone_num=check_unique(stmt,phone_num,input);
      PreparedStatement myStmt = conn.prepareStatement("INSERT INTO sim_details(sim_id,ope_id,c_ssn,plann_id,phone_number,exp_date)VALUES(?,?,?,?,?,?)");
      myStmt.setInt(1,sim_id_c);
      myStmt.setString(2,ope_id);
      myStmt.setString(3,id);
      myStmt.setString(4,plan_id);
      myStmt.setString(5,phone_num);
      myStmt.setDate(6,convertToDateUsingDate(date2));
      myStmt.execute();

      String sql1=String.format("INSERT INTO sim_and_cus(simm_id,iid,f_ssn_id)VALUES('%d','%d','%s');",sim_id_c,sim_and_cus_id_c,id);
      int result1=updateSqlStmt(stmt,sql1);
      if( result1!=0)
      {
         System.out.println("Yay!! you got new_sim and its expiry date:"+date2);
         sim_id_c++;
         sim_and_cus_id_c++;
      }
      else{
         System.out.println("Try again !!");
      }
   }
   catch(Exception e){
      
   }  
   }
static String check_unique(Statement stmt,String num,Scanner input)
   {
      String sql=String.format("SELECT sim_id,phone_number from sim_details");
      ResultSet rs=executeSqlStmt(stmt,sql);
      try{
         while(rs.next())
         {
            String num1=rs.getString("phone_number");
            if(num.equals(num1))
            {
               System.out.println("This number is already taken. Enter another number:");
               num=input.nextLine();
            } 
         }
        
      }
      catch(SQLException e){
    
    } 
    return num  ;
   }
static int get_days(Statement stmt,String id)
{
   String sql=String.format("SELECT plan_id,days from plans");
   ResultSet rs=executeSqlStmt(stmt,sql);
   try{
      while(rs.next())
      {
         String id_e=rs.getString("plan_id");
         int days=rs.getInt("days");
         if(id.equals(id_e)){
            return days;
         }
       
      }
     
   }
   catch(SQLException e){
 
 }   
 return 0;
}
static void add_plan(Statement stmt,Scanner input)
{
   try{

      System.out.print("Plan_name: ");
      String name=input.nextLine();

      System.out.print("operator_id: ");
      String oper_id=input.nextLine();

      System.out.print("Days : ");
      String days=input.nextLine();
      
      String sql=String.format("INSERT INTO plans(plan_id,plan_name,oper_id,days)VALUES('%s','%s','%s','%s');",plan_id_c,name,oper_id,days);
      int result=updateSqlStmt(stmt,sql);      
 
      if(result!=0)
      {
         System.out.println("Plan added in the network !! and the plan id: "+plan_id_c);
         plan_id_c++;
      }
      else{
         System.out.println("Went Wrong!!");
      }
   }
   catch(Exception e){
      
   }       
}
static void add_network(Statement stmt,Scanner input)
{
   try{
      int id=oper_id_c;

      System.out.print("Network name: ");
      String name=input.nextLine();

      System.out.print("Owner SSN: ");
      String own_ssn=input.nextLine();
      String name1=find_ssn(stmt,input,own_ssn);
      if(name1.equals(""))
      {
         System.out.println("SSN_Details not found\n Try again\n");
         return;
      }
      System.out.println("Hello!! "+name1);
      int a=Check_age(stmt,own_ssn);
      if(a==0){
         System.out.println("You are less than 18");
         return;
      }
      String sql=String.format("INSERT INTO telecom_operators(operator_id,operator_name,owner_ssn)VALUES('%s','%s','%s');",id,name,own_ssn);
      int result=updateSqlStmt(stmt,sql);      

      if(result!=0)
      {
         System.out.println("Network added!! and the Network id:"+oper_id_c+"\n");
         oper_id_c++;
      }
      else{
         System.out.println("Went Wrong!!");
      }
   }
   catch(Exception e){
      
   }      
   
}
static void add_people(Statement stmt,Scanner input)
{
   try{

      System.out.print("name: ");
      String name=input.nextLine();
      
      System.out.print("Enter DOB(yyyy-mm-dd): ");
      String addre=input.nextLine();      

      String sql=String.format("INSERT INTO ssn(fname,DOB,ssn_id)VALUES('%s','%s','%s');",name,addre,ssn_id_c);
      int result=updateSqlStmt(stmt,sql);

      if(result!=0)
      {
         System.out.println("Its Done!! and yours SSN:"+ssn_id_c+"\n");
         ssn_id_c++;
      }
      else{
         System.out.println("Went Wrong!!");
      }
   }
   catch(Exception e){
      
   }
}
static int updateSqlStmt(Statement stmt, String sql) {
   try {
      int rs = stmt.executeUpdate(sql);
      return rs;
   } catch (SQLException se) {
       //se.printStackTrace();
   } catch (Exception e) {
      //e.printStackTrace();
   }
   return 0;
}

static ResultSet executeSqlStmt(Statement stmt, String sql) {
   try {
      
      ResultSet rs = stmt.executeQuery(sql);
      return rs;
   } catch (SQLException se) {
    se.printStackTrace();
   } catch (Exception e) {
       e.printStackTrace();
   }
   return null;
}
static void clearScreen() {
   System.out.println("\033[H\033[J");
   System.out.flush();
}
}