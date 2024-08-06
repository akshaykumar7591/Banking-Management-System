import  java.sql.*;
import java.util.*;
public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection con,Scanner scanner){
        this.connection=con;
        this.scanner=scanner;
    }
    public void register(){
        scanner.nextLine();
        System.out.print("Full Name : ");
        String full_name=scanner.nextLine();
        System.out.print("Email : ");
        String email=scanner.nextLine();
        System.out.print("Password : ");
        String password=scanner.nextLine();

        if(user_exist(email)){
            System.out.println("User Already Exists for this Email Address !!");
            return ;
        }
        String register_query="INSERT INTO  USER(full_name,email,password) Values(?,?,?)";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            int rowaffects=preparedStatement.executeUpdate();

            if(rowaffects>0){
                System.out.println("Registration Successfully !");
            }
            else{
                System.out.println("Registration Failed !!!");
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }
    public String login(){
        scanner.nextLine();
        System.out.print("Email : ");
        String email=scanner.nextLine();
        System.out.print("Password : ");
        String password=scanner.nextLine();


        String login_query="Select * From User where email= ? AND password= ? ";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next()){
                return email;

            }
            else {
                return null;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    return null;

    }
    public boolean user_exist(String email){
        String login_query="Select * From User where email= ? ";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);

            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next()){
                return true;

            }
            else {
                return false;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }


        return false;
    }
}
