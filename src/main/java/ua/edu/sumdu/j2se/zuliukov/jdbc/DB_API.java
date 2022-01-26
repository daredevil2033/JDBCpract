package ua.edu.sumdu.j2se.zuliukov.jdbc;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.sql.*;
import java.util.Iterator;
import java.util.Objects;

public class DB_API {
    private Connection connection;

    public DB_API(String name) {
        try {
            SAXReader reader = new SAXReader();
            reader.setValidation(true);
            Document document = reader.read(new File("dataSource.xml"));
            for (Iterator<Element> it = document.getRootElement().elementIterator(); it.hasNext(); ) {
                Element config = it.next();
                if (Objects.equals(config.selectSingleNode("source-name").getText(), name)) {
                    initDB(config);
                    break;
                }
            }
        } catch (SQLException | DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initDB(Element config) throws ClassNotFoundException, SQLException {
        Class.forName(config.selectSingleNode("driver-class").getText());
        connection = DriverManager.getConnection(
                config.selectSingleNode("connection-url").getText(),
                config.selectSingleNode("user-name").getText(),
                config.selectSingleNode("password").getText());
    }

    public ResultSet getAll() throws SQLException {
        return connection.createStatement().executeQuery("select e.*, loc, dname, grade from emp e join dept d on e.deptno = d.deptno join salgrade on sal between minsal and hisal");
    }

    public ResultSet getByID(int empno) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select e.*, loc, dname, grade from emp e join dept d on e.deptno = d.deptno join salgrade on sal between minsal and hisal where empno=?");
        preparedStatement.setInt(1, empno);
        return preparedStatement.executeQuery();
    }

    public int create(int empno, String ename, String job, int mgr, Date hiredate, int sal, int comm, int deptno) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into emp values (?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(1,empno);
        preparedStatement.setString(2,ename);
        preparedStatement.setString(3,job);
        preparedStatement.setInt(4,mgr);
        preparedStatement.setDate(5,hiredate);
        preparedStatement.setInt(6,sal);
        preparedStatement.setInt(7,comm);
        preparedStatement.setInt(8,deptno);
        return preparedStatement.executeUpdate();
    }

    public int deleteByID(int empno) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from emp where empno=?");
        preparedStatement.setInt(1,empno);
        return preparedStatement.executeUpdate();
    }
}
