package ua.edu.sumdu.j2se.zuliukov.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static DB_API oracleDB = new DB_API("OracleDB");
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true){
            try {
                System.out.println("Options:");
                System.out.println("0 - exit");
                System.out.println("1 - select by empno");
                System.out.println("2 - create");
                System.out.println("3 - delete by empno");
                switch (sc.nextLine()) {
                    case "0":
                        System.out.println("Exiting...");
                        return;
                    case "1":
                        System.out.println("Enter EMPNO");
                        printResultSet(oracleDB.getByID(Integer.parseInt(sc.nextLine())));
                        break;
                    case "2":
                        System.out.println(createEmployee()+" employee(s) was(were) created");
                        break;
                    case "3":
                        printResultSet(oracleDB.getAll());
                        System.out.println("Enter EMPNO");
                        System.out.println(oracleDB.deleteByID(Integer.parseInt(sc.nextLine()))+" employee(s) was(were) deleted");
                        break;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            catch (IllegalArgumentException e){
                System.err.println("Wrong input");
            }
        }
    }

    private static void printResultSet(ResultSet rs) throws SQLException {
        if(!rs.next()) System.err.println("No results");
        else{
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-11.11s", rsmd.getColumnName(i));
            }
            System.out.print("\n");
            do {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-11.11s", rs.getString(i));
                }
                System.out.print("\n");
            } while (rs.next());
        }
    }

    private static int createEmployee() throws SQLException {
        int empno, mgr, sal, comm, deptno;
        String ename, job;
        Date hiredate;
        System.out.println("Enter EMPNO");
        empno = Integer.parseInt(sc.nextLine());
        System.out.println("Enter ENAME");
        ename = sc.nextLine();
        System.out.println("Enter JOB");
        job = sc.nextLine();
        System.out.println("Enter MGR");
        mgr = Integer.parseInt(sc.nextLine());
        System.out.println("Enter HIREDATE(yyyy-mm-dd)");
        hiredate = Date.valueOf(sc.nextLine());
        System.out.println("Enter SAL");
        sal = Integer.parseInt(sc.nextLine());
        System.out.println("Enter COMM");
        comm = Integer.parseInt(sc.nextLine());
        System.out.println("Enter DEPTNO");
        deptno = Integer.parseInt(sc.nextLine());
        return oracleDB.create(empno,ename,job,mgr,hiredate,sal,comm,deptno);
    }
}
