import java.sql.*;
import java.util.Scanner;
import java.util.SimpleTimeZone;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection con,Scanner scanner) {
        this.connection = con;
        this.scanner = scanner;
    }
    public long open_account(String email){
        if(!account_exist(email)){
            String  open_account_query="INSERT INTO ACCOUNTS(account_number,full_name,email,balance,security_pin) values(?,?,?,?,?)";
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name= scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            Double balance= scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin= scanner.nextLine();
            System.out.println();
            try {
                long account_number=generateAccountNumber();
                PreparedStatement preparedStatement =connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);

                int rowaffect=preparedStatement.executeUpdate();
                if(rowaffect>0){
                    return account_number;
                }
                else{
                    throw new RuntimeException("Account Creation Failed !!");
                }

           }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        throw new RuntimeException("Account Already Exist");
    }

    public long getAccount_number(String email){
        try{
            String query="Select account_number from accounts where email = ?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet res=preparedStatement.executeQuery();
            if(res.next()){
                return res.getLong("account_number");
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Account Number Doesn`t Exist!");
    }

 public long generateAccountNumber(){
        try {

            Statement statement=connection.createStatement();
            String query="Select account_number from accounts ORDER BY account_number DESC LIMIT 1";
            ResultSet rs=statement.executeQuery(query);

            if(rs.next()){
                long last_account_number=rs.getLong("account_number");
                return  last_account_number+1;

            }
            else{
                return 1000100;
            }


        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 1000100;
 }

    public boolean account_exist(String email){
        String query="Select account_number from Accounts where email= ?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet res=preparedStatement.executeQuery();

            if(res.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return  false;
    }
}
