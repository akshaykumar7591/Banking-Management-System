import javax.xml.transform.Result;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection con,Scanner scanner) {
        this.connection = con;
        this.scanner = scanner;
    }

    public void credit_money(long account_number)throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Ammout: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin : ");
        String security_pin=scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                String query="Select *from accounts where account_number= ? and security_pin=?";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);

                ResultSet rs=preparedStatement.executeQuery();
                if(rs.next()){

                        String credit_query="Update Accounts Set balance=balance + ? where account_number=?";
                        PreparedStatement preparedStatement1=connection.prepareStatement(credit_query);

                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);


                        int rowAffect=preparedStatement1.executeUpdate();
                        if(rowAffect>0){
                            System.out.println("Rs." + amount +"credited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction Failed !!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }


                }
                else{
                    System.out.println("Invalid Pin !");
                }


            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);

    }

    public void debit_money(long account_number)throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Ammout: ");
        double amount=scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin : ");
        String security_pin=scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                String query="Select *from accounts where account_number= ? and security_pin=?";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);

                ResultSet rs=preparedStatement.executeQuery();
                if(rs.next()){
                    double current_balnce=rs.getDouble("balance");
                    if(current_balnce>=amount){
                        String credit_query="Update Accounts Set balance=balance - ? where account_number=?";
                        PreparedStatement preparedStatement1=connection.prepareStatement(credit_query);

                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);


                        int rowAffect=preparedStatement1.executeUpdate();
                        if(rowAffect>0){
                            System.out.println("Rs." + amount +"debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction Failed !!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance");
                    }

                }
                else{
                    System.out.println("Invalid Pin !");
                }


            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
  connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number)throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Reciver Account Number: ");
        long reciver_account_number= scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount= scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin=scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && reciver_account_number!=0){
                String  query="Select * from accounts where account_number=? and security_pin=?";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);

                ResultSet rs=preparedStatement.executeQuery();
                if(rs.next()){
                    double current_balance=rs.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query="Update accounts set balance=balance -? where account_number= ?";
                        String credit_query="Update accounts set balance=balance +? where account_number= ?";
                        PreparedStatement credit=connection.prepareStatement(credit_query);
                        PreparedStatement debit=connection.prepareStatement(debit_query);
                        credit.setDouble(1,amount);
                        credit.setLong(2,reciver_account_number);
                        debit.setDouble(1,amount);
                        debit.setLong(2,sender_account_number);
                        int rowaffect =credit.executeUpdate();
                        int rowaffect1=debit.executeUpdate();

                        if(rowaffect1>0 && rowaffect>0){
                            System.out.println("Transaction Successful !");
                            System.out.println("Rs." +amount+" Transfered successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return ;
                        }
                        else{
                            System.out.println("Transaction Failed !!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else{
                        System.out.println("Insufficient Balance! ");
                    }
                }
                else{
                    System.out.println("Invalid security Pin! ");

                }
            }
            else{
                System.out.println("Invalid account number");
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void getBalance(long account_numbet){
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin= scanner.nextLine();
        try{
            String query="Seelct balance from account where account_number=? and security_pin=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setLong(1,account_numbet);
            preparedStatement.setString(2,security_pin);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next()){
                double balance=rs.getDouble("balance");
                System.out.println("Balance" +balance);
            }
            else{
                System.out.println("Invalid Pin !!");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


}
